package com.mca.pdfgen.beans.db;

public interface LockResult 
{
    Long getSeqNo();
    Integer getBatchId();
    String getLockFlag();
}
