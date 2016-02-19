/**
 * 
 */
package org.avinash.fhirclientjs.util;

import java.util.UUID;

/**
 * @author ashanbhag
 *
 */
public class StateProvider {
	
	public String getNewState()
	{
		// Get a random number and append to the prefix 
		return (UUID.randomUUID().toString());
	}

}
