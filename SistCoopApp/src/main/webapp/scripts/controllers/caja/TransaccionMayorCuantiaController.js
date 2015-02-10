define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TransaccionMayorCuantiaController', ["$scope", "$state", "$filter", "CajaService","CuentaBancariaService", "RedirectService",
        function($scope, $state, $filter, CajaService, CuentaBancariaService, RedirectService) {

            CajaService.getVoucherTransaccionBancaria($scope.id).then(
                function(data){
                    $scope.transaccion = data;
                },
                function error(error){
                    alert("Transaccion Cuenta Bancaria no encontrada");
                }
            );

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.transaccion.depositoRetiro');
                }
            };

            $scope.mayorCuantia = function(){
                $state.transitionTo('app.transaccion.transaccionMayorCuantia', {id: $scope.id});
            };

            $scope.imprimir = function(){
                alert("imprimiendo");
            };
        }]);
});