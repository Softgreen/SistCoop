define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('BuscarSobreGirosController', ['$scope', '$state', '$filter', '$modal', 'CajaService', 'SobreGiroService', 'SessionService',
    function ($scope, $state, $filter, $modal, CajaService, SobreGiroService, SessionService) {

      $scope.nuevo = function () {
        $state.transitionTo('app.transaccion.crearSobreGiro');
      };

      $scope.loadSobreGiros = function () {
        //ACA PONER PARAMETROS
        SobreGiroService.getSobreGiros().then(function (data) {
          $scope.transacciones = data;
        });
      };

      //cobro de giros
      $scope.gridOptions = {
        data: 'transacciones',
        multiSelect: false,
        columnDefs: [
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy HH:mm:ss'", displayName: 'FECHA ENVIO', width: 115},
          {displayName: 'CLIENTE RECEPTOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteReceptor}}</span></div>'},
          {displayName: 'CLIENTE EMISOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteEmisor}}</span></div>', width: 160},
          {displayName: 'AG. ORIGEN', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col8 colt8"><span>{{row.entity.agenciaOrigen.abreviatura}}</span></div>', width: 90},
          {displayName: 'MONEDA', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col2 colt2" style="text-align: center;"><span>{{row.entity.moneda.simbolo}}</span></div>', width:30},
          {field: "monto", displayName: 'MONTO', cellFilter: "currency: ''", width: 70},
          {field: "comision", displayName: 'COMISION', cellFilter: "currency: ''", width: 70},
          {field: "lugarPagoComision", displayName: 'PAGO COMISION', width: 100},
          {field: "estado", displayName: 'ESTADO', width: 80},
          {
              displayName: 'EDIT',
              width: 65,
              cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;">' +
              '<button type="button" ng-click="verGiro(row.entity)" class="btn btn-primary btn-xs">' +
              'Detalle</button>' +
              '</div>'
            }]
      };

      $scope.verSobreGiro = function (row) {
        $state.transitionTo("app.transaccion.editarSobreGiro", {id: row.id});
      };

    }]);
});
