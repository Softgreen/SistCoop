define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarChequeraController', [ "$scope", "$state", "$modal", "focus", "CuentaBancariaService",
        function($scope, $state, $modal, focus, CuentaBancariaService) {

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


            $scope.anularCheque = function(cheque){
                var modalInstance = $modal.open({
                    templateUrl: 'views/util/confirmPopUp.html',
                    controller: "ConfirmPopUpController"
                });
                modalInstance.result.then(function (result) {
                    CuentaBancariaService.anularCheque($scope.id, cheque.numeroChequeUnico).then(
                        function(data){
                            cheque.estado = "ANULADO";
                            $scope.alerts = [{ type: "success", msg: "Cheque anulado." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error:" + error.data.message +"." }];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {
                });
            };

        }]);
});