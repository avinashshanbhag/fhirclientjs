/**
 * 
 */
package org.avinash.fhirclientjs.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.domain.DocumentReferenceInfo;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.composite.AttachmentDt;
import ca.uhn.fhir.model.dstu2.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.DocumentReference;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.gclient.StringClientParam;

/**
 * Main Class that handles queries to <i>DocumentReference</i> FHIR resource.
 * 
 * 
 * @author ashanbhag
 *
 */
public class DocumentReferenceInfoProvider extends BaseProvider
{
	private String thePatientId; 	// Single Patient ID required

	
	/**
	 * Default CTOR
	 */
	public DocumentReferenceInfoProvider()
	{
		super();
		this.thePatientId = null;
	}
	
	/**
	 * Business CTOR
	 * @param myCtx
	 * @param myFhirURL
	 * @param myAccessToken
	 * @param myPatientId
	 */
	public DocumentReferenceInfoProvider(FhirContext myCtx, String myFhirURL,
										 String myAccessToken, String myPatientId)
	{
		
		super(myCtx, myFhirURL, myAccessToken);
		this.thePatientId = myPatientId;
	
	}

	/**
	 * This method queries the FHIR service and returns <i>List</i> of <i>DocumentReferenceInfo</i> objects
	 * for a single patient 
	 * 
	 * @return List of <i>DocumentReferenceInfo</i> objects 
	 */
	
	public List<DocumentReferenceInfo> buildDocumentReferenceInfo()
	{
		Validate.notNull(this.getTheCtx(), "Fhir CTX cannot be null!");
		Validate.notEmpty(this.getTheFhirURL(), "FHIR Base URL cannot be null or empty!");
		Validate.notEmpty(this.thePatientId, "Patient Id cannot be null or empty");

		// Connect to HAPI Client API - need to make Rest Call
		IGenericClient client = getHapiClient();
		
		// Query for DocumentReference
		//List<DocumentReference> docRefLs = this.queryResource(client);
		
		@SuppressWarnings("unchecked")
		List<DocumentReference> docRefLs = (List<DocumentReference>) this.queryResource(client,
															  DocumentReference.class,
															  this.thePatientId);
		
		// Get patient info for details
		List <DocumentReferenceInfo> docRefs = getDocumentReferenceInfoForDisplay(docRefLs);
		
		return (docRefs);
		
	}
	
	
////////////////All Private methods below ////////////////////////////////
	/*
	protected List<DocumentReference> queryResource(IGenericClient client)
	{
		
		List<DocumentReference> docRefLs = new ArrayList<DocumentReference>();
		
		Bundle bundle = client.search()
				.forResource(DocumentReference.class)
				.where(new StringClientParam("patient").matches().value(this.thePatientId) )
				.include(new Include("*"))
				.returnBundle(ca.uhn.fhir.model.dstu2.resource.Bundle.class)
				.encodedJson()
				.execute();

		if (bundle != null)
		{
			docRefLs = bundle.getAllPopulatedChildElementsOfType(DocumentReference.class); 
		}
		
		return (docRefLs);
	}
	*/
	
	/**
	 * Takes List of <i>DocumentReference</i> objects which are raw results 
	 * from FHIR query and returns List of <i>DocumentReferenceInfo</i> objects which are used for display
	 * 
	 * @param docRefLs List of <i>DocumentReference</i> objects from the query of FHIR server
	 * 
	 * @return list of <i>DocumentReferenceInfo</i> objects used for display
	 * 
	 */
	private List<DocumentReferenceInfo> getDocumentReferenceInfoForDisplay(List<DocumentReference> docRefLs)
	{
		List <DocumentReferenceInfo> docRefInfoLs = new ArrayList<DocumentReferenceInfo>();
		
		if (docRefLs != null && docRefLs.size() > 0 )
		{
			
			for (int i=0; i < docRefLs.size(); i++)
			{
				DocumentReference docRef = docRefLs.get(i);
				
				// For each patient, fill PatientInfo Details
				DocumentReferenceInfo docRefInfo = getDocumentReferenceInfoForDisplay(docRef);
				
				if (docRefInfo != null)
				{
					docRefInfoLs.add(docRefInfo);
				}
			}
		}
		return (docRefInfoLs);
	}
	
	/**
	 * Parse <i>DocumentReference</i> object and fill <i>DocumentRefernceInfo</i> object used for display
	 * 
	 * @param docRef
	 * @return
	 */
	
	private DocumentReferenceInfo getDocumentReferenceInfoForDisplay(DocumentReference docRef)
	{
		
		if (docRef == null || docRef.isEmpty())
			return null;
		
		// Create DocumentRefernce Object.
		DocumentReferenceInfo docRefInfo = new DocumentReferenceInfo();

		
		// Author
		List <ResourceReferenceDt> authls  = docRef.getAuthor();
		
		String author = null;
		if (authls != null & authls.size() > 0)
		{
			author = authls.get(0).getDisplayElement().getValue();
		}
		
		String creationDate = docRef.getCreated().toString();
		
		String status = docRef.getStatus();
		
		String typeCode = docRef.getType().getText();
		
		String classCode = docRef.getClassElement().getText();

		List <DocumentReference.Content> contents = docRef.getContent();
		
		String url = null;
		String contentType = null;
		String language = null;
		String title = null;
		
		// For now, pick the attached document. Later enhance to get collection
		if (contents != null && contents.size() > 0)
		{
			DocumentReference.Content content = contents.get(0);
			AttachmentDt attachment = content.getAttachment();
			
			if (attachment != null)
			{
				url = attachment.getUrl();
				contentType = attachment.getContentType();
				language = attachment.getLanguage();
				title = attachment.getTitle();
			}
		}
		
		// Set all the values into DocumentReferenceInfo Object
		docRefInfo.setAuthor(author);
		docRefInfo.setContentType(contentType);
		docRefInfo.setCreationDate(creationDate);
		docRefInfo.setURL(url);
		docRefInfo.setStatus(status);
		docRefInfo.setLanguage(language);
		docRefInfo.setTitle(title);
		docRefInfo.setClassCode(classCode);
		docRefInfo.setTypeCode(typeCode);
		
		
		return (docRefInfo);
	}
	
	
}
