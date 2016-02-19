/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.MedicationDetails;
import org.avinash.fhirclientjs.domain.MedicationDispenseDetails;
import org.avinash.fhirclientjs.domain.MedicationOrderDetails;
import org.avinash.fhirclientjs.domain.MedicationStatementDetails;
import org.avinash.fhirclientjs.domain.MedicineInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.MedicationDispense;
import ca.uhn.fhir.model.dstu2.resource.MedicationOrder;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement;
import ca.uhn.fhir.model.dstu2.resource.MedicationStatement.Dosage;
import ca.uhn.fhir.model.primitive.BaseDateTimeDt;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.UriDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;

/**
 *  Main Class that handles queries to <i>MedicationStatement</i>,
 *  <i>MedicationOrder</i> and <i>MedicationDispense</i> FHIR resources.
 * 
 * @author ashanbhag
 *
 */
public class MedicineInfoProvider extends BaseProvider 
{
	private String thePatientId;
	
	private Map<String, MedicineInfo> medInfoMap;
	private Map<String, MedicationOrderDetails> medOrdersMap;
	private Map<String, MedicationDispenseDetails> medDispenseMap;
	private List<MedicationStatementDetails> medStatementDetails;
	
	
	/**
	 * Default CTOR
	 */
	public MedicineInfoProvider()
	{
		super();
		this.thePatientId = null;
		
		this.medInfoMap = null;
		this.medOrdersMap = null;
		this.medDispenseMap = null;
		this.medStatementDetails = null;
		
	}

	/**
	 * Real CTOR
	 * 
	 * @param myCtx	Instance of <i>FhirContext</i> class required to perform HAPI based query
	 * @param myFhirURL Based FHIR server URL
	 * @param myAccessToken Bearer token obtained from OAuth2 workflow
	 * @param myPatientId The single patient Id on which to query for medication information
	 * 
	 */
	public MedicineInfoProvider(FhirContext myCtx, String myFhirURL,
										 String myAccessToken, String myPatientId)
	 {
		
		super(myCtx, myFhirURL, myAccessToken);
		this.thePatientId = myPatientId;
		
		this.medInfoMap = null;
		this.medOrdersMap = null;
		this.medDispenseMap = null;
		this.medStatementDetails = null;
		
	 }
	
	/**
	 * Public API that queries <i>MedicationStatement</i>, <i>MedicationOrder</i> and
	 * <i>MedicationDispense</i> FHIR resources and builds <i>MedicineInfo</i> objects
	 * based on common medication "code". Used to display flattened list of all medication.
	 * 
	 * 
	 * @return List of <i>MedicineInfo</i> objects
	 */
	public List <MedicineInfo> buildMedicineInfo()
	{
		
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		//Get Medication Statement
		@SuppressWarnings("unchecked")
		List<MedicationStatement> medStatementLs =  (List<MedicationStatement>) queryResource(client, 
																				MedicationStatement.class,
																				this.thePatientId);
		
		// Get the information required for display
		this.medInfoMap = getMedicationStatementDataIntoMedInfo(medStatementLs);


		// Get MedicationOrder
		@SuppressWarnings("unchecked")
		List<MedicationOrder> medOrderLs =  (List<MedicationOrder>) queryResource(client, 
																				MedicationOrder.class,
																				this.thePatientId);
		
		// Get the information required for display
		this.medOrdersMap = getMedicationOrderData(medOrderLs);
		
		// Get Medication Dispense
		@SuppressWarnings("unchecked")
		List<MedicationDispense> medDispenseLs =  (List<MedicationDispense>) queryResource(client, 
																				MedicationDispense.class,
																				this.thePatientId);
		
		this.medDispenseMap = getMedicationDispenseData(medDispenseLs);
		

		List <MedicineInfo> medInfoListForDisplay = this.getMedicineInfoForDisplay();
		
		
		return (medInfoListForDisplay);
	}
	
