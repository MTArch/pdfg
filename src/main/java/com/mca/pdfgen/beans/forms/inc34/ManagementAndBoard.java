
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
    "articletype",
    "description"
})
@Generated("jsonschema2pojo")
public class ManagementAndBoard {

    @JsonProperty("articletype")
    private String articletype;
    @JsonProperty("description")
    private List<Description__1> description = new ArrayList<Description__1>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("articletype")
    public String getArticletype() {
        return articletype;
    }

    @JsonProperty("articletype")
    public void setArticletype(String articletype) {
        this.articletype = articletype;
    }

    public ManagementAndBoard withArticletype(String articletype) {
        this.articletype = articletype;
        return this;
    }

    @JsonProperty("description")
    public List<Description__1> getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(List<Description__1> description) {
        this.description = description;
    }

    public ManagementAndBoard withDescription(List<Description__1> description) {
        this.description = description;
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

    public ManagementAndBoard withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(ManagementAndBoard.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("articletype");
        sb.append('=');
        sb.append(((this.articletype == null)?"<null>":this.articletype));
        sb.append(',');
        sb.append("description");
        sb.append('=');
        sb.append(((this.description == null)?"<null>":this.description));
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
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        result = ((result* 31)+((this.articletype == null)? 0 :this.articletype.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof ManagementAndBoard) == false) {
            return false;
        }
        ManagementAndBoard rhs = ((ManagementAndBoard) other);
        return ((((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description)))&&((this.articletype == rhs.articletype)||((this.articletype!= null)&&this.articletype.equals(rhs.articletype))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}
