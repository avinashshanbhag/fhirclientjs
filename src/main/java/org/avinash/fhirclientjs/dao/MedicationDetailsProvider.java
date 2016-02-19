/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.MedicationDetails;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.Medication;
import ca.uhn.fhir.model.dstu2.resource.Medication.Product;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * @author ashanbhag
 *
 */
public class MedicationDetailsProvider extends BaseProvider 
{
	private String theMedReferenceURL;
	

	/**
	 * Default CTOR
	 */
	public MedicationDetailsProvider() 
	{
		super();
		this.theMedReferenceURL = null;
		
	}
	
	/**
	 * Business CTOR
	 * 
	 * @param myCtx
	 * @param myFhirURL
	 * @param myAccessToken
	 * @param myMedReferenceURL
	 */

	public MedicationDetailsProvider(FhirContext myCtx, String myFhirURL,
			String myAccessToken, String myMedReferenceURL) 
	{
		super(myCtx, myFhirURL, myAccessToken);
		this.theMedReferenceURL = myMedReferenceURL;
		
	}
	
	public MedicationDetails buildMedicationDetails()
	{
		MedicationDetails medDetails = null;
		
		// Step 1: Get the HAPI Client 
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.theMedReferenceURL, "MedicationReference cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		Medication medicationRes = (Medication) queryResourceByURL(client, 
													  Medication.class,
													  this.theMedReferenceURL);
		
		
		if (medicationRes != null)
		{
			medDetails = getMedicationDetailsForDisplay(medicationRes);
		}
		
		return (medDetails);
		
	}

///////////////////////////////////// ALL PRIVATE METHODS BELOW ///////////////////////
	
	private MedicationDetails getMedicationDetailsForDisplay(Medication medicationRes)
	{
		MedicationDetails medDetails = null;
		
		if (medicationRes != null)
		{
			// First dump the string
			System.out.println(this.getTheCtx()
							   .newXmlParser()
							   .setPrettyPrint(true)
							   .encodeResourceToString(medicationRes));


			String code = null;
			CodeableConceptDt medCodeConcept =medicationRes.getCode();
			
			if (medCodeConcept != null)
			{
				code = ((medCodeConcept).getCodingFirstRep() != null) ? 
	  				    (medCodeConcept).getCodingFirstRep().getCode() : null;
			}
			
			String productForm = null;
			
			Product product = medicationRes.getProduct();
			
			if (product != null)
			{
				CodeableConceptDt productFormCodeableConcept = product.getForm();
				
				if (productFormCodeableConcept != null)
				{
					productForm = ((productFormCodeableConcept).getCodingFirstRep() != null) ? 
		  				    	   (productFormCodeableConcept).getCodingFirstRep().getCode() : null;
					
				}
			}
			
			String packageContainer = null;
			
			Medication.Package packageContainerObj = medicationRes.getPackage();
			
			if (packageContainerObj != null)
			{
				CodeableConceptDt packageContCodeableConcept = packageContainerObj.getContainer();
				
				if (packageContCodeableConcept != null)
				{
					packageContainer = ((packageContCodeableConcept).getCodingFirstRep() != null) ? 
	  				    	   (packageContCodeableConcept).getCodingFirstRep().getCode() : null;
				}
			}
			
			
			// Create MedicationDetails object to pass to UI
			if (code != null)
			{
				medDetails = new MedicationDetails();
				
				medDetails.setCode(code);
				medDetails.setProductForm(productForm);
				medDetails.setPackageContainer(packageContainer);
				
			}
			
		}
		
		return (medDetails);
		
	}
	
	
	
	
	

}
