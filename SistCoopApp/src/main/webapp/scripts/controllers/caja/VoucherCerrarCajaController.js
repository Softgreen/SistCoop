define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherCerrarCajaController', ['$scope', "$state", '$filter', "CajaService",
        function($scope, $state, $filter, CajaService) {

            CajaService.getVoucherCierreCaja($scope.cajaSession.id, $scope.id).then(
                function(voucher){
                    $scope.voucherByMoneda = voucher;
                }
            );
            CajaService.getResumenCierreCaja($scope.cajaSession.id, $scope.id).then(
            	function(resumen){
            		console.log("Session Caja" + $scope.cajaSession.id);
            		console.log("ID" + $scope.id);
                    $scope.resumenCaja = resumen;
                }
            );

            $scope.total = function(detalle){
                var totalVoucher = 0;
                for(var i = 0; i < detalle.length; i++){
                    totalVoucher = totalVoucher + (detalle[i].valor*detalle[i].cantidad);
                }
                return totalVoucher;
            };

            $scope.imprimirResumen = function(){

                if (notReady()) {return;}

                qz.append("\x1B\x40");
                qz.append("\x1B\x21\x08");
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("MULTISERVICIOS DEL SUR\r\n");
                qz.append("RESUMEN DE OPERACIONES\r\n");

                qz.append("\x1B\x21\x01");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");

                qz.append("AGENCIA: " + $scope.resumenCaja.agencia + "\r\n");
                qz.append("CAJA   : " + $scope.resumenCaja.caja + "\r\n");
                qz.append("FEC. APERT.: " + ($filter('date')($scope.resumenCaja.fechaApertura, 'dd/MM/yyyy'))+ " ");
                qz.append(($filter('date')($scope.resumenCaja.horaApertura, 'HH:mm:ss'))+"\r\n");
                qz.append("FEC. CIERRE: " + ($filter('date')($scope.resumenCaja.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append(($filter('date')($scope.resumenCaja.horaCierre, 'HH:mm:ss'))+"\r\n");
                qz.append("TRABAJADOR : " + $scope.resumenCaja.trabajador+"\r\n");

                qz.append("\r\n");
                qz.append("\x1B\x40");
                qz.append("\x1B\x21\x08");
                qz.append("DEPOSITOS (TOTAL): " + ($scope.resumenCaja.depositosAporte + $scope.resumenCaja.depositosAhorro + $scope.resumenCaja.depositosPlazoFijo + $scope.resumenCaja.depositosCorriente)+"\n");
                qz.append("\x1B\x21\x01");
                qz.append("C.APORTE:" + $scope.resumenCaja.depositosAporte + "\t");
                qz.append("C.AHORRO:" + $scope.resumenCaja.depositosAhorro + "\r\n");
                qz.append("C.P.FIJO:" + $scope.resumenCaja.depositosPlazoFijo + "\t");
                qz.append("C.CORRIENTE:" + $scope.resumenCaja.depositosCorriente + "\r\n");

                qz.append("\x1B\x40");
                qz.append("\x1B\x21\x08");
                qz.append("RETIROS (TOTAL): " + ($scope.resumenCaja.retirosAporte + $scope.resumenCaja.retirosAhorro + $scope.resumenCaja.retirosPlazoFijo + $scope.resumenCaja.retirosCorriente)+"\n");
                qz.append("\x1B\x21\x01");
                qz.append("C.APORTE:"+$scope.resumenCaja.retirosAporte+"\t");
                qz.append("C.AHORRO:"+$scope.resumenCaja.retirosAhorro+"\r\n");
                qz.append("C.P.FIJO:"+$scope.resumenCaja.retirosPlazoFijo+"\t");
                qz.append("C.CORRIENTE:"+$scope.resumenCaja.retirosCorriente+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("COMPRA/VENTA (TOTAL): "+($scope.resumenCaja.compra + $scope.resumenCaja.venta)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("COMPRA:" + $scope.resumenCaja.compra+"\t");
                qz.append("VENTA:"+$scope.resumenCaja.venta+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("TRANS. MAYOR CUANTIA(TOTAL): "+($scope.resumenCaja.depositoMayorCuantia+$scope.resumenCaja.retiroMayorCuantia+$scope.resumenCaja.compraVentaMayorCuantia)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("DEPOSITOS:"+$scope.resumenCaja.depositoMayorCuantia+"\t");
                qz.append("RETIROS:"+$scope.resumenCaja.retiroMayorCuantia+"\r\n");
                qz.append("COMPRA/VENTA:"+$scope.resumenCaja.compraVentaMayorCuantia+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("TRANS. CAJA-CAJA(TOTAL): " + ($scope.resumenCaja.enviadoCajaCaja+$scope.resumenCaja.recibidoCajaCaja)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("ENVIADOS:"+$scope.resumenCaja.enviadoCajaCaja+"\t");
                qz.append("RECIBIDOS:"+$scope.resumenCaja.recibidoCajaCaja+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("TRANS. BOVEDA-CAJA(TOTAL): " + ($scope.resumenCaja.enviadoBovedaCaja+$scope.resumenCaja.enviadoBovedaCaja)+"\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("ENVIADOS:"+$scope.resumenCaja.enviadoBovedaCaja+"\t");
                qz.append("RECIBIDOS:"+$scope.resumenCaja.enviadoBovedaCaja+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("CIERRE Caja(PENDIENTES):\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("SOBRANTE:"+$scope.resumenCaja.pendienteSobrante+"\t");
                qz.append("FALTANTE:"+$scope.resumenCaja.pendienteSobrante+"\r\n");

                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
				qz.append("____________" + "\t\t" + "_______________" + "\r\n");
				qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Firma Cajero" + "\t\t" + "Firma Jefe Caja" + "\r\n");

                qz.append("\x1D\x56\x41"); // 4
                qz.append("\x1B\x40"); // 5
                qz.print();
            };

            $scope.imprimirVoucherPorMoneda = function(index){

                qz.findPrinter("EPSON TM-U220");

                $scope.voucherPrint = $scope.voucherByMoneda[index];

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("CASA DE CAMBIOS VENTURA\r\n");
                qz.append("BALANCE CAJA\r\n");

                qz.append("\x1B\x21\x01");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");

                qz.append("AGENCIA: " + $scope.voucherPrint.agencia + "\r\n");
                qz.append("CAJA   : " + $scope.voucherPrint.caja + "\r\n");
                qz.append("FEC. APERT.: " + ($filter('date')($scope.voucherPrint.fechaApertura, 'dd/MM/yyyy'))+ " ");
                qz.append(($filter('date')($scope.voucherPrint.horaApertura, 'HH:mm:ss'))+"\r\n");
                qz.append("FEC. CIERRE: " + ($filter('date')($scope.voucherPrint.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append(($filter('date')($scope.voucherPrint.horaCierre, 'HH:mm:ss'))+"\r\n");
                qz.append("TRABAJADOR : " + $scope.voucherPrint.trabajador +"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Denominacion");
                qz.append("\t");
                qz.append("Cantidad");
                qz.append("  ");
                qz.append("Subtotal");
                qz.append("\r\n");

                qz.append("\x1B\x21\x01"); // 3
                for(var i = 0; i<$scope.voucherPrint.detalle.length;i++){
                	qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                	qz.append(($filter('currency')($scope.voucherPrint.detalle[i].valor, '')));
                	qz.append("\t\t");
                	qz.append($scope.voucherPrint.detalle[i].cantidad.toString());
                	qz.append("\t" + "     ");

                	if($scope.voucherPrint.moneda.simbolo == "€"){
                    	qz.append(($filter('currency')($scope.voucherPrint.detalle[i].valor * $scope.voucherPrint.detalle[i].cantidad,  chr(238))) + "\r\n");
                    } else {
                    	qz.append(($filter('currency')($scope.voucherPrint.detalle[i].valor * $scope.voucherPrint.detalle[i].cantidad, $scope.voucherPrint.moneda.simbolo))+ "\r\n");
                    }
                }

                qz.append("\n");
                qz.append("SALDO AYER: "+($filter('currency')($scope.voucherPrint.saldoAyer, $scope.voucherPrint.moneda.simbolo))+"\r\n");
                qz.append("ENTRADAS  : "+($filter('currency')($scope.voucherPrint.entradas, $scope.voucherPrint.moneda.simbolo))+"\r\n");
                qz.append("SALIDAS   : "+($filter('currency')($scope.voucherPrint.salidas, $scope.voucherPrint.moneda.simbolo))+"\r\n");
                qz.append("SOBRANTES : "+($filter('currency')($scope.voucherPrint.entradas, $scope.voucherPrint.moneda.simbolo))+"\r\n");
                qz.append("FALTANTES : "+($filter('currency')($scope.voucherPrint.salidas, $scope.voucherPrint.moneda.simbolo))+"\r\n");
                qz.append("SALDO X DEVOLVER: "+($filter('currency')($scope.voucherPrint.porDevolver, $scope.voucherPrint.moneda.simbolo))+"\r\n");

                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
				qz.append("____________" + "\t\t" + "_______________" + "\r\n");
				qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                qz.append("Firma Cajero" + "\t\t" + "Firma Jefe Caja" + "\r\n");

                qz.append("\x1D\x56\x41"); // 4
                qz.append("\x1B\x40"); // 5
                qz.print();
            };
        }]);
});
