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
                console.log("cargando");
            };

            $scope.loadEntidades();
            $scope.loadBovedas();

        }]);
});