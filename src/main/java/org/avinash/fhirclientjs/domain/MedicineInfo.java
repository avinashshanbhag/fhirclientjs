/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 */
public class MedicineInfo implements Serializable 
{

	private static final long serialVersionUID = 1L;
	
	
	private String name;	// medicine name
	private String code; 	// Code
	private String dosage; 	// value + units
	private String when; 	// When given
	private String device; 	// device used in administering the medicine
	
	private String route;	// How the medication was delivered.
	
	private String medicationStatus; // Whether patient is still taking the medication
	private String orderStatus;		// Whether the order is fulfilled or not or something else.
	private String wasNotTaken; 	// true: if the patient did not take the medication
	
	private String dispenseStatus;
	private String whenHandedOver;
	
	
	/**
	 * Default CTOR
	 */
	public MedicineInfo()
	{
		this.name = null;
		this.code = null;
		this.dosage = null;
		this.when = null;
		this.route = null;
		this.medicationStatus = null;
		this.orderStatus = null;
		this.device = null;
		this.wasNotTaken = null;
		this.dispenseStatus = null;
		this.whenHandedOver = null;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDosage() {
		return dosage;
	}
	public void setDosage(String dosage) {
		this.dosage = dosage;
	}
	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}

	/**
	 * @return the medicationStatus
	 */
	public String getMedicationStatus() {
		return medicationStatus;
	}

	/**
	 * @param medicationStatus the medicationStatus to set
	 */
	public void setMedicationStatus(String medicationStatus) {
		this.medicationStatus = medicationStatus;
	}

	/**
	 * @return the orderStatus
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the wasNotTaken
	 */
	public String getWasNotTaken() {
		return wasNotTaken;
	}

	/**
	 * @param wasNotTaken the wasNotTaken to set
	 */
	public void setWasNotTaken(String wasNotTaken) {
		this.wasNotTaken = wasNotTaken;
	}

	/**
	 * @return the route
	 */
	public String getRoute() {
		return route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(String route) {
		this.route = route;
	}

	/**
	 * @return the dispenseStatus
	 */
	public String getDispenseStatus() {
		return dispenseStatus;
	}

	/**
	 * @param dispenseStatus the dispenseStatus to set
	 */
	public void setDispenseStatus(String dispenseStatus) {
		this.dispenseStatus = dispenseStatus;
	}

	/**
	 * @return the whenHandedOver
	 */
	public String getWhenHandedOver() {
		return whenHandedOver;
	}

	/**
	 * @param whenHandedOver the whenHandedOver to set
	 */
	public void setWhenHandedOver(String whenHandedOver) {
		this.whenHandedOver = whenHandedOver;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MedicineInfo [name=" + name + ", code=" + code + ", dosage="
				+ dosage + ", when=" + when + ", device=" + device + ", route="
				+ route + ", medicationStatus=" + medicationStatus
				+ ", orderStatus=" + orderStatus + ", wasNotTaken="
				+ wasNotTaken + ", dispenseStatus=" + dispenseStatus
				+ ", whenHandedOver=" + whenHandedOver + "]";
	}


}
