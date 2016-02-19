/**
 * Bean to hold input from FHIR Client UI
 */
package org.avinash.fhirclientjs.domain;

/**
 * @author ashanbhag
 *
 */
public class FhirQueryBean {
	
	//@Pattern(regexp = "^(https?://)?([da-z.-]+).([a-z.]{2,6})([w .-]*)*/?$")
	private String url;
	
	//@NotNull
	private String resource;
	
	//@NotNull
	private String id;
	
	private String auth;
	
	private String launch;
	
	/**
	 * Default CTOR
	 */
	public FhirQueryBean()
	{
		this.url = null;
		this.id  = null;
		this.auth = null;
		this.resource = null;
		this.launch = null;
		
	}
	
	


	/**
	 * @return the launch
	 */
	public String getLaunch() {
		return launch;
	}


	/**
	 * @param launch the launch to set
	 */
	public void setLaunch(String launch) {
		this.launch = launch;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	/**
	 * @return the auth
	 */
	public String getAuth() {
		return auth;
	}

	/**
	 * @param auth the auth to set
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FhirQueryBean [url=" + url + ", resource=" + resource + ", id="
				+ id + ", auth=" + auth + ", launch=" + launch + "]";
	}
	
	

}
