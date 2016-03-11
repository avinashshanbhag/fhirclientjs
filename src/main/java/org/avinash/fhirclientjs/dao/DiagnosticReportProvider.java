/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.DiagnosticReportInfo;
import org.avinash.fhirclientjs.domain.MedicationStatementDetails;
import org.avinash.fhirclientjs.domain.ObservationInfo;
import org.avinash.fhirclientjs.util.FhirUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.DiagnosticReport;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * @author ashanbhag
 *
 */
public class DiagnosticReportProvider extends BaseProvider {
	
	private String thePatientId;


	/**
	 * Default CTOR
	 */
	public DiagnosticReportProvider() 
	{
		super();
		this.thePatientId = null;
	}

	/**
	 * Real CTOR used before calling the FHIR server 
	 * @param myCtx
	 * @param myFhirURL
	 * @param myAccessToken
	 * @param myPatientId
	 */
	public DiagnosticReportProvider(FhirContext myCtx, String myFhirURL,
			String myAccessToken, String myPatientId) 
	{
		super(myCtx, myFhirURL, myAccessToken);
		this.thePatientId = myPatientId;
	}

	/**
	 * Primary Public API that queries the appropriate FHIR resource for the given
	 * patient and obtains <i>DiagnosticReport</i> information 
	 * 
	 * @return <i>List</i> of <i>DiagnosticReportInfo</i> data for single patient. 
	 */
	public List<DiagnosticReportInfo> buildDiagnosticReportInfo()
	{
		// Step 1: Get the HAPI Client 
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		// Query the appropriate FHIR resource.
		@SuppressWarnings("unchecked")
		List<DiagnosticReport> diagReportLs =  (List<DiagnosticReport>) queryResource(client, 
				DiagnosticReport.class, this.thePatientId);

		List <DiagnosticReportInfo> diagnosticReportInfoLs = getDiagnosticReportInfoForDisplay(diagReportLs);
		return (diagnosticReportInfoLs);
	}
	
////////////////All Private methods below ////////////////////////////////
	
