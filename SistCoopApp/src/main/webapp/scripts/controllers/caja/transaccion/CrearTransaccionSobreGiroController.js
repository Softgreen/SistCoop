define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('CrearTransaccionSobreGiroController', ["$scope", "$state", "PersonaNaturalService", "PersonaJuridicaService", "SessionService", "MaestroService",
    function ($scope, $state, PersonaNaturalService, PersonaJuridicaService, SessionService, MaestroService) {

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
          $scope.socioNatural = undefined;
          $scope.socioJuridico = undefined;
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

      $scope.buscarPersonaSocio = function ($event) {
        if (angular.isUndefined($scope.view.idTipoDocumento) || angular.isUndefined($scope.view.numeroDocumento)) {
          if (!angular.isUndefined($event))
            $event.preventDefault();
          return;
        }

        var tipoDoc = $scope.view.idTipoDocumento;
        var numDoc = $scope.view.numeroDocumento;
        if ($scope.view.tipoPersona == "NATURAL") {
          PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function (data) {
            $scope.socioNatural = data;
            if (angular.isUndefined(data) || data === null) {
              $scope.socioNatural = undefined;
              $scope.alerts = [{type: "danger", msg: "Persona No Encontrada."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            } else {
              $scope.alerts = [{type: "success", msg: "Persona (Socio) Encontrada."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }

          }, function error(error) {
            $scope.socioNatural = undefined;
            $scope.alerts = [{type: "danger", msg: "Error al buscar la persona."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };
          });
        } else {
          if ($scope.view.tipoPersona == "JURIDICA") {
            PersonaJuridicaService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function (persona) {
              $scope.socioJuridico = persona;
              if (angular.isUndefined(persona) || persona === null) {
                $scope.socioJuridico = undefined;
                $scope.alerts = [{type: "danger", msg: "Persona Jur&iacute;dica No Encontrada."}];
                $scope.closeAlert = function (index) {
                  $scope.alerts.splice(index, 1);
                };
              } else {
                $scope.alerts = [{type: "success", msg: "Persona (Socio) Encontrada."}];
                $scope.closeAlert = function (index) {
                  $scope.alerts.splice(index, 1);
                };
              }
            }, function error(error) {
              $scope.socioJuridico = undefined;
              $scope.alerts = [{type: "danger", msg: "Error al buscar la persona."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              }
            });
          }
        }
        if ($event !== undefined)
          $event.preventDefault();
      };


    }]);
});
