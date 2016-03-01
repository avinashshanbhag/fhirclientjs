/**
 * This class makes actual call to FHIR DAO services using HAPI interface
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.client.HeaderInterceptor;
import org.avinash.fhirclientjs.domain.AllergyInfo;
import org.avinash.fhirclientjs.domain.DiagnosticReportInfo;
import org.avinash.fhirclientjs.domain.DocumentReferenceInfo;
import org.avinash.fhirclientjs.domain.FhirQueryBean;
import org.avinash.fhirclientjs.domain.MedicationDispenseDetails;
import org.avinash.fhirclientjs.domain.MedicationOrderDetails;
import org.avinash.fhirclientjs.domain.MedicationStatementDetails;
import org.avinash.fhirclientjs.domain.MedicineInfo;
import org.avinash.fhirclientjs.domain.PatientDetails;
import org.avinash.fhirclientjs.domain.PatientInfo;
import org.avinash.fhirclientjs.domain.ProblemInfo;
import org.avinash.fhirclientjs.util.TextUtils;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.AllergyIntolerance;
import ca.uhn.fhir.model.dstu2.resource.Condition;
import ca.uhn.fhir.model.dstu2.resource.DocumentReference;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.MedicationAdministration;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement.Dosage;
import ca.uhn.fhir.model.dstu2.composite.AddressDt;
import ca.uhn.fhir.model.dstu2.composite.AgeDt;
import ca.uhn.fhir.model.dstu2.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.HumanNameDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.composite.TimingDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.DateTimeDt;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;

/**
 * @author ashanbhag
 *
 */
public class FhirDAO {
	

	/**
	 * Main entry point for obtaining information to display single patients details page. The 
	 * information returned includes (for a single patient):
	 * Patient Demographics - <i>Patient</i> FHIR Resource
	 * Medicine Information - <i>MedicationStatement</i>, <i>MedicationOrder</i> and <i>MedicationDispense</i> FHIR resource
	 * Document Reference -   <i>DocumentReference</i> FHIR resource
	 * Allergy Information -  <i>AllergyIntolerance</i> FHIR resource
	 * Problem Information -  <i>Condition</i> FHIR resource
	 *  
	 * @param fhirBean	contains the FHIR URL
	 * @param ctx		<i>FhirContext</i> object instantiated for HAPI Client
	 * @param token		the Access Token from OAuth workflow
	 * 
	 * @return JSON String containing the serialization of array of <i>PatientDetails</i> objects.
	 * 
	 * @throws Exception
	 */
	
	public static String querySinglePatient(FhirQueryBean fhirBean, FhirContext ctx, String token) throws Exception
	{
		String retString = null;
		
		// Build the JSON Result Object
		PatientDetails patDetails = new PatientDetails();

		// Get Patient Demographics from the patient resource
		PatientInfoProvider patInfoPr = new PatientInfoProvider(ctx,
																fhirBean.getUrl(),
																token,
																fhirBean.getId());
		
		if (patInfoPr != null)
		{
			List<PatientInfo> patInfoLs = patInfoPr.buildPatientInfo();

			// There is only one patient
			if (patInfoLs != null && patInfoLs.size() == 1)
				patDetails.setDemographics(patInfoLs.get(0));
		}
		
		// Obtain Medicines

		MedicineInfoProvider medInfoPr = new MedicineInfoProvider(ctx,
																  fhirBean.getUrl(), 
																  token, 
																  fhirBean.getId());
		
		
		if (medInfoPr != null)
		{

			// Add separately - MedicationStatement
			List <MedicationStatementDetails> medStatements = medInfoPr.buildMedicationStatementDetails();

			if (medStatements != null)
				patDetails.setMedStatementDetails(medStatements);
			
			// MedicationOrder
			List <MedicationOrderDetails> medOrders = medInfoPr.buildMedicationOrderDetails();

			if (medOrders != null)
				patDetails.setMedOrderDetails(medOrders);
			
			// MedicationDispense
			List <MedicationDispenseDetails> medDispenses = medInfoPr.buildMedicationDispenseDetails();

			if (medDispenses != null)
				patDetails.setMedDispenseDetails(medDispenses);
			
		}
		
		// Document Reference
		
		DocumentReferenceInfoProvider docInfoPr = new DocumentReferenceInfoProvider(ctx,
																					fhirBean.getUrl(),
																					token,
																					fhirBean.getId());
		
		if (docInfoPr != null)
		{
			List <DocumentReferenceInfo> docRefInfoLs = docInfoPr.buildDocumentReferenceInfo();
			
			if (docRefInfoLs != null)
				patDetails.setDocumentReferences(docRefInfoLs);
		}
		
		// Problems
		ProblemInfoProvider probInfoPr = new ProblemInfoProvider(ctx, fhirBean.getUrl(),
				token, fhirBean.getId());
		
		if (probInfoPr != null)
		{
			List <ProblemInfo> probInfoLs = probInfoPr.buildProblemInfo();
			
			if (probInfoLs != null)
				patDetails.setProblems(probInfoLs);
			
		}
		
		// Allergies
		AllergyInfoProvider allergyInfoPr = new AllergyInfoProvider(ctx, fhirBean.getUrl(), 
				token, fhirBean.getId());
		
		if (allergyInfoPr != null)
		{
			List <AllergyInfo> allergyInfoLs = allergyInfoPr.buildAllergyInfo();
			
			if (allergyInfoLs != null)
				patDetails.setAllergies(allergyInfoLs);
		}
		
		DiagnosticReportProvider diagReportPr = new DiagnosticReportProvider(ctx, fhirBean.getUrl(),
				token, fhirBean.getId());
		
		if (diagReportPr != null)
		{
			List <DiagnosticReportInfo> diagReportLs = diagReportPr.buildDiagnosticReportInfo();
			
			if (diagReportLs != null)
				patDetails.setDiagnosticReports(diagReportLs);
			
		}
		
		
		// Convert to JSON String
		retString = TextUtils.convertObjectToJSON(patDetails);
		System.out.println("Demographics, DocReference, Meds, Allergies, Problems and DiagnosticReports: " + retString);
		
		return (retString);
		
	}
	
	
	/**
	 * Main entry point from UI to get list of patients available for query via OAuth2
	 * 
	 * 
	 * @param fhirBean	Contains the FHIR Server URL to query
	 * @param ctx		HAPI FHIR Context needed to use HAPI API to perform query
	 * @param token		Access Token from OAuth, needed to perform secured query via OAuth
	 * @return			JSON String representing array of <i>PatientInfo</i> objects
	 * 
	 * @throws Exception
	 */
	
