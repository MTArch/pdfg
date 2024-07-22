package com.mca.pdfgen.beans.dms;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataDefCriterionBDO 
{
	private String dataDefName = "";
    private String dataDefIndex = "";
    
    @JsonProperty("NGOAddDocDataDefCriteriaDataBDO")
    private List<MetaDataBDO> NGOAddDocDataDefCriteriaDataBDO;
}
