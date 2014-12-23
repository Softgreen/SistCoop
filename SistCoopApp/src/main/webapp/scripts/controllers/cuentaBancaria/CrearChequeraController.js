define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearChequeraController', [ "$scope", "$state", "focus", "CuentaBancariaService",
        function($scope, $state, focus, CuentaBancariaService) {

            $scope.alerts = [];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};

            $scope.combo = {
                cantidad: [50, 100]
            };
            $scope.combo.selected = {
                cantidad: undefined
            };
            $scope.$watch("combo.selected.cantidad", function(){
                if(!angular.isUndefined($scope.combo.selected.cantidad)) {
                    if(!angular.isUndefined($scope.view.numeroInicio)){
                        $scope.view.numeroFin = $scope.view.numeroInicio + $scope.combo.selected.cantidad - 1;
                    }
                }
            }, true);

            $scope.today = new Date();

            $scope.view = {
                cuentaBancaria: undefined,
                chequeraUltima: undefined,
                numeroInicio: undefined,
                numeroFin: undefined,
                fechaEntrega: $scope.today,
                fechaFin: ($scope.today.getDate())
            };

            //cargar datos
            $scope.loadCuentaBancaria = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getCuentasBancaria($scope.id).then(
                        function(data){
                            $scope.view.cuentaBancaria = data;
                        }, function error(error){
                            $scope.alerts.push({ type: "danger", msg: "Cuenta bancaria no encontrada."});
                        }
                    );
                }
            };
            $scope.loadChequeraUltima = function(){
                if(!angular.isUndefined($scope.id)){
                    CuentaBancariaService.getChequeraUltima($scope.id).then(
                        function(data){
                            $scope.view.chequeraUltima = data;
                            if(angular.isUndefined(data)){
                                $scope.view.numeroInicio = 1;
                            } else {
                                $scope.view.numeroInicio = data.numeroFin + 1;
                            }
                        }, function error(error){
                            $scope.alerts.push({ type: "danger", msg: "Ultima chequera no encontrada."});
                        }
                    );
                }
            };

            $scope.loadCuentaBancaria();
            $scope.loadChequeraUltima();

            $scope.crearTransaccion = function(){
                if($scope.form.$valid){
                    CuentaBancariaService.crearChequera($scope.id, $scope.view.numeroFin - $scope.view.numeroInicio + 1).then(
                        function(data){
                            $state.transitionTo("app.socio.editarChequera", {idCuentaBancaria: $scope.id, id: data.id} );
                        }, function error(error){
                            $scope.alerts.push({ type: "danger", msg: "Chequera no creada."});
                        }
                    );
                } else {
                    $scope.form.$setDirty()
                }
            };

            $scope.salir = function(){
                $scope.redireccion();
            };

        }]);
});