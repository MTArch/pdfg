package com.mca.pdfgen.beans;

import com.mca.pdfgen.PdfProcessor;

import lombok.Data;

@Data
public class WorkerArea 
{
    public static enum WorkerStatus {WORKER_IDLE, WORKER_TASK_ASSIGNED, WORKER_PROCESSING, 
                                     WORKER_COMPLETED, WORKER_EXIT};
    private long seq;
    private int workerId;
    private boolean exitFlag;
    private PdfProcessor myProcessor;
    private volatile WorkerStatus workerStatus;
}
