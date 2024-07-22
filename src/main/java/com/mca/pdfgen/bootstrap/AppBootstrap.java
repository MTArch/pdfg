package com.mca.pdfgen.bootstrap;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.itextpdf.licensing.base.LicenseKey;
import com.mca.pdfgen.constants.AppConstants;

public class AppBootstrap 
{
    private static final String JASYPT = "jasypt.encryptor.password";
    private static final String JASYPT_VALUE = "hHFGMHI2IiDf1iKmcoYnGtN3Imz7qkre";

    public static void initialize()
	{
	    System.setProperty(JASYPT, JASYPT_VALUE);
		
	    loadiTextLicenseFiles();
	}

    private static void loadiTextLicenseFiles() 
    {
    	final String    licenseFilesSet = System.getenv(AppConstants.ITEXT_LICENSE_FILES);
    	final String    licensePath = System.getenv(AppConstants.LICENSE_BASE_PATH).trim();
    	
        try
        {
        	final String[]    licenseFiles = licenseFilesSet.split(AppConstants.SEMICOLON);
        	
            for (final String licenseFile : licenseFiles) 
            {
                final String    filePath = licensePath + licenseFile.trim();
                
                if (Files.exists(Paths.get(filePath)))  
                {
                    LicenseKey.loadLicenseFile(new File(filePath));
                    
                    System.out.println("Loaded license file from path = " + filePath);
                }
                else
                {
                    final String    errorMsg = "Could not Load license file from path = " 
                                               + filePath;
                    System.err.println(errorMsg);
                    throw new RuntimeException(errorMsg);
                }
            }
        }
        catch(final Exception currEx)
        {
            System.err.println("Failed to load iText license files with error: ");
            currEx.printStackTrace(System.err);
            
            throw new RuntimeException("Failed to load iText license files", currEx);
        }
    }
}
