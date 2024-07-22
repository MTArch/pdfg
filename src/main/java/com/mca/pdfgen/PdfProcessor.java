package com.mca.pdfgen;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.beans.dms.FileAttachmentDetailsBean;
import com.mca.pdfgen.beans.pdf.PdfContext;
import com.mca.pdfgen.beans.pdf.PdfMetaData;
import com.mca.pdfgen.configs.FormsConfig;
import com.mca.pdfgen.configs.FormsConfigManager;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.constants.ProcessingStatus;
import com.mca.pdfgen.exception.AttachmentSizeException;
import com.mca.pdfgen.exception.PDFGenException;
import com.mca.pdfgen.generators.PdfGeneratorContext;
import com.mca.pdfgen.generators.PdfOutputGenerator;
import com.mca.pdfgen.helpers.DBHelper;
import com.mca.pdfgen.helpers.DMSHelper;
import com.mca.pdfgen.helpers.PDFHelper;
import com.mca.pdfgen.helpers.WSO2Helper;
import com.mca.pdfgen.metrics.FormMetrics;
import com.mca.pdfgen.utils.CommonUtils;

import lombok.Data;

@Data
public class PdfProcessor 
{
    private static final int PDF_BUFFER_SIZE = 1024 * 5000;
    private static final Logger LOGGER = LoggerFactory.getLogger(PdfProcessor.class);
    
    private final DBHelper dbHelper;
    private final BatchConfig batchConfig;
    private final SysParamsConfig sysParamsConfig;

    private DMSHelper dmsHelper;
    private WSO2Helper wso2Helper;
    
    private final PdfContext pdfContext = new PdfContext();
    private final Map<String, PdfGeneratorContext> generatorsCtxMap = new HashMap<>();

    // This is for doing test runs where PDF will be generated and saved on the filesystem. Run 
    // this in single worker config with shutdown file created. No DMS / DB / WSO2 will be updated
    private boolean testRun = false;

    public PdfProcessor(final BatchConfig inBatchConfig, final SysParamsConfig inSysParamsConfig, 
                        final DBHelper inHelper)
    {
        dbHelper = inHelper;
        batchConfig = inBatchConfig;
        sysParamsConfig = inSysParamsConfig;

        testRun = sysParamsConfig.isTestRun();
        
        dmsHelper = new DMSHelper(sysParamsConfig, batchConfig.getCxnErrLogIntervalInMillis());
        wso2Helper = new WSO2Helper(sysParamsConfig, batchConfig.getCxnErrLogIntervalInMillis());
        
        pdfContext.setRetryMode(batchConfig.isRetryMode());

        resetContext();
    }

    public void resetContext()
    {
        pdfContext.setGenerator(null);
        pdfContext.setPdfGenReq(null);
        pdfContext.setFormConfig(null);
        pdfContext.setMetricsRec(null);
        pdfContext.setFormDataStr(null);
        pdfContext.setMetaDataStr(null);
        pdfContext.setPdfWithDSCsOutStream(null);
        pdfContext.setWso2InputReqFileContents(null);
        pdfContext.setGeneratedPdfFileContents(null);
        pdfContext.setFormattedDataJson(new JSONObject());
    }
    
    public void cleanUp()
    {
        if (dbHelper != null)
        {
            dbHelper.cleanUp();
        }
        if (dmsHelper != null)
        {
            dmsHelper.cleanUp();
            dmsHelper = null;
        }
        if (wso2Helper != null)
        {
            wso2Helper.cleanUp();
            wso2Helper = null;
        }
        final Map<String, PdfGeneratorContext> generatorsCtxMap = new HashMap<>();

        if (generatorsCtxMap != null)
        {
            final Set<String> keys = generatorsCtxMap.keySet();
            
            for (String currKey : keys) 
            {
                final PdfGeneratorContext currCtx = generatorsCtxMap.get(currKey);
                
                if (currCtx != null)
                {
                    currCtx.cleanUp();
                }
            }
        }
    }

    public void generatePDF(final long seqNo)
    {
        boolean              processingOk = true;
        boolean              updateRecord = false;
        final FormMetrics    metricEvent = new FormMetrics(batchConfig.getBatchId());
        
        resetContext();
        pdfContext.setMetricsRec(metricEvent);
        
        try 
        {
            metricEvent.startCollection();
            metricEvent.startStep(FormMetrics.LOAD_DBREC_STEP);
            
            LOGGER.info("PdfProcessor initiating processing for seqNo = {}", seqNo);
        
            // performs initialization of states and validations needed before processing a 
            // generate PDF request. Loads the generation request record from DB and sets it
            // up in workerArea for further use
            if (!readyToProcess(seqNo))
            {
                return;
            }
            // From here on we will update this record's processing status in DB, irrespective 
            // of whether we processed the record successfully or not
            updateRecord = true;

            // First step within the process is now complete
            metricEvent.endStep(FormMetrics.LOAD_DBREC_STEP);
            
            // Now process the PDF generation request
            processingOk = processPDFRequest();

            LOGGER.debug("processPDFRequest() completed with result = {} ", processingOk);
        }
        catch (final Exception currEx)
        {
            processingOk = false;
            
            LOGGER.error("Unexpected Exception while processing for seqNo = {}. processing "
                         + "failed, with Error: \n{}", seqNo, currEx);
        }
        finally
        {
            // Update Request record in DB as required
            updateRequestRecord(updateRecord);

            // Ending this metric step here will ensure time for this step is captured even 
            // if an error occurred. If endStep has already been invoked then this call will
            // have no effect on metrics
            metricEvent.endStep(FormMetrics.LOAD_DBREC_STEP);
            
            metricEvent.endCollection();
            metricEvent.logEvent(processingOk);
        }
    }
    
