/**
 * 
 */
package org.avinash.fhirclientjs.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Utility class to do any general non-instance specific activities
 * 
 * @author ashanbhag
 *
 */
public class FhirUtils {
	
	

	/**
	 * 
	 */
	public FhirUtils() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * @param urlString
	 * @return
	 */
	public static boolean isReferenceUrlAbsolute(String urlString)
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
	
	/**
	 * 
	 * @param refURL
	 * @param baseFhirUrl
	 * @return
	 */
	public static String buildFullFhirURL(String urlValue, String baseFhirUrl)
	{
		boolean bAbs = isReferenceUrlAbsolute(urlValue);
		
		if (!bAbs)
			return (baseFhirUrl + "/" + urlValue);
		else
			return (urlValue);
		
	}
	
	
}
