define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', ['$scope','$state','$window','$timeout','localStorageService','SessionService',
        function($scope,$state,$window,$timeout,localStorageService,SessionService) {

            $scope.agenciaSession = {};
            $scope.usuarioSession = undefined;

            $scope.loadUsuarioOfSession = function(){
                SessionService.getUsuarioOfSession().then(
                    function(data){
                        $scope.usuarioSession = data;
                    },
                    function error(error){
                        $scope.usuarioSession = undefined;
                        if(error.status == 400){
                            $scope.loadUsuarioOfSession();
                        }
                    }
                );
            };
            $scope.loadAgenciaOfSession = function(){
                SessionService.getAgenciaOfSession().then(
                    function(data){
                        $scope.agenciaSession = data;
                    },
                    function error(error){
                        $scope.agenciaSession = {
                            "denominacion":"undefined",
                            "abreviatura":"undefined",
                            "ubigeo": "undefined",
                            "estado":false
                        };
                        if(error.status == 400){
                            $scope.loadAgenciaOfSession();
                        }
                    }
                );
            };

            if(angular.isUndefined($scope.usuarioSession)){
                $scope.loadUsuarioOfSession();
            };
            if(angular.isUndefined($scope.agenciaSession.id)){
                $scope.loadAgenciaOfSession();
            };

        }]);
});