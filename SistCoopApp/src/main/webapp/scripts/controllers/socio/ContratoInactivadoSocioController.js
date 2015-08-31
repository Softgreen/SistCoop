define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('ContratoInactivadoSocioController', [ '$scope','$state','$filter','SessionService','CajaService','SocioService',
        function($scope,$state,$filter,SessionService,CajaService,SocioService) {

            $scope.view = {
                condiciones: false
            };

            $scope.siguiente = function(){
                if(!angular.isUndefined($scope.id)){
                    if($scope.view.condiciones == true)
                        $scope.inactivarSocio();
                    else
                        alert("Acepte los términos y condiciones");
                }
            };

            $scope.inactivarSocio = function(){
                if(!angular.isUndefined($scope.id)){
                    SessionService.inactivarSocioConRetiro($scope.id).then(
                        function(data){
                            var idTransaccion = data.id;
                            $state.transitionTo("app.socio.voucherCancelacionCuenta", { id:$scope.id, idTransaccion:idTransaccion });
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };

            $scope.loadVoucherRetiroCuentaAporte = function(){
                if(!angular.isUndefined($scope.idTransaccion)){
                    CajaService.getVoucherCuentaAporte($scope.idTransaccion).then(
                        function(data){
                            $scope.transaccion = data;
                        }
                    );
                }
            };
            $scope.loadVoucherRetiroCuentaAporte();

            $scope.imprimir = function(){
                if(angular.isUndefined($scope.transaccion))
                    return;
                if (notReady()) {return;}

                qz.findPrinter("EPSON TM-U220");												//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("MULTISERVICIOS DEL SUR \r\n");											// \r\n salto de linea

                qz.append(($scope.transaccion.tipoTransaccion) + " CUENTA " + ($scope.transaccion.tipoCuentaBancaria) + "\r\n");
                // \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append(($scope.transaccion.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.transaccion.idTransaccionBancaria) + "\r\n");
                qz.append("CAJA:" + "\t" + ($scope.transaccion.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.transaccion.numeroOperacion) + "\r\n");
                qz.append("FECHA:" + "\t" + ($filter('date')($scope.transaccion.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccion.hora, 'HH:mm:ss')) + "\r\n");
                qz.append("CUENTA:" + "\t" + ($scope.transaccion.numeroCuenta) + "\r\n");
                qz.append("SOCIO:" + "\t" + ($scope.transaccion.socio) + "\r\n");
                qz.append("MONEDA:" + "\t" + ($scope.transaccion.moneda.denominacion) + "\r\n");

                if(!angular.isUndefined($scope.transaccion.referencia))
                    qz.append("REF:" + "\t" + ($scope.transaccion.referencia) + "\r\n");
                else{
                    qz.append(" ");
                }

                if (($scope.transaccion.tipoTransaccion)=="DEPOSITO") {
                    qz.append("\r\n");
                    qz.append("IMPORTE RECIBIDO:" + "\t" + ($filter('currency')($scope.transaccion.monto, $scope.transaccion.moneda.simbolo)) + "\r\n");
                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                    qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");
                } else {
                    qz.append("\r\n");
                    qz.append("IMPORTE PAGADO:" + "\t\t" + ($filter('currency')($scope.transaccion.monto, $scope.transaccion.moneda.simbolo)) + "\r\n");
                    qz.append("SALDO DISPONIBLE:" + "\t" + ($filter('currency')($scope.transaccion.saldoDisponible, $scope.transaccion.moneda.simbolo)) + "\r\n");
                    qz.append("\r\n");
                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                    qz.append("_________________" + "\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                    qz.append("Firma Titular(es)" + "\r\n");
                    qz.append("\r\n");
                    qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                    qz.append("Verifique su dinero antes de retirarse  de ventanilla" + "\r\n");
                }
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

            $scope.cancelar = function(){
                $state.transitionTo("app.socio.panelSocio", { id: $scope.id });
            };
        }]);
});
