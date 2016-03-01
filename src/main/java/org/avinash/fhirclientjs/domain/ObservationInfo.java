/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * This class provides accesss to data from <i>Observation</i> FHIR resource
 * @author ashanbhag
 *
 */
public class ObservationInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// Observation type: CodeableConcept, Quantity, etc - used to determine which value to pick
	public  enum ObservationType 
	{
		QUANTITY,
		CODEABLECONCEPT
	}
	
	private ObservationType type; 
	
	// Observation Category
	private String categoryCodeValue;
	private String categoryCodeSystem;
	private String categoryDisplayValue;

	// Observation Key 
	private String obsCodeValue;
	private String obsCodeSystem;
	private String obsCodeDisplayValue;
	
	// Observation Val - CodeableConcept
	private String valCodeSystem;
	private String valCodeValue;
	private String valCodeDisplayValue;
	
	// Observation val - Quantity
	private double value; 
	private String unit;
	private String unitCodeSystem;
	
	

	/**
	 * Default CTOR
	 */
	public ObservationInfo() 
	{
		
		this.type = null;
		
		this.categoryCodeSystem = null;
		this.categoryCodeValue = null;
		this.categoryDisplayValue = null;
		
		this.obsCodeDisplayValue = null;
		this.obsCodeSystem = null;
		this.obsCodeValue = null;
		
		this.valCodeDisplayValue = null;
		this.valCodeSystem = null;
		this.valCodeValue = null;
		
		this.value = 0.0;
		this.unit = null;
		this.unitCodeSystem = null;
		
	}

	
	/**
	 * @return the categoryCodeValue
	 */
	public String getCategoryCodeValue() {
		return categoryCodeValue;
	}


	/**
	 * @param categoryCodeValue the categoryCodeValue to set
	 */
	public void setCategoryCodeValue(String categoryCodeValue) {
		this.categoryCodeValue = categoryCodeValue;
	}


	/**
	 * @return the categoryCodeSystem
	 */
	public String getCategoryCodeSystem() {
		return categoryCodeSystem;
	}


	/**
	 * @param categoryCodeSystem the categoryCodeSystem to set
	 */
	public void setCategoryCodeSystem(String categoryCodeSystem) {
		this.categoryCodeSystem = categoryCodeSystem;
	}


	/**
	 * @return the categoryDisplayValue
	 */
	public String getCategoryDisplayValue() {
		return categoryDisplayValue;
	}


	/**
	 * @param categoryDisplayValue the categoryDisplayValue to set
	 */
	public void setCategoryDisplayValue(String categoryDisplayValue) {
		this.categoryDisplayValue = categoryDisplayValue;
	}


	/**
	 * @return the obsCodeValue
	 */
	public String getObsCodeValue() {
		return obsCodeValue;
	}


	/**
	 * @param obsCodeValue the obsCodeValue to set
	 */
	public void setObsCodeValue(String obsCodeValue) {
		this.obsCodeValue = obsCodeValue;
	}


	/**
	 * @return the obsCodeSystem
	 */
	public String getObsCodeSystem() {
		return obsCodeSystem;
	}


	/**
	 * @param obsCodeSystem the obsCodeSystem to set
	 */
	public void setObsCodeSystem(String obsCodeSystem) {
		this.obsCodeSystem = obsCodeSystem;
	}


	/**
	 * @return the obsCodeDisplayValue
	 */
	public String getObsCodeDisplayValue() {
		return obsCodeDisplayValue;
	}


	/**
	 * @param obsCodeDisplayValue the obsCodeDisplayValue to set
	 */
	public void setObsCodeDisplayValue(String obsCodeDisplayValue) {
		this.obsCodeDisplayValue = obsCodeDisplayValue;
	}


	/**
	 * @return the valCodeSystem
	 */
	public String getValCodeSystem() {
		return valCodeSystem;
	}


	/**
	 * @param valCodeSystem the valCodeSystem to set
	 */
	public void setValCodeSystem(String valCodeSystem) {
		this.valCodeSystem = valCodeSystem;
	}


	/**
	 * @return the valCodeValue
	 */
	public String getValCodeValue() {
		return valCodeValue;
	}


	/**
	 * @param valCodeValue the valCodeValue to set
	 */
	public void setValCodeValue(String valCodeValue) {
		this.valCodeValue = valCodeValue;
	}


	/**
	 * @return the valCodeDisplayValue
	 */
	public String getValCodeDisplayValue() {
		return valCodeDisplayValue;
	}


	/**
	 * @param valCodeDisplayValue the valCodeDisplayValue to set
	 */
	public void setValCodeDisplayValue(String valCodeDisplayValue) {
		this.valCodeDisplayValue = valCodeDisplayValue;
	}


	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}


	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}


	/**
	 * @return the unit
	 */
	public String getUnit() {
		return unit;
	}


	/**
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}


	/**
	 * @return the unitCodeSystem
	 */
	public String getUnitCodeSystem() {
		return unitCodeSystem;
	}


	/**
	 * @param unitCodeSystem the unitCodeSystem to set
	 */
	public void setUnitCodeSystem(String unitCodeSystem) {
		this.unitCodeSystem = unitCodeSystem;
	}


	/**
	 * @return the type
	 */
	public ObservationType getType() {
		return type;
	}


	/**
	 * @param type the type to set
	 */
	public void setType(ObservationType type) {
		this.type = type;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ObservationInfo [type=" + type + ", categoryCodeValue="
				+ categoryCodeValue + ", categoryCodeSystem="
				+ categoryCodeSystem + ", categoryDisplayValue="
				+ categoryDisplayValue + ", obsCodeValue=" + obsCodeValue
				+ ", obsCodeSystem=" + obsCodeSystem + ", obsCodeDisplayValue="
				+ obsCodeDisplayValue + ", valCodeSystem=" + valCodeSystem
				+ ", valCodeValue=" + valCodeValue + ", valCodeDisplayValue="
				+ valCodeDisplayValue + ", value=" + value + ", unit=" + unit
				+ ", unitCodeSystem=" + unitCodeSystem + "]";
	}




}
