/**
 * Controller Class for Rest API calls from FHIR Client 
 */
package org.avinash.fhirclientjs.api;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.Validate;
import org.avinash.fhirclientjs.authorization.access.AccessToken;
import org.avinash.fhirclientjs.authorization.access.AccessTokenProvider;
import org.avinash.fhirclientjs.authorization.authorizationcode.AuthorizationCodeRequest;
import org.avinash.fhirclientjs.authorization.authorizationcode.AuthorizationCoderRequestBuilder;
import org.avinash.fhirclientjs.dao.DocumentProvider;
import org.avinash.fhirclientjs.dao.FhirDAO;
import org.avinash.fhirclientjs.domain.FhirQueryBean;
import org.avinash.fhirclientjs.util.FhirAuthorizationCodeUtils;
import org.avinash.fhirclientjs.util.StateProvider;
import org.avinash.fhirclientjs.util.TextUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import ca.uhn.fhir.context.FhirContext;


/**
 * @author ashanbhag
 *
 */

//@Controller
@RestController
//@Scope("session")
public class FhirQueryController {

	// Required to initialized HAPI service
	private FhirContext ctx = null;
	
	// CTOR
	public FhirQueryController()
	{
		// Do it once because expensive!
		ctx = FhirContext.forDstu2();
		System.out.println("Initialized new FhirContext()");
		
	}

	public FhirContext getCtx() {
		return ctx;
	}

	public void setCtx(FhirContext ctx) {
		this.ctx = ctx;
	}
	

	
	/**
	 * Method called for Single patient query from UI. This is called after the OAuth workflow
	 * is successfully completed and access token is put in the session.
	 * 
	 * @param request 	httpServletRequest used to get access to session attributes
	 * @param response  httpServletResponse unused
	 * @param fhirBean	FhirQueryBean used to transport data from UI to the server
	 * @param result	BindingResult used to handle validation results.
	 * 
	 * @return String 	JSON object with FHIR data related to single Patient
	 */
	
