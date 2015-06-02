define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('VoucherCerrarBoveda', ['$scope','$state','BovedaService', '$filter',
        function($scope,$state,BovedaService, $filter) {
    		
    		$scope.objetosCargados = {
    				detalleBoveda: []
    		};

            $scope.loadVoucher = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.getVoucherCerrarBoveda($scope.id).then(
                        function(data){
                            $scope.cerrarBovedaVoucher = data;
                        },
                        function error(error){
                            alert("Boveda no encontrada");
                        }
                    );
                };
            };

            $scope.loadVoucher();
            
            $scope.imprimir = function(){
            	if (notReady()) {return;}														//Elegir impresora
                qz.append("\x1B\x40");															//reset printer

                qz.append("\x1B\x21\x08");														//texto en negrita
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
                qz.append("CASA DE CAMBIOS VENTURA\r\n");											// \r\n salto de linea
                qz.append("DETALLE CIERRE BOVEDA " + "\r\n");
               
                
                // \t tabulador
                qz.append("\x1B\x21\x01");														//texto normal (no negrita)
                qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

                qz.append("AGENCIA: " + ($scope.cerrarBovedaVoucher.agenciaDenominacion) + "\r\n");
                qz.append("BOVEDA : " + ($scope.cerrarBovedaVoucher.boveda) + "\r\n");
                
                if($scope.cerrarBovedaVoucher.moneda.simbolo == "€"){            
                	qz.append("MONEDA : " + ($scope.cerrarBovedaVoucher.moneda.denominacion) + "(" + chr(238) + ")" + "\r\n");
                } else {
                	qz.append("MONEDA : " + ($scope.cerrarBovedaVoucher.moneda.denominacion) + "(" + $scope.cerrarBovedaVoucher.moneda.simbolo + ")" + "\r\n");
                } 
                
                qz.append("FECHA APERTURA: " + ($filter('date')($scope.cerrarBovedaVoucher.fechaApertura, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.cerrarBovedaVoucher.horaApertura, 'HH:mm:ss')) + "\r\n");
                qz.append("FECHA CIERRE  : " + ($filter('date')($scope.cerrarBovedaVoucher.fechaCierre, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.cerrarBovedaVoucher.horaCierre, 'HH:mm:ss')) + "\r\n");
                qz.append("TRABAJADOR: " + ($scope.cerrarBovedaVoucher.trabajador) + "\r\n");
                
                //Detalle de Transaccion
                qz.append("Denominacion");
                qz.append("\t");
                qz.append("Cantidad");
                qz.append("    ");
                qz.append("Subtotal");
                qz.append("\r\n");

                for(var i = 0 ; i < $scope.cerrarBovedaVoucher.detalle.length ; i++){
                    qz.append(String.fromCharCode(27) + "\x61" + "\x30");
                    qz.append(($filter('currency')($scope.cerrarBovedaVoucher.detalle[i].valor, '')));
                    qz.append("\t\t");
                    qz.append($scope.cerrarBovedaVoucher.detalle[i].cantidad.toString());
                    qz.append("\t" + "    ");
 
                    if($scope.cerrarBovedaVoucher.moneda.simbolo == "€"){            
                    	qz.append(($filter('currency')($scope.cerrarBovedaVoucher.detalle[i].valor * $scope.cerrarBovedaVoucher.detalle[i].cantidad,  chr(238))) + "\r\n");
                    } else {
                    	qz.append(($filter('currency')($scope.cerrarBovedaVoucher.detalle[i].valor * $scope.cerrarBovedaVoucher.detalle[i].cantidad, $scope.cerrarBovedaVoucher.moneda.simbolo))+ "\r\n");
                    }                   
                }

                qz.append(String.fromCharCode(27) + "\x61" + "\x32");							//texto a la derecha
                if($scope.cerrarBovedaVoucher.moneda.simbolo == "€"){            
                	qz.append("TOTAL BOVEDA: " + chr(238) + ($filter('currency')($scope.cerrarBovedaVoucher.TotalCierreBoveda, '')) + "\r\n");
                } else {
                	qz.append("TOTAL BOVEDA: " + ($filter('currency')($scope.cerrarBovedaVoucher.TotalCierreBoveda, $scope.cerrarBovedaVoucher.moneda.simbolo)) + "\r\n");
                }

                qz.append("\r\n");
                qz.append("\r\n");
                qz.append("\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("______________" + "\r\n");
                qz.append(String.fromCharCode(27) + "\x61" + "\x31");
                qz.append("Jefe Caja" + "\r\n");
                qz.append("\x1D\x56\x41");														//cortar papel
                qz.append("\x1B\x40");
                qz.print();
            };

            $scope.salir = function(){
                $scope.redireccion();
            };
            
            $scope.redireccion = function(){
            	$state.transitionTo('app.boveda.buscarBoveda');
            };
        }]);
});