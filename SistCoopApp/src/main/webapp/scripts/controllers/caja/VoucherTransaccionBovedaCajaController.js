define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionBovedaCajaController', ['$scope', "$state", '$filter','CajaService','RedirectService',
        function($scope, $state, $filter, CajaService, RedirectService) {

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    CajaService.getVoucherTransaccionBovedaCaja($scope.id).then(
                        function(data){
                            $scope.transaccionBovedaCaja = data;
                        },
                        function error(error){
                            alert("Transaccion no encontrada");
                        }
                    );
                };
            };
            $scope.loadVoucher();

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.caja.createTransaccionBovedaCaja');
                }
            };

            $scope.imprimir = function(){
                if (notReady()) {return;}														//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append("TRANSACCION BOVEDA/CAJA " + "\r\n");
                																				// \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append("AGENCIA:" + ($scope.transaccionBovedaCaja.agenciaAbreviatura) + "\r\n");
                qz.append("TRANS:" + "\t" + ($scope.transaccionBovedaCaja.id) + "\r\n");
                qz.append("ORIGEN:" + "\t" + ($scope.transaccionBovedaCaja.cajaDenominacion) + "(" + $scope.transaccionBovedaCaja.cajaAbreviatura + ")" + "\r\n");
                qz.append("DESTINO:" + "BOVEDA " + ($scope.transaccionBovedaCaja.moneda.denominacion) + "\r\n");
                qz.append("FECHA:" + "\t" + ($filter('date')($scope.transaccionBovedaCaja.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionBovedaCaja.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("MONEDA:" + "\t" + ($scope.transaccionBovedaCaja.moneda.denominacion) + "(" + $scope.transaccionBovedaCaja.moneda.simbolo + ")" + "\r\n");
                qz.append("MONTO:" + "\t" + ($filter('currency')($scope.transaccionBovedaCaja.monto, $scope.transaccionBovedaCaja.moneda.simbolo)) + "\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
				qz.append("_______________" + "\t\t" + "_______________" + "\r\n");
				qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Firma Cajero" + "\t\t" + "Firma Jefe Caja" + "\r\n");
                
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

        }]);
});