	@RequestMapping(value = "/fhirquery", method = RequestMethod.GET)
	public String fhirQuery(HttpServletRequest request, HttpServletResponse response, 
							@ModelAttribute("fhirBean") @Valid FhirQueryBean fhirBean, BindingResult result)
	{
		System.out.println("Inside [fhirQuery] method!");
		
		// make sure the fhir URL and Patient Id are both filled
		// Step 1: Request Parameter (will be filled if standalone)
		// Step 2: Session (will be filled if via EHR launch)
		if ( (fhirBean.getUrl() == null || fhirBean.getUrl().length() == 0 ) ||
			 (fhirBean.getId() == null || fhirBean.getId().length()== 0 ) ) 
		{
			// Check if can be populated from Launch Context
			fhirBean = (FhirQueryBean) request.getSession().getAttribute("FhirBeanFromLogin");
			
			if ((fhirBean == null) ||
				(fhirBean.getUrl() == null || fhirBean.getUrl().length() == 0 ) ||
				(fhirBean.getId() == null || fhirBean.getId().length()== 0 ) )
			{
				System.out.println("Returning null since FhirBean is not filled!");
				return null;
			}
		}

		// Check for any form validation errors
		if (result.hasErrors())
		{
			System.out.println("Returning null since form validation failed!");
			return (null);
		}
		
		String token = null;
		
		// Get Bearer token from the Session. If one does not exist, then you cannot query securely.
		AccessToken accessToken = (AccessToken) request.getSession().getAttribute("AccessTokenObject");
		
		if (accessToken != null)
		{
			token = accessToken.getAccess_token();
		}
		
		System.out.println("Calling [fhirQuery] method with bean: " + fhirBean.toString());
		
		// Pass the FhirBean to the DAO to call FHIR server
		String resString = null;
		try 
		{
			resString = FhirDAO.querySinglePatient(fhirBean, this.ctx, token);
			
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (resString);
	}
	
	

	
	/**
	 * Method called for "All Patients" query from UI. This is called after the OAuth workflow
	 * is successfully completed and access token is put in the session. This method is called
	 * only for "standalone" launch
	 * 
	 * @param request
	 * @param response
	 * @param fhirBean
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/allPatients", method = RequestMethod.GET)
	public String fhirAllPatients(HttpServletRequest request, HttpServletResponse response,
								@ModelAttribute("fhirBean") @Valid FhirQueryBean fhirBean, 
								BindingResult result)
	{
		
		System.out.println("Inside [allPatients] method!");

		System.out.println("Calling [allpatients] with bean: " + fhirBean.toString());

		
		// Check for any form validation errors
		if (result.hasErrors())
		{
			System.out.println("Returning null since form validation failed!");
			return (null);
		}
		
		// make sure the fhir URL is filled
		if ( (fhirBean.getUrl() == null || fhirBean.getUrl().length() == 0 ))
		{
			System.out.println("Returning null since FhirBean is not filled!");
			return null;
		}

		// Get Bearer token from the Session. If one does not exist, then you cannot query securely.
		String token = null;
		AccessToken accessToken = (AccessToken) request.getSession().getAttribute("AccessTokenObject");
		
		if (accessToken != null)
		{
			token = accessToken.getAccess_token();
		}
		
		// Pass the FhirBean to the DAO to call FHIR server
		String resString = null;
		try 
		{
				resString = FhirDAO.getAllPatients(fhirBean, ctx, token);
				
		} catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return (resString);
		
	}
	
	/**
	 * Main entry point to OAuth workflow. 
	 * Method called to build Authorization URL to begin the OAuth Workflow.
	 * 1) Checks if need to call Authorization URL
	 * 2) Adds the FhirBean and Token URL into session so that it can be used after authorization is completed.
	 * 
	 * @param request
	 * @param httpServletResponse
	 * @param fhirBean
	 * @param result
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(HttpServletRequest request, 
						HttpServletResponse httpServletResponse,  
						@ModelAttribute("fhirBean") @Valid FhirQueryBean fhirBean, 
						BindingResult result)
	{
		String retStr = null;
		
		System.out.println("Inside [login] method!");


		
		// Make sure that the Fhir URL is filled.
		if ( fhirBean.getUrl() == null || fhirBean.getUrl().length() == 0 ) 
		{
			// Check if can be populated from Launch Context
			fhirBean = (FhirQueryBean) request.getSession().getAttribute("FhirBeanFromEhrLaunch");
			
			if (fhirBean == null || fhirBean.getUrl() == null)
			{
				System.out.println("Returning null since FhirBean URL is not filled!");
				return null;
			}
		}
		else
		{
			System.out.println("FhirBean URL from Request:" + fhirBean.getUrl() );
		}
	
		
		// Check for any form validation errors
		if (result.hasErrors())
		{
			System.out.println("Returning null since form validation failed!");
			return null;
		}

		System.out.println("[login]FhirQueryBean: " + fhirBean.toString());
		
		// Check for authorization workflow
		if ((fhirBean.getAuth() != null && fhirBean.getAuth().equalsIgnoreCase("true")) &&
			doAuthorization(request, fhirBean)
			)
		{
			// Clean up old session data and then 
			// put the FhirBean and Token URL into Session so that it is used
			// by the Authorization controller during Oauth workflow.
			
			// Remove all existing session objects
			cleanupSessionData(request);
			
			// Set the FhirBean into session
			request.getSession().setAttribute("FhirBeanFromLogin", fhirBean);
			
			// Get Client ID
			String clientId = FhirAuthorizationCodeUtils.getClientId(fhirBean.getUrl());
			
			// Get new state
			StateProvider sp = new StateProvider();

			// Get redirect URI from the deployment context.
			String redirect_uri = FhirAuthorizationCodeUtils.getRedirectURL(fhirBean.getUrl());
			
			// Get Launch Context
			String launch = fhirBean.getLaunch();
			
			AuthorizationCoderRequestBuilder authBuilder = new AuthorizationCoderRequestBuilder(this.ctx, fhirBean.getUrl());
			AuthorizationCodeRequest authCodeReq = authBuilder.buildAuthorizationCodeRequest(sp.getNewState(), 
					clientId, launch);
			
			// Set the Token URL into Session for use later on
			System.out.println("Token URL [in session] = " + authCodeReq.getToken_url() );
			
			request.getSession().setAttribute("TokenURL", authCodeReq.getToken_url());

			
			if (authCodeReq != null)
			{
				String auth_url = "";
				String launch_context = null;

				auth_url =  authCodeReq.getAuthorize_url() + "?" + 
							"client_id=" + authCodeReq.getClient_id() + 
							"&response_type=" + authCodeReq.getResponse_type() + 
							"&scope=" + authCodeReq.getScope() + 
							"&redirect_uri=" + redirect_uri + 
							"&state=" + authCodeReq.getState() + 
							"&aud=" + authCodeReq.getAud();
				
				// Get launch context if EHR launch
				launch_context = authCodeReq.getLaunch();

				if (launch_context != null && launch_context.length() >0 )
				{
					auth_url += "&launch=" + launch_context;
				}

				System.out.println("Authorize URL: "  + auth_url);

				// Build JSON return string
				Map<String, String> map = new HashMap<String, String>();
				map.put("url", auth_url);
				retStr = TextUtils.convertObjectToJSON(map);
			}
		}

		return (retStr);

	}
	
	/**
	 * This method is called when the "EHR Launch" sequence is invoked from the SMART FHIR server.
	 * This method will use the parameters passed from the SMART FHIR server, to obtain data on the 
	 * selected patient
	 * 
	 * 
	 * @param request	<i>HttpServletRequest</i>
	 * @param response	<i>HttpServletResponse</i>
	 * @param iss		identifies the EHR's FHIR end point and used to obtain additional information
	 * @param launchStr	opaque identifier for this specific launch and EHR associated with it. This parameter
	 * 					needs to be communicated back to the EHR at authorization time.
	 * @param model		Used to pass the <i>FhirBean</i> object instantiated from the request parameters
	 * 
	 * 
	 * @return "true" if successful or else "false"
	 */
	
	@RequestMapping(value = "/ehrLaunch", method = RequestMethod.GET)
	public  String ehrLaunch(HttpServletRequest request,
					HttpServletResponse response,
					@RequestParam(value = "iss", required = false) String iss,
					@RequestParam(value = "launch", required = false) String launchStr,
					Model model)
	{
		String retStr = null;
		
		System.out.println("Inside [ehrLaunch] method!");

		System.out.println("[ehrLaunch] " + "iss=" + iss + " and launch=" + launchStr);
		
		// Check that the iss and launch parameters are not null. 
		Validate.notEmpty(iss, "iss cannot be null or empyty");
		Validate.notEmpty(launchStr, "Launch string cannot be null or empty");
		
		// Set the FhirBean into session and then forward request to login
		FhirQueryBean fhirBean = new FhirQueryBean();
		fhirBean.setUrl(iss);
		fhirBean.setAuth("true");
		fhirBean.setLaunch(launchStr);
		
		request.getSession().setAttribute("FhirBeanFromEhrLaunch", fhirBean);

		// Build JSON return string simply for returning to Client UI
		Map<String, String> map = new HashMap<String, String>();
		map.put("value", "true");
		retStr = TextUtils.convertObjectToJSON(map);

		return (retStr);
		
	}

	
	/**
	 * Method called to obtain <i>Document</i> pointed by the <i>DocumentReference</i> FHIR resource
	 * @param request	HttpServletRequest
	 * @param response	HttpServletResponse
	 * @param docURL	String				URL of the Document	
	 * @param fhirURL	String 				URL of the FHIR server
	 * 
	 * @return
	 */
	
	@RequestMapping(value = "/getDocument", method = RequestMethod.GET)
	public String getDocument(HttpServletRequest request,
					HttpServletResponse response, 
					@RequestParam("docURL") String docURL, 
					@RequestParam("url") String fhirURL)
	{
	
		System.out.println("Inside [getDocument] method!");
		
		// Check that we have valid auth code
		System.out.println("Inside [getDocument] method with request params " + 
							" docURL = " + docURL + " and Fhir URL = " + fhirURL);
		
		// Check that the FHIR URL and Doc URL are not null. 
		Validate.notEmpty(fhirURL, "FHIR Base URL cannot be null or empyty");
		Validate.notEmpty(docURL, "Document URL cannot be null or empty");
		
		// Get Access token from Session
		String token = null;
		
		// Get Bearer token from the Session. If one does not exist, then you cannot query securely.
		AccessToken accessToken = (AccessToken) request.getSession().getAttribute("AccessTokenObject");
		
		if (accessToken != null)
		{
			token = accessToken.getAccess_token();
		}
		
		// Call a GET to obtain Document data. And, for now, just dump it in the response.
		DocumentProvider docProvider = new DocumentProvider(this.ctx, fhirURL, token);
		String docStr = docProvider.doGet(docURL);
		
		// Right now, see if I can return a simple JSON object for Document just to 
		// get the roundtrip w/ UI completed. Later on figure out better way to 
		// show the document.
		
		Map<String, String> map = new HashMap<String, String>();
		map.put("attachedDocument", docStr);
		
		return (TextUtils.convertObjectToJSON(map));
		
	}
	
	
	
///////////////////// Methods below are helper methods //////////////////////////
	
	/**
	 * Cleans up session data if FHIR server has changed
	 * @param request
	 */
	protected void cleanupSessionData(HttpServletRequest request)
	{
		// New FHIR server is selected. Clean up all session data
		request.getSession().removeAttribute("FhirBeanFromLogin");
		request.getSession().removeAttribute("FhirBeanFromEhrLaunch");
		request.getSession().removeAttribute("TokenURL");
		request.getSession().removeAttribute("AccessTokenObject");
		
	}
	
	/**
	 * Returns true if there needs to be call to do new authorization or False, if the 
	 * existing authorization can be re-used. 
	 * 
	 * If it is a EHR Launch situation, then do authorization everytime for now.
	 * 
	 * @param request				<i>HttpServletRequest</i> object
	 * @param fhirBeanFromRequest	<i>FhirQueryBean</i> from the request
	 * @return
	 */
	
	protected boolean doAuthorization(HttpServletRequest request,
											FhirQueryBean fhirBeanFromRequest)
	{
		boolean bNew = true;
		
		Validate.notNull(fhirBeanFromRequest, "FhirBean from Request is null!");
		
		AccessToken accessToken = (AccessToken) request.getSession().getAttribute("AccessTokenObject");
		FhirQueryBean fhirBeanFromSession = (FhirQueryBean) request.getSession().getAttribute("FhirBeanFromLogin");

		// If FHIR Bean does not exist in session, this is first time! So, have to do new authorization
		if (fhirBeanFromSession == null)
		{
			return (true);
			
		}
		
		// Old Access token does not exist. So, have to get new access token
		if (accessToken == null)
		{
			return (true);
		}
		
		// This is EHR Launch. For now, just return "True". Later optimize.
		if (fhirBeanFromRequest.getLaunch() != null && fhirBeanFromRequest.getLaunch().length() > 0 )
		{
			return (true);
		}
		
		// Old Access Token exists. So, have to check if they belong to the same
		// FHIR server. 
		
		// FHIR Server URLS are not same. So, have to get new access token
		if (!fhirBeanFromSession.getUrl().equals(fhirBeanFromRequest.getUrl()))
		{
			return (true);
		}
		
		return (false);
		
	}
	
}