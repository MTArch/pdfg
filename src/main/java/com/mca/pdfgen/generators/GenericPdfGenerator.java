package com.mca.pdfgen.generators;

import java.io.ByteArrayOutputStream;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.beans.pdf.PdfContext;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.exception.PDFGenException;
import com.mca.pdfgen.utils.CommonUtils;

public abstract class GenericPdfGenerator implements PdfOutputGenerator
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericPdfGenerator.class);

    protected SysParamsConfig sysParamsConfig;

	public GenericPdfGenerator(final SysParamsConfig inSysParamsConfig)
	{
		sysParamsConfig = inSysParamsConfig;
	}
    
	@Override
	abstract public String getGeneratorId();

	@Override
	abstract public PdfGeneratorContext createContext() throws PDFGenException;
	
	@Override
    abstract public ByteArrayOutputStream createOutputPDF(final PdfContext pdfContext) 
    		                                              throws PDFGenException;
	
    protected String getTempFilesBaseDir()
    {
		return sysParamsConfig.getPdfTempLocationPath();
    }
    
	protected String saveFileToDisk(final PDFGenRequestRec request, final byte[] fileData,
			                        final String fileSuffix, final boolean logAtInfoLevel) 
	{
    	String                 result = null;
		final StringBuilder    fileNameBuilder = new StringBuilder();

		fileNameBuilder.append(getTempFilesBaseDir())
		               .append(File.separator)
				       .append(request.getSrnRef())
				       .append(AppConstants.UNDERSCORE)
				       .append(request.getSeqNo())
				       .append(fileSuffix);

		result = fileNameBuilder.toString();
		
		if (logAtInfoLevel) 
		{
			LOGGER.info("In GenericPdfGenerator.saveFileToDisk(), saving {} file " + "to disk",
					    result);
		} 
		else 
		{
			LOGGER.debug("In GenericPdfGenerator.saveFileToDisk(), saving {} file " + "to disk",
					     result);
		}
		CommonUtils.writeToFile(result, fileData);
		
		return result;
	}
}
