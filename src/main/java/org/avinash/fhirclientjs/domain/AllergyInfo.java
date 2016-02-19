/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 */
public class AllergyInfo implements Serializable 
{
	private static final long serialVersionUID = 1L;

	private String substance;
	private String category;
	private String status;
	private String reaction;
	private String severity;
	private String duration;
	private String when;
	/**
	 * @return the substance
	 */
	public String getSubstance() {
		return substance;
	}
	/**
	 * @param substance the substance to set
	 */
	public void setSubstance(String substance) {
		this.substance = substance;
	}
	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	 * @return the reaction
	 */
	public String getReaction() {
		return reaction;
	}
	/**
	 * @param reaction the reaction to set
	 */
	public void setReaction(String reaction) {
		this.reaction = reaction;
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
	 * @return the duration
	 */
	public String getDuration() {
		return duration;
	}
	/**
	 * @param duration the duration to set
	 */
	public void setDuration(String duration) {
		this.duration = duration;
	}
	/**
	 * @return the when
	 */
	public String getWhen() {
		return when;
	}
	/**
	 * @param when the when to set
	 */
	public void setWhen(String when) {
		this.when = when;
	}

	public String toString()
	{
		StringBuffer buf = null;
		
		buf = new StringBuffer();

		buf.append("Substance:");
		buf.append(this.substance);
		buf.append("\n");
				
		buf.append("Category:");
		buf.append(this.category);
		buf.append("\n");
		
		buf.append("Status:");
		buf.append(this.status);
		buf.append("\n");

		buf.append("Reaction:");
		buf.append(this.reaction);
		buf.append("\n");
	
		buf.append("Severity:");
		buf.append(this.severity);
		buf.append("\n");

		buf.append("Duration:");
		buf.append(this.duration);
		buf.append("\n");
				
		buf.append("When:");
		buf.append(this.when);
		
		return (buf.toString());
	}
	
	
	
}
