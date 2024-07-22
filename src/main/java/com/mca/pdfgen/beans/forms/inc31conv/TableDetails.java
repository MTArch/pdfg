
package com.mca.pdfgen.beans.forms.inc31conv;

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
    "shareCapitalDetails",
    "managementAndBoard",
    "meetings"
})
@Generated("jsonschema2pojo")
public class TableDetails {

    @JsonProperty("shareCapitalDetails")
    private List<ShareCapitalDetail> shareCapitalDetails = new ArrayList<ShareCapitalDetail>();
    @JsonProperty("managementAndBoard")
    private List<ManagementAndBoard> managementAndBoard = new ArrayList<ManagementAndBoard>();
    @JsonProperty("meetings")
    private List<Meeting> meetings = new ArrayList<Meeting>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("shareCapitalDetails")
    public List<ShareCapitalDetail> getShareCapitalDetails() {
        return shareCapitalDetails;
    }

    @JsonProperty("shareCapitalDetails")
    public void setShareCapitalDetails(List<ShareCapitalDetail> shareCapitalDetails) {
        this.shareCapitalDetails = shareCapitalDetails;
    }

    public TableDetails withShareCapitalDetails(List<ShareCapitalDetail> shareCapitalDetails) {
        this.shareCapitalDetails = shareCapitalDetails;
        return this;
    }

    @JsonProperty("managementAndBoard")
    public List<ManagementAndBoard> getManagementAndBoard() {
        return managementAndBoard;
    }

    @JsonProperty("managementAndBoard")
    public void setManagementAndBoard(List<ManagementAndBoard> managementAndBoard) {
        this.managementAndBoard = managementAndBoard;
    }

    public TableDetails withManagementAndBoard(List<ManagementAndBoard> managementAndBoard) {
        this.managementAndBoard = managementAndBoard;
        return this;
    }

    @JsonProperty("meetings")
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @JsonProperty("meetings")
    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    public TableDetails withMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
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

    public TableDetails withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TableDetails.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("shareCapitalDetails");
        sb.append('=');
        sb.append(((this.shareCapitalDetails == null)?"<null>":this.shareCapitalDetails));
        sb.append(',');
        sb.append("managementAndBoard");
        sb.append('=');
        sb.append(((this.managementAndBoard == null)?"<null>":this.managementAndBoard));
        sb.append(',');
        sb.append("meetings");
        sb.append('=');
        sb.append(((this.meetings == null)?"<null>":this.meetings));
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
        result = ((result* 31)+((this.managementAndBoard == null)? 0 :this.managementAndBoard.hashCode()));
        result = ((result* 31)+((this.meetings == null)? 0 :this.meetings.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        result = ((result* 31)+((this.shareCapitalDetails == null)? 0 :this.shareCapitalDetails.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TableDetails) == false) {
            return false;
        }
        TableDetails rhs = ((TableDetails) other);
        return (((((this.managementAndBoard == rhs.managementAndBoard)||((this.managementAndBoard!= null)&&this.managementAndBoard.equals(rhs.managementAndBoard)))&&((this.meetings == rhs.meetings)||((this.meetings!= null)&&this.meetings.equals(rhs.meetings))))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))))&&((this.shareCapitalDetails == rhs.shareCapitalDetails)||((this.shareCapitalDetails!= null)&&this.shareCapitalDetails.equals(rhs.shareCapitalDetails))));
    }

}
