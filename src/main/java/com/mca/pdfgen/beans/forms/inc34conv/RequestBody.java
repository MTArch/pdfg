
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
    "reason",
    "srNumber",
    "requestType",
    "serveAction",
    "linking",
    "prefill",
    "integrationId",
    "CIN",
    "formDescription",
    "formVersion",
    "userId",
    "srn",
    "formAttachment",
    "referenceNumber",
    "formattachList",
    "SRFOStatus",
    "formName",
    "formData",
    "parentServiceRequestId",
    "status"
})
@Generated("jsonschema2pojo")
public class RequestBody {

    @JsonProperty("reason")
    private String reason;
    @JsonProperty("srNumber")
    private String srNumber;
    @JsonProperty("requestType")
    private String requestType;
    @JsonProperty("serveAction")
    private String serveAction;
    @JsonProperty("linking")
    private String linking;
    @JsonProperty("prefill")
    private String prefill;
    @JsonProperty("integrationId")
    private String integrationId;
    @JsonProperty("CIN")
    private String cin;
    @JsonProperty("formDescription")
    private String formDescription;
    @JsonProperty("formVersion")
    private String formVersion;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("srn")
    private String srn;
    @JsonProperty("formAttachment")
    private List<FormAttachment> formAttachment = new ArrayList<FormAttachment>();
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    @JsonProperty("formattachList")
    private String formattachList;
    @JsonProperty("SRFOStatus")
    private String sRFOStatus;
    @JsonProperty("formName")
    private String formName;
    @JsonProperty("formData")
    private FormData formData;
    @JsonProperty("parentServiceRequestId")
    private String parentServiceRequestId;
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("reason")
    public String getReason() {
        return reason;
    }

    @JsonProperty("reason")
    public void setReason(String reason) {
        this.reason = reason;
    }

    public RequestBody withReason(String reason) {
        this.reason = reason;
        return this;
    }

    @JsonProperty("srNumber")
    public String getSrNumber() {
        return srNumber;
    }

    @JsonProperty("srNumber")
    public void setSrNumber(String srNumber) {
        this.srNumber = srNumber;
    }

    public RequestBody withSrNumber(String srNumber) {
        this.srNumber = srNumber;
        return this;
    }

    @JsonProperty("requestType")
    public String getRequestType() {
        return requestType;
    }

    @JsonProperty("requestType")
    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public RequestBody withRequestType(String requestType) {
        this.requestType = requestType;
        return this;
    }

    @JsonProperty("serveAction")
    public String getServeAction() {
        return serveAction;
    }

    @JsonProperty("serveAction")
    public void setServeAction(String serveAction) {
        this.serveAction = serveAction;
    }

    public RequestBody withServeAction(String serveAction) {
        this.serveAction = serveAction;
        return this;
    }

    @JsonProperty("linking")
    public String getLinking() {
        return linking;
    }

    @JsonProperty("linking")
    public void setLinking(String linking) {
        this.linking = linking;
    }

    public RequestBody withLinking(String linking) {
        this.linking = linking;
        return this;
    }

    @JsonProperty("prefill")
    public String getPrefill() {
        return prefill;
    }

    @JsonProperty("prefill")
    public void setPrefill(String prefill) {
        this.prefill = prefill;
    }

    public RequestBody withPrefill(String prefill) {
        this.prefill = prefill;
        return this;
    }

    @JsonProperty("integrationId")
    public String getIntegrationId() {
        return integrationId;
    }

    @JsonProperty("integrationId")
    public void setIntegrationId(String integrationId) {
        this.integrationId = integrationId;
    }

    public RequestBody withIntegrationId(String integrationId) {
        this.integrationId = integrationId;
        return this;
    }

    @JsonProperty("CIN")
    public String getCin() {
        return cin;
    }

    @JsonProperty("CIN")
    public void setCin(String cin) {
        this.cin = cin;
    }

    public RequestBody withCin(String cin) {
        this.cin = cin;
        return this;
    }

    @JsonProperty("formDescription")
    public String getFormDescription() {
        return formDescription;
    }

    @JsonProperty("formDescription")
    public void setFormDescription(String formDescription) {
        this.formDescription = formDescription;
    }

    public RequestBody withFormDescription(String formDescription) {
        this.formDescription = formDescription;
        return this;
    }

    @JsonProperty("formVersion")
    public String getFormVersion() {
        return formVersion;
    }

    @JsonProperty("formVersion")
    public void setFormVersion(String formVersion) {
        this.formVersion = formVersion;
    }

    public RequestBody withFormVersion(String formVersion) {
        this.formVersion = formVersion;
        return this;
    }

    @JsonProperty("userId")
    public String getUserId() {
        return userId;
    }

    @JsonProperty("userId")
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RequestBody withUserId(String userId) {
        this.userId = userId;
        return this;
    }

    @JsonProperty("srn")
    public String getSrn() {
        return srn;
    }

