/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.MedicationDetails;
import org.avinash.fhirclientjs.domain.ObservationInfo;
import org.avinash.fhirclientjs.domain.ObservationInfo.ObservationType;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.composite.QuantityDt;
import ca.uhn.fhir.model.dstu2.resource.Observation;
import ca.uhn.fhir.rest.client.IGenericClient;

/**
 * 
 * @author ashanbhag
 * 
 * This class queries <i>Observation</i> FHIR resource based on Reference URL in
 * <i>DiagnosticReport</i> FHIR resource and obtains data for display.
 * 
 *
 */
public class ObservationInfoProvider extends BaseProvider {
	
	private String theObsReferenceURL;
	
	
	/**
	 * Defult CTOR
	 *  
	 */
	public ObservationInfoProvider() {
		// TODO Auto-generated constructor stub
		super();
		this.theObsReferenceURL = null;
		
	}

	/**
	 * Business CTOR used in practice.
	 * 
	 * @param myCtx			The FHIR Context
	 * @param myFhirURL		The base FHIR URL
	 * @param myAccessToken	The bearer token from OAuth2
	 * 
	 * 
	 */
	public ObservationInfoProvider(FhirContext myCtx, String myFhirURL,
			String myAccessToken, String myObsReferenceURL) 
	{
		super(myCtx, myFhirURL, myAccessToken);
		this.theObsReferenceURL = myObsReferenceURL;
	}
	
	/**
	 * Main public API to get <i>ObservationInfo</i>
	 * 
	 * @return instance of <i>ObservationInfo</i> FHIR resource
	 */
	public ObservationInfo buildObservationInfo()
	{
		ObservationInfo obsInfo = null;
		
		// Step 1: Get the HAPI Client 
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.theObsReferenceURL, "Observation reference URL cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		Observation obsRes = (Observation) queryResourceByURL(client, 
												Observation.class, 
												this.theObsReferenceURL);
		
		
		if (obsRes != null)
		{
			obsInfo = getObservationInfoForDisplay(obsRes);
		}
		
		return (obsInfo);
		
	}

///////////////////////////////////// ALL PRIVATE METHODS BELOW ///////////////////////
	
	private ObservationInfo getObservationInfoForDisplay(Observation obsRes)
	{
		ObservationInfo obsInfo = null;
		
		if (obsRes != null)
		{
			// First dump the string
			System.out.println(this.getTheCtx()
							   .newXmlParser()
							   .setPrettyPrint(true)
							   .encodeResourceToString(obsRes));
			
			// Observation Category
			String categoryCodeValue = null;
			String categoryCodeSystem = null;
			String categoryDisplayValue = null;
			
			if (obsRes.getCategory() != null && 
				obsRes.getCategory() instanceof CodeableConceptDt)
			{
				CodingDt codeDt = obsRes.getCategory().getCodingFirstRep();
				
				// code
				categoryCodeValue = (codeDt != null) ?
						            (codeDt.getCode()) : null;
						
				// code system
				categoryCodeSystem = (codeDt != null) ?
									 (codeDt.getSystem()) : null;
									 
				// code display
				categoryDisplayValue = (codeDt != null) ?
									   (codeDt.getDisplay()) : null;
									   
			}
			
			String obsCodeValue = null;
			String obsCodeSystem = null;
			String obsCodeDisplayValue = null;
			
			if (obsRes.getCode() != null)
			{
				CodingDt codeData = obsRes.getCode().getCodingFirstRep();
				
				obsCodeValue = (codeData.getCode() != null) ?
							   (codeData.getCode()) : null;
							   
				obsCodeSystem = (codeData.getSystem() != null) ?
							    (codeData.getSystem()) : null;
				
				obsCodeDisplayValue = (codeData.getDisplay() != null) ?
									  (codeData.getDisplay()) : null;
									  
			}
			
			String valCodeSystem = null;
			String valCodeValue = null;
			String valCodeDisplayValue= null;
			
			double value = 0.0;
			String unit = null;
			String unitCodeSystem = null;
			
			ObservationType type = null;
					

			if (obsRes.getValue() != null)
			{
				
				// If observation is codeable concept
				if (obsRes.getValue() instanceof CodeableConceptDt)
				{
					CodeableConceptDt valueConcept = (CodeableConceptDt) obsRes.getValue();
					
					valCodeValue = (valueConcept.getCodingFirstRep() != null) ?
								    (valueConcept.getCodingFirstRep().getCode()) : null;
								    
					valCodeSystem = (valueConcept.getCodingFirstRep() != null) ?
									  (valueConcept.getCodingFirstRep().getSystem()): null;
									  
					valCodeDisplayValue = (valueConcept.getCodingFirstRep() != null) ?
										  (valueConcept.getCodingFirstRep().getDisplay()) : null;
					
					type = ObservationType.CODEABLECONCEPT;
										  
				}
				else if (obsRes.getValue() instanceof QuantityDt)
				{
					QuantityDt valDt = (QuantityDt) obsRes.getValue();
					
					value =  (valDt.getValue() != null) ?
							 (valDt.getValue().doubleValue()) : 0.0;
							 
					unit = (valDt.getUnit() != null) ?
						   (valDt.getUnit()) : null;
					
					unitCodeSystem = (valDt.getSystem() != null) ?
									 (valDt.getSystem()) : null;
									 
					type = ObservationType.QUANTITY;
				}
	
			}
			
			// Now create the object and add it.
			obsInfo = new ObservationInfo();
			
			obsInfo.setType(type);
			
			obsInfo.setCategoryCodeSystem(categoryCodeSystem);
			obsInfo.setCategoryCodeValue(categoryCodeValue);
			obsInfo.setCategoryCodeValue(categoryCodeValue);
			
			obsInfo.setObsCodeSystem(obsCodeSystem);
			obsInfo.setObsCodeValue(obsCodeValue);
			obsInfo.setObsCodeDisplayValue(obsCodeDisplayValue);
			
			obsInfo.setValCodeSystem(valCodeSystem);
			obsInfo.setValCodeValue(valCodeValue);
			obsInfo.setValCodeDisplayValue(valCodeDisplayValue);
			
			obsInfo.setValue(value);
			obsInfo.setUnit(unit);
			obsInfo.setUnitCodeSystem(unitCodeSystem);
			
		}
		return (obsInfo);
	}

}
