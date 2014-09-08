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
                idBoveda: undefined,
                idMoneda: undefined,
                denominacion: undefined,
                abierto: undefined,
                estadoMovimiento: undefined,
                estado: undefined
            };

            $scope.loadMonedas = function(){
                MonedaService.getMonedas().then(function(data){
                    $scope.combo.moneda = data;
                });
            };

            $scope.loadBoveda = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.findById($scope.id).then(
                        function(data){
                            $scope.view.idBoveda = $scope.id;
                            $scope.view.idMoneda = data.moneda.id;
                            $scope.view.denominacion = data.denominacion;
                            $scope.view.abierto = data.abierto;
                            $scope.view.estadoMovimiento = data.estadoMovimiento;
                            $scope.view.estado = data.estado;
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la boveda."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.crearBovedaForm.$valid) {
                    $scope.control.inProcess = true;

                    BovedaService.actualizar($scope.id, $scope.view.denominacion).then(
                        function(data){
                            $scope.redireccion();
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo editar la boveda."}];
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
            $scope.loadBoveda();

        }]);
});