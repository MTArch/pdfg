package com.mca.pdfgen.generators;

import java.io.ByteArrayOutputStream;

import com.mca.pdfgen.beans.pdf.PdfContext;
import com.mca.pdfgen.exception.PDFGenException;

public interface PdfOutputGenerator 
{
	String getGeneratorId();

	PdfGeneratorContext createContext() throws PDFGenException;

	ByteArrayOutputStream createOutputPDF(PdfContext pdfContext) throws PDFGenException;
}
