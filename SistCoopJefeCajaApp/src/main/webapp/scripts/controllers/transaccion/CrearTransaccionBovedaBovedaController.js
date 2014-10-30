define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionBovedaBovedaController', ['$scope','$state','BovedaService','AgenciaService',
        function($scope,$state,BovedaService,AgenciaService) {

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
                idAgenciaOrigen: undefined,
                idBovedaOrigen: undefined,
                idAgenciaDestino: undefined,
                idBovedaDestino: undefined
            };

            $scope.objetosCargados = {
                agenciaOrigen: undefined,
                bovedasOrigen: undefined,
                detalleBovedaOrigen: [],
                detalleDisponibleEnOrigen:[],
                agenciasDestino: undefined,
                bovedasDestino: undefined
            };

            $scope.loadAgenciaOrigen = function(){
                $scope.objetosCargados.agenciaOrigen = $scope.agenciaSession;
            };
            $scope.loadAgenciasDestino = function(){
                AgenciaService.getAgencias(true).then(function(data){
                    $scope.objetosCargados.agenciasDestino = data;
                });
            };
            $scope.loadBovedasOrigen = function(){
                BovedaService.getBovedas($scope.objetosCargados.agenciaOrigen.id).then(function(data){
                    $scope.objetosCargados.bovedasOrigen = data;
                });
            };
            
            $scope.loadBovedasDestino = function(){
                BovedaService.getBovedas($scope.view.idAgenciaDestino).then(function(data){
                    $scope.objetosCargados.bovedasDestino = data;
                });
            };
                       
            $scope.loadDetalleBovedaOrigen = function(){
                if(!angular.isUndefined($scope.view.idBovedaOrigen)){
                    BovedaService.getDetalle($scope.view.idBovedaOrigen).then(function(data){
                    	$scope.objetosCargados.detalleDisponibleEnOrigen = data;
                    	
                    	$scope.objetosCargados.detalleBovedaOrigen = angular.copy(data);
                        for(var i = 0; i < $scope.objetosCargados.detalleBovedaOrigen.length; i++){
                        	$scope.objetosCargados.detalleBovedaOrigen[i].cantidad = 0;
                        }
                    },
                    function error(error){
                        $scope.objetosCargados.detalleBovedaOrigen = [];
                        $scope.alerts = [{ type: "danger", msg: "Error: " + error.data + "."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                    );
                }
            };
            
            $scope.loadAgenciaOrigen();
            $scope.loadAgenciasDestino();
            $scope.loadBovedasOrigen();


            $scope.getBovedaOrigen = function(){
                if(!angular.isUndefined($scope.view.idBovedaOrigen)) {
                    for(var i = 0; i < $scope.objetosCargados.bovedasOrigen.length; i++){
                        if($scope.objetosCargados.bovedasOrigen[i].id == $scope.view.idBovedaOrigen){
                            return $scope.objetosCargados.bovedasOrigen[i];
                        }
                    }
                }
            };
            $scope.getBovedaDestino = function() {
                if(!angular.isUndefined($scope.view.idBovedaDestino)) {
                    for(var i = 0; i < $scope.objetosCargados.bovedasDestino.length; i++){
                        if($scope.objetosCargados.bovedasDestino[i].id == $scope.view.idBovedaDestino){
                            return $scope.objetosCargados.bovedasDestino[i];
                        }
                    }
                }
            };

            $scope.crearTransaccion = function() {
                var monedaOrigen = $scope.getBovedaOrigen();
                var monedaDestino = $scope.getBovedaDestino();

                if($scope.formCrearTransaccionBovedaCaja.$valid
                    && $scope.view.idBovedaOrigen != $scope.view.idBovedaDestino
                    && !angular.isUndefined(monedaOrigen) && !angular.isUndefined(monedaDestino)
                    && monedaOrigen.moneda.id == monedaDestino.moneda.id){

                	var det = [];
                	for(var i = 0; i<$scope.objetosCargados.detalleBovedaOrigen.length; i++){
                		det[i] = {
                			valor: $scope.objetosCargados.detalleBovedaOrigen[i].valor,
                			cantidad: $scope.objetosCargados.detalleBovedaOrigen[i].cantidad
                		};
                	}
                	
                    BovedaService.crearTransaccioBovedaBoveda($scope.view.idBovedaOrigen, $scope.view.idBovedaDestino, det).then(
                        function(data){
                            $state.transitionTo('app.transaccion.voucherTransaccionBovedaBoveda', {id: data.id});
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
            
            $scope.totalTransaccion = function(){
                var total = 0;
                for(var i = 0; i<$scope.objetosCargados.detalleBovedaOrigen.length; i++){
                    total = total + ($scope.objetosCargados.detalleBovedaOrigen[i].valor * $scope.objetosCargados.detalleBovedaOrigen[i].cantidad);
                }
                return total;
            };
            
            $scope.totalDisponibleBoveda = function(){
                var totalDisponible = 0;
                for(var i = 0; i<$scope.objetosCargados.detalleDisponibleEnOrigen.length; i++){
                    totalDisponible = totalDisponible + ($scope.objetosCargados.detalleDisponibleEnOrigen[i].valor * $scope.objetosCargados.detalleDisponibleEnOrigen[i].cantidad);
                }
                return totalDisponible;
            };
            
            $scope.cancelar = function(){
                $state.transitionTo('app.transaccion.buscarTransaccionBovedaBoveda');
            };

        }]);
});