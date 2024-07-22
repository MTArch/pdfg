package com.mca.pdfgen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mca.pdfgen.beans.WorkerArea;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.helpers.DBHelper;
import com.mca.pdfgen.utils.ApplicationContextUtil;
import com.mca.pdfgen.utils.CommonUtils;

public class BatchWorker 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchWorker.class);
    
    private DBHelper dbHelper;
    private BatchConfig batchConfig;
    private SysParamsConfig sysParamsConfig;

    private int myWorkerId;
    private WorkerArea workerArea;
    private PdfProcessor pdfProcessor;
    
    public BatchWorker(final WorkerArea workArea, final int workerId)
    {
        myWorkerId = workerId;
        workerArea = workArea;

        dbHelper = ApplicationContextUtil.getBean(DBHelper.class);
        batchConfig = ApplicationContextUtil.getBean(BatchConfig.class);
        sysParamsConfig = ApplicationContextUtil.getBean(SysParamsConfig.class);

        pdfProcessor = new PdfProcessor(batchConfig, sysParamsConfig, dbHelper);

        workerArea.setExitFlag(false);
        workerArea.setMyProcessor(pdfProcessor);
        workerArea.setWorkerStatus(WorkerArea.WorkerStatus.WORKER_IDLE);

        LOGGER.info("Worker# " + myWorkerId + " initialization completed successfully.");
    }
    
    public void executeWorkerRoutine()
    {
        LOGGER.info("Worker# {} Starting execution of infinite loop ...", myWorkerId);
        
        workerArea.setExitFlag(false);

        while (true)
        {
            //TODO : Siva and Vindhya - adding a catch-all, since we saw worker crash in html 
            //form processing
            try
            {
                final int     opsResult = isOkToProcessRecord();
                
                if (opsResult < 0)
                {
                    LOGGER.info("Worker# {} Exiting Processing Loop ...", myWorkerId);
                    break;
                }
                if (opsResult > 0)
                {
                    // Record processing is to be skipped
                    continue;
                }
                workerArea.setWorkerStatus(WorkerArea.WorkerStatus.WORKER_PROCESSING);
                
                final long    seqNo = workerArea.getSeq();
    
                LOGGER.info("Worker# {} received Task SEQ = {}", myWorkerId, seqNo);
                
                // Now process the PDF generation request
                pdfProcessor.generatePDF(seqNo);
    
                LOGGER.debug("Worker# {} completed Task SEQ = {}", myWorkerId, seqNo);
            }
            catch (Throwable thrown)
            {
                LOGGER.error("Exception Caught in Batch Worker: {} ",thrown);
            }
            
            workerArea.setWorkerStatus(WorkerArea.WorkerStatus.WORKER_COMPLETED);
        }
        workerArea.setWorkerStatus(WorkerArea.WorkerStatus.WORKER_IDLE);
        
        LOGGER.info("Worker# {} exited infinite processing loop", myWorkerId);
    }
    
    private int isOkToProcessRecord()
    {
        if (workerArea.isExitFlag())
        {
            return -1;
        }
        //  If there no task currently assigned, sleep for a small interval.            
        if (workerArea.getWorkerStatus() == WorkerArea.WorkerStatus.WORKER_IDLE 
            || workerArea.getWorkerStatus() == WorkerArea.WorkerStatus.WORKER_COMPLETED)
        {
            CommonUtils.yieldToOtherThreads(batchConfig.getMinorSleepTime());
            return 1;
        }
        // If task is not in assigned status, nothing needs to be done, mark task completed
        if (workerArea.getWorkerStatus() != WorkerArea.WorkerStatus.WORKER_TASK_ASSIGNED)
        {
            workerArea.setWorkerStatus(WorkerArea.WorkerStatus.WORKER_COMPLETED);
            return 1;
        }
        return 0;
    }
}
