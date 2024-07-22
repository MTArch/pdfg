package com.mca.pdfgen.beans.dms;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormAttachment 
{
    private String attachmentLabel;
    private String attachmentCategory;
    private String activityFileName;
    private String activityFileSrcPath;
    private String activityFileSrcType;
    private String activityFileExt;
    private String activityFileSize;
    private String attachmentDMSId;
    private String totalNoOfPages;
    private String activityFileDate;
    private String publicDocument;
    private String activityComments;
    private String folderName;
    private String folderId;
    private String version;
    private String addedBy;
}
