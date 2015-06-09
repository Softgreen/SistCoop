define(['../module'], function (controllers) {
  'use strict';
  controllers.controller("TipoCambioController", ["$scope", "$state", "TasaInteresService",
    function ($scope, $state, TasaInteresService) {

      $scope.monedas = {
        dolar: {
          id: 0,
          denominacion: 'Dolar americano'
        },
        nuevoSol: {
          id: 1,
          denominacion: 'Nuevo sol'
        },
        euro: {
          id: 2,
          denominacion: 'Euro'
        }
      };

      $scope.view = {
        tasaCompraDolar: undefined,
        tasaVentaDolar: undefined,
        tasaCompraEuro: undefined,
        tasaVentaEuro: undefined
      };

      $scope.loadTasas = function () {

        TasaInteresService.getTasaCambio($scope.monedas.nuevoSol.id, $scope.monedas.dolar.id).then(function (response) {
          $scope.view.tasaCompraDolar = response;
        });
        TasaInteresService.getTasaCambio($scope.monedas.dolar.id, $scope.monedas.nuevoSol.id).then(function (response) {
          $scope.view.tasaVentaDolar = response;
        });

        TasaInteresService.getTasaCambio($scope.monedas.nuevoSol.id, $scope.monedas.euro.id).then(function (response) {
          $scope.view.tasaCompraEuro = response;
        });
        TasaInteresService.getTasaCambio($scope.monedas.euro.id, $scope.monedas.nuevoSol.id).then(function (response) {
          $scope.view.tasaVentaEuro = response;
        });

      };
      $scope.loadTasas();



      $scope.guardarTasaCompraDolar = function () {
        TasaInteresService.setTasaCambio($scope.monedas.nuevoSol.id, $scope.monedas.dolar.id, $scope.view.tasaCompraDolar).then(
          function (response) {
            $scope.alerts = [{ type: "success", msg: "Tasa guardada"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          },
          function error(err){
            $scope.alerts = [{ type: "danger", msg: "No pudo guardar la tasa"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          }
        );
      };
      $scope.guardarTasaVentaDolar = function () {
        TasaInteresService.setTasaCambio($scope.monedas.dolar.id, $scope.monedas.nuevoSol.id, $scope.view.tasaVentaDolar).then(
          function (response) {
            $scope.alerts = [{ type: "success", msg: "Tasa guardada"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          },
          function error(err){
            $scope.alerts = [{ type: "danger", msg: "No pudo guardar la tasa"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          }
        );
      };

      $scope.guardarTasaCompraEuro = function () {
        TasaInteresService.setTasaCambio($scope.monedas.nuevoSol.id, $scope.monedas.euro.id, $scope.view.tasaCompraEuro).then(
          function (response) {
            $scope.alerts = [{ type: "success", msg: "Tasa guardada"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          },
          function error(err){
            $scope.alerts = [{ type: "danger", msg: "No pudo guardar la tasa"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          }
        );
      };
      $scope.guardarTasaVentaEuro = function () {
        TasaInteresService.setTasaCambio($scope.monedas.euro.id, $scope.monedas.nuevoSol.id, $scope.view.tasaVentaEuro).then(
          function (response) {
            $scope.alerts = [{ type: "success", msg: "Tasa guardada"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          },
          function error(err){
            $scope.alerts = [{ type: "danger", msg: "No pudo guardar la tasa"}];
            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
          }
        );
      };


    }]);
});
