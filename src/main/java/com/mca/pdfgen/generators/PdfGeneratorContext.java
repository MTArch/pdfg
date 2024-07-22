package com.mca.pdfgen.generators;

import java.util.Map;

import lombok.Data;

@Data
public class PdfGeneratorContext 
{
    private final Map<String, Object> valuesMap;

	public void cleanUp()
	{
		// No cleanup required at this point
	}
}
