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

      //cobro de giros
      $scope.gridOptionsRecibidos = {
        data: 'transaccionesRecibidas',
        multiSelect: false,
        columnDefs: [
          {field: "numeroDocumentoReceptor", displayName: 'Receptor', width: 70},
          {field: "clienteReceptor", displayName: 'Receptor', width: 175},

          {field: "moneda.simbolo", displayName: 'M.', width: 25},
          {field: "monto", displayName: 'Monto', cellFilter: "currency: ''", width: 60},

          {field: "comision", displayName: 'Comision', cellFilter: "currency: ''", width: 60},
          {field: "lugarPagoComision", displayName: 'Comision', width: 135},
          {
            displayName: 'Comision',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoPagoComision">PAGADO</span><span ng-hide="row.entity.estadoPagoComision">NO PAGADO</span></div>',
            width: 75
          },

          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Envio', width: 70},
          {field: "fechaDesembolso", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Desembolso', width: 70},
          {field: "estado", displayName: 'Estado', width: 95},
          {
            displayName: 'Edit',
            width: 80,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;">' +
            '<button type="button" class="btn btn-primary btn-xs">' +
            'Detalle</button>' +
            '</div>'
          }]
      };

      //giros
      $scope.gridOptionsEnviados = {
        data: 'transaccionesEnviadas',
        multiSelect: false,
        columnDefs: [
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy HH:mm:ss'", displayName: 'FECHA ENVIO', width: 115},
          {displayName: 'CLIENTE EMISOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.numeroDocumentoEmisor}} - {{row.entity.clienteEmisor}}</span></div>', width: 200},
          {displayName: 'CLIENTE RECEPTOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteReceptor}}</span></div>', width: 150},
          {displayName: 'MONEDA', cellTemplate: '<div ng-class="col.colIndex()" style="text-align: center;"><span>{{row.entity.moneda.simbolo}}</span></div>', width:30},
          {field: "monto", displayName: 'MONTO', cellFilter: "currency: ''", width: 70},
          {field: "comision", displayName: 'COMISION', cellFilter: "currency: ''", width: 70},
          {field: "lugarPagoComision", displayName: 'PAGO COMISION', width: 135},
          {field: "estado", displayName: 'Estado', width: 100},
          {
            displayName: 'Edit',
            width: 60,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;">' +
            '<button type="button" ng-click="verGiro(row.entity)" class="btn btn-primary btn-xs">' +
            'Detalle</button>' +
            '</div>'
          }]
      };

      $scope.verGiro = function (giro) {
        $state.transitionTo("app.transaccion.editarGiro", {id: giro.id});
      };

    }]);
});
