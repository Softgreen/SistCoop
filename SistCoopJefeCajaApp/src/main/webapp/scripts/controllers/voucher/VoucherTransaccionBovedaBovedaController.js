define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionBovedaBovedaController', ['$scope', "$state", '$filter','BovedaService',
        function($scope, $state, $filter, BovedaService) {

    	 	$scope.objetosCargados = {
             	detalleTransaccion: []
             };
    	
            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){ 
                	BovedaService.getVoucherTransaccionBovedaBoveda($scope.id).then(
                        function(data){
                            $scope.transaccion = data;
                        },
                        function error(error){
                            alert("Transaccion no encontrada");
                        }
                    );
                	
                	BovedaService.getDetalleTransaccionBovedaBoveda($scope.id).then(
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
            	$state.transitionTo('app.transaccion.nuevaTransaccionBovedaBoveda');
            };

            $scope.imprimir = function(){
            	if (notReady()) {return;}														//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("CASA DE CAMBIOS VENTURA\r\n");											// \r\n salto de linea
                qz.append("TRANSACCION BOVEDA/BOVEDA " + "\r\n");
               
                // \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append("AGENCIA:" + " " + ($scope.transaccion.agenciaOrigen) + "\r\n");
                qz.append("TRANS:" + "\t" + " " + ($scope.transaccion.id) + "\r\n");
                qz.append("FECHA:" + "\t" + " " + ($filter('date')($scope.transaccion.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccion.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("ORIGEN:" + "\t" + " " + ($scope.transaccion.agenciaOrigen) + "\r\n");
                qz.append("DESTINO:" + " " + ($scope.transaccion.agenciaDestino) + "\r\n");
                qz.append("MONEDA:" + "\t" + " " + ($scope.transaccion.moneda.denominacion) + "(" + $scope.transaccion.moneda.simbolo + ")" + "\r\n");

                //Detalle de Transaccion
                qz.append("Denominacion");
                qz.append("\t");
                qz.append("Cantidad");
                qz.append("    ");
                qz.append("Subtotal");
                qz.append("\r\n");

                for(var i = 0 ; i < $scope.objetosCargados.detalleTransaccion.length ; i++){
                    qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                    qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor, '')));
                    qz.append("\t\t");
                    qz.append($scope.objetosCargados.detalleTransaccion[i].cantidad.toString());
                    qz.append("\t" + "    ");
                    qz.append(($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor * $scope.objetosCargados.detalleTransaccion[i].cantidad, $scope.transaccion.moneda.simbolo)));
                    qz.append("\r\n");
                }

                qz.append(String.fromCharCode(27) + "\x61" + "\x32");							//texto a la derecha
                qz.append("MONTO TRANSACCION: " + ($filter('currency')($scope.transaccion.monto, $scope.transaccion.moneda.simbolo)) + "\r\n");
                qz.append("\r\n");
                if ($scope.transaccion.estadoSolicitud) {
                    qz.append("ESTADO SOLICTUD:" + " " + "SOLICITADO" + "\r\n");
                }else{
                    qz.append("ESTADO SOLICTUD:" + " " + "CANCELADO" + "\r\n");
                }
                if ($scope.transaccion.estadoConfirmacion) {
                    qz.append("ESTADO CONFIRMACION:" + " " + "CONFIRMADO" + "\r\n");
                }else{
                    qz.append("ESTADO CONFIRMACION:" + " " + "NO CONFIRMADO" + "\r\n");
                }

                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("______________" + "\t\t" + "_______________" + "\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Agencia Origen" + "\t\t" + "Agencia Destino" + "\r\n");
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

        }]);
});
