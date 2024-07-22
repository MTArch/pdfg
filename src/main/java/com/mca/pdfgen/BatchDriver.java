package com.mca.pdfgen;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.mca.pdfgen.beans.LogTimer;
import com.mca.pdfgen.beans.WorkerArea;
import com.mca.pdfgen.beans.db.LockResult;
import com.mca.pdfgen.configs.DBHelperConfig;
import com.mca.pdfgen.configs.FormsConfigManager;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.helpers.DBHelper;
import com.mca.pdfgen.metrics.FormMetrics;
import com.mca.pdfgen.metrics.SingleStepEventMetrics;
import com.mca.pdfgen.utils.CommonUtils;

@Component
public class BatchDriver
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchDriver.class);
    private static final Logger METRICSLOGGER = LoggerFactory.getLogger(
                                                              SingleStepEventMetrics.class);
    private int batchId;
    private WorkerArea[] workerAreas;
    private ThreadPoolExecutor pooledExecutor;

    @Autowired
    private DBHelper dbHelper;
    
    @Autowired 
    private BatchConfig batchConfig;
    
    @Autowired
    private SysParamsConfig sysParamsConfig;
    
    @Autowired
    private DBHelperConfig dbReqHelper;
    
    @EventListener({ApplicationStartedEvent.class})
    public void executeBatchProcessor()
    {
        try 
        {
            LOGGER.info("Calling Batch Processor Init");
            
            if (!this.initBatchProcessor())
            {
                LOGGER.error("Batch Processor Initialization failed.");
                return;
            }
            if (!startWorkerThreads())
            {
                LOGGER.error("Starting Worker Threads failed.");
                return;
            }
            if (!executeBatchDriver())
            {
                LOGGER.error("Batch Driver execution failed.");
                return;
            }
            LOGGER.info("Batch Processor exiting now");
            this.exitBatchProcessor();
        }           
        catch (final Exception currEx)
        {
            LOGGER.error("Encountered an exception in executeBatchProcessor(): \n{}", currEx);
            System.exit(1);
        }
    }

    private boolean initBatchProcessor()
    {
        LOGGER.info("Batch Processor Init started ...");
        
        printStartupConfigParams();
        
        if (!validateConfigs())
        {
            LOGGER.error("Error Validating Paths in configuration. Startup Failed");
            return false;
        }
        this.batchId = this.batchConfig.getBatchId();
        
        LOGGER.info("Configured no. of worker threads : {}", this.batchConfig.getNumThreads());

        this.workerAreas = new WorkerArea[this.batchConfig.getNumThreads()];

        for (int index = 0; index < this.workerAreas.length; index++) 
        {
            this.workerAreas[index] = new WorkerArea();         
        }
        LOGGER.info("Batch Processor work areas created successfully");
        
        this.pooledExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(
                                                                    this.workerAreas.length);
        
        LOGGER.info("Batch Processor Thread pool initialization completed successfully");
        LOGGER.info("Shutdown file path is : {}", batchConfig.getShutdownFile());
        
        final FormsConfigManager    formsCfgMgr = FormsConfigManager.getInstance();
        
        if (formsCfgMgr == null)
        {
            LOGGER.error("Forms configuration Initialization failed. Exiting Batch Driver");
            return false;
        }
        try 
        {
            formsCfgMgr.initFormsConfigurations();
            
            LOGGER.info("Forms configuration initialized. Batch id for this batch = {}", batchId);
        }
        catch(final Exception currEx)
        {
            LOGGER.error("Forms configuration Initialization failed. Exiting Batch Driver ...", 
                          currEx);
            return false;
        }
        // Print column headers information once at startup for all header columns used in 
        // different metrics events
        METRICSLOGGER.info("Headers for columns used in metric events logged are: \n{}\n{}", 
                           SingleStepEventMetrics.getHeaderInfo(), FormMetrics.getHeaderInfo());
        return true;
    }
    
    private boolean executeBatchDriver()
    {
        LOGGER.info("BatchDriver open for business and starting main processing loop ...");
        
        // Drive the execution which is an infinite loop for the driver till it needs to exit
        driveExecution();
        
        // Driver is ready to exit, prepare for exit
        prepareForExecutionEnd();
        
        LOGGER.info("Exiting Main Event Loop ...");
        
        return true;
    }
    
    private void exitBatchProcessor()
    {
        LOGGER.info("Initiating Batch Processor shutdown ...");
        
        pooledExecutor.shutdown();
        
        for (WorkerArea workerArea : workerAreas) 
        {
            if (workerArea.getMyProcessor() != null)
            {
                workerArea.getMyProcessor().cleanUp();
            }
        }
        LOGGER.info("Batch Processor shutdown completed successfully.");
    }

    private void driveExecution() 
    {
        final LogTimer    logtime = new LogTimer(batchConfig.getStatusLogInterval());
        final LogTimer    errorLogTime = new LogTimer(batchConfig.getCxnErrLogIntervalInMillis()); 
        
        final SingleStepEventMetrics    metric = new SingleStepEventMetrics(
                                                     "LockDBRecords", batchConfig.getBatchId(),
                                                     batchConfig.getLockRecordLogEveryNEvents());
        final SingleStepEventMetrics    metricsWithNoRecord = new SingleStepEventMetrics(
                                                     "LockDBRecords", batchConfig.getBatchId(),
                                                     batchConfig.getLockRecordLogEveryNEvents());
        final SingleStepEventMetrics    resetMetric = new SingleStepEventMetrics(
                                                     "UnLockDBRecords", batchConfig.getBatchId(), 
                                                     batchConfig.getUnlockRecordLogEveryNEvents());
        final SingleStepEventMetrics    resetMetricsWithNoRecord = new SingleStepEventMetrics(
                                                     "UnLockDBRecords", batchConfig.getBatchId(),
                                                     batchConfig.getUnlockRecordLogEveryNEvents());
        while (true)
        {
            List<LockResult>    seqList = null;
            int                 numRequests = 0;
            
            try 
            {
                // Perform idle processing
                final List<WorkerArea>    idleWorkers = doIdleProcessing(errorLogTime, 
                                                       resetMetric, resetMetricsWithNoRecord);
                
                final int    recordsToFetch = Math.min(idleWorkers.size(), 
                                                       batchConfig.getMaxPollerFetchSize()); 
                if (recordsToFetch > 0)
                {
                    metric.startCollection();
                    metricsWithNoRecord.startCollection();

                    try 
                    {
                        seqList = dbHelper.getSeqListWithRanges(dbReqHelper, recordsToFetch);
                        
                        // A DB operation was completed successfully, reset the errorLogTime
                        // so that we log an error only if it keeps coming in consecutive calls 
                        errorLogTime.resetLogTime();
                    }
                    catch (final Exception currEx)
                    {
                        CommonUtils.timedLog(errorLogTime, LOGGER, true, 
                                    "Exception encountered in retrieving SEQ List from DB: {}",
                                    currEx);
                    }
                    numRequests = (seqList == null) ? 0 : seqList.size();

                    if (numRequests > 0)
                    {
                        metric.endCollection(numRequests);
                    }
                    else
                    {
                        metricsWithNoRecord.endCollection(numRequests);
                    }
                }
                if (numRequests > 0) 
                {
                    metricsWithNoRecord.logEvent(true);
                    metric.logEvent(true);
                }
                CommonUtils.timedLog(logtime, LOGGER, false, "#Workers Free = {}, #Requests "
                                     + "received from DB = {}", idleWorkers.size(), numRequests);

                for (int index = 0; index < numRequests; index++)
                {
                    final WorkerArea workArea = (WorkerArea) idleWorkers.get(index);

                    workArea.setSeq(seqList.get(index).getSeqNo());
                    workArea.setWorkerStatus(WorkerArea.WorkerStatus.WORKER_TASK_ASSIGNED);
                }
                if (checkForExit())
                {
                    break;
                }
            }
            catch(final Exception currEx)
            {
                LOGGER.error("Exception Occurred in executeBatchDriver() with Error: \n{}", 
                             currEx);
                LOGGER.error("Going to begining of proessing loop");
            }
        }
        // Write to log any accumulated metrics for the case where no request records were found
        metricsWithNoRecord.logEvent(true);
    }
    
    private void prepareForExecutionEnd()
    {
        LOGGER.info("BatchDriver exited Main Event Loop. Waiting for threads to "
                + "finish processing ...");
    
        for (WorkerArea workerArea : workerAreas) 
        {
            workerArea.setExitFlag(true);
        }
        while (true)
        {
            final List<WorkerArea>    idleworkers = getIdleWorkers();
            
            if (idleworkers.size() == batchConfig.getNumThreads())
            {
                break;
            }
            try 
            {
                LOGGER.info("Batch driver waiting for {} threads to complete",
                            batchConfig.getNumThreads() - idleworkers.size());
                
                Thread.sleep(batchConfig.getMajorSleepTime());
            }
            catch (final Exception currEx)
            {
                LOGGER.info("Batch Driver interrupted while waiting for threads to complete");
                break;
            }
        }
    }

    private List<WorkerArea> doIdleProcessing(final LogTimer errorLogTime,
                                              final SingleStepEventMetrics metric, 
                                              final SingleStepEventMetrics noRecMetric)
    {
        final List<WorkerArea>    result = getIdleWorkers();
        
        if (result.size() == batchConfig.getNumThreads())
        {
            dbHelper.resetBatchRecords(metric, noRecMetric);
            CommonUtils.yieldToOtherThreads(batchConfig.getMajorSleepTime());
        }
        return result;
    }
    
    private boolean startWorkerThreads()
    {
        LOGGER.info("Starting Worker Threads ...");
        
        try
        {
            for (int index = 0; index < workerAreas.length; index++)
            {
                final BatchWorker    worker = new BatchWorker(workerAreas[index], index);
                pooledExecutor.submit(() -> { 
                                                worker.executeWorkerRoutine(); 
                                            }); 
            }
        }
        catch(final Exception currEx)
        {
            LOGGER.error("Error encountered during initialization of Worker Threads", currEx);
            return false;
        }
        LOGGER.info("Worker Threads started successfully.");

        return true;
    }

    private void printStartupConfigParams()
    {
        batchConfig.printConfigParams();
        
        sysParamsConfig.printConfigParams();
        
        dbReqHelper.printConfigParams();
    }
    
    private boolean validateConfigs()
    {
        return (batchConfig.validateConfigParams() 
                && sysParamsConfig.validateConfigParams());
    }

    private boolean checkForExit()
    {
        if (Files.exists(Paths.get(batchConfig.getShutdownFile())))
        {
            LOGGER.info("Shutdown file found, returning true from checkForExit()");
            
            return true;
        }
        return false;
    }
    
    private List<WorkerArea> getIdleWorkers()
    {
        List<WorkerArea>    result = null;
        
        result = Arrays.stream(this.workerAreas).filter(workerArea -> 
                     (workerArea.getWorkerStatus() == WorkerArea.WorkerStatus.WORKER_IDLE 
                      || workerArea.getWorkerStatus() == WorkerArea.WorkerStatus.WORKER_COMPLETED)
                     ).collect(Collectors.toList());
        
        return (result != null) ? result : new ArrayList<WorkerArea>();
    }
}
