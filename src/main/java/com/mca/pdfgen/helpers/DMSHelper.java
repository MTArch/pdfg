package com.mca.pdfgen.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.mca.pdfgen.beans.AuthToken;
import com.mca.pdfgen.beans.dms.DataDefCriterionBDO;
import com.mca.pdfgen.beans.dms.FormAttachment;
import com.mca.pdfgen.beans.dms.MetaDataBDO;
import com.mca.pdfgen.beans.dms.NGOExecuteAPIBDO;
import com.mca.pdfgen.beans.dms.NGOGetDocumentBDO;
import com.mca.pdfgen.beans.dms.UploadDocumentRequest;
import com.mca.pdfgen.configs.SysParamsConfig;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.exception.PDFGenException;
import com.mca.pdfgen.utils.CommonUtils;

public class DMSHelper 
{
    public enum HttpStatusCodeRange 
    {
        SUCCESS_RANGE, CLIENT_ERROR_RANGE, SERVER_ERROR_RANGE, UNKNOWN
    }
    
    public static final String STR_ZERO = "0";
    private static final String CSR_AWARDS = "CSR Awards";
    private static final Logger LOGGER = LoggerFactory.getLogger(DMSHelper.class);
    
    private String dmsAuthRequestTokenKey;
    
    private final AuthToken authToken;
    private final String dmsGetDocEndPtUrl;
    private final String dmsAddDocEndPtUrl;
    private final String dmsExecApiEndPtUrl;
    private final SysParamsConfig sysParamsConfig;
    private final Map<String, JsonObject> dataDefnMap;
    
    private int NETWORK_TIMEOUT = 0;
    private final RequestConfig requestConfig;

    public DMSHelper(final SysParamsConfig inSysParamsConfig, final long timePeriod)
    {
        super();

        sysParamsConfig = inSysParamsConfig;

        authToken = new AuthToken();
        dataDefnMap = new HashMap<>();
        
        dmsAuthRequestTokenKey = null;

        dmsAddDocEndPtUrl = sysParamsConfig.getDmsTokenUrl() + AppConstants.DMS_ADD_DOC_ENDPOINT;
        dmsGetDocEndPtUrl = sysParamsConfig.getDmsTokenUrl() + AppConstants.DMS_GET_DOC_ENDPOINT;
        dmsExecApiEndPtUrl = sysParamsConfig.getDmsTokenUrl() + AppConstants.DMS_EXEC_API_ENDPOINT;
        NETWORK_TIMEOUT = sysParamsConfig.getNetworkTimeout();
        requestConfig = RequestConfig.custom()
                .setConnectTimeout(NETWORK_TIMEOUT)
                .setConnectionRequestTimeout(NETWORK_TIMEOUT)
                .setSocketTimeout(NETWORK_TIMEOUT).build();
    }
    
    public JsonObject getDocumentFromDMS(final String docId) throws PDFGenException 
    {
        int                 responseStatus = -1;
        String              responseMessage = "";
        
        LOGGER.debug("getDocumentFromDMS() for docId = {}", docId);

        final String               token = getAuthToken();
        final NGOGetDocumentBDO    inputReqBean = new NGOGetDocumentBDO();

        inputReqBean.setDocIndex(docId);
        inputReqBean.setUserDBId(token);
        inputReqBean.setCabinetName(sysParamsConfig.getDmsCabinetName());

        final JsonObject    ngoConnectCabinetInput = CommonUtils.getAsJsonObject(inputReqBean);
        
        if(ngoConnectCabinetInput == null)
        {
            throw new PDFGenException("Error creating requestJson for getDocumentFromDMS()");
        }
        final JsonObject    jsonRequest = new JsonObject();

        jsonRequest.add(JsonParserConstants.NGOGETDOCUMENTBDO, ngoConnectCabinetInput);
        
        final String    request = jsonRequest.toString();
        
        LOGGER.debug("In getDocumentFromDMS() request = [{}]", request);

        final JsonObject    responseResult = getResponseFromDMS(dmsGetDocEndPtUrl, request);
        
        if (responseResult != null)
        {
            final JsonObject    response = responseResult.getAsJsonObject(JsonParserConstants.DATA)
                                         .getAsJsonObject(JsonParserConstants.NGOGETDOCBDORESPTAG); 
            
            responseStatus = response.get("statusCode").getAsInt();
            responseMessage = response.get("message").getAsString();
        }
        LOGGER.debug("Response from getDocumentFromDMS() status code = {} and message = {}", 
                     responseStatus, responseMessage);
        
        return responseResult;
    }

    public JsonObject uploadPDFToDMS(final String folderName, final String fileName,
            final String fileMetaData, final byte[] pdfDoc)
            throws IOException, PDFGenException
    {
        LOGGER.debug("In uploadPDFToDMS() foldername = {}, filename = {}", folderName, fileName);
        final String    folderId = CommonUtils.isInteger(folderName) ? folderName :
                         searchOrCreateFolderIdFromName(folderName);

        LOGGER.debug("In uploadPDFToDMS() FolderId = [{}]", folderId);

        if (folderId.equals("-1"))
        {
            LOGGER.info("Folder Id could not be retrieved. Cannot uploadDocumentToDMS ");
            throw new PDFGenException("In uploadPDFToDMS Failed to retrieve folderId from folder"
                     + " name " + folderName);  
        }
        final DataDefCriterionBDO    metaDataObj = createMetaDataDocument(fileMetaData);
        
        LOGGER.debug("In uploadPDFToDMS() createMetaDataDocument response = [{}]", metaDataObj);
        
        JsonObject    result = null;
        
        // TODO: AAN - Temporary patches added to our code to ensure we try invoking WSO2 or DMS 
        // APIs twice as they randomly fail at times with malformed JSON Exception and on retry
        // go through. This is a patch till the problem is identified and fixed in DMS & WSO2 APIs
        // by those systems
        for (int currRetryIdx = 0; currRetryIdx < 2; currRetryIdx++)
        {
            try
            {
                result = addDocument(AppConstants.FOLDER_DATA_CLASS_NAME, folderName, folderId,
                            AppConstants.FILE_TYPE, pdfDoc, fileName, metaDataObj,
                            AppConstants.UNIQUE_ID);
                
                if (!checkFormName(fileMetaData))
                {
                    changeFolderName(fileName, folderId, AppConstants.FOLDER_DATA_CLASS_NAME);
                }
                return result;
            }
            catch (final PDFGenException currEx)
            {
                LOGGER.info("uploadPDFToDMS() failed with error msg = {} and currRetryIdx = {}", 
                            currEx.getMessage(), currRetryIdx);
                
                if (currRetryIdx == 0)
                {
                    continue;
                }
                else
                {
                    throw currEx;
                }
            }
        }
        return result;
    }
    
    public void clearCachedToken()
    {
        authToken.setCachedToken(null);
    }
    
    public void cleanUp()
    {
        // Nothing to clean up for now
    }
    
    public boolean checkDMSConnection()
    {
        boolean result = false;
        String authToken = null;
        clearCachedToken();
        try 
        {
            authToken = getAuthToken();
            if (authToken != null && !authToken.equals(""))
            {
                result = true;
            }
        }
        catch (Exception ex)
        {
            // Ignore the Exception
        }
        return result;
    }
    
    private String getAuthToken() throws PDFGenException  
    {
        JsonObject jsonResponse;
        long lastTokenGetTime = 0L;
        JsonObject jsonReq = new JsonObject();
        String cachedToken = JsonParserConstants.EMPTY_STRING;
        String responseString = JsonParserConstants.EMPTY_STRING;

        cachedToken = authToken.getCachedToken();
        lastTokenGetTime = authToken.getTokenTime();

        if (cachedToken != null && ((System.currentTimeMillis() - 
                        (lastTokenGetTime) < sysParamsConfig.getDmsTokenRefreshInterval())))
        {
            return cachedToken;
        }
        if (cachedToken != null)
        {
            disconnectDMSSession(cachedToken);
            clearCachedToken();
        }
        if (dmsAuthRequestTokenKey == null)
        {
            NGOExecuteAPIBDO ngoExecuteBean = new NGOExecuteAPIBDO();
            ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setCabinetName(
                                                            sysParamsConfig.getDmsCabinetName());
            ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setUserName(
                                                            sysParamsConfig.getDmsUserName());
            ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setUserPassword(
                                                            sysParamsConfig.getDmsPassword());

            JsonObject beanJson = CommonUtils.getAsJsonObject(ngoExecuteBean);
            
            if (beanJson == null)
            {
                throw new PDFGenException("In DMS Helper Error creating requestJson object for "
                                          + "getting authentication token");
            }
            jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, beanJson);
            
            dmsAuthRequestTokenKey = jsonReq.toString();

            LOGGER.debug("getAuthToken Request [{}] retrieved DMS Auth Request Token as: ", 
                         dmsAuthRequestTokenKey);
        }
        jsonResponse = getResponseFromDMS(dmsExecApiEndPtUrl, dmsAuthRequestTokenKey);
        
