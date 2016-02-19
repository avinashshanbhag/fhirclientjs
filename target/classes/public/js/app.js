'use strict';

/* App Module */

var fhirApp = angular.module('fhirApp', [
    'ui.grid',
    'ui.grid.selection',
    'ngRoute',
    'fhirControllers',
    'fhirServices'
]);


fhirApp.config(

    function($routeProvider, $locationProvider, $httpProvider) {

        $locationProvider.html5Mode(true);


        $routeProvider.
            when('/', {
                templateUrl: 'partials/_all_patients.html',
                controller: 'fhirAllPatientsCtrl'
            }).
            when('/all', {
                templateUrl: 'partials/_all_patients.html',
                controller: 'fhirAllPatientsCtrl'
            }).
            when('/details', {
                templateUrl: 'partials/_fhir_query.html',
                controller: 'FhirDetailsCtrl'
            }).
            when('/about', {
                templateUrl: 'partials/_about.html',
                controller: 'fhirInputCtrl'
            }).
            when('/login', {
                templateUrl: 'partials/_login.html',
                controller: 'loginCtrl'
            }).
            when('/launch.html', {
                templateUrl: 'partials/_launch.html',
                controller: 'FhirLaunchCtrl'
            }).
            otherwise({
                redirectTo: '/'
            });
    }
)

