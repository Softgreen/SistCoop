define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTrabajadorController', ['$scope','$state','focus','TrabajadorService','SucursalService','PersonaNaturalService','Restangular',
        function($scope,$state,focus,TrabajadorService,SucursalService,PersonaNaturalService,Restangular) {

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
                idSucursal: undefined,
                idAgencia: undefined,
                idTipoDocumento: undefined,
                numeroDocumento: undefined,
                usuario: undefined,

                persona: undefined
            };

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

            $scope.registrarPersonaNatural = function(){
                $state.transitionTo('app.administracion.crearPersonaNatural');
            };

            $scope.crearTransaccion = function(){
                if ($scope.crearTrabajadorForm.$valid && !angular.isUndefined($scope.view.persona)) {
                    $scope.control.inProcess = true;

                    var trabajador = {
                        idSucursal: $scope.view.idSucursal,
                        idAgencia: $scope.view.idAgencia,
                        idTipoDocumento: $scope.view.idTipoDocumento,
                        numeroDocumento: $scope.view.numeroDocumento,
                        usuario: $scope.view.usuario ? $scope.view.usuario.username : undefined
                    };

                    TrabajadorService.crear(trabajador).then(
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

            $scope.$watch("view.numeroDocumento", function(){
                $scope.validarNumeroDocumento();
            });
            $scope.$watch("view.idTipoDocumento", function(){
                $scope.validarNumeroDocumento();
            });
            $scope.validarNumeroDocumento = function(){
                if(!angular.isUndefined($scope.crearTrabajadorForm.numeroDocumento)){
                    if(!angular.isUndefined($scope.view.numeroDocumento)){
                        if(!angular.isUndefined($scope.view.idTipoDocumento)){
                            var tipoDoc = $scope.getTipoDocumento();
                            if(!angular.isUndefined(tipoDoc)) {
                                if($scope.view.numeroDocumento.length == tipoDoc.numeroCaracteres) {
                                    $scope.crearTrabajadorForm.numeroDocumento.$setValidity("sgmaxlength",true);
                                } else {$scope.crearTrabajadorForm.numeroDocumento.$setValidity("sgmaxlength",false);}
                            } else {$scope.crearTrabajadorForm.numeroDocumento.$setValidity("sgmaxlength",false);}
                        } else{$scope.crearTrabajadorForm.numeroDocumento.$setValidity("sgmaxlength",false);}
                    } else {$scope.crearTrabajadorForm.numeroDocumento.$setValidity("sgmaxlength",false);}}
            };
            $scope.getTipoDocumento = function(){
                if(!angular.isUndefined($scope.combo.tipoDocumento)){
                    for(var i = 0; i < $scope.combo.tipoDocumento.length; i++){
                        if($scope.view.idTipoDocumento == $scope.combo.tipoDocumento[i].id)
                            return $scope.combo.tipoDocumento[i];
                    }
                }
                return undefined;
            };

            $scope.loadSucursales();
            $scope.loadTipoDocumento();

            $scope.redireccion = function(){
                $state.transitionTo('app.trabajador.buscarTrabajador');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
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