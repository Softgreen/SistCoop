define(['../../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearTransaccionChequeController', ["$scope", "$state", "$window", "$filter", "$modal","focus","CuentaBancariaService","SessionService","MonedaService",
        function($scope, $state, $window, $filter, $modal,focus, CuentaBancariaService, SessionService, MonedaService) {

            $scope.view = {
                numeroCheque: undefined,
                monto: undefined,
                observacion: undefined
            };

            $scope.buscarCheque = function(){
                CuentaBancariaService.getChequeByNumeroChequeUnico($scope.view.numeroCheque).then(
                    function(data){
                        $scope.cheque = data;
                    }, function error(error){
                        $scope.alerts.push({ type: "danger", msg: "Cheque no encontrado."});
                    }
                );
            };

            $scope.buscarCuentaBancaria = function(){
                CuentaBancariaService.getCuentaBancariaByNumeroChequeUnico($scope.view.numeroCheque).then(
                    function(data){
                        $scope.cuentaBancaria = data;

                        CuentaBancariaService.getTitulares($scope.cuentaBancaria.id).then(
                            function(data){
                                $scope.titulares = data;
                            }
                        );
                    }, function error(error){
                        $scope.alerts.push({ type: "danger", msg: "Cheque no encontrado."});
                    }
                );
            };

            $scope.buscar = function($event){
                if(!angular.isUndefined){
                    $event.preventDefault();
                }
                $scope.buscarCheque();
                $scope.buscarCuentaBancaria();
            };


            //firmas
            $scope.showFirma = function(index){
                if(!angular.isUndefined($scope.titulares)){
                    if(!angular.isUndefined($scope.titulares[index])){
                        var modalInstance = $modal.open({
                            templateUrl: 'views/cajero/util/firmaPopUp.html',
                            controller: "FirmaPopUpController",
                            resolve: {
                                idPersonas: function () {
                                    var idPersonas = [];
                                    idPersonas.push($scope.titulares[index].personaNatural.id);
                                    return idPersonas;
                                },
                                nombres: function(){
                                    var nombres = [];
                                    nombres.push($scope.titulares[index].personaNatural.apellidoPaterno+" "+$scope.titulares[index].personaNatural.apellidoMaterno+","+$scope.titulares[index].personaNatural.nombres);
                                    return nombres;
                                }
                            }
                        });
                        modalInstance.result.then(function (cuenta) {
                        }, function () {
                        });
                    }
                }
            };
            $scope.showFirmaTodos = function(){
                if(!angular.isUndefined($scope.titulares)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/firmaPopUp.html',
                        controller: "FirmaPopUpController",
                        resolve: {
                            idPersonas: function () {
                                var idPersonas = [];
                                for(var i = 0; i < $scope.titulares.length; i++)
                                    idPersonas.push($scope.titulares[i].personaNatural.id);
                                return idPersonas;
                            },
                            nombres: function(){
                                var nombres = [];
                                for(var i = 0; i < $scope.titulares.length; i++)
                                    nombres.push($scope.titulares[i].personaNatural.apellidoPaterno+" "+$scope.titulares[i].personaNatural.apellidoMaterno+","+$scope.titulares[i].personaNatural.nombres);
                                return nombres;
                            }
                        }
                    });
                    modalInstance.result.then(function (cuenta) {
                    }, function () {
                    });
                }
            };

            $scope.crearTransaccion = function(){

            };


        }]);
});