	/**
	 * Builds a list of <i>MedicationOrderDetails</i> objects for display, that are 
	 * populated from <i>MedicationOrder</i> FHIR resource.
	 * 
	 * @return List of <i>MedicationOrderDetails</i> objects
	 */
	public List<MedicationOrderDetails> buildMedicationOrderDetails()
	{
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");
		

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();

		// Get MedicationOrder
		@SuppressWarnings("unchecked")
		List<MedicationOrder> medOrderLs =  (List<MedicationOrder>) queryResource(client, 
																				MedicationOrder.class,
																				this.thePatientId);
		
		// Get Map - key : Code, Value: MedicationOrderDetails
		this.medOrdersMap = getMedicationOrderData(medOrderLs);
		
		return (new ArrayList<MedicationOrderDetails>(this.medOrdersMap.values()));
	}
	
	/**
	 * Builds a list of <i>MedicationDispenseDetails</i> objects for display, that are 
	 * populated from <i>MedicationDispense</i> FHIR resource.
	 * 
	 * @return List of <i>MedicationDispenseDetails</i> objects
	 */
	public List<MedicationDispenseDetails> buildMedicationDispenseDetails()
	{
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();

		@SuppressWarnings("unchecked")
		List<MedicationDispense> medDispenseLs =  (List<MedicationDispense>) queryResource(client, 
																				MedicationDispense.class,
																				this.thePatientId);
		
		this.medDispenseMap = getMedicationDispenseData(medDispenseLs);

		return (new ArrayList<MedicationDispenseDetails>(this.medDispenseMap.values()));
	}	
	
	/**
	 * Builds a list of <i>MedicationStatementDetails</i> objects for display, that are 
	 * populated from <i>MedicationStatement</i> FHIR resource.
	 * 
	 * @return List of <i>MedicationStatementDetails</i> objects
	 */
	public List<MedicationStatementDetails> buildMedicationStatementDetails()
	{
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();

		//Get Medication Statement
		@SuppressWarnings("unchecked")
		List<MedicationStatement> medStatementLs =  (List<MedicationStatement>) queryResource(client, 
																				MedicationStatement.class,
																				this.thePatientId);
		
		// Get the information required for display
		this.medStatementDetails = getMedicationStatementData(medStatementLs);
		
		return (this.medStatementDetails);
	}	
	
	
	
////////////////All Private methods below ////////////////////////////////
	/**
	 * This method is used to query for all medication related FHIR resources, one at a time, for 
	 * single patient Id.
	 * 
	 * @param client 	<i>IGenericClient</i> interface that is used to make a HAPI Client API
	 * @param theClass One of <i>MedicationStatement</i>, <i>MedicationOrder</i> or <i>MedicationDispense</i> class
	 * 
	 * @return List of medication FHIR resources bqsed on the type passed in the "theClass" parameter.
	 */

	/*
	protected List<? extends IResource> queryResource(IGenericClient client, Class<? extends IResource> theClass)
	{
		
		List<? extends IResource> resLs = new ArrayList<IResource>();
		
		Bundle bundle = client.search()
				.forResource(theClass)
				.where(new StringClientParam("patient").matches().value(this.thePatientId) )
				.include(new Include("*"))
				.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
				.encodedJson()
				.execute();

		if (bundle != null)
		{
			resLs = bundle.getAllPopulatedChildElementsOfType(theClass); 
		}

		return (resLs);
	}
	*/
	
