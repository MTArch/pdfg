package com.mca.pdfgen.configs;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.mca.pdfgen.beans.db.FormBatchMaster;
import com.mca.pdfgen.beans.forms.inc31.Inc31;
import com.mca.pdfgen.beans.forms.inc31conv.Inc31Conversion;
import com.mca.pdfgen.beans.forms.inc34.Inc34;
import com.mca.pdfgen.beans.forms.inc34conv.Inc34Conversion;
import com.mca.pdfgen.beans.pdf.SigFieldDefn;
import com.mca.pdfgen.constants.AppConstants;
import com.mca.pdfgen.constants.JsonParserConstants;
import com.mca.pdfgen.exception.PDFGenException;
import com.mca.pdfgen.generators.Html2PdfGenerator;
import com.mca.pdfgen.generators.PdfOutputGenerator;
import com.mca.pdfgen.generators.XfaTemplate2PdfGenerator;
import com.mca.pdfgen.helpers.DBHelper;
import com.mca.pdfgen.utils.ApplicationContextUtil;

@Component
public class FormsConfigManager 
{
    private static final Logger LOGGER = LoggerFactory.getLogger(FormsConfigManager.class);

    // Form Config Constants for Dir3 Web Forms
    public static final String DIR3KYCWEB_REQ_FORMNAME = "dir-3kyc-web"; 
    public static final String DIR3KYCWEB_MSTR_FORMNAME = "DIR-3KYC" ;    
    public static final String DIR3KYCWEB_TEMPLATE_FILE_NAME  = "dir3_kyc_web.pdf";
    
    // Form Config Constants for Dir3 eKyc Forms
    public static final String DIR3_EKYC_REQ_FORMNAME = "dir3-kyc"; 
    public static final String DIR3_EKYC_MSTR_FORMNAME = "DIR3KYC eForm" ;    
    public static final String DIR3_EKYC_TEMPLATE_FILE_NAME  = "dir3_kyc_eform.pdf";
    
    // Form Config Constants for INC-9 Forms
    public static final String INC9_REQ_FORMNAME = "inc-9"; 
    public static final String INC9_MSTR_FORMNAME = "INC-9" ;    
    public static final String INC9_TEMPLATE_FILE_NAME  = "inc_9.pdf";

    // Form Config Constants for Spice A Form
    public static final String SPICEA_REQ_FORMNAME = "company-name-reservation"; 
    public static final String SPICEA_MSTR_FORMNAME = "Spice+ Part A" ;    
    public static final String SPICEA_TEMPLATE_FILE_NAME  = "spice_part_a.pdf";

    // Form Config Constants for URC-1 Form
    public static final String URC1_REQ_FORMNAME = "urc1-new1"; 
    public static final String URC1_MSTR_FORMNAME = "URC-1" ;    
    public static final String URC1_TEMPLATE_FILE_NAME  = "urc_1.pdf";

    // Form Config Constants for RUN 
    public static final String RUN_REQ_FORMNAME = "run-company";
    public static final String RUN_MSTR_FORMNAME = "RUN";
    public static final String RUN_TEMPLATE_FILE_NAME = "run_company.pdf";

    // Form Config Constants for Agile Pro
    public static final String AGILEPRO_REQ_FORMNAME = "agile-pro-journey"; 
    public static final String AGILEPRO_MSTR_FORMNAME = "AGILE PRO" ;    
    public static final String AGILEPRO_TEMPLATE_FILE_NAME  = "agile_pro.pdf";
    
    // Form Config Constants for INC-33 Forms
    public static final String INC33_REQ_FORMNAME = "inc33"; 
    public static final String INC33_MSTR_FORMNAME = "INC-33" ;    
    public static final String INC33_TEMPLATE_FILE_NAME  = "inc_33.pdf";

