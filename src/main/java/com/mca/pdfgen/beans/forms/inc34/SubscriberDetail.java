
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
    "subSerialNo",
    "subSignedPlace",
    "subscriberName",
    "subPassportdinpan",
    "dscCount"
})
@Generated("jsonschema2pojo")
public class SubscriberDetail {

    @JsonProperty("subSerialNo")
    private String subSerialNo;
    @JsonProperty("subSignedPlace")
    private String subSignedPlace;
    @JsonProperty("subscriberName")
    private String subscriberName;
    @JsonProperty("subPassportdinpan")
    private String subPassportdinpan;
    @JsonProperty("dscCount")
    private String dscCount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("subSerialNo")
    public String getSubSerialNo() {
        return subSerialNo;
    }

    @JsonProperty("subSerialNo")
    public void setSubSerialNo(String subSerialNo) {
        this.subSerialNo = subSerialNo;
    }

    public SubscriberDetail withSubSerialNo(String subSerialNo) {
        this.subSerialNo = subSerialNo;
        return this;
    }

    @JsonProperty("subSignedPlace")
    public String getSubSignedPlace() {
        return subSignedPlace;
    }

    @JsonProperty("subSignedPlace")
    public void setSubSignedPlace(String subSignedPlace) {
        this.subSignedPlace = subSignedPlace;
    }

    public SubscriberDetail withSubSignedPlace(String subSignedPlace) {
        this.subSignedPlace = subSignedPlace;
        return this;
    }

    @JsonProperty("subscriberName")
    public String getSubscriberName() {
        return subscriberName;
    }

    @JsonProperty("subscriberName")
    public void setSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
    }

    public SubscriberDetail withSubscriberName(String subscriberName) {
        this.subscriberName = subscriberName;
        return this;
    }

    @JsonProperty("subPassportdinpan")
    public String getSubPassportdinpan() {
        return subPassportdinpan;
    }

    @JsonProperty("subPassportdinpan")
    public void setSubPassportdinpan(String subPassportdinpan) {
        this.subPassportdinpan = subPassportdinpan;
    }

    public SubscriberDetail withSubPassportdinpan(String subPassportdinpan) {
        this.subPassportdinpan = subPassportdinpan;
        return this;
    }

    @JsonProperty("dscCount")
    public String getDscCount() {
        return dscCount;
    }

    @JsonProperty("dscCount")
    public void setDscCount(String dscCount) {
        this.dscCount = dscCount;
    }

    public SubscriberDetail withDscCount(String dscCount) {
        this.dscCount = dscCount;
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

    public SubscriberDetail withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(SubscriberDetail.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("subSerialNo");
        sb.append('=');
        sb.append(((this.subSerialNo == null)?"<null>":this.subSerialNo));
        sb.append(',');
        sb.append("subSignedPlace");
        sb.append('=');
        sb.append(((this.subSignedPlace == null)?"<null>":this.subSignedPlace));
        sb.append(',');
        sb.append("subscriberName");
        sb.append('=');
        sb.append(((this.subscriberName == null)?"<null>":this.subscriberName));
        sb.append(',');
        sb.append("subPassportdinpan");
        sb.append('=');
        sb.append(((this.subPassportdinpan == null)?"<null>":this.subPassportdinpan));
        sb.append(',');
        sb.append("dscCount");
        sb.append('=');
        sb.append(((this.dscCount == null)?"<null>":this.dscCount));
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
        result = ((result* 31)+((this.subSerialNo == null)? 0 :this.subSerialNo.hashCode()));
        result = ((result* 31)+((this.subSignedPlace == null)? 0 :this.subSignedPlace.hashCode()));
        result = ((result* 31)+((this.subscriberName == null)? 0 :this.subscriberName.hashCode()));
        result = ((result* 31)+((this.subPassportdinpan == null)? 0 :this.subPassportdinpan.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.dscCount == null)? 0 :this.dscCount.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof SubscriberDetail) == false) {
            return false;
        }
        SubscriberDetail rhs = ((SubscriberDetail) other);
        return (((((((this.subSerialNo == rhs.subSerialNo)||((this.subSerialNo!= null)&&this.subSerialNo.equals(rhs.subSerialNo)))&&((this.subSignedPlace == rhs.subSignedPlace)||((this.subSignedPlace!= null)&&this.subSignedPlace.equals(rhs.subSignedPlace))))&&((this.subscriberName == rhs.subscriberName)||((this.subscriberName!= null)&&this.subscriberName.equals(rhs.subscriberName))))&&((this.subPassportdinpan == rhs.subPassportdinpan)||((this.subPassportdinpan!= null)&&this.subPassportdinpan.equals(rhs.subPassportdinpan))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.dscCount == rhs.dscCount)||((this.dscCount!= null)&&this.dscCount.equals(rhs.dscCount))));
    }

}
