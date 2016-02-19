/**
 * 
 */
package org.avinash.fhirclientjs.authorization.access;

/**
 * This class holds OAuth2 Client Credential information, which is obtained out-of-band
 * during the client registration process.
 * @author ashanbhag
 *
 */
public class OAuth2ClientCredential {
	
	private String fhirURL = null;		// FHIR Server Endpoint
	private String client_id  = null;	// Client ID provided by the Oauth server
	private String client_secret = null; // client secret provided by Oauth server
	
	private String authorize = null;
	private String token = null;
	
	private String __comment = null; 	// indicate the name of the FHIR server endpoint
	
	private String launch = null;		// launch context for EHR Launch
	
	private String scope = null; 		// scope for launch
	
	private String redirect_url = null; // Full path of client app's redirect URL.
	
			
	
	/**
	 * @return the launch
	 */
	public String getLaunch() {
		return launch;
	}
	/**
	 * @return the scope
	 */
	public String getScope() {
		return scope;
	}
	/**
	 * @param scope the scope to set
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}
	/**
	 * @param launch the launch to set
	 */
	public void setLaunch(String launch) {
		this.launch = launch;
	}
	/**
	 * @return the fhirURL
	 */
	public String getFhirURL() {
		return fhirURL;
	}
	/**
	 * @param fhirURL the fhirURL to set
	 */
	public void setFhirURL(String fhirURL) {
		this.fhirURL = fhirURL;
	}
	/**
	 * @return the client_id
	 */
	public String getClient_id() {
		return client_id;
	}
	/**
	 * @param client_id the client_id to set
	 */
	public void setClient_id(String client_id) {
		this.client_id = client_id;
	}
	/**
	 * @return the client_secret
	 */
	public String getClient_secret() {
		return client_secret;
	}
	/**
	 * @param client_secret the client_secret to set
	 */
	public void setClient_secret(String client_secret) {
		this.client_secret = client_secret;
	}
	/**
	 * @return the authorize
	 */
	public String getAuthorize() {
		return authorize;
	}
	/**
	 * @param authorize the authorize to set
	 */
	public void setAuthorize(String authorize) {
		this.authorize = authorize;
	}
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}
	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
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
	 * @return the redirect_url
	 */
	public String getRedirect_url() {
		return redirect_url;
	}
	/**
	 * @param redirect_url the redirect_url to set
	 */
	public void setRedirect_url(String redirect_url) {
		this.redirect_url = redirect_url;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OAuth2ClientCredential [fhirURL=" + fhirURL + ", client_id="
				+ client_id + ", client_secret=" + client_secret
				+ ", authorize=" + authorize + ", token=" + token
				+ ", __comment=" + __comment + ", launch=" + launch
				+ ", scope=" + scope + ", redirect_url=" + redirect_url + "]";
	}
	

}
