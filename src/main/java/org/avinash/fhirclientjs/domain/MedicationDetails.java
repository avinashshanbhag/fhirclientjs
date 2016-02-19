/**
 * 
 */
package org.avinash.fhirclientjs.domain;

import java.io.Serializable;

/**
 * Contains medication data from <i>Medication</i> FHIR resource
 * 
 * @author ashanbhag
 *
 */
public class MedicationDetails implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String productForm; 
	private String packageContainer;
	

	/**
	 * CTOR
	 */
	public MedicationDetails() 
	{
		this.code = null;
		this.productForm = null;
		this.packageContainer = null;
	}


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}


	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}


	/**
	 * @return the productForm
	 */
	public String getProductForm() {
		return productForm;
	}


	/**
	 * @param productForm the productForm to set
	 */
	public void setProductForm(String productForm) {
		this.productForm = productForm;
	}


	/**
	 * @return the packageContainer
	 */
	public String getPackageContainer() {
		return packageContainer;
	}


	/**
	 * @param packageContainer the packageContainer to set
	 */
	public void setPackageContainer(String packageContainer) {
		this.packageContainer = packageContainer;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MedicationDetails [code=" + code + ", productForm="
				+ productForm + ", packageContainer=" + packageContainer + "]";
	}
	
	
	
	

}
