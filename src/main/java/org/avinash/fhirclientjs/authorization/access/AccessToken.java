/**
 * 
 */
package org.avinash.fhirclientjs.authorization.access;

import java.io.Serializable;

/**
 * @author ashanbhag
 *
 * This class contains all the important information pertaining to Access Tokens
	"access_token": "i8hweunweunweofiwweoijewiwe",
	  "token_type": "bearer",
	  "expires_in": 3600,
	  "scope": "patient/Observation.read patient/Patient.read",
	  "intent": "client-ui-name",
	  "patient":  "123",
	  "encounter": "456"
 */

public class AccessToken implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String access_token;
	
	private String token_type;
	
	private long expires_in;
	
	private String scope;
	
	private String patient;
	
	private String smart_style_url;
	
	private boolean need_patient_banner;
	
	
	/**
	 * CTOR
	 */
	public AccessToken()
	{
		this.access_token = null;
		this.token_type = null;
		this.expires_in = 0L;
		this.scope = null;
		this.patient = null;
		this.smart_style_url = null;
		this.need_patient_banner = false;
	}
	
	
	/**
	 * @return the access_token
	 */
	public String getAccess_token() {
		return access_token;
	}

	/**
	 * @param access_token the access_token to set
	 */
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	/**
	 * @return the token_type
	 */
	public String getToken_type() {
		return token_type;
	}

	/**
	 * @param token_type the token_type to set
	 */
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}

	/**
	 * @return the expires_in
	 */
	public long getExpires_in() {
		return expires_in;
	}

	/**
	 * @param expires_in the expires_in to set
	 */
	public void setExpires_in(long expires_in) {
		this.expires_in = expires_in;
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
	 * @return the patient
	 */
	public String getPatient() {
		return patient;
	}


	/**
	 * @param patient the patient to set
	 */
	public void setPatient(String patient) {
		this.patient = patient;
	}


	/**
	 * @return the smart_style_url
	 */
	public String getSmart_style_url() {
		return smart_style_url;
	}


	/**
	 * @param smart_style_url the smart_style_url to set
	 */
	public void setSmart_style_url(String smart_style_url) {
		this.smart_style_url = smart_style_url;
	}


	/**
	 * @return the need_patient_banner
	 */
	public boolean isNeed_patient_banner() {
		return need_patient_banner;
	}


	/**
	 * @param need_patient_banner the need_patient_banner to set
	 */
	public void setNeed_patient_banner(boolean need_patient_banner) {
		this.need_patient_banner = need_patient_banner;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", token_type="
				+ token_type + ", expires_in=" + expires_in + ", scope="
				+ scope + ", patient=" + patient + ", smart_style_url="
				+ smart_style_url + ", need_patient_banner="
				+ need_patient_banner + "]";
	}


	


}
