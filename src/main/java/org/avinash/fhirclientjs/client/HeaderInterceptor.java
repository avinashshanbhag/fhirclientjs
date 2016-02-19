package org.avinash.fhirclientjs.client;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

import ca.uhn.fhir.rest.client.IClientInterceptor;

public class HeaderInterceptor implements IClientInterceptor {
	
	private static final String HEADER_ACCEPT = "Accept";
	private String myContentType = null;

	
	public HeaderInterceptor(String theContentType)
	{
		super();
		myContentType = theContentType;
		
	}

	@Override
	public void interceptRequest(HttpRequestBase theRequest) {
		if (myContentType != null)
			theRequest.addHeader(HEADER_ACCEPT, myContentType);
	}

	@Override
	public void interceptResponse(HttpResponse arg0) throws IOException {
		// do nothing
		
	}

}
