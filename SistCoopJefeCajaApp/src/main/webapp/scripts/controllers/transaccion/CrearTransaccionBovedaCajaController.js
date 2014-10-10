define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionBovedaCajaController', ['$scope','$state','BovedaService', 'AgenciaService', 'focus', '$window', 'CajaService', 'MonedaService', 'SessionService',
        function($scope, $state, BovedaService, AgenciaService, focus, $window, CajaService, MonedaService, SessionService) {
    		
    		$scope.setInitialFocus = function($event){
    			if(!angular.isUndefined($event))
    				$event.preventDefault();
    			focus('focusCaja');
    			$window.scrollTo(0, 0);
    		};
    		
    		$scope.setInitialFocus();
    	
            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };
            
            $scope.combo = {
                cajas: undefined,
                boveda: undefined
            };

            $scope.view = {
            	idCaja: undefined,
            	idBoveda: undefined
            };
            

            $scope.loadCajasOfAgencia = function(){
                AgenciaService.getCajas($scope.agenciaSession.id).then(
                    function(cajas){
                        $scope.combo.cajas = cajas;
                    }
                );
            };
            
            $scope.loadCajasOfAgencia();
            
            $scope.loadBovedas = function(){
                CajaService.getBovedas($scope.view.idCaja).then(
                    function(data){
                        $scope.combo.boveda = data;
                    }
                );
            };
            
            $scope.getBoveda = function(){
                if(!angular.isUndefined($scope.view.idBoveda) && !angular.isUndefined($scope.combo.boveda)){
                    for(var i = 0; i < $scope.combo.boveda.length; i++){
                        if($scope.view.idBoveda == $scope.combo.boveda[i].id)
                          return $scope.combo.boveda[i];
                    }
                    return undefined;
                } else{
                    return undefined;
                }
              };
            
            $scope.objetosCargados = {
            	detalles: [],
            	detalleDisponible: []
            };
            
            $scope.loadDetalleBoveda = function(){
                if(!angular.isUndefined($scope.view.idBoveda)){
                    
                	BovedaService.getDetalle($scope.view.idBoveda).then(
                        function(data){
                        	$scope.objetosCargados.detalleDisponible = data;
                        	
                            $scope.objetosCargados.detalles = angular.copy(data);
                            for(var i = 0; i < $scope.objetosCargados.detalles.length; i++){
                                $scope.objetosCargados.detalles[i].cantidad = 0;
                            }
                            
                        },
                        function error(error){
                            $scope.objetosCargados.detalles = [];
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            
            $scope.$watch('view.idBoveda', function(newVal, oldVal){
                if(newVal != oldVal){
                    if(!angular.isUndefined($scope.view.idBoveda)){
                        $scope.loadDetalleBoveda();
                    }
                }
            });
            
            $scope.totalDisponibleBoveda = function(){
                var totalDisponible = 0;
                for(var i = 0; i<$scope.objetosCargados.detalleDisponible.length; i++){
                    totalDisponible = totalDisponible + ($scope.objetosCargados.detalleDisponible[i].valor * $scope.objetosCargados.detalleDisponible[i].cantidad);
                }
                return totalDisponible;
            };

            $scope.totalTransaccion = function(){
                var total = 0;
                for(var i = 0; i<$scope.objetosCargados.detalles.length; i++){
                    total = total + ($scope.objetosCargados.detalles[i].valor * $scope.objetosCargados.detalles[i].cantidad);
                }
                return total;
            };
            
            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionBovedaCaja.$valid && ($scope.totalTransaccion() != 0 && !angular.isUndefined($scope.totalTransaccion()))) {
                    $scope.control.inProcess = true;

                    var transaccion = [];
                    for(var i = 0; i<$scope.objetosCargados.detalles.length; i++){
                        transaccion[i] = {
                            valor: $scope.objetosCargados.detalles[i].valor,
                            cantidad: $scope.objetosCargados.detalles[i].cantidad
                        };
                    }

                    SessionService.crearTransaccionBovedaCajaOrigenBoveda($scope.view.idBoveda, transaccion, $scope.view.idCaja).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            //redireccion al voucher
                            $state.transitionTo('app.transaccion.voucherTransaccionBovedaCaja', { id: data.id});
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            //mostrar error al usuario
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                    $scope.alerts = [{ type: "danger", msg: "Error: Monto de Transacción Invalido."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            };

            $scope.cancelar = function(){
                $state.transitionTo('app.transaccion.buscarTransaccionBovedaCaja');
            };

        }]);
});