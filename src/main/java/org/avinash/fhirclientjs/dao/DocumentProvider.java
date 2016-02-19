/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import ca.uhn.fhir.context.FhirContext;


/**
 * @author ashanbhag
 * Service Method to get attached <i>Document</i> from <i>DocumentReference</i> resource.
 *
 */
public class DocumentProvider
{
	private FhirContext theCtx;
	private String theFhirURL;
	private String theAccessToken;
	
	/**
	 * Default CTOR
	 */
	public DocumentProvider()
	{
		this.theCtx = null;
		this.theFhirURL = null;
		this.theAccessToken = null;
	}
	

	/**
	 * 
	 * @param myCtx 		FHIR Context
	 * @param myFhirURL		FHIR Server Base URL
	 * @param myAccessToken OAuth2 access token obtained from OAuth2 protocol
	 */
	
	public DocumentProvider(FhirContext myCtx, String myFhirURL, String myAccessToken)
	{
		this.theCtx = myCtx;
		this.theFhirURL = myFhirURL;
		this.theAccessToken = myAccessToken;
	}
	
	
	/**
	 * Performs GET to obtain stored <i>Document</i> resource in a secured manner. Security is provided 
	 * using the access token obtained from the OAuth workflow.
	 * 
	 * @param serviceURL URL linked from the base FHIR server, that points to stored <i>Document</i> that can
	 * 					 be obtained securely
	 * @return String representation of content in the URL 
	 * @throws IOException 
	 */
	public String doGet(String serviceURL) 
	{
		// Validate that need data is present
		Validate.notEmpty(serviceURL, "URL for document is null!");
		Validate.notNull(this.theCtx, "FHIR Context cannot be null!");
		Validate.notEmpty(this.theFhirURL, "FHIR Base URL is null!");
		Validate.notEmpty(this.theAccessToken, "Access Token is null!");
		
		// Build full URL path that includes FHIR base URL
		String urlFullPath = buildFullURLPath(serviceURL);
		
		// HTTP GET protocol
		HttpGet httpGet = new HttpGet(urlFullPath);
		
		// Set the bearer token to the header
		setAuthorizationHeaders(httpGet, this.theAccessToken);
		
		HttpResponse response = null;
		String responseString = null;
		
		try 
		{
			response = this.theCtx.getRestfulClientFactory().getHttpClient().execute(httpGet);
	        int status = response.getStatusLine().getStatusCode();

            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
 
            if (status != 200) 
            {
                throw new RuntimeException(String.format("There was a problem attempting to get the data from URL.\nResponse Status : %s .\nResponse Detail :%s."
                        , response.getStatusLine()
                        , responseString));
            }

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Doc:" + responseString);
		
		return (responseString);
	}
	
//////////////// All Private methods below ////////////////////////////////
	/**
	 * Obtain full URL path including FHIR base server.
	 * @param serviceURL
	 * @return
	 */
	
	private String buildFullURLPath(String serviceURL)
	{
		String fullPathURLStr = null;
		
		// Check if the service URL is already full path.
		boolean bAbsPath = isURLAbsolutePath(serviceURL);

		if (!bAbsPath)
		{
			// Add the FHIR base path to the service URL
			fullPathURLStr = this.theFhirURL + serviceURL;
		}
		else
			fullPathURLStr = serviceURL;
		
		return (fullPathURLStr);
	}
	
	/**
	 * 
	 * @param urlString
	 * @return
	 */
	
	protected boolean isURLAbsolutePath(String urlString)
	{
		boolean bAbsPath = false;
		
		URI u = null;
		
		
		try 
		{
			
			u = new URI(urlString);

			if(u.isAbsolute())
			{
				System.out.println("Yes, i am absolute!: " + urlString);
				bAbsPath = true;
			}
			
		} catch (URISyntaxException e) 
		{
			System.out.println("URL string :" + urlString + " is not proper!" + e.getReason() );
		} 
		
		return (bAbsPath);
	}

	/*
	This interceptor adds a header resembling the following:<br>
	 * &nbsp;&nbsp;&nbsp;<code>Authorization: Bearer dsfu9sd90fwp34.erw0-reu</code><br>
	 * where the token portion (at the end of the header) is supplied by the invoking code.
	 
	 */
	
	protected void setAuthorizationHeaders(HttpRequest request, String myBearerToken)
	{
		
		String header_name = "Authorization";
		String header_value = "Bearer " + myBearerToken;
				
		request.addHeader(header_name, header_value);
		
		return;
		
	}
 
	 
	 
	
	

}
