define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionBovedaCajaController', ['$scope','$state', '$filter','focus', "MonedaService", "CajaService","SessionService",
        function($scope,$state,$filter,focus,MonedaService,CajaService,SessionService) {

            $scope.focusElements = {
                boveda: 'focusBoveda'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus($scope.focusElements.boveda);
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
              idBoveda: undefined
            };

            $scope.combo = {
                boveda: undefined
            };

            $scope.objetosCargados = {
                detalles: []
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
            $scope.loadBovedas = function(){
                CajaService.getBovedas($scope.cajaSession.id).then(
                    function(data){
                        $scope.combo.boveda = data;
                    }
                );
            };
            $scope.loadBovedas();

            $scope.loadDetalleBoveda = function(){
                if(!angular.isUndefined($scope.view.idBoveda)){
                    MonedaService.getDenominaciones($scope.view.idBoveda).then(
                        function(data){
                            $scope.objetosCargados.detalles = data;
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

            $scope.total = function(){
                var total = 0;
                for(var i = 0; i<$scope.objetosCargados.detalles.length; i++){
                    total = total + ($scope.objetosCargados.detalles[i].valor * $scope.objetosCargados.detalles[i].cantidad);
                }
                return total;
            };

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionBovedaCaja.$valid && ($scope.total() != 0 && !angular.isUndefined($scope.total()))) {
                    $scope.control.inProcess = true;

                    var transaccion = [];
                    for(var i = 0; i<$scope.objetosCargados.detalles.length; i++){
                        transaccion[i] = {
                            valor: $scope.objetosCargados.detalles[i].valor,
                            cantidad: $scope.objetosCargados.detalles[i].cantidad
                        }
                    }

                    SessionService.crearTransaccionBovedaCajaOrigenCaja($scope.view.idBoveda, transaccion).then(
                        function(data){
                            $scope.control.inProcess = false;
                            $scope.control.success = true;
                            //redireccion al voucher
                            $state.transitionTo('app.caja.voucherTransaccionBovedaCaja', { id: data.id});
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
                    $scope.alerts = [{ type: "danger", msg: "Error: Monto de transaccion invalido."}];
                    $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                }
            };

            $scope.cancelar = function(){
                $state.transitionTo('app.caja.buscarTransaccionBovedaCaja');
            };

        }]);
});