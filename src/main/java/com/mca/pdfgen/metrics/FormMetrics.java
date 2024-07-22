package com.mca.pdfgen.metrics;

public class FormMetrics extends MultiStepEventMetrics
{
    public static final int TOTAL_PDF_GEN_STEPS = 10;
    
    public static final int PRE_PROCESS_STEP = 0;
    public static final int LOAD_DBREC_STEP = 1;
    public static final int PARSE_JSON_STEP = 2;
    public static final int CREATE_PDF_STEP = 3;
    public static final int ADD_DSCBOXES_STEP = 4;
    public static final int GET_ATTACHMENTS_STEP = 5;
    public static final int ADD_ATTACHMENTS_STEP = 6;
    public static final int INSERT_IN_DMS_STEP = 7;
    public static final int ADD_DMSID_TO_DB_STEP = 8;
    public static final int UPDATE_DB_REC_STEP = 9;
    
    public FormMetrics(final int ownerId)
    {
        super(TOTAL_PDF_GEN_STEPS, ownerId);
    }
    
    public static String getHeaderInfo()
    {
        return "Timestamp\tThreadId\tSuccess\\Error\tMultiStepEvent\tBatchId\tFormType"
                + "\tSRN\tSEQ\tCreationTime\tGenCounter\tTotalTime\tLoadDBRec\tPreProcess"
                + "\tParseJSON\tCreatePDF\tAddDSCs\tGetAttachments\tAddAttachments\tAddPDFtoDMS"
                + "\tAddDMSIdtoDB\tUpdateDBRec";
    }
}
