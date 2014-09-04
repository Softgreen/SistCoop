define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarBovedaController', ['$scope','$state','focus','MonedaService','BovedaService',
        function($scope,$state,focus,MonedaService,BovedaService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusMoneda');
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                moneda: undefined
            };

            $scope.view = {
                idMoneda: undefined,
                denominacion: undefined
            };

            $scope.loadMonedas = function(){
                MonedaService.getMonedas().then(function(data){
                    $scope.combo.moneda = data;
                });
            };

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.crearBovedaForm.$valid) {
                    $scope.control.inProcess = true;

                    BovedaService.crear($scope.view.idMoneda, $scope.view.denominacion).then(
                        function(data){
                            $scope.redireccion();
                            $scope.control.inProcess = false;
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.redireccion = function(){
                $state.transitionTo('app.boveda.buscarBoveda');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };

            $scope.loadMonedas();

        }]);
});