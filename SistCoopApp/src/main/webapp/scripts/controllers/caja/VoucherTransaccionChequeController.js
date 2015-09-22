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
                $scope.imprimir = function(){
                    if (notReady()) {return;}

                    qz.append("\x1B\x40");															//reset printer
                    qz.append("\x1B\x21\x08");														//texto en negrita
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                    qz.append("MULTIVALORES DEL SUR\r\n");											// \r\n salto de linea

                    qz.append("COBRO DE CHEQUE" + "\r\n");
                    // \t tabulador
                    qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                    qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                    qz.append(($scope.transaccion.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.transaccion.idTransaccionBancaria) + "\r\n");
                    qz.append("CAJA:" + ($scope.transaccion.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.transaccion.numeroOperacion) + "\r\n");
                    qz.append("FECHA:" + ($filter('date')($scope.transaccion.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccion.hora, 'HH:mm:ss')) + "\r\n");
                    qz.append("NUM. CHEQUE:" + ($scope.transaccion.numeroCheque) + "\r\n");
                    qz.append("TIP. DOC:" + ($scope.transaccion.tipoDocumento.abreviatura) + "\t" + "NUM. DOC.:" + ($scope.transaccion.numeroDocumento) + "\r\n");
//                    qz.append("NUM DOCUMENTO:" + ($scope.transaccion.numeroDocumento) + "\r\n");
                    qz.append("CLIENTE:" + ($scope.transaccion.socio) + "\r\n");
                    qz.append("MONTO:" + "\t" + $scope.transaccion.moneda.simbolo + $filter('number')($scope.transaccion.monto, 2) + "\r\n");

                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                    qz.append("Gracias por su preferencia" + "\r\n");
                    qz.append("Verifique su dinero antes de retirarse  de ventanilla" + "\r\n");

                    qz.append("\x1D\x56\x41");														//cortar papel
                    qz.append("\x1B\x40");
                    qz.print();
                };
            };

        }]);
});
