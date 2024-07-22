
package com.mca.pdfgen.beans.forms.inc34conv;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "activityFileSize",
    "addedBy",
    "attachmentCategory",
    "activityFileSrcType",
    "folderId",
    "publicDocument",
    "totalNoOfPages",
    "activityFileExt",
    "attachmentDMSId",
    "versionNo",
    "folderName",
    "activityFileName",
    "activityComments",
    "activityFileDate",
    "attachmentLabel"
})
@Generated("jsonschema2pojo")
public class FormAttachment {

    @JsonProperty("activityFileSize")
    private String activityFileSize;
    @JsonProperty("addedBy")
    private String addedBy;
    @JsonProperty("attachmentCategory")
    private String attachmentCategory;
    @JsonProperty("activityFileSrcType")
    private String activityFileSrcType;
    @JsonProperty("folderId")
    private String folderId;
    @JsonProperty("publicDocument")
    private String publicDocument;
    @JsonProperty("totalNoOfPages")
    private String totalNoOfPages;
    @JsonProperty("activityFileExt")
    private String activityFileExt;
    @JsonProperty("attachmentDMSId")
    private String attachmentDMSId;
    @JsonProperty("versionNo")
    private String versionNo;
    @JsonProperty("folderName")
    private String folderName;
    @JsonProperty("activityFileName")
    private String activityFileName;
    @JsonProperty("activityComments")
    private String activityComments;
    @JsonProperty("activityFileDate")
    private String activityFileDate;
    @JsonProperty("attachmentLabel")
    private String attachmentLabel;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("activityFileSize")
    public String getActivityFileSize() {
        return activityFileSize;
    }

    @JsonProperty("activityFileSize")
    public void setActivityFileSize(String activityFileSize) {
        this.activityFileSize = activityFileSize;
    }

    public FormAttachment withActivityFileSize(String activityFileSize) {
        this.activityFileSize = activityFileSize;
        return this;
    }

    @JsonProperty("addedBy")
    public String getAddedBy() {
        return addedBy;
    }

    @JsonProperty("addedBy")
    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public FormAttachment withAddedBy(String addedBy) {
        this.addedBy = addedBy;
        return this;
    }

    @JsonProperty("attachmentCategory")
    public String getAttachmentCategory() {
        return attachmentCategory;
    }

    @JsonProperty("attachmentCategory")
    public void setAttachmentCategory(String attachmentCategory) {
        this.attachmentCategory = attachmentCategory;
    }

    public FormAttachment withAttachmentCategory(String attachmentCategory) {
        this.attachmentCategory = attachmentCategory;
        return this;
    }

    @JsonProperty("activityFileSrcType")
    public String getActivityFileSrcType() {
        return activityFileSrcType;
    }

    @JsonProperty("activityFileSrcType")
    public void setActivityFileSrcType(String activityFileSrcType) {
        this.activityFileSrcType = activityFileSrcType;
    }

    public FormAttachment withActivityFileSrcType(String activityFileSrcType) {
        this.activityFileSrcType = activityFileSrcType;
        return this;
    }

    @JsonProperty("folderId")
    public String getFolderId() {
        return folderId;
    }

    @JsonProperty("folderId")
    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public FormAttachment withFolderId(String folderId) {
        this.folderId = folderId;
        return this;
    }

    @JsonProperty("publicDocument")
    public String getPublicDocument() {
        return publicDocument;
    }

    @JsonProperty("publicDocument")
    public void setPublicDocument(String publicDocument) {
        this.publicDocument = publicDocument;
    }

    public FormAttachment withPublicDocument(String publicDocument) {
        this.publicDocument = publicDocument;
        return this;
    }

    @JsonProperty("totalNoOfPages")
    public String getTotalNoOfPages() {
        return totalNoOfPages;
    }

    @JsonProperty("totalNoOfPages")
    public void setTotalNoOfPages(String totalNoOfPages) {
        this.totalNoOfPages = totalNoOfPages;
    }

    public FormAttachment withTotalNoOfPages(String totalNoOfPages) {
        this.totalNoOfPages = totalNoOfPages;
        return this;
    }

    @JsonProperty("activityFileExt")
    public String getActivityFileExt() {
        return activityFileExt;
    }

    @JsonProperty("activityFileExt")
    public void setActivityFileExt(String activityFileExt) {
        this.activityFileExt = activityFileExt;
    }

    public FormAttachment withActivityFileExt(String activityFileExt) {
        this.activityFileExt = activityFileExt;
        return this;
    }

