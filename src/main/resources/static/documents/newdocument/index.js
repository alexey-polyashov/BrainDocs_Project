angular.module('app').controller('newDocumentController', function ($scope, $http, $localStorage) {

    $scope.baseURL_tasksAPI = baseURL_tasksAPI;
    $scope.baseURL_taskdata = '#!/tasks/taskdata';
    $scope.baseURL_newtask = '#!/tasks/newtask';


});