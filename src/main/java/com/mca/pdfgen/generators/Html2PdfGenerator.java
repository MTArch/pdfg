package com.mca.pdfgen.generators;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.UrlEscapers;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.geom.Point;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.PdfCanvasProcessor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.IPdfTextLocation;
import com.itextpdf.kernel.pdf.canvas.parser.listener.RegexBasedLocationExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.beans.pdf.PdfContext;
import com.mca.pdfgen.beans.pdf.PdfMetaData;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.exception.PDFGenException;

public class Html2PdfGenerator extends GenericPdfGenerator implements PdfOutputGenerator
{
	public static final String REGEX_PAGE_NUMBER = "Page X of Y";
	private static final Logger LOGGER = LoggerFactory.getLogger(Html2PdfGenerator.class);

	public Html2PdfGenerator(final SysParamsConfig inSysParamsConfig)
	{
        super(inSysParamsConfig);
	}

	@Override
	public String getGeneratorId()
	{
		return "HTML2PDF";
	}

	@Override
	public PdfGeneratorContext createContext() throws PDFGenException
	{
		final Html2PdfGeneratorContext    result = new Html2PdfGeneratorContext();

		result.setJsonObjectMapper(new ObjectMapper());
		result.setThymeleafTemplateEngine(initializeThymeleafEngine());
		result.setThymeleafContext(initializeThymeleafContext());
		result.setChromeWebDriver(initializeChromeDriver());

		return result;
	}

	@Override
	public ByteArrayOutputStream createOutputPDF(final PdfContext pdfContext) 
			                                     throws PDFGenException
	{
		final PDFGenRequestRec            request = pdfContext.getPdfGenReq();
		final String                      dataJson = pdfContext.getFormDataStr();
		final String                      metaDataJson = pdfContext.getMetaDataStr();
		final Class<?>                    formModel = pdfContext.getFormConfig().getFormModel();

		ByteArrayOutputStream result = null;

		LOGGER.debug("In Html2PdfGenerator.createOutputPDF(), starting creation of flattened "
				     + "PDF document for SRN = {} and SEQ = {}", request.getSrnRef(), 
				     request.getSeqNo());

		final Html2PdfGeneratorContext    generatorContext = (Html2PdfGeneratorContext) 
				                                                pdfContext.getGeneratorContext();
		try
		{
			LOGGER.debug("In Html2PdfGenerator.createOutputPDF() dataJson = \n{}", dataJson);
			

			final TemplateEngine    engine = generatorContext.getThymeleafTemplateEngine();
			final String            template = pdfContext.getFormConfig().getTemplateFileName();
			final Context           thymeleafContext = populateThymeleafContext(
					                       generatorContext, metaDataJson, dataJson, formModel);
			final String            generatedHTML = engine.process(template, thymeleafContext);
			
			if (sysParamsConfig.isSaveBeforeFlattening())
			{
				saveFileToDisk(request, generatedHTML.getBytes(), "_unflattened.html", false);
			}
			// Generate PDF using the published HTML file into a byte array input stream
			result = decoratePDF(generatePDFFromHTML(generatedHTML, generatorContext));
		} 
		catch (final Exception currEx)
		{
			final String errorMsg = "Failed in Html2PdfGenerator.createOutputPDF() with " 
			                        + "SRN = " + request.getSrnRef() + ", Seq = " 
					                + request.getSeqNo() + ". ";
			PDFGenException.mapAndThrow(currEx, errorMsg);
		}
		return result;
	}

