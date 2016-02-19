/**
 * Controller class to handle "launch" and "redirect" steps of oAuth workflow
 * Author: Avinash Shanbhag
 * Created:  Sept 10, 2015
 */


package org.avinash.fhirclientjs.authorization.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.avinash.fhirclientjs.authorization.access.AccessToken;
import org.avinash.fhirclientjs.authorization.access.AccessTokenProvider;
import org.avinash.fhirclientjs.domain.FhirQueryBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ca.uhn.fhir.context.FhirContext;


@Controller
@Scope("session")
public class AuthorizationController {

	// Required to initialized HAPI service
	private FhirContext ctx = null;
	
	public static final String REDIRECT_ALL_PATIENT_PAGE = "redirect:/all";
	public static final String REDIREC_SINGLE_PATIENT_PAGE = "redirect:/details";
	
	
	
	// CTOR
	public AuthorizationController()
	{
		ctx = FhirContext.forDstu2();
		System.out.println("[Authorization Controller] Initialized new FhirContext()");
	}

	public FhirContext getCtx() {
		return ctx;
	}

	public void setCtx(FhirContext ctx) {
		this.ctx = ctx;
	}
	
	/**
	 * This method checks if the <i>FhirQueryBean</i> from the UI has patient ID. If so, 
	 * then, the redirect has to go to Single page. Otherwise, the redirect is to the ALL Page
	 * 
	 * @param fhirBean
	 * @return String representation of redirect page
	 */
	private String getRedirectPage(FhirQueryBean fhirBean)
	{
		String retStr = AuthorizationController.REDIRECT_ALL_PATIENT_PAGE;

		if (fhirBean != null)
		{
			if (fhirBean.getId() != null && fhirBean.getId().length() > 0)
			{
				retStr = AuthorizationController.REDIREC_SINGLE_PATIENT_PAGE;
			}
		}
		
		return (retStr);
	}
	
	/**
	 * This method updates the <i>FhirBean.id</id> field with the "patient" id from
	 * <i>AccessToken</i> if available 
	 * @param accessToken <i>AccessToken</i> object returned from doing POST on Token URL
	 * @param fhirBeanFromSession <i>FhirQueryBean</i> object from Session.
	 * 
	 */
	private FhirQueryBean updateFhirBeanWithPatientID(AccessToken accessToken, FhirQueryBean fhirBeanFromSession)
	{
		
		
		
		
		
		
		return null;
		
		
	}



	/**
	 * 
	 * @param request
	 * @param response
	 * @param code
	 * @param state
	 * @return
	 */
	
	@RequestMapping(value = "/avifhirclient/oauth", method = RequestMethod.GET)
	public String auth(HttpServletRequest request,
					HttpServletResponse response, 
					@RequestParam("code") String code, 
					@RequestParam("state") String state)
	{
		
		System.out.println("Inside [auth] method!");

		// Check that we have valid auth code
		System.out.println("[auth]Inside auth method with request params " + 
							" code = " + code + " and state = " + state);
		
		
		// Obtain FhirBean from the session
		FhirQueryBean fhirBeanSession = (FhirQueryBean) request.getSession().getAttribute("FhirBeanFromLogin");
		System.out.println("Fhir URL (from Session): " + fhirBeanSession.getUrl());


		// Obtain Token URL from the Session. Needed to make a call
		// to TokenURL to get Access token
		String tokenURL = (String) request.getSession().getAttribute("TokenURL");
		System.out.println("Token URL (from Session): " + tokenURL);
		
		// Obtain Access Token
		AccessTokenProvider accessTokenProv = new AccessTokenProvider(this.ctx, 
																	  fhirBeanSession.getUrl(),
																	  code, 
																	  "authorization_code",
																	  tokenURL);

		
		AccessToken accessToken = accessTokenProv.getAccessToken();
		
		if (accessToken != null)
		{
			System.out.println("Access Token: " + accessToken.toString());

			// Check if Patient ID was obtained from the Access Token. If so, 
			// add it to the FhirBean that will be put in session.
			
			if (accessToken.getPatient() != null && accessToken.getPatient().length() > 0)
			{
				String patientId = accessToken.getPatient();

				// Add the patient Id to the FhirBean
				fhirBeanSession.setId(patientId);
				
				// Update the Session 
				request.getSession().setAttribute("FhirBeanFromLogin", fhirBeanSession);
				
			}
			
			// add access token to the Session so it can be used for query
			request.getSession().setAttribute("AccessTokenObject", accessToken);
		}

		// redirect to ALL or single page
		return (getRedirectPage(fhirBeanSession));
	}

}
