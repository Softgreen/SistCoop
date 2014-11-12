define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarTrabajadorController', ['$scope','$state','$modal','focus','TrabajadorService','SucursalService','PersonaNaturalService','Restangular',
        function($scope,$state,$modal,focus,TrabajadorService,SucursalService,PersonaNaturalService,Restangular) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusSucursal');
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                sucursal: undefined,
                agencia: undefined,
                tipoDocumento: undefined
            };

            $scope.view = {
                id: undefined,
                idSucursal: undefined,
                idAgencia: undefined,
                idTipoDocumento: undefined,
                numeroDocumento: undefined,
                usuario: undefined,

                persona: undefined
            };

            $scope.loadTrabajador = function(){
                TrabajadorService.getTrabajador($scope.id).then(function(data){
                    $scope.trabajador = data;

                    $scope.view.id = data.id;
                    $scope.view.idSucursal = data.idSucursal;
                    $scope.view.idAgencia = data.idAgencia;
                    $scope.view.idTipoDocumento = data.idTipoDocumento;
                    $scope.view.numeroDocumento = data.numeroDocumento;
                    $scope.view.usuario = data.usuario;
                });
            };
            $scope.loadTrabajador();

            $scope.loadSucursales = function(){
                SucursalService.getSucursales().then(function(data){
                    $scope.combo.sucursal = data;
                });
            };
            $scope.loadAgencias = function(){
                if(!angular.isUndefined($scope.view.idSucursal)){
                    SucursalService.getAgencias($scope.view.idSucursal).then(function(data){
                        $scope.combo.agencia = data;
                    });
                }
            };
            $scope.loadTipoDocumento = function(){
                PersonaNaturalService.getTipoDocumentos().then(function(data){
                    $scope.combo.tipoDocumento = data;
                });
            };

            $scope.$watch('view.idSucursal', function(){
                $scope.loadAgencias();
            });

            $scope.buscarTrabajador = function(){
                if($scope.crearTrabajadorForm.numeroDocumento.$valid && $scope.crearTrabajadorForm.tipoDocumento.$valid){
                    $scope.control.inProcess = true;
                    PersonaNaturalService.findByTipoNumeroDocumento($scope.view.idTipoDocumento, $scope.view.numeroDocumento).then(
                        function(data){
                            if(angular.isUndefined(data) || data === null){
                                $scope.control.inProcess = false;
                                $scope.alertsAccionistas = [{ type: 'danger', msg: 'Persona No Registrado' }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            } else{
                                $scope.control.inProcess = false;
                                $scope.view.persona = data;
                                $scope.alertsAccionistas = [{ type: 'success', msg: 'Persona Registrado' }];
                            }
                        }, function error(error){
                            $scope.control.inProcess = false;
                            $scope.alertsAccionistas = [{ type: 'danger', msg: 'Error al buscar la persona' }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.crearTransaccion = function(){
                if ($scope.crearTrabajadorForm.$valid) {
                    $scope.control.inProcess = true;

                    var trabajador = {
                        idSucursal: $scope.view.idSucursal,
                        idAgencia: $scope.view.idAgencia,
                        idTipoDocumento: $scope.view.idTipoDocumento,
                        numeroDocumento: $scope.view.numeroDocumento,
                        usuario: $scope.view.usuario
                    };

                    TrabajadorService.actualizar($scope.view.id, trabajador).then(
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

            $scope.loadSucursales();
            $scope.loadTipoDocumento();

            $scope.redireccion = function(){
                $state.transitionTo('app.trabajador.buscarTrabajador');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };

            $scope.eliminar = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    TrabajadorService.desactivar($scope.id).then(
                        function(data){
                            $scope.alerts = [{ type: "success", msg: "Trabajador eliminado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {
                });
            };

            $scope.buscarUsuarios = function(){
                Restangular.one('token').get().then(function(data){
                    var token = data;
                    Restangular.allUrl('keycloak','http://localhost:8080/auth/admin/realms/SistemaFinanciero/users')
                        .customGETLIST('',{},{'Authorization': 'Bearer '+ token})
                        .then(function(data){
                            $scope.usuarios = data;
                        });
                });
            };
            $scope.buscarUsuarios();

        }]);
});