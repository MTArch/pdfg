
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
    "checkboxifaltered",
    "checkboxifapplicable",
    "articlenum",
    "articledescription"
})
@Generated("jsonschema2pojo")
public class Description {

    @JsonProperty("checkboxifaltered")
    private String checkboxifaltered;
    @JsonProperty("checkboxifapplicable")
    private String checkboxifapplicable;
    @JsonProperty("articlenum")
    private String articlenum;
    @JsonProperty("articledescription")
    private String articledescription;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("checkboxifaltered")
    public String getCheckboxifaltered() {
        return checkboxifaltered;
    }

    @JsonProperty("checkboxifaltered")
    public void setCheckboxifaltered(String checkboxifaltered) {
        this.checkboxifaltered = checkboxifaltered;
    }

    public Description withCheckboxifaltered(String checkboxifaltered) {
        this.checkboxifaltered = checkboxifaltered;
        return this;
    }

    @JsonProperty("checkboxifapplicable")
    public String getCheckboxifapplicable() {
        return checkboxifapplicable;
    }

    @JsonProperty("checkboxifapplicable")
    public void setCheckboxifapplicable(String checkboxifapplicable) {
        this.checkboxifapplicable = checkboxifapplicable;
    }

    public Description withCheckboxifapplicable(String checkboxifapplicable) {
        this.checkboxifapplicable = checkboxifapplicable;
        return this;
    }

    @JsonProperty("articlenum")
    public String getArticlenum() {
        return articlenum;
    }

    @JsonProperty("articlenum")
    public void setArticlenum(String articlenum) {
        this.articlenum = articlenum;
    }

    public Description withArticlenum(String articlenum) {
        this.articlenum = articlenum;
        return this;
    }

    @JsonProperty("articledescription")
    public String getArticledescription() {
        return articledescription;
    }

    @JsonProperty("articledescription")
    public void setArticledescription(String articledescription) {
        this.articledescription = articledescription;
    }

    public Description withArticledescription(String articledescription) {
        this.articledescription = articledescription;
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

    public Description withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Description.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("checkboxifaltered");
        sb.append('=');
        sb.append(((this.checkboxifaltered == null)?"<null>":this.checkboxifaltered));
        sb.append(',');
        sb.append("checkboxifapplicable");
        sb.append('=');
        sb.append(((this.checkboxifapplicable == null)?"<null>":this.checkboxifapplicable));
        sb.append(',');
        sb.append("articlenum");
        sb.append('=');
        sb.append(((this.articlenum == null)?"<null>":this.articlenum));
        sb.append(',');
        sb.append("articledescription");
        sb.append('=');
        sb.append(((this.articledescription == null)?"<null>":this.articledescription));
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
        result = ((result* 31)+((this.checkboxifaltered == null)? 0 :this.checkboxifaltered.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.checkboxifapplicable == null)? 0 :this.checkboxifapplicable.hashCode()));
        result = ((result* 31)+((this.articledescription == null)? 0 :this.articledescription.hashCode()));
        result = ((result* 31)+((this.articlenum == null)? 0 :this.articlenum.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Description) == false) {
            return false;
        }
        Description rhs = ((Description) other);
        return ((((((this.checkboxifaltered == rhs.checkboxifaltered)||((this.checkboxifaltered!= null)&&this.checkboxifaltered.equals(rhs.checkboxifaltered)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.checkboxifapplicable == rhs.checkboxifapplicable)||((this.checkboxifapplicable!= null)&&this.checkboxifapplicable.equals(rhs.checkboxifapplicable))))&&((this.articledescription == rhs.articledescription)||((this.articledescription!= null)&&this.articledescription.equals(rhs.articledescription))))&&((this.articlenum == rhs.articlenum)||((this.articlenum!= null)&&this.articlenum.equals(rhs.articlenum))));
    }

}
