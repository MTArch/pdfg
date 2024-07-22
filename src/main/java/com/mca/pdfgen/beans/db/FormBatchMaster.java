package com.mca.pdfgen.beans.db;

import java.time.Instant;

import org.hibernate.annotations.DynamicUpdate;

import com.mca.pdfgen.constants.AppConstants;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@DynamicUpdate
@Table(name = AppConstants.DB_TABLE_FORM_MASTER, schema = AppConstants.DB_SCHEMA)
public class FormBatchMaster
{
	@Id
	@Column (name = "FORM_NAME")
	private String formName;
	
	@Column (name = "BATCH_ID")
	private int batchId;
	
	@Column (name = "LIVE_DT")
	private Instant liveDate;
	
    @Column (name = "FORM_PRIORITY")
    private int formPriority;
	
    @Column (name = "FORM_TYPE")
    private String formType;
	
	@Column (name = "DYNAMIC_ATTACHMENTS")
	private int dynamicAttachments;
	
	@Column (name = "DYNAMIC_DSC")
	private int dynamicDsc;
	
	@Column (name = "BATCH_TYPE")
	private int batchType;
}
