package com.mca.pdfgen.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mca.pdfgen.constants.AppConstants;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Component
public class DBHelperConfig 
{
    @Value("${pdfgenbatch.batchId}")
    private int batchId;   

    @Value("${pdfgenbatch.batchType}")
    private int batchType;

    @Value("${pdfgenbatch.batchMode}")
    private String batchMode;

    private boolean normalMode;
    
    private boolean retryMode;

    private int procStatusMin;
    
    private int procStatusMax;
    
    @Value("${gencounter.min}")
    private int minRetries;

    @Value("${gencounter.max}")
    private int maxRetries;
    
    @Value("${formpriority.min}")
    private int formPriorityMin;

    @Value("${formpriority.max}")
    private int formPriorityMax;

    @Value("${pdfgenbatch.retryDelay}")
    private int retryDelay;
    
    @PostConstruct
    public void initDBHelperRequest()
    {
        this.retryMode = (AppConstants.BATCH_MODE_RETRY.equalsIgnoreCase(batchMode));
        this.normalMode = (AppConstants.BATCH_MODE_NORMAL.equalsIgnoreCase(batchMode));
    }
    
    public void printConfigParams()
    {
        log.info("DBHelperConfig Params {}", this.toString());
    }
}
