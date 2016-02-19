/**
 * 
 */
package org.avinash.fhirclientjs.authorization.access;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.Validate;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.avinash.fhirclientjs.util.FhirAuthorizationCodeUtils;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonToken;

import ca.uhn.fhir.context.FhirContext;

/**
 * @author ashanbhag
 * 
 * This class is called in the redirect workflow and used to obtain Access Token.
 */
public class AccessTokenProvider {
	
	private FhirContext ctx = null;
	private String fhirURL = null;
	
	private String authCode = null;
	private String grant_type = null;
	private String token_url = null;
	
	public static final String ACCESS_TOKEN = "access_token";
	public static final String TOKEN_TYPE  = "token_type";
	public static final String SCOPE = "scope";
	public static final String EXPIRES_IN = "expires_in";
	public static final String PATIENT = "patient";
	public static final String SMART_STYLE_URL = "smart_style_url";
	public static final String NEED_PATIENT_BANNER = "need_patient_banner";
	
	
	
	
	
	
	
	// CTOR
	public AccessTokenProvider( FhirContext ctx, 
								String fhirURL, 
								String authCode, 
								String grant_type, 
								String token_url)
	{
		
		this.ctx = ctx;
		this.fhirURL = fhirURL;
		this.authCode = authCode;
		this.grant_type = grant_type;
		this.token_url = token_url;
		
	}
	
	
	public AccessToken getAccessToken()
	{
		Validate.notNull(this.token_url, "Token URL cannot be null!");
		return (post(this.token_url));
	}
	
	
	protected AccessToken post(String serviceURL)
	{
		
		HttpPost httpPost = new HttpPost(serviceURL);
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

		Validate.notNull(this.fhirURL, "Fhir URL cannot be null!");

		// Get client credentials
		String client_id = FhirAuthorizationCodeUtils.getClientId(this.fhirURL);
		String client_secret = FhirAuthorizationCodeUtils.getClientSecret(this.fhirURL);
		
		// Set authorization header
		setAuthorizationHeaders(httpPost, client_id, client_secret);

		// Do Post and obtain Response
		return (processRequest(httpPost, client_id, client_secret));
		
	}


	protected AccessToken processRequest(HttpPost httpPost, String client_id, String client_secret) 
	{
		
		Validate.notNull(this.authCode, "Code cannot be null!");
		
		
		// Request parameters for the post
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("code", this.authCode));
		nvps.add(new BasicNameValuePair("grant_type", this.grant_type));
		
		nvps.add(new BasicNameValuePair("redirect_uri", FhirAuthorizationCodeUtils.getRedirectURL(this.fhirURL)));
		
		// If public app, then add client_id as POST parameter.
		if (!isConfidentialApp(client_secret))
		{
			nvps.add(new BasicNameValuePair("client_id", client_id));
		}
		
        HttpResponse response = null;
        AccessToken accessToken = null;
        
		try 
		{
			httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			response = this.ctx.getRestfulClientFactory().getHttpClient().execute(httpPost);
	        int status = response.getStatusLine().getStatusCode();
			
            if (status != 200) 
            {
                HttpEntity entity = response.getEntity();
                String responseString = EntityUtils.toString(entity, "UTF-8");
                throw new RuntimeException(String.format("There was a problem attempting to get the access token.\nResponse Status : %s .\nResponse Detail :%s."
                        , response.getStatusLine()
                        , responseString));
            }		

            // Get the response into AccessToken object
            accessToken = extractAccessToken(response);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (accessToken);
		
		
	}


	protected AccessToken extractAccessToken(HttpResponse response) 
	{
		// Build Access Token
		AccessToken accessToken = null;
		
		// Get the Response data
		
		try {

			JsonFactory jsonFactory = new JsonFactory(); 
			JsonParser jp = jsonFactory.createJsonParser(new InputStreamReader(response.getEntity().getContent()));
			
			accessToken = new AccessToken();
			
			
			while(!jp.isClosed())
			{
			    JsonToken jsonToken = jp.nextToken();
			    
			    if(JsonToken.FIELD_NAME.equals(jsonToken))
			    {
			    	
		    	        String fieldName = jp.getCurrentName();
		    	        System.out.println("Field Name: " + fieldName + " and Field Value: " + jp.getText());
		    	        
		    	        jsonToken = jp.nextToken();
		    	        
		    	        if (fieldName.equalsIgnoreCase(ACCESS_TOKEN))
		    	        {
		    	        	accessToken.setAccess_token(jp.getText());
		    	        }
		    	        else if (fieldName.equalsIgnoreCase(TOKEN_TYPE))
		    	        {
		    	        	accessToken.setToken_type(jp.getText());
		    	        }
		    	        else if(fieldName.equalsIgnoreCase(SCOPE))
		    	        {
		    	        	accessToken.setScope(jp.getText());
		    	        }
		    	        else if (fieldName.equalsIgnoreCase(AccessTokenProvider.EXPIRES_IN))
		    	        {
		    	        	accessToken.setExpires_in(jp.getValueAsLong());
		    	        }
		    	        else if (fieldName.equalsIgnoreCase(AccessTokenProvider.PATIENT))
		    	        {
		    	        	accessToken.setPatient(jp.getText());
		    	        }
		    	        else if (fieldName.equalsIgnoreCase(AccessTokenProvider.SMART_STYLE_URL))
		    	        {
		    	        	accessToken.setSmart_style_url(jp.getText());
		    	        }
		    	        else if (fieldName.equalsIgnoreCase(AccessTokenProvider.NEED_PATIENT_BANNER))
		    	        {
		    	        	accessToken.setNeed_patient_banner(jp.getBooleanValue());
		    	        }
			        }
			    }
		    
		} catch (IOException io_ex) {
		    throw new RuntimeException("There was a problem attempting to get the access token", io_ex);
		}
		
		return accessToken;
	}
	
	/**
	 * Sets the authorization header for the post to token url
	 * 
	 * @param request
	 * @param client_id Client ID provided by Auth Server 
	 * @param client_secret Secret provided by Auth Server during registration. Will be null for Public Apps
	 * 
	 */
	protected void setAuthorizationHeaders(HttpRequest request, String client_id, String client_secret)
	{
		
		// If client Secret is null or empty, then, it is a "public" app and the header does not
		// need to be filled.
		
		if (isConfidentialApp(client_secret))
		{
			String encodingString = client_id + ":" + client_secret;
			byte[] encodedBytes = Base64.encodeBase64(encodingString.getBytes());
			String encodedStr = new String(encodedBytes);
			request.setHeader("Authorization", "Basic " + encodedStr);	
		}
		
	}
	
	/**
	 * Takes client secret value, and determines if the application is registered
	 * as confidential or public application
	 * 
	 * @param client_secret
	 * @return
	 */
	private boolean isConfidentialApp(String client_secret)
	{
		boolean bConfidential = false;
		
		if (client_secret != null && client_secret.length() > 0)
		{
			bConfidential = true;
		}
		return (bConfidential);
	}

	

}
