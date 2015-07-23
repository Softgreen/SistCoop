define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionBovedaCajaController', ['$scope', "$state", '$filter','BovedaService',
        function($scope, $state, $filter, BovedaService) {

            $scope.objetosCargados = {
                detalleTransaccion: []
            };

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.getVoucherTransaccionBovedaCaja($scope.id).then(
                        function(data){
                            $scope.transaccionBovedaCaja = data;
                        },
                        function error(error){
                            alert("Transaccion no encontrada");
                        }
                    );

                    BovedaService.getDetalleTransaccionBovedaCaja($scope.id).then(
                        function(data){
                            $scope.objetosCargados.detalleTransaccion = data;
                        },
                        function error(error){
                            alert("Error al cargar el detalle de boveda caja");
                        }
                    );
                };
            };

            $scope.loadVoucher();

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                $state.transitionTo('app.transaccion.nuevaTransaccionBovedaCaja');
            };

            $scope.imprimir = function(){
                if (notReady()) {return;}														//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("CASA DE CAMBIOS VENTURA\r\n");											// \r\n salto de linea
                if ($scope.transaccionBovedaCaja.origen == "CAJA") {
                    qz.append("TRANSACCION CAJA/BOVEDA " + "\r\n");
                }
                if ($scope.transaccionBovedaCaja.origen == "BOVEDA") {
                    qz.append("TRANSACCION BOVEDA/CAJA " + "\r\n");
                }
                // \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append("AGENCIA:" + " " + ($scope.transaccionBovedaCaja.agenciaAbreviatura) + "\r\n");
                qz.append("TRANS:" + "\t" + " " + ($scope.transaccionBovedaCaja.id) + "\r\n");
                qz.append("ORIGEN:" + "\t" + " " + ($scope.transaccionBovedaCaja.origenTransaccion) + "\r\n");
                qz.append("DESTINO:" + " " + ($scope.transaccionBovedaCaja.destinoTransaccion) + "\r\n");
                qz.append("FECHA:" + "\t" + " " + ($filter('date')($scope.transaccionBovedaCaja.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionBovedaCaja.hora, 'HH:mm:ss')) + "\r\n");
                
                if($scope.transaccionBovedaCaja.moneda.simbolo == "€"){            
                	qz.append("MONEDA:" + "\t" + " " + ($scope.transaccionBovedaCaja.moneda.denominacion) + "(" + chr(238) + ")" + "\r\n");
                } else {
                	qz.append("MONEDA:" + "\t" + " " + ($scope.transaccionBovedaCaja.moneda.denominacion) + "(" + $scope.transaccionBovedaCaja.moneda.simbolo + ")" + "\r\n");
                }
                //qz.append("MONEDA:" + "\t" + " " + ($scope.transaccionBovedaCaja.moneda.denominacion) + "(" + $scope.transaccionBovedaCaja.moneda.simbolo + ")" + "\r\n");

                qz.append("Denominacion");
                qz.append("\t");
                qz.append("Cantidad");
                qz.append("     ");
                qz.append("Subtotal");
                qz.append("\r\n");

                for(var i = 0 ; i < $scope.objetosCargados.detalleTransaccion.length ; i++){
                    //if($scope.objetosCargados.detalleTransaccion[i].cantidad != 0){
                    //qz.append("\x1B\x21\x01"); 												//texto normal (no negrita)
                    //qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor, '')) + "\t\t" + $scope.objetosCargados.detalleTransaccion[i].cantidad + "\t" + "   " + ($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor * $scope.objetosCargados.detalleTransaccion[i].cantidad, $scope.transaccionBovedaCaja.moneda.simbolo)) +  "\r\n");

                    qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                    qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor, '')));
                    qz.append("\t\t");
                    qz.append($scope.objetosCargados.detalleTransaccion[i].cantidad.toString());
                    qz.append("\t" + "   ");
                    
                    if($scope.transaccionBovedaCaja.moneda.simbolo == "€"){            
                    	qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor * $scope.objetosCargados.detalleTransaccion[i].cantidad, chr(238))));
                    } else {
                    	qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor * $scope.objetosCargados.detalleTransaccion[i].cantidad, $scope.transaccionBovedaCaja.moneda.simbolo)));
                    }
                    //qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor * $scope.objetosCargados.detalleTransaccion[i].cantidad, $scope.transaccionBovedaCaja.moneda.simbolo)));
                    qz.append("\r\n");
                    //}
                }

                qz.append(String.fromCharCode(27) + "\x61" + "\x32");							//texto a la derecha
                
                if($scope.transaccionBovedaCaja.moneda.simbolo == "€"){            
                	qz.append("MONTO TRANSACCION: " + ($filter('currency')($scope.transaccionBovedaCaja.monto, chr(238))) + "\r\n");
                } else {
                	qz.append("MONTO TRANSACCION: " + ($filter('currency')($scope.transaccionBovedaCaja.monto, $scope.transaccionBovedaCaja.moneda.simbolo)) + "\r\n");
                }
                //qz.append("MONTO TRANSACCION: " + ($filter('currency')($scope.transaccionBovedaCaja.monto, $scope.transaccionBovedaCaja.moneda.simbolo)) + "\r\n");
                qz.append("\r\n");
                if ($scope.transaccionBovedaCaja.estadoSolicitud) {
                    qz.append("ESTADO SOLICTUD:" + " " + "SOLICITADO" + "\r\n");
                }else{
                    qz.append("ESTADO SOLICTUD:" + " " + "CANCELADO" + "\r\n");
                }
                if ($scope.transaccionBovedaCaja.estadoConfirmacion) {
                    qz.append("ESTADO CONFIRMACION:" + " " + "CONFIRMADO" + "\r\n");
                }else{
                    qz.append("ESTADO CONFIRMACION:" + " " + "NO CONFIRMADO" + "\r\n");
                }

                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("____________" + "\t\t" + "_______________" + "\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Firma Cajero" + "\t\t" + "Firma Jefe Caja" + "\r\n");
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

        }]);
});