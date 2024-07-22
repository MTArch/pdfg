package com.mca.pdfgen;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mca.pdfgen.constants.AppConstants;

import jakarta.annotation.PostConstruct;
import lombok.Data;

@Data
@Component
public class BatchConfig 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchConfig.class);
	
    @Value("#{environment.PDFGENSHUTDOWNFILE}")
    private String shutdownFile;    
    
    @Value("#{environment.MCATHREADMAJORSLEEPTIME}")
    private int majorSleepTime;
    
    @Value("#{environment.MCATHREADMINORSLEEPTIME}")
    private int minorSleepTime;
    
    @Value("#{environment.BATCHRECORDSLOCKRESETTIME}")
    private int batchLockResetTime;

    @Value("${pdfgenbatch.batchId}")
    private int batchId;   

    @Value("${pdfgenbatch.batchType}")
    private int batchType;

    @Value("${pdfgenbatch.poolsize}")
    private int numThreads;
    
    @Value("${formpriority.min}")
    private int formPriorityMin;

    @Value("${formpriority.max}")
    private int formPriorityMax;
    
    @Value("${gencounter.min}")
    private int genCounterMin;

    @Value("${gencounter.max}")
    private int genCounterMax;
    
    @Value("${pdfgenbatch.retryDelay}")
    private int retryDelay;
    
    @Value("${pdfgenbatch.batchMode}")
    private String batchMode; 

    @Value("${pdfgenbatch.statusLogInterval}")
    private long statusLogInterval;
    
    @Value("${pdfgenbatch.metrics.lockRecord.logEveryNEvents}")
    private int lockRecordLogEveryNEvents;
    
    @Value("${pdfgenbatch.metrics.unlockRecord.logEveryNEvents}")
    private int unlockRecordLogEveryNEvents;

    @Value("${pdfgenbatch.maxPollerFetchSize}")
    private int maxPollerFetchSize;

    @Value("${pdfgen.cxnErrLogIntervalms ?: 600000}")
    private long cxnErrLogIntervalInMillis;

    private boolean normalMode;
    private boolean retryMode;
    
    @PostConstruct
    public void initBatchConfig()
    {
    	batchMode = (batchMode == null) ? "" : StringUtils.trim(batchMode);
    	
        this.retryMode = (AppConstants.BATCH_MODE_RETRY.equalsIgnoreCase(batchMode));
        this.normalMode = (AppConstants.BATCH_MODE_NORMAL.equalsIgnoreCase(batchMode));
    }

    public void printConfigParams()
    {
        String    fomattedParams = this.toString().split("\\(")[1];
        
        fomattedParams = fomattedParams.split("\\)")[0];
        
        LOGGER.info("Startup config Params for Class: {} are: {}", this.getClass().getName(), 
        		    fomattedParams.split(","));
    }
    
    public boolean validateConfigParams()
    {
        LOGGER.info("Starting parameters configuration Validation for BatchConfig ...");
        
        if (AppConstants.BATCH_MODE_NORMAL.equalsIgnoreCase(this.batchMode) 
        	|| AppConstants.BATCH_MODE_RETRY.equalsIgnoreCase(this.batchMode))
        {
        	LOGGER.info("Parameter configuration validation of BatchConfig completed "
        			    + "successfully.");
        	return true;
        }
        LOGGER.error("Error validating parameters in Batch Config, invalid batchMode = {}",
        		     ((this.batchMode == null)? "" : this.batchMode));
        return false;
    }
}
