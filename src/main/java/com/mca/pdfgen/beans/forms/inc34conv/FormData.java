
package com.mca.pdfgen.beans.forms.inc34conv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    "articleDocumentId",
    "aoaFlag",
    "companyName",
    "declarantName",
    "declarantResolutionNumber",
    "declarantAmendmentDetails1",
    "declarantAmendmentDetails2",
    "declarantAmendmentDetails3",
    "tableName",
    "declarantAmendmentDetails4",
    "declarantAmendmentDetails",
    "subscriberDetails",
    "declarantDIN",
    "declarantVerifiedDate",
    "formIntegrationId",
    "table"
})
@Generated("jsonschema2pojo")
public class FormData {

    @JsonProperty("articleDocumentId")
    private String articleDocumentId;
    @JsonProperty("aoaFlag")
    private String aoaFlag;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("declarantName")
    private String declarantName;
    @JsonProperty("declarantResolutionNumber")
    private String declarantResolutionNumber;
    @JsonProperty("declarantAmendmentDetails1")
    private String declarantAmendmentDetails1;
    @JsonProperty("declarantAmendmentDetails2")
    private String declarantAmendmentDetails2;
    @JsonProperty("declarantAmendmentDetails3")
    private String declarantAmendmentDetails3;
    @JsonProperty("tableName")
    private String tableName;
    @JsonProperty("declarantAmendmentDetails4")
    private String declarantAmendmentDetails4;
    @JsonProperty("declarantAmendmentDetails")
    private String declarantAmendmentDetails;
    @JsonProperty("subscriberDetails")
    private List<SubscriberDetail> subscriberDetails = new ArrayList<SubscriberDetail>();
    @JsonProperty("declarantDIN")
    private String declarantDIN;
    @JsonProperty("declarantVerifiedDate")
    private String declarantVerifiedDate;
    @JsonProperty("formIntegrationId")
    private String formIntegrationId;
    @JsonProperty("table")
    private Table table;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("articleDocumentId")
    public String getArticleDocumentId() {
        return articleDocumentId;
    }

    @JsonProperty("articleDocumentId")
    public void setArticleDocumentId(String articleDocumentId) {
        this.articleDocumentId = articleDocumentId;
    }

    public FormData withArticleDocumentId(String articleDocumentId) {
        this.articleDocumentId = articleDocumentId;
        return this;
    }

    @JsonProperty("aoaFlag")
    public String getAoaFlag() {
        return aoaFlag;
    }

    @JsonProperty("aoaFlag")
    public void setAoaFlag(String aoaFlag) {
        this.aoaFlag = aoaFlag;
    }

    public FormData withAoaFlag(String aoaFlag) {
        this.aoaFlag = aoaFlag;
        return this;
    }

    @JsonProperty("companyName")
    public String getCompanyName() {
        return companyName;
    }

    @JsonProperty("companyName")
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public FormData withCompanyName(String companyName) {
        this.companyName = companyName;
        return this;
    }

    @JsonProperty("declarantName")
    public String getDeclarantName() {
        return declarantName;
    }

    @JsonProperty("declarantName")
    public void setDeclarantName(String declarantName) {
        this.declarantName = declarantName;
    }

    public FormData withDeclarantName(String declarantName) {
        this.declarantName = declarantName;
        return this;
    }

    @JsonProperty("declarantResolutionNumber")
    public String getDeclarantResolutionNumber() {
        return declarantResolutionNumber;
    }

    @JsonProperty("declarantResolutionNumber")
    public void setDeclarantResolutionNumber(String declarantResolutionNumber) {
        this.declarantResolutionNumber = declarantResolutionNumber;
    }

    public FormData withDeclarantResolutionNumber(String declarantResolutionNumber) {
        this.declarantResolutionNumber = declarantResolutionNumber;
        return this;
    }

    @JsonProperty("declarantAmendmentDetails1")
    public String getDeclarantAmendmentDetails1() {
        return declarantAmendmentDetails1;
    }

    @JsonProperty("declarantAmendmentDetails1")
    public void setDeclarantAmendmentDetails1(String declarantAmendmentDetails1) {
        this.declarantAmendmentDetails1 = declarantAmendmentDetails1;
    }

