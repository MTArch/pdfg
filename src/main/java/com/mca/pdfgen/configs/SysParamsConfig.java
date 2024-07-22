package com.mca.pdfgen.configs;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.mca.pdfgen.utils.CommonUtils;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@Configuration
public class SysParamsConfig 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(SysParamsConfig.class);
    
    @Value("#{environment.WSO2_API_PROTOCOL}")
    public String wso2APIProtocol;

    @Value("#{environment.WSO2_SSL_KEYSTORE_FILEPATH}")
    public String wso2SslKeyFilePath;
    
    @Value("#{environment.WSO2_TOKEN}")
    public String wso2Token;
    
    @Value("#{environment.WSO2_TOKEN_URL}")
    public String wso2TokenUrl;
    
    @Value("${pdfgenbatch.WSO2Username}")
    public String wso2UserName;
    
    @ToString.Exclude
    @Value("${pdfgenbatch.WSO2Password}")
    public String wso2Password;
    
    @Value("#{environment.WSO2_GRANT_TYPE}")
    public String wso2GrantType;
    
    @Value("#{environment.WSO2_ENDPOINT_IP}")
    public String wso2EndpointHostIp;

    @Value("${pdfgenbatch.DMSUsername}")
    public String dmsUserName;
    
    @ToString.Exclude
    @Value("${pdfgenbatch.DMSPassword}")
    public String dmsPassword;
    
    @Value("#{environment.DMS_CABINET_NAME}")
    public String dmsCabinetName;
    
    @Value("#{environment.DMS_TOKEN_URL}")
    public String dmsTokenUrl;

    @Value("#{environment.DMS_SSL_KEYSTORE_FILEPATH}")
    public String dmsSslKeyFilePath;
       
    @Value("#{environment.DMS_TOKEN_REFRESH_INTERVAL_MS}")
    public int dmsTokenRefreshInterval;

    @Value("#{environment.TEMPLATES_BASE_PATH}")
    public String templatesBasePath;
    
    @Value("#{environment.PDF_TEMP_LOCATION_PATH}")
    public String pdfTempLocationPath;
    
    @Value("#{environment.BATCHRETRYCOUNT ?:3}")
    private int batchRetryCount;

    @Value("${pdfgen.dms.folderDataClassNameId}")
    private String folderDataClassNameId;

    @Value("${pdfgen.dms.documentDataClassNameId}")
    private String documentDataClassNameId;
    
    @Value("${pdfgen.testRun:false}")
    private boolean testRun;
    
    @Value("${pdfgen.saveBeforeFlattening:false}")
    private boolean saveBeforeFlattening;

    @Value("${pdfgen.saveAfterFlattening:false}")
    private boolean saveAfterFlattening;
    
    @Value("#{environment.CHROME_DRIVER_PATH}")
    private String chromeDriverPath;
       
    @Value("${pdfgen.networkTimeout}")
    private int networkTimeout;

    public void printConfigParams()
    {
        LOGGER.info("Startup config Params {}", this.toString());
    }

    public boolean validateConfigParams()
    {
        LOGGER.info("System Configuration Parameters validation started ...");
        
        if (isInvalidFilePath(chromeDriverPath)
        	|| isInvalidFilePath(dmsSslKeyFilePath)
        	|| isInvalidFilePath(wso2SslKeyFilePath)
        	|| templatesBasePath == null 
        	|| pdfTempLocationPath == null 
        	|| folderDataClassNameId == null 
        	|| documentDataClassNameId == null 
        	|| !CommonUtils.directoryExists(templatesBasePath) 
        	|| !CommonUtils.directoryExists(pdfTempLocationPath))
        {
            LOGGER.error("One or more System Configuration Parameters is not valid : " + this);
        	return false;
        }
        LOGGER.info("System Configuration Parameters validation completed successfully.");
        return true;
    }
    
    private boolean isInvalidFilePath(String filePath) {
        boolean retval = ( filePath == null)  || !Files.exists(Paths.get(filePath));
        LOGGER.debug("Validating file {}, success {}",filePath,retval);
        return retval;
    }
}
