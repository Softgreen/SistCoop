define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherCerrarBoveda', ['$scope','$state','BovedaService',
        function($scope,$state,BovedaService) {
    		
    		$scope.objetosCargados = {
    				detalleBoveda: []
    		};

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.getVoucherCerrarBoveda($scope.id).then(
                        function(data){
                            $scope.cerrarBovedaVoucher = data;
                        },
                        function error(error){
                            alert("Boveda no encontrada");
                        }
                    );
/*
                    BovedaService.getDetalle($scope.id).then(
                        function(data){
                            $scope.objetosCargados.detalleBoveda = data;
                        },
                        function error(error){
                            alert("Error al cargar el detalle de boveda");
                        }
                    );
                    */
                };
            };

            $scope.loadVoucher();

            $scope.salir = function(){
                $scope.redireccion();
            };
        }]);
});