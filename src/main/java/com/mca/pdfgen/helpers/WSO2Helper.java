package com.mca.pdfgen.helpers;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mca.pdfgen.beans.AuthToken;
import com.mca.pdfgen.beans.pdf.PdfMetaData;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.exception.PDFGenException;

public class WSO2Helper 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WSO2Helper.class);

    private final AuthToken authToken;
    private SysParamsConfig sysParamsConfig;
    
    public WSO2Helper(final SysParamsConfig inSysParamsConfig, final long timePeriod)
    {
        super();

        sysParamsConfig = inSysParamsConfig;
        authToken = new AuthToken();
    }
    
    public String documentInsertUpdateRecord(final String reqBody) 
                                             throws PDFGenException
    {
        final String    token = getActiveToken();
        String          response = JsonParserConstants.EMPTY_STRING;

        LOGGER.debug("documentInsertUpdateRecord  WSO2Token = [{}] ", token);

        String endPointUrl = sysParamsConfig.getWso2EndpointHostIp() + 
                                                           AppConstants.DOC_INSERT_UPLOAD_URL;
        try
        {
        // if token or endPointUrl is null, postMethod will throw an exception
            response = postMethod(endPointUrl, token, reqBody);

            LOGGER.debug("In documentInsertUpdateRecord WSO2 Response = [{}] ", response); 
            
            JSONObject respJson = new JSONObject(response);
            String err = JsonParserConstants.EMPTY_STRING;

            if (respJson.has("ERROR"))
            {
                err = respJson.getString("ERROR");
            }
            if (err.equals(JsonParserConstants.EMPTY_STRING) && (respJson.has("error")))
            {
                err = respJson.getString("error");
            }               
            if(! err.equalsIgnoreCase(JsonParserConstants.EMPTY_STRING))
            {
                LOGGER.error("Error occured in WSO2Helper documentInsertUpdateRecord. Error "
                             + "code is Message is {}", err, respJson.getString("message"));
                LOGGER.error("WSO2 Error Response is [{}]", respJson);
                
                throw new PDFGenException("Error occured in WSO2Helper API "
                                          + "documentInsertUpdateRecord with "
                                          + "response" + response);  
            }
            else
            {
                LOGGER.debug("WSO2Helper documentInsertUpdateRecord response message is ", 
                          respJson.getString("Message"));
            }
        }
        catch (final Exception currEx)
        {
            LOGGER.error("Failed in WSO2 API documentInsertUpdateRecord() with request = [{}], "
                         + " and response = [{}] and Error Message = {}", reqBody, response,
                         currEx.getMessage());
            PDFGenException.mapAndThrow(currEx, "Failed in WSO2 API documentInsertUpdateRecord. ");
        }
        return response;
    }

    public String prepareReqForWSO2InsertUpdate(final PdfMetaData pdfMetaData, 
                                                final JsonObject dmsResponse)
    {
        JsonObject wso2ReqJson = new JsonObject();
        
        dmsResponse.addProperty("activityComments", "Generated PDF Stored Successfully");

        dmsResponse.addProperty("attachmentLabel", pdfMetaData.getForm());

        dmsResponse.addProperty("attachmentCategory", "Forms");
        
        JsonObject insidebody = new JsonObject();
        
        insidebody.addProperty("operation", "Uploaded");
        insidebody.addProperty("userId", pdfMetaData.getUserid());
        insidebody.addProperty("integrationId", pdfMetaData.getIntegrationId());
        insidebody.addProperty("srn", pdfMetaData.getSrn());
        insidebody.addProperty("formName", pdfMetaData.getForm());
        insidebody.addProperty("formDescription", pdfMetaData.getFormDescription());
        insidebody.addProperty("formVersion", pdfMetaData.getFormVersion());
        
        JsonArray formAttachment = new JsonArray();
        formAttachment.add(dmsResponse);
        
        insidebody.add("formAttachment", formAttachment);

        wso2ReqJson.add("requestBody", insidebody);
        
        LOGGER.debug("WSO2 Insert Upload Request = [{}] \n", wso2ReqJson);

        return wso2ReqJson.toString();
    }
    
    public String getActiveToken() throws PDFGenException
    {
        if (authToken.getCachedToken() == null || authToken.getCachedToken().length() == 0)
        {
            LOGGER.info("Generating token for this first time");
            refreshToken();
        } 
        else if (authToken.getTokenTime() < System.currentTimeMillis()) 
        {
            LOGGER.info("Refreshing token in get token method");
            clearToken();
            refreshToken();
        }
        return authToken.getCachedToken();
    }
    
    public void clearToken()
    {
        authToken.setCachedToken(null);
    }
    
    public void cleanUp()
    {
        // Nothing to clean up for now
    }
    
    public boolean checkWSO2Connection()
    {
        boolean result = false;
        clearToken();
        String token = null;
        try 
        {
            token = getActiveToken();
            if (token != null && !token.equals(""))
            {
                result = true;
            }
        }
        catch (Exception ex)
        {
            // Ignore the exception
        }
        return result;
    }

    private void refreshToken() throws PDFGenException
    {
        CloseableHttpClient    httpClient;
        String                 token = StringUtils.EMPTY;
        String                 resToken = StringUtils.EMPTY;
        String                 responseString = StringUtils.EMPTY;
        
        try
        {
            if (sysParamsConfig.getWso2APIProtocol().equalsIgnoreCase(AppConstants.HTTPS)) 
            {
                SSLContext                    sslContext = null;
                SSLContextBuilder             sslBuilder = null;
                HttpClientBuilder             clientBuilder = null;
                SSLConnectionSocketFactory    sslConSocFactory = null;
                final File                    keyStoreFile = new File(
                                                     sysParamsConfig.getWso2SslKeyFilePath());

                sslBuilder = SSLContexts.custom().loadTrustMaterial(
                                                      keyStoreFile, "changeit".toCharArray());
                sslContext = sslBuilder.build();
                sslConSocFactory = new SSLConnectionSocketFactory(sslContext, 
                                                                  new NoopHostnameVerifier());
                clientBuilder = HttpClients.custom().setSSLSocketFactory(sslConSocFactory);

                // Building the CloseableHttpClient
                httpClient = clientBuilder.build();
            }
            else 
            {
                httpClient = HttpClientBuilder.create().build();
            }
            final HttpPost    httpPost = new HttpPost(sysParamsConfig.getWso2TokenUrl());
            
            token = sysParamsConfig.getWso2Token();
            
            LOGGER.debug("WSO2 OAuth2 Token Generation Token = {}", token);
            
            httpPost.addHeader(HttpHeaders.AUTHORIZATION, String.format("Basic %s", token));
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, 
                               AppConstants.HTTP_CONTENT_TYPE_URL_ENCODED);
            
            final List <NameValuePair>    nvPair = new ArrayList<>();
            
            nvPair.add(new BasicNameValuePair("username", sysParamsConfig.getWso2UserName()));
            nvPair.add(new BasicNameValuePair("password", sysParamsConfig.getWso2Password()));
            nvPair.add(new BasicNameValuePair("grant_type", sysParamsConfig.getWso2GrantType()));

            httpPost.setEntity(new UrlEncodedFormEntity(nvPair, StandardCharsets.UTF_8));
            
            LOGGER.debug("HTTP Post Request for getWSO2Token() = {} with args = [{}]", 
                         httpPost.toString(), 
                         ((UrlEncodedFormEntity) httpPost.getEntity()).toString());

            final HttpResponse    response = httpClient.execute(httpPost);
            final int             responseCode = response.getStatusLine().getStatusCode();
            
            LOGGER.info("Response Code = {} from getWSO2Token()", responseCode);
            
            switch (responseCode) 
            {
                case 200:
                {
                    // everything is fine, handle the response
                    responseString = EntityUtils.toString(response.getEntity());  

                    LOGGER.debug("responseString =[{}] from getWSO2Token()",responseString);

                    JsonObject jsonResStr = (JsonObject)JsonParser.parseString(responseString); 
                    resToken = jsonResStr.get(AppConstants.ACCESS_TOKEN).getAsString();
                    
                    LOGGER.debug("WSO2 token = [{}]", resToken);

                    authToken.setCachedToken(resToken);

                    // Token expire time needs to be set to 55 minutes 
                    long tokenExpTimeInSec = Long.parseLong(jsonResStr.get(
                                                 AppConstants.ACCESS_TOKEN_EXPIRES).toString());
                    tokenExpTimeInSec = tokenExpTimeInSec - 300L;
                    
                    authToken.setTokenTime(
                                  System.currentTimeMillis() + (tokenExpTimeInSec * 1000L));
                    
                    LOGGER.info("Setting token expiry time value = {}", authToken.getTokenTime());
                    break;
                }

                case 500:
                {
                    // server problems ?
                    responseString = EntityUtils.toString(response.getEntity());
                    
                    LOGGER.error("WSO2 Token Server return code = {} responseString = {}",
                                responseCode, responseString);
                    throw new PDFGenException("Error getting WSO2 Token: response code=" +
                            responseCode + " responseString=" + responseString);
                }
               
                default:
                {
                    responseString = EntityUtils.toString(response.getEntity());
                    
                    LOGGER.error("WSO2 Token Server returned code = {}, responseString = {}", 
                                responseCode, responseString);
                    throw new PDFGenException("Error getting WSO2 Token: response code=" +
                            responseCode + " responseString=" + responseString);
                }
            }
        }
        catch (final Exception currEx)
        {
            PDFGenException.mapAndThrow(currEx, "Error in refreshing WSO2 Token. ");
        }
    }

    private String postMethod(final String endPointUrl, final String token, final String reqBody) 
                              throws PDFGenException
    {
        LOGGER.debug("Inside WSO2 Helper postMethod.\n Http connection URL {}  Request Body [{}]", 
                  endPointUrl, reqBody);

        CloseableHttpClient client = null;
        String responseString = StringUtils.EMPTY;
        
        try
        {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            builder.setProtocol("TLSv1.2");
            
            SSLConnectionSocketFactory sslConnectionSocketFactory;
            sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
                                                                NoopHostnameVerifier.INSTANCE);
            
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>
                        create().register("http", new PlainConnectionSocketFactory())
                        .register("https", sslConnectionSocketFactory).build();
            
            PoolingHttpClientConnectionManager cm = 
                                                 new PoolingHttpClientConnectionManager(registry);
            try
            {
                cm.setMaxTotal(20);
                client = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory)
                                                                .setConnectionManager(cm).build();

                HttpPost httpPost = new HttpPost(endPointUrl);
                
                if (StringUtils.isNotEmpty(token))
                {
                    httpPost.addHeader(HttpHeaders.AUTHORIZATION, 
                                                        String.format("Bearer %s", token));
                }
                
                httpPost.setHeader(HttpHeaders.ACCEPT, AppConstants.HTTP_CONTENT_TYPE_JSON);
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, AppConstants.HTTP_CONTENT_TYPE_JSON);
                StringEntity entity = new StringEntity(reqBody);
                httpPost.setEntity(entity);
                
                // This method will either throw exceptions or return a non-null response containing
                // responseCode
                HttpResponse response = client.execute(httpPost);

                int responseCode = response.getStatusLine().getStatusCode();

                // handle the response
                responseString = EntityUtils.toString(response.getEntity());

                switch (responseCode) 
                {
                    case 200: 
                    {
                        LOGGER.debug("WSO2Helper postMethod() responseString is [{}]", 
                                     responseString);
                        break;
                    }
                    default:
                    {
                        LOGGER.error("WSO2Helper postMethod() returned HTTP Status Code {} and "
                                     + "response string [{}]",
                                       responseCode, responseString);                        
                        throw new Exception(responseString);
                    }
                }
            } 
            catch (final Exception currEx)
            {
                PDFGenException.mapAndThrow(currEx, "Error in WSO2Helper.postMethod(). ");
            }
            finally
            {
                cm.close();
            }
        } 
        catch (final Exception currEx) 
        {
            PDFGenException.mapAndThrow(currEx, "Error in WSO2Helper.postMethod(). Please look "
                                                + "into keystore/certificate setup. ");
        }
        return responseString;
    }
}