    public FormData withDeclarantAmendmentDetails1(String declarantAmendmentDetails1) {
        this.declarantAmendmentDetails1 = declarantAmendmentDetails1;
        return this;
    }

    @JsonProperty("declarantAmendmentDetails2")
    public String getDeclarantAmendmentDetails2() {
        return declarantAmendmentDetails2;
    }

    @JsonProperty("declarantAmendmentDetails2")
    public void setDeclarantAmendmentDetails2(String declarantAmendmentDetails2) {
        this.declarantAmendmentDetails2 = declarantAmendmentDetails2;
    }

    public FormData withDeclarantAmendmentDetails2(String declarantAmendmentDetails2) {
        this.declarantAmendmentDetails2 = declarantAmendmentDetails2;
        return this;
    }

    @JsonProperty("declarantAmendmentDetails3")
    public String getDeclarantAmendmentDetails3() {
        return declarantAmendmentDetails3;
    }

    @JsonProperty("declarantAmendmentDetails3")
    public void setDeclarantAmendmentDetails3(String declarantAmendmentDetails3) {
        this.declarantAmendmentDetails3 = declarantAmendmentDetails3;
    }

    public FormData withDeclarantAmendmentDetails3(String declarantAmendmentDetails3) {
        this.declarantAmendmentDetails3 = declarantAmendmentDetails3;
        return this;
    }

    @JsonProperty("tableName")
    public String getTableName() {
        return tableName;
    }

    @JsonProperty("tableName")
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public FormData withTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    @JsonProperty("declarantAmendmentDetails4")
    public String getDeclarantAmendmentDetails4() {
        return declarantAmendmentDetails4;
    }

    @JsonProperty("declarantAmendmentDetails4")
    public void setDeclarantAmendmentDetails4(String declarantAmendmentDetails4) {
        this.declarantAmendmentDetails4 = declarantAmendmentDetails4;
    }

    public FormData withDeclarantAmendmentDetails4(String declarantAmendmentDetails4) {
        this.declarantAmendmentDetails4 = declarantAmendmentDetails4;
        return this;
    }

    @JsonProperty("declarantAmendmentDetails")
    public String getDeclarantAmendmentDetails() {
        return declarantAmendmentDetails;
    }

    @JsonProperty("declarantAmendmentDetails")
    public void setDeclarantAmendmentDetails(String declarantAmendmentDetails) {
        this.declarantAmendmentDetails = declarantAmendmentDetails;
    }

    public FormData withDeclarantAmendmentDetails(String declarantAmendmentDetails) {
        this.declarantAmendmentDetails = declarantAmendmentDetails;
        return this;
    }

    @JsonProperty("subscriberDetails")
    public List<SubscriberDetail> getSubscriberDetails() {
        return subscriberDetails;
    }

    @JsonProperty("subscriberDetails")
    public void setSubscriberDetails(List<SubscriberDetail> subscriberDetails) {
        this.subscriberDetails = subscriberDetails;
    }

    public FormData withSubscriberDetails(List<SubscriberDetail> subscriberDetails) {
        this.subscriberDetails = subscriberDetails;
        return this;
    }

    @JsonProperty("declarantDIN")
    public String getDeclarantDIN() {
        return declarantDIN;
    }

    @JsonProperty("declarantDIN")
    public void setDeclarantDIN(String declarantDIN) {
        this.declarantDIN = declarantDIN;
    }

    public FormData withDeclarantDIN(String declarantDIN) {
        this.declarantDIN = declarantDIN;
        return this;
    }

    @JsonProperty("declarantVerifiedDate")
    public String getDeclarantVerifiedDate() {
        return declarantVerifiedDate;
    }

    @JsonProperty("declarantVerifiedDate")
    public void setDeclarantVerifiedDate(String declarantVerifiedDate) {
        this.declarantVerifiedDate = declarantVerifiedDate;
    }

    public FormData withDeclarantVerifiedDate(String declarantVerifiedDate) {
        this.declarantVerifiedDate = declarantVerifiedDate;
        return this;
    }

    @JsonProperty("formIntegrationId")
    public String getFormIntegrationId() {
        return formIntegrationId;
    }