    @JsonProperty("attachmentDMSId")
    public String getAttachmentDMSId() {
        return attachmentDMSId;
    }

    @JsonProperty("attachmentDMSId")
    public void setAttachmentDMSId(String attachmentDMSId) {
        this.attachmentDMSId = attachmentDMSId;
    }

    public FormAttachment withAttachmentDMSId(String attachmentDMSId) {
        this.attachmentDMSId = attachmentDMSId;
        return this;
    }

    @JsonProperty("versionNo")
    public String getVersionNo() {
        return versionNo;
    }

    @JsonProperty("versionNo")
    public void setVersionNo(String versionNo) {
        this.versionNo = versionNo;
    }

    public FormAttachment withVersionNo(String versionNo) {
        this.versionNo = versionNo;
        return this;
    }

    @JsonProperty("folderName")
    public String getFolderName() {
        return folderName;
    }

    @JsonProperty("folderName")
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public FormAttachment withFolderName(String folderName) {
        this.folderName = folderName;
        return this;
    }

    @JsonProperty("activityFileName")
    public String getActivityFileName() {
        return activityFileName;
    }

    @JsonProperty("activityFileName")
    public void setActivityFileName(String activityFileName) {
        this.activityFileName = activityFileName;
    }

    public FormAttachment withActivityFileName(String activityFileName) {
        this.activityFileName = activityFileName;
        return this;
    }

    @JsonProperty("activityComments")
    public String getActivityComments() {
        return activityComments;
    }

    @JsonProperty("activityComments")
    public void setActivityComments(String activityComments) {
        this.activityComments = activityComments;
    }

    public FormAttachment withActivityComments(String activityComments) {
        this.activityComments = activityComments;
        return this;
    }

    @JsonProperty("activityFileDate")
    public String getActivityFileDate() {
        return activityFileDate;
    }

    @JsonProperty("activityFileDate")
    public void setActivityFileDate(String activityFileDate) {
        this.activityFileDate = activityFileDate;
    }

    public FormAttachment withActivityFileDate(String activityFileDate) {
        this.activityFileDate = activityFileDate;
        return this;
    }

    @JsonProperty("attachmentLabel")
    public String getAttachmentLabel() {
        return attachmentLabel;
    }

    @JsonProperty("attachmentLabel")
    public void setAttachmentLabel(String attachmentLabel) {
        this.attachmentLabel = attachmentLabel;
    }

