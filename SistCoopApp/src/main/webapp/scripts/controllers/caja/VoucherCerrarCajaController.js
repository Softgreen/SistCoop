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
                qz.append("C.A.C. CAJA VENTURA\r\n");
                qz.append("RESUMEN DE OPERACIONES\r\n");
                
                qz.append("\x1B\x21\x01");
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                
                qz.append("AGENCIA:" + $scope.resumenCaja.agencia + "\r\n");
                qz.append("CAJA:" + "\t\t" + $scope.resumenCaja.caja + " " + "\r\n");
                qz.append("FEC. APERT.: " + ($filter('date')($scope.resumenCaja.fechaApertura, 'dd/MM/yyyy'))+ " ");
                qz.append(($filter('date')($scope.resumenCaja.horaApertura, 'HH:mm:ss'))+"\r\n");
                qz.append("FEC. CIERRE: " + ($filter('date')($scope.resumenCaja.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append(($filter('date')($scope.resumenCaja.horaCierre, 'HH:mm:ss'))+"\r\n");
                qz.append("TRABAJADOR:  " + $scope.resumenCaja.trabajador+"\r\n");

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
                qz.append("SOBRANTE :"+$scope.resumenCaja.pendienteSobrante+"\t");
                qz.append("FALTANTE:"+$scope.resumenCaja.pendienteSobrante+"\r\n");

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
                qz.append("______ C.A.C. CAJA VENTURA ______\r\n");
                qz.append("\x1B\x21\x01"); // 3

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("VOUCHER CIERRE CAJA\r\n");
                qz.append("\x1B\x21\x01"); // 3
                qz.append("Agencia:"+ $scope.voucherPrint.agencia + " ");
                qz.append("Caja:"+ $scope.voucherPrint.caja + " " + "\r\n");
                qz.append("F.Apertu:"+($filter('date')($scope.voucherPrint.fechaApertura, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Apertu:"+($filter('date')($scope.voucherPrint.horaApertura, 'HH:mm:ss'))+"\r\n");
                qz.append("F.Cierre:"+($filter('date')($scope.voucherPrint.fechaCierre, 'dd/MM/yyyy'))+ " ");
                qz.append("H.Cierre:"+($filter('date')($scope.voucherPrint.horaCierre, 'HH:mm:ss'))+"\r\n");
                qz.append("Trabajador:"+$scope.voucherPrint.trabajador+"\r\n");

                qz.append("\x1B\x40"); // 1
                qz.append("\x1B\x21\x08"); // 2
                qz.append("Denominacion   Cantidad   Subtotal"+"\n");
                qz.append("\x1B\x21\x01"); // 3
                for(var i = 0; i<$scope.voucherPrint.detalle.length;i++){
                    qz.append($scope.voucherPrint.detalle.valor + "   ");
                    qz.append($scope.voucherPrint.detalle.cantidad + "   ");
                    qz.append($filter('currency')(($scope.voucherPrint.detalle.valor*$scope.voucherPrint.detalle.cantidad),$scope.voucherPrint.moneda.simbolo)+ "\r\n");
                }

                qz.append("\n");
                qz.append("Saldo ayer:"+$scope.voucherPrint.saldoAyer+"\r\n");
                qz.append("Entradas:"+$scope.voucherPrint.entradas+"\r\n");
                qz.append("Salidas:"+$scope.voucherPrint.salidas+"\r\n");
                qz.append("Sobrantes:"+$scope.voucherPrint.entradas+"\r\n");
                qz.append("Faltantes:"+$scope.voucherPrint.salidas+"\r\n");
                qz.append("Faltantes:"+$scope.voucherPrint.porDevolver+"\r\n");

                qz.append("\x1D\x56\x41"); // 4
                qz.append("\x1B\x40"); // 5
                qz.print();
            };
        }]);
});