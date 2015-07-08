define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('EditarTransaccionGiroController', ["$scope", "$state", "PersonaNaturalService", "AgenciaService", "SessionService", "GiroService",
    function ($scope, $state, PersonaNaturalService, AgenciaService, SessionService, GiroService) {

      $scope.giro = {};

      $scope.loadGiro = function () {
        GiroService.findById($scope.id).then(function (data) {
          $scope.giro = data;
        });
      };
      $scope.loadGiro();

      //transaccion
      $scope.desembolsar = function () {
        var giro = angular.copy($scope.giro);
        giro.estadoPagoComision = true;
        giro.estado = "COBRADO";

        GiroService.update(giro.id, giro).then(
          function (data) {
            $scope.alerts = [{type: "success", msg: "Monto Desembolsado..."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };

            $scope.loadGiro();
          },
          function error(error) {
            $scope.alerts = [{type: "danger", msg: "Error: No se pudo desembolsar..."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };
          }
        );
      };

      $scope.extornar = function () {
        var giro = angular.copy($scope.giro);
        giro.estadoPagoComision = false;
        giro.estado = "EXTORNADO";

        GiroService.update(giro.id, giro).then(
          function (data) {
            $scope.alerts = [{type: "success", msg: "Giro Extornado..."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };

            $scope.loadGiro();
          },
          function error(error) {
            $scope.alerts = [{type: "danger", msg: "Error: No se pudo extornar..."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };
          }
        );
      };

      $scope.imprimir = function () {

        if (notReady()) {
          return;
        }

        qz.append("\x1B\x40");															//reset printer
        qz.append("\x1B\x21\x08");														//texto en negrita
        qz.append(String.fromCharCode(27) + "\x61" + "\x31");							//texto centrado
        qz.append("CASA DE CAMBIOS VENTURA\r\n");											// \r\n salto de linea

        qz.append(" OPERACION EN EFECTIVO: GIRO " + "\r\n");
        // \t tabulador
        qz.append("\x1B\x21\x01");														//texto normal (no negrita)
        qz.append(String.fromCharCode(27) + "\x61" + "\x30");							//texto a la izquierda

        qz.append("CAJA:" + "\t" + ($scope.cajaSession.abreviatura) + "\t\t" + "Nro OP:" + "\t" + ($scope.giro.id) + "\r\n");
        qz.append("FECHA:" + "\t" + ($filter('date')($scope.giro.fechaEnvio, 'dd/MM/yyyy')) + " " + ($filter('date')($scope.giro.fechaEnvio, 'HH:mm:ss')) + "\r\n");
        qz.append("ORDENANTE:" + "\t" + ($scope.giro.clienteEmisor) + "\r\n");
        qz.append("BENEFICIARIO:" + "\t" + ($scope.giro.numeroDocumentoReceptor + ' ' + $scope.giro.clienteReceptor) + "\r\n");
        qz.append("MONEDA:" + "\t" + ($scope.giro.moneda.denominacion) + "\r\n");

        if (($scope.giro.estado) == "ENVIADO") {
          qz.append("\r\n");
          qz.append("MONTO:" + "\t" + ($filter('currency')($scope.giro.monto, $scope.giro.moneda.simbolo)) + "\r\n");
          qz.append("\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          qz.append("Verifique su dinero antes  de retirarse de ventanilla" + "\r\n");
        } else if (($scope.giro.estado) == "DESEMBOLSADO") {
          qz.append("\r\n");
          qz.append("MONTO:" + "\t" + ($filter('currency')($scope.giro.monto, $scope.giro.moneda.simbolo)) + "\r\n");
          qz.append("\r\n");
          qz.append("\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          qz.append("_________________" + "\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          //qz.append("Firma Titular(es)" + "\r\n");
          qz.append("\r\n");
          qz.append(String.fromCharCode(27) + "\x61" + "\x31");
          qz.append("Verifique su dinero antes de retirarse  de ventanilla" + "\r\n");
        }
        qz.append("\x1D\x56\x41");														//cortar papel
        qz.append("\x1B\x40");
        qz.print();

      };

      $scope.cancelar = function () {
        $state.transitionTo("app.transaccion.buscarGiros");
      };

    }]);
});
