package com.mca.pdfgen.beans.dms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mca.pdfgen.constants.JsonParserConstants;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
public class UploadDocumentRequest 
{
    private String accessType = JsonParserConstants.ACCESS_TYPE;
    private String cabinetName = JsonParserConstants.EMPTY_STRING;
    private String comment = JsonParserConstants.EMPTY_STRING;
    private String createdByAppName = JsonParserConstants.EMPTY_STRING;
    private String documentName = JsonParserConstants.EMPTY_STRING;
    private String enableLog = JsonParserConstants.Y;
    private String encrFlag = JsonParserConstants.N;
    private String folderIndex = JsonParserConstants.EMPTY_STRING;
    private String imageData = JsonParserConstants.EMPTY_STRING;
    private String locale = JsonParserConstants.LOCALE_VALUE;
    private String nameLength = JsonParserConstants.EMPTY_STRING;
    private String ownerIndex = JsonParserConstants.EMPTY_STRING;
    private String ownerType = "U";
    private String passAlgoType = "MD5";
    private String textAlsoFlag = JsonParserConstants.EMPTY_STRING;
    private String thumbNailFlag = JsonParserConstants.N;
    private String userDBId = JsonParserConstants.EMPTY_STRING;
    private String userName = JsonParserConstants.EMPTY_STRING;
    private String versionFlag = JsonParserConstants.EMPTY_STRING;
    private String volumeId = JsonParserConstants.EMPTY_STRING;
    
    @ToString.Exclude
    private String userPassword = JsonParserConstants.EMPTY_STRING;
    
    @JsonProperty("NGOAddDocDataDefCriterionBDO")
    private DataDefCriterionBDO NGOAddDocDataDefCriterionBDO;
}
