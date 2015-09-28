define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('BuscarGirosController', ['$scope', '$state', '$filter', '$modal', 'CajaService', 'AgenciaService', 'SessionService',
    function ($scope, $state, $filter, $modal, CajaService, AgenciaService, SessionService) {

      $scope.estadoGiro = "ENVIADO";

      $scope.nuevo = function () {
        $state.transitionTo('app.transaccion.crearGiro');
      };

      $scope.loadGirosEnviados = function () {
        AgenciaService.getGirosEnviados($scope.agenciaSession.id, $scope.estadoGiro).then(function (enviados) {
          $scope.transaccionesEnviadas = enviados;
        });
      };
      $scope.loadGirosRecibidos = function () {
        AgenciaService.getGirosRecibidos($scope.agenciaSession.id, $scope.estadoGiro).then(function (recibidos) {
          $scope.transaccionesRecibidas = recibidos;
        });
      };

      $scope.loadGirosEnviados();
      $scope.loadGirosRecibidos();

      $scope.$watch("estadoGiro", function(newValue, oldValue){
        if(newValue != oldValue){
          $scope.loadGirosEnviados();
          $scope.loadGirosRecibidos();
        }
      }, true);

      //cobro de giros
      $scope.gridOptionsRecibidos = {
        data: 'transaccionesRecibidas',
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

      //giros
      $scope.gridOptionsEnviados = {
        data: 'transaccionesEnviadas',
        multiSelect: false,
        columnDefs: [
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy HH:mm:ss'", displayName: 'FECHA ENVIO', width: 115},
          {displayName: 'CLIENTE EMISOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteEmisor}}</span></div>'},
          {displayName: 'CLIENTE RECEPTOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteReceptor}}</span></div>', width: 160},
          {displayName: 'AG. DESTINO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col8 colt8"><span>{{row.entity.agenciaDestino.abreviatura}}</span></div>', width: 90},
          {displayName: 'MONEDA', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col2 colt2" style="text-align: center;"><span>{{row.entity.moneda.simbolo}}</span></div>', width:30},
          {field: "monto", displayName: 'MONTO', cellFilter: "currency: ''", width: 70},
          {field: "comision", displayName: 'COMISION', cellFilter: "currency: ''", width: 70},
          {field: "lugarPagoComision", displayName: 'PAGO COMISION', width: 100},
          {field: "estado", displayName: 'ESTADO', width: 80},
          {
            displayName: 'EDIT',
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