    // Form Config Constants for INC-13 Forms
    public static final String INC13_REQ_FORMNAME = "inc13"; 
    public static final String INC13_MSTR_FORMNAME = "INC-13" ;    
    public static final String INC13_TEMPLATE_FILE_NAME  = "inc_13.pdf";
    
    // Form Config Constants for INC-31 Forms
    public static final String INC31_REQ_FORMNAME = "spiceaoa-inc31"; 
    public static final String INC31_MSTR_FORMNAME = "INC-31" ;    
    public static final String INC31_TEMPLATE_FILE_NAME  = "/html/inc_31.html";

    // Form Config Constants for INC-34 Forms
    public static final String INC34_REQ_FORMNAME = "spiceAOA-INC34"; 
    public static final String INC34_MSTR_FORMNAME = "INC-34" ;    
    public static final String INC34_TEMPLATE_FILE_NAME  = "/html/inc_34.html";

    // Form Config Constants for Spice-B Forms
    public static final String SPICEB_REQ_FORMNAME = "spice-plus-part-b"; 
    public static final String SPICEB_MSTR_FORMNAME = "SPICE + Part B" ;    
    public static final String SPICEB_TEMPLATE_FILE_NAME  = "spice_plus_part_b.pdf";

    // Form Config Constants for INC-31 Forms
    public  static final String INC31CONV_REQ_FORMNAME = "inc-31"; 
    public static final String INC31CONV_MSTR_FORMNAME = "INC-31" ;    
    public static final String INC31CONV_TEMPLATE_FILE_NAME  = "/html/inc_31_conversion.html";

    // Form Config Constants for INC-34 Forms
    public static final String INC34CONV_REQ_FORMNAME = "inc-34"; 
    public static final String INC34CONV_MSTR_FORMNAME = "INC-34" ;    
    public static final String INC34CONV_TEMPLATE_FILE_NAME  = "/html/inc_34_conversion.html";
    
    // Form Config 
    public static final String CHG1_REQ_FORMNAME = "chg-1";
    public static final String CHG1_MSTR_FORMNAME    = "CHG-1";
    public static final String CHG1_TEMPLATE_FILE_NAME  = "chg_1.pdf";
    
    public static final String DIR3_REQ_FORMNAME = "dir-3";
    public static final String DIR3_MSTR_FORMNAME    = "DIR-3";
    public static final String DIR3_TEMPLATE_FILE_NAME  = "dir_3.pdf";
    
    public static final String DPT3_REQ_FORMNAME = "company-user";
    public static final String DPT3_MSTR_FORMNAME    = "DPT-3";
    public static final String DPT3_TEMPLATE_FILE_NAME  = "dpt_3.pdf";
    
    public static final String MGT14_REQ_FORMNAME = "form-mgt-14-01";
    public static final String MGT14_MSTR_FORMNAME    = "MGT-14";
    public static final String MGT14_TEMPLATE_FILE_NAME  = "mgt_14.pdf";
    
    public static final String DIR12_REQ_FORMNAME = "dir-12";
    public static final String DIR12_MSTR_FORMNAME    = "DIR-12";
    public static final String DIR12_TEMPLATE_FILE_NAME  = "dir_12.pdf";
    
    public static final String INC13_CONVERSION_REQ_FORMNAME = "inc-13";
    public static final String INC13_CONVERSION_MSTR_FORMNAME    = "INC-13";
    public static final String INC13_CONVERSION_TEMPLATE_FILE_NAME  = "inc_13_conversion.pdf";
    
    public static final String INC33_CONVERSION_REQ_FORMNAME  = "inc-33";
    public static final String INC33_CONVERSION_MSTR_FORMNAME    = "INC-33";
    public static final String INC33_CONVERSION_TEMPLATE_FILE_NAME  = "inc_33_conversion.pdf";
    
    public static final String RUNLLP_REQ_FORMNAME  = "run-llp1";
    public static final String RUNLLP_MSTR_FORMNAME    = "RUN LLP";
    public static final String RUNLLP_TEMPLATE_FILE_NAME  = "run_llp.pdf";
    
