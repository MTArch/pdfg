package com.mca.pdfgen.generators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.forms.PdfAcroForm;
import com.itextpdf.forms.xfa.XfaForm;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener;
import com.itextpdf.tool.xml.xtra.xfa.font.XFAFontSettings;
import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.beans.pdf.PdfContext;
import com.mca.pdfgen.configs.FormsConfigManager;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.exception.PDFGenException;

public class XfaTemplate2PdfGenerator extends GenericPdfGenerator 
{
    private static final int PDF_BUFFER_SIZE = 1024 * 5000;
    private static final Logger LOGGER = LoggerFactory.getLogger(XfaTemplate2PdfGenerator.class);
    
    public XfaTemplate2PdfGenerator(final SysParamsConfig inSysParamsConfig)
    {
        super(inSysParamsConfig);
    }
    
    @Override
    public String getGeneratorId()
    {
        return "XFA2PDF";
    }

    @Override
    public PdfGeneratorContext createContext() 
    {
        return new PdfGeneratorContext(new HashMap<String, Object>());
    }

    @Override
    public ByteArrayOutputStream createOutputPDF(PdfContext pdfContext) throws PDFGenException
    {
        ByteArrayOutputStream     result = null;
        final PDFGenRequestRec    request = pdfContext.getPdfGenReq();

        try
        {
            LOGGER.debug("In XfaTemplate2PdfGenerator.createOutputPDF(), starting creation of "
                         + "flattened PDF document for SRN = {} and SEQ = {}", request.getSrnRef(), 
                         request.getSeqNo());

            final byte[]                   templatePdf = pdfContext.getFormConfig().
                                                                 getTemplateFileContent();
            final ByteArrayOutputStream    templatePdfOutStream = 
                                                  new ByteArrayOutputStream(PDF_BUFFER_SIZE);

            // Filled Form based PDF Document to be created based on PDF Template
            final PdfDocument filledFormPdf = new PdfDocument(
                                       new PdfReader(new ByteArrayInputStream(templatePdf)),
                                       new PdfWriter(templatePdfOutStream));

            // Get the XfaForm object of the template PDF document from AcroForm
            final XfaForm    xfaForm = PdfAcroForm.getAcroForm(filledFormPdf, true).getXfaForm();
            
            // Pre-process input data json if required
            final byte[]    inputDataJsonBytes = preprocessDataJson(pdfContext);
            
            // Create an input stream of incoming form data from database formatted as a decoded
            // JSON with the Form tag
            final InputStream    inputJsonDataStream = new ByteArrayInputStream(inputDataJsonBytes);

            // Fill the XFA Form with XFA data provided by the input stream 
            xfaForm.fillXfaForm(inputJsonDataStream);
            
            // Write the XFAForm to the output PDF document 
            xfaForm.write(filledFormPdf);
            filledFormPdf.close();

            LOGGER.debug("In createFlattenedPDF(), Form data filled successfully in PDF template "
                         + "for SRN = {} and SEQ = {}", request.getSrnRef(), request.getSeqNo());
            
            if (sysParamsConfig.isSaveBeforeFlattening())
            {
                saveFileToDisk(request, templatePdfOutStream.toByteArray(), 
                               "_unflattened.pdf", false);
            }
            final ByteArrayOutputStream flattendedPdfOutStream = 
                                                  new ByteArrayOutputStream(PDF_BUFFER_SIZE);
                
            final XFAFontSettings    fontSettings = new XFAFontSettings();
            
            fontSettings.setUseDocumentNotXFAFonts(true);
            fontSettings.setEmbedExternalFonts(true);
                
            final XFAFlattener    xfaFlattener = new XFAFlattener();
            
            xfaFlattener.setFontSettings(fontSettings);
            xfaFlattener.flatten(new ByteArrayInputStream(templatePdfOutStream.toByteArray()), 
                                 flattendedPdfOutStream);

            LOGGER.debug("In XfaTemplate2PdfGenerator.createOutputPDF(), Form filled PDF File "
                         + "flattened successfully for SRN = {} and SEQ = {}", request.getSrnRef(), 
                         request.getSeqNo());
       
            // Set up the flattened PDF Output stream as the result to be returned
            result = flattendedPdfOutStream;
            
            if (sysParamsConfig.isSaveAfterFlattening())
            {
                saveFileToDisk(request, flattendedPdfOutStream.toByteArray(), 
                               "_flattened.pdf", false);
            }
        }
        catch (final Exception currEx)
        {
            final String errorMsg = "Failed in XfaTemplate2PdfGenerator.createOutputPDF() with "
                                    + "SRN = " + request.getSrnRef() + ", Seq = " + 
                                    request.getSeqNo() + ". ";
  
            PDFGenException.mapAndThrow(currEx, errorMsg);
        }
        return result;
    }
    
