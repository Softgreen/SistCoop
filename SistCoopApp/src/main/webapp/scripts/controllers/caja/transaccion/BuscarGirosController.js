define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('BuscarGirosController', ['$scope', '$state', '$filter', '$modal', 'CajaService', 'AgenciaService', 'SessionService',
    function ($scope, $state, $filter, $modal, CajaService, AgenciaService, SessionService) {

      $scope.nuevo = function () {
        $state.transitionTo('app.transaccion.crearGiro');
      };

      $scope.loadGirosEnviados = function () {
        AgenciaService.getGirosEnviados($scope.agenciaSession.id).then(function (enviados) {
            $scope.transaccionesEnviadas = enviados;
        });
      };
      $scope.loadGirosRecibidos = function () {
        AgenciaService.getGirosRecibidos($scope.agenciaSession.id).then(function (recibidos) {
            $scope.transaccionesRecibidas = recibidos;
        });
      };

      $scope.loadGirosEnviados();
      $scope.loadGirosRecibidos();

      $scope.gridOptionsRecibidos = {
        data: 'transaccionesRecibidas',
        multiSelect: false,
        columnDefs: [
          {field: "fecha", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'Fecha', width: 70},
          {field: "hora", cellFilter: "date : 'HH:mm:ss'", displayName: 'Hora', width: 60},
          {
            displayName: 'Estado solicitud',
            width: 100,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'
          },
          {
            displayName: 'Estado confirmación',
            width: 125,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'
          },
          {field: "boveda", displayName: 'Origen'},
          {field: 'caja', displayName: 'Destino', width: 80},
          {field: "monto", cellFilter: "currency :''", displayName: 'Monto', width: 80},
          {
            displayName: 'Edit',
            width: 170,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-success btn-xs" ng-click="confirmarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-ok"></span>Confirmar</button></div>'
          }]
      };

      $scope.gridOptionsEnviados = {
        data: 'transaccionesEnviadas',
        multiSelect: false,
        columnDefs: [
          {field: "fecha", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'Fecha', width: 80},
          {field: "hora", cellFilter: "date : 'HH:mm:ss'", displayName: 'Hora', width: 70},
          {
            displayName: 'Estado Solicitud',
            width: 100,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'
          },
          {
            displayName: 'Estado Confirmación',
            width: 125,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'
          },
          {field: "caja", displayName: 'Origen', width: 90},
          {field: 'boveda', displayName: 'Destino'},
          {field: "monto", cellFilter: "currency :''", displayName: 'Monto', width: 90},
          {
            displayName: 'Edit',
            width: 180,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'
          }]
      };

    }]);
});
