package org.avinash.fhirclientjs.authorization.authorizationcode;

/**
 * This class will have the following information required to call the "authorize" URL:
 * 1) FHIR server Authorize URL
 * 2) client_id 
 * 3) response_type
 * 4) scope
 * 5) redirect_uri
 * 6) state
 * 7) aud
 * 
 * Example:
 * "https://authorize-dstu2.smarthealthit.org/authorize?
 *  client_id=123ebdc1-8eca-4f23-881d-882b8be90207
 *  &response_type=code
 *  &scope=user/*.read
 *  &redirect_uri=http://localhost:8080/avifhirclient/oauth
 *  &state=abc
 *  &aud=https://fhir-api-dstu2.smarthealthit.org";

 * 
 * @author ashanbhag
 *
 */

public class AuthorizationCodeRequest {

	public static final String RESPONSE_TYPE_CODE = "code";
	public static final String SCOPE_USER_READ = "user/*.read";
	public static final String SCOPE_LAUNCH = "launch";
	
	
	private String token_url = null;


	private String authorize_url  = null;
	private String client_id = null;
	private String response_type = AuthorizationCodeRequest.RESPONSE_TYPE_CODE;
	private String scope = AuthorizationCodeRequest.SCOPE_USER_READ;
	private String state = null;
	private String aud = null;
	private String launch = null;
	
	
	/**
	 * @return the launch context
	 */
	public String getLaunch() {
		return launch;
	}

	/**
	 * @param launch the launch context to set
	 */
	public void setLaunch(String launch) {
		this.launch = launch;
	}

	public AuthorizationCodeRequest() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the authorize_url
	 */
	public String getAuthorize_url() {
		return authorize_url;
	}



	/**
	 * @param authorize_url the authorize_url to set
	 */
	public void setAuthorize_url(String authorize_url) {
		this.authorize_url = authorize_url;
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
	 * @return the response_type
	 */
	public String getResponse_type() {
		return response_type;
	}



	/**
	 * @param response_type the response_type to set
	 */
	public void setResponse_type(String response_type) {
		this.response_type = response_type;
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}



	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}



	/**
	 * @return the aud
	 */
	public String getAud() {
		return aud;
	}



	/**
	 * @param aud the aud to set
	 */
	public void setAud(String aud) {
		this.aud = aud;
	}

	/**
	 * @return the token_url
	 */
	public String getToken_url() {
		return token_url;
	}

	/**
	 * @param token_url the token_url to set
	 */
	public void setToken_url(String token_url) {
		this.token_url = token_url;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AuthorizationCodeRequest [token_url=" + token_url
				+ ", authorize_url=" + authorize_url + ", client_id="
				+ client_id + ", response_type=" + response_type + ", scope="
				+ scope + ", state=" + state + ", aud=" + aud + ", launch="
				+ launch + "]";
	}

	
	
	
	
}
