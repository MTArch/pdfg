package com.mca.pdfgen.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleStepEventMetrics 
{
	private static final Logger logger = LoggerFactory.getLogger(SingleStepEventMetrics.class);
	
	final private int ownerId;
	private long endTime = 0L;
	private long eventCount = 0;
	private long startTime = 0L;
	private long totalTime = 0L;
	final private String recordId;
	private long minTotalTime = -1L;
	private long maxTotalTime = -1L;
	private int recordsProcessed = 0;
	private int logEveryNEvent = 1000;
    
    public SingleStepEventMetrics(final String recordId, final int ownerId, 
    		                      final int logEveryNEvent)
    {
    	this.ownerId = ownerId;
    	this.logEveryNEvent = logEveryNEvent;
    	this.recordId = recordId.trim().toUpperCase();
    }

    public void startCollection()
    {
    	endTime = 0L;
        startTime = System.currentTimeMillis();
    }
    
    public void endCollection(final int recordsProcessed)
    {
    	if (endTime > 0L)
    	{
    		// Multiple requests to end a collection event without corresponding start event are 
    		// ignored after the first endCollection() call
    		return;
    	}
    	endTime = System.currentTimeMillis();
        final long    elapsedTime = endTime - startTime;
        
        totalTime += elapsedTime;
        this.recordsProcessed += recordsProcessed;
        
        minTotalTime = (minTotalTime == -1L) ? elapsedTime : Math.min(elapsedTime, minTotalTime);
        maxTotalTime = (maxTotalTime == -1L) ? elapsedTime : Math.max(elapsedTime, maxTotalTime);
        
        eventCount++;
        
        if (eventCount >= logEveryNEvent)
        {
        	logEvent(true);
        }
    }
    
    public void logEvent(final boolean success)
    {
        if (eventCount > 0) 
        {
        	long avgTotalTime = totalTime / eventCount;
        	
        	// Logging format: \tSingleStep\tOwnerId\tRecordId\tEventCount\tAvgTime\tMinTime
        	//                 \tMaxTime\tRecordsProcessed 
        	if (success)
        	{
        		logger.info("SingleStepEvent\t{}\t{}\t{}\t{}\t{}\t{}\t{}", ownerId, recordId, 
        				  eventCount, avgTotalTime, minTotalTime, maxTotalTime, recordsProcessed);
        	}
        	else
        	{
        		logger.error("SingleStepEvent\t{}\t{}\t{}\t{}\t{}\t{}\t{}", ownerId, recordId, 
        				  eventCount, avgTotalTime, minTotalTime, maxTotalTime, recordsProcessed);
        	}
        }
    	// After event is logged, reset all values
        this.endTime = 0L;
        this.startTime = 0L;
    	this.totalTime = 0L;
    	this.eventCount = 0L;
    	this.maxTotalTime = -1L;
    	this.minTotalTime = -1L;
    	this.recordsProcessed = 0;
    }
    
    public static String getHeaderInfo()
    {
        return "Timestamp\tThreadId\tSuccess\\Error\tSingleStepEvent"
        		+ "\tBatchId\tEventType\tEventCount\tAvgTime\tMinTime\tMaxTime\tRecordsProcessed";
    }
}
