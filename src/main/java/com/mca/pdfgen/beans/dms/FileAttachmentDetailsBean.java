package com.mca.pdfgen.beans.dms;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileAttachmentDetailsBean 
{
	private byte[] fileData;
    private String fileContentType;
    private String attachmentFileName;
}
