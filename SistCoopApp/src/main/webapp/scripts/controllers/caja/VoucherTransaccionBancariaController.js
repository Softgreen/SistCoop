define(['../module'], function (controllers) {
  'use strict';
  controllers.controller('VoucherTransaccionBancariaController', ["$scope", "$state", "$filter", "CajaService", "CuentaBancariaService", "VariablesSistema", "RedirectService",
    function ($scope, $state, $filter, CajaService, CuentaBancariaService, VariablesSistema, RedirectService) {

      $scope.imprimirSaldo = false;

      CajaService.getVoucherTransaccionBancaria($scope.id).then(
        function (data) {
          $scope.transaccionCuentaBancaria = data;

          VariablesSistema.findMayorCuantiaByMoneda(data.moneda.id).then(function (data) {
            if ($scope.transaccionCuentaBancaria.monto >= data) {
              $scope.mayorCuantia = true;
            } else {
              $scope.mayorCuantia = false;
            }
          });

        },
        function error(error) {
          alert("Transaccion Cuenta Bancaria no Encontrado");
        }
      );

      $scope.salir = function () {
        $scope.redireccion();
      };

      $scope.redireccion = function () {
        if (RedirectService.haveNext()) {
          var nextState = RedirectService.getNextState();
          var parametros = RedirectService.getNextParamsState();
          $state.transitionTo(nextState, parametros);
        } else {
          $state.transitionTo('app.transaccion.depositoRetiro');
        }
      };

      $scope.goToMayorCuantia = function () {
        $state.transitionTo('app.transaccion.transaccionMayorCuantia', {id: $scope.id});
      };

      $scope.imprimir = function () {
        if (notReady()) {
          return;
        }

        qz.append("\x1B\x40");															//reset printer
        qz.append("\x1B\x21\x08");														//texto en negrita
        qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
        qz.append("MULTIVALORES DEL SUR\r\n");											// \r\n salto de linea

        qz.append(($scope.transaccionCuentaBancaria.tipoTransaccion) + " CUENTA " + ($scope.transaccionCuentaBancaria.tipoCuentaBancaria) + ($scope.transaccionCuentaBancaria.moneda.simbolo == "S/." ? " M.N." : " M.E.") + "\r\n");
        // \t tabulador
        qz.append("\x1B\x21\x01");														//texto normal (no negrita)
        qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

        qz.append(($scope.transaccionCuentaBancaria.agenciaAbreviatura) + "\t\t" + "TRANS:" + "\t" + ($scope.transaccionCuentaBancaria.idTransaccionBancaria) + "\r\n");
        qz.append("CAJA:" + "\t" + ($scope.transaccionCuentaBancaria.cajaDenominacion) + "\t\t" + "Nro OP:" + "\t" + ($scope.transaccionCuentaBancaria.numeroOperacion) + "\r\n");
        qz.append("FECHA:" + "\t" + ($filter('date')($scope.transaccionCuentaBancaria.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.transaccionCuentaBancaria.hora, 'HH:mm:ss')) + "\r\n");
        qz.append("CUENTA:" + "\t" + ($scope.transaccionCuentaBancaria.numeroCuenta) + "\r\n");
        qz.append("CLIENTE:" + "\t" + ($scope.transaccionCuentaBancaria.socio) + "\r\n");
        qz.append("MONEDA:" + "\t" + ($scope.transaccionCuentaBancaria.moneda.denominacion) + "\r\n");

        if (!angular.isUndefined($scope.transaccionCuentaBancaria.referencia))
          qz.append("REF:" + "\t" + ($scope.transaccionCuentaBancaria.referencia) + "\r\n");
        else {
          qz.append(" ");
        }

        if (($scope.transaccionCuentaBancaria.tipoTransaccion) == "DEPOSITO") {
          qz.append("\r\n");

          qz.append("IMPORTE RECIBIDO:" + "\t" + $scope.transaccionCuentaBancaria.moneda.simbolo + $filter('number')($scope.transaccionCuentaBancaria.monto, 2) + "\r\n");
          //qz.append("IMPORTE RECIBIDO:" + "\t" + ($filter('currency')($scope.transaccionCuentaBancaria.monto, $scope.transaccionCuentaBancaria.moneda.simbolo)) + "\r\n");
          if($scope.imprimirSaldo){
            if($scope.transaccionCuentaBancaria.interes != 0){
              qz.append("INTERES:" + "\t" + "\t" + $scope.transaccionCuentaBancaria.moneda.simbolo + $filter('number')($scope.transaccionCuentaBancaria.interes, 2) + "\r\n");
            }
            qz.append("SALDO DISPONIBLE:" + "\t" + $scope.transaccionCuentaBancaria.moneda.simbolo + $filter('number')($scope.transaccionCuentaBancaria.saldoDisponible, 2) + "\r\n");
            //qz.append("SALDO DISPONIBLE:" + "\t" + ($filter('currency')($scope.transaccionCuentaBancaria.saldoDisponible, $scope.transaccionCuentaBancaria.moneda.simbolo)) + "\r\n");
          }
          qz.append("\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");
        } else {
          qz.append("\r\n");
          qz.append("IMPORTE PAGADO:" + "\t" + $scope.transaccionCuentaBancaria.moneda.simbolo + $filter('number')($scope.transaccionCuentaBancaria.monto, 2) + "\r\n");
          //qz.append("IMPORTE PAGADO:" + "\t\t" + ($filter('currency')($scope.transaccionCuentaBancaria.monto, $scope.transaccionCuentaBancaria.moneda.simbolo)) + "\r\n");
          if($scope.imprimirSaldo){
            if($scope.transaccionCuentaBancaria.interes != 0){
              qz.append("INTERES:" + "\t" + "\t" + $scope.transaccionCuentaBancaria.moneda.simbolo + $filter('number')($scope.transaccionCuentaBancaria.interes, 2) + "\r\n");
            }
            qz.append("SALDO DISPONIBLE:" + "\t" + $scope.transaccionCuentaBancaria.moneda.simbolo + $filter('number')($scope.transaccionCuentaBancaria.saldoDisponible, 2) + "\r\n");
            //qz.append("SALDO DISPONIBLE:" + "\t" + ($filter('currency')($scope.transaccionCuentaBancaria.saldoDisponible, $scope.transaccionCuentaBancaria.moneda.simbolo)) + "\r\n");
          }
          qz.append("\r\n");
          qz.append("\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          qz.append("_________________" + "\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          //qz.append("Firma Titular(es)" + "\r\n");
          qz.append($scope.transaccionCuentaBancaria.titulares + "\r\n");
          qz.append("\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          qz.append("Verifique su dinero antes de retirarse  de ventanilla" + "\r\n");
        }
        qz.append("\x1D\x56\x41");														//cortar papel
        qz.append("\x1B\x40");
        qz.print();
      };
    }]);
});
