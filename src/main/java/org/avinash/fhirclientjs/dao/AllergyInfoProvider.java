/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.AllergyInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * @author ashanbhag
 * 
 * This class queries <i>AllergyIntolerance</i> FHIR resource for a single patient 
 * and obtains data for display.
 *
 *
 */
public class AllergyInfoProvider extends BaseProvider 
{
	private String thePatientId; 
	

	/**
	 * Default CTOR
	 */
	public AllergyInfoProvider() 
	{
		// TODO Auto-generated constructor stub
		super();
		this.thePatientId = null;
	}

	/**
	 * 
	 * 
	 * @param myCtx
	 * @param myFhirURL
	 * @param myAccessToken
	 */
	public AllergyInfoProvider(FhirContext myCtx, String myFhirURL,
			String myAccessToken, String myPatientId) 
	{
		super(myCtx, myFhirURL, myAccessToken);
		
		this.thePatientId = myPatientId;
	}
	
	/**
	 * Primary Public API that queries the appropriate FHIR resource for the given
	 * patient and obtains <i>AllergyIntolerance</i> information 
	 * 
	 * @return <i>List</i> of <i>AllergyInfo</i> data for single patient. 
	 * 
	 */
	
	public List<AllergyInfo> buildAllergyInfo()
	{
		// Step 1: Get the HAPI Client 
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		// Query the appropriate FHIR resource.
		@SuppressWarnings("unchecked")
		List<AllergyIntolerance> allergyIntoleranceLs =  (List<AllergyIntolerance>) queryResource(client, 
				AllergyIntolerance.class, this.thePatientId);

		List <AllergyInfo> allergyInfoLs = getAllergyInfoForDisplay(allergyIntoleranceLs);

		return (allergyInfoLs);
	}
	
	
////////////////All Private methods below ////////////////////////////////
	
	private List<AllergyInfo> getAllergyInfoForDisplay(List<AllergyIntolerance> allergyIntoleranceLs)
	{
		List <AllergyInfo> allergyLst = new ArrayList<AllergyInfo>();

		if (allergyIntoleranceLs != null && allergyIntoleranceLs.size() >0)
		{
			for (int i=0; i < allergyIntoleranceLs.size(); i++)
			{
				AllergyIntolerance allergy = allergyIntoleranceLs.get(i);
				
				// For printing out the results..
				System.out.println("AllergyIntolerance Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(allergy));
				
				// substance
				String substance = allergy.getSubstance().getText();
				
				// Category
				String category = allergy.getCategory();
				
				// Status
				String status = allergy.getStatus();
				
				List <AllergyIntolerance.Reaction> reactions = allergy.getReaction();
				
				String reaction  = null;
				String severity = null;
				String when = null;
				
				for (AllergyIntolerance.Reaction r : reactions)
				{
					
					if (reaction == null)
						reaction = r.getManifestationFirstRep().getText();
					
					if (severity == null)
						severity = r.getSeverity();
					
					if (when == null)
						when = r.getOnsetElement().getValueAsString();
					
				}
				
				// Create AllergyInfo object to pass to UI
				AllergyInfo allergyInfo = new AllergyInfo();
				allergyInfo.setSubstance(substance);
				allergyInfo.setCategory(category);
				allergyInfo.setStatus(status);
				allergyInfo.setReaction(reaction);
				allergyInfo.setSeverity(severity);
				allergyInfo.setWhen(when);
				
				//add to the list
				allergyLst.add(allergyInfo);
				
			}
		}
		
		return (allergyLst);
	}
	
	
	

	

}
