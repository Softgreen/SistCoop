define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherTransaccionEntidadBovedaController', ['$scope', "$state", '$filter','BovedaService',
        function($scope, $state, $filter, BovedaService) {

            $scope.objetosCargados = {
                detalleTransaccion: []
            };

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.getVoucherTransaccionEntidadBoveda($scope.id).then(
                        function(data){
                            $scope.transaccionEntidadBoveda = data;
                        },
                        function error(){
                            alert("No se pudo cargar el voucher");
                        }
                    );
                };
            };
            $scope.loadVoucher();

            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                $state.transitionTo('app.transaccion.nuevaTransaccionEntidadBoveda');
            };

            $scope.imprimir = function(){
                if (notReady()) {return;}														//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("C.A.C. CAJA VENTURA \r\n");											// \r\n salto de linea
                qz.append("TRANSACCION ENTIDAD/BOVEDA " + "\r\n");

                // \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append("AGENCIA:" + " " + ($scope.transaccionEntidadBoveda.agenciaDenominacion) + "\r\n");
                qz.append("TRANS:" + "\t" + " " + ($scope.transaccionEntidadBoveda.id) + "\r\n");
                qz.append("T.TRANSACCION:" + "\t" + " " + ($scope.transaccionEntidadBoveda.tipoTransaccion) + "\r\n");
                qz.append("FECHA:" + "\t" + " " + ($filter('date')($scope.transaccionEntidadBoveda.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionEntidadBoveda.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("MONEDA:" + "\t" + " " + ($scope.transaccionEntidadBoveda.moneda.denominacion) + "(" + $scope.transaccionEntidadBoveda.moneda.simbolo + ")" + "\r\n");

                /*
                for(var i = 0 ; i < $scope.objetosCargados.detalleTransaccion.length ; i++){
                    qz.append(  ($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor, 'S/.')) + $scope.objetosCargados.detalleTransaccion[i].cantidad + ($filter('currency')($scope.objetosCargados.detalleTransaccion[i].valor * $scope.objetosCargados.detalleTransaccion[i].cantidad, 'S/.')) + "\t" +  "\r\n");
                }*/

                qz.append("MONTO:" + "\t"+ " " + ($filter('currency')($scope.transaccionEntidadBoveda.monto, $scope.transaccionEntidadBoveda.moneda.simbolo)) + "\r\n");
                qz.append("ESTADO:" + ($scope.transaccionEntidadBoveda.estado ? 'ACTIVO' : 'INACTIVO') + "\r\n");

                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("____________" + "\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Firma" + "\r\n");

                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

        }]);
});