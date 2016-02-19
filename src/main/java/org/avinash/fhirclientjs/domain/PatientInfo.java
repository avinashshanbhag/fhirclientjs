/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 */
public class PatientInfo implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String gender;
	private String id;
	private String address;
	private String dob;
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
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the dob
	 */
	public String getDob() {
		return dob;
	}
	/**
	 * @param dob the dob to set
	 */
	public void setDob(String dob) {
		this.dob = dob;
	}
	
	public String toString()
	{
		StringBuffer buf = null;
		
		buf = new StringBuffer();

		buf.append("Id:");
		buf.append(this.id);
		buf.append("\n");
				
		buf.append("Name:");
		buf.append(this.name);
		buf.append("\n");
		
		buf.append("Address:");
		buf.append(this.address);
		buf.append("\n");

		buf.append("Date of Birth:");
		buf.append(this.dob);
		buf.append("\n");
	
		buf.append("Gender:");
		buf.append(this.gender);
		buf.append("\n");

				
		return (buf.toString());
	}
	

}
