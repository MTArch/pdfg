package com.mca.pdfgen.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFGenException extends Exception
{
	private static final long serialVersionUID = 8634427070005861638L;
	private static final Logger LOGGER = LoggerFactory.getLogger(PDFGenException.class);

	public PDFGenException(final String message) 
	{
		super(message);
	}

	public PDFGenException(final Throwable cause) 
	{
		super(cause);
	}

	public PDFGenException(final String message, final Throwable cause) 
	{
		super(message, cause);
	}
	
    public static void mapAndThrow(final Exception currEx) throws PDFGenException
    {
        if (currEx instanceof PDFGenException)
        {
            throw (PDFGenException) currEx;
        }
        throw new PDFGenException("Nested Cause : " + currEx.getMessage(), currEx);
    }
    
    public static void mapAndThrow(final Exception currEx, final String errorMsg)
                                   throws PDFGenException
    {
        final String    message = (errorMsg != null) ? errorMsg : "";
 
        LOGGER.error(message + "Cause : " + currEx.getMessage());
        
        mapAndThrow(currEx);
    }
}