    @JsonProperty("formIntegrationId")
    public void setFormIntegrationId(String formIntegrationId) {
        this.formIntegrationId = formIntegrationId;
    }

    public FormData withFormIntegrationId(String formIntegrationId) {
        this.formIntegrationId = formIntegrationId;
        return this;
    }

    @JsonProperty("table")
    public Table getTable() {
        return table;
    }

    @JsonProperty("table")
    public void setTable(Table table) {
        this.table = table;
    }

    public FormData withTable(Table table) {
        this.table = table;
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

    public FormData withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FormData.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("articleDocumentId");
        sb.append('=');
        sb.append(((this.articleDocumentId == null)?"<null>":this.articleDocumentId));
        sb.append(',');
        sb.append("aoaFlag");
        sb.append('=');
        sb.append(((this.aoaFlag == null)?"<null>":this.aoaFlag));
        sb.append(',');
        sb.append("companyName");
        sb.append('=');
        sb.append(((this.companyName == null)?"<null>":this.companyName));
        sb.append(',');
        sb.append("declarantName");
        sb.append('=');
        sb.append(((this.declarantName == null)?"<null>":this.declarantName));
        sb.append(',');
        sb.append("declarantResolutionNumber");
        sb.append('=');
        sb.append(((this.declarantResolutionNumber == null)?"<null>":this.declarantResolutionNumber));
        sb.append(',');
        sb.append("declarantAmendmentDetails1");
        sb.append('=');
        sb.append(((this.declarantAmendmentDetails1 == null)?"<null>":this.declarantAmendmentDetails1));
        sb.append(',');
        sb.append("declarantAmendmentDetails2");
        sb.append('=');
        sb.append(((this.declarantAmendmentDetails2 == null)?"<null>":this.declarantAmendmentDetails2));
        sb.append(',');
        sb.append("declarantAmendmentDetails3");
        sb.append('=');
        sb.append(((this.declarantAmendmentDetails3 == null)?"<null>":this.declarantAmendmentDetails3));
        sb.append(',');
        sb.append("tableName");
        sb.append('=');
        sb.append(((this.tableName == null)?"<null>":this.tableName));
        sb.append(',');
        sb.append("declarantAmendmentDetails4");
        sb.append('=');
        sb.append(((this.declarantAmendmentDetails4 == null)?"<null>":this.declarantAmendmentDetails4));
        sb.append(',');
        sb.append("declarantAmendmentDetails");
        sb.append('=');
        sb.append(((this.declarantAmendmentDetails == null)?"<null>":this.declarantAmendmentDetails));
        sb.append(',');
        sb.append("subscriberDetails");
        sb.append('=');
        sb.append(((this.subscriberDetails == null)?"<null>":this.subscriberDetails));
        sb.append(',');
        sb.append("declarantDIN");
        sb.append('=');
        sb.append(((this.declarantDIN == null)?"<null>":this.declarantDIN));
        sb.append(',');
        sb.append("declarantVerifiedDate");
        sb.append('=');
        sb.append(((this.declarantVerifiedDate == null)?"<null>":this.declarantVerifiedDate));
        sb.append(',');
        sb.append("formIntegrationId");
        sb.append('=');
        sb.append(((this.formIntegrationId == null)?"<null>":this.formIntegrationId));
        sb.append(',');
        sb.append("table");
        sb.append('=');
        sb.append(((this.table == null)?"<null>":this.table));
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
        result = ((result* 31)+((this.articleDocumentId == null)? 0 :this.articleDocumentId.hashCode()));
        result = ((result* 31)+((this.aoaFlag == null)? 0 :this.aoaFlag.hashCode()));
        result = ((result* 31)+((this.companyName == null)? 0 :this.companyName.hashCode()));
        result = ((result* 31)+((this.declarantName == null)? 0 :this.declarantName.hashCode()));
        result = ((result* 31)+((this.declarantResolutionNumber == null)? 0 :this.declarantResolutionNumber.hashCode()));
        result = ((result* 31)+((this.declarantAmendmentDetails1 == null)? 0 :this.declarantAmendmentDetails1 .hashCode()));
        result = ((result* 31)+((this.declarantAmendmentDetails2 == null)? 0 :this.declarantAmendmentDetails2 .hashCode()));
        result = ((result* 31)+((this.declarantAmendmentDetails3 == null)? 0 :this.declarantAmendmentDetails3 .hashCode()));
        result = ((result* 31)+((this.tableName == null)? 0 :this.tableName.hashCode()));
        result = ((result* 31)+((this.declarantAmendmentDetails4 == null)? 0 :this.declarantAmendmentDetails4 .hashCode()));
        result = ((result* 31)+((this.declarantAmendmentDetails == null)? 0 :this.declarantAmendmentDetails.hashCode()));
        result = ((result* 31)+((this.subscriberDetails == null)? 0 :this.subscriberDetails.hashCode()));
        result = ((result* 31)+((this.declarantDIN == null)? 0 :this.declarantDIN.hashCode()));
        result = ((result* 31)+((this.declarantVerifiedDate == null)? 0 :this.declarantVerifiedDate.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.formIntegrationId == null)? 0 :this.formIntegrationId.hashCode()));
        result = ((result* 31)+((this.table == null)? 0 :this.table.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FormData) == false) {
            return false;
        }
        FormData rhs = ((FormData) other);
        return ((((((((((((((((((this.articleDocumentId == rhs.articleDocumentId)||((this.articleDocumentId!= null)&&this.articleDocumentId.equals(rhs.articleDocumentId)))&&((this.aoaFlag == rhs.aoaFlag)||((this.aoaFlag!= null)&&this.aoaFlag.equals(rhs.aoaFlag))))&&((this.companyName == rhs.companyName)||((this.companyName!= null)&&this.companyName.equals(rhs.companyName))))&&((this.declarantName == rhs.declarantName)||((this.declarantName!= null)&&this.declarantName.equals(rhs.declarantName))))&&((this.declarantResolutionNumber == rhs.declarantResolutionNumber)||((this.declarantResolutionNumber!= null)&&this.declarantResolutionNumber.equals(rhs.declarantResolutionNumber))))&&((this.declarantAmendmentDetails1 == rhs.declarantAmendmentDetails1)||((this.declarantAmendmentDetails1 != null)&&this.declarantAmendmentDetails1 .equals(rhs.declarantAmendmentDetails1))))&&((this.declarantAmendmentDetails2 == rhs.declarantAmendmentDetails2)||((this.declarantAmendmentDetails2 != null)&&this.declarantAmendmentDetails2 .equals(rhs.declarantAmendmentDetails2))))&&((this.declarantAmendmentDetails3 == rhs.declarantAmendmentDetails3)||((this.declarantAmendmentDetails3 != null)&&this.declarantAmendmentDetails3 .equals(rhs.declarantAmendmentDetails3))))&&((this.tableName == rhs.tableName)||((this.tableName!= null)&&this.tableName.equals(rhs.tableName))))&&((this.declarantAmendmentDetails4 == rhs.declarantAmendmentDetails4)||((this.declarantAmendmentDetails4 != null)&&this.declarantAmendmentDetails4 .equals(rhs.declarantAmendmentDetails4))))&&((this.declarantAmendmentDetails == rhs.declarantAmendmentDetails)||((this.declarantAmendmentDetails!= null)&&this.declarantAmendmentDetails.equals(rhs.declarantAmendmentDetails))))&&((this.subscriberDetails == rhs.subscriberDetails)||((this.subscriberDetails!= null)&&this.subscriberDetails.equals(rhs.subscriberDetails))))&&((this.declarantDIN == rhs.declarantDIN)||((this.declarantDIN!= null)&&this.declarantDIN.equals(rhs.declarantDIN))))&&((this.declarantVerifiedDate == rhs.declarantVerifiedDate)||((this.declarantVerifiedDate!= null)&&this.declarantVerifiedDate.equals(rhs.declarantVerifiedDate))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.formIntegrationId == rhs.formIntegrationId)||((this.formIntegrationId!= null)&&this.formIntegrationId.equals(rhs.formIntegrationId))))&&((this.table == rhs.table)||((this.table!= null)&&this.table.equals(rhs.table))));
    }

}