	public static String getAllPatients(FhirQueryBean fhirBean, FhirContext ctx, String token) throws Exception
	{
		String resString = null;

		List <PatientInfo> pats = new ArrayList<PatientInfo>();
		
		PatientInfoProvider patInfoProv = new PatientInfoProvider(ctx,
												fhirBean.getUrl(),
												token,
												fhirBean.getId());

		if (patInfoProv != null)
		{
			pats = patInfoProv.buildPatientInfo();
		}
		
		// Convert to JSON Array for UI
		resString = TextUtils.convertObjectListToJSON(pats);
		System.out.println(resString);
		return (resString);
	}


	
	/**
	 * Queries list of Patients with general information
	 * 
	 * @param pats
	 * @param client
	 */
	/*
	protected static void queryAllPatientsGeneralInfo(List<PatientInfo> pats, IGenericClient client) 
	{
		
		// Make a call and get results
		ca.uhn.fhir.model.api.Bundle bundle = client.search().forResource(Patient.class)
				.encodedJson()
				.execute();

		if (bundle != null && !bundle.isEmpty())
		{
			int total = bundle.getTotalResults().getValue();
			
			//System.out.println("Total number of patients: " + total);
			
		}
		
		// Flag to get all records. Otherwise, get only one page!
		boolean bAllRecords = false;
		
		do
		{
			// probe each bundle
			getbundleDetails(bundle, pats);
			
			if (bundle.getLinkNext() != null &&
				!bundle.getLinkNext().isEmpty())
			{
				//System.out.println("Bundle Next Link=" + bundle.getLinkNext()) ;
				
				bundle = client.loadPage().next(bundle).execute();

				if (bundle.getLinkNext().equals(bundle.getLinkLast()))
					bundle = null;
			}
			
		}
		while (bundle != null && !bundle.isEmpty() && bAllRecords);
		
	}
	*/
	
	
	
	/**
	 * Utility method to query individual bundle data
	 */
	
	/*
	static void getbundleDetails(ca.uhn.fhir.model.api.Bundle bundle, List <PatientInfo> pats)
	{
		
		
		if (bundle == null || bundle.isEmpty())
			return;
		
		// Iterate thru the bundle
		for (int i = 0; i <bundle.getEntries().size();i++)
		{
			PatientInfo singlePatient = new PatientInfo();
			
			Patient p = (Patient) bundle.getEntries().get(i).getResource();
			
			// Id
			String IdStr = p.getId().getIdPart();	
			singlePatient.setId(IdStr);
			
			// Full Name
			singlePatient.setName(FhirDAO.getFullName(p));
			
			// Gender
			singlePatient.setGender(p.getGender());
			
			// Date of Birth
			Date date = p.getBirthDate();
			
			if (date != null)
				singlePatient.setDob(date.toString());
			
			// Address
			singlePatient.setAddress(FhirDAO.getAddress(p));
			
			// For Debugging.
			//System.out.println("patient[" + i + "]=" + singlePatient.toString());
			
			// Add to the list
			pats.add(singlePatient);
			
		}
		
		return;
	}
	*/
	
	// Below are utility methods to extract data from FHIR Resource
	
	
	
	


}