    @JsonProperty("srn")
    public void setSrn(String srn) {
        this.srn = srn;
    }

    public RequestBody withSrn(String srn) {
        this.srn = srn;
        return this;
    }

    @JsonProperty("formAttachment")
    public List<FormAttachment> getFormAttachment() {
        return formAttachment;
    }

    @JsonProperty("formAttachment")
    public void setFormAttachment(List<FormAttachment> formAttachment) {
        this.formAttachment = formAttachment;
    }

    public RequestBody withFormAttachment(List<FormAttachment> formAttachment) {
        this.formAttachment = formAttachment;
        return this;
    }

    @JsonProperty("referenceNumber")
    public String getReferenceNumber() {
        return referenceNumber;
    }

    @JsonProperty("referenceNumber")
    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public RequestBody withReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
        return this;
    }

    @JsonProperty("formattachList")
    public String getFormattachList() {
        return formattachList;
    }

    @JsonProperty("formattachList")
    public void setFormattachList(String formattachList) {
        this.formattachList = formattachList;
    }

    public RequestBody withFormattachList(String formattachList) {
        this.formattachList = formattachList;
        return this;
    }

    @JsonProperty("SRFOStatus")
    public String getSRFOStatus() {
        return sRFOStatus;
    }

    @JsonProperty("SRFOStatus")
    public void setSRFOStatus(String sRFOStatus) {
        this.sRFOStatus = sRFOStatus;
    }

    public RequestBody withSRFOStatus(String sRFOStatus) {
        this.sRFOStatus = sRFOStatus;
        return this;
    }

    @JsonProperty("formName")
    public String getFormName() {
        return formName;
    }

    @JsonProperty("formName")
    public void setFormName(String formName) {
        this.formName = formName;
    }

    public RequestBody withFormName(String formName) {
        this.formName = formName;
        return this;
    }

    @JsonProperty("formData")
    public FormData getFormData() {
        return formData;
    }

    @JsonProperty("formData")
    public void setFormData(FormData formData) {
        this.formData = formData;
    }

    public RequestBody withFormData(FormData formData) {
        this.formData = formData;
        return this;
    }

    @JsonProperty("parentServiceRequestId")
    public String getParentServiceRequestId() {
        return parentServiceRequestId;
    }

    @JsonProperty("parentServiceRequestId")
    public void setParentServiceRequestId(String parentServiceRequestId) {
        this.parentServiceRequestId = parentServiceRequestId;
    }

    public RequestBody withParentServiceRequestId(String parentServiceRequestId) {
        this.parentServiceRequestId = parentServiceRequestId;
        return this;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public RequestBody withStatus(String status) {
        this.status = status;
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

    public RequestBody withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RequestBody.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("reason");
        sb.append('=');
        sb.append(((this.reason == null)?"<null>":this.reason));
        sb.append(',');
        sb.append("srNumber");
        sb.append('=');
        sb.append(((this.srNumber == null)?"<null>":this.srNumber));
        sb.append(',');
        sb.append("requestType");
        sb.append('=');
        sb.append(((this.requestType == null)?"<null>":this.requestType));
        sb.append(',');
        sb.append("serveAction");
        sb.append('=');
        sb.append(((this.serveAction == null)?"<null>":this.serveAction));
        sb.append(',');
        sb.append("linking");
        sb.append('=');
        sb.append(((this.linking == null)?"<null>":this.linking));
        sb.append(',');
        sb.append("prefill");
        sb.append('=');
        sb.append(((this.prefill == null)?"<null>":this.prefill));
        sb.append(',');
        sb.append("integrationId");
        sb.append('=');
        sb.append(((this.integrationId == null)?"<null>":this.integrationId));
        sb.append(',');
        sb.append("cin");
        sb.append('=');
        sb.append(((this.cin == null)?"<null>":this.cin));
        sb.append(',');
        sb.append("formDescription");
        sb.append('=');
        sb.append(((this.formDescription == null)?"<null>":this.formDescription));
        sb.append(',');
        sb.append("formVersion");
        sb.append('=');
        sb.append(((this.formVersion == null)?"<null>":this.formVersion));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("srn");
        sb.append('=');
        sb.append(((this.srn == null)?"<null>":this.srn));
        sb.append(',');
        sb.append("formAttachment");
        sb.append('=');
        sb.append(((this.formAttachment == null)?"<null>":this.formAttachment));
        sb.append(',');
        sb.append("referenceNumber");
        sb.append('=');
        sb.append(((this.referenceNumber == null)?"<null>":this.referenceNumber));
        sb.append(',');
        sb.append("formattachList");
        sb.append('=');
        sb.append(((this.formattachList == null)?"<null>":this.formattachList));
        sb.append(',');
        sb.append("sRFOStatus");
        sb.append('=');
        sb.append(((this.sRFOStatus == null)?"<null>":this.sRFOStatus));
        sb.append(',');
        sb.append("formName");
        sb.append('=');
        sb.append(((this.formName == null)?"<null>":this.formName));
        sb.append(',');
        sb.append("formData");
        sb.append('=');
        sb.append(((this.formData == null)?"<null>":this.formData));
        sb.append(',');
        sb.append("parentServiceRequestId");
        sb.append('=');
        sb.append(((this.parentServiceRequestId == null)?"<null>":this.parentServiceRequestId));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
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
        result = ((result* 31)+((this.sRFOStatus == null)? 0 :this.sRFOStatus.hashCode()));
        result = ((result* 31)+((this.reason == null)? 0 :this.reason.hashCode()));
        result = ((result* 31)+((this.srNumber == null)? 0 :this.srNumber.hashCode()));
        result = ((result* 31)+((this.requestType == null)? 0 :this.requestType.hashCode()));
        result = ((result* 31)+((this.serveAction == null)? 0 :this.serveAction.hashCode()));
        result = ((result* 31)+((this.linking == null)? 0 :this.linking.hashCode()));
        result = ((result* 31)+((this.prefill == null)? 0 :this.prefill.hashCode()));
        result = ((result* 31)+((this.integrationId == null)? 0 :this.integrationId.hashCode()));
        result = ((result* 31)+((this.cin == null)? 0 :this.cin.hashCode()));
        result = ((result* 31)+((this.formDescription == null)? 0 :this.formDescription.hashCode()));
        result = ((result* 31)+((this.formVersion == null)? 0 :this.formVersion.hashCode()));
        result = ((result* 31)+((this.userId == null)? 0 :this.userId.hashCode()));
        result = ((result* 31)+((this.srn == null)? 0 :this.srn.hashCode()));
        result = ((result* 31)+((this.formAttachment == null)? 0 :this.formAttachment.hashCode()));
        result = ((result* 31)+((this.referenceNumber == null)? 0 :this.referenceNumber.hashCode()));
        result = ((result* 31)+((this.formattachList == null)? 0 :this.formattachList.hashCode()));
        result = ((result* 31)+((this.formName == null)? 0 :this.formName.hashCode()));
        result = ((result* 31)+((this.formData == null)? 0 :this.formData.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.parentServiceRequestId == null)? 0 :this.parentServiceRequestId.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RequestBody) == false) {
            return false;
        }
        RequestBody rhs = ((RequestBody) other);
        return ((((((((((((((((((((((this.sRFOStatus == rhs.sRFOStatus)||((this.sRFOStatus!= null)&&this.sRFOStatus.equals(rhs.sRFOStatus)))&&((this.reason == rhs.reason)||((this.reason!= null)&&this.reason.equals(rhs.reason))))&&((this.srNumber == rhs.srNumber)||((this.srNumber!= null)&&this.srNumber.equals(rhs.srNumber))))&&((this.requestType == rhs.requestType)||((this.requestType!= null)&&this.requestType.equals(rhs.requestType))))&&((this.serveAction == rhs.serveAction)||((this.serveAction!= null)&&this.serveAction.equals(rhs.serveAction))))&&((this.linking == rhs.linking)||((this.linking!= null)&&this.linking.equals(rhs.linking))))&&((this.prefill == rhs.prefill)||((this.prefill!= null)&&this.prefill.equals(rhs.prefill))))&&((this.integrationId == rhs.integrationId)||((this.integrationId!= null)&&this.integrationId.equals(rhs.integrationId))))&&((this.cin == rhs.cin)||((this.cin!= null)&&this.cin.equals(rhs.cin))))&&((this.formDescription == rhs.formDescription)||((this.formDescription!= null)&&this.formDescription.equals(rhs.formDescription))))&&((this.formVersion == rhs.formVersion)||((this.formVersion!= null)&&this.formVersion.equals(rhs.formVersion))))&&((this.userId == rhs.userId)||((this.userId!= null)&&this.userId.equals(rhs.userId))))&&((this.srn == rhs.srn)||((this.srn!= null)&&this.srn.equals(rhs.srn))))&&((this.formAttachment == rhs.formAttachment)||((this.formAttachment!= null)&&this.formAttachment.equals(rhs.formAttachment))))&&((this.referenceNumber == rhs.referenceNumber)||((this.referenceNumber!= null)&&this.referenceNumber.equals(rhs.referenceNumber))))&&((this.formattachList == rhs.formattachList)||((this.formattachList!= null)&&this.formattachList.equals(rhs.formattachList))))&&((this.formName == rhs.formName)||((this.formName!= null)&&this.formName.equals(rhs.formName))))&&((this.formData == rhs.formData)||((this.formData!= null)&&this.formData.equals(rhs.formData))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.parentServiceRequestId == rhs.parentServiceRequestId)||((this.parentServiceRequestId!= null)&&this.parentServiceRequestId.equals(rhs.parentServiceRequestId))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
