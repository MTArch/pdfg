
package com.mca.pdfgen.beans.forms.inc31;

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
@JsonPropertyOrder(
	{ "signers", "afSubmissionTime", "afPath", "lastFocusItem", "computedMetaInfo", "stateOverrides" })
@Generated("jsonschema2pojo")
public class AfSubmissionInfo
{

	@JsonProperty("signers")
	private Signers signers;
	@JsonProperty("afSubmissionTime")
	private String afSubmissionTime;
	@JsonProperty("afPath")
	private String afPath;
	@JsonProperty("lastFocusItem")
	private String lastFocusItem;
	@JsonProperty("computedMetaInfo")
	private ComputedMetaInfo computedMetaInfo;
	@JsonProperty("stateOverrides")
	private StateOverrides stateOverrides;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("signers")
	public Signers getSigners()
	{
		return signers;
	}

	@JsonProperty("signers")
	public void setSigners(Signers signers)
	{
		this.signers = signers;
	}

	public AfSubmissionInfo withSigners(Signers signers)
	{
		this.signers = signers;
		return this;
	}

	@JsonProperty("afSubmissionTime")
	public String getAfSubmissionTime()
	{
		return afSubmissionTime;
	}

	@JsonProperty("afSubmissionTime")
	public void setAfSubmissionTime(String afSubmissionTime)
	{
		this.afSubmissionTime = afSubmissionTime;
	}

	public AfSubmissionInfo withAfSubmissionTime(String afSubmissionTime)
	{
		this.afSubmissionTime = afSubmissionTime;
		return this;
	}

	@JsonProperty("afPath")
	public String getAfPath()
	{
		return afPath;
	}

	@JsonProperty("afPath")
	public void setAfPath(String afPath)
	{
		this.afPath = afPath;
	}

	public AfSubmissionInfo withAfPath(String afPath)
	{
		this.afPath = afPath;
		return this;
	}

	@JsonProperty("lastFocusItem")
	public String getLastFocusItem()
	{
		return lastFocusItem;
	}

	@JsonProperty("lastFocusItem")
	public void setLastFocusItem(String lastFocusItem)
	{
		this.lastFocusItem = lastFocusItem;
	}

	public AfSubmissionInfo withLastFocusItem(String lastFocusItem)
	{
		this.lastFocusItem = lastFocusItem;
		return this;
	}

	@JsonProperty("computedMetaInfo")
	public ComputedMetaInfo getComputedMetaInfo()
	{
		return computedMetaInfo;
	}

	@JsonProperty("computedMetaInfo")
	public void setComputedMetaInfo(ComputedMetaInfo computedMetaInfo)
	{
		this.computedMetaInfo = computedMetaInfo;
	}

	public AfSubmissionInfo withComputedMetaInfo(ComputedMetaInfo computedMetaInfo)
	{
		this.computedMetaInfo = computedMetaInfo;
		return this;
	}

	@JsonProperty("stateOverrides")
	public StateOverrides getStateOverrides()
	{
		return stateOverrides;
	}

	@JsonProperty("stateOverrides")
	public void setStateOverrides(StateOverrides stateOverrides)
	{
		this.stateOverrides = stateOverrides;
	}

	public AfSubmissionInfo withStateOverrides(StateOverrides stateOverrides)
	{
		this.stateOverrides = stateOverrides;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties()
	{
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
	}

	public AfSubmissionInfo withAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(AfSubmissionInfo.class.getName()).append('@')
				.append(Integer.toHexString(System.identityHashCode(this))).append('[');
		sb.append("signers");
		sb.append('=');
		sb.append(((this.signers == null) ? "<null>" : this.signers));
		sb.append(',');
		sb.append("afSubmissionTime");
		sb.append('=');
		sb.append(((this.afSubmissionTime == null) ? "<null>" : this.afSubmissionTime));
		sb.append(',');
		sb.append("afPath");
		sb.append('=');
		sb.append(((this.afPath == null) ? "<null>" : this.afPath));
		sb.append(',');
		sb.append("lastFocusItem");
		sb.append('=');
		sb.append(((this.lastFocusItem == null) ? "<null>" : this.lastFocusItem));
		sb.append(',');
		sb.append("computedMetaInfo");
		sb.append('=');
		sb.append(((this.computedMetaInfo == null) ? "<null>" : this.computedMetaInfo));
		sb.append(',');
		sb.append("stateOverrides");
		sb.append('=');
		sb.append(((this.stateOverrides == null) ? "<null>" : this.stateOverrides));
		sb.append(',');
		sb.append("additionalProperties");
		sb.append('=');
		sb.append(((this.additionalProperties == null) ? "<null>" : this.additionalProperties));
		sb.append(',');
		if (sb.charAt((sb.length() - 1)) == ',')
		{
			sb.setCharAt((sb.length() - 1), ']');
		} else
		{
			sb.append(']');
		}
		return sb.toString();
	}

	@Override
	public int hashCode()
	{
		int result = 1;
		result = ((result * 31) + ((this.signers == null) ? 0 : this.signers.hashCode()));
		result = ((result * 31) + ((this.afSubmissionTime == null) ? 0 : this.afSubmissionTime.hashCode()));
		result = ((result * 31) + ((this.afPath == null) ? 0 : this.afPath.hashCode()));
		result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
		result = ((result * 31) + ((this.lastFocusItem == null) ? 0 : this.lastFocusItem.hashCode()));
		result = ((result * 31) + ((this.computedMetaInfo == null) ? 0 : this.computedMetaInfo.hashCode()));
		result = ((result * 31) + ((this.stateOverrides == null) ? 0 : this.stateOverrides.hashCode()));
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == this)
		{
			return true;
		}
		if ((other instanceof AfSubmissionInfo) == false)
		{
			return false;
		}
		AfSubmissionInfo rhs = ((AfSubmissionInfo) other);
		return ((((((((this.signers == rhs.signers) || ((this.signers != null) && this.signers.equals(rhs.signers)))
				&& ((this.afSubmissionTime == rhs.afSubmissionTime)
						|| ((this.afSubmissionTime != null) && this.afSubmissionTime.equals(rhs.afSubmissionTime))))
				&& ((this.afPath == rhs.afPath) || ((this.afPath != null) && this.afPath.equals(rhs.afPath))))
				&& ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null)
						&& this.additionalProperties.equals(rhs.additionalProperties))))
				&& ((this.lastFocusItem == rhs.lastFocusItem)
						|| ((this.lastFocusItem != null) && this.lastFocusItem.equals(rhs.lastFocusItem))))
				&& ((this.computedMetaInfo == rhs.computedMetaInfo)
						|| ((this.computedMetaInfo != null) && this.computedMetaInfo.equals(rhs.computedMetaInfo))))
				&& ((this.stateOverrides == rhs.stateOverrides)
						|| ((this.stateOverrides != null) && this.stateOverrides.equals(rhs.stateOverrides))));
	}

}
