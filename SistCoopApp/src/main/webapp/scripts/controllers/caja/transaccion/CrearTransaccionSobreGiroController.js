define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('CrearTransaccionSobreGiroController', ["$scope", "$state", "PersonaNaturalService", "PersonaJuridicaService", "SocioService", "MaestroService", "MonedaService",
    function ($scope, $state, PersonaNaturalService, PersonaJuridicaService, SocioService, MaestroService, MonedaService) {

      $scope.control = {
        success: false,
        inProcess: false,
        submitted: false
      };

      $scope.combo = {
        tipoPersonas: MaestroService.getTipoPersonas(),
        tipoDocumentos: []
      };

      $scope.socioNatural = undefined;
      $scope.socioJuridico = undefined;

      $scope.view = {
        tipoPersona: undefined,
        idTipoDocumento: undefined,
        numeroDocumento: undefined,

        idMoneda: undefined,
        monto: '0',
        interes: '10',
        tipoInteres: 'FIJO'//PORCENTUAL
      };

      $scope.combo = {
        moneda: undefined
      };

      $scope.$watch('view.tipoPersona', function (newValue, oldValue) {
        if ($scope.view.tipoPersona) {
          $scope.socio = undefined;
          if ($scope.view.tipoPersona == "NATURAL") {
            PersonaNaturalService.getTipoDocumentos().then(function (data) {
              $scope.combo.tipoDocumentos = data;
            });
          } else {
            if ($scope.view.tipoPersona == "JURIDICA") {
              PersonaJuridicaService.getTipoDocumentos().then(function (data) {
                $scope.combo.tipoDocumentos = data;
              });
            }
          }
        }
      }, true);

      $scope.loadMonedas = function () {
        MonedaService.getMonedas().then(function (data) {
          $scope.combo.moneda = data;
        });
      };
      $scope.loadMonedas();
      $scope.getTipoMoneda = function () {
        if (!angular.isUndefined($scope.view.idMoneda) && !angular.isUndefined($scope.combo.moneda)) {
          for (var i = 0; i < $scope.combo.moneda.length; i++)
            if ($scope.view.idMoneda == $scope.combo.moneda[i].id)
              return $scope.combo.moneda[i];
        } else {
          return undefined;
        }
      };

      $scope.buscarSocio = function ($event) {
        if (!angular.isUndefined($event)) {
          $event.preventDefault();
        }
        if (angular.isUndefined($scope.view.idTipoDocumento) || angular.isUndefined($scope.view.numeroDocumento)) {
          return;
        }

        var tipoDoc = $scope.view.idTipoDocumento;
        var numDoc = $scope.view.numeroDocumento;
        SocioService.find($scope.view.tipoPersona, tipoDoc, numDoc).then(
          function (data) {
            $scope.socio = data;
          }, function error(error) {
            $scope.socio = undefined;
            $scope.alerts = [{type: "danger", msg: "Error al buscar socio."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };
          }
        );

      };

      $scope.$watch('view.monto', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.$watch('view.interes', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.$watch('view.tipoInteres', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.updateTotal = function () {
        var monto = parseFloat($scope.view.monto);
        var interes = 0;

        //hallando comision
        if ($scope.view.tipoInteres == 'PORCENTURAL') {
          interes = parseFloat($scope.view.monto) * parseFloat($scope.view.interes) / 100;
          //redondeo a un decimal y hacia arriba
          interes = Math.round(interes * 10) / 10;
        } else {
          interes = parseFloat($scope.view.interes);
        }

        $scope.transaccion = {
          "monto": monto,
          "interes": interes
        };

      };

    }]);
});
