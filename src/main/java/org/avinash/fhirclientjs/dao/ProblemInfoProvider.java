package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.ProblemInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.AgeDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * @author ashanbhag
 * 
 * This class queries <i>Condition</i> FHIR resource for a single patient 
 * and obtains data for display.
 *
 */
public class ProblemInfoProvider extends BaseProvider 
{
	private String thePatientId;
	
	
	/*
	 * Default CTOR
	 */
	public ProblemInfoProvider()
	{
		super();
		this.thePatientId = null;
	}
	

	/**
	 * Real CTOR. 
	 * 
	 * @param myCtx	Instance of <i>FhirContext</i> class required to perform HAPI based query
	 * @param myFhirURL Based FHIR server URL
	 * @param myAccessToken Bearer token obtained from OAuth2 workflow
	 * @param myPatientId The single patient Id on which to query for problem information
	 */
	public ProblemInfoProvider(FhirContext myCtx, String myFhirURL,
			 String myAccessToken, String myPatientId)
	 {
		super(myCtx, myFhirURL, myAccessToken);
		this.thePatientId = myPatientId;
	 }
	
	/**
	 * Primary Public API that queries the appropriate FHIR resource for the given
	 * patient and obtains <i>Condition</i> information 
	 * 
	 * 
	 * @return <i>List</i> of <i>ProblemInfo</i> data for single patient from <i>Condition</i> FHIR resource
	 * 
	 */
	
	public List <ProblemInfo> buildProblemInfo()
	{
		// Step 1: Get the HAPI Client 
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		// Query the appropriate FHIR resource.
		@SuppressWarnings("unchecked")
		List<Condition> condLs =  (List<Condition>) queryResource(client, 
				Condition.class, this.thePatientId);

		List <ProblemInfo> probInfoLs = getProblemInfoForDisplay(condLs);

		return (probInfoLs);
	}
	
////////////////All Private methods below ////////////////////////////////

	
	private List<ProblemInfo> getProblemInfoForDisplay(List<Condition> condLs)
	{
		
		List <ProblemInfo> probLst = new ArrayList<ProblemInfo>();
		
		if (condLs != null && condLs.size() > 0)
		{
			for (int i=0; i < condLs.size(); i++)
			{
				Condition prob = condLs.get(i);
				
				// For printing out the results..
				System.out.println("Condition Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(prob));
				
				
				// Problem type - Condition.category
				String type = prob.getCategory().getText();
				
				// Problem age of Onset - Condition.onsetAge
				String onSetValue = null;
				
				if (prob.getOnset() instanceof AgeDt)
				{
					System.out.println("Onset is instance of AgeDt");
					onSetValue = ((AgeDt) prob.getOnset()).getValue().toPlainString() + 
							" " + ((AgeDt) prob.getOnset()).getUnit().toString();
					
				}
				else if (prob.getOnset() instanceof DateTimeDt)
				{
					System.out.println("Onset is instance of DateTimeDt:");
					onSetValue = ((DateTimeDt) prob.getOnset()).getValueAsString().toString();
					
				}
				/*
				else if (prob.getOnset() instanceof PeriodDt)
				{
					
				}
				else if(prob.getOnset() instanceof RangeDt)
				{
					
				}
				else if (prob.getOnset() instanceof StringDt)
				{
					
				}
				*/
				else
				{
					if (prob.getOnset() != null)
						System.out.println("Onset is instance of :" + prob.getOnset().getClass().toString());
				}
				
				// Problem date of documentation
				String dateRecorded = null;
				if (prob.getDateRecorded() != null)
					dateRecorded = prob.getDateRecorded().toString();
				
				List<CodingDt> codingLst = prob.getSeverity().getCoding();

				// for now, just get the first one
				String severity = null;
				if (codingLst != null && codingLst.size() > 0)
				{
					CodingDt codeDt = codingLst.get(0);

					// Problem severity
					severity = codeDt.getDisplay();
				}

				// Problem code
				String code = null;
				String desc = null;
				
				List<CodingDt> codeLst = prob.getCode().getCoding();
				if (codeLst != null && codeLst.size() > 0)
				{
					CodingDt cd = codeLst.get(0);

					// Problem code
					code = cd.getCode();
					
					// Problem description
					desc = cd.getDisplay();
				}
				
				ProblemInfo probInfo = new ProblemInfo();
				probInfo.setCode(code);
				probInfo.setDescription(desc);
				probInfo.setDateRecorded(dateRecorded);
				probInfo.setType(type);
				probInfo.setSeverity(severity);
				probInfo.setAgeAtOnset(onSetValue);
				
				// Add to list
				probLst.add(probInfo);
			}
		}
		
		return (probLst);
		
	}

}
