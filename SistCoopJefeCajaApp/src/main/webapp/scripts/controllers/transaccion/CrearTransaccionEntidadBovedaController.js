define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionEntidadBovedaController', ['$scope','$state','BovedaService','EntidadService',
        function($scope,$state,BovedaService,EntidadService) {

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.combo = {
                entidad: undefined,
                boveda: undefined,
                origen: ["INGRESO", "EGRESO"]
            };

            $scope.view = {
                idEntidad: undefined,
                idBoveda: undefined,
                detalle: undefined,
                origen: undefined
            };

            $scope.loadEntidades = function(){
                EntidadService.getEntidades().then(function(data){
                    $scope.combo.entidad = data;
                });
            };
            
            $scope.loadBovedas = function(){
                BovedaService.getBovedas($scope.agenciaSession.id).then(function(data){
                    $scope.combo.boveda = data;
                });
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
            
            $scope.loadDetalleBoveda = function(){
                BovedaService.getDetalle($scope.view.idBoveda).then(function(data){
                   $scope.view.detalle = data;
                   for(var i = 0; i < $scope.view.detalle.length; i++){
                       $scope.view.detalle[i].cantidad = 0;
                   }
                });
            };

            $scope.crearTransaccion = function(){
                if($scope.crearTransaccionBovedaCajaForm.$valid){

                    var origin;
                    if(!angular.isUndefined($scope.view.origen)){
                        if($scope.view.origen == "ENTRADA")
                            origin = "ENTIDAD";
                        else
                            origin = "BOVEDA";
                    } else {
                        origin = undefined;
                    }

                    BovedaService.crearTransaccioEntidadBoveda(origin, $scope.view.idEntidad, $scope.view.idBoveda, $scope.view.detalle).then(
                        function(data){
                            $state.transitionTo('app.transaccion.voucherTransaccionEntidadBoveda', {id: data.id});
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

            $scope.loadEntidades();
            $scope.loadBovedas();

        }]);
});