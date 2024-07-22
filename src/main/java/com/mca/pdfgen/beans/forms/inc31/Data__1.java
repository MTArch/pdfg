
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
	{ "datepicker1641552607770", "referenceNumber", "language-translation-radio", "table_name",
			"Path_for_PDFGeneration" })
@Generated("jsonschema2pojo")
public class Data__1
{

	@JsonProperty("datepicker1641552607770")
	private String datepicker1641552607770;
	@JsonProperty("referenceNumber")
	private String referenceNumber;
	@JsonProperty("language-translation-radio")
	private String languageTranslationRadio;
	@JsonProperty("table_name")
	private String tableName;
	@JsonProperty("Path_for_PDFGeneration")
	private String pathForPDFGeneration;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("datepicker1641552607770")
	public String getDatepicker1641552607770()
	{
		return datepicker1641552607770;
	}

	@JsonProperty("datepicker1641552607770")
	public void setDatepicker1641552607770(String datepicker1641552607770)
	{
		this.datepicker1641552607770 = datepicker1641552607770;
	}

	public Data__1 withDatepicker1641552607770(String datepicker1641552607770)
	{
		this.datepicker1641552607770 = datepicker1641552607770;
		return this;
	}

	@JsonProperty("referenceNumber")
	public String getReferenceNumber()
	{
		return referenceNumber;
	}

	@JsonProperty("referenceNumber")
	public void setReferenceNumber(String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
	}

	public Data__1 withReferenceNumber(String referenceNumber)
	{
		this.referenceNumber = referenceNumber;
		return this;
	}

	@JsonProperty("language-translation-radio")
	public String getLanguageTranslationRadio()
	{
		return languageTranslationRadio;
	}

	@JsonProperty("language-translation-radio")
	public void setLanguageTranslationRadio(String languageTranslationRadio)
	{
		this.languageTranslationRadio = languageTranslationRadio;
	}

	public Data__1 withLanguageTranslationRadio(String languageTranslationRadio)
	{
		this.languageTranslationRadio = languageTranslationRadio;
		return this;
	}

	@JsonProperty("table_name")
	public String getTableName()
	{
		return tableName;
	}

	@JsonProperty("table_name")
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}

	public Data__1 withTableName(String tableName)
	{
		this.tableName = tableName;
		return this;
	}

	@JsonProperty("Path_for_PDFGeneration")
	public String getPathForPDFGeneration()
	{
		return pathForPDFGeneration;
	}

	@JsonProperty("Path_for_PDFGeneration")
	public void setPathForPDFGeneration(String pathForPDFGeneration)
	{
		this.pathForPDFGeneration = pathForPDFGeneration;
	}

	public Data__1 withPathForPDFGeneration(String pathForPDFGeneration)
	{
		this.pathForPDFGeneration = pathForPDFGeneration;
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

	public Data__1 withAdditionalProperty(String name, Object value)
	{
		this.additionalProperties.put(name, value);
		return this;
	}

	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(Data__1.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this)))
				.append('[');
		sb.append("datepicker1641552607770");
		sb.append('=');
		sb.append(((this.datepicker1641552607770 == null) ? "<null>" : this.datepicker1641552607770));
		sb.append(',');
		sb.append("referenceNumber");
		sb.append('=');
		sb.append(((this.referenceNumber == null) ? "<null>" : this.referenceNumber));
		sb.append(',');
		sb.append("languageTranslationRadio");
		sb.append('=');
		sb.append(((this.languageTranslationRadio == null) ? "<null>" : this.languageTranslationRadio));
		sb.append(',');
		sb.append("tableName");
		sb.append('=');
		sb.append(((this.tableName == null) ? "<null>" : this.tableName));
		sb.append(',');
		sb.append("pathForPDFGeneration");
		sb.append('=');
		sb.append(((this.pathForPDFGeneration == null) ? "<null>" : this.pathForPDFGeneration));
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
		result = ((result * 31)
				+ ((this.datepicker1641552607770 == null) ? 0 : this.datepicker1641552607770.hashCode()));
		result = ((result * 31) + ((this.referenceNumber == null) ? 0 : this.referenceNumber.hashCode()));
		result = ((result * 31) + ((this.additionalProperties == null) ? 0 : this.additionalProperties.hashCode()));
		result = ((result * 31) + ((this.pathForPDFGeneration == null) ? 0 : this.pathForPDFGeneration.hashCode()));
		result = ((result * 31)
				+ ((this.languageTranslationRadio == null) ? 0 : this.languageTranslationRadio.hashCode()));
		result = ((result * 31) + ((this.tableName == null) ? 0 : this.tableName.hashCode()));
		return result;
	}

	@Override
	public boolean equals(Object other)
	{
		if (other == this)
		{
			return true;
		}
		if ((other instanceof Data__1) == false)
		{
			return false;
		}
		Data__1 rhs = ((Data__1) other);
		return (((((((this.datepicker1641552607770 == rhs.datepicker1641552607770)
				|| ((this.datepicker1641552607770 != null)
						&& this.datepicker1641552607770.equals(rhs.datepicker1641552607770)))
				&& ((this.referenceNumber == rhs.referenceNumber)
						|| ((this.referenceNumber != null) && this.referenceNumber.equals(rhs.referenceNumber))))
				&& ((this.additionalProperties == rhs.additionalProperties) || ((this.additionalProperties != null)
						&& this.additionalProperties.equals(rhs.additionalProperties))))
				&& ((this.pathForPDFGeneration == rhs.pathForPDFGeneration) || ((this.pathForPDFGeneration != null)
						&& this.pathForPDFGeneration.equals(rhs.pathForPDFGeneration))))
				&& ((this.languageTranslationRadio == rhs.languageTranslationRadio)
						|| ((this.languageTranslationRadio != null)
								&& this.languageTranslationRadio.equals(rhs.languageTranslationRadio))))
				&& ((this.tableName == rhs.tableName)
						|| ((this.tableName != null) && this.tableName.equals(rhs.tableName))));
	}

}
