package com.mca.pdfgen.beans.db;

import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import com.mca.pdfgen.constants.AppConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@DynamicUpdate
@Table(name = AppConstants.DB_TABLE_GENERATEPDF, schema = AppConstants.DB_SCHEMA)
public class PDFGenRequestRec 
{
	@Column (name = "SRNREF")
	private String srnRef;
	
	@Column (name = "CREATED")
	private Instant creationTime;
	
	@Lob
	@Column (name = "PDFJSON")
	private String formData;

	@Lob
	@Column (name="FORMMETA")
	private String formMetaData;
	
	@Column (name = "REFER")
	private String formType;
	
	@Column (name = "REQESTTYPE")
	private String requestType;
	
	@Column (name = "PDFGENERATED")
	private String pdfGeneratedFlag;
	
	@Column (name = "GENCOUNTER")
	private int retriesCount;
	
	@Column (name = "DSCCOUNT")
	private int dscCount;
	
	@Column (name = "LOCK_FLG")
	private String lockFlag;
	
	@Column (name = "LOCK_DT")
	private Instant lockDate;
	
	@Column (name = "UNLOCK_DT")
	private Instant unLockDate;
	
	@Id
	@Column (name = "SEQ")
	private long seqNo;
	
	@Column (name = "DMSID")
	private String dmsId;
	
	@Column (name = "PDFMODE")
	private String pdfMode;  
	
	@Column (name = "FAILUREREASON")
	private String failureReason;
	
	@Column (name = "LAST_UPD")
	private Instant lastUpdateTime;
	
	@Column (name = "FORM_PRIORITY")
	private int formPriority;
	
	@Column (name = "PROCESS_DATA")
	private String processData;   
	
	@Column (name = "PROCESS_STATUS")
	private int processStatus;
	
	@Column (name = "BATCH_ID")
	private int batchId;    
	
	@Column (name = "BATCH_TYPE")
	private int batchType;   
}
