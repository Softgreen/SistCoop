define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearChequeraController', [ "$scope", "$state", "focus", "CuentaBancariaService",
        function($scope, $state, focus, CuentaBancariaService) {

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

            //cargar datos
            $scope.loadCuentaBancaria = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getCuentasBancaria($scope.id).then(
                        function(data){
                            $scope.cuentaBancaria = data;
                            console.log("ok");
                        }, function error(error){
                            $scope.alerts.push({ type: "danger", msg: "Cuenta bancaria no encontrada."});
                        }
                    );
                }
            };

            $scope.loadTitulares = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getTitulares($scope.id).then(
                        function(data){
                            $scope.titulares = data;
                        }, function error(error){
                            $scope.titulares = undefined;
                            $scope.alerts.push({ type: "danger", msg: "Titulares no encontrados."});
                        }
                    );
                };
            };

            console.log("cargando");
            $scope.loadCuentaBancaria();
            $scope.loadTitulares();

            $scope.salir = function(){
                $scope.redireccion();
            };

        }]);
});