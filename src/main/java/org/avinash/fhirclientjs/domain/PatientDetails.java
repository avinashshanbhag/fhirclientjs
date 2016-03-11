/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;

/**
 * @author ashanbhag
 *
 */
public class PatientDetails implements Serializable {



	private static final long serialVersionUID = 1L;
	
	private PatientInfo demographics;
	
	private List<MedicineInfo> medicines;

	private List<AllergyInfo> allergies;
	
	private List<ProblemInfo> problems;
	
	private List<DiagnosticReportInfo> diagnosticReports;
	
	private List<DocumentReferenceInfo> documentReferences;
	
	private List<MedicationOrderDetails> medOrderDetails;
	
	private List<MedicationDispenseDetails> medDispenseDetails;
	
	private List<MedicationStatementDetails> medStatementDetails;
	
	
	
	public PatientDetails() 
	{
		super();
		
		this.medicines = new ArrayList<MedicineInfo>();
		this.allergies = new ArrayList<AllergyInfo>();
		this.problems  = new ArrayList<ProblemInfo>();
		this.documentReferences = new ArrayList<DocumentReferenceInfo>();
		
		this.diagnosticReports = new ArrayList<DiagnosticReportInfo>();
		
		this.medOrderDetails = new ArrayList<MedicationOrderDetails>();
		this.medDispenseDetails = new ArrayList<MedicationDispenseDetails>();
		this.medStatementDetails = new ArrayList<MedicationStatementDetails>();
		
		this.demographics = null;
	}

	
	/**
	 * @return the diagnosticReports
	 */
	public List<DiagnosticReportInfo> getDiagnosticReports() {
		return diagnosticReports;
	}


	/**
	 * @param diagnosticReports the diagnosticReports to set
	 */
	public void setDiagnosticReports(List<DiagnosticReportInfo> diagnosticReports) {
		this.diagnosticReports = diagnosticReports;
	}

	public void addDiagnosticReport(DiagnosticReportInfo singleDiagnosticReportInfo)
	{
		this.diagnosticReports.add(singleDiagnosticReportInfo);
	}


	/**
	 * @return the demographics
	 */
	public PatientInfo getDemographics() {
		return demographics;
	}
	/**
	 * @param demographics the demographics to set
	 */
	public void setDemographics(PatientInfo demographics) {
		this.demographics = demographics;
	}
	/**
	 * @return the medicines
	 */
	public List<MedicineInfo> getMedicines() {
		return medicines;
	}
	/**
	 * @param medicines the medicines to set
	 */
	public void setMedicines(List<MedicineInfo> medicines) {
		this.medicines = medicines;
	}
	
	public void addMedicine(MedicineInfo med)
	{
		this.medicines.add(med);
		
	}

	/**
	 * @return List of <i>MedicationStatementDetails</i> objects
	 */
	public List<MedicationStatementDetails> getMedStatementDetails() {
		return this.medStatementDetails;
	}
	
	/**
	 * @param  medStatementDetailsLs the list <i>MedicationStatementDetails</i> to set
	 */
	
	public void setMedStatementDetails(List<MedicationStatementDetails> medStatementDetailsLs) {
		this.medStatementDetails = medStatementDetailsLs;
	}
	
	public void addMedStatementDetails(MedicationStatementDetails medStatement)
	{
		this.medStatementDetails.add(medStatement);
	}	
	
	/**
	 * @return List of <i>MedicationOrderDetails</i> objects
	 */
	public List<MedicationOrderDetails> getMedOrderDetails() {
		return this.medOrderDetails;
	}
	
	/**
	 * @param  medStatementDetailsLs the list <i>MedicationStatementDetails</i> to set
	 */
	
	public void setMedOrderDetails(List<MedicationOrderDetails> medOrderDetailsLs) {
		this.medOrderDetails = medOrderDetailsLs;
	}
	
	public void addMedOrderDetails(MedicationOrderDetails medOrder)
	{
		this.medOrderDetails.add(medOrder);
	}	

	/**
	 * @return List of <i>MedicationDispenseDetails</i> objects
	 */
	public List<MedicationDispenseDetails> getMedDispenseDetails() {
		return this.medDispenseDetails;
	}
	
	/**
	 * @param  medStatementDetailsLs the list <i>MedicationStatementDetails</i> to set
	 */
	
	public void setMedDispenseDetails(List<MedicationDispenseDetails> medDispenseDetailsLs) {
		this.medDispenseDetails = medDispenseDetailsLs;
	}
	
	public void addMedDispenseDetails(MedicationDispenseDetails medDispense)
	{
		this.medDispenseDetails.add(medDispense);
	}		
	
	
	/**
	 * @return the allergies
	 */
	public List<AllergyInfo> getAllergies() {
		return allergies;
	}


	/**
	 * @param allergies the allergies to set
	 */
	public void setAllergies(List<AllergyInfo> allergies) {
		this.allergies = allergies;
	}
	
	public void addAllergy(AllergyInfo allergy)
	{
		this.allergies.add(allergy);
		
	}
	
	/**
	 * @return the problems
	 */
	public List<ProblemInfo> getProblems() {
		return problems;
	}


	/**
	 * @param problems the problems to set
	 */
	public void setProblems(List<ProblemInfo> problems) {
		this.problems = problems;
	}

	/**
	 * Add single problem
	 */
	public void addProblem(ProblemInfo prob)
	{
		this.problems.add(prob);
		
	}
	
	
	
	/**
	 * @return the documentReferences
	 */
	public List<DocumentReferenceInfo> getDocumentReferences() {
		return documentReferences;
	}


	/**
	 * @param documentReferences the documentReferences to set
	 */
	public void setDocumentReferences(List<DocumentReferenceInfo> documentReferences) {
		this.documentReferences = documentReferences;
	}


	/**
	 * Returns String representation of Object
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		
		buf.append("Patient Demographics: ");
		buf.append("\n");
		buf.append(this.demographics.toString());
		buf.append("\n");
		buf.append("Medications:");
		buf.append("\n");
		
		// MedicineInfo object
		for (MedicineInfo med : this.medicines) 
		{
			buf.append(med.toString());
			buf.append("\n");
		}		
		
		for (MedicationStatementDetails medStatement : this.medStatementDetails) 
		{
			buf.append(medStatement.toString());
			buf.append("\n");
		}		
		
		for (MedicationOrderDetails medOrder : this.medOrderDetails) 
		{
			buf.append(medOrder.toString());
			buf.append("\n");
		}		
		
		for (MedicationDispenseDetails medDispense : this.medDispenseDetails) 
		{
			buf.append(medDispense.toString());
			buf.append("\n");
		}		
		
		
		for (AllergyInfo allergy : this.allergies) 
		{
			buf.append(allergy.toString());
			buf.append("\n");
		}		
		
		for (ProblemInfo prob : this.problems) 
		{
			buf.append(prob.toString());
			buf.append("\n");
		}		
		
		for (DocumentReferenceInfo docRefInfo : this.documentReferences) 
		{
			buf.append(docRefInfo.toString());
			buf.append("\n");
		}		

		for (DiagnosticReportInfo diagReportInfo : this.diagnosticReports) 
		{
			buf.append(diagReportInfo.toString());
			buf.append("\n");
		}		
		
		
		return (buf.toString());
	}


}