    public static final String INC20A_REQ_FORMNAME  = "inc20a-flow1";
    public static final String INC20A_MSTR_FORMNAME    = "INC-20A";
    public static final String INC20A_TEMPLATE_FILE_NAME  = "inc_20a.pdf";
    
    public static final String FORM_11_REQ_FORMNAME  = "form-11";
    public static final String FORM_11_MSTR_FORMNAME    = "LLP Form 11";
    public static final String FORM_11_TEMPLATE_FILE_NAME  = "form_11.pdf";
    
    public static final String FORM_8_REQ_FORMNAME  = "llp-form-8";
    public static final String FORM_8_MSTR_FORMNAME    = "LLP Form 8";
    public static final String FORM_8_TEMPLATE_FILE_NAME  = "form_8.pdf";
    
    public static final String REGEXWITHHASHMARK =
                                                "([D|d][I|i][N|n][1-9][0-9]{0,1}\\#\\#\\#)(.*)";
    public static final String REGEXWITHOUTHASHMARK = 
                                                "([D|d][I|i][N|n][1-9][0-9]{0,1})(.*)";

    // Note: Not used at the moment. Will need to uncomment when we get forms having DPIN pattern
    // public static final String REGEX_DPIN  = "([D|d][P|p][I|i][N|n][1-9][0-9]{0,1})(.*)";
    // public static final String REGEX_DINDPIN_WITHOUTHASHMARK = 
    //                                        "([D|d][P|p]{0,1}[I|i][N|n][1-9][0-9]{0,1})(.*)";

    private static FormsConfigManager formsManager; 

    @Autowired
    private SysParamsConfig sysconfig;

    private boolean initialized = false;
    
    private int totalForms = 0;
    private DBHelper dbHelper = null;
    private PdfOutputGenerator xfaPdfGenerator;
    private PdfOutputGenerator htmlPdfGenerator;
    private Map<String, FormsConfig> formsByReqForNameMap = new HashMap<String, FormsConfig>();
//    private static Map<String, FormsConfig> formsByMasterNameMap = 
//                                                            new HashMap<String, FormsConfig>();
    
    @Bean
    public static FormsConfigManager getInstance()
    {
        if (formsManager == null)
        {
            formsManager = new FormsConfigManager();
        }
        return formsManager;
    }
    
    public void initFormsConfigurations() throws IOException, PDFGenException
    {
        synchronized (this)
        {
            if (initialized)
            {
                return;
            }
            if (dbHelper == null)
            {
                dbHelper = ApplicationContextUtil.getBean(DBHelper.class);
            }
            htmlPdfGenerator = new Html2PdfGenerator(sysconfig);
            xfaPdfGenerator = new XfaTemplate2PdfGenerator(sysconfig);
            
            setupAllSupportedFormsConfig();
            
            initialized = true;
        }
    }
    
    public static String getRegExPatternToUse(final boolean regExWithHashMark)
    {
        return (regExWithHashMark ? REGEXWITHHASHMARK : REGEXWITHOUTHASHMARK);
    }
    
    public FormsConfig getFormConfiguration(final String reqFormName)
    {
        if (!isSupported(reqFormName))
        {
            LOGGER.info("Processing Error: Form Type = {} not supported.", reqFormName);
            
            return null;
        }
        return getFormConfig(reqFormName); 
    }
    
    public int getTotalForms()
    {
        return totalForms;
    }