    public FormAttachment withAttachmentLabel(String attachmentLabel) {
        this.attachmentLabel = attachmentLabel;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public FormAttachment withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FormAttachment.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("activityFileSize");
        sb.append('=');
        sb.append(((this.activityFileSize == null)?"<null>":this.activityFileSize));
        sb.append(',');
        sb.append("addedBy");
        sb.append('=');
        sb.append(((this.addedBy == null)?"<null>":this.addedBy));
        sb.append(',');
        sb.append("attachmentCategory");
        sb.append('=');
        sb.append(((this.attachmentCategory == null)?"<null>":this.attachmentCategory));
        sb.append(',');
        sb.append("activityFileSrcType");
        sb.append('=');
        sb.append(((this.activityFileSrcType == null)?"<null>":this.activityFileSrcType));
        sb.append(',');
        sb.append("folderId");
        sb.append('=');
        sb.append(((this.folderId == null)?"<null>":this.folderId));
        sb.append(',');
        sb.append("publicDocument");
        sb.append('=');
        sb.append(((this.publicDocument == null)?"<null>":this.publicDocument));
        sb.append(',');
        sb.append("totalNoOfPages");
        sb.append('=');
        sb.append(((this.totalNoOfPages == null)?"<null>":this.totalNoOfPages));
        sb.append(',');
        sb.append("activityFileExt");
        sb.append('=');
        sb.append(((this.activityFileExt == null)?"<null>":this.activityFileExt));
        sb.append(',');
        sb.append("attachmentDMSId");
        sb.append('=');
        sb.append(((this.attachmentDMSId == null)?"<null>":this.attachmentDMSId));
        sb.append(',');
        sb.append("versionNo");
        sb.append('=');
        sb.append(((this.versionNo == null)?"<null>":this.versionNo));
        sb.append(',');
        sb.append("folderName");
        sb.append('=');
        sb.append(((this.folderName == null)?"<null>":this.folderName));
        sb.append(',');
        sb.append("activityFileName");
        sb.append('=');
        sb.append(((this.activityFileName == null)?"<null>":this.activityFileName));
        sb.append(',');
        sb.append("activityComments");
        sb.append('=');
        sb.append(((this.activityComments == null)?"<null>":this.activityComments));
        sb.append(',');
        sb.append("activityFileDate");
        sb.append('=');
        sb.append(((this.activityFileDate == null)?"<null>":this.activityFileDate));
        sb.append(',');
        sb.append("attachmentLabel");
        sb.append('=');
        sb.append(((this.attachmentLabel == null)?"<null>":this.attachmentLabel));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.activityFileSize == null)? 0 :this.activityFileSize.hashCode()));
        result = ((result* 31)+((this.addedBy == null)? 0 :this.addedBy.hashCode()));
        result = ((result* 31)+((this.attachmentCategory == null)? 0 :this.attachmentCategory.hashCode()));
        result = ((result* 31)+((this.activityFileSrcType == null)? 0 :this.activityFileSrcType.hashCode()));
        result = ((result* 31)+((this.folderId == null)? 0 :this.folderId.hashCode()));
        result = ((result* 31)+((this.publicDocument == null)? 0 :this.publicDocument.hashCode()));
        result = ((result* 31)+((this.totalNoOfPages == null)? 0 :this.totalNoOfPages.hashCode()));
        result = ((result* 31)+((this.activityFileExt == null)? 0 :this.activityFileExt.hashCode()));
        result = ((result* 31)+((this.attachmentDMSId == null)? 0 :this.attachmentDMSId.hashCode()));
        result = ((result* 31)+((this.versionNo == null)? 0 :this.versionNo.hashCode()));
        result = ((result* 31)+((this.folderName == null)? 0 :this.folderName.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.activityFileName == null)? 0 :this.activityFileName.hashCode()));
        result = ((result* 31)+((this.activityComments == null)? 0 :this.activityComments.hashCode()));
        result = ((result* 31)+((this.activityFileDate == null)? 0 :this.activityFileDate.hashCode()));
        result = ((result* 31)+((this.attachmentLabel == null)? 0 :this.attachmentLabel.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FormAttachment) == false) {
            return false;
        }
        FormAttachment rhs = ((FormAttachment) other);
        return (((((((((((((((((this.activityFileSize == rhs.activityFileSize)||((this.activityFileSize!= null)&&this.activityFileSize.equals(rhs.activityFileSize)))&&((this.addedBy == rhs.addedBy)||((this.addedBy!= null)&&this.addedBy.equals(rhs.addedBy))))&&((this.attachmentCategory == rhs.attachmentCategory)||((this.attachmentCategory!= null)&&this.attachmentCategory.equals(rhs.attachmentCategory))))&&((this.activityFileSrcType == rhs.activityFileSrcType)||((this.activityFileSrcType!= null)&&this.activityFileSrcType.equals(rhs.activityFileSrcType))))&&((this.folderId == rhs.folderId)||((this.folderId!= null)&&this.folderId.equals(rhs.folderId))))&&((this.publicDocument == rhs.publicDocument)||((this.publicDocument!= null)&&this.publicDocument.equals(rhs.publicDocument))))&&((this.totalNoOfPages == rhs.totalNoOfPages)||((this.totalNoOfPages!= null)&&this.totalNoOfPages.equals(rhs.totalNoOfPages))))&&((this.activityFileExt == rhs.activityFileExt)||((this.activityFileExt!= null)&&this.activityFileExt.equals(rhs.activityFileExt))))&&((this.attachmentDMSId == rhs.attachmentDMSId)||((this.attachmentDMSId!= null)&&this.attachmentDMSId.equals(rhs.attachmentDMSId))))&&((this.versionNo == rhs.versionNo)||((this.versionNo!= null)&&this.versionNo.equals(rhs.versionNo))))&&((this.folderName == rhs.folderName)||((this.folderName!= null)&&this.folderName.equals(rhs.folderName))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.activityFileName == rhs.activityFileName)||((this.activityFileName!= null)&&this.activityFileName.equals(rhs.activityFileName))))&&((this.activityComments == rhs.activityComments)||((this.activityComments!= null)&&this.activityComments.equals(rhs.activityComments))))&&((this.activityFileDate == rhs.activityFileDate)||((this.activityFileDate!= null)&&this.activityFileDate.equals(rhs.activityFileDate))))&&((this.attachmentLabel == rhs.attachmentLabel)||((this.attachmentLabel!= null)&&this.attachmentLabel.equals(rhs.attachmentLabel))));
    }

}
