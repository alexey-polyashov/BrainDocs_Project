var baseURL = window.location.protocol + '//' + window.location.host;
var baseUrL_application = '/';
if(baseURL.indexOf('localhost')>-1){
    var baseUrL_application = '/braindocs';
}

const baseURL_documentsAPI = baseUrL_application + '/api/v1/documents/';
const baseURL_filesAPI = baseUrL_application + '/api/v1/files/';
const baseURL_testAPI = baseUrL_application + '/api/v1/test/';


(function ($localStorage) {
    'use strict';

    angular
        .module('app', ['ngRoute', 'ngStorage'])
        .config(config)
        .run(run);

    function config($routeProvider) {
        $routeProvider
//            .when('/documents/newdocument', {
//                  templateUrl: 'documents/newdocument/index.html',
//                  controller: 'newDocumentController'
//            })
//            .when('/files/document/upload', {
//                    templateUrl: 'files/document/upload/index.html',
//                    controller: 'filesUploadController'
//              })
//            .when('/files/document/list', {
//                    templateUrl: 'files/document/list/index.html',
//                    controller: 'filesListController'
//              })
            .otherwise({
                redirectTo: '/'
            })
            ;
    }

    function run($rootScope, $http, $localStorage) {


    }

})();


angular.module('app').controller('indexController', function ($rootScope, $location, $scope, $http, $localStorage) {

    $scope.swagger = baseUrL_application + "/swagger-ui.html";

    $scope.testcheck = function(){
        $http({
              method: 'GET',
              url: baseURL_testAPI
           })
           .then(function success(result){
                $scope.testresult = result.data[0];
            })
            ;
    }

    $scope.testcheck();

})