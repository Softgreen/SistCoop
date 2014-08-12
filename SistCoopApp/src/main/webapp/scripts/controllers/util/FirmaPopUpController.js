define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('FirmaPopUpController', ['$scope','ConfiguracionService','$modalInstance','idPersonas','nombres',
        function($scope, ConfiguracionService,$modalInstance, idPersonas, nombres) {

            $scope.urlBase = ConfiguracionService.getRestApiUrl();

            $scope.idPersonas = idPersonas;
            $scope.nombres = nombres;

            $scope.getUrlFirma = function(index) {
                return $scope.urlBase + '/personas/naturales/' + $scope.idPersonas[index] + '/firma';
            };
            $scope.getNombre = function(index){
                return $scope.nombres[index];
            };

            $scope.ok = function () {
                $modalInstance.close();
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }]);
});