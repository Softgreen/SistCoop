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
          {field: "numeroDocumentoEmisor", displayName: 'Emisor', width: 70},
          {field: "clienteEmisor", displayName: 'Emisor', width: 70},
          {field: "numeroDocumentoReceptor", displayName: 'Receptor', width: 70},
          {field: "clienteReceptor", displayName: 'Receptor', width: 70},
          {field: "moneda", displayName: 'Moneda', width: 70},
          {field: "monto", displayName: 'Monto', width: 70},
          {field: "comision", displayName: 'Comision', width: 70},
          {field: "lugarPagoComision", displayName: 'Comision lugar pago', width: 70},
          {field: "estadoPagoComision", displayName: 'Estado lugar pago', width: 70},
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Envio', width: 70},
          {field: "fechaDesembolso", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Desembolso', width: 60},
          {field: "estado", displayName: 'Estado lugar pago', width: 70},
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
          {field: "numeroDocumentoEmisor", displayName: 'Emisor', width: 70},
          {field: "clienteEmisor", displayName: 'Emisor', width: 70},
          {field: "numeroDocumentoReceptor", displayName: 'Receptor', width: 70},
          {field: "clienteReceptor", displayName: 'Receptor', width: 70},
          {field: "moneda", displayName: 'Moneda', width: 70},
          {field: "monto", displayName: 'Monto', width: 70},
          {field: "comision", displayName: 'Comision', width: 70},
          {field: "lugarPagoComision", displayName: 'Comision lugar pago', width: 70},
          {field: "estadoPagoComision", displayName: 'Estado lugar pago', width: 70},
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Envio', width: 70},
          {field: "fechaDesembolso", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Desembolso', width: 60},
          {field: "estado", displayName: 'Estado lugar pago', width: 70},
          {
            displayName: 'Edit',
            width: 170,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-success btn-xs" ng-click="confirmarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-ok"></span>Confirmar</button></div>'
          }]
      };

    }]);
});
