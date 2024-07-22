package com.mca.pdfgen.beans.pdf;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PdfMetaData 
{
    private String form;
    private String srn;
    private String documentType;
    private String module;
    private String userid;
    private String applicationId;
    private String attachmentCategory;
    private String integrationId;
    private String formDescription;
    private String formVersion;
    private String formId;
    private String foldername;
    private String action;
}
