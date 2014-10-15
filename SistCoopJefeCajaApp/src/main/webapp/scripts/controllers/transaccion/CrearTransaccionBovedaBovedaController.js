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
                detalleBovedaOrigen: undefined,

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
                        for(var i = 0; i<data.length; i++){
                            data[i].cantidad = 0;
                        }
                        $scope.objetosCargados.detalleBovedaOrigen = data;
                    });
                }
            };
            $scope.loadAgenciaOrigen();
            $scope.loadAgenciasDestino();
            $scope.loadBovedasOrigen();


            $scope.crearTransaccion = function() {
                if($scope.formCrearTransaccionBovedaCaja.$valid){

                } else {
                    $scope.control.submitted = true;
                }
            };

        }]);
});