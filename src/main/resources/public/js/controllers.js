'use strict';

/* Controllers */
var fhirControllers = angular.module('fhirControllers', []);


// This controller runs when the "All Patients" page is queried.
fhirControllers.controller('fhirAllPatientsCtrl', ['$scope','$location', '$window','AllPatientsService', 'OAuthLogIn', 'Fhir','FhirBeanService',
	function($scope, $location, $window, AllPatientsService, OAuthLogIn, Fhir, FhirBeanService) {

		// Initialize Scope Variables

		$scope.result = {};
		$scope.names = {};
		$scope.text = {};


		$scope.fhirBean = FhirBeanService.get();


		console.log("FhirBean is: %s", JSON.stringify($scope.fhirBean));

		// Make sure that the Patient ID is removed from the bean, even if it was present before
		$scope.fhirBean.id = "";


		// Get all data only if authorized or unsecured
		$scope.myData = AllPatientsService.getAllPatientInfo($scope.fhirBean);


		// Submit button handler
		$scope.query = function()
		{

			// Add the Bean to the service
			FhirBeanService.update($scope.fhirBean);

			console.log("[Inside Query - Before save] FhirBean is: %s", JSON.stringify($scope.fhirBean));
			console.log("[Inside Query - After save] FhirBean is: %s", JSON.stringify(FhirBeanService.get()));


			// Get the auth URL and do client side redirect.
			OAuthLogIn.getData($scope.fhirBean)
				.$promise
				.then(function(result) {

					// Redirect to Authorize URL if needed
					if (result.url != null && result.url.length > 0) {
						$window.location.href = result.url;
					}
					else
					{
						// Get all data only if authorized or unsecured
						$scope.myData = AllPatientsService.getAllPatientInfo($scope.fhirBean);
					}


				})

		};



		// Define Column headers

		$scope.myCols = [
			{ field: 'name', displayName: 'First Name'},
			{ field: 'gender', displayName: 'Sex'},
			{ field: 'id', displayName: 'Id'},
			{ field: 'dob', displayName: 'DOB'},
			{ field: 'address', displayName: 'Address'}
		];


		// Initialize Grid and bind data

		$scope.gridOptions = {
			enableRowSelection: true,
			enableRowHeaderSelection: false,
			data: 'myData',
			columnDefs: $scope.myCols
		};

		// Only allow single row selection

		$scope.gridOptions.multiSelect = false;


		// This registers the call-back function to be used to update the FhirBean on row selection

		$scope.gridOptions.onRegisterApi = function( gridApi )
		{
			$scope.gridApi = gridApi;

			gridApi.selection.on.rowSelectionChanged($scope,function(row)
			{
				$scope.fhirBean.id = row.entity.id;
			});

		};

		// This method changes the location URL to details page, and updates the FhirBean in the Service
		// This would trigger opening the "Detail" page

		$scope.forwardToDetails = function() {

			$location.path( "details" );

			// Add the Bean to the service
			FhirBeanService.update($scope.fhirBean);

			console.log("[Inside ForwardToDetails]FhirBean is: %s", JSON.stringify($scope.fhirBean));


		};

	}]);

