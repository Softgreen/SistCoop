define(['../module'], function (controllers) {
  'use strict';
  controllers.controller('DebeHaberController', ['$scope', 'ReportesService',
    function ($scope, ReportesService) {

      $scope.fecha = new Date();

      $scope.listDebeNuevosSoles = [];
      $scope.listDebeDolares = [];
      $scope.listDebeEuros = [];

      $scope.listHaberNuevosSoles = [];
      $scope.listHaberDolares = [];
      $scope.listHaberEuros = [];

      $scope.dateOptions = {
        formatYear: 'yyyy',
        startingDay: 1
      };

      $scope.open = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.opened = true;
      };

      $scope.generarReporte = function () {
        if ($scope.form.$valid) {
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'DEBE', idMoneda: 0}).then(function (response) {
            $scope.listDebeDolares = response;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'DEBE', idMoneda: 1}).then(function (response) {
            $scope.listDebeNuevosSoles = response;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'DEBE', idMoneda: 2}).then(function (response) {
            $scope.listDebeEuros = response;
          });

          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'HABER', idMoneda: 0}).then(function (response) {
            $scope.listHaberDolares = response;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'HABER', idMoneda: 1}).then(function (response) {
            $scope.listHaberNuevosSoles = response;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'HABER', idMoneda: 2}).then(function (response) {
            $scope.listHaberEuros = response;
          });
        }
      };

    }]);
});
