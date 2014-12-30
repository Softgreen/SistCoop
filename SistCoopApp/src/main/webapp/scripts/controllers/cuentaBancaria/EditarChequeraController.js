define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarChequeraController', [ "$scope", "$state", "focus", "CuentaBancariaService",
        function($scope, $state, focus, CuentaBancariaService) {

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

            //cargar datos
            $scope.loadChequera = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getChequera($scope.idCuentaBancaria, $scope.id).then(
                        function(data){
                            $scope.chequera = data;
                            CuentaBancariaService.getCheques($scope.idCuentaBancaria, $scope.id).then(
                                function(data){
                                   $scope.cheques = data;
                                }, function error(error){
                                    $scope.alerts.push({ type: "danger", msg: "Cheques no encontrada."});
                                }
                            );
                        }, function error(error){
                            $scope.alerts.push({ type: "danger", msg: "Chequera no encontrada."});
                        }
                    );
                }
            };
            $scope.loadChequera();

        }]);
});