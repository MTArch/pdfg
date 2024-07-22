package com.mca.pdfgen.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.fields.PdfSignatureFormField;
import com.itextpdf.forms.fields.SignatureFormFieldBuilder;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.StampingProperties;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.RegexBasedLocationExtractionStrategy;
import com.itextpdf.kernel.pdf.filespec.PdfFileSpec;
import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.beans.dms.FileAttachmentDetailsBean;
import com.mca.pdfgen.beans.pdf.PDFSignatureLocationInfo;
import com.mca.pdfgen.beans.pdf.SigFieldDefn;
import com.mca.pdfgen.configs.FormsConfig;
import com.mca.pdfgen.configs.FormsConfigManager;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.exception.AttachmentSizeException;
import com.mca.pdfgen.exception.PDFGenException;
import com.mca.pdfgen.utils.CommonUtils;

public final class PDFHelper 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(PDFHelper.class);
    
    /** 
     * Traverses the JSONObject tree and gets the actual number of form attachments, then traverses 
     * the attachment array and gets the attachmentDMSId, and activityFileName values from the json, 
     * replaces the special characters from activityFileName string and creates a 
     * filename = dmsID + "_" + activityFileName(without spl chrs). Using this filename downloads 
     * the document from DMS using dmsId gets it as a byte[] sets the filename, fileType and byte[] 
     * in FileAttachmentWrapper object and returns a list of such objects
     * @param formDataStr
     * @param srn
     * @param formName
     * @param seqNo 
     * @return a list of fileAttachments or null if there are no attachments available
     * @throws PDFGenException in case of connectivity issues with DMS while downloading file
     */
    public static List<FileAttachmentDetailsBean> getAttachments(final DMSHelper dmsHelper,
    		                                                     final String formDataStr, 
    		                                                     final String srn, 
                                                                 final String formName, 
                                                                 final long seqNo) 
                                                                 throws PDFGenException, 
                                                                        AttachmentSizeException 
    {
    	FileAttachmentDetailsBean          attachmentBean;
        List<FileAttachmentDetailsBean>     fileAttachments = null;

        try 
        {
            JSONObject reqBodyJson; 
            JSONArray jsonfileAttach = null;
            JSONObject json = new JSONObject(formDataStr);
            
            if (json.has(JsonParserConstants.REQBODY))
            {
                reqBodyJson = json.getJSONObject(JsonParserConstants.REQBODY);
                if (reqBodyJson.has(JsonParserConstants.FORMDATA))
                {
                	JSONObject formDataJson = reqBodyJson
                								.getJSONObject(JsonParserConstants.FORMDATA);
                	if (formDataJson.has(JsonParserConstants.FORMATTACH))
                	{
                		jsonfileAttach = formDataJson.getJSONArray(JsonParserConstants.FORMATTACH);
                	}
                }
                if (reqBodyJson.has(JsonParserConstants.FORMATTACH))
                {
                    jsonfileAttach = reqBodyJson.getJSONArray(JsonParserConstants.FORMATTACH);
                }
            }
            else
            {
                JSONObject formDataJson;
                formDataJson = json.getJSONObject(JsonParserConstants.AFDATA)
                                .getJSONObject(JsonParserConstants.AFBOUNDDATA)
                                .getJSONObject(JsonParserConstants.DATA)
                                .getJSONObject(JsonParserConstants.REQBODY)
                                .getJSONObject(JsonParserConstants.FORMDATA);
 
                if (formDataJson.has(JsonParserConstants.FORMATTACH))
                {
                    jsonfileAttach = formDataJson.getJSONArray(JsonParserConstants.FORMATTACH);
                }
            }
            int noOfAttachments = 0;
            if (jsonfileAttach != null)
            {
                noOfAttachments = jsonfileAttach.length();
                LOGGER.info("In getAttachments() Total Attachments as per form metadata = {} for " 
                    +" SRN = {}, SEQ = {} and formName = {}",noOfAttachments, srn, seqNo, formName);
            }
            else
            {
                LOGGER.info("No Attachment required for SRN = {}, SEQ = {} and formName = {}",
                            srn, seqNo, formName);
                return fileAttachments;
            }
            if (noOfAttachments == 0)
            {
                LOGGER.info("No Attachment required for SRN = {}, SEQ = {} and formName = {}",
                        srn, seqNo, formName);

                return fileAttachments;
            }
            fileAttachments = new ArrayList<>();
            
            JSONObject attachedObj;
            long totalAttachmentsSize = 0;
            for (int ctr = 0; ctr < noOfAttachments; ctr++)
            {
                attachedObj = jsonfileAttach.getJSONObject(ctr);
                String filename = null;
                
                String dmsId = attachedObj.getString(JsonParserConstants.ATTACHDMSID);
                LOGGER.debug("In getAttachments() Attachment# = {}, DMS Id = {}", ctr, dmsId);

                if (attachedObj.has(JsonParserConstants.ACTIVTIYFILENAME)) 
                {
                    String fn = attachedObj.get(JsonParserConstants.ACTIVTIYFILENAME).toString();
                    LOGGER.debug("In getAttachments() activityFileName = {}", 
                                        attachedObj.get(JsonParserConstants.ACTIVTIYFILENAME));
                    
                    fn = CommonUtils.replaceSpecialCharacters(fn);
                    LOGGER.debug("In getAttachments() after replacing spl chars activityFileName = "
                                 + "{}", fn);
                    
                    filename = dmsId + AppConstants.UNDERSCORE + fn;
                    LOGGER.debug("In getAttachments() FileName = {} for attachment# = {} for "
                                 + "dmsId = {}", filename, ctr, dmsId);
                }
                // Download the file from DMS here
                byte[] fileBytes = downloadFileFromDMS(dmsHelper, dmsId);
                
                if (fileBytes != null) 
                {
                    totalAttachmentsSize += fileBytes.length;
                    if (totalAttachmentsSize > AppConstants.ATTACHMENT_FILE_SIZE)
                    {
                        throw new AttachmentSizeException("Total File Attachment Size > 10 MB,"
                                                          + " size is:" + totalAttachmentsSize);
                    }
                }
                
                if (fileBytes != null && fileBytes.length > 0 && filename != null) 
                {
                    LOGGER.debug("In getAttachments() File of {} bytes downloaded from DMS",
                                 fileBytes.length);
                    attachmentBean = new FileAttachmentDetailsBean(
                    		             fileBytes, CommonUtils.getFileType(filename), filename);
                    fileAttachments.add(attachmentBean);
                    LOGGER.debug("In getAttachments() File added to list. Size of list = {}",
                                  fileAttachments.size());
                }
                else 
                {
                    LOGGER.warn("In getAttachments() File = {} not downloaded for SRN = {}, SEQ = "
                            + "{} and formName = {}", filename, srn, seqNo, formName);
                }
            }
            LOGGER.info("In getAttachments() Total Files = {} / {} downloaded from DMS for SRN = {}"
                        + ", SEQ = {} and formName = {}", fileAttachments.size(), noOfAttachments, 
                        srn, seqNo, formName);
        } 
        catch (org.json.JSONException currEx) 
        {
            LOGGER.error("Exception in downloading fileAttachments for SRNREF = {}, and "
            		     + "formName = {} with details: [{}]", srn, formName, currEx);
        }
        return fileAttachments;
    }

    /**
     * Helper method that embeds the attachment files into the given PDF document
     * @param attachList
     * @param pdfDoc
     * @throws PDFGenException
     */
    public static PdfDocument addAttachmentsToPDF(List<FileAttachmentDetailsBean> attachList, 
                                           PdfDocument pdfDoc) throws PDFGenException
    {
        if (attachList == null || pdfDoc == null)
        {
            throw new PDFGenException("Invalid inputs received in addAttachmentsToPDF.");
        }
        
        for (FileAttachmentDetailsBean attachment: attachList)
        {
            String embeddedFileNm = attachment.getAttachmentFileName();
            PdfName mimeType = new PdfName(attachment.getFileContentType());
        
            PdfFileSpec pdfSpec = PdfFileSpec.createEmbeddedFileSpec(pdfDoc, 
                                                                    attachment.getFileData(), 
                                                                    embeddedFileNm, embeddedFileNm, 
                                                                    mimeType, null, null);
            pdfDoc.addFileAttachment(embeddedFileNm, pdfSpec);

            LOGGER.debug("File with fileName = {} attached successfully to PDF.", embeddedFileNm);
        }
        return pdfDoc;
    }

    /**
     * This method searches for the configured regEx pattern used in the form for getting location
     * co-ordinates on the form where rectangle placeholders for digital signatures need to be 
     * added in the PDF document.
     * @param flatbaos - ByteArrayOutputStream which holds flattened pdf doc
     * @param signedbaos - ByteArrayOutputStream which will hold signature fields
     * @param formConfig - FormsConfig details for this form type
     * @param record - DB record from where the data is fetched
     */
    public static PdfDocument addDigitalSignatures(ByteArrayOutputStream flatbaos, 
                          ByteArrayOutputStream signedbaos, FormsConfig formConfig,
                          PDFGenRequestRec record, JSONObject dataJson) throws PDFGenException
    {
        int cfgSignCount;
        // Create an output stream to which will hold the flattened pdf, the digital signatures 
        // and attachments
        try 
        {
            PdfDocument flatPdf = new PdfDocument(new PdfReader(new 
                                                  ByteArrayInputStream(flatbaos.toByteArray())), 
                                                  new PdfWriter(signedbaos),
                                                  new StampingProperties().useAppendMode());
            
            cfgSignCount = getDSCCount(formConfig, record);

            if (cfgSignCount <= 0)
            {
                return flatPdf;
            }
            // Check if the formconfig has dscCount and signature field definitions are valid
            Map <String, String> valueMap = new HashMap<>();
            
            validateAndFetchSigFields(formConfig, cfgSignCount, dataJson, valueMap);
            
            String regExpToUse = FormsConfigManager.getRegExPatternToUse(
            		                                    formConfig.isUseWithHashmarkRegEx());
            
            final List<PDFSignatureLocationInfo> signaturesInfoList; 
            signaturesInfoList = getAllSignaturesToAdd(flatPdf, cfgSignCount, regExpToUse, 
                                                       formConfig.getSigFieldPrefix(), valueMap);

            final int listSize = signaturesInfoList.size();
            LOGGER.debug("Number of signatures to be added = {}", listSize);
            
            PdfAcroForm formDoc = PdfAcroForm.getAcroForm(flatPdf, true);
            
            for (int index = 0; index < listSize; index++)
            {
                PdfSignatureFormField field = null;
                final PDFSignatureLocationInfo currSignInfo = signaturesInfoList.get(index);
                
                if (currSignInfo == null)
                {
                    continue;
                }
                field = new SignatureFormFieldBuilder(flatPdf, 
                                                 currSignInfo.getSignFieldLabelTxt())
                                                 .setWidgetRectangle(currSignInfo.getLocation())
                                                 .createSignature();
                
                // iText fix for multiple signature issue
                field.getPdfObject().put(PdfName.Type, PdfName.Annot);
                
                field.getWidgets().get(0).setHighlightMode(PdfAnnotation.HIGHLIGHT_INVERT)
                        .setFlags(PdfAnnotation.PRINT).setColor(ColorConstants.LIGHT_GRAY);

                field.getWidgets().get(0).setFlags(PdfAnnotation.LOCKED);
                
                field.getFirstFormAnnotation().setPage(currSignInfo.getPageNumber());
                
                formDoc.addField(field);
            }
            return flatPdf;
        }
        catch (final Exception currEx)
        {
            PDFGenException.mapAndThrow(currEx, "Could not add digital signature boxes to "
            		                            + "the form. ");
        }
        return null;
    }
    
    public static String addToRequestBody(String formDataStr, String key, String value) 
    {
        JSONObject dataJsonObj;
        JSONObject jsonObj = new JSONObject(formDataStr);
        
        if (jsonObj.has(JsonParserConstants.REQBODY)) 
        {
            jsonObj.getJSONObject(JsonParserConstants.REQBODY).put(key, value);
            formDataStr = jsonObj.toString();
            return formDataStr;
        }
        dataJsonObj = jsonObj.getJSONObject(JsonParserConstants.AFDATA)
                     .getJSONObject(JsonParserConstants.AFBOUNDDATA)
                     .getJSONObject(JsonParserConstants.DATA); 
        
        if (dataJsonObj.has(JsonParserConstants.REQBODY))
        {
            dataJsonObj.getJSONObject(JsonParserConstants.REQBODY).put(key, value);
            formDataStr = jsonObj.toString();
            return formDataStr;
        }
        return formDataStr;
    }

    /**
     * Helper method which will check a flag and return the number of digital signature boxes
     * to be added to the generated PDF
     * @param frmConfig
     * @param record
     * @return number of digital signature boxes to be added
     */
    public static int getDSCCount(FormsConfig frmConfig, PDFGenRequestRec record)
    {
        return (frmConfig.isUseConfigDSCCount()) ? frmConfig.getSignatureCount() : 
                                                   record.getDscCount();
    }
    
    /**
     * Fetches the authentication token and then gets the document from DMS server. 
     * @param docId of the file to get from DMS
     * @return byte[] containing the file contents fetched from DMS
     * @throws PDFGenException
     */
    private static byte[] downloadFileFromDMS(final DMSHelper dmsHelper, final String docId) 
    		                                  throws PDFGenException 
    {
        byte[]    result = null;

        // TODO: AAN - Temporary patches added to our code to ensure we try invoking WSO2 or DMS 
        // APIs twice as they randomly fail at times with malformed JSON Exception and on retry
        // go through. This is a patch till the problem is identified and fixed in DMS & WSO2 APIs
        // by those systems
        for (int currRetryIdx = 0; currRetryIdx < 2; currRetryIdx++)
        {
            try
            {
		        if (docId != null && ! docId.equals(JsonParserConstants.EMPTY_STRING)) 
		        {
		            final JsonObject    documentJson = dmsHelper.getDocumentFromDMS(docId);  
		            
		            if (documentJson == null)
		            {
		                LOGGER.warn("getDocumentFromDMS() recieved null from "
		                		    + "DMSHelper.getDocumentFromDMS() method call."); 
		            	return result; 
		            }
		            if (documentJson.getAsJsonObject(JsonParserConstants.DATA)
		                             .getAsJsonObject(JsonParserConstants.NGOGETDOCBDORESPTAG)
		                             .get("statusCode").getAsInt() != 0)
		            {
		                LOGGER.warn("No document found in DMS for DocId = {}", docId);
		                LOGGER.debug("getDocumentFromDMS() response = [{}]", documentJson); 
		            }
		            else if (documentJson.getAsJsonObject(JsonParserConstants.DATA)
		                            .getAsJsonObject(JsonParserConstants.NGOGETDOCBDORESPTAG)
		                            .get(JsonParserConstants.DOCCONTENT) != null) 
		            {
		                result = Base64.decodeBase64(
		                	documentJson.getAsJsonObject(JsonParserConstants.DATA)
		                                .getAsJsonObject(JsonParserConstants.NGOGETDOCBDORESPTAG)
		                                .get(JsonParserConstants.DOCCONTENT).toString()
		                                .getBytes(StandardCharsets.UTF_8));
		                
		                LOGGER.debug("Length of document downloaded from DMS = {} bytes", 
		                		     result.length);
		            }
		            else
		            {
		                LOGGER.error("Document not downloaded from DMS response = [{}] ", 
		                		     documentJson);
		            }
		        }
		        return result;
            }
            catch (final Exception currEx)
            {
                LOGGER.info("downloadFileFromDMS() failed with error msg = {} and currRetryIdx "
                		     + "= {}", currEx.getMessage(), currRetryIdx);
                
                if (currRetryIdx == 0)
                {
                    continue;
                }
                else
                {
                    throw currEx;
                }
            }
        }
        return result;
    }
    
    /**
     * Helper method which will find the exact locations of signature boxes to be added to the final
     * pdf based on the provided regular expression which will contain the string pattern which
     * was used during form creation to mark the placeholders for signature fields. 
     *   
     * @param pdfDocument - document in which to search for the markers
     * @param dscCount - number of signatures to be affixed in total
     * @param regExp
     * @param sigFieldPrefix 
     */
    public static List<PDFSignatureLocationInfo> getAllSignaturesToAdd( PdfDocument pdfDocument, 
                                    final int dscCount, final String regExp, String sigFieldPrefix,
                                    Map<String, String> valueMap)
    {
        final List<PDFSignatureLocationInfo> result = new ArrayList<>();
        
        final Map<String, PDFSignatureLocationInfo> locationLabelsFoundMap = new HashMap<>();

        int pageCount = pdfDocument.getNumberOfPages();

        LOGGER.debug("NumPages in PDF = {} RegExp to search = [{}]", pageCount, regExp);

        try 
        {
            PDFSignatureLocationInfo    currSigLocInfo = null;
            
            for (int page = 1; page <= pageCount; page++)
            {
                final RegexBasedLocationExtractionStrategy strategy = 
                                            new RegexBasedLocationExtractionStrategy(regExp);
                
                final PdfCanvasProcessor processor = new PdfCanvasProcessor(strategy);
                
                processor.processPageContent(pdfDocument.getPage(page));
    
                Collection<IPdfTextLocation> locations = strategy.getResultantLocations();
                
                // If Regex was not found on a given page the collection size will be 0
                if (locations == null) 
                {
                    continue;
                }
                
                final Iterator<IPdfTextLocation> lociter = locations.iterator();

                while (lociter.hasNext())
                {
                    IPdfTextLocation location = lociter.next();
                    
                    if (location == null)
                    {
                        continue;
                    }
                    currSigLocInfo = getSignatureLocationInfo(location, dscCount, page, pdfDocument,
                                                 sigFieldPrefix, locationLabelsFoundMap, valueMap);
      
                    if (currSigLocInfo == null)
                    {
                        continue;
                    }              
                    result.add(currSigLocInfo);
                    
                    LOGGER.debug("Signature Location = [{}]", currSigLocInfo);
                }
                // If size matches total signature count it means all the required signature
                // box locations have already been found, do not search on remaining pages
                if(result.size() == dscCount)
                {
                    LOGGER.info("Total Signatures required = {} matches total found.", dscCount);
                    break;
                }
            }
        }
        catch(final EmptyStackException currEx)
        {
            // iText code is throwing this exception when the page is blank.
            // We are not able to find any other way of determining a blank page programmatically
            // hence we are suppressing this for now.
            LOGGER.debug("PDF Document likely contains a blank page. {}", currEx);
        }
        return result;
    }
    
    /**
     * Validates that the form has valid signature field definitions and then fetches the 
     * first and second word key/value pairs, populates the valueMap object. 
     * Note: There may be a scenario where the second word is not required for certain forms, in
     * which case the form config may not have SigFieldDefinitions but still require signatures.
     * @param formConfig
     * @param sigCount
     * @param formData
     * @param valueMap
     * @throws PDFGenException 
     */
    public static void validateAndFetchSigFields(FormsConfig formConfig, int sigCount,
                         JSONObject formData, Map<String, String> valueMap) throws PDFGenException
    {
        SigFieldDefn[] fieldDefs = formConfig.getFieldDefs();  
        if (sigCount > 0 &&  fieldDefs == null)
        {
            LOGGER.warn("Signature Fields are required in the form but no signature field "
                        + "definition has been provided for form id = {} and form name = {}", 
                        formConfig.getFormId(), formConfig.getRequestFormName());
            // Note: This may be a case where signature fields do not require second word suffix
            return;
        }
        LOGGER.debug("Number of SigFieldDefinitions = {}", fieldDefs.length);
        for (SigFieldDefn fieldDef : fieldDefs)
        {
            fieldDef.getFieldValuePairs(formData, valueMap);
        }
        LOGGER.debug("Retrieved Signature Field Details for {} signatures", valueMap.size());
    }

    /**
     * This takes in a location object gets the location label, splits it into words as required
     * for naming the signature field, adds the found location in the map and returns the array 
     * containing words required for signatureField label
     * @param location
     * @param dscCount
     * @param page
     * @param pageWidth 
     * @param sigFieldPrefix 
     * @param locationLabelsFoundMap
     * @param valueMap
     * @return PDFSignatureLocationInfo
     */
     private static PDFSignatureLocationInfo getSignatureLocationInfo(IPdfTextLocation location, 
                                int dscCount, int page, PdfDocument doc, String sigFieldPrefix, 
                                Map<String, PDFSignatureLocationInfo> locationLabelsFoundMap,
                                Map<String, String> valueMap)
    {
        String locationLabel = location.getText();
        
        if (locationLabel == null || locationLabel.trim().length() == 0)
        {
            LOGGER.warn("Pattern Matched but no location label found.");
            return null;
        }
        // In case of incorrect form configuration
        if (sigFieldPrefix == null || sigFieldPrefix.equalsIgnoreCase(
                                                                JsonParserConstants.EMPTY_STRING))
        {
            LOGGER.warn("Pattern Matched but no signature field prefix specified.");

            return null;
        }
        LOGGER.debug("locationLabel before trim = {}", locationLabel);
        
        locationLabel = locationLabel.trim().replaceAll("\s+", AppConstants.SINGLE_SPACE);
        
        LOGGER.debug("location with label = {} on page = {} at "
                    + "position = {}", locationLabel, page, location.getRectangle());
        
        // The location label text contains words separated by a space
        final String[] words = locationLabel.split(AppConstants.SINGLE_SPACE);
        
        String firstWord = (words.length > 0) ? words[0].trim().toUpperCase() : 
                                                                JsonParserConstants.EMPTY_STRING;
        String secondWord = JsonParserConstants.EMPTY_STRING;
        
        String firstWordTrim = firstWord.replaceAll("[D|d][I|i][N|n][1-9][0-9]{0,1}\\#\\#\\#", 
                                                                JsonParserConstants.EMPTY_STRING);
        if (firstWordTrim.length() != 0)
        {
            firstWordTrim = firstWordTrim.replaceAll("[D|d][I|i][N|n][1-9][0-9]{0,1}", 
                                                                JsonParserConstants.EMPTY_STRING);
        }
        if (firstWordTrim.length() == 0)
        {
              secondWord = valueMap.get(firstWord);
              LOGGER.debug("Retrieved secondWord = {} for firstWord = {}", secondWord, firstWord);
        }
        
        // Now get the number index of a given DIN 
        int dscIndex = -1;
        int fieldIndex = firstWord.indexOf(sigFieldPrefix);
        fieldIndex += sigFieldPrefix.length();
        
        char secondDigit = 'A';
        boolean seconddigitfound = false;
        
        char firstDigit = (char) (firstWord.charAt(fieldIndex) - '0');

        if (fieldIndex < firstWord.length() - 1)
        { 
            secondDigit = firstWord.charAt(fieldIndex + 1);
            if (secondDigit >= '0' && secondDigit <= '9')
            {
                secondDigit = (char) (secondDigit - '0');
                seconddigitfound = true; 
            }
        }
        dscIndex = (seconddigitfound) ? (firstDigit * 10 + secondDigit) : firstDigit;
        
        String keyName = sigFieldPrefix + dscIndex;
        
        if (locationLabelsFoundMap.containsKey(keyName))
        {
            LOGGER.warn("Skipping location with label = {} on page = {} at position = {} as key = "
                    + "{} already exists in the map.", firstWord, page, location.getRectangle(), 
                    keyName);
            return null;
        }
        String [] result = new String[2]; 
        result[0] = (words.length > 0) ?  keyName : JsonParserConstants.EMPTY_STRING;
        result[1] = (secondWord != null) ? secondWord : JsonParserConstants.EMPTY_STRING;

        Rectangle rect = location.getRectangle();
        LOGGER.debug("Location found = {} on page = {} : getX = {} getY = {} getWidth = {} "
                    + "getHeight = {}", location.getText(), page, rect.getX(), rect.getY(), 
                    rect.getWidth(), rect.getHeight());
        
        PDFSignatureLocationInfo currSigLocInfo = new PDFSignatureLocationInfo(
                                                        page, rect, dscIndex, result[0], result[1]);
        locationLabelsFoundMap.put(keyName, currSigLocInfo);
        return currSigLocInfo;
    }
}