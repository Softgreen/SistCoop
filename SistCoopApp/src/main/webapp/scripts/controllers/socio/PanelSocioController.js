define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('PanelSocioController', ['$scope', '$state','$location','$window','$modal','SocioService','PersonaNaturalService','PersonaJuridicaService','MaestroService','RedirectService','ConfiguracionService',
        function($scope,$state,$location,$window,$modal,SocioService,PersonaNaturalService,PersonaJuridicaService,MaestroService,RedirectService,ConfiguracionService) {

            $scope.viewState = "app.socio.panelSocio";
            
            $scope.loadRedireccion = function(){
                if(RedirectService.haveNext()){
                    var state = RedirectService.getNextState();
                    if(state == $scope.viewState){
                        $window.scroll(0,0);
                        RedirectService.clearLast();
                    }
                }
            };

            $scope.loadSocio = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getSocio($scope.id).then(
                        function(data){
                            $scope.socio = data;
                            if($scope.socio.tipoPersona == 'NATURAL'){
                                PersonaNaturalService.findByTipoNumeroDocumento($scope.socio.idTipoDocumento, $scope.socio.numeroDocumento).then(function(data){
                                    $scope.personaNatural = data;
                                    $scope.loadPais();
                                });
                            } else if ($scope.socio.tipoPersona == 'JURIDICA'){
                                PersonaJuridicaService.findByTipoNumeroDocumento($scope.socio.idTipoDocumento, $scope.socio.numeroDocumento).then(function(data){
                                    $scope.personaJuridica = data;
                                    $scope.loadPais();
                                });
                            }
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Socio no encontrado."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            
            $scope.loadPais = function(){
            	if ($scope.socio.tipoPersona == 'NATURAL') {
            		MaestroService.getPaisByCodigo($scope.personaNatural.codigoPais).then(function(data){
                    	$scope.pais = data;
                    });
				} else {
					MaestroService.getPaisByCodigo($scope.personaJuridica.representanteLegal.codigoPais).then(function(data){
                    	$scope.pais = data;
                    });
				}
            };
            
            $scope.loadCuentaAporte = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getCuentaAporte($scope.id).then(
                        function(data){
                            $scope.cuentaAporte = data;
                        }, function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Cuenta de aporte no encontrada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.loadApoderado = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getApoderado($scope.id).then(function(data){
                        $scope.apoderado = data;
                    });
                }
            };
            $scope.loadCuentasBancarias = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getCuentasBancarias($scope.id).then(function(data){
                        $scope.cuentasBancarias = data;
                    });
                }
            };

            $scope.loadBeneficiarios = function(){
                if(!angular.isUndefined($scope.id)){
                    SocioService.getBeneficiarios($scope.id).then(
                        function(data){
                            $scope.beneficiarios = data;
                        }, function error(error){
                            $scope.beneficiarios = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Beneficiarios no encontrados."});
                        }
                    );
                };
            };               

            $scope.loadRedireccion();
            $scope.loadSocio();
            $scope.loadCuentaAporte();
            $scope.loadApoderado();
            $scope.loadCuentasBancarias();
            $scope.loadBeneficiarios();

            $scope.editarSocioPN = function(){
                if(!angular.isUndefined($scope.personaNatural)){
                    var parametros = {
                        id: $scope.id
                    };
                    var nextState = $scope.viewState;
                    RedirectService.addNext(nextState,parametros);
                    $state.transitionTo('app.administracion.editarPersonaNatural', { id: $scope.personaNatural.id });
                }
            };
            $scope.editarSocioPJ = function(){
                if(!angular.isUndefined($scope.personaJuridica)){
                    var parametros = {
                        id: $scope.id
                    };
                    var nextState = $scope.viewState;
                    RedirectService.addNext(nextState,parametros);
                    $state.transitionTo('app.administracion.editarPersonaJuridica', { id: $scope.personaJuridica.id });
                }
            };
            $scope.editarRepresentantePJ = function(){
                if(!angular.isUndefined($scope.personaJuridica)){
                    var parametros = {
                        id: $scope.id
                    };
                    var nextState = $scope.viewState;
                    RedirectService.addNext(nextState,parametros);
                    $state.transitionTo('app.administracion.editarPersonaNatural', { id: $scope.personaJuridica.representanteLegal.id });
                }
            };
            $scope.editarApoderado = function(){
                if(!angular.isUndefined($scope.apoderado)){
                    var parametros = {
                        id: $scope.id
                    };
                    var nextState = $scope.viewState;
                    RedirectService.addNext(nextState,parametros);
                    $state.transitionTo('app.administracion.editarPersonaNatural', { id: $scope.apoderado.id });
                }
            };
            $scope.cambiarApoderado = function(){
                if(!angular.isUndefined($scope.socio)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/socio/apoderadoPopUp.html',
                        controller: "ApoderadoPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        var apoderado = {
                            "idTipoDocumento" : result.tipoDocumento.id,
                            "numeroDocumento": result.numeroDocumento
                        };
                        SocioService.cambiarApoderado($scope.socio.id, apoderado).then(
                            function(data){
                                $scope.loadApoderado();
                                $scope.alerts = [{ type: "success", msg: "Apoderado cambiado." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }, function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                $window.scrollTo(0,0);
                            }
                        );
                    }, function () {
                    });
                }
            };
            $scope.eliminarApoderado = function(){
                if(!angular.isUndefined($scope.socio)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SocioService.eliminarApoderado($scope.socio.id).then(
                            function(data){
                                $scope.apoderado = undefined;
                                $scope.alerts = [{ type: "success", msg: "Apoderado eliminado." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }, function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                $window.scrollTo(0,0);
                            }
                        );
                    }, function () {
                    });
                }
            };
            $scope.editarCuentaBancaria = function(index){
                if(!angular.isUndefined($scope.cuentasBancarias)){
                    var parametros = {
                        id: $scope.id
                    };
                    var nextState = $scope.viewState;
                    RedirectService.addNext(nextState,parametros);
                    $state.transitionTo('app.socio.editarCuentaBancaria', { id: $scope.cuentasBancarias[index].id });
                }
            };

            $scope.congelarCuentaAporte = function(){
                if(!angular.isUndefined($scope.socio)){
                    SocioService.congelarCuentaAporte($scope.socio.id).then(
                        function(data){
                            $scope.loadCuentaAporte();
                            $scope.alerts = [{ type: "success", msg: "Cuenta de aportes congelada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Error al congelar cuenta, intente nuevamente."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.descongelarCuentaAporte = function(){
                if(!angular.isUndefined($scope.socio)){
                    SocioService.descongelarCuentaAporte($scope.socio.id).then(
                        function(data){
                            $scope.loadCuentaAporte();
                            $scope.alerts = [{ type: "success", msg: "Cuenta de aportes descongelada."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.alerts = [{ type: "warning", msg: "Error al descongelar cuenta, intente nuevamente."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.inactivarSocio = function(){
                if(!angular.isUndefined($scope.socio)){
                    if(!angular.isUndefined($scope.cuentasBancarias)){
                        for(var i = 0; i < $scope.cuentasBancarias.length; i++){
                            if($scope.cuentasBancarias[i].estado != 'INACTIVO'){
                                $scope.alerts = [{ type: "warning", msg: "Error: debe de desactivar todas las cuentas bancarias."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                                return;
                            }
                        }
                        $state.transitionTo("app.socio.contratoInactivadoSocio", { id: $scope.socio.id });
                    }
                }
            };

            $scope.addBeneficiario = function(){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/cuentaBancaria/beneficiarioPopUp.html',
                    controller: "BeneficiarioPopUpController",
                    resolve: {
                        total: function () {
                            var tot = 0;
                            if(!angular.isUndefined($scope.beneficiarios))
                                for(var i = 0; i < $scope.beneficiarios.length; i++)
                                    tot = tot + $scope.beneficiarios[i].porcentajeBeneficio;
                            return tot;
                        },
                        obj: function(){
                            return undefined;
                        }
                    }
                });
                modalInstance.result.then(function (result) {
                    SocioService.addBeneficiario($scope.id, result).then(
                        function(data){
                            SocioService.getBeneficiario($scope.id, data.id).then(function(beneficiario){
                                $scope.beneficiarios.push(beneficiario);
                            });
                            $scope.alerts = [{ type: "success", msg: "Beneficiario creado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };
            $scope.deleteBeneficiario = function(index){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    SocioService.eliminarBeneficiario($scope.id, $scope.beneficiarios[index].id).then(
                        function(data){
                            $scope.beneficiarios.splice(index, 1);
                            $scope.alerts = [{ type: "success", msg: "Beneficiario eliminado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };
            $scope.editBeneficiario = function(index){
                var modalInstance = $modal.open({
                    templateUrl: 'views/cajero/cuentaBancaria/beneficiarioPopUp.html',
                    controller: "BeneficiarioPopUpController",
                    resolve: {
                        total: function () {
                            var tot = 0;
                            if(!angular.isUndefined($scope.beneficiarios))
                                for(var i = 0; i < $scope.beneficiarios.length; i++)
                                    tot = tot + $scope.beneficiarios[i].porcentajeBeneficio;
                            return tot;
                        },
                        obj: function(){
                            return $scope.beneficiarios[index];
                        }
                    }
                });
                modalInstance.result.then(function (result) {
                	SocioService.actualizarBeneficiario($scope.id, $scope.beneficiarios[index].id, result).then(
                        function(data){ 
                            $scope.beneficiarios.splice(index, 1);
                            $scope.beneficiarios.push(result);
                            $scope.alerts = [{ type: "success", msg: "Beneficiario Actualizado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = true;
                            $scope.control.beneficiario.message = '<span class="label label-success">Actualizado</span>';
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $scope.control.beneficiario.success = false;
                            $scope.control.beneficiario.message = '';
                            $window.scrollTo(0,0);
                        }
                    );
                }, function () {
                });
            };

            $scope.imprimirCartilla = function(){
                var restApiUrl = ConfiguracionService.getRestApiUrl();
                $window.open(restApiUrl+'/socios/'+ $scope.id+'/cartilla');
            };

        }]);
});