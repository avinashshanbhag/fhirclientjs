/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.avinash.fhirclientjs.client.HeaderInterceptor;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;

/**
 * Provides implementation of common methods. It cannot be itself instantiated.
 * 
 * @author ashanbhag
 *
 */
public abstract class BaseProvider 
{


	private FhirContext theCtx;		// HAPI FHIR Context		
	private String theFhirURL;		// FHIR Base URL
	private String theAccessToken;	// Bearer Token

	

	
	/**
	 * Default CTOR
	 */
	public BaseProvider()
	{
		this.theCtx = null;
		this.theFhirURL = null;
		this.theAccessToken = null;
	}

	
	/**
	 * Business CTOR
	 * 
	 * @param myCtx
	 * @param myFhirURL
	 * @param myAccessToken
	 */
	public BaseProvider(FhirContext myCtx, String myFhirURL, String myAccessToken)
	{
		this.theCtx = myCtx;
		this.theFhirURL = myFhirURL;
		this.theAccessToken = myAccessToken;
	}

	/**
	 * @return the theCtx
	 */
	public FhirContext getTheCtx() {
		return theCtx;
	}

	/**
	 * @param theCtx the theCtx to set
	 */
	public void setTheCtx(FhirContext theCtx) {
		this.theCtx = theCtx;
	}

	/**
	 * @return the theFhirURL
	 */
	public String getTheFhirURL() {
		return theFhirURL;
	}

	/**
	 * @param theFhirURL the theFhirURL to set
	 */
	public void setTheFhirURL(String theFhirURL) {
		this.theFhirURL = theFhirURL;
	}

	/**
	 * @return the theAccessToken
	 */
	public String getTheAccessToken() {
		return theAccessToken;
	}

	/**
	 * @param theAccessToken the theAccessToken to set
	 */
	public void setTheAccessToken(String theAccessToken) {
		this.theAccessToken = theAccessToken;
	}
	
	
	/**
	 * This method create instance of <i>IGenericClient</i> with appropriate headers 
	 * based on OAuth (i.e. bearer token) and content-type. This is required before
	 * actual query can be conducted using HAPI Client.
	 * 
	 * @return IGenericClient object with appropriate headers.
	 */
	protected IGenericClient getHapiClient()
	{
		IGenericClient client = null;

		// Connect to the HAPI FHIR Server
		client = this.theCtx.newRestfulGenericClient(this.theFhirURL);
		
		if (client != null)
		{
			
			// Register Authorization Interceptor - addes Authorization header to the REST query
			
			if (isSecuredQuery())
			{
				// Bearer Authorization Interceptor
				BearerTokenAuthInterceptor authInterceptor = new BearerTokenAuthInterceptor(this.theAccessToken);

				// Register the interceptor with your client
				client.registerInterceptor(authInterceptor);
				
			}
			
			// Register Logging Interceptor  - Adds logging flag
			LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
			loggingInterceptor.setLogRequestHeaders(true);
			loggingInterceptor.setLogRequestBody(true);

			client.registerInterceptor(loggingInterceptor);
			
			// Register content-type  - Addes Content-type to the header
			HeaderInterceptor headerInterceptor = new HeaderInterceptor("application/json");
			
			// Register the interceptor with the client.
			client.registerInterceptor(headerInterceptor);
		}
		
		return (client);
	}

	/**
	 * This method is used to query for FHIR resource when simply URL is provided. This is typically
	 * done when the URL is obtained as a reference from another FHIR resource.
	 * 
	 * @param client 		HAPI client
	 * @param theClass		the java class representing the FHIR resource
	 * @param fullURL		the full URL to be used for query
	 * @return fully populated instance of <i>theClass</i> or null
	 */
	protected IResource queryResourceByURL(IGenericClient client,
			Class<? extends IResource> theClass,
			String fullURL)
	{
		IResource res = null;
		
		
		try
		{
			
			res = client
					.read()
					.resource(theClass)
					.withUrl(fullURL)
					.execute();
			
			
		}
		catch (Exception e)
		{
			System.out.println("Exception in querying for class < " + theClass + "> = " + e.getMessage());
		}
		
		return (res);

	}
	
	/**
	 * This method is used to query for all FHIR resources, one at a time, for 
	 * single patient Id.
	 * 
	 * @param client 	<i>IGenericClient</i> interface that is used to make a HAPI Client API
	 * @param theClass  FHIR class
	 * 
	 * @return List of FHIR resources based on the type passed in the "theClass" parameter.
	 */
	protected List<? extends IResource> queryResource(IGenericClient client, 
			Class<? extends IResource> theClass,
			String patientId)
	{
		List<? extends IResource> resLs = new ArrayList<IResource>();
		
		try
		{
			Bundle bundle = client.search()
					.forResource(theClass)
					.where(new StringClientParam("patient").matches().value(patientId) )
					.include(new Include("*"))
					.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
					.encodedJson()
					.execute();
			
			System.out.println("Obtained bundle: " + bundle);
	
			if (bundle != null)
			{
				resLs = bundle.getAllPopulatedChildElementsOfType(theClass); 
			}
		}
		catch (Exception e)
		{
			System.out.println("Exception in querying for class < " + theClass + "> = " + e.getMessage());
		}
		/*
		do
		{
			// Get the current bundle data
			if (bundle != null)
			{
				List<? extends IResource> resBundleLs = new ArrayList<IResource>();
				resBundleLs = bundle.getAllPopulatedChildElementsOfType(theClass);
				
				// add to the overall list
				//resLs.addAll(resBundleLs);
			}
			
			// Now check if there are additional bundles.
			if (bundle.getLink(IBaseBundle.LINK_NEXT) != null &&
				!bundle.getLink(IBaseBundle.LINK_NEXT).isEmpty())
			{
				System.out.println("Loading Next bundle!");
				
				bundle = client.loadPage().next(bundle).execute();

				// Reached the end!
				if (bundle.getLink(IBaseBundle.LINK_NEXT).equals(bundle.getLink(IBaseBundle.LINK_PREV)))
				{
					System.out.println("Last Link reached");
					bundle = null;
				}
				else
				{
					System.out.println("More bundles remaining!");
				}
			}
			else
			{
				System.out.println("No more bundle!");
				bundle = null;
			}
			
			
		}
		while(bundle != null && !bundle.isEmpty());
		*/
		
		
		return (resLs);

	}
	
	

////////////////All Private methods below ////////////////////////////////

	
	/**
	 * This method is used to determine if the API is to be secured by OAuth
	 * 
	 * @return true is Access Token is given, otherwise false
	 */
	private boolean isSecuredQuery()
	{
		boolean bRet = false;
		
		// If access token is given, then, its a secured query using OAuth2.
		if (this.theAccessToken != null && this.theAccessToken.length() > 0)
			bRet = true;
			
		return (bRet);
		
	}
	

}
