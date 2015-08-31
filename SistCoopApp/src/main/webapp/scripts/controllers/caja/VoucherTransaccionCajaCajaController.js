define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionCajaCajaController', ['$scope', "$state", '$filter', 'MonedaService', 'CajaService','RedirectService',
        function($scope, $state, $filter, MonedaService,CajaService,RedirectService) {

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    CajaService.getVoucherTransaccionCajaCaja($scope.id).then(
                        function(data){
                            $scope.transaccionCajaCaja = data;
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
                    $state.transitionTo('app.caja.createTransaccionCajaCaja');
                }
            };

            $scope.imprimir = function(){
                if (notReady()) {return;}														//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("MULTISERVICIOS DEL SUR\r\n");											// \r\n salto de linea
                qz.append("TRANSACCION CAJA/CAJA " + "\r\n");
                // \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append("AGENCIA:" + " " + ($scope.transaccionCajaCaja.agenciaAbreviatura) + "\r\n");
                qz.append("TRANS:" + "\t" + " " + ($scope.transaccionCajaCaja.id) + "\r\n");
                qz.append("ORIGEN:" + "\t" + " " + ($scope.transaccionCajaCaja.cajaOrigenDenominacion) + "(" + $scope.transaccionCajaCaja.cajaOrigenAbreviatura + ")" + "\r\n");
                qz.append("DESTINO:" + " " + ($scope.transaccionCajaCaja.cajaDestinoDenominacion) + "(" + $scope.transaccionCajaCaja.cajaDestinoAbrevitura + ")" + "\r\n");
                qz.append("FECHA:" + "\t" + " " + ($filter('date')($scope.transaccionCajaCaja.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionCajaCaja.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("MONEDA:" + "\t" + " " + ($scope.transaccionCajaCaja.moneda.denominacion) + "(" + $scope.transaccionCajaCaja.moneda.simbolo + ")" + "\r\n");
                qz.append("MONTO:" + "\t" + " " + ($filter('currency')($scope.transaccionCajaCaja.monto, $scope.transaccionCajaCaja.moneda.simbolo)) + "\r\n");
                if ($scope.transaccionCajaCaja.estadoSolicitud) {
                	qz.append("ESTADO SOLICTUD:" + " " + "SOLICITADO" + "\r\n");
				}else{
					qz.append("ESTADO SOLICTUD:" + " " + "CANCELADO" + "\r\n");
				}
                if ($scope.transaccionCajaCaja.estadoConfirmacion) {
                	qz.append("ESTADO CONFIRMACION:" + " " + "CONFIRMADO" + "\r\n");
				}else{
					qz.append("ESTADO CONFIRMACION:" + " " + "NO CONFIRMADO" + "\r\n");
				}
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
				qz.append("_________________  " + "  __________________" + "\r\n");
				qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Firma Caja Origen  " + "  Firma Caja Destino" + "\r\n");

                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

        }]);
});
