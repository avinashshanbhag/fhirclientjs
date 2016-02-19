/**
 * 
 */
package org.avinash.fhirclientjs.authorization.access;

import java.util.List;



/**
 * This class is the object representation of the JSON file stored on the server, which 
 * contain OAuth2 client registration/access information.
 * @author ashanbhag
 *
 */
public class JSONClientCredentials {
	
	private String __comment = null;
	
	private List<OAuth2ClientCredential> endpoints = null;

	/**
	 * @return the __comment
	 */
	public String get__comment() {
		return __comment;
	}

	/**
	 * @param __comment the __comment to set
	 */
	public void set__comment(String __comment) {
		this.__comment = __comment;
	}

	/**
	 * @return the endpoints
	 */
	public List<OAuth2ClientCredential> getEndpoints() {
		return endpoints;
	}

	/**
	 * @param endpoints the endpoints to set
	 */
	public void setEndpoints(List<OAuth2ClientCredential> endpoints) {
		this.endpoints = endpoints;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JSONClientCredentials [__comment=" + __comment + ", endpoints="
				+ endpoints + "]";
	}
	
	

}
