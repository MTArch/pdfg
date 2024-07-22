package com.mca.pdfgen.helpers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mca.pdfgen.BatchConfig;
import com.mca.pdfgen.beans.db.FormBatchMaster;
import com.mca.pdfgen.beans.db.LockResult;
import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.configs.DBHelperConfig;
import com.mca.pdfgen.constants.ProcessingStatus;
import com.mca.pdfgen.dataaccess.FormsBatchMasterDAO;
import com.mca.pdfgen.dataaccess.PDFGenRequestRecDAO;
import com.mca.pdfgen.exception.PDFGenException;
import com.mca.pdfgen.metrics.SingleStepEventMetrics;

@Component
@Transactional
public class DBHelper
{
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
    @Autowired 
    BatchConfig batchConfig;
    
    @Autowired
    PDFGenRequestRecDAO pdfgenDAO;
    
    @Autowired
    FormsBatchMasterDAO formsDAO;
    
    private long recordsResetTime = 0L;
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void resetBatchRecords(final SingleStepEventMetrics metric, 
                                  final SingleStepEventMetrics metricsWithNoRecord)
    {
    	int          nrecords = 0;
    	final long   currTime = System.currentTimeMillis();

        if ((currTime - recordsResetTime) < batchConfig.getBatchLockResetTime())
        {
            return;
        }
        metric.startCollection();
        metricsWithNoRecord.startCollection();

        try 
        {
        	recordsResetTime = System.currentTimeMillis();
        	nrecords = pdfgenDAO.resetLockedRecords(batchConfig.getBatchId());

        	if (nrecords > 0)
            {
        		metric.endCollection(nrecords);
        		metric.logEvent(true);
            }
            else
            {
                metricsWithNoRecord.endCollection(0);
                metricsWithNoRecord.logEvent(true);
            }
        }
        catch(final Exception currEx)
        {
            LOGGER.error("Exception in Reset Locked records", currEx);           
        }
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<LockResult> getSeqListWithRanges(final DBHelperConfig request, 
    		                                     final int recCount) 
                                                 throws PDFGenException 
    {
    	List<LockResult>    result = null;
        final int           numRecords = (recCount <= 0) ? 1 : recCount;

        if (request == null) 
        {
            LOGGER.error("Invalid DBHelperConfig received");
            return result;
        }
        LOGGER.debug("Retrieving lock list with input request = {}, recCount = {}", 
                      request, recCount);
        
        if (request.isNormalMode())
        {
            result = pdfgenDAO.findSeqList_Normal(request.getBatchType(), 
                                                  ProcessingStatus.PROCESSING_PENDING, 
                                                  request.getMinRetries(), 
                                                  request.getFormPriorityMin(),
                                                  request.getFormPriorityMax(),
                                                  numRecords);
        }
        else if (request.isRetryMode())
        {
            result = pdfgenDAO.findSeqList_Retry(request.getBatchType(), 
                                                 ProcessingStatus.PROCESSING_PENDING, 
                                                 ProcessingStatus.PDF_GEN_COMPLETE,
                                                 request.getMinRetries(), 
                                                 request.getMaxRetries(),
                                                 request.getFormPriorityMin(), 
                                                 request.getFormPriorityMax(), 
                                                 request.getRetryDelay(), 
                                                 numRecords);
        }
        if (result != null && result.size() > 0)
        {
            LOGGER.debug("Locked Results list = {}", result);
            
            final List<Long> updateList = new ArrayList<Long>();
            
            for (final LockResult lockResult : result) 
            {
                updateList.add(lockResult.getSeqNo());
            }
            if (pdfgenDAO.lockRecords(batchConfig.getBatchId(), updateList) != updateList.size())
            {
                LOGGER.error("Failed to update all records in {}", updateList);

                throw new PDFGenException("Failed to lock all records in getSeqListWithRanges");
            }
        }
        else
        {
            LOGGER.debug("No locked results were found");
        }
        return result;
    }
    
    @Transactional(propagation = Propagation.REQUIRED)
    public PDFGenRequestRec getRecordBySeq(final long SEQ)
    {
        PDFGenRequestRec    result = null;
        
    	try
    	{
	        result = pdfgenDAO.findBySeqNo(SEQ);
    	}
    	catch(final Exception currEx)
    	{
    		LOGGER.error("Failed in getRecordBySeq() for SEQ = {} with Error: \n{}", 
    				     SEQ, currEx);
    	}
	    return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean updateRequestRec(final PDFGenRequestRec requestRec)
    {
        boolean    result = true;

        LOGGER.debug("Updating Record for seq = {} and genCounter = {}.", requestRec.getSeqNo(),
        			  requestRec.getRetriesCount());

        pdfgenDAO.save(requestRec);

        LOGGER.debug("Updated Record for seq = {} and genCounter = {} successfully.", 
        		     requestRec.getSeqNo(), requestRec.getRetriesCount());
        return result;
    }    

    /**
     * Method to get the forms Master record for a given form name
     * @param name
     * @return FormBatchMaster
     */
    public FormBatchMaster getFormMasterByName(final String name)
    {
        return formsDAO.findByFormName(name);
    }
    
    public boolean checkDBConnection()
    {
        boolean result = false;
        String dbDate = null;
        try
        {
            dbDate = pdfgenDAO.getDBSysDate();
            if (dbDate != null && !dbDate.equals(""))
            {
                result = true;
            }
        }
        catch(Exception ex)
        {
            // Ignore exceptions
        }
        return result;
    }
    
    public void cleanUp()
    {
        // Nothing to clean up for now
    }
}
