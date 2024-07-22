package com.mca.pdfgen.beans.pdf;

import java.io.ByteArrayOutputStream;

import org.json.JSONObject;

import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.configs.FormsConfig;
import com.mca.pdfgen.generators.PdfGeneratorContext;
import com.mca.pdfgen.generators.PdfOutputGenerator;
import com.mca.pdfgen.metrics.FormMetrics;

import lombok.Data;

@Data
public class PdfContext 
{
    private boolean retryMode;
    private String formDataStr;
    private String metaDataStr;
    private FormMetrics metricsRec;
    private FormsConfig formConfig;
    private PDFGenRequestRec pdfGenReq;
    private PdfOutputGenerator generator;
    private JSONObject formattedDataJson;
	private byte[] wso2InputReqFileContents;
	private byte[] generatedPdfFileContents;
	private PdfGeneratorContext generatorContext;
	private ByteArrayOutputStream pdfWithDSCsOutStream;
}
