package com.mca.pdfgen.beans.dms;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mca.pdfgen.constants.JsonParserConstants;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@JsonPropertyOrder({
	"CabinetName",
	"Option",
	"UserExist",
	"UserName",
	"UserPassword",
	"locale"
})
public class NGOConnectCabinetInput
{
    @JsonProperty("Option") 
    public String option = JsonParserConstants.NGOCONNECTCABINET;
    
    @JsonProperty("UserExist") 
    public String userExist = JsonParserConstants.N;
    
    @JsonProperty("CabinetName") 
    public String cabinetName;
    
    @JsonProperty("UserName") 
    public String userName;
    
    @ToString.Exclude
    @JsonProperty("UserPassword")
    public String userPassword;
    
    public String locale = JsonParserConstants.LOCALE_VALUE;
}
