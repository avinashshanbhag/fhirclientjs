/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 */
public class MedicationDispenseDetails implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String displayName;
	private String status;
	private String whenHandedOver;
	
	/**
	 * Default CTOR
	 */
	public MedicationDispenseDetails()
	{
		this.code = null;
		this.displayName = null;
		this.status = null;
		this.whenHandedOver = null;
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
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
		return "MedicationDispenseDetails [code=" + code + ", displayName="
				+ displayName + ", status=" + status + ", whenHandedOver="
				+ whenHandedOver + "]";
	}
	

}