    private boolean readyToProcess(final long seqNo) 
    {
        final FormMetrics    metricEvent = pdfContext.getMetricsRec();

        LOGGER.debug("Fetching Request record for seqNo = {}", seqNo);
        
        final PDFGenRequestRec    currReq = dbHelper.getRecordBySeq(seqNo);

        pdfContext.setPdfGenReq(currReq);
        
        if (currReq == null)
        {
            LOGGER.error("Invalid request record for SEQ = {}, Skipping further processing for "
                         + "this seqNo = {}", seqNo);

            // We did not get the Request record even from DB. Cannot do anything to unlock the 
            // record even, so just skip processing at this point by returning failure
            return false;
        }
        LOGGER.debug("Completed Fetching records for seqNo = {}. Request Details SRNREF = {}, "
             + "PDFGENERATED = {}, PROCESS_STATUS = {}", currReq.getSeqNo(), currReq.getSrnRef(), 
             currReq.getPdfGeneratedFlag(), currReq.getProcessStatus());

        // We have found the record. Set the record Id of the record with Form TYPE set to 
        // UNKNOWN-FORM for now
        metricEvent.setRecordId(String.format("UNKNOWN-FORM\t%s\t%d\t%s\t%d",  
                                currReq.getSrnRef(), currReq.getSeqNo(),
                                currReq.getCreationTime().toString(),
                                currReq.getRetriesCount()));

        // If PDF is already generated, no further processing is required
        if (currReq.getProcessStatus() == ProcessingStatus.PDF_GEN_COMPLETE)
        {
            LOGGER.warn("Processing already completed for SRN = {}, SEQ = {}", currReq.getSrnRef(),
                         currReq.getSeqNo());
            return true;
        }
        final int        batchId = currReq.getBatchId();
        final String     lockFlag = currReq.getLockFlag();
        
        if (batchId != batchConfig.getBatchId() || !"Y".equals(lockFlag))
        {
            LOGGER.warn("Error validating record for SEQ = {}, skipping record with Lockflag = "
                        + "{}, batchID = {}, configured batchID = {}", seqNo, lockFlag, batchId, 
                        batchConfig.getBatchId());
            
            // Record is either not owned by this batch or record is not locked. In both cases 
            // we cannot touch this record, so just skip processing at this point by returning 
            // failure
            return false;
        }
        return true;
    }
    
    private boolean processPDFRequest() throws PDFGenException
    {
        String                             formName = null;
        PdfMetaData                        pdfMetaData = null;
        JsonObject                         dmsResponse = null;
        PdfDocument                        pdfDocWithDSCFields = null; 
        List<FileAttachmentDetailsBean>    fileAttachmentsList = null; 
        ByteArrayOutputStream              flattenedPdfOutStream = null;

        // Prepare for processing by validating input parameters as required and setting up of 
        // data references to be used locally in this method
        int    processingStatus = preProcessForPdfGeneration();

        // Irrespective of what is the processing status for the request, we will always need 
        // the PDF MetaData json based data set. So we always parse and load the same
        pdfMetaData = parseJSONDataForPDF();
        
        // Setup the form name for use in next set of calls
        formName = pdfMetaData.getForm().trim();
        
        if (processingStatus == ProcessingStatus.PDF_GEN_PENDING)
        {
            flattenedPdfOutStream = mapInputDataSetIntoOutputPdf();
            
            pdfDocWithDSCFields = addDSCFieldsIntoPdf(flattenedPdfOutStream, formName);
            
            fileAttachmentsList = retrieveAllAttachmentsForPdf(formName);
            
            final boolean skipProcessing = embedAttachmentsInPdf(pdfDocWithDSCFields, 
                                                                 fileAttachmentsList);
            
            // Setup the generated PDF File contents in a byte array for later use
            pdfContext.setGeneratedPdfFileContents(
                                    pdfContext.getPdfWithDSCsOutStream().toByteArray());
            if (skipProcessing)
            {
                // This happens only when we are executing in testRun mode. In test run mode
                // processing is stopped after PDF generation without updating any system state
                return true;
            }
            processingStatus = ProcessingStatus.DMS_INS_PENDING;
        }
        if (processingStatus == ProcessingStatus.DMS_INS_PENDING)
        {
            dmsResponse = uploadGeneratedPdfInDMS(pdfMetaData);
            
            processingStatus = ProcessingStatus.ATT_UPD_PENDING;
        }
        if (processingStatus == ProcessingStatus.ATT_UPD_PENDING)
        {
            updatePDFDocDMSIdUsingWSO2Service(pdfMetaData, dmsResponse);

            processingStatus = ProcessingStatus.QUE_UPD_PENDING;
        }
        if (processingStatus == ProcessingStatus.QUE_UPD_PENDING)
        {
            markPDFGenerationRequestComplete();
        }
        return true;
    }

