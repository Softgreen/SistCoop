define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('ConfiguracionImpresoraController', ['$scope','$state','localStorageService','ConfiguracionService',
        function($scope,$state,localStorageService,ConfiguracionService) {

            $scope.view = {
                defaulPrinterName: undefined
            };

            $scope.loadPrinter = function(){
                var cookieName = ConfiguracionService.getCookiePrinterName();
                var valueCookie = localStorageService.get(cookieName);
                if(valueCookie !== null){
                    $scope.view.defaulPrinterName = valueCookie;
                } else {
                    var defaulPrinterName = ConfiguracionService.getDefaultPrinterName();
                    localStorageService.set(cookieName, defaulPrinterName);
                    $scope.view.defaulPrinterName = defaulPrinterName;
                }
            };
            $scope.loadPrinter();


            $scope.verificar = function(){
                if ($scope.formConfigurarImpresora.$valid) {
                   findPrinter($scope.view.defaulPrinterName);
                } else {
                    $scope.formConfigurarImpresora.$setDirty();
                }
            };
            $scope.guardar = function(){
                if ($scope.formConfigurarImpresora.$valid) {
                    findPrinter($scope.view.defaulPrinterName);

                    var cookieName = ConfiguracionService.getCookiePrinterName();
                    localStorageService.set(cookieName, $scope.view.defaulPrinterName);

                    $state.transitionTo('app.home');
                } else {
                    $scope.formConfigurarImpresora.$setDirty();
                }
            };
            $scope.cancelar = function(){

            };


            $scope.defaulPrinterName = undefined;
            
        }]);
});
