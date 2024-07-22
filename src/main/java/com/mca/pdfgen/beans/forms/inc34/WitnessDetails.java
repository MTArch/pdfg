
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
    "witnessPlace",
    "dscCount1",
    "witnessName",
    "witnessSerialNo",
    "witnessPassportDinPan",
    "witnessAddrDescOccpn"
})
@Generated("jsonschema2pojo")
public class WitnessDetails {

    @JsonProperty("witnessPlace")
    private String witnessPlace;
    @JsonProperty("dscCount1")
    private String dscCount1;
    @JsonProperty("witnessName")
    private String witnessName;
    @JsonProperty("witnessSerialNo")
    private String witnessSerialNo;
    @JsonProperty("witnessPassportDinPan")
    private String witnessPassportDinPan;
    @JsonProperty("witnessAddrDescOccpn")
    private String witnessAddrDescOccpn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("witnessPlace")
    public String getWitnessPlace() {
        return witnessPlace;
    }

    @JsonProperty("witnessPlace")
    public void setWitnessPlace(String witnessPlace) {
        this.witnessPlace = witnessPlace;
    }

    public WitnessDetails withWitnessPlace(String witnessPlace) {
        this.witnessPlace = witnessPlace;
        return this;
    }

    @JsonProperty("dscCount1")
    public String getDscCount1() {
        return dscCount1;
    }

    @JsonProperty("dscCount1")
    public void setDscCount1(String dscCount1) {
        this.dscCount1 = dscCount1;
    }

    public WitnessDetails withDscCount1(String dscCount1) {
        this.dscCount1 = dscCount1;
        return this;
    }

    @JsonProperty("witnessName")
    public String getWitnessName() {
        return witnessName;
    }

    @JsonProperty("witnessName")
    public void setWitnessName(String witnessName) {
        this.witnessName = witnessName;
    }

    public WitnessDetails withWitnessName(String witnessName) {
        this.witnessName = witnessName;
        return this;
    }

    @JsonProperty("witnessSerialNo")
    public String getWitnessSerialNo() {
        return witnessSerialNo;
    }

    @JsonProperty("witnessSerialNo")
    public void setWitnessSerialNo(String witnessSerialNo) {
        this.witnessSerialNo = witnessSerialNo;
    }

    public WitnessDetails withWitnessSerialNo(String witnessSerialNo) {
        this.witnessSerialNo = witnessSerialNo;
        return this;
    }

    @JsonProperty("witnessPassportDinPan")
    public String getWitnessPassportDinPan() {
        return witnessPassportDinPan;
    }

    @JsonProperty("witnessPassportDinPan")
    public void setWitnessPassportDinPan(String witnessPassportDinPan) {
        this.witnessPassportDinPan = witnessPassportDinPan;
    }

    public WitnessDetails withWitnessPassportDinPan(String witnessPassportDinPan) {
        this.witnessPassportDinPan = witnessPassportDinPan;
        return this;
    }

    @JsonProperty("witnessAddrDescOccpn")
    public String getWitnessAddrDescOccpn() {
        return witnessAddrDescOccpn;
    }

    @JsonProperty("witnessAddrDescOccpn")
    public void setWitnessAddrDescOccpn(String witnessAddrDescOccpn) {
        this.witnessAddrDescOccpn = witnessAddrDescOccpn;
    }

    public WitnessDetails withWitnessAddrDescOccpn(String witnessAddrDescOccpn) {
        this.witnessAddrDescOccpn = witnessAddrDescOccpn;
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

    public WitnessDetails withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(WitnessDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("witnessPlace");
        sb.append('=');
        sb.append(((this.witnessPlace == null)?"<null>":this.witnessPlace));
        sb.append(',');
        sb.append("dscCount1");
        sb.append('=');
        sb.append(((this.dscCount1 == null)?"<null>":this.dscCount1));
        sb.append(',');
        sb.append("witnessName");
        sb.append('=');
        sb.append(((this.witnessName == null)?"<null>":this.witnessName));
        sb.append(',');
        sb.append("witnessSerialNo");
        sb.append('=');
        sb.append(((this.witnessSerialNo == null)?"<null>":this.witnessSerialNo));
        sb.append(',');
        sb.append("witnessPassportDinPan");
        sb.append('=');
        sb.append(((this.witnessPassportDinPan == null)?"<null>":this.witnessPassportDinPan));
        sb.append(',');
        sb.append("witnessAddrDescOccpn");
        sb.append('=');
        sb.append(((this.witnessAddrDescOccpn == null)?"<null>":this.witnessAddrDescOccpn));
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
        result = ((result* 31)+((this.witnessPlace == null)? 0 :this.witnessPlace.hashCode()));
        result = ((result* 31)+((this.dscCount1 == null)? 0 :this.dscCount1 .hashCode()));
        result = ((result* 31)+((this.witnessName == null)? 0 :this.witnessName.hashCode()));
        result = ((result* 31)+((this.witnessSerialNo == null)? 0 :this.witnessSerialNo.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.witnessPassportDinPan == null)? 0 :this.witnessPassportDinPan.hashCode()));
        result = ((result* 31)+((this.witnessAddrDescOccpn == null)? 0 :this.witnessAddrDescOccpn.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof WitnessDetails) == false) {
            return false;
        }
        WitnessDetails rhs = ((WitnessDetails) other);
        return ((((((((this.witnessPlace == rhs.witnessPlace)||((this.witnessPlace!= null)&&this.witnessPlace.equals(rhs.witnessPlace)))&&((this.dscCount1 == rhs.dscCount1)||((this.dscCount1 != null)&&this.dscCount1 .equals(rhs.dscCount1))))&&((this.witnessName == rhs.witnessName)||((this.witnessName!= null)&&this.witnessName.equals(rhs.witnessName))))&&((this.witnessSerialNo == rhs.witnessSerialNo)||((this.witnessSerialNo!= null)&&this.witnessSerialNo.equals(rhs.witnessSerialNo))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.witnessPassportDinPan == rhs.witnessPassportDinPan)||((this.witnessPassportDinPan!= null)&&this.witnessPassportDinPan.equals(rhs.witnessPassportDinPan))))&&((this.witnessAddrDescOccpn == rhs.witnessAddrDescOccpn)||((this.witnessAddrDescOccpn!= null)&&this.witnessAddrDescOccpn.equals(rhs.witnessAddrDescOccpn))));
    }

}
