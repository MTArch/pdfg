package com.mca.pdfgen.generators;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class Html2PdfGeneratorContext extends PdfGeneratorContext 
{
	private Context thymeleafContext;
	private WebDriver chromeWebDriver;
	private ObjectMapper jsonObjectMapper;
	private TemplateEngine thymeleafTemplateEngine;

	public Html2PdfGeneratorContext() 
	{
		super(new HashMap<String, Object>());
	}

	@Override
	public void cleanUp()
	{
		if(chromeWebDriver != null) 
		{
			chromeWebDriver.quit();
			chromeWebDriver = null;
		}
	}
}