	/**
	 * Obtains map with Medication codes being "Key" and <i>MedicineIfo</i> value, from the list of <i>MedicationStatement</i> 
	 * resource
	 * 
	 * @param medStatementLs
	 * @return
	 */
	private Map<String, MedicineInfo> getMedicationStatementDataIntoMedInfo(List<MedicationStatement> medStatementLs)
	{
		Map<String, MedicineInfo> medMap = new HashMap<String, MedicineInfo>();
		
		if (medStatementLs != null & medStatementLs.size() > 0)
		{
			for (int i=0; i < medStatementLs.size(); i++)
			{
				
				MedicationStatement medStatement = medStatementLs.get(i);
				
				// For printing out the results..
				System.out.println("MedicationStatement Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(medStatement));
				
				
				// Need following information
				String name = null;		// medicine name
				String code = null;		// medicine code 
				String route = null; 	// how delivered
				String when = null; 	// When given
				String status  = null;	// whether completed, or still active.
				String wasNotTaken = null; // Was taken or not
				
				
				// Get name and code
				IDatatype medDt = medStatement.getMedication();
				
				if (medDt != null)
				{
					if (medDt instanceof CodeableConceptDt)
					{
						// get name and code if available
						
						name = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getDisplay() : null;

						code = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getCode() : null;
					}
				}
				
				// Get Route
				List<Dosage> dosageLs = medStatement.getDosage();
				
				// Medicine information - Route
				if (dosageLs != null && dosageLs.size() > 0)
				{
					Dosage d = dosageLs.get(0);

					CodeableConceptDt rt = d.getRoute();
					
					if (rt != null)
					{
						route = rt.getCodingFirstRep().getDisplay();
					}
				}
				
				// When 
				BaseDateTimeDt dt = (BaseDateTimeDt) medStatement.getEffective();
				if (dt != null)
					when = dt.getValueAsString();
				
				// Status
				status = medStatement.getStatus();
				
				// WasNotTaken
				wasNotTaken = (medStatement.getWasNotTaken()) ? "true" : "false"; 

				// Create the medicineInfo Object and add to it.
				MedicineInfo medObject = new MedicineInfo();
				medObject.setName(name);
				medObject.setCode(code);
				medObject.setRoute(route);
				medObject.setWhen(when);
				medObject.setCode(code);
				medObject.setWasNotTaken(wasNotTaken);
				medObject.setMedicationStatus(status);
				
				// Add to the HashMap if code exists.
				if (code !=null && !code.isEmpty())
				{
					medMap.put(code, medObject);
				}
			}
		}
		return (medMap);
	}
	
	private List<MedicationStatementDetails> getMedicationStatementData(List<MedicationStatement> medStatementLs)
	{

		List<MedicationStatementDetails> medStatementDetailsLs = new ArrayList<MedicationStatementDetails>();
		
		if (medStatementLs != null & medStatementLs.size() > 0)
		{
			for (int i=0; i < medStatementLs.size(); i++)
			{
				
				MedicationStatement medStatement = medStatementLs.get(i);
				
				// For printing out the results..
				System.out.println("MedicationStatement Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(medStatement));
				
				
				// Need following information
				String name = null;		// medicine name
				String code = null;		// medicine code 
				String route = null; 	// how delivered
				String when = null; 	// When given
				String status  = null;	// whether completed, or still active.
				String wasNotTaken = null; // Was taken or not
				
				
				// Get name and code
				IDatatype medDt = medStatement.getMedication();
				
				if (medDt != null)
				{
					if (medDt instanceof CodeableConceptDt)
					{
						// get name and code if available
						
						name = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getDisplay() : null;

						code = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getCode() : null;
					}
				}
				
				// Get Route
				List<Dosage> dosageLs = medStatement.getDosage();
				
				// Medicine information - Route
				if (dosageLs != null && dosageLs.size() > 0)
				{
					Dosage d = dosageLs.get(0);

					CodeableConceptDt rt = d.getRoute();
					
					if (rt != null)
					{
						route = rt.getCodingFirstRep().getDisplay();
					}
				}
				
				// When 
				BaseDateTimeDt dt = (BaseDateTimeDt) medStatement.getEffective();
				if (dt != null)
					when = dt.getValueAsString();
				
				// Status
				status = medStatement.getStatus();
				
				// WasNotTaken
				wasNotTaken = (medStatement.getWasNotTaken()) ? "true" : "false"; 

				// Create the medicineInfo Object and add to it.
				MedicationStatementDetails medStatementObject = new MedicationStatementDetails();
				medStatementObject.setName(name);
				medStatementObject.setCode(code);
				medStatementObject.setRoute(route);
				medStatementObject.setWhen(when);
				medStatementObject.setCode(code);
				medStatementObject.setWasNotTaken(wasNotTaken);
				medStatementObject.setStatus(status);
				
				// Add to the list
				medStatementDetailsLs.add(medStatementObject);
			}
		}
		return (medStatementDetailsLs);

	}


	private List <MedicationOrderDetails> getMedicationOrderDataForDisplay(List<MedicationOrder> medOrders)
	{
		List<MedicationOrderDetails> medOrderLs = new ArrayList<MedicationOrderDetails>();
		
		if (medOrders != null && medOrders.size() > 0)
		{
			for (int i = 0; i < medOrders.size(); i++)
			{
				MedicationOrder medSingleOrder = medOrders.get(i);
				
				// For printing out the results..
				System.out.println("MedicationOrder Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(medSingleOrder));
				
				String code = null;
				String status = null;
				String dosageInstruction = null;
				
				String displayName = null;
									
				// Get Name and Code
				IDatatype medDt = medSingleOrder.getMedication();
				
				if (medDt != null)
				{
					if (medDt instanceof CodeableConceptDt)
					{
						// get name and code if available
						displayName = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getDisplay() : null;
													
						code = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getCode() : null;
					}
				}
				
				// Get Order Status
				status = medSingleOrder.getStatus();
				
				// Get dosage Instruction
				dosageInstruction = (medSingleOrder.getDosageInstructionFirstRep() != null) ?
									medSingleOrder.getDosageInstructionFirstRep().getText() : null;
									
				if (code != null)
				{
					MedicationOrderDetails medOrderDetails = new MedicationOrderDetails();
					medOrderDetails.setCode(code);
					medOrderDetails.setStatus(status);
					medOrderDetails.setDosageInstruction(dosageInstruction);
					medOrderDetails.setDisplayName(displayName);
					
					medOrderLs.add(medOrderDetails);
					
				}
			}
		}

		return (medOrderLs);
	}
	
	
	private Map<String, MedicationOrderDetails> getMedicationOrderData(List<MedicationOrder> medOrders)
	{
		Map<String, MedicationOrderDetails> medOrderMap = new HashMap<String, MedicationOrderDetails>();
		
		if (medOrders != null && medOrders.size() > 0)
		{
			for (int i = 0; i < medOrders.size(); i++)
			{
				MedicationOrder medSingleOrder = medOrders.get(i);
				
				// For printing out the results..
				System.out.println("MedicationOrder Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(medSingleOrder));
				
				String code = null;
				String status = null;
				String dosageInstruction = null;
				
				String displayName = null;
									
				// Get Name and Code
				IDatatype medDt = medSingleOrder.getMedication();
				
				
				if (medDt != null)
				{
					if (medDt instanceof CodeableConceptDt)
					{
						// get name and code if available
						displayName = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getDisplay() : null;
													
						code = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getCode() : null;
					}
					else if (medDt instanceof ResourceReferenceDt)
					{
						// if there is reference to Medication resource, then 
						// use it obtain the code.
						// Display Name is provided at the <medicationReference> element level
						//System.out.println("Class type:" + medDt.getClass());
						
						// Display Name
						displayName = (((ResourceReferenceDt) medDt).getDisplay() != null) ?
									   ((ResourceReferenceDt) medDt).getDisplay().getValue() : null;
						
						
						// Get Code
						UriDt refVal = ((ResourceReferenceDt) medDt).getReference();
						
						if (refVal != null && 
							(refVal.getValue() != null && refVal.getValue().length() != 0))
						{
							System.out.println("Reference value:" + refVal.getValue());

							// Get Medication Resource
							MedicationDetailsProvider medDetailsProv = new MedicationDetailsProvider(
																			this.getTheCtx(), 
																			this.getTheFhirURL(), 
																			this.getTheAccessToken(),
																			refVal.getValue());
							
							if (medDetailsProv != null)
							{
								MedicationDetails medDetails = medDetailsProv.buildMedicationDetails();
								
								if (medDetails != null)
								{
									System.out.println("Med Details" + medDetails.toString());
									code = medDetails.getCode();
								}
							}
						}
						
					}
				}
				
				// Get Order Status
				status = medSingleOrder.getStatus();
				
				// Get dosage Instruction
				dosageInstruction = (medSingleOrder.getDosageInstructionFirstRep() != null) ?
									medSingleOrder.getDosageInstructionFirstRep().getText() : null;
									
				if (code != null)
				{
					MedicationOrderDetails medOrderDetails = new MedicationOrderDetails();
					medOrderDetails.setCode(code);
					medOrderDetails.setStatus(status);
					medOrderDetails.setDosageInstruction(dosageInstruction);
					medOrderDetails.setDisplayName(displayName);
					
					// Put the object into map with key: code
					medOrderMap.put(code, medOrderDetails);
				}
			}
		}

		return (medOrderMap);
	}
	
	

	private Map<String, MedicationDispenseDetails> getMedicationDispenseData(List<MedicationDispense> medDispenseLs)
	{
		
		Map<String, MedicationDispenseDetails> medDispenserMap = new HashMap<String, MedicationDispenseDetails>();

		if (medDispenseLs != null && medDispenseLs.size() > 0)
		{
			for (int i = 0; i < medDispenseLs.size(); i++)
			{
				MedicationDispense medDispenseData = medDispenseLs.get(i);
				
				// For printing out the results..
				System.out.println("MedicationDispense Resource[" + i + "]=");
				
				System.out.println(this.getTheCtx()
						.newXmlParser()
						.setPrettyPrint(true)
						.encodeResourceToString(medDispenseData));
				
				String displayName = null;
				String code = null;
				String whenHandedOver = null;
				String dispenseStatus = null;
				
				
				// Get Name and Code
				IDatatype medDt = medDispenseData.getMedication();
				
				if (medDt != null)
				{
					if (medDt instanceof CodeableConceptDt)
					{
						// get name and code if available
						
						displayName = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getDisplay() : null;
													
						code = (((CodeableConceptDt) medDt).getCodingFirstRep() != null) ? 
								((CodeableConceptDt) medDt).getCodingFirstRep().getCode() : null;
					}
				}
				
				// MedicationDispense.whenHandedOver
				whenHandedOver = (medDispenseData.getWhenHandedOver() != null) ?
								 medDispenseData.getWhenHandedOver().toString() : null;
								 
				// MedicationDispense.status
				dispenseStatus = medDispenseData.getStatus();
								 
				if (code != null)
				{
					MedicationDispenseDetails medDispenseObj = new MedicationDispenseDetails();
					
					medDispenseObj.setCode(code);
					medDispenseObj.setDisplayName(displayName);
					medDispenseObj.setStatus(dispenseStatus);
					medDispenseObj.setWhenHandedOver(whenHandedOver);
					
					System.out.println("MedicationDispense Details"+ medDispenseObj.toString());
					
					// Put the object into map with key: code
					medDispenserMap.put(code, medDispenseObj);
				}
			}
		}
		return (medDispenserMap);
	}
	
	
	
	private List<MedicineInfo> getMedicineInfoForDisplay()
	{

		// Step 1: Iterate thru MedicationStatement and add all the MedicineInfo with 
		//          details from MedicationOrderDetails and MedicationDispenseDetails
		Map<String, MedicineInfo>medInfoObjMap = UpdateExistingMedicineInfoObjects(this.medInfoMap,
				this.medOrdersMap, this.medDispenseMap);
		
		
		// Add new MedicationOrders to the MedicineInfo Map
		addNewMedicationOrdersToMedicineInfoMap(this.medOrdersMap, medInfoObjMap);
		
		// Step 3: Add new MedicationDispenseDetails
		addNewMedicationDispenseDetailsToMedicineInfoMap(this.medDispenseMap,
				medInfoObjMap);
		
		return (new ArrayList<MedicineInfo>(medInfoObjMap.values()));
		
	}
	
	/**
	 * This method takes two <i>Map</i> which contain information from <i>MedicationStatement</i> and 
	 * <i>MedicationOrder</i> FHIR resources, with common key, and then, builds
	 * a single <i>MedicineInfo</i> object for the purposes of display.
	 * 
	 * @param medInfoMap	Contains <i>MedicineInfo</i> object from <i>MedicationStatement</i> resource.
	 * @param medOrderMap   Contains <i>MedicationOrderDetails</i> object from <i>MedicationOrder</i> resource.
	 * @param medDispenseMap   Contains <i>MedicationDispenseDetails</i> object from <i>MedicationDispense</i> resource.
	 * 
	 * @return Array list of <i>MedicineInfo</i> objects
	 */

	private Map<String, MedicineInfo> UpdateExistingMedicineInfoObjects(
			Map<String, MedicineInfo> medInfoMap,
			Map<String, MedicationOrderDetails> medOrderMap,
			Map<String, MedicationDispenseDetails> medDispenseMap
			) 
	{

		Map<String, MedicineInfo> newMedInfoMap = new HashMap<String, MedicineInfo>();
		
		for (Entry<String, MedicineInfo> entry : medInfoMap.entrySet()) 
		{
		    String key = entry.getKey();
		    
		    MedicineInfo value = (MedicineInfo) entry.getValue();

		    if (value == null)
		    	break;
		    
		    // Find MedicationOrderDetail from the key
		    MedicationOrderDetails medOrder = (MedicationOrderDetails) medOrderMap.get(key);
		    // Get MedicationOrder information into MedicineInfo
		    if (medOrder != null)
		    {
		    	// Order Status
		    	String orderStatus = medOrder.getStatus();
		    	value.setOrderStatus(orderStatus);
		    	
		    	// Dosage Instruction
		    	String dosageInstruction = medOrder.getDosageInstruction();
		    	value.setDosage(dosageInstruction);
		    	
		    }
		    
		    // Find MedicationDispenseDetail from the key
		    MedicationDispenseDetails medDispense = (MedicationDispenseDetails) medDispenseMap.get(key);
		    
		    if (medDispense != null)
		    {
		    	String whenHandedOver = medDispense.getWhenHandedOver();
		    	String dispenseStatus = medDispense.getStatus();
		    
		    	// Add to the MedicineInfo Object
		    	value.setWhenHandedOver(whenHandedOver);
		    	value.setDispenseStatus(dispenseStatus);
		    }

		    // Add to the list
		    if (key != null && !key.isEmpty() &&
		    	value != null)
		    {
		    	newMedInfoMap.put(key, value);
		    }
		}
		
		return (newMedInfoMap);
	}

	private void addNewMedicationOrdersToMedicineInfoMap(
			Map<String, MedicationOrderDetails> medOrderMap,
			Map<String, MedicineInfo> medInfoObjMap) 
	{
		// Step 2: Add any remaining MedicationOrder resources that are not in the existing map
		for (Entry<String, MedicationOrderDetails> medOrderDetailsEntry : medOrderMap.entrySet()) 
		{
			String key = medOrderDetailsEntry.getKey();
			MedicationOrderDetails medOrderR = medOrderMap.get(key);
			
			if (medOrderR == null)
				return;

			String orderStatus = medOrderR.getStatus();
			String dosageInstruction = medOrderR.getDosageInstruction();
			String displayName = medOrderR.getDisplayName();
	
			MedicineInfo medObj = null;
			
			
			// If the key is not in the Map, then create it and add to the map
			if (medInfoObjMap.isEmpty() || !medInfoObjMap.containsKey(key))
			{
				// Create new MedicineInfo Object
				medObj = new MedicineInfo();
			}
			else
			{
				// get existing object
				medObj = medInfoObjMap.get(key);
			}
			medObj.setCode(key);
			medObj.setDosage(dosageInstruction);
			medObj.setOrderStatus(orderStatus);
			medObj.setName(displayName);
					
			// Add to the map
			medInfoObjMap.put(key, medObj);
			
		}
		
	}
	
	private void addNewMedicationDispenseDetailsToMedicineInfoMap(
			Map<String, MedicationDispenseDetails> medDispenseMap,
			Map<String, MedicineInfo> medInfoObjMap) 
	{
		
		for (Entry<String, MedicationDispenseDetails> medDispenseDetailsEntry : medDispenseMap.entrySet()) 
		{
			String key = medDispenseDetailsEntry.getKey();
			MedicationDispenseDetails medDispR = medDispenseMap.get(key);
			
			if (medDispR == null)
				return;
			
			System.out.println("MedicationDispenseDetails:" + medDispR.toString() );
			
			// Get data
			String displayName = medDispR.getDisplayName();
			String dispenseStatus = medDispR.getStatus();
			String whenHandedOver = medDispR.getWhenHandedOver();
			
			// If the key is not in the Map, then create it and add to the map
			MedicineInfo medObj = null;
			if (medInfoObjMap.isEmpty() || !medInfoObjMap.containsKey(key))
			{
				medObj = new MedicineInfo();
			}
			else
			{
				medObj = medInfoObjMap.get(key);
			}
			
			medObj.setCode(key);
			medObj.setName(displayName);
			medObj.setDispenseStatus(dispenseStatus);
			medObj.setWhenHandedOver(whenHandedOver);
					
			// Add to the map
			medInfoObjMap.put(key, medObj);
		}
	}
	
	
}
