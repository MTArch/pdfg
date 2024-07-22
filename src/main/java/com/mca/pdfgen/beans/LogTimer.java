package com.mca.pdfgen.beans;

import lombok.Data;

@Data
public class LogTimer 
{
	private static final long DEFAULT_LOGGING_INTERVAL = 5000L;
	
    private long loggingInterval;
    private long lastLoggedTime;
    
    public LogTimer(final long interval)
    {
    	lastLoggedTime = 0;
    	loggingInterval = (interval > 0) ? interval : DEFAULT_LOGGING_INTERVAL;
    }
    
    public void resetLogTime()
    {
        lastLoggedTime = 0;
    }
}
