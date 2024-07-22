package com.mca.pdfgen.exception;

public class AttachmentSizeException extends PDFGenException
{
    
    public AttachmentSizeException(final String message) 
    {
        super(message);
    }

    public AttachmentSizeException(final Throwable cause) 
    {
        super(cause);
    }

}