        LOGGER.debug("In getAuthToken() json response[{}]", jsonResponse);

        if (jsonResponse == null || ! jsonResponse.has("code")) 
        {
            throw new PDFGenException("Could not get a valid DMS Token. Response = [ " 
                                      + jsonResponse+" ]");
        }
        if (getRange(jsonResponse.get("code").getAsInt()) == HttpStatusCodeRange.SUCCESS_RANGE) 
        {
            JsonObject ngoConnectCabOutput = 
                                jsonResponse.getAsJsonObject(JsonParserConstants.DATA)
                                .getAsJsonObject(JsonParserConstants.NGOEXECUTEAPIRESPTAG)
                                .getAsJsonObject(JsonParserConstants.OUTPUTDATATAG)
                                .getAsJsonObject(JsonParserConstants.NGOCONNOUTPUTTAG);
            
            if (ngoConnectCabOutput == null)
            {
                LOGGER.error("In getAuthToken() the output response is null");
                throw new PDFGenException("Something went wrong in getting DMS Token [ "+ 
                                          jsonResponse+" ]");
            }
            String error;
            error = ngoConnectCabOutput.has("Error") ? jsonResponse.get("Error").getAsString():
                                                              JsonParserConstants.EMPTY_STRING;

            if (error.equals(JsonParserConstants.EMPTY_STRING))
            {
                JsonPrimitive userToken = ngoConnectCabOutput.getAsJsonPrimitive(
                                                                JsonParserConstants.USERDBID);
                if (userToken == null)
                {
                    LOGGER.error("In getAuthToken() the user token is null");
                    throw new PDFGenException("DMS Token is null.[ " + jsonResponse+" ]");
                }
                responseString = userToken.getAsString();
                
                authToken.setCachedToken(responseString);
                lastTokenGetTime = System.currentTimeMillis();
                authToken.setTokenTime(lastTokenGetTime);
            }
            else
            {
                LOGGER.error("Error Msg from getAuthToken() {}", error);
                throw new PDFGenException("ERROR in getting DMS Token [ "+ jsonResponse +" ]");
            }
        }
        else
        {
            LOGGER.error("Error in getAuthToken() response code is {}", jsonResponse.get("code")
                                                                                   .getAsString());
            throw new PDFGenException("ERROR in getting DMS Token [ "+ jsonResponse +" ]");
        }
        LOGGER.debug("Returning from getAuthToken() Token = [{}]", responseString);
        return responseString;
    }

    protected String getConnectCabinetRequest(JsonObject jsonReq) throws PDFGenException
    {
        String req;
        NGOExecuteAPIBDO ngoExecuteBean = new NGOExecuteAPIBDO();
        ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setCabinetName(
                                                        sysParamsConfig.getDmsCabinetName());
        ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setUserName(
                                                        sysParamsConfig.getDmsUserName());
        ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setUserPassword(
                                                        sysParamsConfig.getDmsPassword());

        JsonObject beanJson = CommonUtils.getAsJsonObject(ngoExecuteBean);
        if(beanJson == null)
        {
            throw new PDFGenException("In DMS Helper Error creating requestJson object for "
                                      + "getting authentication token");
        }
        jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, beanJson);
        
        req = jsonReq.toString();
        
        return req;
    }

    private JsonObject handlehttpsResponse(final URL rProfileURL, final String req) 
            throws PDFGenException 
    {
        JsonObject    result = null;
        StringBuilder response = new StringBuilder();
        
        try 
        {
            CommonUtils.trustAllHosts();

            HttpsURLConnection conn = (HttpsURLConnection) rProfileURL.openConnection();
            conn.setConnectTimeout(NETWORK_TIMEOUT);
            conn.setReadTimeout(NETWORK_TIMEOUT);
        
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty(HttpHeaders.ACCEPT, AppConstants.HTTP_CONTENT_TYPE_JSON);
            conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, AppConstants.HTTP_CONTENT_TYPE_JSON);

            OutputStream os = conn.getOutputStream();
            
            try 
            {
                final byte[]    input = req.getBytes(StandardCharsets.UTF_8);
            
                os.write(input, 0, input.length);
            }
            catch (final Exception currEx) 
            {
                PDFGenException.mapAndThrow(currEx, "DMSHelper.handlehttpsResponse failed for "
                                                    + "url = " + rProfileURL.toString() + ", "
                                                    + "req = " + req + ". ");
            }
            finally 
            {
                os.flush();
                os.close();
            }
            InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream(), 
                                                                        StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(inputStreamReader);

            String responseLine = null;
            
            try
            {
                while ((responseLine = br.readLine()) != null) 
                {
                    response.append(responseLine.trim());
                }
                JsonObject    data = (JsonObject)JsonParser.parseString(response.toString());
                
                result = new JsonObject();
                
                result.addProperty("code", "200");
                result.addProperty("status", "Success");
                result.add("data", data);
                
                LOGGER.debug("In DMSHelper.handlehttpsResponse() response is [{}]", result);
            } 
            catch (final Exception currEx)
            {
                // TODO: AAN - Temporary patches added to our code to ensure we try invoking WSO2 
                // or DMS APIs twice as they randomly fail at times with malformed JSON Exception 
                // and on retry go through. This is a patch till the problem is identified and 
                // fixed in DMS & WSO2 APIs by those systems
                final String    errorMsg = currEx.getMessage();
                
                if (errorMsg.contains("com.google.gson.stream.MalformedJsonException")
                    && errorMsg.contains("Invalid escape sequence"))
                {
                    LOGGER.error("DMS API Error issue encountered that led to Malformed JSON "
                                + "exception, ignoring exception and returning null with "
                                 + "errorMsg = {}", errorMsg);
                    return null;
                }
                // Propagate the exception after logging request-response in error case
                PDFGenException.mapAndThrow(currEx, "Exception caught in DMS API with url = " 
                                                    + rProfileURL.toString() + ", " + "req = " 
                                                    + req + ", resp = " + response + ". ");
            }
            finally 
            {
                inputStreamReader.close();
                br.close();
            }
        } 
        catch (final Exception currEx) 
        {   
            PDFGenException.mapAndThrow(currEx);
        }
        return result;
    }

    private JsonObject handleRestResponse(HttpResponse response, String request) 
                                          throws PDFGenException 
    {
        JsonObject data = new JsonObject();
        JsonObject responseJson = new JsonObject();
        String responseStr = "";
        
        try 
        {
            HttpStatusCodeRange range = getRange(response.getStatusLine().getStatusCode());
            responseStr = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            
            switch (range) 
            {
                case SUCCESS_RANGE:
                    data = (JsonObject)JsonParser.parseString(responseStr);

                    responseJson.addProperty("code", response.getStatusLine().getStatusCode());
                    responseJson.addProperty("status", "Success");
                    responseJson.add("data", data);

                    break;
                case CLIENT_ERROR_RANGE:
                    responseJson.addProperty("code", response.getStatusLine().getStatusCode());
                    responseJson.addProperty("status", "Client-side exception");
                    responseJson.add("data", data);
                    LOGGER.info("responseJson 400 {}", responseJson);
                    break;
                case SERVER_ERROR_RANGE:
                    responseJson.addProperty("code", response.getStatusLine().getStatusCode());
                    responseJson.addProperty("status", "Server-side exception");
                    responseJson.add("data", data);
                    LOGGER.info("responseJson 500 {}", responseJson);
                    break;
                default:
                    responseJson.addProperty("code", response.getStatusLine().getStatusCode());
                    responseJson.addProperty("status", "Unknown error");
                    responseJson.add("data", data);
                    break;
            }
        }
        catch (final Exception currEx)
        {
            PDFGenException.mapAndThrow(currEx, "Error in DMSHelper HandleRestResponse() with "
                                                + "req = " + request + ", response = " 
                                                + responseStr + ". ");
        }
        return responseJson;
    }

    private HttpStatusCodeRange getRange(int code) 
    {
        if (code >= 200 && code < 300) 
        {
            return HttpStatusCodeRange.SUCCESS_RANGE;
        }
        if (code >= 400 && code < 500) 
        {
            return HttpStatusCodeRange.CLIENT_ERROR_RANGE;
        }
        if (code >= 500 && code < 600) 
        {
            return HttpStatusCodeRange.SERVER_ERROR_RANGE;
        }
        return HttpStatusCodeRange.UNKNOWN;
    }
    
    private HttpClientBuilder dmsSslContext(String sslKeyFilePath) throws PDFGenException 
    {
        HttpClientBuilder clientbuilder = null;
        try 
        {
            SSLContextBuilder sslBuilder = SSLContexts.custom();
            sslBuilder.setProtocol("TLSv1.2");

            File file = new File(sslKeyFilePath);
            sslBuilder = sslBuilder.loadTrustMaterial(file, "changeit".toCharArray());

            //Building the SSLContext
            SSLContext sslContext = sslBuilder.build();
            
            //Creating SSLConnectionSocketFactory SSLConnectionSocketFactory object
            SSLConnectionSocketFactory sslConSocFactory = 
                          new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());

            //Creating HttpClientBuilder
            clientbuilder = HttpClients.custom();

            //Setting the SSLConnectionSocketFactory
            clientbuilder = clientbuilder.setSSLSocketFactory(sslConSocFactory);
            clientbuilder.setDefaultRequestConfig(requestConfig);
        } 
        catch (final Exception currEx) 
        {
           PDFGenException.mapAndThrow(currEx, "Error in DMSHelper dmsSslContext(). Please look "
                                                  + "into keystore / certificate setup. ");
        }
        return clientbuilder;
    }

    private void disconnectDMSSession(final String token) throws PDFGenException 
    {
        JsonObject jsonReq = new JsonObject();
        JsonObject ngoInput = new JsonObject();
        JsonObject ngoExecute = new JsonObject();
        JsonObject ngoConnectCabinetInput = new JsonObject();

        try 
        {
            ngoConnectCabinetInput.addProperty(JsonParserConstants.CABINETNAME, 
                                               sysParamsConfig.getDmsCabinetName());

            ngoConnectCabinetInput.addProperty(JsonParserConstants.OPTION, 
                                                JsonParserConstants.NGODISCONNECTCABINET);

            ngoConnectCabinetInput.addProperty(JsonParserConstants.USERDBID, token);

            ngoInput.add(JsonParserConstants.NGODISCONNECTINPUTTAG, ngoConnectCabinetInput);
            
            ngoExecute.add(JsonParserConstants.INPUTDATATAG, ngoInput);
            ngoExecute.addProperty(JsonParserConstants.BASE64ENCODED, JsonParserConstants.N);
            ngoExecute.addProperty(JsonParserConstants.LOCALE, JsonParserConstants.LOCALE_VALUE);
            
            jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, ngoExecute);
            
            String req = jsonReq.toString();

            URL rProfileURL = new URL(dmsExecApiEndPtUrl);

            if (dmsExecApiEndPtUrl.contains(AppConstants.HTTPS)) 
            {
                handlehttpsResponse(rProfileURL, req);
            } 
            else
            {
                CloseableHttpClient client = HttpClientBuilder.create()
                                             .setDefaultRequestConfig(requestConfig).build();
                HttpPost httpPost = new HttpPost(dmsExecApiEndPtUrl);
                httpPost.setHeader(HttpHeaders.ACCEPT, AppConstants.HTTP_CONTENT_TYPE_JSON);
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, AppConstants.HTTP_CONTENT_TYPE_JSON);
                
                StringEntity entity = new StringEntity(req);
                httpPost.setEntity(entity);

                handleRestResponse(client.execute(httpPost), req);
            }
        } 
        catch (final IOException currEx)
        {
            PDFGenException.mapAndThrow(currEx, "DMSHelper:disconnectDMSSession() failed. ");
        }
    }

    private String getAuthTokenAndParentFolderId() throws PDFGenException 
    {
        JsonObject jsonResponse;
        JsonObject ngoConnectCabinetOutput;
        JsonObject jsonReq = new JsonObject();
        String responseString = JsonParserConstants.EMPTY_STRING;
        
        if (dmsAuthRequestTokenKey == null)
        {
            NGOExecuteAPIBDO ngoExecuteBean = new NGOExecuteAPIBDO();
            ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setCabinetName(
                                                            sysParamsConfig.getDmsCabinetName());
            ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setUserName(
                                                            sysParamsConfig.getDmsUserName());
            ngoExecuteBean.getInputData().getNGOConnectCabinet_Input().setUserPassword(
                                                            sysParamsConfig.getDmsPassword());

            JsonObject beanJson = CommonUtils.getAsJsonObject(ngoExecuteBean);

            if (beanJson == null)
            {
                throw new PDFGenException("In DMS Helper Error creating requestJson object for "
                                          + "getting authentication token");
            }
            jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, beanJson);
            
            dmsAuthRequestTokenKey = jsonReq.toString();
            
            LOGGER.debug("getAuthTokenAndParentFolderId Request [{}] retrieved DMS Auth "
                         + "Request Token Key as: ", dmsAuthRequestTokenKey);
        }
        jsonResponse = getResponseFromDMS(dmsExecApiEndPtUrl, dmsAuthRequestTokenKey);
        
        LOGGER.debug("getAuthTokenAndParentFolderId Response [{}]", jsonResponse);
        
        if (jsonResponse != null && jsonResponse.has("code")) 
        {
            if (getRange(jsonResponse.get("code").getAsInt()) == HttpStatusCodeRange.SUCCESS_RANGE) 
            {
                ngoConnectCabinetOutput = jsonResponse.getAsJsonObject(JsonParserConstants.DATA)
                                      .getAsJsonObject(JsonParserConstants.NGOEXECUTEAPIRESPTAG)
                                      .getAsJsonObject(JsonParserConstants.OUTPUTDATATAG)
                                      .getAsJsonObject(JsonParserConstants.NGOCONNOUTPUTTAG);

                if (ngoConnectCabinetOutput.has(JsonParserConstants.USERDBID) && 
                 ngoConnectCabinetOutput.getAsJsonPrimitive(JsonParserConstants.USERDBID) != null)
                {
                    responseString = ngoConnectCabinetOutput.getAsJsonPrimitive(
                                                    JsonParserConstants.USERDBID).getAsString();
                }
                else
                {
                    LOGGER.error("Could not retrieve DMS Authentication token. Response from DMS ="
                                 + " [{}]", jsonResponse);
                    throw new PDFGenException("Could not retrieve DMS Authentication token. "
                                              + "Please try again later.");
                }
                try
                {
                    responseString = responseString + "|" + 
                                     ngoConnectCabinetOutput.getAsJsonObject("Folders")
                                     .getAsJsonObject("Folder").getAsJsonPrimitive("FolderIndex")
                                     .getAsString();
                }
                catch (final Exception currEx)
                {
                    PDFGenException.mapAndThrow(currEx, "In getAuthTokenAndParentFolderId "
                                                        + "Could not retrieve folder Id from "
                                                        + "Json Response = [" + jsonResponse + 
                                                        "]. ");
                }
                LOGGER.debug("getAuthTokenAndParentFolderId response = [{}]", responseString );
            }
        } 
        else
        {
            LOGGER.debug("Malformed DMS response received, either empty response or missing "
                         + "'code' attribute, response received = [{}]", 
                        ((jsonResponse == null) ? "null" : jsonResponse));
        }
        return responseString;
    }
    
    private String searchFolderIdFromName(final String folderName) 
    {
        JsonObject opData = new JsonObject();
        JsonObject jsonReq = new JsonObject();
        JsonObject NGOFolderIdOp = new JsonObject();
        JsonObject ngoExecuteApiObj = new JsonObject();
        JsonObject ngoGetIdFromName = new JsonObject();
        JsonObject ngoGetIdFromNameObj = new JsonObject();    
        JsonObject NGOExecuteAPIResponseBDO = new JsonObject();
        
        JsonObject responseObj = new JsonObject();
        String objectIndex = "-1";

        String token = JsonParserConstants.EMPTY_STRING;
        try
        {
            token = getAuthToken();
            
            ngoGetIdFromName.addProperty(JsonParserConstants.OPTION, "NGOSearchFolder");
            ngoGetIdFromName.addProperty(JsonParserConstants.USERDBID, token);
            ngoGetIdFromName.addProperty(JsonParserConstants.CABINETNAME, 
                                                            sysParamsConfig.getDmsCabinetName());
            ngoGetIdFromName.addProperty("LookInFolder", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("Name", folderName);
            ngoGetIdFromName.addProperty("IncludeSubFolder", JsonParserConstants.Y);
            ngoGetIdFromName.addProperty("LookInFolder", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("MaximumHitCountFlag", JsonParserConstants.N);
            ngoGetIdFromName.addProperty("FolderType", "G");
            ngoGetIdFromName.addProperty("DataAlsoFlag", JsonParserConstants.N);
            ngoGetIdFromName.addProperty("IncludeTrashFlag", JsonParserConstants.N);
            ngoGetIdFromName.addProperty("ShowPath", JsonParserConstants.N);
            ngoGetIdFromName.addProperty("CountFolDocFlag", JsonParserConstants.Y);
            ngoGetIdFromName.addProperty("Owner", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("CreationDateRange", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("ExpiryDateRange", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("AccessDateRange", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("RevisedDateRange", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("DataDefCriterion", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("SearchScope", STR_ZERO);
            ngoGetIdFromName.addProperty("PrevFolderList", JsonParserConstants.EMPTY_STRING);
            ngoGetIdFromName.addProperty("ReferenceFlag", "B");
            ngoGetIdFromName.addProperty("StartFrom", JsonParserConstants.ONE);
            ngoGetIdFromName.addProperty("NoOfRecordsToFetch", JsonParserConstants.ONE);
            ngoGetIdFromName.addProperty("OrderBy", "2");
            ngoGetIdFromName.addProperty("SortOrder", "D");
            ngoGetIdFromNameObj.add("NGOSearchFolder_Input", ngoGetIdFromName);
            ngoExecuteApiObj.add(JsonParserConstants.INPUTDATATAG, ngoGetIdFromNameObj);
            ngoExecuteApiObj.addProperty(JsonParserConstants.BASE64ENCODED, JsonParserConstants.N);
            ngoExecuteApiObj.addProperty(JsonParserConstants.LOCALE, 
                                                                JsonParserConstants.LOCALE_VALUE);
            jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, ngoExecuteApiObj);

            String req = jsonReq.toString();
            LOGGER.debug("NGOSearchFolder Request Object = [{}] ", req);

            responseObj = getResponseFromDMS(dmsExecApiEndPtUrl, req);
            
            LOGGER.debug("NGOSearchFolder Response Object = [{}]", responseObj);
            
            if (responseObj != null) 
            {
                NGOExecuteAPIResponseBDO = responseObj.getAsJsonObject(JsonParserConstants.DATA)
                                .get(JsonParserConstants.NGOEXECUTEAPIRESPTAG).getAsJsonObject();
                
                if (NGOExecuteAPIResponseBDO.has(JsonParserConstants.OUTPUTDATATAG)) 
                {
                    opData = NGOExecuteAPIResponseBDO.get(JsonParserConstants.OUTPUTDATATAG)
                            .getAsJsonObject();
                    
                    NGOFolderIdOp = opData.get("NGOSearchFolder_Output").getAsJsonObject();
                    
                    if (NGOFolderIdOp.get("Status").getAsString().equalsIgnoreCase(STR_ZERO) 
                        && (!NGOFolderIdOp.get("TotalNoOfRecords").getAsString().
                                                                    equalsIgnoreCase(STR_ZERO)))
                    {
                        objectIndex = NGOFolderIdOp.get("Folders").getAsJsonObject().get("Folder")
                                      .getAsJsonObject().get("FolderIndex").getAsString();
                    }
                    else
                    {
                        objectIndex = "-1";
                    }
                    LOGGER.debug("In searchFolderIdFromName() folderId = {} is for folderName = {}", 
                                  objectIndex, folderName);
                }
            }
        }
        catch (final Exception currEx) 
        {
            LOGGER.error("Exception in searchFolderIdFromName()", currEx);
        }
        return objectIndex;
    }
    
    private String getFolderIdUsingParentFolderIdAndFolderName(final String folderName) 
                                                               throws PDFGenException
    {
        String token = JsonParserConstants.EMPTY_STRING;
        String parentFolderId = JsonParserConstants.EMPTY_STRING;;
        
        JsonObject opData = new JsonObject();
        JsonObject jsonReq = new JsonObject();
        JsonObject NGOFolderIdOp = new JsonObject();
        JsonObject ngoExecuteApiObj = new JsonObject();
        JsonObject ngoGetIdFromName = new JsonObject();
        JsonObject ngoGetIdFromNameObj = new JsonObject();
        JsonObject NGOExecuteAPIResponseBDO = new JsonObject();
     
        String objectIndex = "-1";
        
        JsonObject responseObj = new JsonObject();
        
        String token1 = getAuthTokenAndParentFolderId();
        
        if (token1.length() > 0 && token1.contains("|"))
        {
            token = token1.substring(0, token1.indexOf("|"));
            parentFolderId = token1.substring(token1.indexOf("|") + 1);
        } 
        else
        {
            LOGGER.error("In getFolderIdUsingParentFolderIdAndFolderName() Token not found or "
                        + "Invalid token");
            throw new PDFGenException("Token not found or Invalid token");
        }
        
        try
        {
            ngoGetIdFromName.addProperty(JsonParserConstants.OPTION, "NGOGetFolderIdForName");
            ngoGetIdFromName.addProperty(JsonParserConstants.USERDBID, token);
            ngoGetIdFromName.addProperty(JsonParserConstants.CABINETNAME, 
                                                            sysParamsConfig.getDmsCabinetName()); 
            ngoGetIdFromName.addProperty("ParentFolderIndex", parentFolderId);
            ngoGetIdFromName.addProperty("FolderName", folderName);
            
            ngoGetIdFromNameObj.add("NGOGetFolderIdForName_Input", ngoGetIdFromName);
            
            ngoExecuteApiObj.add(JsonParserConstants.INPUTDATATAG, ngoGetIdFromNameObj);
            ngoExecuteApiObj.addProperty(JsonParserConstants.BASE64ENCODED, JsonParserConstants.N);
            ngoExecuteApiObj.addProperty(JsonParserConstants.LOCALE, 
                                                                JsonParserConstants.LOCALE_VALUE);

            jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, ngoExecuteApiObj);

            String req = jsonReq.toString();
            LOGGER.debug("NGOGetFolderIdForName Request = [{}] ", req);

            responseObj = getResponseFromDMS(dmsExecApiEndPtUrl, req);
            
            LOGGER.debug("NGOGetFolderIdForName Response = [{}]", responseObj);
            
            if (responseObj != null) 
            {
                NGOExecuteAPIResponseBDO = responseObj.getAsJsonObject(JsonParserConstants.DATA)
                                           .get(JsonParserConstants.NGOEXECUTEAPIRESPTAG)
                                           .getAsJsonObject();
                
                opData = NGOExecuteAPIResponseBDO.get("outputData").getAsJsonObject();
                    
                NGOFolderIdOp = opData.get("NGOGetFolderIdForName_Output").getAsJsonObject();
                
                if (NGOFolderIdOp.get("Status").getAsString().equalsIgnoreCase(STR_ZERO))
                {
                    objectIndex = NGOFolderIdOp.get("FolderIndex").getAsString();
                }
                else
                {
                    objectIndex = "-1";
                }
            }
        }
        catch (final Exception currEx) 
        {
            LOGGER.error("Exception in getIdFromName:: ", currEx);
        } 
        return objectIndex;
    }

    private boolean checkFormName(String metaData)
    {
        JsonObject metaDataObj;

        metaDataObj = (JsonObject)JsonParser.parseString(metaData);

        if (metaDataObj.has("form"))
        {
            String formName = metaDataObj.get(JsonParserConstants.FORM).getAsString().trim();
            return CSR_AWARDS.equalsIgnoreCase(formName);
        }
        return  false;
    }

    private HashMap<String, String> createMetaDataHashmap(String metadata) 
    {
        JsonObject jsonMetadata = (JsonObject)JsonParser.parseString(metadata);

        Iterator<String> iterator = jsonMetadata.keySet().iterator();

        HashMap<String, String> datavalue = new HashMap<>();

        while (iterator.hasNext()) 
        {
            String key = iterator.next();
            if (! jsonMetadata.get(key).isJsonNull())
            {
                datavalue.put(key, jsonMetadata.get(key).getAsString() != null ? 
                          jsonMetadata.get(key).getAsString() : JsonParserConstants.EMPTY_STRING);
            }
        }
        LOGGER.debug("In createMetaDataHashmap() hashmap = [{}]", datavalue);

        return datavalue;
    }

    private DataDefCriterionBDO createMetaDataDocument(final String metadata) 
                                                       throws PDFGenException 
    {
        JsonObject fields = new JsonObject();
        HashMap<String, String> datavalue = createMetaDataHashmap(metadata);
        String documentDataClassName = sysParamsConfig.getDocumentDataClassNameId();
        String title = datavalue.get("title");
        
        if (title != null && CSR_AWARDS.equals(title.trim())) 
        {
            fields = getDataDefList(AppConstants.DATA_DEF_PORTAL_REPORT);
        } 
        else
        {
            // Fetch the fields for D_DocumentMetaData class type with id as 65
            fields = getDataDefProperties(documentDataClassName);
        }
        if (fields == null)
        {
            String errMsg = "In createMetaDataDocument() Cannot proceed further as DMS call did not"
                   + "return metadata for[ "+ AppConstants.DOCUMENT_DATA_CLASS_NAME +" and id = " +
                    documentDataClassName; 
            LOGGER.error(errMsg);
            throw new PDFGenException(errMsg);
        }

        DataDefCriterionBDO documentMetadata = new DataDefCriterionBDO();
        List<MetaDataBDO> setofMetaDataBDO = new ArrayList<>();
        
        JsonArray fieldsArray = 
                            fields.get("Fields").getAsJsonObject().get("Field").getAsJsonArray();
        
        Iterator<String> it = datavalue.keySet().iterator();
        
        while (it.hasNext())
        {
            String key = it.next();
            MetaDataBDO metadataObj;
            for (int i = 0; i < fieldsArray.size(); i++)
            {
                JsonObject jsonObj = fieldsArray.get(i).getAsJsonObject();
                if (key.equalsIgnoreCase(jsonObj.get("IndexName").getAsString())) 
                {
                    metadataObj = new MetaDataBDO();
                    metadataObj.setIndexId(jsonObj.get("IndexId").getAsString());
                    metadataObj.setIndexType(jsonObj.get("IndexType").getAsString());
                    metadataObj.setIndexValue(datavalue.get(key));
                    
                    setofMetaDataBDO.add(metadataObj);
                }
            }
        }
        documentMetadata.setNGOAddDocDataDefCriteriaDataBDO(setofMetaDataBDO);
        documentMetadata.setDataDefName(AppConstants.DOCUMENT_DATA_CLASS_NAME);
        
        String dataDefIndex = fields.get("DataDefIndex").getAsString();
        LOGGER.debug("DataDefIndex =[{}]", dataDefIndex);
        if (dataDefIndex != null)
        {
            documentMetadata.setDataDefIndex(dataDefIndex);
        }
        return documentMetadata;
    }

    private JsonObject createMetaDataFolder(final HashMap<String, String> datavalue, 
                                            final String withFields) 
                                            throws PDFGenException 
    {
        JsonArray fieldsArray;
        JsonObject metafields = null;
        JsonObject fields = new JsonObject();
        JsonObject finalObject = new JsonObject();
        JsonArray metadataFields = new JsonArray();
        
        // id for dataclass name "F_ServiceRequest_Complaints" will be fetched from properties file
        final String    folderClassName = sysParamsConfig.getFolderDataClassNameId().trim();
        fields = getDataDefProperties(folderClassName); 

        if (fields == null)
        {
            String error = "In createMetaDataFolder() DMS did not return metadata information for "
                            + "folder classname = " + AppConstants.FOLDER_DATA_CLASS_NAME +
                            "id = "+folderClassName;
            LOGGER.error(error);
            throw new PDFGenException(error);
        }
        Iterator<String> it = datavalue.keySet().iterator();
        fieldsArray = fields.get(JsonParserConstants.FIELDS)
                            .getAsJsonObject().get(JsonParserConstants.FIELD).getAsJsonArray();
        
        while (it.hasNext()) 
        {
            String key = it.next();
            JsonObject valueObject =  new JsonObject();
            
            for (int i = 0; i < fieldsArray.size(); i++) 
            {
                valueObject = fieldsArray.get(i).getAsJsonObject();
                String indexValue = valueObject.get(JsonParserConstants.INDEXNAME).getAsString();
                
                if (key.equalsIgnoreCase(indexValue)) 
                {
                    metafields = new JsonObject();
 
                    metafields.addProperty(JsonParserConstants.INDEXNAME, indexValue);

                    metafields.addProperty(JsonParserConstants.INDEXID, 
                                       valueObject.get(JsonParserConstants.INDEXID).getAsString());
                    
                    metafields.addProperty(JsonParserConstants.INDEXTYPE, 
                                    valueObject.get(JsonParserConstants.INDEXTYPE).getAsString());
                    metafields.addProperty(JsonParserConstants.INDEXVALUE, datavalue.get(key));                            
                }
            }
            metadataFields.add(metafields);
        }
        if (withFields.equalsIgnoreCase(JsonParserConstants.YES)) 
        {
            JsonObject fFields = new JsonObject();
            fFields.add(JsonParserConstants.FIELD, metadataFields);
            finalObject.add(JsonParserConstants.FIELDS, fFields);
        
            finalObject.addProperty(JsonParserConstants.DATADEFNAME, 
                                    fields.get(JsonParserConstants.DATADEFNAME).getAsString());
            
            finalObject.addProperty(JsonParserConstants.DATADEFINDEX, 
                                    fields.get(JsonParserConstants.DATADEFINDEX).getAsString());
        } 
        else 
        {
            finalObject.add(JsonParserConstants.FIELD, metadataFields);
            finalObject.addProperty(JsonParserConstants.DATACLASSNAMEFOLD, 
                                    fields.get(JsonParserConstants.DATADEFNAME).getAsString());
        }
        LOGGER.debug("In createMetaDataFolder() metadata value object = [{}]" +finalObject);
        return finalObject;
    }

    private JsonObject addDocument(final String folderClassName, final String folderName, 
                                   final String folderId, final String extension, 
                                   final byte[] pdfDoc, final String fileName, 
                                   final DataDefCriterionBDO documentMetaData, 
                                   final String uniqueId) 
                                   throws PDFGenException
    {
        String token = JsonParserConstants.EMPTY_STRING;
        JsonObject returnObject = new JsonObject();
        try 
        {
            UploadDocumentRequest uploadDocumentRequest = new UploadDocumentRequest();
            uploadDocumentRequest.setFolderIndex(folderId);
            uploadDocumentRequest.setDocumentName(fileName);
            uploadDocumentRequest.setCreatedByAppName(extension);
            uploadDocumentRequest.setCabinetName(sysParamsConfig.getDmsCabinetName());
            uploadDocumentRequest.setComment(fileName + ":" + uniqueId);
            uploadDocumentRequest.setVolumeId(JsonParserConstants.ONE);
            uploadDocumentRequest.setNGOAddDocDataDefCriterionBDO(documentMetaData);   
            
            token = getAuthToken();
            uploadDocumentRequest.setUserDBId(token);
            
            ObjectMapper mapper = new ObjectMapper();
            String uploadDocument = mapper.writeValueAsString(uploadDocumentRequest);
            
            JsonObject jsonReq = new JsonObject();
            jsonReq.add(JsonParserConstants.NGOADDDOCUMENTBDO, 
                        ((JsonObject)JsonParser.parseString(uploadDocument)));

            LOGGER.debug("In addDocument() json request = [{}]", jsonReq);
            
            CloseableHttpClient client;

            if (StringUtils.isNotEmpty(sysParamsConfig.getDmsSslKeyFilePath())) 
            {
                HttpClientBuilder httpClientBuilder = 
                                            dmsSslContext(sysParamsConfig.getDmsSslKeyFilePath());
                client = httpClientBuilder.build();
            } 
            else 
            {
                client = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            }

            HttpPost httpPost = new HttpPost(dmsAddDocEndPtUrl);
           
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
           
            // Change as per our processing change, temp file to be created only if add to DMS fails
            // hence sending byte[] of PDF as attachment.
            builder.addBinaryBody("file", pdfDoc, ContentType.APPLICATION_OCTET_STREAM, fileName);
                    
            builder.addTextBody("NGOAddDocumentBDO", jsonReq.toString(), ContentType.TEXT_PLAIN);
            
            LOGGER.debug("In uploadDocument() request created = [{}] and filename attached = [{}]"+
                        jsonReq.toString(), fileName);
            
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
          
            JsonObject responseObj = handleRestResponse(client.execute(httpPost), 
                                                        jsonReq.toString());
            LOGGER.debug("In uploadDocument() response from DMS = [{}]" + responseObj);
            
            FormAttachment dmsResult = new FormAttachment();

            if (! responseObj.get("status").getAsString().equalsIgnoreCase("Success")) 
            {
                String errMsg = "In addDocument, error response recieved from DMS. Response is [{ "+
                                 responseObj + " }]";
                LOGGER.warn(errMsg);
                
                throw new PDFGenException(errMsg);
            }
            JsonObject result = responseObj.getAsJsonObject("data")
                                .get("NGOAddDocumentResponseBDO").getAsJsonObject();
                
            if (result.get("status").getAsInt() != 0) 
            {
                String errorMsg = "In addDocument() status = [{"+result.get("status")+"}] and "
                                   + "message = [{"+ result.get("message").getAsString() + " }]"; 
                LOGGER.error(errorMsg);

                throw new PDFGenException(errorMsg);
            }
            JsonObject docData = result.get("NGOGetDocListDocDataBDO").getAsJsonObject();
            
            String revisedDateTime = docData.get("revisedDateTime").getAsString();
            
            String docSize = docData.get("documentSize").getAsString();
            String docType = docData.get("documentType").getAsString();
            String docIndex = docData.get("documentIndex").getAsString();
            String noOfPages = docData.get("noOfPages").getAsString(); 

            Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(revisedDateTime);
            DateFormat targetFormat = new SimpleDateFormat("MM/dd/yyyy");
            
            dmsResult.setActivityFileDate(targetFormat.format(date1));
            dmsResult.setActivityFileExt(extension);
            dmsResult.setActivityFileName(fileName);
            dmsResult.setActivityFileSize(docSize);
            dmsResult.setActivityFileSrcPath(folderId);
            dmsResult.setActivityFileSrcType(docType);

            dmsResult.setAttachmentCategory("Attachment");

            dmsResult.setAttachmentDMSId(docIndex);
            dmsResult.setFolderName(folderName);
            dmsResult.setFolderId(folderId);

            dmsResult.setVersion(JsonParserConstants.EMPTY_STRING);
            dmsResult.setAddedBy("Fo User");

            if (uniqueId.equalsIgnoreCase(AppConstants.UNIQUE_ID)) 
            {
                dmsResult.setAttachmentCategory("Forms");
                dmsResult.setAttachmentLabel("Signed");
            }
            else 
            {
                dmsResult.setAttachmentLabel(uniqueId);
            }

            dmsResult.setTotalNoOfPages(noOfPages);
            
            dmsResult.setPublicDocument(JsonParserConstants.Y);
            dmsResult.setActivityComments(folderId + ":" + uniqueId + ":" + 
                                                    result.get("message").getAsString());
             
            returnObject = (JsonObject)JsonParser.parseString(mapper.writeValueAsString(dmsResult));
            
            LOGGER.debug("In addDocument() returning response = [{}]\n", returnObject);
        }
        catch (final Exception currEx)
        {
            PDFGenException.mapAndThrow(currEx, "Error in addDocument(). ");
        }
        return returnObject;
    }
    
    private String changeFolderName(final String folderName, final String folderIndex, 
                                    final String folderClassName) 
                                    throws PDFGenException 
    {
        LOGGER.debug("In changeFolderName() folderName = {}, folderIndex = {}, "
                     + "folderClassName = {}", folderName, folderIndex, folderClassName);
        
        JsonObject folder = new JsonObject();
        JsonObject opData = new JsonObject();
        JsonObject jsonReq = new JsonObject();
        JsonObject inputData = new JsonObject();
        JsonObject responseObj = new JsonObject();
        JsonObject NGOFolderIdOp = new JsonObject();
        JsonObject ngoExecuteApiObj = new JsonObject();
        JsonObject NGOExecuteAPIResponseBDO = new JsonObject();
        JsonObject NGOChangeFolderProperty_Input = new JsonObject();
        String objectIndex = JsonParserConstants.EMPTY_STRING;

        HashMap<String, String> keys = folderKeySet(folderName);

        JsonObject metadataFolder = createMetaDataFolder(keys, AppConstants.YES);
        
        String token = JsonParserConstants.EMPTY_STRING;
        token = getAuthToken();
        NGOChangeFolderProperty_Input.addProperty(JsonParserConstants.OPTION, 
                                                                    "NGOChangeFolderProperty");
        NGOChangeFolderProperty_Input.addProperty(JsonParserConstants.USERDBID, token);
        NGOChangeFolderProperty_Input.addProperty(JsonParserConstants.CABINETNAME, 
                                                         sysParamsConfig.getDmsCabinetName());
        folder.addProperty("FolderIndex", folderIndex);
        folder.addProperty("FolderName", folderName);
        folder.addProperty("NameLength", "100");
        folder.addProperty("AccessType", JsonParserConstants.ACCESS_TYPE);
        folder.addProperty("ImageVolumeIndex", JsonParserConstants.ONE);
        folder.addProperty("VersionFlag", JsonParserConstants.Y);
        folder.addProperty("DuplicateName", JsonParserConstants.EMPTY_STRING);
        folder.addProperty("Comment", "Not Defined");
        folder.addProperty("FinalizedFlag", JsonParserConstants.N);
        folder.addProperty("LogGeneration", JsonParserConstants.N);
        folder.addProperty("EnableFTSFlag", JsonParserConstants.N);
        folder.add("DataDefinition", metadataFolder);
        folder.addProperty("Owner", JsonParserConstants.EMPTY_STRING);
        NGOChangeFolderProperty_Input.add("Folder", folder);
        inputData.add("NGOChangeFolderProperty_Input", NGOChangeFolderProperty_Input);
        ngoExecuteApiObj.add(JsonParserConstants.INPUTDATATAG, inputData);
        ngoExecuteApiObj.addProperty(JsonParserConstants.BASE64ENCODED, JsonParserConstants.N);
        ngoExecuteApiObj.addProperty(JsonParserConstants.LOCALE, 
                                                            JsonParserConstants.LOCALE_VALUE);

        jsonReq.add("NGOExecuteAPIBDO", ngoExecuteApiObj);

        String req = jsonReq.toString();
        LOGGER.debug("NGOChangeFolderProperty request = [{}] ", req);
        
        responseObj = getResponseFromDMS(dmsExecApiEndPtUrl, req);
        
        LOGGER.debug("NGOChangeFolderProperty response = [{}]", responseObj);

        if (responseObj != null) 
        {
            NGOExecuteAPIResponseBDO = responseObj.getAsJsonObject(JsonParserConstants.DATA)
                            .get(JsonParserConstants.NGOEXECUTEAPIRESPTAG).getAsJsonObject();
            
            if (NGOExecuteAPIResponseBDO.has("outputData")) 
            {
                opData = NGOExecuteAPIResponseBDO.get("outputData").getAsJsonObject();
                NGOFolderIdOp = opData.get("NGOChangeFolderProperty_Output").getAsJsonObject();
            
                if (NGOFolderIdOp.get("Status").getAsString().equalsIgnoreCase(STR_ZERO))
                {
                    objectIndex = STR_ZERO; 
                }
                else
                {
                    LOGGER.info("NGOChangeFolderProperty response = [{}]", responseObj);    
                    LOGGER.info("Issue in changeFolderName() is = {}", 
                                                NGOFolderIdOp.get("Error").getAsString());
                    objectIndex = "-1";
                }
            }
        }
        else
        {
            LOGGER.info("Issue in changeFolderName Response is null ");
            objectIndex = "-1";
        }
        return objectIndex;
    }

    private JsonObject getDataDefList(final String name) throws PDFGenException 
    {
        JsonObject opData = new JsonObject();
        JsonObject fields = new JsonObject();
        JsonObject jsonReq = new JsonObject();
        JsonObject jsoninput = new JsonObject();
        String token = JsonParserConstants.EMPTY_STRING;
        JsonObject NGOExecuteAPIBDO = new JsonObject();
        JsonObject NGOGetIDFromName_Input = new JsonObject();
        String objectIndex = JsonParserConstants.EMPTY_STRING;
        JsonObject NGOExecuteAPIResponseBDO = new JsonObject();
    
        token = getAuthToken();

        NGOGetIDFromName_Input.addProperty(JsonParserConstants.OPTION, "NGOGetIDFromName");
        NGOGetIDFromName_Input.addProperty(JsonParserConstants.USERDBID, token);
        NGOGetIDFromName_Input.addProperty(JsonParserConstants.CABINETNAME, 
                                                        sysParamsConfig.getDmsCabinetName());
        NGOGetIDFromName_Input.addProperty("ObjectType", "X");
        NGOGetIDFromName_Input.addProperty("ObjectName", name);
        jsoninput.add("NGOGetIDFromName_Input", NGOGetIDFromName_Input);
        NGOExecuteAPIBDO.add(JsonParserConstants.INPUTDATATAG, jsoninput);
        NGOExecuteAPIBDO.addProperty(JsonParserConstants.BASE64ENCODED, JsonParserConstants.N);
        NGOExecuteAPIBDO.addProperty(JsonParserConstants.LOCALE, 
                                                        JsonParserConstants.LOCALE_VALUE);

        jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, NGOExecuteAPIBDO);

        String req = jsonReq.toString();
        LOGGER.debug("In getDataDefList() NGOGetIDFromName DMS Request =[{}]" + req);
        
        JsonObject responseObj = getResponseFromDMS(dmsExecApiEndPtUrl, req);
        
        LOGGER.debug("In getDataDefList() NGOGetIDFromName DMS Response =[{}]" + responseObj);
        
        if (responseObj != null) 
        {
            NGOExecuteAPIResponseBDO =responseObj.getAsJsonObject(JsonParserConstants.DATA)
                            .get(JsonParserConstants.NGOEXECUTEAPIRESPTAG).getAsJsonObject();
            
            opData = NGOExecuteAPIResponseBDO.get(JsonParserConstants.OUTPUTDATATAG)
                                                                            .getAsJsonObject();
            
            JsonObject NGOFolderIdOp = opData.get("NGOGetIDFromName_Output").getAsJsonObject();
                
            if (NGOFolderIdOp.get("Status").getAsString().equalsIgnoreCase(STR_ZERO))
            {
                objectIndex = NGOFolderIdOp.get("ObjectIndex").getAsString();
            }
            else
            {
                objectIndex = "-1";
            }
        }
        jsonReq = new JsonObject();

        NGOExecuteAPIBDO = new JsonObject();
        jsoninput = new JsonObject();
        NGOGetIDFromName_Input = new JsonObject();
        NGOExecuteAPIResponseBDO = new JsonObject();
        opData = new JsonObject();
        NGOGetIDFromName_Input.addProperty("Option", "NGOGetDataDefProperty");
        NGOGetIDFromName_Input.addProperty("UserDBId", token);
        NGOGetIDFromName_Input.addProperty("CabinetName", sysParamsConfig.getDmsCabinetName());
        NGOGetIDFromName_Input.addProperty("DataDefIndex", objectIndex);

        jsoninput.add("NGOGetDataDefProperty_Input", NGOGetIDFromName_Input);
        NGOExecuteAPIBDO.add("inputData", jsoninput);
        NGOExecuteAPIBDO.addProperty("base64Encoded", "N");
        NGOExecuteAPIBDO.addProperty("locale", "en_US");

        jsonReq.add("NGOExecuteAPIBDO", NGOExecuteAPIBDO);
        
        LOGGER.debug("In getDataDefList() NGOGetDataDefProperty  request is [{}] ", jsonReq);

        responseObj = getResponseFromDMS(dmsExecApiEndPtUrl, jsonReq.toString());
        
        LOGGER.debug("In getDataDefList() NGOGetDataDefProperty response is [{}]", responseObj);

        if (responseObj != null) 
        {
            JsonObject  NGOFolderIdOp = responseObj.getAsJsonObject("data")
                                        .get("NGOExecuteAPIResponseBDO").getAsJsonObject()
                                        .get("outputData").getAsJsonObject()
                                        .get("NGOGetDataDefProperty_Output").getAsJsonObject();
            
            if (NGOFolderIdOp.get("Status").getAsString().equalsIgnoreCase(STR_ZERO))
                fields = NGOFolderIdOp.get("DataDefinition").getAsJsonObject();
        }
        return fields;
    }

    private String getDMSDataDefInput(String dataDefIndex, String token) 
    {
        String retVal;
        retVal = 
                "{"
                +    "\"NGOExecuteAPIBDO\": {"
                +        "\"inputData\": {"
                +        "\"NGOGetDataDefProperty_Input\": {"
                +        "\"Option\": \"NGOGetDataDefProperty\","
                +        "\"CabinetName\": \"" + sysParamsConfig.getDmsCabinetName() +"\","
                +        "\"UserPassword\": \"" + sysParamsConfig.getDmsPassword()+ "\","
                +        "\"UserDBId\": \"" + token +"\","
                +        "\"DataDefIndex\": \""+ dataDefIndex + "\""
                +        "}"
                +       "},"
                +        "\"base64Encoded\": \"N\","
                +        "\"locale\": \"en_US\""
                +    "}}";
        return retVal;
    }

    private String addBulkFolder(final String folderName) throws PDFGenException
    {
        JsonObject opData = new JsonObject();
        JsonObject jsonReq = new JsonObject();
        JsonObject inputData = new JsonObject();
        JsonObject responseObj = new JsonObject();
        JsonObject NGOFolderIdOp = new JsonObject();
        JsonObject ngoExecuteApiObj = new JsonObject();
        JsonObject NGOBlkLoad_Input = new JsonObject();
        JsonObject NGOExecuteAPIResponseBDO = new JsonObject();

        String objectIndex = JsonParserConstants.EMPTY_STRING;

        HashMap<String, String> keys = folderKeySet(folderName);
        
        JsonObject metadataFolder = createMetaDataFolder(keys, AppConstants.NO);
        String token = JsonParserConstants.EMPTY_STRING;
        
        token = getAuthToken();
        
        NGOBlkLoad_Input.addProperty(JsonParserConstants.OPTION, "NGOBlkLoad");
        NGOBlkLoad_Input.addProperty(JsonParserConstants.USERDBID, token);
        NGOBlkLoad_Input.addProperty(JsonParserConstants.CABINETNAME, 
                                                        sysParamsConfig.getDmsCabinetName());
        NGOBlkLoad_Input.addProperty("FolderName", folderName);
        
        LOGGER.debug("In addBulkFolder folderName = [{}]" + folderName);
        
        if (folderName.contains("COMPLIANCE"))
        {
            NGOBlkLoad_Input.addProperty("RootFolderPath", sysParamsConfig.getDmsCabinetName() + 
                                    getFolderStructure(AppConstants.FOLDER_TYPE_COMPLIANCES));
        }
        else
        {
            NGOBlkLoad_Input.addProperty("RootFolderPath", sysParamsConfig.getDmsCabinetName() + 
                                getFolderStructure(AppConstants.FOLDER_TYPE_SERVICE_REQUEST));
        }
        NGOBlkLoad_Input.addProperty("ParentFolderIndex", JsonParserConstants.EMPTY_STRING);
        NGOBlkLoad_Input.addProperty("FastMode", JsonParserConstants.N);
        NGOBlkLoad_Input.addProperty("ValidateImage", JsonParserConstants.EMPTY_STRING);
        NGOBlkLoad_Input.addProperty("GenerateLog", JsonParserConstants.Y);
        NGOBlkLoad_Input.addProperty("GenerateName", JsonParserConstants.EMPTY_STRING);
        NGOBlkLoad_Input.addProperty("CheckExistenceFlag", JsonParserConstants.EMPTY_STRING);
        NGOBlkLoad_Input.addProperty("EnableFTSFlag", JsonParserConstants.N);
        NGOBlkLoad_Input.addProperty("Owner", JsonParserConstants.EMPTY_STRING);
        NGOBlkLoad_Input.add("DataDefinition", metadataFolder);
        
        inputData.add("NGOBlkLoad_Input", NGOBlkLoad_Input);
        ngoExecuteApiObj.add("inputData", inputData);

        ngoExecuteApiObj.addProperty(JsonParserConstants.BASE64ENCODED, JsonParserConstants.N);
        ngoExecuteApiObj.addProperty(JsonParserConstants.LOCALE, 
                                                            JsonParserConstants.LOCALE_VALUE);

        jsonReq.add(JsonParserConstants.NGOEXECUTEAPIBDOTAG, ngoExecuteApiObj);

        String req = jsonReq.toString();
        LOGGER.debug("addBulkFolder NGOBlkLoad request is [{}]", req);
        
        responseObj = getResponseFromDMS(dmsExecApiEndPtUrl, req);
        
        LOGGER.debug("addBulkFolder NGOBlkLoad response is [{}]", responseObj);

        if (responseObj != null) 
        {
            NGOExecuteAPIResponseBDO = responseObj.getAsJsonObject(JsonParserConstants.DATA)
                                        .get(JsonParserConstants.NGOEXECUTEAPIRESPTAG)
                                        .getAsJsonObject();
            
            if (NGOExecuteAPIResponseBDO.has(JsonParserConstants.OUTPUTDATATAG)) 
            {
                opData = NGOExecuteAPIResponseBDO.get(JsonParserConstants.OUTPUTDATATAG)
                        .getAsJsonObject();
                
                NGOFolderIdOp = opData.get("NGOBlkLoad_Output").getAsJsonObject();
               
                if (NGOFolderIdOp.get("Status").getAsString().equalsIgnoreCase(STR_ZERO))
                {
                    objectIndex = NGOFolderIdOp.get("ParentFolderIndex").getAsString();
                }
                else
                {
                    LOGGER.info("Issue in addBulkFolder, error = [{}] ", 
                                                NGOFolderIdOp.get("Error").getAsString());
                    objectIndex = "-1";
                }
            }
        }
        LOGGER.debug("In addBulkFolder() retrieved folderId = {} for folderName = {}", 
                    objectIndex, folderName);
        
        return objectIndex;
    }

    private String searchOrCreateFolderIdFromName(final String folderName) throws PDFGenException
    {
        String folderId = CommonUtils.isInteger(folderName) ? folderName : 
                                             searchFolderIdFromName(folderName);
        if (folderId.equalsIgnoreCase("-1"))
        {
            folderId = getFolderIdUsingParentFolderIdAndFolderName(folderName);
        
            if (folderId.equalsIgnoreCase("-1"))
            {
                folderId = addBulkFolder(folderName);
            }
        }
        LOGGER.debug("In searchOrCreateFolderIdFromName() folderId = {} retrieved for folderName "
                    + "= {}", folderId, folderName);
        
        return folderId;
    }

    private HashMap<String, String> folderKeySet(String mvalue) 
    {
        HashMap<String, String> map = new HashMap<>();

        map.put(AppConstants.SRN, mvalue);
        return map;
    }

    private JsonObject getResponseFromDMS(final String endPointUrl, final String jsonReq) 
                                          throws PDFGenException
    {
        JsonObject responseObj = new JsonObject();

        if (endPointUrl.contains(AppConstants.HTTPS)) 
        { 
            try
            {
                URL rProfileURL = new URL(endPointUrl);
                responseObj = handlehttpsResponse(rProfileURL, jsonReq);
            }
            catch (final MalformedURLException currEx)
            {
                PDFGenException.mapAndThrow(currEx, "Incorrect endpoint URL = [" 
                                                    + endPointUrl + "]. ");
            }
        }
        else
        {
            CloseableHttpClient client = HttpClientBuilder.create()
                                         .setDefaultRequestConfig(requestConfig).build();
            HttpPost httpPost = new HttpPost(endPointUrl);
            httpPost.setHeader(HttpHeaders.ACCEPT, AppConstants.HTTP_CONTENT_TYPE_JSON);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, AppConstants.HTTP_CONTENT_TYPE_JSON);
           
            try 
            {
                StringEntity entity = new StringEntity(jsonReq);
                httpPost.setEntity(entity);
                responseObj = handleRestResponse(client.execute(httpPost), jsonReq);
            } 
            catch (final Exception currEx)
            {
                PDFGenException.mapAndThrow(currEx, "Error in DMSHelper.handlehttpResponse(). ");
            }
        }
        return responseObj;
    }

    private JsonObject getDataDefProperties(final String dataDefIndex) throws PDFGenException
    {
        JsonObject dataDefJson = null;
        
        Object response = dataDefnMap.get(dataDefIndex);
        if (response != null)
        {
            return (JsonObject)response;
        }

        String token = JsonParserConstants.EMPTY_STRING;

        // Create Input JSON From cabinet and data def Index
        String inputRequest;
        
        JsonObject jsonResponse = new JsonObject();
        token = getAuthToken();
        inputRequest = getDMSDataDefInput(dataDefIndex, token);
        
        LOGGER.debug("In getDataDefProperties() NGOGetDataDefProperty DMS request = [{}]",
                      inputRequest);
        
        jsonResponse = getResponseFromDMS(dmsExecApiEndPtUrl, inputRequest);
        
        LOGGER.debug("In getDataDefProperties() NGOGetDataDefProperty DMS response = [{}]",
                    jsonResponse);

        // Parse the JSON upto DataDefinition             
        JsonObject outputJson =  jsonResponse.getAsJsonObject("data")
                                .getAsJsonObject("NGOExecuteAPIResponseBDO")
                                .getAsJsonObject("outputData")
                                .getAsJsonObject("NGOGetDataDefProperty_Output");
        
        if (outputJson.get("Status").getAsString().equalsIgnoreCase(STR_ZERO))
        {
            dataDefJson = outputJson.getAsJsonObject("DataDefinition");
            dataDefnMap.put(dataDefIndex, dataDefJson);
        }
        LOGGER.debug("In getDataDefProperties() returning Json = [{}]", dataDefJson);

        return dataDefJson;
    }
    
    private String getFolderStructure(String type) 
    {
        StringBuffer folderStructure = new StringBuffer();

        folderStructure.append(AppConstants.SEPARATOR + AppConstants.V3 + AppConstants.SEPARATOR +  
                                type + AppConstants.SEPARATOR);  
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);

        int year = calendar.get(Calendar.MONTH) < 3 ? calendar.get(Calendar.YEAR) % 100  - 1 : 
                                calendar.get(Calendar.YEAR) % 100 ;

        folderStructure.append (AppConstants.MCA + year + JsonParserConstants.EMPTY_STRING +
                                (year + 1) + AppConstants.SEPARATOR);

        int month = calendar.get(Calendar.MONTH);
        String quarter = (month >= Calendar.JANUARY && month <= Calendar.MARCH) ? "QTR4" :
                         (month >= Calendar.APRIL && month <= Calendar.JUNE) ? "QTR1" :
                         (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) ? "QTR2" : "QTR3";
        
        folderStructure.append(quarter).append(AppConstants.SEPARATOR);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        String strMonth = sdf.format(today);

        sdf = new SimpleDateFormat("dd");
        String strDay = sdf.format(today);
        folderStructure.append(strMonth.toUpperCase() + strDay);
        
        LOGGER.debug("In getFolderStructure folder structure to use in addBulkFolder is = [{}]", 
                      folderStructure.toString());

        return folderStructure.toString();
    }
}
