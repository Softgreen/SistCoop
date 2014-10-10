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
                boveda: undefined
            };

            $scope.view = {
                idEntidad: undefined,
                idBoveda: undefined,
                detalle: undefined
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
            $scope.loadDetalleBoveda = function(){
                BovedaService.getDetalle($scope.view.idBoveda).then(function(data){
                   $scope.view.detalle = data;
                   for(var i = 0; i < $scope.view.detalle.length; i++){
                       $scope.view.detalle[i].cantidad = 0;
                   }
                });
            };

            $scope.crearTransaccion = function(){

            };

            $scope.loadEntidades();
            $scope.loadBovedas();

        }]);
});