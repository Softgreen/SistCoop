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

      $scope.totalDebeNuevosSoles = 0;
      $scope.totalDebeDolares = 0;
      $scope.totalDebeEuros = 0;
      $scope.totalHaberNuevosSoles = 0;
      $scope.totalHaberDolares = 0;
      $scope.totalHaberEuros = 0;

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
            var total = 0;
            for (var i = 0; i < response.length; i++) {
              total = total + response[i].monto;
            }
            $scope.totalDebeDolares = total;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'DEBE', idMoneda: 1}).then(function (response) {
            $scope.listDebeNuevosSoles = response;
            var total = 0;
            for (var i = 0; i < response.length; i++) {
              total = total + response[i].monto;
            }
            $scope.totalDebeNuevosSoles = total;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'DEBE', idMoneda: 2}).then(function (response) {
            $scope.listDebeEuros = response;
            var total = 0;
            for (var i = 0; i < response.length; i++) {
              total = total + response[i].monto;
            }
            $scope.totalDebeEuros = total;
          });

          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'HABER', idMoneda: 0}).then(function (response) {
            $scope.listHaberDolares = response;
            var total = 0;
            for (var i = 0; i < response.length; i++) {
              total = total + response[i].monto;
            }
            $scope.totalHaberDolares = total;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'HABER', idMoneda: 1}).then(function (response) {
            $scope.listHaberNuevosSoles = response;
            var total = 0;
            for (var i = 0; i < response.length; i++) {
              total = total + response[i].monto;
            }
            $scope.totalHaberNuevosSoles = total;
          });
          ReportesService.getDebeHaber({fecha: $scope.fecha.getTime(), tipo: 'HABER', idMoneda: 2}).then(function (response) {
            $scope.listHaberEuros = response;
            var total = 0;
            for (var i = 0; i < response.length; i++) {
              total = total + response[i].monto;
            }
            $scope.totalHaberEuros = total;
          });
        }
      };

    }]);
});
