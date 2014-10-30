define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionBovedaBovedaController', ['$scope', "$state", '$filter','BovedaService',
        function($scope, $state, $filter, BovedaService) {

    	 	$scope.objetosCargados = {
             	detalleTransaccion: []
             };
    	
            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){ 
                	BovedaService.getVoucherTransaccionBovedaBoveda($scope.id).then(
                        function(data){
                            $scope.transaccion = data;
                        },
                        function error(error){
                            alert("Transaccion no encontrada");
                        }
                    );
                	
                	BovedaService.getDetalleTransaccionBovedaBoveda($scope.id).then(
                        function(data){
                        	$scope.objetosCargados.detalle = data;
                        },
                        function error(error){
                        	alert("Error al cargar el detalle de boveda caja");
                        }
                    );
                };
            };
            
            $scope.loadVoucher();

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
            	$state.transitionTo('app.transaccion.nuevaTransaccionBovedaBoveda');
            };

            $scope.imprimir = function(){

            };

        }]);
});
