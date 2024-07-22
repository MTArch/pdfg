package com.mca.pdfgen.beans.dms;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mca.pdfgen.constants.JsonParserConstants;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "cabinetName",
    "docIndex",
    "siteId",
    "volumeId",
    "userName",
    "userPassword",
    "userDBId",
    "locale",
    "passAlgoType",
    "encrFlag"
})
@Data
@NoArgsConstructor
public class NGOGetDocumentBDO 
{
    @JsonProperty("cabinetName")
    public String cabinetName;
    
    @JsonProperty("docIndex")
    public String docIndex;
    
    @JsonProperty("userDBId")
    public String userDBId;
    
    @JsonProperty("siteId")
    public String siteId = JsonParserConstants.ONE;
    
    @JsonProperty("volumeId")
    public String volumeId = JsonParserConstants.ONE;
    
    @JsonProperty("userName")
    public String userName = JsonParserConstants.EMPTY_STRING;
    
    @JsonProperty("userPassword")
    @ToString.Exclude
    public String userPassword = JsonParserConstants.EMPTY_STRING;
    
    @JsonProperty("locale")
    public String locale = JsonParserConstants.LOCALE_VALUE;
    
    @JsonProperty("passAlgoType")
    public String passAlgoType = JsonParserConstants.EMPTY_STRING;
    
    @JsonProperty("encrFlag")
    public String encrFlag = JsonParserConstants.EMPTY_STRING;
}
