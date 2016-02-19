/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 */
public class MedicationOrderDetails implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String status;
	private String dosageInstruction;
	
	private String displayName;
	
	
	/**
	 * Default CTOR
	 */
	public MedicationOrderDetails()
	{
		this.code = null;
		this.status = null;
		this.dosageInstruction = null;
		this.displayName = null;
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
	 * @return the dosageInstruction
	 */
	public String getDosageInstruction() {
		return dosageInstruction;
	}

	/**
	 * @param dosageInstruction the dosageInstruction to set
	 */
	public void setDosageInstruction(String dosageInstruction) {
		this.dosageInstruction = dosageInstruction;
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


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MedicationOrderDetails [code=" + code + ", status=" + status
				+ ", dosageInstruction=" + dosageInstruction + ", displayName="
				+ displayName +  "]";
	}


	
}
