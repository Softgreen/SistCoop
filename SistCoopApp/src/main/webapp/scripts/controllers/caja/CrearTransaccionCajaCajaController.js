define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionCajaCajaController', ['$scope', '$state','$window', '$filter','focus', 'CajaService','AgenciaService','SessionService',
        function($scope, $state, $window, $filter, focus, CajaService,AgenciaService, SessionService) {

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
                monedas: undefined
            };

            $scope.view = {
                idCaja: undefined,
                idMoneda: undefined,
                monto: undefined,
                observacion: undefined
            };

            $scope.getMoneda = function(){
              if(!angular.isUndefined($scope.view.idMoneda) && !angular.isUndefined($scope.combo.monedas)){
                  for(var i = 0; i<$scope.combo.monedas.length;i++){
                      if($scope.view.idMoneda == $scope.combo.monedas[i].id)
                        return $scope.combo.monedas[i];
                  }
                  return undefined;
              } else {
                  return undefined;
              }
            };
            $scope.loadMonedas = function(){
                CajaService.getMonedas($scope.cajaSession.id).then(
                    function(monedas){
                        $scope.combo.monedas = monedas;
                    }
                );
            };
            $scope.loadCajasOfAgencia = function(){
                AgenciaService.getCajas($scope.agenciaSession.id).then(
                    function(cajas){
                        $scope.combo.cajas = cajas;
                    }
                );
            };
            $scope.loadMonedas();
            $scope.loadCajasOfAgencia();

            $scope.crearTransaccion = function(){
                if ($scope.formCrearTransaccionCajaCaja.$valid) {
                    $scope.buttonDisableState = true;
                    SessionService.crearTransaccionCajaCaja($scope.view.idCaja, $scope.view.idMoneda, $scope.view.monto, $scope.view.observacion).then(
                        function(data){
                            alert("okkk");
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            $window.scrollTo(0,0);
                        }
                    );
                    $scope.buttonDisableState = false;
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };
            $scope.buttonDisableState = function(){
                return $scope.control.inProcess;
            };

        }]);
});