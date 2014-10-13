define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', ['$scope','$state','$window','$timeout','localStorageService','SessionService','ConfiguracionService',
        function($scope,$state,$window,$timeout,localStorageService,SessionService,ConfiguracionService) {

    	$scope.defaulPrinterName = undefined;

        $scope.searchPrinter = function(){
            $timeout(function(){
                if(isLoaded()){
                    findPrinter($scope.defaulPrinterName);
                    return;
                } else {
                    $scope.searchPrinter();
                }
            },3000);
        };
        $scope.loadPrinter = function(){
            var cookieName = ConfiguracionService.getCookiePrinterName();
            var valueCookie = localStorageService.get(cookieName);
            if(valueCookie !== null){
                $scope.defaulPrinterName = valueCookie;
            } else {
                var defaulPrinterName = ConfiguracionService.getDefaultPrinterName();
                localStorageService.set(cookieName, defaulPrinterName);
                $scope.defaulPrinterName = defaulPrinterName;
            }
            $scope.searchPrinter();
        };
        $scope.loadPrinter();
        
            $scope.$watch('redirect', function(newValue, oldvalue){
                if(newValue != oldvalue)
                    if($scope.redirect == true)
                        RedirectService.limpiar();
            }, true);

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