    private void setupAllSupportedFormsConfig() throws IOException, PDFGenException 
    {
        final FormsConfigManager  currInstance = this;
        
        // Dir3-KYC Web
        currInstance.createFormConfigAndPopulateMap(totalForms++, 0, 0, 
                                         DIR3KYCWEB_REQ_FORMNAME, DIR3KYCWEB_MSTR_FORMNAME,
                                         DIR3KYCWEB_TEMPLATE_FILE_NAME, true, false, null, null, 
                                         xfaPdfGenerator, null);
        // Dir3-eKYC
        final SigFieldDefn[] dir3KycSigfieldDef = 
                 {new SigFieldDefn(true, "DIN1", false, new String[] {"DIN"}, 
                                                             JsonParserConstants.EMPTY_STRING),
                  new SigFieldDefn(true, "DIN2", false, new String[] {"membandcerti"}, 
                                                              JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 2, 20, 
                                    DIR3_EKYC_REQ_FORMNAME, DIR3_EKYC_MSTR_FORMNAME,
                                    DIR3_EKYC_TEMPLATE_FILE_NAME, true, false, 
                                    AppConstants.DIN, dir3KycSigfieldDef, xfaPdfGenerator, null); 
        // INC-9 Config
        final String[]          secondName = {"DIN", "PAN"};
        final SigFieldDefn[]    inc9FieldDef = {new SigFieldDefn(false, "DINDSC", false,
                                                secondName, JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 20, 50, INC9_REQ_FORMNAME, 
                                    INC9_MSTR_FORMNAME, INC9_TEMPLATE_FILE_NAME, false, true, 
                                    AppConstants.DIN, inc9FieldDef, xfaPdfGenerator, null);

        // INC-13 (eMoA) Config
        final String[]          inc13SecondName = {"DINPANPassportNumber"};
        final SigFieldDefn[]    inc13FieldDef = {
                new SigFieldDefn(false, "dscCount", false, inc13SecondName, 
                                 JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "dscCount2", false, new String[] {"id"}, 
                                 JsonParserConstants.EMPTY_STRING)};

        currInstance.createFormConfigAndPopulateMap(totalForms++, 8, 20, INC13_REQ_FORMNAME, 
                                    INC13_MSTR_FORMNAME, INC13_TEMPLATE_FILE_NAME, false, true, 
                                    AppConstants.DIN, inc13FieldDef, xfaPdfGenerator, null);

        // INC-33 (eMoA) Config 
        final String[]          inc33SecondName = {"DINPANPassportNumber"};
        final SigFieldDefn[]    inc33FieldDef = {
                new SigFieldDefn(false, "dscCount", false, inc33SecondName, 
                                 JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "dscCount1", false, new String[] {"id"}, 
                                 JsonParserConstants.EMPTY_STRING)};

        FormsConfigManager.getInstance().createFormConfigAndPopulateMap(totalForms++, 8, 20, 
                                         INC33_REQ_FORMNAME, INC33_MSTR_FORMNAME,
                                         INC33_TEMPLATE_FILE_NAME, false, true, 
                                         AppConstants.DIN, inc33FieldDef, xfaPdfGenerator, null); 
        
        // INC-31 (eAoA) Config
        final String[]          inc31SecondName = {"subPassportdinpan"};
        final SigFieldDefn[]    inc31FieldDef = { 
                new SigFieldDefn(false, "dscCount", false, inc31SecondName,
                                 JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "dscCount1", false,    new String[] {"witnessPassportDinPan"}, 
                                 JsonParserConstants.EMPTY_STRING)};

        currInstance.createFormConfigAndPopulateMap(totalForms++, 8, 20, INC31_REQ_FORMNAME, 
                                INC31_MSTR_FORMNAME, INC31_TEMPLATE_FILE_NAME, false, true, 
                                AppConstants.DIN, inc31FieldDef, htmlPdfGenerator, Inc31.class);
        
        // INC-34 (eAoA) Config
        final String[]          inc34SecondName = {"subPassportdinpan"};
        final SigFieldDefn[]    inc34FieldDef = {
                new SigFieldDefn(false, "dscCount", false, inc34SecondName, 
                                 JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "dscCount1", false,    new String[] {"witnessPassportDinPan"},
                                 JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 8, 20, INC34_REQ_FORMNAME, 
                                INC34_MSTR_FORMNAME, INC34_TEMPLATE_FILE_NAME, false, true, 
                                AppConstants.DIN, inc34FieldDef, htmlPdfGenerator, Inc34.class);
	    // INC-31 (eAoA) Config
        final SigFieldDefn[] inc31ConvFieldDef = { 
        		new SigFieldDefn(true, "DIN1", false, new String[] {"declarantDIN"},
        														JsonParserConstants.EMPTY_STRING)};

        currInstance.createFormConfigAndPopulateMap(totalForms++, 1, 20, INC31CONV_REQ_FORMNAME, 
        								INC31CONV_MSTR_FORMNAME, INC31CONV_TEMPLATE_FILE_NAME, true,
        								false, AppConstants.DIN, inc31ConvFieldDef, htmlPdfGenerator, 
		                                Inc31Conversion.class);
		// INC-34 (eAoA) Config
        final SigFieldDefn[] inc34ConvFieldDef = {
        		new SigFieldDefn(true, "DIN1", false, new String[] {"declarantDIN"}, 
        														JsonParserConstants.EMPTY_STRING)};
        
		currInstance.createFormConfigAndPopulateMap(totalForms++, 1, 20, INC34CONV_REQ_FORMNAME, 
									INC34CONV_MSTR_FORMNAME, INC34CONV_TEMPLATE_FILE_NAME, true,
									false, AppConstants.DIN, inc34ConvFieldDef, htmlPdfGenerator, 
                                    Inc34Conversion.class);
        
        // SPICeA Config
        currInstance.createFormConfigAndPopulateMap(totalForms++, 0, 5, 
                                         SPICEA_REQ_FORMNAME, SPICEA_MSTR_FORMNAME,
                                         SPICEA_TEMPLATE_FILE_NAME, true, false, null, null, 
                                         xfaPdfGenerator, null);
        
        // SPICeB Config
        String[] spiceBDin1SecondName = {"opcDetails:opcdin", "opcDetails:pan"};
        
        String[] spiceBDin2SecondName = {"dinPan"};
        
        String[] spiceBDin3SecondName = {"directorDeclaration:membershipNumber", 
                                         "directorDeclaration:pan", 
                                         "directorDeclaration:certificateofPracticeNumber"};
        
        SigFieldDefn[] spiceBFieldDef = {
                new SigFieldDefn(false, "DIN1DSC", false, spiceBDin1SecondName, 
                                                                JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "DIN2DSC", false,    spiceBDin2SecondName,
                                                                 JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "DIN3DSC", false,    spiceBDin3SecondName,
                                                                JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 3, 20, SPICEB_REQ_FORMNAME, 
                                    SPICEB_MSTR_FORMNAME, SPICEB_TEMPLATE_FILE_NAME, true, false, 
                                    AppConstants.DIN, spiceBFieldDef, xfaPdfGenerator, null);
        // URC-1 Config
        final String[] urc1Name2 = {"certificateProfessionalMembershipNo"};
        final SigFieldDefn[] urc1SigfieldDef = 
            {new SigFieldDefn(true, "DIN1", false, new String[] {"directorDINORPAN"}, 
                                                                JsonParserConstants.EMPTY_STRING),
             new SigFieldDefn(true, "DIN2", false, urc1Name2, JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 2, 20,URC1_REQ_FORMNAME, 
                                URC1_MSTR_FORMNAME, URC1_TEMPLATE_FILE_NAME, false, false, 
                                AppConstants.DIN, urc1SigfieldDef, xfaPdfGenerator, null);
        
        // Agile Pro Config
        final String[]          agileSecondName = {"directorDIN", "directorPAN"};
        final SigFieldDefn[]    agileProSigfieldDef = {
                                    new SigFieldDefn(true, "DIN2", false, agileSecondName, 
                                                     AppConstants.AGILE_DEFAULT_SIGFIELD_TEXT)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 1, 20, AGILEPRO_REQ_FORMNAME, 
                                AGILEPRO_MSTR_FORMNAME, AGILEPRO_TEMPLATE_FILE_NAME, false, false, 
                                AppConstants.DIN, agileProSigfieldDef, xfaPdfGenerator, null);
        
        // RUN Config
        currInstance.createFormConfigAndPopulateMap(totalForms++, 0, 20, 
                                         RUN_REQ_FORMNAME, RUN_MSTR_FORMNAME,
                                         RUN_TEMPLATE_FILE_NAME, false, false, 
                                            AppConstants.DIN, null, xfaPdfGenerator, null);

        // DIR-12 Config
        final SigFieldDefn[]    dir12FieldDef = { 
                new SigFieldDefn(false, "DINDSC", false, new String[] {"DIN"}, 
                                 JsonParserConstants.EMPTY_STRING),
                new SigFieldDefn(false, "declarationMembershipNumberDSC", false, 
                                 new String[] {"declarationMembershipNumber"}, 
                                 JsonParserConstants.EMPTY_STRING),          
                new SigFieldDefn(false, "practiceNumberDSC", false, 
                                 new String[] {"practiceNumber1"}, 
                                 JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 20, 20, DIR12_REQ_FORMNAME, 
                                DIR12_MSTR_FORMNAME, DIR12_TEMPLATE_FILE_NAME, false, true, 
                                AppConstants.DIN, dir12FieldDef, xfaPdfGenerator, null);

        // DIR3
        final SigFieldDefn[]    dir3SigfieldDef = {
                  new SigFieldDefn(true, "DIN1", false, new String[] {"applicantDSC"}, 
                                                     AppConstants.DIR3_DEFAULT_SIGFIELD_TEXT),
                  new SigFieldDefn(true, "DIN2", false, new String[] {"din_PAN_MembershipNumber"}, 
                                     JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 2,20, DIR3_REQ_FORMNAME, 
                                                    DIR3_MSTR_FORMNAME, DIR3_TEMPLATE_FILE_NAME,
                                                     true, false, AppConstants.DIN, dir3SigfieldDef, 
                                                     xfaPdfGenerator, null);
        // CHG-1
        final SigFieldDefn[]    chg1SigfieldDef = { 
                          new SigFieldDefn(false, "DINDSC", false, new String[] {"DIN"}, 
                                           JsonParserConstants.EMPTY_STRING),
                          new SigFieldDefn(false, "PAN1DSC", true, new String[] {"bank_bank"}, 
                                            JsonParserConstants.EMPTY_STRING),
                          new SigFieldDefn(false, "PAN2DSC", true, new String[] {"bank_bank"}, 
                                            JsonParserConstants.EMPTY_STRING),
                          new SigFieldDefn(false, "MCNDSC", false, new String[] {"membandcerti"}, 
                                            JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 4, 20, CHG1_REQ_FORMNAME, 
                                                    CHG1_MSTR_FORMNAME, CHG1_TEMPLATE_FILE_NAME, 
                                                    true, false, AppConstants.DIN, chg1SigfieldDef,
                                                    xfaPdfGenerator, null);
        
        // DPT3
        final SigFieldDefn[]    dpt3SigfieldDef = { 
             new SigFieldDefn(true, "DIN1", false, new String[] {"practicenumber"}, 
                              JsonParserConstants.EMPTY_STRING),
             new SigFieldDefn(true, "DIN2", false, new String[] {"declarationID"}, 
                              JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 2, 20, DPT3_REQ_FORMNAME, 
                                                    DPT3_MSTR_FORMNAME, DPT3_TEMPLATE_FILE_NAME, 
                                                    true, false, AppConstants.DIN, dpt3SigfieldDef, 
                                                    xfaPdfGenerator, null);
        // MGT14
        final SigFieldDefn[]    mgt14SigfieldDef = {
            new SigFieldDefn(false, "DINNoDSC", false, new String[] {"identificationNumber"}, 
                             JsonParserConstants.EMPTY_STRING),
            new SigFieldDefn(false, "membershipNoDSC", false, new String[] {"finalCOP"}, 
                             JsonParserConstants.EMPTY_STRING)};
        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 2, 20, MGT14_REQ_FORMNAME, 
                                                    MGT14_MSTR_FORMNAME, MGT14_TEMPLATE_FILE_NAME, 
                                                    true, false, AppConstants.DIN, mgt14SigfieldDef, 
                                                    xfaPdfGenerator, null);
        // INC-13-CONVERSION
        final SigFieldDefn[]    inc13conversionSigfieldDef = {
                             new SigFieldDefn(true, "DIN1", false, new String[] {"declarantDIN"},
                             JsonParserConstants.EMPTY_STRING)};
                
        currInstance.createFormConfigAndPopulateMap(totalForms++, 1, 20, 
                                                    INC13_CONVERSION_REQ_FORMNAME, 
                                                    INC13_CONVERSION_MSTR_FORMNAME, 
                                                    INC13_CONVERSION_TEMPLATE_FILE_NAME, true, 
                                                    false, AppConstants.DIN, 
                                                    inc13conversionSigfieldDef, xfaPdfGenerator, 
                                                    null);
                
        // INC-33-CONVERSION
        final SigFieldDefn[] inc33conversionSigfieldDef = {
            new SigFieldDefn(true, "DIN1", false, new String[] {"declarantDIN"},
                             JsonParserConstants.EMPTY_STRING)};
                        
        currInstance.createFormConfigAndPopulateMap(totalForms++, 1, 20,
                                                    INC33_CONVERSION_REQ_FORMNAME,
                                                    INC33_CONVERSION_MSTR_FORMNAME,
                                                    INC33_CONVERSION_TEMPLATE_FILE_NAME, true, 
                                                    false, AppConstants.DIN, 
                                                    inc33conversionSigfieldDef, xfaPdfGenerator,
                                                    null);
        
        // RUN-LLP
        final SigFieldDefn[] runllpSigfieldDef = {new SigFieldDefn(true, "DIN1", true, 
                                                  new String[] {"Page 1 of 1"},
                                                  JsonParserConstants.EMPTY_STRING)};
                
        currInstance.createFormConfigAndPopulateMap(totalForms++, 1, 20,
                                                    RUNLLP_REQ_FORMNAME,
                                                    RUNLLP_MSTR_FORMNAME,
                                                    RUNLLP_TEMPLATE_FILE_NAME, true, 
                                                    false, AppConstants.DIN, 
                                                    runllpSigfieldDef, xfaPdfGenerator,
                                                    null);
        // INC-20A
        final SigFieldDefn[] inc20aSigfieldDef = {
           new SigFieldDefn(true, "DIN1", false, new String[] {"membershipNumber","practiceNumber"},
                             JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN2", false, new String[] {"declarationID"},
                    JsonParserConstants.EMPTY_STRING)};
                
        currInstance.createFormConfigAndPopulateMap(totalForms++, 2, 20, INC20A_REQ_FORMNAME,
                                                    INC20A_MSTR_FORMNAME, INC20A_TEMPLATE_FILE_NAME,
                                                    true, false, AppConstants.DIN, 
                                                    inc20aSigfieldDef, xfaPdfGenerator, null);
        // LLP FORM 11
        final SigFieldDefn[] llpform11aSigfieldDef = {
           new SigFieldDefn(true, "DIN1", false, new String[] {"digitallySignDSC"},
                             JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN2", false, new String[] {"dpinDesignatedPartnerTextbox",
                            "companySecretaryInPtacticeValue"}, JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN3", false, new String[] {"companySecretaryInPtacticeValue"},
                               JsonParserConstants.EMPTY_STRING)};

        currInstance.createFormConfigAndPopulateMap(totalForms++, 3, 20, FORM_11_REQ_FORMNAME,
                                                    FORM_11_MSTR_FORMNAME,
                                                    FORM_11_TEMPLATE_FILE_NAME, true, 
                                                    false, AppConstants.DIN, 
                                                    llpform11aSigfieldDef, xfaPdfGenerator,
                                                    null);
       
        
        // LLP FORM 8
        final SigFieldDefn[] llpform8aSigfieldDef = {
           new SigFieldDefn(true, "DIN1", false, new String[] {"dpinIncometaxPAN11"},
                             		 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN2", false, new String[] {"dpinIncometaxPAN12"},
                                     JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN3", false, new String[] {"personIncomeTaxPAN"},
                   					 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN8", false, new String[] {"dpinIncometaxPANMembershipNumber"},
                   					 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN9", false, new String[] {"membershipNoOrCertificateNo"},
                   					 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN5", false, new String[] {"dpinIncometaxPAN14"},
                   					 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN6", false, new String[] {"designation"},
                   					 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN7", false, new String[] {"digitallySignDesignation"},
                   					 JsonParserConstants.EMPTY_STRING),
           new SigFieldDefn(true, "DIN4", false, new String[] {"membershipNoOrCertificateNoChg"},
                   					 JsonParserConstants.EMPTY_STRING)};
                
        currInstance.createFormConfigAndPopulateMap(totalForms++, 9, 20, FORM_8_REQ_FORMNAME,
                                                    FORM_8_MSTR_FORMNAME,
                                                    FORM_8_TEMPLATE_FILE_NAME, true, 
                                                    false, AppConstants.DIN, 
                                                    llpform8aSigfieldDef, xfaPdfGenerator,
                                                    null);
        
        
    }
    
    private  boolean isSupported(String formType)
    {
        return (formsByReqForNameMap.containsKey(formType.toUpperCase())); 
//                || formsByMasterNameMap.containsKey(formType.toUpperCase()));
    }

    private FormsConfig getFormConfig(final String reqType)
    {
        FormsConfig    result = formsByReqForNameMap.get(reqType.toUpperCase());
        
//        if (result == null)
//        {
//            result = formsByMasterNameMap.get(reqType.toUpperCase());
//        }
        return result;
    }
    
    private void createFormConfigAndPopulateMap(final int formId, final int signatureCount, 
            final int attachmentCount, final String requestFormName, final String masterFormName, 
            final String templateFileName, final boolean useConfigDSCCount,
            final boolean useWithHashmarkRegEx, final String sigFieldPrefix,
            final SigFieldDefn[] fieldDef, final PdfOutputGenerator pdfGeneratorToUse,
            final Class<?> formModel) throws IOException, PDFGenException
    {
        byte[]    templateFileContent = Files.readAllBytes(Paths.get(
                                            sysconfig.getTemplatesBasePath() + templateFileName));
        
        LOGGER.debug("Input Form data for form RequestFormName = {}, MasterFormName = {}, ",
                     requestFormName, masterFormName);
        
        final FormBatchMaster    formMasterData = dbHelper.getFormMasterByName(masterFormName);

        LOGGER.debug("Retrieved Form master data as {}",
                     (formMasterData == null) ? "null" : formMasterData);
            
        final FormsConfig    formConfig = new FormsConfig(formId, signatureCount, attachmentCount, 
                                           masterFormName, requestFormName, useConfigDSCCount, 
                                           templateFileName, useWithHashmarkRegEx, sigFieldPrefix, 
                                           pdfGeneratorToUse, formModel, templateFileContent, 
                                           formMasterData, fieldDef);

        LOGGER.info("Loaded Form data for form RequestFormName = {}, MasterFormName = {}, "
                    + "FormMasterData FormName = {} ", requestFormName, masterFormName, 
                    formMasterData.getFormName());
    
//        formsByMasterNameMap.put(formConfig.getMasterFormName().toUpperCase(), formConfig);
        formsByReqForNameMap.put(formConfig.getRequestFormName().toUpperCase(), formConfig);
    }
}