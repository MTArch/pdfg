
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
	{ "afBoundData", "afUnboundData", "afSubmissionInfo" })
@Generated("jsonschema2pojo")
public class AfData
{

	@JsonProperty("afBoundData")
	private AfBoundData afBoundData;
	@JsonProperty("afUnboundData")
	private AfUnboundData afUnboundData;
	@JsonProperty("afSubmissionInfo")
	private AfSubmissionInfo afSubmissionInfo;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("afBoundData")
	public AfBoundData getAfBoundData()
	{
		return afBoundData;
	}

	@JsonProperty("afBoundData")
	public void setAfBoundData(AfBoundData afBoundData)
	{
		this.afBoundData = afBoundData;
	}

	public AfData withAfBoundData(AfBoundData afBoundData)
	{
		this.afBoundData = afBoundData;
		return this;
	}

	@JsonProperty("afUnboundData")
	public AfUnboundData getAfUnboundData()
	{
		return afUnboundData;
	}

	@JsonProperty("afUnboundData")
	public void setAfUnboundData(AfUnboundData afUnboundData)
	{
		this.afUnboundData = afUnboundData;
	}

	public AfData withAfUnboundData(AfUnboundData afUnboundData)
	{
		this.afUnboundData = afUnboundData;
		return this;
	}

	@JsonProperty("afSubmissionInfo")
	public AfSubmissionInfo getAfSubmissionInfo()
	{
		return afSubmissionInfo;
	}

	@JsonProperty("afSubmissionInfo")
	public void setAfSubmissionInfo(AfSubmissionInfo afSubmissionInfo)
	{
		this.afSubmissionInfo = afSubmissionInfo;
	}

	public AfData withAfSubmissionInfo(AfSubmissionInfo afSubmissionInfo)
	{
		this.afSubmissionInfo = afSubmissionInfo;
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

	public AfData withAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(AfData.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
				.append('[');
		sb.append("afBoundData");
		sb.append('=');
		sb.append(((this.afBoundData == null) ? "<null>" : this.afBoundData));
		sb.append(',');
		sb.append("afUnboundData");
		sb.append('=');
		sb.append(((this.afUnboundData == null) ? "<null>" : this.afUnboundData));
		sb.append(',');
		sb.append("afSubmissionInfo");
		sb.append('=');
		sb.append(((this.afSubmissionInfo == null) ? "<null>" : this.afSubmissionInfo));
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
		result = ((result * 31) + ((this.afBoundData == null) ? 0 : this.afBoundData.hashCode()));
		result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
		result = ((result * 31) + ((this.afUnboundData == null) ? 0 : this.afUnboundData.hashCode()));
		result = ((result * 31) + ((this.afSubmissionInfo == null) ? 0 : this.afSubmissionInfo.hashCode()));
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == this)
		{
			return true;
		}
		if ((other instanceof AfData) == false)
		{
			return false;
		}
		AfData rhs = ((AfData) other);
		return (((((this.afBoundData == rhs.afBoundData)
				|| ((this.afBoundData != null) && this.afBoundData.equals(rhs.afBoundData)))
				&& ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null)
						&& this.additionalProperties.equals(rhs.additionalProperties))))
				&& ((this.afUnboundData == rhs.afUnboundData)
						|| ((this.afUnboundData != null) && this.afUnboundData.equals(rhs.afUnboundData))))
				&& ((this.afSubmissionInfo == rhs.afSubmissionInfo)
						|| ((this.afSubmissionInfo != null) && this.afSubmissionInfo.equals(rhs.afSubmissionInfo))));
	}

}