// This controller is called when "Single Patient" page is invoked
fhirControllers.controller('FhirDetailsCtrl', ['$scope','$location', '$window','AllPatientsService',
	                       'OAuthLogIn', 'DocumentService', 'Fhir','FhirBeanService',
	function($scope, $location, $window, AllPatientsService, OAuthLogIn, DocumentService, Fhir, FhirBeanService) {

		$scope.tab = 1;
		$scope.result = {};
		$scope.fhirBean = FhirBeanService.get();

		console.log("Calling Controller for single Patient FhirBean is: %s", JSON.stringify($scope.fhirBean));


		// This method is called when redirected from "All Patients" page to Single Patient Page.
		$scope.result = Fhir.getData($scope.fhirBean);


		// This function is called when "Submit" button is clicked on the Single Patien Page
		$scope.query = function()
		{
			FhirBeanService.update($scope.fhirBean);

			console.log("[Inside Query - Before save] FhirBean is: %s", JSON.stringify($scope.fhirBean));
			console.log("[Inside Query - After save] FhirBean is: %s", JSON.stringify(FhirBeanService.get()));

			// Get the auth URL and do client side redirect.
			OAuthLogIn.getData($scope.fhirBean)
				.$promise
				.then(function(result) {

					// Redirect to Authorize URL if needed
					if (result.url != null && result.url.length > 0) {
						$window.location.href = result.url;
					}
					else
					{
						// Get all data only if authorized or unsecured
						$scope.result = Fhir.getData($scope.fhirBean);
					}

				})
		};


		// The functions below are used to manage tabs
		// function called to select tab

		$scope.selectTab = function(setTab)
		{
			$scope.tab = setTab;
		};

		// Check if tab is selected

		$scope.isSelected = function(checkTab)
		{
			if ($scope.tab == checkTab)
				return true;
			else
				return false;
		};

		// Upto here.




		// This function is called when user wants to access document
		$scope.getDocument = function(docURL)
		{
			//$window.alert(docURL);
			console.log("[Inside getDocument] url is: %s", $scope.fhirBean.url);
			console.log("[Inside getDocument  ] docURL is: %s", docURL);

			$scope.params = {};

			$scope.params.url = $scope.fhirBean.url;
			$scope.params.docURL = docURL;


			// This should get the actual Document stream.
			DocumentService.getDocument($scope.params)
				.$promise
				.then(function(result){
					$scope.result.attachedDocument = result.attachedDocument;
				})

		};

		// This function is called when "Reset" is hit
		$scope.reset = function()
		{
			$scope.result = {};
			$scope.names = {};

			$scope.fhirBean = { "url": "",
				"resource": "",
				"id": "",
				"auth": "false"
			};
		};

		// Used to control display of Demographics Information on the details page
		$scope.isDemographicsEmpty = function()
		{
			if ($scope.result.demographics == null)
				return true;
			else
				return false;
		};

		// Used to control display of DocumentReference Information on the details page
		$scope.isDocReferenceEmpty = function()
		{
			if ($scope.result.documentReferences == null)
				return true;
			else
				return false;
		};


		// Used to control display of Medicine Information on the details page
		$scope.isMedicineInfoEmpty = function()
		{
			if ($scope.result.medicines == null || $scope.result.medicines.length == 0)
				return true;
			else
				return false;
		};

		// Used to control display of MedicationOrder on the details page
		$scope.isMedOrderInfoEmpty = function()
		{
			if ($scope.result.medOrderDetails == null || $scope.result.medOrderDetails.length == 0)
				return true;
			else
				return false;
		};

		// Used to control display of Medication Dispense details on the details page
		$scope.isMedDispenseInfoEmpty = function()
		{
			if ($scope.result.medDispenseDetails == null || $scope.result.medDispenseDetails.length == 0)
				return true;
			else
				return false;
		};

		// Used to control display of Medication Dispense details on the details page
		$scope.isMedStatementInfoEmpty = function()
		{
			if ($scope.result.medStatementDetails == null || $scope.result.medStatementDetails.length == 0)
				return true;
			else
				return false;
		};


		// Used to control display of Allergy Information on the details page
		$scope.isAllergyInfoEmpty = function()
		{
			if ($scope.result.allergies == null || $scope.result.allergies.length == 0)
				return true;
			else
				return false;
		};

		// Used to control display of Allegy information on the details page
		$scope.isProblemInfoEmpty = function()
		{
			if ($scope.result.problems == null || $scope.result.problems.length == 0)
				return true;
			else
				return false;
		};

		// Used to control display of diagnostic report information on the details page
		$scope.isDiagnosticReportInfoEmpty = function()
		{
			if ($scope.result.diagnosticReports == null || $scope.result.diagnosticReports.length == 0)
				return true;
			else
				return false;
		};


		// Used to check if the page is from EHR Launch. If Fhirbeah.Launch has valid value, then,
		// its launched from EHR; otherwise it is a standalone launch;
		$scope.isEHRLaunch = function()
		{

			if ($scope.fhirBean.launch == null || $scope.fhirBean.launch.length == 0)
				return false;
			else
				return true;
		};


		// Checks if the observation is a Codeable Concept
		$scope.isTypeConceptCode = function(dr)
		{
			if (dr.observation != null && dr.observation.type != null)
			{
				if (dr.observation.type == "CODEABLECONCEPT")
					return true;
				else
					return false;
			}
			else
				return false;
		};

		// Checks if the observation is quantity & units

		$scope.isTypeQuantity = function(dr)
		{
			if (dr.observation != null && dr.observation.type != null)
			{
				if (dr.observation.type == "QUANTITY")
					return true;
				else
					return false;
			}
			else
				return false;
		};



	}]);


