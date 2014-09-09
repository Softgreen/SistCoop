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

            $scope.operaciones = {
                changeEstadoMovimiento: undefined,
                changeEstadoAbierto: undefined,
                changeEstado: undefined
            };

            $scope.$watch("view.abierto",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.abierto)){
                        $scope.operaciones.changeEstadoMovimiento = $scope.view.abierto;
                    }
                }
            },true);
            $scope.$watch("view.estadoMovimiento",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.estadoMovimiento)){
                        $scope.operaciones.changeEstadoAbierto = $scope.view.estadoMovimiento;
                    }
                }
            },true);
            $scope.$watch("view.estado",function (newVal, oldVal) {
                if (newVal !== oldVal) {
                    if(!angular.isUndefined($scope.view.estado)){
                        $scope.operaciones.changeEstado = $scope.view.abierto;
                    }
                }
            },true);

            $scope.getEstadoAsString = function(){
                if(!angular.isUndefined($scope.view.idBoveda)){
                    var result = '';
                    if($scope.view.abierto)
                        result = result + 'ABIERTO, ';
                    else
                        result = result + 'CERRADO, ';
                    if($scope.view.estadoMovimiento)
                        result = result + 'DESCONGELADO, ';
                    else
                        result = result + 'CONGELADO, ';
                    if($scope.view.estado)
                        result = result + 'ACTIVO';
                    else
                        result = result + 'INACTIVO';
                    return result;
                } else {
                    return undefined;
                }
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

            $scope.abrirBoveda = function(){
                $state.transitionTo('app.boveda.abrirBoveda', { id: $scope.id });
            };

            $scope.cerrarBoveda = function(){
               // console.log("cerrando");
               // $state.go('app.boveda.cerrarBoveda', { id: $scope.id });
            };

            $scope.congelarBoveda = function(){
                //console.log("congelando");
            };
            $scope.descongelarBoveda = function(){
                //console.log("descongelando");
            };
            $scope.inactivarBoveda = function(){
               // console.log("inactivando");
            };

            $scope.loadMonedas();
            $scope.loadBoveda();

        }]);
});