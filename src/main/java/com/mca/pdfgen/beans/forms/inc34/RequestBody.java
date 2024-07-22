
package com.mca.pdfgen.beans.forms.inc34;

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
    "requestType",
    "referenceNumber",
    "serveAction",
    "linking",
    "formName",
    "integrationId",
    "formData",
    "formDescription",
    "formVersion",
    "parentServiceRequestId",
    "userId",
    "srn"
})
@Generated("jsonschema2pojo")
public class RequestBody {

    @JsonProperty("requestType")
    private String requestType;
    @JsonProperty("referenceNumber")
    private String referenceNumber;
    @JsonProperty("serveAction")
    private String serveAction;
    @JsonProperty("linking")
    private String linking;
    @JsonProperty("formName")
    private String formName;
    @JsonProperty("integrationId")
    private String integrationId;
    @JsonProperty("formData")
    private FormData formData;
    @JsonProperty("formDescription")
    private String formDescription;
    @JsonProperty("formVersion")
    private String formVersion;
    @JsonProperty("parentServiceRequestId")
    private String parentServiceRequestId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("srn")
    private String srn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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
        sb.append("requestType");
        sb.append('=');
        sb.append(((this.requestType == null)?"<null>":this.requestType));
        sb.append(',');
        sb.append("referenceNumber");
        sb.append('=');
        sb.append(((this.referenceNumber == null)?"<null>":this.referenceNumber));
        sb.append(',');
        sb.append("serveAction");
        sb.append('=');
        sb.append(((this.serveAction == null)?"<null>":this.serveAction));
        sb.append(',');
        sb.append("linking");
        sb.append('=');
        sb.append(((this.linking == null)?"<null>":this.linking));
        sb.append(',');
        sb.append("formName");
        sb.append('=');
        sb.append(((this.formName == null)?"<null>":this.formName));
        sb.append(',');
        sb.append("integrationId");
        sb.append('=');
        sb.append(((this.integrationId == null)?"<null>":this.integrationId));
        sb.append(',');
        sb.append("formData");
        sb.append('=');
        sb.append(((this.formData == null)?"<null>":this.formData));
        sb.append(',');
        sb.append("formDescription");
        sb.append('=');
        sb.append(((this.formDescription == null)?"<null>":this.formDescription));
        sb.append(',');
        sb.append("formVersion");
        sb.append('=');
        sb.append(((this.formVersion == null)?"<null>":this.formVersion));
        sb.append(',');
        sb.append("parentServiceRequestId");
        sb.append('=');
        sb.append(((this.parentServiceRequestId == null)?"<null>":this.parentServiceRequestId));
        sb.append(',');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null)?"<null>":this.userId));
        sb.append(',');
        sb.append("srn");
        sb.append('=');
        sb.append(((this.srn == null)?"<null>":this.srn));
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
        result = ((result* 31)+((this.requestType == null)? 0 :this.requestType.hashCode()));
        result = ((result* 31)+((this.serveAction == null)? 0 :this.serveAction.hashCode()));
        result = ((result* 31)+((this.linking == null)? 0 :this.linking.hashCode()));
        result = ((result* 31)+((this.integrationId == null)? 0 :this.integrationId.hashCode()));
        result = ((result* 31)+((this.formDescription == null)? 0 :this.formDescription.hashCode()));
        result = ((result* 31)+((this.formVersion == null)? 0 :this.formVersion.hashCode()));
        result = ((result* 31)+((this.userId == null)? 0 :this.userId.hashCode()));
        result = ((result* 31)+((this.srn == null)? 0 :this.srn.hashCode()));
        result = ((result* 31)+((this.referenceNumber == null)? 0 :this.referenceNumber.hashCode()));
        result = ((result* 31)+((this.formName == null)? 0 :this.formName.hashCode()));
        result = ((result* 31)+((this.formData == null)? 0 :this.formData.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.parentServiceRequestId == null)? 0 :this.parentServiceRequestId.hashCode()));
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
        return ((((((((((((((this.requestType == rhs.requestType)||((this.requestType!= null)&&this.requestType.equals(rhs.requestType)))&&((this.serveAction == rhs.serveAction)||((this.serveAction!= null)&&this.serveAction.equals(rhs.serveAction))))&&((this.linking == rhs.linking)||((this.linking!= null)&&this.linking.equals(rhs.linking))))&&((this.integrationId == rhs.integrationId)||((this.integrationId!= null)&&this.integrationId.equals(rhs.integrationId))))&&((this.formDescription == rhs.formDescription)||((this.formDescription!= null)&&this.formDescription.equals(rhs.formDescription))))&&((this.formVersion == rhs.formVersion)||((this.formVersion!= null)&&this.formVersion.equals(rhs.formVersion))))&&((this.userId == rhs.userId)||((this.userId!= null)&&this.userId.equals(rhs.userId))))&&((this.srn == rhs.srn)||((this.srn!= null)&&this.srn.equals(rhs.srn))))&&((this.referenceNumber == rhs.referenceNumber)||((this.referenceNumber!= null)&&this.referenceNumber.equals(rhs.referenceNumber))))&&((this.formName == rhs.formName)||((this.formName!= null)&&this.formName.equals(rhs.formName))))&&((this.formData == rhs.formData)||((this.formData!= null)&&this.formData.equals(rhs.formData))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.parentServiceRequestId == rhs.parentServiceRequestId)||((this.parentServiceRequestId!= null)&&this.parentServiceRequestId.equals(rhs.parentServiceRequestId))));
    }

}
