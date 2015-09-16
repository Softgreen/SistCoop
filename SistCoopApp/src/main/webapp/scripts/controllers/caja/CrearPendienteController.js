define(['../module'], function (controllers) {
  'use strict';
  controllers.controller('CrearPendienteController', ["$scope", "$state", "$filter", "$modal", "CajaService", "SessionService", "MonedaService", "PendienteCajaService",
    function ($scope, $state, $filter, $modal, CajaService, SessionService, MonedaService, PendienteCajaService) {

      $scope.Math = window.Math;
      $scope.control = {"success": false, "inProcess": false, "submitted": false};

      $scope.view = {
        idBoveda: $scope.idBoveda,
        tipoPendiente: $scope.tipoPendiente,
        monto: undefined,
        observacion: undefined,
        idPendienteRelacionado: $scope.idPendienteRelacionado
      };
      $scope.view.load = {
        pendienteRelacionado: undefined
      };

      $scope.combo = {
        boveda: undefined,
        tipoPendiente: [
          {"denominacion": "FALTANTE", "factor": -1},
          {"denominacion": "SOBRANTE", "factor": 1},
          {"denominacion": "PAGO", "factor": 1},
          {"denominacion": "RETIRO", "factor": -1}
        ]
      };
      $scope.combo.selected = {
        boveda: undefined,
        tipoPendiente: undefined
      };

      $scope.loadMonto = function(){
        if($scope.monto){
          $scope.view.monto = $scope.monto;
        }
      };
      $scope.loadMonto();

      $scope.loadPendienteRelacionado = function () {
        if (!angular.isUndefined($scope.view.idPendienteRelacionado)) {
          PendienteCajaService.findById($scope.view.idPendienteRelacionado).then(function (response) {
            $scope.view.load.pendienteRelacionado = response;
            $scope.view.monto = Math.abs(response.monto).toString();
          });
        }
      };
      $scope.loadPendienteRelacionado();

      //Cargar datos pregargados
      var cargarDatos = function () {
        if (!angular.isUndefined($scope.view.idBoveda)) {
          if (!angular.isUndefined($scope.combo.boveda)) {
            for (var i = 0; i < $scope.combo.boveda.length; i++) {
              if ($scope.view.idBoveda == $scope.combo.boveda[i].id) {
                $scope.combo.selected.boveda = $scope.combo.boveda[i];
              }
            }
          }
        }

        if (!angular.isUndefined($scope.view.tipoPendiente)) {
          for (var i = 0; i < $scope.combo.tipoPendiente.length; i++) {
            if ($scope.view.tipoPendiente == $scope.combo.tipoPendiente[i].denominacion) {
              $scope.combo.selected.tipoPendiente = $scope.combo.tipoPendiente[i];
            }
          }
        }
      };
      cargarDatos();

      $scope.loadCombo = function () {
        if (angular.isUndefined($scope.cajaSession.id)) {
          //se creó una ventana nueva
          $scope.$watch('cajaSession.id', function () {
            if (!angular.isUndefined($scope.cajaSession.id)) {
              CajaService.getBovedas($scope.cajaSession.id).then(function (response) {
                $scope.combo.boveda = response;
                cargarDatos();
              });
            }
          }, true);

        } else {
          //se hizo un redirect
          CajaService.getBovedas($scope.cajaSession.id).then(function (response) {
            $scope.combo.boveda = response;
            cargarDatos();
          });
        }
      };
      $scope.loadCombo();

      $scope.crearPendiente = function () {
        if ($scope.form.$valid) {
          $scope.control.inProcess = true;

          SessionService.crearPendiente(
            $scope.combo.selected.boveda.id,
            (Math.abs($scope.view.monto) * $scope.combo.selected.tipoPendiente.factor),
            $scope.view.observacion,
            $scope.combo.selected.tipoPendiente.denominacion,
            $scope.view.load.pendienteRelacionado ? $scope.view.load.pendienteRelacionado.id : undefined
          ).then(
            function (response) {
              $scope.control.inProcess = false;
              $state.transitionTo('app.caja.pendienteVoucher', {id: response.id});
            },
            function error(error) {
              $scope.control.inProcess = false;
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

      $scope.buttonDisableState = function () {
        return $scope.control.inProcess;
      };

      $scope.cancelar = function () {
        $state.transitionTo('app.caja.pendiente');
      };

    }]);
});
