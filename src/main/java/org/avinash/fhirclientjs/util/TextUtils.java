/**
 * 
 */
package org.avinash.fhirclientjs.util;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * @author ashanbhag
 * This class provides utility functions to convert objects to text formats required for display 
 * 
 * 
 */
public class TextUtils {
	
	
	/*
	 * Convert object to JSON string
	 */
	public static String convertObjectListToJSON(@SuppressWarnings("rawtypes") List objectList)
	{
		String jsonStr = null;
		
		 ObjectMapper mapper = new ObjectMapper();
		 
		 try {
			jsonStr = mapper.writeValueAsString(objectList);
		 }
		 catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
 		 }
		 catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (jsonStr);
	}

	
	/*
	 * Convert object to JSON string
	 */
	public static String convertObjectToJSON(@SuppressWarnings("rawtypes") Object obj)
	{
		String jsonStr = null;

		 ObjectMapper mapper = new ObjectMapper();
		 
		 try {
			jsonStr = mapper.writeValueAsString(obj);
		 }
		 catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
 		 }
		 catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
		 catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (jsonStr);
	}
	
	
}
