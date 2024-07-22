package com.mca.pdfgen.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mca.pdfgen.beans.LogTimer;
import com.mca.pdfgen.helpers.DBHelper;
import com.mca.pdfgen.helpers.DMSHelper;
import com.mca.pdfgen.helpers.WSO2Helper;

public final class CommonUtils 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtils.class);
	private static final String REMOVE_SPCL_CHAR_REGEX = "[^a-zA-Z 0-9-_.!\t)]";
	
    public static void trustAllHosts()
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] 
        { 
            new X509TrustManager() 
            {
                public X509Certificate[] getAcceptedIssuers() 
                {
                    return new X509Certificate[]{};
                }

                @Override
                public void checkClientTrusted(X509Certificate[] paramArrayOfX509Certificate, 
                                               String paramString) throws CertificateException 
                {
                	return;
                }

                @Override
                public void checkServerTrusted(X509Certificate[] paramArrayOfX509Certificate, 
                                               String paramString) throws CertificateException 
                {
                    return;
                }
            } 
        };

        // Install the all-trusting trust manager
        try 
        {
            final SSLContext    sslContext = SSLContext.getInstance("TLSv1.2");
            
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(NoopHostnameVerifier.INSTANCE);
        } 
        catch (final Exception currEx) 
        {
        	LOGGER.error("Caught an exception while creating a Trust Manager: ", currEx);
        }
    }

    public static String getFileType(final String input)
    {
    	final String    toTestStr = input.toLowerCase();
    	
        if (toTestStr.contains(".pdf"))
        {
            return "application/pdf";
        }
        if (toTestStr.contains(".jpg"))
        {
            return "image/jpeg";
        }
        if (toTestStr.contains(".jpeg"))
        {
            return "image/jpeg";
        }
        if (toTestStr.contains(".png"))
        {
            return "image/png";
        }
        return "text/plain";
    }
    
    public static String replaceSpecialCharacters(final String input) 
    {
    	return input.replaceAll(REMOVE_SPCL_CHAR_REGEX, "");
    }
    
    public static boolean isInteger(final String numberStr) 
    {
        try 
        {
            Integer.parseInt(numberStr);
        } 
        catch (final NumberFormatException | NullPointerException currEx) 
        {
            return false;
        } 
        return true;
    }

    public static JsonObject getAsJsonObject(final Object inputObj)
    {
        try 
        {
            return (JsonObject) JsonParser.parseString(
            		                           new ObjectMapper().writeValueAsString(inputObj));
        } 
        catch (final JsonProcessingException currEx) 
        {
        	LOGGER.debug("Caught an exception while mapping an Obj to JSON Object as: ", currEx);
            return null;
        }
    }
    
    public static void timedLog(final LogTimer timer, final Logger logger, final boolean logError, 
    		                    final String message, final Object ... args)
    {
        final long    currTime = System.currentTimeMillis();
        
        if ((timer.getLastLoggedTime() + timer.getLoggingInterval()) < currTime)
        {
        	if (logError)
            {
        		logger.error(message, args);
            }
        	else
        	{
        		logger.info(message, args);
        	}
            timer.setLastLoggedTime(currTime);
        }
    }
    
    public static void writeToFile(String outFileName, byte[] fileBytes)
    {
        try
        {
            final FileOutputStream fos = new FileOutputStream(outFileName);
            
            fos.write(fileBytes);
            fos.close();
        }
        catch (final IOException currEx)
        {
            LOGGER.error("Failed to write to file: {} with error: \n{}", outFileName, currEx);
        }
    }

    public static void yieldToOtherThreads(final long sleepTimeInMillis)
    {
	    try 
	    {
	    	Thread.sleep(sleepTimeInMillis);
	    }
	    catch(final InterruptedException currEx)
	    {
	        // Interrupted during idle processing. Ignore and proceed
	    }
    }
    
    public static void deleteFiles(final String partialFileName, final String baseDirPath) 
    {
    	try
    	{
	        if (partialFileName == null || partialFileName.trim().length() == 0)
	        {
	            return;
	        }
	        LOGGER.debug("In deleteFiles() with partialFileName = {}, baseDirPath = {}", 
	        		     partialFileName, baseDirPath);
	        
	        Collection<File> listFiles = FileUtils.listFiles(new File(baseDirPath), 
	                                       new PrefixFileFilter(partialFileName.trim()), null);

	        if (listFiles == null || listFiles.size() == 0)
	        {
		        LOGGER.debug("In deleteFiles(), no files found that need to be deleted.");
	        	return;
	        }
	        LOGGER.debug("In deleteFiles() listFiles size = {}", listFiles.size());
	
	        for (File file : listFiles) 
	        {
	            try 
	            {
	                LOGGER.debug("In deleteFiles(), Deleting file  = {} ", file.getName());
	                
	                FileUtils.forceDelete(file);
	            } 
	            catch (final Exception currEx)
	            {
	                LOGGER.error("Error occured in deleting file: " + file.getAbsolutePath(), 
	                		     currEx);
	            }
			}
    	}
    	catch (final Exception currEx)
    	{
    		// We log and gobble up the exception as nothing can be done if DB operation fails 
    		// in BatchWorker
    		LOGGER.error("Failed in deleteFiles() for partialFileName = {} and baseDirPath "
    				     + "= {}, with Error: \n{}", partialFileName, baseDirPath, currEx);
    	}
    }

    public static boolean fileExists(final String filePath)
    {
    	final File    fileToCheck = new File(filePath);

    	return (!fileToCheck.isDirectory() && fileToCheck.length() > 0L);
    }

    public static boolean directoryExists(final String dirPath)
    {
    	return (new File(dirPath)).isDirectory();
    }
    
    public static int checkConnections(DMSHelper dmsHelper, WSO2Helper wso2Helper, 
                                           DBHelper dbHelper)
    {
        int result = 0;
        if ( !dmsHelper.checkDMSConnection())
        {
            result = 1;
        }
        else if ( !wso2Helper.checkWSO2Connection())
        {
            result = 2;
        }
        else if (!dbHelper.checkDBConnection())
        {
            result = 3;
        }
        return result;
    }
}
