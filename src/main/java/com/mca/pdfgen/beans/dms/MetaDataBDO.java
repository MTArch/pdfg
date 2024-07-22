package com.mca.pdfgen.beans.dms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataBDO 
{
    private String indexId = "";
    private String indexType = "S";
    private String indexValue = "";
    private String indexAttribute = "";
    private String indexFlag = "";
    private String indexLength = "";
    private String indexName = "";
    private String usefulInfoFlag = "";
    private String usefulInfoSize = "";
}
