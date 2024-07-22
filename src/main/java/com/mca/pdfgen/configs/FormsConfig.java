package com.mca.pdfgen.configs;

import com.mca.pdfgen.beans.db.FormBatchMaster;
import com.mca.pdfgen.beans.pdf.SigFieldDefn;
import com.mca.pdfgen.generators.PdfOutputGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FormsConfig
{
    // form id for the given form
    private int formId;

    // Max number of signatures to be affixed for this form
    private int signatureCount;
    
    // Max number of attachments possible for this form
    private int attachmentCount;
    
    // Form name as specified in the Form_Name column of FORMSIGNBATCHMSTR table 
    private String masterFormName;
    
    // Form name as specified in the Refer column of cx_siebel.generatepdf table
    private String requestFormName;
    
    // This flag will specify whether we want to use signatureCount configured above or the one 
    // configured in the DSCCount column of cx_siebel.generatepdf table
    private boolean useConfigDSCCount;
    
    // Template file name for generating PDF
    private String templateFileName;
    
    // RegEx used for searching signature box location contains ### or not
    private boolean useWithHashmarkRegEx;
    
    private String sigFieldPrefix;
    
    //Defines the generator used for creating the pdf - XDA template based or HTML template based.
    private PdfOutputGenerator pdfGenerator;
    
    //Defines the model to load the json for HTML template
    private Class<?> formModel;
    
    // Template file content read once and stored in the form configuration for this form
    private byte[] templateFileContent;
    
    // FormBatchMaster record for this form 
    private FormBatchMaster formMasterData; 
    
    // Array of SignatureFieldDefinitions containing firstField and secondFieldNames
    private SigFieldDefn[] fieldDefs;
}
