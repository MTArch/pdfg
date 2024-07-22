package com.mca.pdfgen.dataaccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mca.pdfgen.beans.db.LockResult;
import com.mca.pdfgen.beans.db.PDFGenRequestRec;
import com.mca.pdfgen.constants.AppConstants;

public interface PDFGenRequestRecDAO extends JpaRepository< PDFGenRequestRec, Long> 
{
    PDFGenRequestRec findBySeqNo(Long seqNo);

    @Query (value = "SELECT SEQ AS seqNo, BATCH_ID as batchId, LOCK_FLG as lockFlag" 
            + " FROM " + AppConstants.DB_SCHEMA + "." + AppConstants.DB_TABLE_GENERATEPDF
            + " WHERE"
            + " PDFGENERATED ='N'"
            + " AND LOCK_FLG='N'"
            + " AND PDFMODE = 'ASYNC'"
            + " AND BATCH_TYPE = :BATCHTYPE"
            + " AND PROCESS_STATUS >= :STATUSMIN"
            + " AND PROCESS_STATUS < :STATUSMAX"
            + " AND GENCOUNTER >  :GENCOUNTERMIN"
            + " AND GENCOUNTER <= :GENCOUNTERMAX"            
            + " AND FORM_PRIORITY >= :FORMPRIORITYMIN"
            + " AND FORM_PRIORITY <= :FORMPRIORITYMAX"
            + " AND LAST_UPD < (systimestamp - numtodsinterval(:RETRYDELAY,'SECOND'))"
            + " AND ROWNUM <= :NROWS"
            + " ORDER BY LAST_UPD ASC FOR UPDATE", nativeQuery = true)
    List<LockResult> findSeqList_Retry( 
                    @Param("BATCHTYPE") int batchType, 
                    @Param("STATUSMIN") int statusmin, 
                    @Param("STATUSMAX") int statusmax,
                    @Param("GENCOUNTERMIN") int gencounterMin,
                    @Param("GENCOUNTERMAX") int gencounterMax,
                    @Param ("FORMPRIORITYMIN") int formprioritymin, 
                    @Param("FORMPRIORITYMAX") int formprioritymax, 
                    @Param("RETRYDELAY") int retryDelay,
                    @Param("NROWS") int nrows);
    
    @Query (value = "SELECT SEQ AS seqNo, BATCH_ID as batchId, LOCK_FLG as lockFlag" 
            + " FROM " + AppConstants.DB_SCHEMA + "." + AppConstants.DB_TABLE_GENERATEPDF
            + " WHERE"
            + " PDFGENERATED ='N'"
            + " AND LOCK_FLG='N'"
            + " AND PDFMODE = 'ASYNC'"
            + " AND BATCH_TYPE = :BATCHTYPE"
            + " AND PROCESS_STATUS = :PROCSTATUS"
            + " AND GENCOUNTER = :GENCOUNTER"
            + " AND FORM_PRIORITY >= :FORMPRIORITYMIN"
            + " AND FORM_PRIORITY <= :FORMPRIORITYMAX"
            + " AND ROWNUM <= :NROWS"
            + " ORDER BY LAST_UPD ASC FOR UPDATE", nativeQuery = true)
    List<LockResult> findSeqList_Normal( 
                    @Param("BATCHTYPE") int batchType, 
                    @Param("PROCSTATUS") int status, 
                    @Param("GENCOUNTER") int retriesCount,
                    @Param ("FORMPRIORITYMIN") int formPriorityMin, 
                    @Param("FORMPRIORITYMAX") int formPriorityMax, 
                    @Param("NROWS") int numRows);

    @Modifying
    @Query(value = "UPDATE " + AppConstants.DB_SCHEMA + "." + AppConstants.DB_TABLE_GENERATEPDF
                   + " SET LOCK_FLG='N' WHERE BATCH_ID = :BATCHID AND LOCK_FLG='Y'", 
                   nativeQuery=true)
    int resetLockedRecords(@Param("BATCHID") int batchId);

    @Modifying
    @Query(value = "UPDATE " + AppConstants.DB_SCHEMA + "." + AppConstants.DB_TABLE_GENERATEPDF 
                   + " SET LOCK_FLG='Y', BATCH_ID = :BATCHID, LOCK_DT = SYSDATE WHERE LOCK_FLG='N' "
                   + "AND SEQ IN :SEQIDS", nativeQuery=true)
    int lockRecords(@Param("BATCHID") int batchId, @Param("SEQIDS") List<Long> seqIdsList);

    @Query (value = "SELECT SYSDATE FROM DUAL", nativeQuery = true)
    String getDBSysDate(); 

}
