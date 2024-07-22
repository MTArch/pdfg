package com.mca.pdfgen.beans.dms;

import com.mca.pdfgen.constants.JsonParserConstants;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NGOExecuteAPIBDO
{
    public InputData inputData = new InputData();
    public String base64Encoded = JsonParserConstants.N;
    public String locale = JsonParserConstants.LOCALE_VALUE;
}
