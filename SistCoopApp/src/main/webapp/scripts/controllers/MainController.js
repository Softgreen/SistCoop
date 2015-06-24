define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', ['$scope','$state','$window','$timeout','localStorageService','hotkeys','SessionService','HotKeysFunctionsService','RedirectService','ConfiguracionService',
        function($scope,$state,$window,$timeout,localStorageService,hotkeys,SessionService,HotKeysFunctionsService,RedirectService,ConfiguracionService) {

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

            $scope.cajaSession = {};
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

            $scope.loadCajaOfSession = function(){
                SessionService.getCajaOfSession().then(
                    function(data){
                        $scope.cajaSession = data;
                        $scope.cajaNotFound = "<span>hola</span>";
                        if(angular.isUndefined($scope.cajaSession)){
                            //$scope.cajaNotFound = '<div class="modal-backdrop fade in" ng-class="{in: animate}" style="z-index: 1040" modal-backdrop="" style="z-index: 1040;"></div>';
                        }
                    },
                    function error(error){
                        $scope.cajaSession = {
                            "denominacion": undefined,
                            "abreviatura": undefined,
                            "abierto": false,
                            "estadoMovimiento":false,
                            "estado": false
                        };
                        if(error.status == 400){
                            $scope.loadCajaOfSession();
                        } else {
                            //$scope.cajaNotFound = '<div class="modal-backdrop fade in" ng-class="{in: animate}" style="z-index: 1040" modal-backdrop="" style="z-index: 1040;"></div>';
                            console.log("La caja no pudo ser cargada");
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
            if(angular.isUndefined($scope.cajaSession.id)){
                $scope.loadCajaOfSession();
            };
            if(angular.isUndefined($scope.agenciaSession.id)){
                $scope.loadAgenciaOfSession();
            };

        }]);
});