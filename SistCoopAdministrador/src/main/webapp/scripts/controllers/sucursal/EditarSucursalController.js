define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarSucursalController', ['$scope','$state','$modal','focus','SucursalService',
        function($scope,$state,$modal,focus,SucursalService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusDenominacion');
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
                id: undefined,
                denominacion: undefined,
                abreviatura: undefined,
                estado: undefined
            };

            $scope.loadSucursal = function(){
                if(!angular.isUndefined($scope.id)){
                    SucursalService.getSucursal($scope.id).then(function(){
                        $scope.view.id = data.id;
                        $scope.view.denominacion = data.denominacion;
                        $scope.view.abreviatura = data.abreviatura;
                        $scope.view.estado = data.estado;
                    });
                }
            };
            $scope.loadAgencias = function(){
                if(!angular.isUndefined($scope.id)){
                    SucursalService.getAgencias($scope.id).then(function(data){
                        $scope.agencias = data;
                    });
                }
            };

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.crearSucursalForm.$valid) {
                    $scope.control.inProcess = true;

                    var sucursal = {
                        denominacion: $scope.view.denominacion,
                        abreviatura: $scope.view.abreviatura
                    };

                    SucursalService.actualizar($scope.id, sucursal).then(
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

            $scope.eliminarSucursal = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    SucursalService.desactivar($scope.id).then(
                        function(data){
                            $scope.alerts = [{ type: "success", msg: "Sucursal desactivada." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.redireccion();
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {
                });
            };

            $scope.openNuevaAgencia = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/administrador/agencia/crearAgenciaPopUp.html',
                    controller: "CrearAgenciaPopUpController"
                });
                modalInstance.result.then(function (agencia) {
                    SucursalService.crearAgencia($scope.id, agencia).then(
                        function(data){
                            agencia.estado = true;
                            $scope.agencias.push(agencia);
                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: "success", msg: "Agencia añadida."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {

                });
            };
            $scope.editarAgencia = function (index) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/administrador/agencia/editarAgenciaPopUp.html',
                    controller: "EditarAgenciaPopUpController",
                    resolve: {
                        agencia: function () {
                            return $scope.agencias[index];
                        }
                    }
                });
                modalInstance.result.then(function (agencia) {
                    SucursalService.actualizarAgencia($scope.id, agencia.id, agencia).then(
                        function(data){
                            $scope.agencias[index] = agencia;
                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: "success", msg: "Agencia actualizada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {

                });
            };

            $scope.eliminarAgencia = function (index) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    SucursalService.desactivarAgencia($scope.id, $scope.agencias[index].id).then(
                        function(data){
                            $scope.alerts = [{ type: "success", msg: "Agencia desactivada." }];
                            $scope.loadAgencias();
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {
                });
            };

            $scope.loadSucursal();
            $scope.loadAgencias();

            $scope.redireccion = function(){
                $state.transitionTo('app.sucursal.buscarSucursal');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };


        }]);
});
