
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
	{ "tableDetails" })
@Generated("jsonschema2pojo")
public class Table
{

	@JsonProperty("tableDetails")
	private TableDetails tableDetails;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("tableDetails")
	public TableDetails getTableDetails()
	{
		return tableDetails;
	}

	@JsonProperty("tableDetails")
	public void setTableDetails(TableDetails tableDetails)
	{
		this.tableDetails = tableDetails;
	}

	public Table withTableDetails(TableDetails tableDetails)
	{
		this.tableDetails = tableDetails;
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

	public Table withAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Table.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
				.append('[');
		sb.append("tableDetails");
		sb.append('=');
		sb.append(((this.tableDetails == null) ? "<null>" : this.tableDetails));
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
		result = ((result * 31) + ((this.tableDetails == null) ? 0 : this.tableDetails.hashCode()));
		result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == this)
		{
			return true;
		}
		if ((other instanceof Table) == false)
		{
			return false;
		}
		Table rhs = ((Table) other);
		return (((this.tableDetails == rhs.tableDetails)
				|| ((this.tableDetails != null) && this.tableDetails.equals(rhs.tableDetails)))
				&& ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null)
						&& this.additionalProperties.equals(rhs.additionalProperties))));
	}

}
