define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('EditarTransaccionSobreGiroController', ["$scope", "$state", "$filter", "PersonaNaturalService", "AgenciaService", "SessionService", "SobreGiroService",
    function ($scope, $state, $filter, PersonaNaturalService, AgenciaService, SessionService, SobreGiroService) {

      $scope.sobregiro = {};

      $scope.loadSobreGiro = function () {
        SobreGiroService.findById($scope.id).then(function (data) {
          $scope.sobregiro = data;
        });
      };
      $scope.loadSobreGiro();
      $scope.loadHistorialSobreGiro = function () {
        SobreGiroService.getHistoriales($scope.id).then(function (data) {
          $scope.historiales = data;

          var total = 0;
          for (var i = 0; i < data.length; i++) {
            total = total + data[i].monto;
          }
          $scope.total = total;
        });
      };
      $scope.loadHistorialSobreGiro();


      $scope.control = {
        success: false,
        inProcess: false,
        submitted: false
      };
      $scope.view = {
        monto: '0'
      };
      $scope.pagar = function () {
        if ($scope.form.$valid) {
          $scope.control.inProcess = true;
          var transaccion = {
            "idSobreGiro": $scope.id,
            "monto": parseFloat($scope.view.monto)
          };

          SessionService.crearTransaccionHistorialSobreGiro(transaccion).then(
            function (data) {
              $scope.control.success = true;
              $scope.control.inProcess = false;

              $scope.alerts = [{type: "success", msg: "Pago realizado exitosamente"}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };

              $scope.loadHistorialSobreGiro();
              $scope.loadSobreGiro();
            },
            function error(error) {
              $scope.control.inProcess = false;
              $scope.control.success = false;
              $scope.alerts = [{type: "danger", msg: "Error: " + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }
          );
        } else {
          $scope.control.submitted = true;
        }
      };

      $scope.imprimirSobreGiroCreado = function () {
        if (notReady()) {
          return;
        }

        qz.append("\x1B\x40");															//reset printer
        qz.append("\x1B\x21\x08");														//texto en negrita
        qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
        qz.append("MULTIVALORES DEL SUR\r\n");											// \r\n salto de linea

        qz.append("ENTREGA SOBRE GIRO" + ($scope.sobregiro.moneda.simbolo == "S/." ? " M.N." : " M.E.") + "\r\n");
        // \t tabulador
        qz.append("\x1B\x21\x01");														//texto normal (no negrita)
        qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

        qz.append("NUM.SOBRE GIRO:" + "\t" + ($scope.sobregiro.id) + "\r\n");
        qz.append("DOCUMENTO:" + "\t" + ($scope.sobregiro.socio.personaNatural.tipoDocumento.abreviatura) + "\t" + "NUMERO:" + "\t" + ($scope.sobregiro.socio.personaNatural.numeroDocumento) + "\r\n");

        if ($scope.sobregiro.socio.personaNatural) {
          qz.append("CLIENTE:" + "\t" + ($scope.sobregiro.socio.personaNatural.apellidoPaterno) + " " + ($scope.sobregiro.socio.personaNatural.apellidoMaterno) + "," + ($scope.sobregiro.socio.personaNatural.nombres) + "\r\n");
        } else {
          qz.append("CLIENTE:" + "\t" + ($scope.sobregiro.socio.personaJuridica.razonSocial) + "\r\n");
        }

        qz.append("FECHA ENTREGA:" + "\t" + ($filter('date')($scope.sobregiro.fechaCreacion, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.sobregiro.fechaCreacion, 'HH:mm:ss')) + "\r\n");
        qz.append("FECHA MAX.PAGO:" + "\t" + ($filter('date')($scope.sobregiro.fechaLimitePago, 'dd/MM/yyyy')) + "\r\n");
        qz.append("\r\n");
        qz.append("MONEDA:" + "\t" + ($scope.sobregiro.moneda.denominacion) + "\r\n");
        qz.append("IMPORTE ENTREGADO:" + "\t" + $scope.sobregiro.moneda.simbolo + $filter('number')($scope.sobregiro.monto, 2) + "\r\n");
        qz.append("INTERES A PAGAR:" + "\t" + $scope.sobregiro.moneda.simbolo + $filter('number')($scope.sobregiro.interes, 2) + "\r\n");
        qz.append("DEUDA TOTAL:" + "\t" + "\t" + $scope.sobregiro.moneda.simbolo + $filter('number')($scope.sobregiro.monto + $scope.sobregiro.interes, 2) + "\r\n");

        qz.append("\r\n");
        qz.append(String.fromCharCode(27) + "\x61" + "\x31");
        qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");

        qz.append("\x1D\x56\x41");														//cortar papel
        qz.append("\x1B\x40");
        qz.print();
      };

      $scope.imprimir = function (historial) {
        if (notReady()) {
          return;
        }

        qz.append("\x1B\x40");															//reset printer
        qz.append("\x1B\x21\x08");														//texto en negrita
        qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
        qz.append("MULTIVALORES DEL SUR\r\n");											// \r\n salto de linea

        qz.append("PAGO SOBRE GIRO" + ($scope.sobregiro.moneda.simbolo == "S/." ? " M.N." : " M.E.") + "\r\n");
        // \t tabulador
        qz.append("\x1B\x21\x01");														//texto normal (no negrita)
        qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

        qz.append("NUM.SOBRE GIRO:" + "\t" + ($scope.sobregiro.id) + "\r\n");
        qz.append("DOCUMENTO:" + "\t" + ($scope.sobregiro.socio.personaNatural.tipoDocumento.abreviatura) + "\t" + "NUMERO:" + "\t" + ($scope.sobregiro.socio.personaNatural.numeroDocumento) + "\r\n");

        if ($scope.sobregiro.socio.personaNatural) {
          qz.append("CLIENTE:" + "\t" + ($scope.sobregiro.socio.personaNatural.apellidoPaterno) + " " + ($scope.sobregiro.socio.personaNatural.apellidoMaterno) + "," + ($scope.sobregiro.socio.personaNatural.nombres) + "\r\n");
        } else {
          qz.append("CLIENTE:" + "\t" + ($scope.sobregiro.socio.personaJuridica.razonSocial) + "\r\n");
        }

        qz.append("FECHA PAGO:" + "\t" + ($filter('date')(historial.fecha, 'dd/MM/yyyy')) + " " + ($filter('date')(historial.fecha, 'HH:mm:ss')) + "\r\n");
        qz.append("MONEDA:" + "\t" + "\t" + ($scope.sobregiro.moneda.denominacion) + "\r\n");
        qz.append("IMPORTE RECIBIDO:" + "\t" + $scope.sobregiro.moneda.simbolo + $filter('number')(historial.monto, 2) + "\r\n");

        qz.append("\r\n");
        qz.append(String.fromCharCode(27) + "\x61" + "\x31");
        qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");

        qz.append("\x1D\x56\x41");														//cortar papel
        qz.append("\x1B\x40");
        qz.print();
      };

      $scope.cancelar = function () {
        $state.transitionTo("app.transaccion.buscarSobreGiros");
      };

    }]);
});
