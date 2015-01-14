define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionChequeController', ["$scope", "$state", "$filter", "CajaService","CuentaBancariaService",
        function($scope, $state, $filter, CajaService, CuentaBancariaService) {


            $scope.loadVoucher = function(){
                CajaService.getVoucherTransaccionCheque($scope.id).then(
                    function(data){
                        $scope.transaccion = data;
                    },
                    function error(error){
                        alert("Transaccion Cheque no Encontrado.");
                    }
                );
            };
            $scope.loadVoucher();

            $scope.salir = function(){
                $state.transitionTo("app.transaccion.cheque");
            };

            $scope.imprimir = function() {
                console.log($scope.transaccion);
            };

        }]);
});