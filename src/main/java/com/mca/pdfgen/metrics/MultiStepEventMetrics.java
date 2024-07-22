package com.mca.pdfgen.metrics;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MultiStepEventMetrics 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiStepEventMetrics.class);
    
    private String recordId;
    private final int ownerId;

    private long totalEndTime;
    private long totalStartTime;
    private long totalElapsedTime;

    private long endTime[];
    private long startTime[];
    private long elapsedTime[];

    private final int totalSteps;
    
    public MultiStepEventMetrics(final int totalSteps, final int ownerId)
    {
    	this.ownerId = ownerId;
        this.totalSteps = totalSteps;

        endTime = new long[this.totalSteps];
        startTime = new long[this.totalSteps];
        elapsedTime = new long[this.totalSteps];
    }
    
    public void setRecordId(final String recordId)
    {
    	this.recordId = recordId;
    }

    public void startCollection()
    {
    	if (totalStartTime > 0)
    	{
    		// Multiple requests to start a collection event are ignored after the first call
    		return;
    	}
        totalStartTime = System.currentTimeMillis();
    }
    
    public void endCollection()
    {
    	if (totalEndTime > 0)
    	{
    		// Multiple requests to end a collection event are ignored after the first call
    		return;
    	}
        totalEndTime = System.currentTimeMillis();
        totalElapsedTime = totalEndTime - totalStartTime;
    }
    
    public void startStep(final int stepId)
    {
    	if (stepId < 0 || stepId >= totalSteps)
    	{
    		LOGGER.warn("Invalid StepId = " + stepId + " specified, totalSteps Allowed = " + 
    	                totalSteps + ", in MetricEvent with RecordId = " + recordId);
    		return;
    	}
    	if (startTime[stepId] > 0)
    	{
    		// Multiple requests to start a step are ignored after the first call
    		return;
    	}
        startTime[stepId] = System.currentTimeMillis();
    }
    
    public void endStep(final int stepId)
    {
    	if (stepId < 0 || stepId >= totalSteps)
    	{
    		LOGGER.warn("Invalid StepId = " + stepId + " specified, totalSteps Allowed = " + 
	                totalSteps + ", in MetricEvent with RecordId = " + recordId);
    		return;
    	}
    	if (endTime[stepId] > 0)
    	{
    		// Multiple requests to end a step are ignored after the first call
    		return;
    	}
        endTime[stepId] = System.currentTimeMillis();
        elapsedTime[stepId] = endTime[stepId] - startTime[stepId];
    }
    
    public void logEvent(final boolean success)
    {
    	if (success)
    	{
             LOGGER.info("{}", getFormattedCurRec());
    	}
    	else
    	{
    		LOGGER.error("{}", getFormattedCurRec());
    	}
    }
    
    protected String getFormattedCurRec()
    {
        StringBuilder dataRow = new StringBuilder();
        
        dataRow.append("MultiStepEvent");
        dataRow.append("\t");
        dataRow.append(ownerId);
        dataRow.append("\t");
        dataRow.append(recordId);
        dataRow.append("\t");
        dataRow.append(totalElapsedTime);
        
        for (int index = 0; index < totalSteps; index++)
        {
            dataRow.append("\t");
            dataRow.append(elapsedTime[index]);
        }
        return dataRow.toString();
    }
}
