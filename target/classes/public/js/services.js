'use strict';

/* Services */


var fhirServices = angular.module('fhirServices', ['ngResource']);


/**
 *  Service to keep track of Fhir Information (URL, Resource, ID) across controllers.
 *  Otherwise, the FhirBean value gets reset since each controller is tied to a neW scope.
 *
 */
fhirServices.factory('FhirBeanService', ['$resource', '$window', function($resource, $window) {

	var fhirBean = {};

	// Get FhirBean

	fhirBean.get = function()
	{
		var fhir_session = angular.fromJson($window.sessionStorage.getItem("fhirBean_App")) || {};
		return fhir_session;
	};

	// Update FhirBean
	fhirBean.update = function(updateData)
	{
		$window.sessionStorage.setItem("fhirBean_App", angular.toJson(updateData));
	};

	return (fhirBean);


}]);


// This service calls controller method for a Single Patient

fhirServices.factory('Fhir', ['$resource', function($resource) {

	return $resource('/fhir-app/fhirquery', null,
			{
				'getData': {method: 'GET',
							headers: {'Content-Type': 'application/json;charset=UTF-8'
								}
							}
			});
}]);

// This service calls controller method for a All Patient
fhirServices.factory('AllPatientsService', ['$resource', function($resource) {

	return $resource('/fhir-app/allPatients', null,
			{
			'getAllPatientInfo': {method: 'GET', isArray: true,
				                  headers: {'Content-Type': 'application/json;charset=UTF-8'
				 					}
							}
			});
}]);

// This service calls controller method for getDocument
fhirServices.factory('DocumentService', ['$resource', function($resource) {

	return $resource('/fhir-app/getDocument', null,
		{
			'getDocument': {method: 'GET',
				headers: {'Content-Type': 'application/json;charset=UTF-8'
				}
			}
		});
}]);


// This service calls the controller method for ehrLaunch
fhirServices.factory('ehrLaunchService', ['$resource', function($resource) {

	return $resource('/fhir-app/ehrLaunch', null,
		{
			'getLaunchData': {method: 'GET',
						headers: {'Content-Type': 'application/json;charset=UTF-8'
				}
			}
		});
}]);



// This service calls controller method for "login"
fhirServices.factory('OAuthLogIn', ['$resource', function($resource) {

	return $resource('/fhir-app/login', null,
		{
			'getData': {method: 'GET',
				headers: {'Content-Type': 'application/json;charset=UTF-8'
				}
			}
		});

}]);

/*
fhirServices.factory('OAuthLaunch', ['$resource', function($resource) {

	return $resource('/fhir-app/launch', null,
		{
			'getData': {method: 'GET',
				headers: {'Content-Type': 'application/json;charset=UTF-8'
				}
			}
		});

}]);
*/


fhirServices.factory('OAuth', ['$resource', function($resource) {

	return $resource('/fhir-app/avifhirclient/oauth', null,
		{
			'getData': {method: 'GET',
				headers: {'Content-Type': 'application/json;charset=UTF-8'
				}
			}
		});

}]);