/**
 * 
 */
package org.avinash.fhirclientjs.authorization.access;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Currently, all this class does is read the value of "fhir.server.file.name" from 
 * <i>application.properties</i> file using Spring's Autowired logic.
 * 
 *  
 * @author ashanbhag
 *
 */
@Component
public class FhirServerPropertyInfo {
	
	//@Value("${fhir.server.file.name}")
	private String thefileName;
	
	@Autowired
	public FhirServerPropertyInfo(@Value("${fhir.server.file.name}") String myFileName) 
	{
		this.thefileName = myFileName;
		System.out.println("====== Post Construct== " + this.thefileName + " ===========");

	}
	
	public FhirServerPropertyInfo() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Set the file Name if instantiated separately
	 * @param fileName
	 */
	public void setTheFileName(String myFileName)
	{
		this.thefileName = myFileName;
	}
	
	/**
	 * @return the thefileName
	 */
	public String getThefileName() {
		return thefileName;
	}
	
}
