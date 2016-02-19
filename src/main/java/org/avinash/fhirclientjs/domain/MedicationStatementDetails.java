/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * Obtains Java beans from <i>MedicationStatement</i> FHIR Resource.
 * 
 * @author ashanbhag
 *
 */
public class MedicationStatementDetails implements Serializable 
{
	private static final long serialVersionUID = 1L;
	
	private String name;		// medicine name
	private String code;		// medicine code 
	private String route; 		// how delivered
	private String when; 		// When given
	private String status;		// whether completed, or still active.
	private String wasNotTaken; // Was taken or not
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MedicationStatementDetails [name=" + name + ", code=" + code
				+ ", route=" + route + ", when=" + when + ", status=" + status
				+ ", wasNotTaken=" + wasNotTaken + "]";
	}
	
	
	

}
