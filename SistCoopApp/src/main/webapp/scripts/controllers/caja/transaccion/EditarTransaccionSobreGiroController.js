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

      $scope.cancelar = function () {
        $state.transitionTo("app.transaccion.buscarSobreGiros");
      };

    }]);
});
