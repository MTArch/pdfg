
package com.mca.pdfgen.beans.forms.inc34;

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
    "witnessDetails",
    "companyName",
    "subscriberDetails",
    "formIntegrationId",
    "table",
    "tableName"
})
@Generated("jsonschema2pojo")
public class FormData {

    @JsonProperty("witnessDetails")
    private WitnessDetails witnessDetails;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("subscriberDetails")
    private List<SubscriberDetail> subscriberDetails = new ArrayList<SubscriberDetail>();
    @JsonProperty("formIntegrationId")
    private String formIntegrationId;
    @JsonProperty("table")
    private Table table;
    @JsonProperty("tableName")
    private String tableName;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("witnessDetails")
    public WitnessDetails getWitnessDetails() {
        return witnessDetails;
    }

    @JsonProperty("witnessDetails")
    public void setWitnessDetails(WitnessDetails witnessDetails) {
        this.witnessDetails = witnessDetails;
    }

    public FormData withWitnessDetails(WitnessDetails witnessDetails) {
        this.witnessDetails = witnessDetails;
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
        sb.append("witnessDetails");
        sb.append('=');
        sb.append(((this.witnessDetails == null)?"<null>":this.witnessDetails));
        sb.append(',');
        sb.append("companyName");
        sb.append('=');
        sb.append(((this.companyName == null)?"<null>":this.companyName));
        sb.append(',');
        sb.append("subscriberDetails");
        sb.append('=');
        sb.append(((this.subscriberDetails == null)?"<null>":this.subscriberDetails));
        sb.append(',');
        sb.append("formIntegrationId");
        sb.append('=');
        sb.append(((this.formIntegrationId == null)?"<null>":this.formIntegrationId));
        sb.append(',');
        sb.append("table");
        sb.append('=');
        sb.append(((this.table == null)?"<null>":this.table));
        sb.append(',');
        sb.append("tableName");
        sb.append('=');
        sb.append(((this.tableName == null)?"<null>":this.tableName));
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
        result = ((result* 31)+((this.witnessDetails == null)? 0 :this.witnessDetails.hashCode()));
        result = ((result* 31)+((this.companyName == null)? 0 :this.companyName.hashCode()));
        result = ((result* 31)+((this.subscriberDetails == null)? 0 :this.subscriberDetails.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.formIntegrationId == null)? 0 :this.formIntegrationId.hashCode()));
        result = ((result* 31)+((this.table == null)? 0 :this.table.hashCode()));
        result = ((result* 31)+((this.tableName == null)? 0 :this.tableName.hashCode()));
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
        return ((((((((this.witnessDetails == rhs.witnessDetails)||((this.witnessDetails!= null)&&this.witnessDetails.equals(rhs.witnessDetails)))&&((this.companyName == rhs.companyName)||((this.companyName!= null)&&this.companyName.equals(rhs.companyName))))&&((this.subscriberDetails == rhs.subscriberDetails)||((this.subscriberDetails!= null)&&this.subscriberDetails.equals(rhs.subscriberDetails))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.formIntegrationId == rhs.formIntegrationId)||((this.formIntegrationId!= null)&&this.formIntegrationId.equals(rhs.formIntegrationId))))&&((this.table == rhs.table)||((this.table!= null)&&this.table.equals(rhs.table))))&&((this.tableName == rhs.tableName)||((this.tableName!= null)&&this.tableName.equals(rhs.tableName))));
    }

}
