package com.mca.pdfgen.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mca.pdfgen.beans.db.FormBatchMaster;

public interface FormsBatchMasterDAO extends JpaRepository< FormBatchMaster, String> 
{
	FormBatchMaster findByFormName(String formName);	
}
