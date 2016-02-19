/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.PatientInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * This class queries <i>Patient</i> FHIR resource and obtains data for display. This could 
 * query for a single patient or the entire patient resource, that's accessible to the user.
 * 
 * 
 * @author ashanbhag
 *
 */
public class PatientInfoProvider extends BaseProvider
{
	private String thePatientId; 	// Single Patient ID if available
	
	public static final int DEFAULT_NUM_OF_RECORDS = 50;
	private int theMaxRecordSize = PatientInfoProvider.DEFAULT_NUM_OF_RECORDS;

	
	/**
	 * Default CTOR
	 */
	
	public PatientInfoProvider()
	{
		super();
		this.thePatientId = null;
	}

	/**
	 * The one that's used for actual query
	 * 
	 * @param myFhirCtx
	 * @param myFhirURL
	 * @param myAccessToken
	 * @param myPatientId
	 */
	public PatientInfoProvider(FhirContext myFhirCtx, String myFhirURL, 
								String myAccessToken, String myPatientId)
	{
		super(myFhirCtx, myFhirURL, myAccessToken);
		this.thePatientId = myPatientId;
		
	}
	
	/**
	 * This method queries the FHIR service and returns <i>List</i> of <i>PatientInfo</i> objects
	 * that will be displayed by the Client.
	 * 
	 * 
	 * @return List of <i>PatientInfo</i> objects 
	 */
	
	public List<PatientInfo> buildPatientInfo()
	{
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		// Query patient resource for max number of records
		List <Patient> patResLs = queryResurce(client);
		
		// Get patient info for details
		List <PatientInfo>pats = getPatientInfoForDisplay(patResLs);
		
		return (pats);
		
	}
	

	
	
////////////////All Private methods below ////////////////////////////////
	
	/**
	 * Returns <i>True</i> if Patient ID value is given, otherwise returns <i>false</i>
	 * 
	 * @return boolean true if Patient Id is provided otherwise false
	 */
	private boolean isSinglePatientQuery()
	{
		boolean bRet = false;

		// If patient Id is set, then its a single patient query!
		if (this.thePatientId != null && this.thePatientId.length() > 0)
			bRet = true;
			
		return (bRet);
		
	}

	
	
	
	/**
	 * Queries the FHIR <i>Patient</i> resource using HAPI Client and returns
	 * <i>List</i> of <i>Patient</i> objects
	 * 
	 * @param client HAPI Client
	 * 
	 * @return List of <i>Patient</i> objects
	 */
	
	protected List <Patient> queryResurce(IGenericClient client)
	{
		List<Patient> patResLs = new ArrayList<Patient>();
		
		if (isSinglePatientQuery())
		{
			Patient patientRes = client.read(Patient.class, this.thePatientId);
			patResLs.add(patientRes);
		}
		else
		{
			Bundle bundle = client.search()
					.forResource(Patient.class)
					//.include(new Include("*"))
					.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
					//.limitTo(this.theMaxRecordSize)
					.encodedJson()
					.execute();
			
			if (bundle != null)
			{
				patResLs = bundle.getAllPopulatedChildElementsOfType(Patient.class); 
			}
			
		}
	
		return (patResLs);
		
		
	}
	
	/**
	 * Takes List of <i>Patient</i> objects which are raw results 
	 * from FHIR query and returns List of <i>PatientInfo</i> objects which are used for display
	 * 
	 * 
	 * @param patResLs List of <i>Patient</i> objects from the query of FHIR server.
	 *
	 * @return list of <i>PatientInfo</i> objects
	 */
	
	private List <PatientInfo> getPatientInfoForDisplay(List<Patient> patResLs)
	{
		List <PatientInfo> patientInfoLs = new ArrayList<PatientInfo>();
		
		if (patResLs != null && patResLs.size() > 0 )
		{
			
			for (int i=0; i < patResLs.size(); i++)
			{
				Patient p = patResLs.get(i);
				
				// For each patient, fill PatientInfo Details
				PatientInfo patInfo = getPatientInformationForDisplay(p);
				
				if (patInfo != null)
				{
					patientInfoLs.add(patInfo);
				}
			}
		}
		
		return (patientInfoLs);
	}
	
	/**
	 * Parse <i>Patient</i> object and fill <i>PatientInfo</i> object used for display
	 * 
	 * 
	 * @param patientRes Single <i>Patient</i> object 
	 * @return
	 */
	
	private PatientInfo getPatientInformationForDisplay(Patient patientRes)
	{
		PatientInfo patInfo = null;
		
		if (patientRes == null || patientRes.isEmpty())
			return null;
		
		// Go thru the Patient resource and populate the PatientInfo object
		patInfo = new PatientInfo();
		
		// Id
		String IdStr = patientRes.getId().getIdPart();	
		patInfo.setId(IdStr);
		
		// Full Name
		patInfo.setName(getFullName(patientRes));
		
		// Gender
		patInfo.setGender(patientRes.getGender());
		
		// Date of Birth
		Date date = patientRes.getBirthDate();
		
		if (date != null)
			patInfo.setDob(date.toString());
		
		// Address
		patInfo.setAddress(getAddress(patientRes));
		
		
		return (patInfo);
	}
	
	/**
	 * Get Full Name from <i>Patient</i> object.
	 * 
	 * @param p
	 * @return full name of the form - FamilyName, GivenName
	 */
	private String getFullName(Patient p)
	{
		String fName = null;
		
		List<HumanNameDt> names = p.getName();
		HumanNameDt pName = null;
		
		if (names != null && names.size() > 0)
		{
			 pName = names.get(0);
		}
		
		// Family Name, Given Name
		
		if ( pName != null && !pName.isEmpty())
		{
			fName  = pName.getFamilyAsSingleString() + "," +
					  pName.getGivenAsSingleString();
		}
		
		
		return (fName);
		
	}
	
	/**
	 * Gets Address for given <i>Patient</i> object
	 * @param p
	 * @return
	 */
	private String getAddress(Patient p)
	{
		String addStr = null;
		
		List <AddressDt> addlst = p.getAddress();
		
		if (addlst != null && addlst.size() >0)
		{
			AddressDt addObject = addlst.get(0);
			
			String country = addObject.getCountry();
			String city = addObject.getCity();
			String state = addObject.getState();
			String line1 = addObject.getLineFirstRep().getValueAsString();
			String pCode = addObject.getPostalCode();

			addStr = line1 + ", " + city + ", " + state + ", " + pCode + ", " + country;
			
		}
		return (addStr);
		
	}

}
