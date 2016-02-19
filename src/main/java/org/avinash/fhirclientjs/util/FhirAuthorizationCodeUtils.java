/**
 * 
 */
package org.avinash.fhirclientjs.util;

import java.util.HashMap;
import java.util.Map;

import org.avinash.fhirclientjs.authorization.access.OAuth2ClientCredentialProvider;

/**
 * @author ashanbhag
 *
 */
public class FhirAuthorizationCodeUtils {

	/**
	 * Key - FHIR Server Base URL
	 * Value - Bearer Token
	 */
	private static final Map <String, String> FhirServerAuthCodeMap;

	private static final Map <String, String> FhirServerAuthClientIDMap;
	
	private static final Map<String, String> FhirServerAuthClientSecretMap;
	
	
	
	static
	{
		FhirServerAuthClientSecretMap = new HashMap<String, String>();
		
		// Add Map: Key - OAuth Provider, Val - Client Secret
		
		// SMART
		FhirServerAuthClientSecretMap.put("https://fhir-api-dstu2.smarthealthit.org", 
								"M1avdTm_S0UbdU99lFvAuaqTlxyPzey08Ox2hvQykFuWQT6_-0-zUqGxEbOyEh6gvOyI96FzbIdnjDpFY64LvQ");
		
	}

	
	
	
	static
	{
		FhirServerAuthClientIDMap = new HashMap<String, String>();
		
		// Add Map: Key - FHIR Server, Val - Client ID
		
		// SMART
		FhirServerAuthClientIDMap.put("https://fhir-api-dstu2.smarthealthit.org", 
								"123ebdc1-8eca-4f23-881d-882b8be90207");

		// For now, use the SMART client ID for Argonaut Reference server too.. 
		FhirServerAuthClientIDMap.put("https://argonaut.healthintersections.com.au/open", 
				"123ebdc1-8eca-4f23-881d-882b8be90207");
		
		// RelayHealth Server
		
		FhirServerAuthClientIDMap.put("https://api.stage.data.relayhealth.com/rhc/fhirservice", 
				"7f071245-7743-4839-89aa-2259565a6087");
		
		
		
	}
	
	
	static
	{
		FhirServerAuthCodeMap = new HashMap<String, String>();
		
		// Add Map: Key - FHIR Server, Val - Bearer Token
		// Grahame RI
		FhirServerAuthCodeMap.put("https://argonaut.healthintersections.com.au/closed", 
				"38.78E852A75B76933C51D7F7173E7015839CE70E13464C3F3B2DD8591DF77A75D3");
		
		// SITE
		FhirServerAuthCodeMap.put("http://54.165.58.158:8081/FHIRServer/fhir", "AN3uCTC5B");
		
		// Epic
		FhirServerAuthCodeMap.put("https://open-ic.epic.com/Argonaut-Fixed/api/FHIR/Argonaut", "bX8YB9sTLy3Bcc1nG50NMKc+Das8MOT7efaMvqljDd9XwUz5xTbsCFkIYVnOM5E2uDosnvFYGkfAaQcTFZU4To3ylrcCPj2VvSOTJ+FaIC4hNeHWmlkBzVgzvVKs4mNl");
		
		
	}
	
	/**
	 * Returns the bearer token for the given FHIR URL
	 * @param FhirURL
	 * @return token String
	 */
	public static String getBearerToken(String FhirURL)
	{
		return( FhirServerAuthCodeMap.get(FhirURL));
	}
	
	public static String getClientId(String FhirURL)
	{
		//return (FhirServerAuthClientIDMap.get(FhirURL));
		return (OAuth2ClientCredentialProvider.getInstance().getClientId(FhirURL));
		
	}
	
	public static String getClientSecret(String FhirURL)
	{
		// return (FhirServerAuthClientSecretMap.get(FhirURL));
		return (OAuth2ClientCredentialProvider.getInstance().getClientSecret(FhirURL));
	}
	
	public static String getAuthorizeURL(String FhirURL)
	{
		return (OAuth2ClientCredentialProvider.getInstance().getAuthorizeURL(FhirURL));
	}
	
	public static String getTokenURL(String FhirURL)
	{
		return(OAuth2ClientCredentialProvider.getInstance().getTokenURL(FhirURL));
	}
	
	public static String getLaunchContext(String FhirURL)
	{
		return(OAuth2ClientCredentialProvider.getInstance().getLaunchContext(FhirURL));
	}
	
	public static String getScope(String FhirURL)
	{
		return (OAuth2ClientCredentialProvider.getInstance().getScope(FhirURL));
	}

	
	public static String getRedirectURL(String FhirURL)
	{
		//return ("http://localhost:8080/avifhirclient/oauth"); 
		return (OAuth2ClientCredentialProvider.getInstance().getRedirectURL(FhirURL));
		
	}
	
}
