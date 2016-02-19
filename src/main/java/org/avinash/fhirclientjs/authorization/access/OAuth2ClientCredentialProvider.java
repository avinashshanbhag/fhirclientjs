/**
 * 
 */
package org.avinash.fhirclientjs.authorization.access;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.avinash.fhirclientjs.Application;
import org.avinash.fhirclientjs.util.ApplicationContextUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * This class provides OAuth2 Credentials to client during OAuth2 workflow.
 * @author ashanbhag
 *
 */
public class OAuth2ClientCredentialProvider {
	
	private static final String FHIR_OAUTH2_INFO_FILE_NAME = "fhir_oauth_info.json";
	
	private Map<String,OAuth2ClientCredential> credentialMap = null;
	
	// Only 1 instance across the jvm
	private static OAuth2ClientCredentialProvider instance; 
	
	private FhirServerPropertyInfo theFhirServerPropertyInfo;
	
	

    //static block initialization for exception handling
    static
    {
        try{
        		instance = new OAuth2ClientCredentialProvider();
        }catch(Exception e){
            throw new RuntimeException("Exception occured in creating singleton instance", e);
        }
    }	
	
	/**
	 *  Public getter for the instance
	 */
	public static OAuth2ClientCredentialProvider getInstance()
	{
		
		return instance;
		
	}
	
	/**
	 * CTOR
	 */
	private OAuth2ClientCredentialProvider()
	{
		if (this.credentialMap == null)
		{
			this.credentialMap = new HashMap<String,OAuth2ClientCredential>();
			
			ApplicationContext appCtx = ApplicationContextUtils.getApplicationContext();
			
			if (appCtx != null)
			{
				this.theFhirServerPropertyInfo = (FhirServerPropertyInfo) appCtx.getBean("fhirServerPropertyInfo");
				
				if (this.theFhirServerPropertyInfo == null || 
					(this.theFhirServerPropertyInfo.getThefileName() == null || 
					this.theFhirServerPropertyInfo.getThefileName().length() == 0))
				{
					// set default file name.
					this.theFhirServerPropertyInfo = new FhirServerPropertyInfo();					
					this.theFhirServerPropertyInfo.setTheFileName(FHIR_OAUTH2_INFO_FILE_NAME);
				}
				
				System.out.println("=========" + this.theFhirServerPropertyInfo.getThefileName() + "================");
				
			}
			else
			{
				System.out.println("App Context is null!");
			}
			
			// load the data
			loadData();
		}
	}
			

	/**
	 * Get the client Id for the FHIR endpoint
	 * @param FhirURL
	 * @return
	 */
	public String getClientId(String FhirURL)
	{
		return (this.credentialMap.get(FhirURL).getClient_id());
	}
	
	/**
	 * Get the client secret for the FHIR endpoint
	 * @param FhirURL
	 * @return
	 */
	
	public String getClientSecret(String FhirURL)
	{
		return (this.credentialMap.get(FhirURL).getClient_secret());
	}
	
	/**
	 * Get Authorize URL for the FHIR endpoint
	 * @param FhirURL
	 * @return
	 */
	
	public String getAuthorizeURL(String FhirURL)
	{
		return (this.credentialMap.get(FhirURL).getAuthorize());
	}
	
	/**
	 * Get Token URL for the FHIR Endpoint
	 * @param FhirURL
	 * @return
	 */
	
	public String getTokenURL(String FhirURL)
	{
		return(this.credentialMap.get(FhirURL).getToken());
	}
	
	public String getLaunchContext(String FhirURL)
	{
		return (this.credentialMap.get(FhirURL).getLaunch());
	}
	
	public String getScope(String FhirURL)
	{
		return (this.credentialMap.get(FhirURL).getScope());
	}
	
	public String getRedirectURL(String FhirURL)
	{
		return (this.credentialMap.get(FhirURL).getRedirect_url());
	}
	
	
	/**
	 * Reads the JSON File and loads client credentials.
	 * 
	 * @return
	 */
	private void loadData()
	{

		// Create a hashMap
		//Map<String,OAuth2ClientCredential> credMap = new HashMap<String,OAuth2ClientCredential>();
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		try {

				JSONClientCredentials jsonCreds = 
					mapper.readValue(new FileInputStream(this.theFhirServerPropertyInfo.getThefileName()), JSONClientCredentials.class );
				
				if (jsonCreds != null && jsonCreds.getEndpoints().size() > 0) 
				{
					
					// iterate thru the credential list
					for (int i=0; i < jsonCreds.getEndpoints().size(); i++)
					{
						OAuth2ClientCredential cred = jsonCreds.getEndpoints().get(i);
						
						System.out.println("Cred[" + i + "]=" + cred.toString());
						
						// Add to the map
						if (cred != null && cred.getFhirURL() != null)
						{
							String key = cred.getFhirURL();
							System.out.println("Key: " + key);
							this.credentialMap.put(key, cred);
						}
					}
				}
			
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return;
	}
	

}