// This controller is called when the app is launched from "EHR" with a patient context

fhirControllers.controller('FhirLaunchCtrl', ['$scope','$location', '$window','AllPatientsService',
	'OAuthLogIn', 'ehrLaunchService', 'Fhir','FhirBeanService',
	function($scope, $location, $window, AllPatientsService, OAuthLogIn, ehrLaunchService, Fhir, FhirBeanService) {

		console.log("Inside FhirLaunchCtrl:" + $window.location.search);

		$scope.params = getSearchParameters();


		function getSearchParameters() {
			var prmstr = window.location.search.substr(1);
			return prmstr != null && prmstr != "" ? transformToAssocArray(prmstr) : {};
		}

		function transformToAssocArray( prmstr ) {
			var params = {};
			var prmarr = prmstr.split("&");
			for ( var i = 0; i < prmarr.length; i++) {
				var tmparr = prmarr[i].split("=");
				params[tmparr[0]] = decodeURIComponent(tmparr[1]);
			}
			return params;
		}

		console.log("iss=" + $scope.params["iss"]);
		console.log("launch=" + $scope.params["launch"]);


		$scope.fhirBean = FhirBeanService.get();
		$scope.fhirBean = { "url": "https://fhir-api-dstu2.smarthealthit.org",
			"resource": "",
			"id": "",
			"auth": "true",
			"launch": ""
		};


		$scope.fhirBean.url =  $scope.params["iss"];
		$scope.fhirBean.launch = $scope.params["launch"];
		$scope.fhirBean.auth = "true";

		FhirBeanService.update($scope.fhirBean);


		console.log("Calling [getLaunchData] with params %s and %s", $scope.params["iss"], $scope.params["launch"]);

		ehrLaunchService.getLaunchData($scope.params)
			.$promise
			.then(function(result) {

				console.log("Calling [login] result.value is: %s ", result.value);

				OAuthLogIn.getData($scope.fhirBean)
					.$promise
					.then(function(result) {

						console.log("After [login] result.url is: %s ", result.url);

						// Redirect to Authorize URL if needed
						if (result.url != null && result.url.length > 0) {
							$window.location.href = result.url;
						}
						else
						{
							// Get all data only if authorized or unsecured
							//$scope.result = Fhir.getData($scope.fhirBean);
							$location.path( "details" );
						}

					})
			})

	}]);


// Login Controller runs when user presses login link
fhirControllers.controller('loginCtrl', ['$scope', '$http', '$window', 'Fhir', 'OAuthLogIn', 'OAuthLaunch', 'FhirBeanService',
	function($scope, $http, $window, Fhir, OAuthLogIn, OAuthLaunch, FhirBeanService) {

		// Initialize Scope Variables
		$scope.result = {};

		// Get FhirBean that contain FHIR Server URL
		$scope.fhirBean = FhirBeanService.get();


		/*
		$scope.login = function()
		{
			OAuthLogIn.getData($scope.fhirBean)
				.$promise
				.then(function(result) {
					$scope.result.url = result.url;
					//$window.alert($scope.result.url);
					$window.location.href = $scope.result.url;

				})
		};
		*/

		$scope.login = function()
		{
			OAuthLaunch.getData($scope.fhirBean);
		};


		// test if can do redirect from controller.
		//$scope.login = OAuthLaunch.getData($scope.fhirBean);


	}]);


// Call this controller for tab selection logic
fhirControllers.controller('PanelController', ['$scope', '$http', '$window',
	function($scope, $http, $window) {


		// Initialize selected tab
		$scope.tab = 1;

		// function called to select tab
		$scope.selectTab = function(setTab)
		{
			$scope.tab = setTab;
		};

		// Check if tab is selected
		$scope.isSelected = function(checkTab)
		{
			if ($scope.tab == checkTab)
				return true;
			else
				return false;
		};
	}]);