	/**
	 * This method takes <i>DiagnosticReport</i> HAPI FHIR objects and populates <i>DiagnosticReportInfo</i>
	 * objects used by the client
	 * 
	 * @param diagReportLs <i>List</i> of <i>DiagnosticReport</i> objects
	 * 
	 * @return List of <i>DiagnosticReportInfo</i> objects used to populate the user interface.
	 * 
	 */
	private List<DiagnosticReportInfo> getDiagnosticReportInfoForDisplay(List<DiagnosticReport> diagReportLs)
	{
		List <DiagnosticReportInfo> diagReportInfoLs = new ArrayList<DiagnosticReportInfo>();
		
		if (diagReportLs != null && diagReportLs.size() >0)
		{
			for (int i=0; i < diagReportLs.size(); i++)
			{
				DiagnosticReport singleDiagnosticReport = diagReportLs.get(i);
				
				// For printing out the results..
				System.out.println("DiagnosticReport Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(singleDiagnosticReport));

				String text   = (singleDiagnosticReport.getText() != null) ?
								(singleDiagnosticReport.getText().toString()) : null;
								
				System.out.println("DiagnosticReport Text:" + text);
								
				String status = singleDiagnosticReport.getStatus();
				
				String category = null;
				if (singleDiagnosticReport.getCategory() != null)
				{
					category = (singleDiagnosticReport.getCategory().getCodingFirstRep() != null) ?
							   (singleDiagnosticReport.getCategory().getCodingFirstRep().getCode()) : null;
					
				}
				
				String code = null;
				String display = null;
				
				
				if (singleDiagnosticReport.getCode() != null)
				{
					code = (singleDiagnosticReport.getCode().getCodingFirstRep() != null) ?
						   (singleDiagnosticReport.getCode().getCodingFirstRep().getCode()) : null;
					
					display = (singleDiagnosticReport.getCode().getCodingFirstRep() != null) ?
							   (singleDiagnosticReport.getCode().getCodingFirstRep().getDisplay()) : null;
				}
				
				String issued = (singleDiagnosticReport.getIssued() != null) ?
								(singleDiagnosticReport.getIssued().toString()) : null;

								
				String performer = null;
				
				if (singleDiagnosticReport.getPerformer() != null)
				{
					performer = (singleDiagnosticReport.getPerformer().getReference() != null) ?
								(singleDiagnosticReport.getPerformer().getReference().getValue()) : null;
									
					// Check if the value is relative of absolute. And, build the full URL.
					if (performer != null && performer.length() > 0)
					{
						String fullUrl = FhirUtils.buildFullFhirURL(performer, this.getTheFhirURL());
						System.out.println("Full Performer URL: " + fullUrl);
					}
									
				}
				
				// Get all observations
				List<ObservationInfo> obsInfoLs = getObservations(singleDiagnosticReport);
				
/*				
				String result = null;
				ObservationInfo obsInfo = null;
				
				if (singleDiagnosticReport.getResult() != null)
				{
					
					result =  (singleDiagnosticReport.getResult().get(0).getReference() != null) ?
							  (singleDiagnosticReport.getResult().get(0).getReference().getValue()) : null;
							  
					// Check if the value is relative of absolute. And, build the full URL.
					if (result != null && result.length() > 0)
					{
						String fullUrl = FhirUtils.buildFullFhirURL(result, this.getTheFhirURL());
						System.out.println("Full result URL: " + fullUrl);
						
						if (fullUrl != null)
						{
							ObservationInfoProvider obsInfoProv = new ObservationInfoProvider(this.getTheCtx(),
																								this.getTheFhirURL(), 
																								this.getTheAccessToken(), 
																								fullUrl);
							if (obsInfoProv != null)
							{
								// Get the Observation FHIR Resource
								obsInfo = obsInfoProv.buildObservationInfo();
							}
						}
					}
				}
*/
				
				// Now create new object to populate the data
				DiagnosticReportInfo diagReportInfo = new DiagnosticReportInfo();
				diagReportInfo.setCategory(category);
				diagReportInfo.setCode(code);
				diagReportInfo.setDisplay(display);
				diagReportInfo.setStatus(status);
				diagReportInfo.setPerformer(performer);
				diagReportInfo.setIssued(issued);
				diagReportInfo.setObservationlist(obsInfoLs);
				
				
				//Add to the list
				diagReportInfoLs.add(diagReportInfo);				
			}
		}
		
		return (diagReportInfoLs);
	}
	
	/**
	 * This method iterates the <i>DiagnosticReport.result</i> field for observations and 
	 * then returns <i>List</i> of <i>ObservationInfo</i> objects.
	 * 
	 * @param theDiagnosticReport <i>DiagnosticReport</i> FHIR resource
	 * @return	<i>List</i> of <i>ObservationInfo</i> objects
	 */
	
	private List<ObservationInfo> getObservations(DiagnosticReport theDiagnosticReport)
	{
		if (theDiagnosticReport == null || theDiagnosticReport.getResult() == null)
			return null;
		
		List<ObservationInfo> obsInfoLs = new ArrayList<ObservationInfo>();
		
		List<ResourceReferenceDt> obsRefLs = theDiagnosticReport.getResult();
		
		for (int i=0; i < obsRefLs.size(); i++)
		{
			ResourceReferenceDt singleObsRef = obsRefLs.get(i);
			
			if (singleObsRef != null && singleObsRef.getReference() != null)
			{
				String refVal = singleObsRef.getReference().getValue();

				// Now get the observation Data here.
				if (refVal != null && refVal.length() > 0)
				{
					String fullUrl = FhirUtils.buildFullFhirURL(refVal, this.getTheFhirURL());
					
					System.out.println("Full result URL: " + fullUrl);
					
					if (fullUrl != null)
					{
						ObservationInfoProvider obsInfoProv = new ObservationInfoProvider(this.getTheCtx(),
																							this.getTheFhirURL(), 
																							this.getTheAccessToken(), 
																							fullUrl);
						if (obsInfoProv != null)
						{
							// Get the Observation FHIR Resource
							ObservationInfo obsInfo = obsInfoProv.buildObservationInfo();
							
							// Add to the list
							if (obsInfo != null)
								obsInfoLs.add(obsInfo);
						}
					}
				}
			}
		}
		
		return (obsInfoLs);
	}
	
	
	

}