    private int preProcessForPdfGeneration() throws PDFGenException 
    {
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        int                       result = request.getProcessStatus();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();
        
        try
        {
            currentMetric.startStep(FormMetrics.PRE_PROCESS_STEP);
        
            // Ensure we are dealing with only valid and recognized processing statuses that are 
            // allowed for a request in the system
            validateRequestProcessingStatus(request);

            // Setup PDF Context for this request
            preparePdfContext();

            // If in retry Mode check and tweak statuses based on recovery files availability for
            // this instance
            if (pdfContext.isRetryMode())
            {
                result = preProcessForRetryMode();
            }
            // Any processing status less than PDF_GEN_PENDING is considered as PDF_GEN_PENDING
            if (result < ProcessingStatus.PDF_GEN_PENDING)
            {
                result = ProcessingStatus.PDF_GEN_PENDING;
            }
            if (result != request.getProcessStatus())
            {
                LOGGER.info("Forced into changing Processing Status before PDF generation, for "
                         + "Request with SEQ = {}, SRN = {}, from {} to {}", request.getSeqNo(), 
                         request.getSrnRef(), request.getProcessStatus(), result);
                
                request.setProcessStatus(result);
            }
            if (result == ProcessingStatus.PDF_GEN_PENDING)
            {
                pdfContext.setPdfWithDSCsOutStream(new ByteArrayOutputStream(PDF_BUFFER_SIZE));
            }
        }
        catch (final Exception currEx)
        {
            // Increment retry count and mark record as permanently failed
            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_FAILURE, 
                                      "Preprocessing of incoming request failed : " + 
                                      currEx.getMessage());
            
            PDFGenException.mapAndThrow(currEx, "Failed preprocessing of incoming request "
                                                + "with SEQ: " + request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.PRE_PROCESS_STEP);
        }
        return result;
    }
    
    private PdfMetaData parseJSONDataForPDF() throws PDFGenException
    {
        PdfMetaData               result = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();
        
        try
        {
            currentMetric.startStep(FormMetrics.PARSE_JSON_STEP);
            
            LOGGER.info("Beginning process PDF Request with PROCESS_STATUS = {} for SRN = {} "
                        + "and SEQ = {}", request.getProcessStatus(), request.getSrnRef(), 
                        request.getSeqNo());

            final ObjectMapper    mapper = new ObjectMapper();
            final String          metaDataStr = new String(Base64.getDecoder().
                                                           decode(request.getFormMetaData()));
            
            final PdfMetaData     pdfMetaData = mapper.readValue(metaDataStr, PdfMetaData.class);
            
            // Set it up as the result to be returned and save the metaData decoded string
            // in work area for future reference
            result = pdfMetaData;
            pdfContext.setMetaDataStr(metaDataStr);

            // We load rest of the Data available in Request Data JSON only if processing 
            // status is PDF Generation Pending as this data is required only for PDF generation
            if (request.getProcessStatus() == ProcessingStatus.PDF_GEN_PENDING)
            {
                // Decode the form data JSON object retrieved from DB
                final String    formName = pdfMetaData.getForm().trim();
                String          formDataStr = new String(Base64.getDecoder().decode(
                                                                      request.getFormData()));
                formDataStr = PDFHelper.addToRequestBody(formDataStr, "srn", 
                                                         request.getSrnRef());
                formDataStr = PDFHelper.addToRequestBody(formDataStr, "requestType", 
                                                         request.getRequestType());

                // Save formDataStr for future references, will be needed in later steps for 
                // processing, such as retrieving all attachments
                pdfContext.setFormDataStr(formDataStr);
                
                LOGGER.debug("In parseJSONDataForPDF(), with formName = {}", formName); 
                
                final JSONObject    pdfJsonObj = new JSONObject(formDataStr);
                final JSONObject    fromDataJson;
                
                if (pdfJsonObj.has(JsonParserConstants.REQBODY))
                {
                    fromDataJson = pdfJsonObj;
                }
                else 
                {
                    fromDataJson = pdfJsonObj.getJSONObject(JsonParserConstants.AFDATA)
                                             .getJSONObject(JsonParserConstants.AFBOUNDDATA)
                                             .getJSONObject(JsonParserConstants.DATA);
                }
                pdfContext.getFormattedDataJson().put(JsonParserConstants.FORM1TAG, fromDataJson);
                
                LOGGER.debug("In parseJSONDataForPDF(), Form Data Json = \n [{}] ", 
                             pdfContext.getFormattedDataJson());
            }
        }
        catch (final Exception currEx)
        {
            // Increment retry count and mark record as permanently failed
            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_FAILURE, 
                                      "Metadata json parsing failed: " + currEx.getMessage());
            
            PDFGenException.mapAndThrow(currEx, "Failed parsing JSON data for SEQ: " + 
                                                request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.PARSE_JSON_STEP);
        }
        return result;
    }

    private ByteArrayOutputStream mapInputDataSetIntoOutputPdf() throws PDFGenException
    {
        ByteArrayOutputStream     result = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();

        try
        {
            currentMetric.startStep(FormMetrics.CREATE_PDF_STEP);
        
            LOGGER.debug("In mapInputDataSetIntoOutputPdf(), starting creation of PDF document "
                         + "for SRN = {} and SEQ = {}", request.getSrnRef(), request.getSeqNo());

            result = pdfContext.getGenerator().createOutputPDF(pdfContext);
            
            LOGGER.debug("In mapInputDataSetIntoOutputPdf(), creation of PDF document for SRN = "
                         + "{} and SEQ = {} completed successfully.", request.getSrnRef(), 
                         request.getSeqNo());
        }
        catch (final Exception currEx)
        {
            // Increment retry count and mark record as PDF Generation pending
            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_PENDING, 
                                      "Output PDF creation failed: " + currEx.getMessage());
    
            PDFGenException.mapAndThrow(currEx, "Failed to create output PDF file for SEQ: " + 
                                                request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.CREATE_PDF_STEP);
        }
        return result;
    }
    
    private PdfDocument addDSCFieldsIntoPdf(final ByteArrayOutputStream inStream,
                                            final String formName) throws PDFGenException
    {
        PdfDocument               result = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();

        try
        {
            currentMetric.startStep(FormMetrics.ADD_DSCBOXES_STEP);
        
            LOGGER.debug("In addDSCFieldsIntoPdf(), starting addition of DSC fields to PDF "
                         + "for SRN = {} and SEQ = {}", request.getSrnRef(), request.getSeqNo());

            final int      dscCount = PDFHelper.getDSCCount(pdfContext.getFormConfig(), request);
            
            LOGGER.debug("In addDSCFieldsIntoPdf() SignatureCount = {} for Form Name = {}, "
                         + "SRN = {}, SEQ = {}", dscCount, formName, request.getSrnRef(), 
                         request.getSeqNo());

            final JSONObject    fromDataJson = pdfContext.getFormattedDataJson().
                                                      getJSONObject(JsonParserConstants.FORM1TAG);
            
            result = PDFHelper.addDigitalSignatures(inStream, pdfContext.getPdfWithDSCsOutStream(), 
                                                pdfContext.getFormConfig(), request, fromDataJson);

            LOGGER.debug("In addDSCFieldsIntoPdf(), successfully completed addition of DSC "
                         + "fields to PDF for SRN = {} and SEQ = {}", request.getSrnRef(), 
                         request.getSeqNo());
        }
        catch (final Exception currEx)
        {
            // Increment retry count and mark record as PDF generation pending
            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_PENDING, 
                               "Failed to add DSC fields to the PDF: " + currEx.getMessage());
        
            PDFGenException.mapAndThrow(currEx, "Failed to add DSC fields to the PDF for SRN: " 
                                                + request.getSrnRef() + ", SEQ: " + 
                                                request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.ADD_DSCBOXES_STEP);
        }
        return result;
    }
    
    private List<FileAttachmentDetailsBean> retrieveAllAttachmentsForPdf(final String formName) 
                                                                         throws PDFGenException

    {
        List<FileAttachmentDetailsBean>    result = null;
        final PDFGenRequestRec             request = pdfContext.getPdfGenReq();
        final FormMetrics                  currentMetric = pdfContext.getMetricsRec();

        try
        {
            currentMetric.startStep(FormMetrics.GET_ATTACHMENTS_STEP);
        
            LOGGER.debug("In retrieveAllAttachmentsForPdf(), preparing file attachments list "
                         + "for embedding in the PDF for SRN = {} and SEQ = {}", 
                         request.getSrnRef(), request.getSeqNo());

            result = PDFHelper.getAttachments(dmsHelper, pdfContext.getFormDataStr(), 
                                              request.getSrnRef(), formName, request.getSeqNo());

            LOGGER.debug("In retrieveAllAttachmentsForPdf(), successfully completed preparation "
                         + "of file attachments list for embedding in PDF for SRN = {} and "
                         + "SEQ = {}", request.getSrnRef(), request.getSeqNo());
        }
        catch (final Exception currEx)
        {
            if (currEx instanceof AttachmentSizeException)
            {
                // Increment retry count and mark record as permanently failed
                prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_FAILURE, 
                                   "Failed to prepare file attachments list for PDF: " + 
                                   currEx.getMessage());
            }
            else
            {
            // Increment retry count and mark record as PDF generation pending
            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_PENDING, 
                               "Failed to prepare file attachments list for PDF: " + 
                               currEx.getMessage());
            }
        
            PDFGenException.mapAndThrow(currEx, "Failed to prepare file attachments list for "
                                                + "PDF with SEQ: " + request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.GET_ATTACHMENTS_STEP);
        }
        return result;
    }
    
    private boolean embedAttachmentsInPdf(final PdfDocument document, 
                                          final List<FileAttachmentDetailsBean> filesList)
                                          throws PDFGenException
    {
        boolean                   result = false;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();

        try
        {
            currentMetric.startStep(FormMetrics.ADD_ATTACHMENTS_STEP);
        
            LOGGER.debug("In embedAttachmentsInPdf(), inserting file attachments into PDF for "
                         + "SRN = {} and SEQ = {}", request.getSrnRef(), request.getSeqNo());

            if (filesList != null && filesList.size() > 0)
            {
                PDFHelper.addAttachmentsToPDF(filesList, document);
            }
            document.close();

            final byte[]    outBytesArray = pdfContext.getPdfWithDSCsOutStream().toByteArray();
            
            if (outBytesArray != null && outBytesArray.length > 0)
            {
                if (testRun)
                {
                    savePdfFileToDisk(request, pdfContext.getPdfWithDSCsOutStream(), 
                                      AppConstants.FILE_EXT, true);
                    result = true;
                }
            }
            LOGGER.debug("In embedAttachmentsInPdf(), successfully completed adding of "
                         + "attachments into PDF for SRN = {} and SEQ = {}", request.getSrnRef(), 
                         request.getSeqNo());
        }
        catch (final Exception currEx)
        {
            // Increment retry count and mark record as PDF generation pending
            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_PENDING, 
                               "Failed to embed file attachments in PDF: " + 
                               currEx.getMessage());
        
            PDFGenException.mapAndThrow(currEx, "Failed to embed file attachments in PDF for "
                                                + "SRN: " + request.getSrnRef() + "SEQ: " 
                                                + request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.ADD_ATTACHMENTS_STEP);
        }
        return result;
    }
    
    private JsonObject uploadGeneratedPdfInDMS(final PdfMetaData pdfMetaData) 
                                               throws PDFGenException
    {
        JsonObject                result = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();
        final byte[]              fileContents = pdfContext.getGeneratedPdfFileContents();

        try
        {
            currentMetric.startStep(FormMetrics.INSERT_IN_DMS_STEP);
        
            LOGGER.debug("In uploadGeneratedPdfInDMS(), uploading generated PDF file to DMS for "
                         + "SRN = {} and SEQ = {} with file size = {} bytes.", 
                         request.getSrnRef(), request.getSeqNo(), fileContents.length);

            result = dmsHelper.uploadPDFToDMS(pdfMetaData.getFoldername(), pdfMetaData.getSrn(), 
                                              pdfContext.getMetaDataStr(), fileContents);
            if (result == null || result.has("errorMsg"))
            {
                LOGGER.error("PDF File Contents upload to DMS failed with response "
                             + "= [{}]", result);
                throw new PDFGenException("PDF File Contents upload to DMS failed with "
                                          + "response = " + result);
            }
            if (!result.has("attachmentDMSId") || result.get("attachmentDMSId").getAsInt() == -1)
            {
                final String    dmsResponseStr = result.toString();
                
                LOGGER.error("PDF File Contents upload to DMS failed with response = [{}], "
                             + "attachmentDMSId is either missing or returned as -1 by DMS", 
                             dmsResponseStr);
           
                throw new PDFGenException("PDF File Contents upload to DMS failed, invalid DMS "
                                  + " attachementDMSId found in response = " + dmsResponseStr);
            }
            // We have a valid DMS response with a valid attachmentDMSId, we are good to proceed 
            // further. Set DMS Id in the database record    
            request.setDmsId(result.get("attachmentDMSId").getAsString().trim());

            LOGGER.debug("In uploadGeneratedPdfInDMS(), successfully completed uploading of "
                         + "generated PDF file into DMS for SRN = {} and SEQ = {} with DMS "
                         + "Id = {}", request.getSrnRef(), request.getSeqNo(), result);
        }
        catch (final Exception currEx)
        {
            // Save the generated PDF File contents to temporary store for later retries 
            // if the file does not exist already
            saveGeneratedPDFFileToTempStore(fileContents);
            
            // Increment retry count and mark record as DMS insertion pending
            prepareRequestForDBUpdate(request, true, ProcessingStatus.DMS_INS_PENDING, 
                                      "Failed to upload PDF file contents to DMS: " + 
                                      currEx.getMessage());
        
            PDFGenException.mapAndThrow(currEx, "Failed to upload PDF file contents to DMS "
                                                + "for SEQ: " + request.getSeqNo() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.INSERT_IN_DMS_STEP);
        }
        return result;
    }
    
    private void updatePDFDocDMSIdUsingWSO2Service(final PdfMetaData pdfMetaData, 
                                                   final JsonObject dmsResponse)
                                                   throws PDFGenException
    {
        String                    result = null;
        String                    wso2InputReqStr = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();
        final byte[]              fileContents = pdfContext.getWso2InputReqFileContents();

        try
        {
            currentMetric.startStep(FormMetrics.ADD_DMSID_TO_DB_STEP);
        
            LOGGER.debug("In updatePDFDocDMSIdUsingWSO2Service(), updating DMS Id of generated "
                         + "PDF in DB using WSO2 service with SRN = {} and SEQ = {}", 
                         request.getSrnRef(), request.getSeqNo());

            try
            {
                if (fileContents == null)
                {
                    wso2InputReqStr = wso2Helper.prepareReqForWSO2InsertUpdate(pdfMetaData, 
                                                                               dmsResponse);
    
                    LOGGER.debug("WSO2 Input Request [{}] was created from PdfMetaData and "
                                 + "DMSResponse objects ", wso2InputReqStr);
                }
                else
                {
                    wso2InputReqStr = new String(fileContents, StandardCharsets.UTF_8);
    
                    LOGGER.debug("WSO2 Input Request [{}] was created from file"
                                 , wso2InputReqStr);
                }
                result = wso2Helper.documentInsertUpdateRecord(wso2InputReqStr);

                LOGGER.debug("\nResponse received from WSO2 for documentInsertUpdateRecord() "
                             + "call  = [{}] ", result);
    
                LOGGER.info("DMS ID for the generated PDF was successfully updated in DB "
                            + "using WSO2 service end point with integrationId = {}, SRN = "
                            + "{}, SEQ = {}, FormName = {} in folder = {}, with filename = "
                            + "{}, dmsId = {} ", 
                            pdfMetaData.getIntegrationId(), pdfMetaData.getSrn(), 
                            request.getSeqNo(), pdfMetaData.getForm(), 
                            pdfMetaData.getFoldername(), pdfMetaData.getSrn(),
                            request.getDmsId());
    
                LOGGER.debug("In updatePDFDocDMSIdUsingWSO2Service(), successfully completed "
                             + "for SRN = {}, SEQ = {}, DMSId = {} and IntegrationId = {} "
                             , request.getSrnRef(), request.getSeqNo(), request.getDmsId(),
                             pdfMetaData.getIntegrationId());
            }
            catch (final Exception currEx)
            {
                LOGGER.info("updatePDFDocDMSIdUsingWSO2Service() failed with error msg = {} "
                            , currEx.getMessage());
                
                throw currEx;
            }
        }
        catch (final Exception currEx)
        {
            // Save the WSO2 Request Input File contents to temporary store for later retries 
            // if the file does not exist already
            final int    statusToSet = saveWSO2InputReqFileToTempStore(wso2InputReqStr);
            
            // Increment retry count and mark record as DMS insertion pending
            prepareRequestForDBUpdate(request, true, statusToSet, 
                                      "Failed to update DMS Id in DB using WSO2 service "
                                      + "endpoint: " + currEx.getMessage());
        
            PDFGenException.mapAndThrow(currEx, "Failed to update DMS Id in DB using WSO2 "
                                                + "service endpoint for SEQ = " 
                                                + request.getSeqNo() + " and SRN = "
                                                + request.getSrnRef() + ". ");
        }
        finally
        {
            currentMetric.endStep(FormMetrics.ADD_DMSID_TO_DB_STEP);
        }
    }
    
    private void markPDFGenerationRequestComplete()
    {
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final FormMetrics         currentMetric = pdfContext.getMetricsRec();

        try
        {
            currentMetric.startStep(FormMetrics.UPDATE_DB_REC_STEP);
        
            LOGGER.debug("In markPDFGenerationRequestComplete(), updating PDF Gen Request "
                         + "Record as completed with SRN = {} and SEQ = {}", request.getSrnRef(), 
                         request.getSeqNo());

            prepareRequestForDBUpdate(request, true, ProcessingStatus.PDF_GEN_COMPLETE, "");

            // Now override these values from the defaults that are setup by above call to 
            // mark PDF Generation as completed
            request.setProcessData("");
            request.setPdfGeneratedFlag("Y");
            
            dbHelper.updateRequestRec(request);

            // Record marked as successfully processed, delete any temporary files 
            // created for this SRN + SEQ combination
            CommonUtils.deleteFiles(request.getSrnRef() + AppConstants.UNDERSCORE + 
                                    request.getSeqNo(), sysParamsConfig.getPdfTempLocationPath());

            LOGGER.debug("In markPDFGenerationRequestComplete(), successfully updated PDF Gen "
                         + "Request Record as completed with SRN = {} and SEQ = {}", 
                         request.getSrnRef(), request.getSeqNo());
        }
        catch (final Exception currEx)
        {
            LOGGER.error("Failed to update PDF Generation Request as final step of processing "
                         + "in markPDFGenerationRequestComplete() for SRN  = {} Seq = {} with "
                         + "Error: \n{}", request.getSrnRef(), request.getSeqNo(), currEx);
        }
        finally
        {
            currentMetric.endStep(FormMetrics.UPDATE_DB_REC_STEP);
        }
    }
    
    private String saveGeneratedPDFFileToTempStore(final byte[] fileContents)
    {
        String                    result = null;
        boolean                   needToSaveFile = true;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final String              filePath = getGeneratedPdfFileNameInTempStore(request, true);
        final String              fileName = getGeneratedPdfFileNameInTempStore(request, false);
        
        try
        {
            // If we are not executing in retry mode then blindly overwrite existing file contents 
            // even if they exist, else check if the file does not exist then create the file 
            needToSaveFile = (!pdfContext.isRetryMode() 
                              || (pdfContext.isRetryMode() && !CommonUtils.fileExists(filePath)));
            
            if (needToSaveFile && fileContents != null && fileContents.length > 0)
            {
                CommonUtils.writeToFile(filePath, fileContents);
                result = fileName;
            }
        }
        catch (final Exception currEx)
        {
            // Log the error message and the gobble up the exception
            LOGGER.error("Failed to save generated PDF File to temporary store with "
                         + "FilePath = {} with Error: \n{}", filePath, currEx);
        }
        return result;
    }
    
    private int saveWSO2InputReqFileToTempStore(final String fileContents)
    {
        int                       result = 0;
        boolean                   needToSaveFile = true;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        final String              filePath = getWSO2InputReqFileNameInTempStore(request, true);
        
        try
        {
            // If we are not executing in retry mode then blindly overwrite existing file contents 
            // even if they exist, else check if the file does not exist then create the file 
            needToSaveFile = (!pdfContext.isRetryMode() 
                              || (pdfContext.isRetryMode() && !CommonUtils.fileExists(filePath)));
            
            if (needToSaveFile && fileContents != null && fileContents.length() > 0)
            {
                CommonUtils.writeToFile(filePath, fileContents.getBytes());
            }
            result = ProcessingStatus.ATT_UPD_PENDING;
        }
        catch (final Exception currEx)
        {
            // Log the error message and the gobble up the exception
            LOGGER.error("Failed to save WSO2 Input Request File to temporary store with "
                         + "FilePath = {} with Error: \n{}", filePath, currEx);
            result = ProcessingStatus.DMS_INS_PENDING;
        }
        return result;
    }
    
    private void savePdfFileToDisk(final PDFGenRequestRec request, 
                                   final ByteArrayOutputStream outStream, 
                                   final String fileSuffix, 
                                   final boolean logAtInfoLevel) 
    {
        final StringBuilder fileNameBuilder = new StringBuilder();
        
        fileNameBuilder.append(sysParamsConfig.getPdfTempLocationPath())
                       .append(File.separator)
                       .append(request.getSrnRef())
                       .append(AppConstants.UNDERSCORE)
                       .append(request.getSeqNo())
                       .append(fileSuffix);
        
        if (logAtInfoLevel)
        {
            LOGGER.info("In PdfProcessor.savePdfFileToDisk(), saving {} file to disk", 
                        fileNameBuilder);
        }
        else
        {
            LOGGER.debug("In PdfProcessor.savePdfFileToDisk(), saving {} file to disk", 
                         fileNameBuilder);
        }
        CommonUtils.writeToFile(fileNameBuilder.toString(), outStream.toByteArray());
    }
    
    private void updateRequestRecord(final boolean updateRecord)
    {
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();

        try
        {
            // If this is a test run, then we do not update any states in he system
            if (!updateRecord || sysParamsConfig.isTestRun())
            {
                return;
            }
            dbHelper.updateRequestRec(request);
        
            if (request.getProcessStatus() == ProcessingStatus.PDF_GEN_COMPLETE 
                || request.getProcessStatus() == ProcessingStatus.PDF_GEN_FAILURE)
            {
                // Record marked as permanently failed or successfully processed, delete any 
                // temporary files created for this SRN + SEQ combination
                CommonUtils.deleteFiles(request.getSrnRef() + AppConstants.UNDERSCORE + 
                                request.getSeqNo(), sysParamsConfig.getPdfTempLocationPath());
            }
        }
        catch (final Exception currEx)
        {
            // We log and gobble up the exception as nothing can be done if DB operation fails 
            // in BatchWorker
            LOGGER.error("Failed in updateRequestRecord for Request with SRN = {}, SEQ = {}, "
                         + "Error: \n{}", request.getSrnRef(), request.getSeqNo(), currEx);
        }
    }
    
    private void prepareRequestForDBUpdate(final PDFGenRequestRec request, 
                                           final boolean incrementRetryCount, 
                                           final int processingStatusToSet,
                                           final String statusMsgToSet)
    {
        int    nRetries = request.getRetriesCount();
        
        if (incrementRetryCount)
        {
            nRetries++;
        }
        request.setLockFlag("N");
        request.setPdfGeneratedFlag("N");
        request.setRetriesCount(nRetries);
        
        final String    failureReason = StringUtils.left(statusMsgToSet, 
                                                         AppConstants.FAILURE_REASON_SIZE);
        request.setProcessData(failureReason);
        request.setProcessStatus(processingStatusToSet);
        
        if (nRetries > sysParamsConfig.getBatchRetryCount() && 
            request.getProcessStatus() != ProcessingStatus.PDF_GEN_COMPLETE)
        {
            request.setProcessStatus(ProcessingStatus.PDF_GEN_FAILURE);
        }
    }

    private int preProcessForRetryMode() throws PDFGenException 
    {
        String                    filePath = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();
        int                       result = request.getProcessStatus();
        
        LOGGER.debug("In preProcessForRetryMode(), with incoming request processing "
                     + "status = {}", result);
        
        if (result == ProcessingStatus.ATT_UPD_PENDING)
        {
            filePath = getWSO2InputReqFileNameInTempStore(request, true);
            
            if (CommonUtils.fileExists(filePath))
            {
                pdfContext.setWso2InputReqFileContents(readFileFromTempStore(filePath));
            }
            else
            {
                LOGGER.debug("In preProcessForRetryMode(), changing incoming request processing "
                             + "status from {} to {} as WSO2 Input Req File = {} was not found", 
                             result, ProcessingStatus.DMS_INS_PENDING, filePath);
                
                result = ProcessingStatus.DMS_INS_PENDING;
            }
        }
        if (result == ProcessingStatus.DMS_INS_PENDING)
        {
            filePath = getGeneratedPdfFileNameInTempStore(request, true);
            
            if (CommonUtils.fileExists(filePath))
            {
                pdfContext.setGeneratedPdfFileContents(readFileFromTempStore(filePath));
            }
            else
            {
                LOGGER.debug("In preProcessForRetryMode(), changing incoming request processing "
                             + "status from {} to {} as Generated PDF File = {} was not found", 
                             result, ProcessingStatus.PDF_GEN_PENDING, filePath);
                
                result = ProcessingStatus.PDF_GEN_PENDING;
            }
        }
        return result;
    }
    
    private byte[] readFileFromTempStore(final String filePath) throws PDFGenException
    {
        byte[]          result = null;
        
        LOGGER.debug("Reading content of File in temporary store with file path = {}", filePath);

        final File    pdfFile = new File(filePath);

        if (pdfFile.length() <= 0L || pdfFile.isDirectory())
        {
            final String    errorMsg = "Either file with FilePath = " + filePath + ", does not"
                                       + " exist or is an empty file or a directory.";
            
            LOGGER.error(errorMsg);
            throw new PDFGenException(errorMsg);
        }
        try
        {
            result = FileUtils.readFileToByteArray(pdfFile); 

            LOGGER.debug("File with Path = {}, read successfully with file size = {} bytes", 
                         filePath, result.length);
        }
        catch (final IOException currEx)
        {
            PDFGenException.mapAndThrow(currEx, "Error in reading File contents from temporary "
                                                + "store with filePath = " + filePath + ". ");
        }
        return result;
    }
    
    private String getGeneratedPdfFileNameInTempStore(final PDFGenRequestRec request, 
                                                      final boolean needFullFilePath)
    {
        final StringBuffer    result = new StringBuffer();
        
        if (needFullFilePath)
        {
            result.append(sysParamsConfig.getPdfTempLocationPath())
                  .append(File.separator);
        }
        result.append(request.getSrnRef())
              .append(AppConstants.UNDERSCORE)
              .append(request.getSeqNo())
              .append(AppConstants.FILE_EXT);
        
        return result.toString();
    }
    
    private String getWSO2InputReqFileNameInTempStore(final PDFGenRequestRec request, 
                                                      final boolean needFullFilePath)
    {
        final StringBuffer    result = new StringBuffer();
        
        if (needFullFilePath)
        {
            result.append(sysParamsConfig.getPdfTempLocationPath())
                  .append(File.separator);
        }
        result.append(request.getSrnRef())
              .append(AppConstants.UNDERSCORE)
              .append(request.getSeqNo())
              .append(AppConstants.WSO2REQ_SUFFIX)
              .append(AppConstants.TXT);
        
        return result.toString();
    }
    
    private void validateRequestProcessingStatus(final PDFGenRequestRec request) 
                                                 throws PDFGenException
    {
        final int    status = request.getProcessStatus();
        
        if (status == ProcessingStatus.PROCESSING_PENDING
            || status == ProcessingStatus.PDF_GEN_PENDING
            || status == ProcessingStatus.DMS_INS_PENDING
            || status == ProcessingStatus.ATT_UPD_PENDING
            || status == ProcessingStatus.QUE_UPD_PENDING
            || status == ProcessingStatus.PDF_GEN_COMPLETE    
            || status == ProcessingStatus.PDF_GEN_FAILURE)
        {
            return;
        }
        throw new PDFGenException("Invalid Processing Status in request with SEQ: " + 
                                  request.getSeqNo() + " and Processing Status = " + status);
    }

    private void preparePdfContext() throws PDFGenException
    {
        final PDFGenRequestRec    currReq = pdfContext.getPdfGenReq();
        final String              templateType = getPDFTemplateType(currReq);

        if (templateType == null)
        {
            LOGGER.error("Invalid Form Template Type specified in request with SEQ = {}, "
                         + "SRNREF = {}, FormType = {}", currReq.getSeqNo(), currReq.getSrnRef(),
                         currReq.getFormType());
            throw new PDFGenException("PDF Generator not found for form type: " 
                                      + currReq.getFormType());
        }
        final FormsConfig    formConfig = FormsConfigManager.getInstance().
                                                       getFormConfiguration(templateType);
        if (formConfig == null)
        {
            LOGGER.error("Form Type specified in request with SEQ = {}, SRNREF = {}, "
                         + "FormType = {} is not supported", currReq.getSeqNo(), 
                         currReq.getSrnRef(), currReq.getFormType());
            
            throw new PDFGenException("Form Configuration not available for Template type: " + 
                                      templateType);
        }
        pdfContext.setFormConfig(formConfig);
        
        // Now set the proper record Id with the form type in the metric event
        pdfContext.getMetricsRec().setRecordId(String.format("%s\t%s\t%d\t%s\t%d", 
                                               formConfig.getMasterFormName(), 
                                               currReq.getSrnRef(), currReq.getSeqNo(),
                                               currReq.getCreationTime().toString(), 
                                               currReq.getRetriesCount()));
        
        final PdfOutputGenerator    generator = formConfig.getPdfGenerator();

        if (generator == null)
        {
            LOGGER.error("Could not find a PDF Generator associated with Form Type specified in "
                         + "request with SEQ = {}, SRNREF = {}, FormType = {}", currReq.getSeqNo(), 
                         currReq.getSrnRef(), currReq.getFormType());
    
            throw new PDFGenException("PDF Generator not available for Template type: " + 
                                      templateType);
        }
        pdfContext.setGenerator(generator);
        
        final String           generatorId = generator.getGeneratorId();
        PdfGeneratorContext    generatorCtx = generatorsCtxMap.get(generatorId);
        
        if (generatorCtx == null)
        {
            generatorCtx = generator.createContext();
            generatorsCtxMap.put(generatorId, generatorCtx);
        }
        pdfContext.setGeneratorContext(generatorCtx);
    }
    
    private String getPDFTemplateType(final PDFGenRequestRec request)
    {
        final String    refer = request.getFormType();
        
        if (refer == null || refer.isEmpty())
        {
            LOGGER.error("REFER field is null for SEQ = {}, can't proceed further", 
                         request.getSeqNo());
            return null;
        }
        final String[]    referSplit = refer.split("/");
        final String      result = referSplit[referSplit.length - 1].trim();
        
        LOGGER.debug("Form type = {} for SEQ = {}", result, request.getSeqNo());
        
        return result;
    }
}
