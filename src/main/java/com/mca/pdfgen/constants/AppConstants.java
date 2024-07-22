package com.mca.pdfgen.constants;

public final class AppConstants
{
    public static final String DB_SCHEMA = "CX_SIEBEL";
    public static final String DB_TABLE_GENERATEPDF = "GENERATEPDF";
    public static final String DB_TABLE_FORM_MASTER = "FORMSIGNBATCHMSTR";
    
	public static final int FAILURE_REASON_SIZE = 499;
    
	public static final String LICENSE_BASE_PATH = "LICENSE_BASE_PATH";
	public static final String ITEXT_LICENSE_FILES = "ITEXT_LICENSE_FILE_NAME";
	
    public static final String HTTPS = "https";
    public static final String HTTP = "HTTP";
    public static final String FILE_TYPE = "pdf";
    public static final String FILE_EXT = ".pdf";
    public static final String TXT = ".txt";
    public static final String YES = "yes";
    public static final String NO = "no";
    public static final String UNDERSCORE = "_";
    public static final String SEPARATOR = "/";
    public static final String SEMICOLON = ";";
    public static final String SINGLE_SPACE = " ";
    public static final String DIN = "DIN";
    public static final String DPIN = "DPIN";
    
    public static final String HTTP_CONTENT_TYPE_JSON =  "application/json";
    public static final String HTTP_CONTENT_TYPE_URL_ENCODED = "application/x-www-form-urlencoded";
    
    public static final String ACCESS_TOKEN = "access_token";
    public static final String ACCESS_TOKEN_EXPIRES = "expires_in";
    
    // WSO2 Endpoint URL's
    public static final String FETCH_DATA_ENDPOINT_URL = "/genpdf/select/1.0.0";
    public static final String DOC_INSERT_UPLOAD_URL = "/common/service/documentInsertUpload/1.0.0";
    
    // Suffix used for storing WSO2 request in a filename if the WSO2 update fails
    public static final String WSO2REQ_SUFFIX = "_wso2_req";

    // DMS Related Constants & Endpoint URLs
    public static final String DMS_EXEC_API_ENDPOINT = "/executeAPIJSON";
    public static final String DMS_GET_DOC_ENDPOINT = "/getDocumentJSON";
    public static final String DMS_ADD_DOC_ENDPOINT = "/addDocumentJSON";
    
    // DMS Folder MetaData Class Name
    public static final String FOLDER_DATA_CLASS_NAME = "F_ServiceRequest_Complaints";
    
    // DMS Document MetaData Class Name
    public static final String DOCUMENT_DATA_CLASS_NAME = "D_DocumentMetaData";

    // DMS used for CSR Data Definition 
    public static final String DATA_DEF_PORTAL_REPORT = "Portal_Report";
    
    public static final String SRN = "SRN";
    public static final String V3 = "V3";
    public static final String MCA = "MCA";
    
    public static final String YEAR = "year";
    public static final String QTR =  "qtr";
    public static final String MONTHDATE = "monthdate";
   
    public static final String FOLDER_TYPE_COMPLIANCES = "Compliances";
    public static final String FOLDER_TYPE_SERVICE_REQUEST = "Service Request";         
    public static final String UNIQUE_ID = "dorUpdated";    
    
    public static final String BATCH_MODE_NORMAL = "NORMAL";
    public static final String BATCH_MODE_RETRY = "RETRY";
    
    // Maximum number of signature boxes that can be affixed in a form
    public static final int MAX_DSC_COUNT = 50;
    
    public static final String AGILE_DEFAULT_SIGFIELD_TEXT = "*To be digitally signed by director ";
    public static final String DIR3_DEFAULT_SIGFIELD_TEXT  = "Certification";
    
    // HTML 2 PDF Conversion Constants
    public static final int PDF_BUFFER_SIZE = 1024 * 5000;
    public static final String THYMELEAF_CONTEXT_VARIABLE_LOGO = "img";
    public static final String THYMELEAF_CONTEXT_VARIABLE_JSON_METADATA = "metadata";
    public static final String THYMELEAF_CONTEXT_VARIABLE_JSON_DATA = "data";
    public static final String THYMELEAF_CONTEXT_VARIABLE_FONT = "fonts";
    public static final String THYMELEAF_CONTEXT_VARIABLE_JS = "jsContents";
    public static final String THYMELEAF_CONTEXT_VARIABLE_CSS = "cssContents";
    public static final String HTML_TEMPLATE_FONT_URI = "/html/font/";
    public static final String HTML_TEMPLATE_LOGO_URI = "/html/img/logo.base64.txt";
    public static final String HTML_TEMPLATE_JS_URI = "/html/js/";
    public static final String HTML_TEMPLATE_CSS_URI = "/html/css/";
    public static final String DATA_URL_IMAGE_PREFIX = "data:image/png;base64,";
    public static final String DATA_URL_HTML_PREFIX = "data:text/html;charset=utf-8,";
    public static final String DATA_URL_FONT_PREFIX = "data:font/ttf;charset=utf-8;base64,";
    public static final String FONT_CSS_PREFIX = "@font-face {\r\n"
        + "font-family: 'Myriad Pro';\r\nfont-style:normal;\r\n"
        + "font-weight:400;\r\nsrc: url(";
    public static final String FONT_CSS_POSTFIX = ");}";
    public static final String[] CHROME_DRIVER_CONFIG =
        { "--headless", "--silent", "--run-all-compositor-stages-before-draw" };
	public static final String THYMELEAF_CONTEXT_VARIABLE_DIRECTOR = "director";
	public static final Object DIRECTOR = "Director";
	
	// Maximum file attachments size = 10 MB
	public static final long ATTACHMENT_FILE_SIZE = 10485760;
}
