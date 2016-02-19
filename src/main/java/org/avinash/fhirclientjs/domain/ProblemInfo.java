/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 */
public class ProblemInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String type;
	private String code;
	private String description;
	private String dateRecorded;
	private String ageAtOnset;
	private String severity;
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}




	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}




	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}




	/**
	 * @return the dateRecorded
	 */
	public String getDateRecorded() {
		return dateRecorded;
	}




	/**
	 * @param docDate the dateRecorded to set
	 */
	public void setDateRecorded(String dateRecorded) {
		this.dateRecorded = dateRecorded;
	}




	/**
	 * @return the ageAtOnset
	 */
	public String getAgeAtOnset() {
		return ageAtOnset;
	}




	/**
	 * @param ageAtOnset the ageAtOnset to set
	 */
	public void setAgeAtOnset(String ageAtOnset) {
		this.ageAtOnset = ageAtOnset;
	}




	/**
	 * @return the severity
	 */
	public String getSeverity() {
		return severity;
	}




	/**
	 * @param severity the severity to set
	 */
	public void setSeverity(String severity) {
		this.severity = severity;
	}




	/**
	 * 
	 */
	public ProblemInfo() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ProblemInfo [type=" + type + ", code=" + code
				+ ", description=" + description + ", dateRecorded=" + dateRecorded
				+ ", ageAtOnset=" + ageAtOnset + ", severity=" + severity + "]";
	}




	
}