	private Context initializeThymeleafContext() throws PDFGenException
	{
		final Context    result = new Context();

		// Load the common CSS, JavaScript, images and font contents to be included during 
		// HTML page generation
		final List<String>    css = loadFolderContents(sysParamsConfig.templatesBasePath
										+ AppConstants.HTML_TEMPLATE_CSS_URI);
		final List<String>     js = loadFolderContents(sysParamsConfig.templatesBasePath
										+ AppConstants.HTML_TEMPLATE_JS_URI);
		final String    	image = loadFileContents(sysParamsConfig.templatesBasePath
										+ AppConstants.HTML_TEMPLATE_LOGO_URI);
		final List<String>    base64Font = loadBase64EncodedFileContents(sysParamsConfig.templatesBasePath
										+ AppConstants.HTML_TEMPLATE_FONT_URI);

		// Set context variables in Thymeleaf context.
		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_CSS, css);
		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_LOGO, 
				           AppConstants.DATA_URL_IMAGE_PREFIX + image);
		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_FONT, 
				           AppConstants.FONT_CSS_PREFIX + AppConstants.DATA_URL_FONT_PREFIX 
				           + base64Font + AppConstants.FONT_CSS_POSTFIX);
		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_JS, js);
		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_DIRECTOR, AppConstants.DIRECTOR);

		return result;
	}

	private List<String> loadBase64EncodedFileContents(final String staticContentPath)
		                                         throws PDFGenException
	{
		try
		{
			final List<String> files = new ArrayList<>();
			
			for (File file: new File(staticContentPath).listFiles()) 
			{
				if(!file.isDirectory())
				{
					files.add(Base64.getEncoder().encodeToString(Files.readAllBytes(file.toPath())));
				}
			}
			
			return files;
		} 
		catch (final Exception currEx)
		{
			final String    errorMsg = "Failed to initalize Html2PdfGeneratorContext with "
					                   + "Base64 encoded file contents from from file path " 
					                   + staticContentPath;
			PDFGenException.mapAndThrow(currEx, errorMsg);
		}
		return null;
	}

	private String loadFileContents(final String staticContentPath) throws PDFGenException
	{

		try
		{	
			
			return Files.readString(Path.of(staticContentPath));
		} 
		catch (final Exception currEx)
		{
			final String    errorMsg = "Failed to initalize Html2PdfGeneratorContext with "
					                   + "injectable file contents from file path " 
					                   + staticContentPath;
			PDFGenException.mapAndThrow(currEx, errorMsg);
		}
		return null;
	}

	private List<String> loadFolderContents(final String staticContentPath) throws PDFGenException
	{
		try
		{
			final List<String> files = new ArrayList<>();
			
			for (File file: new File(staticContentPath).listFiles()) 
			{
				if(!file.isDirectory())
				{
					files.add(Files.readString(file.toPath()));
				}
			}
			
			return files;
		} 
		catch (final Exception currEx)
		{
			final String    errorMsg = "Failed to initalize Html2PdfGeneratorContext with "
					                   + "injectable file contents from file path " 
					                   + staticContentPath;
			PDFGenException.mapAndThrow(currEx, errorMsg);
		}
		return null;
	}
	
	private WebDriver initializeChromeDriver()
	{
		if (System.getProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY) == null)
		{
			System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, 
					           sysParamsConfig.getChromeDriverPath());
		}
		final ChromeOptions    options = new ChromeOptions();
		
		options.addArguments(AppConstants.CHROME_DRIVER_CONFIG);
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		
		return new ChromeDriver(options);
	}

	private TemplateEngine initializeThymeleafEngine()
	{
		final TemplateEngine          result = new TemplateEngine();
		final FileTemplateResolver    templateResolver = new FileTemplateResolver();
		
		templateResolver.setPrefix(sysParamsConfig.templatesBasePath);
		templateResolver.setTemplateMode(TemplateMode.HTML);
		
		result.setTemplateResolver(templateResolver);
		
		return result;
	}

	private Context populateThymeleafContext(final Html2PdfGeneratorContext context,
		                                     final String metaDataJson, final String dataJson, 
		                                     final Class<?> formModel)
		                                     throws JsonProcessingException, JsonMappingException
	{
		final Context         result = context.getThymeleafContext();
		final ObjectMapper    objMapper = context.getJsonObjectMapper();

		// Map dataJSON and metadataJSON to form specific model object and metadata
		// model object
		final Object data = objMapper.readValue(dataJson, formModel);
		final PdfMetaData metadata = objMapper.readValue(metaDataJson, PdfMetaData.class);

		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_JSON_DATA, data);
		result.setVariable(AppConstants.THYMELEAF_CONTEXT_VARIABLE_JSON_METADATA, metadata);

		return result;
	}

	private ByteArrayInputStream generatePDFFromHTML(final String generatedHTML,
		                                             final Html2PdfGeneratorContext context)
	{
		final Map<String, Object>    params = new HashMap<>();
		final String                 command = "Page.printToPDF";
		final WebDriver              driver = context.getChromeWebDriver();

		driver.get(AppConstants.DATA_URL_HTML_PREFIX 
				   + UrlEscapers.urlFragmentEscaper().escape(generatedHTML));

		// Set parameters for printing 
		params.put("landscape", false);
		params.put("paperWidth", 8.3);
		params.put("paperHeight", 11.7);

		// Execute the print command 
		final Map<String, Object>    output = ((ChromiumDriver) driver).executeCdpCommand(
				                                                               command, params);

		// Return pdf contents as byte array output stream
		return new ByteArrayInputStream(Base64.getDecoder().decode((String) output.get("data")));
	}
	
	private ByteArrayOutputStream decoratePDF(final ByteArrayInputStream sourcePDF) 
                                              throws IOException 
	{
		final ByteArrayOutputStream    result = new ByteArrayOutputStream(
		                                  AppConstants.PDF_BUFFER_SIZE);
		final PdfDocument              pdfDoc = new PdfDocument(new PdfReader(sourcePDF), 
		                               new PdfWriter(result));
		final Document                 doc = new Document(pdfDoc);
		final int                      numberOfPages = pdfDoc.getNumberOfPages();
			  Point				            location = null;
		
		for (int pageIndex = 1; pageIndex <= numberOfPages; pageIndex++) 
		{
			final PdfPage      page = pdfDoc.getPage(pageIndex);
			
			// Assumption all pages are of same size and the position marker for page x of y is the
			// same across all the pages, so essentially we take the position from the first page
			// and use the same for all pages.
			if (1 == pageIndex) 
			{
				location = getPageNumberLocation(doc, page);
			}
			
			addPageNumber(doc, numberOfPages, pageIndex, location);
		}
		pdfDoc.close();
		doc.close();
		
		return result;
	}


	private Point getPageNumberLocation(final Document doc, final PdfPage page)
	{

		final RegexBasedLocationExtractionStrategy strategy = 
											new RegexBasedLocationExtractionStrategy(REGEX_PAGE_NUMBER);
		final PdfCanvasProcessor 				  processor = new PdfCanvasProcessor(strategy);
		
		float    textStartY;
		float    textStartX;

		processor.processPageContent(page);
		
		Collection<IPdfTextLocation> locations = strategy.getResultantLocations();

		// X,Y coordinates 0,0 represents the bottom left corner of the page
		// The X,Y coordinates determined here are used as the coordinates of the bottom left corner
		// of the actual page number printing.
		if (locations == null || locations.isEmpty()) 
		{
			// If Regex was not found on a given page the collection size will be 0, i.e. marker is not
			// defined on a template and we should use default position for the marker.
			textStartY = page.getPageSize().getBottom() + 23;
			textStartX = page.getPageSize().getRight() - doc.getRightMargin();

		} else {
			
			IPdfTextLocation location = locations.stream().findFirst().get();
			textStartY = location.getRectangle().getBottom();
			textStartX = location.getRectangle().getLeft();
		}

		return  new Point(textStartX, textStartY);
	}

	private void addPageNumber(final Document doc, final int numberOfPages, final int pageNo,
							   final Point pageNumberLocation) 
	{
		doc.setFontSize(9).showTextAligned(new Paragraph(
			        	String.format("Page %s of %s", pageNo, numberOfPages)), 
			        	(float)pageNumberLocation.x, 
			        	(float)pageNumberLocation.y, 
			        	pageNo, 
			        	TextAlignment.LEFT, 
			        	VerticalAlignment.MIDDLE, 
			        	0);
	}
}
