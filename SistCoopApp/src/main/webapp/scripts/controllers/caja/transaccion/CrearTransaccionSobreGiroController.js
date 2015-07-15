define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('CrearTransaccionSobreGiroController', ["$scope", "$state","PersonaNaturalService", "PersonaJuridicaService", "SocioService", "MaestroService",
    function ($scope, $state, PersonaNaturalService, PersonaJuridicaService, SocioService, MaestroService) {

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


        "numeroDocumentoEmisor": undefined,
        "nombreClienteEmisor": undefined,
        "numeroDocumentoReceptor": undefined,
        "nombreClienteReceptor": undefined,
        idAgenciaOrigen: undefined,
        idAgenciaDestino: undefined,
        idMoneda: undefined,
        monto: '0',
        comision: '10',
        tipoComision: 'FIJO',//PORCENTUAL
        modoPagoComision: 'ANADIR',//REDUCIR
        lugarPagoComision: 'AL_ENVIAR',//AL_COBRAR
        total: '0'
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


    }]);
});
