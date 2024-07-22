package com.mca.pdfgen.beans.pdf;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.exception.PDFGenException;

import lombok.Data;

/**
 * This class is used for searching for and populating the second field name values required to be
 * set as the second part of the signature box field name, which is used by later process flows 
 * and downstream systems for signature verification. We parse the JSON data set that is being 
 * used for populating the PDF and retrieve the first & second field values that are used to make 
 * the unique Id of the digital signature certificate (DSC) field in the PDF. We create a map of 
 * first and second field values as the key-value pairs in the map. This map will then be used 
 * during creation of DSC Box field names in the PDF.
 */
@Data
public class SigFieldDefn
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(SigFieldDefn.class);
    
    // Will be true if the attribute is a static field for which we do not need lookup for 2nd 
    // field value. For example, In Dir3Kyc form, DIN1 is a placeholder for first signature field 
    // for which we do not need
    // to parse Json object, however this will be used as a key to map the location attributes of
    //signature box and the value of DIN which will be retrieved as secondFieldValue from the json 
    // data.
    private boolean staticFirstField;
    
    // Will be true when the secondFieldValue is to be hardcoded to a given value, for eg in form 
    // CHG-1 "bank_bank" is used as secondFieldValue 
    private boolean staticSecondFieldValue;
    
    // Name associated with the hidden field in the XFA Form where the signature box will need to 
    // be affixed
    private String firstFieldName; 
    
    // List of possible attribute field names in JSON that can provide the second field value in 
    // JSON. For example: DIN/ DPIN/ PAN/ Membership id, etc. Only one field name from the given 
    // list of field names will be applicable for a single DSC box but for different DSC boxes the 
    // second field value may come from a different attribute name in this list
    private String[] secondFieldNames;  
    
    private String   defaultSecondValue;
    
    public SigFieldDefn(final boolean staticFirstField, final String firstFieldNm, 
                        final boolean staticSecondField, final String[] secondFieldNm,
                        final String defaultSecondValue) throws PDFGenException
    {
        this.firstFieldName = firstFieldNm;

        if (firstFieldName == null || firstFieldName.isEmpty())
        {
            throw new PDFGenException("First field name cannot be null or empty.");
        }
        this.secondFieldNames = secondFieldNm;
        this.staticFirstField = staticFirstField;
        this.defaultSecondValue = defaultSecondValue;
        this.staticSecondFieldValue = staticSecondField;
    }
    
    public void getFieldValuePairs(final JSONObject formDataJson, 
                                   final Map<String, String> valueMap) 
                                   throws PDFGenException
    {
        try 
        {
            LOGGER.debug("Starting retrieval of Field Value pairs. Value Map contains = {} keys"
                         + "at this point", valueMap.size());
            LOGGER.debug("Form Data Json = {} ", formDataJson.toString());
            final JsonNode    root = (new ObjectMapper()).readTree(formDataJson.toString());
            LOGGER.debug("Root Node = {}", root);
            JsonNode          currNode = root;

            if (this.staticFirstField && this.staticSecondFieldValue)
            {
                valueMap.put(firstFieldName, secondFieldNames[0]);
                return;
            }
            if (this.staticFirstField)
            {
                populateValuesMapForStaticFirstField(valueMap, currNode);
            }
            else
            {               
                populateValuesMapForNonStaticFirstField(valueMap, currNode);
            }
        } 
        catch (final Exception currEx)
        {
            PDFGenException.mapAndThrow(currEx, "Exception occured while fetching signature "
                                                + "field key-value pairs. ");
        }
    }

    private void populateValuesMapForStaticFirstField(final Map<String, String> valueMap, 
                                                         final JsonNode currNode) 
    {
        final int    noOfSecondNames = secondFieldNames.length;
        String       secondKey = JsonParserConstants.EMPTY_STRING;
        String       secondValue = JsonParserConstants.EMPTY_STRING;

        for (int index = 0; index < noOfSecondNames; index++)
        {
            LOGGER.debug("Index = {}, node = {} and secondFieldName = {}", index, currNode, 
                         secondFieldNames[index]);
            
            secondKey = secondFieldNames[index];
            JsonNode    secondValNode = currNode.findParent(secondKey);

            if (secondValNode == null)
            {
                LOGGER.debug("Second Value Node not found for secondFieldName = {}. Setting "
                            + "second Field Value to empty string", secondKey);
                continue;
            }
            else
            {
                secondValNode = secondValNode.findValue(secondKey);
                
                if (secondValNode == null || secondValNode.isNull())
                {
                    LOGGER.debug("Second Field Value not found for secondFieldName = {}", 
                                 secondKey);
                    continue;
                }
                secondValue = secondValNode.asText(JsonParserConstants.EMPTY_STRING);

                if (secondValue.isEmpty())
                {
                    LOGGER.debug("Second Field Value not found for secondFieldName = {}", 
                                 secondFieldNames[index]);
                    continue;
                }
                else
                {
                    LOGGER.debug("Second Field Value = {} found for secondFieldName = {} at "
                                 + "position = {}. Will not search further.", secondValue, 
                                 secondKey, index);
                    break;
                }
            }
        }
        if (secondValue.equals(JsonParserConstants.EMPTY_STRING))
        {
               LOGGER.debug("Second Field Value not found for secondFieldName = {}. Setting "
                          + "second Field Value = {}", secondKey, defaultSecondValue);
               secondValue = defaultSecondValue;
        }
        if (valueMap.containsKey(firstFieldName))
        {
               LOGGER.warn("Value Map already contains entry for key = {} with value = {}. "
                            + "Skipping record", firstFieldName, valueMap.get(firstFieldName));
            
        }
        valueMap.put(firstFieldName, secondValue);
        
        LOGGER.info("Found secondValue = {} for static firstFieldName = {}, value map size "
                        + "= {} ", secondValue, firstFieldName, valueMap.size());
    }

    private void populateValuesMapForNonStaticFirstField(final Map<String, String> valueMap, 
                                                         final JsonNode currNode) 
    {
        final int               noOfSecondNames = secondFieldNames.length;
        final List<JsonNode>    list = currNode.findParents(firstFieldName);
        
        LOGGER.debug("Number of First Field Names found = {}", list.size());
        
        for (final JsonNode node : list)
        {
            LOGGER.debug("Found node = {}, containing firstName = {}", node, firstFieldName);
            
            final String    key = node.get(firstFieldName).asText();
            
            LOGGER.debug("key [node.get(firstFieldName).asText()] = {}", key);

            if (key.isEmpty())
            {
                LOGGER.warn("First Field Value cannot be empty.");
                continue;
            }
            String    value = "";
            
            if (this.staticSecondFieldValue)
            {
                valueMap.put(key, secondFieldNames[0]);
                break;
            }
            
            for (int index = 0; index < noOfSecondNames; index++)
            {
                LOGGER.debug("Index = {}, node = {} and secondFieldName = {}", index, node, 
                             secondFieldNames[index]);
                final JsonNode    valueNode;
                
                JsonNode tempNode;
                String secondNm  = secondFieldNames[index];
                LOGGER.debug("secondFieldName = {}", secondNm);

                if (secondNm.contains(":"))
                {
                    tempNode = node; 
                    final String[]    secondFieldNameSplit = secondNm.split(":");
                    int len = secondFieldNameSplit.length;

                    for (int i = 0; i < len; i++)
                    {
                        tempNode = tempNode.findValue(secondFieldNameSplit[i]);
                        LOGGER.debug("tempNode = {} ", tempNode);
                    }
                    valueNode = tempNode;
                }
                else
                {
                    valueNode = node.findValue(secondFieldNames[index]);
                }

                if (valueNode == null || valueNode.isNull())
                {
                    LOGGER.debug("Second Field Value not found for secondFieldName = {}", 
                                 secondFieldNames[index]);
                    continue;
                }
                value = valueNode.asText(JsonParserConstants.EMPTY_STRING);

                if (value.isEmpty())
                {
                    LOGGER.debug("Second Field Value not found for secondFieldName = {}", 
                                 secondFieldNames[index]);
                    continue;
                }
                else
                {
                    LOGGER.debug("Second Field Value = {} found for secondFieldName = {} at "
                                 + "position = {}. Will not search further.", value, 
                                 secondFieldNames[index], index);
                    break;
                }
            }
            if (valueMap.containsKey(key))
            {
                LOGGER.warn("Value Map already contains entry for key = {} with value = {}. "
                            + "Skipping record", key, valueMap.get(key));
                continue;
            }
            if (value.isEmpty())
            {
                value = defaultSecondValue;
            }
            valueMap.put(key, value);
            LOGGER.debug("Map contains entry with key = {} and value = {}", key, value);
        }
        LOGGER.info("Found = {} firstNames, and corresponding second name values = {}", 
                    list.size(), valueMap.size());
    }
}