    byte[] preprocessDataJson(final PdfContext pdfContext)
    {
        final String formName = pdfContext.getFormConfig().getRequestFormName();
        
        if (! FormsConfigManager.AGILEPRO_REQ_FORMNAME.equals(formName) && 
            ! FormsConfigManager.FORM_11_REQ_FORMNAME.equals(formName))
        {
            return XML.toString(pdfContext.getFormattedDataJson()).getBytes(StandardCharsets.UTF_8);
        }

        final JSONObject    dataJson = new JSONObject(pdfContext.getFormDataStr());
        final String[]      checkBoxNames = {
                                                "checkbox_7887388851648095563134", 
                                                "checkbox_11834367961648095592490", 
                                                "checkbox_11599904391648095610265", 
                                                "checkbox_5009260951648095617989", 
                                                "checkbox1648095322969", 
                                                "checkbox_7296771691648095625721", 
                                                "checkbox_1088120651648095633073", 
                                                "checkbox_16161811151648095639471", 
                                                "checkbox_17827682041648095652964", 
                                                "checkbox_8461577941648095660209", 
                                                "checkbox_13315544521648095667940", 
                                                "checkbox_5174644481648095691627", 
                                                "checkbox_12594053241648095674958", 
                                                "checkbox_16996044791648095698510", 
                                                "checkbox1648094766311", 
                                                "checkbox_9624268501648095169175", 
                                                "checkbox_20761176291648095223631"
                                            };
        
        final String[]      form11UnboundMap = {
                                            "categoryIndividualNoOfPartners",
                                            "IndividualNumOfDPResidentInIndia",
                                            "IndividualNumOfDPOthers",
                                            "IndividualTotal",
                                            "categoryLLPNoOfPartners",
                                            "categoryLLPNoOfDPResidentInIndia",
                                            "categoryLLPNoOfDPOthers",
                                            "categoryLLPTotal",
                                            "categoryCompaniesNoOfPartners",
                                            "categoryCompaniesNoOfDPResidentIndia",
                                            "categoryCompaniesNoOfDPOthers",
                                            "categoryCompaniesTotal",
                                            "categoryForeignLLPsNoOfPartners",
                                            "categoryForeignLLPsNoOfDPInIndia",
                                            "categoryForeignLLPsNoOfDPOthers",
                                            "categoryForeignLLPsTotal",
                                            "categoryForeignCompaniesNoOfPartners",
                                            "categoryForeignCompaniesNoOfDPInIndia",
                                            "categoryForeignCompaniesNoOfDPOthers",
                                            "categoryForeignCompaniesTotal",
                                            "categoryLLPsOutsideIndiaNoOfPartners",
                                            "categoryLLPsOutsideIndiaNoOfDPInIndia",
                                            "categoryLLPsOutsideIndiaNoOfDPOthers",
                                            "categoryLLPsOutsideIndiaTotal",
                                            "categoryCompaniesOutsideIndiaAndSikkimNoOfPartners",
                                            "categoryCompaniesOutsideIndiaAndSikkimNoOfDPInIndia",
                                            "categoryCompaniesOutsideIndiaAndSikkimNoOfDPOthers",
                                            "categoryCompaniesOutsideIndiaAndSikkimTotal",
                                            "categoryTotalNoOfPartners",
                                            "categoryTotalNoOfDPInIndia",
                                            "categoryTotalNoOfDPOthers",
                                            "categoryTotalFinal",
                                            "rowsRequired_10a",
                                            "designatedPartnerrowsRequired",
                                            "compoundingOffencerowsRequired",
                                            "turnoverOfTheLLPExceeds5Cr",
                                            "attachmentVerificationConsent",
                                            "nameOfPersonSigning",
                                            "designationOfPersonSigning",
                                            "idOfPersonSigning",
                                            "annualReturnCertify",
                                            "dpinDesignatedPartnerTextbox",
                                            "indInstanceCount",
                                            "bodyCorpInstanceCount",
                                            "whetherAssociateorFellowRButton",
                                            "certiPracticeNoBox",
                                            "herebyCertified",
                                            "certifiedHerebyLLPname"
                                        };

        final JSONObject    boundedDataJson = pdfContext.getFormattedDataJson()
                                                    .getJSONObject(JsonParserConstants.FORM1TAG);

        final JSONObject    unboundedDataJson = dataJson
                                               .getJSONObject(JsonParserConstants.AFDATA)
                                               .getJSONObject(JsonParserConstants.AFUNBOUNDDATA)
                                               .getJSONObject(JsonParserConstants.DATA);
        
        if (FormsConfigManager.AGILEPRO_REQ_FORMNAME.equals(
                                            pdfContext.getFormConfig().getRequestFormName()))
        {
            for (final String currName : checkBoxNames) 
            {
                String    foundName = "0";
                
                if (unboundedDataJson.has(currName))
                {
                    final String    val = unboundedDataJson.getString(currName);

                    if (val != null && val.length() > 0)
                    {
                        foundName = val;
                    }
                }
                boundedDataJson.put(currName, foundName);
            }
        }
        if (FormsConfigManager.FORM_11_REQ_FORMNAME.equals(
                                                pdfContext.getFormConfig().getRequestFormName()))
        {
            for (final String currName : form11UnboundMap) 
            {
                String    foundName = JsonParserConstants.EMPTY_STRING;

                if (unboundedDataJson.has(currName))
                {
                    final Object    val = unboundedDataJson.get(currName);

                    if (val != null && val.toString().length() > 0)
                    {
                        foundName = val.toString();
                    }
                }
                boundedDataJson.put(currName, foundName);
            }
        }
        LOGGER.debug("Json being returned = \n {}", pdfContext.getFormattedDataJson());
        
        return XML.toString(pdfContext.getFormattedDataJson()).getBytes(StandardCharsets.UTF_8);
    }
}