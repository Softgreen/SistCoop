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
          {field: "numeroDocumentoReceptor", displayName: 'Receptor', width: 70},
          {field: "clienteReceptor", displayName: 'Receptor', width: 170},

          {field: "moneda.simbolo", displayName: 'M.', width: 25},
          {field: "monto", displayName: 'Monto', cellFilter: "currency: ''", width: 60},

          {field: "comision", displayName: 'Comision', cellFilter: "currency: ''", width: 60},
          {field: "lugarPagoComision", displayName: 'Comision', width: 110},
          {displayName: 'Comision', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoPagoComision">PAGADO</span><span ng-hide="row.entity.estadoPagoComision">NO PAGADO</span></div>', width: 95},

          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Envio', width: 70},
          {field: "fechaDesembolso", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Desembolso', width: 70},
          {field: "estado", displayName: 'Estado', width: 95},
          {
            displayName: 'Edit',
            width: 135,
            cellTemplate:
            '<div class="btn-group dropdown">' +
            '<button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">' +
            'Button<span class="caret"></span>' +
            '</button>' +
            '<ul class="dropdown-menu">' +
            '<li><a href="#">Action</a></li>' +
            '<li><a href="#">Another action</a></li>' +
            '<li><a href="#">Something else here</a></li>' +
            '<li class="divider"></li>' +
            '<li><a href="#">Separated link</a></li>' +
            '</ul>' +
            '</div>'
          }]
      };

      $scope.gridOptionsEnviados = {
        data: 'transaccionesEnviadas',
        multiSelect: false,
        columnDefs: [
          {field: "numeroDocumentoReceptor", displayName: 'Receptor', width: 70},
          {field: "clienteReceptor", displayName: 'Receptor', width: 170},

          {field: "moneda.simbolo", displayName: 'M.', width: 25},
          {field: "monto", displayName: 'Monto', cellFilter: "currency: ''", width: 60},

          {field: "comision", displayName: 'Comision', cellFilter: "currency: ''", width: 60},
          {field: "lugarPagoComision", displayName: 'Comision', width: 110},
          {displayName: 'Comision', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoPagoComision">PAGADO</span><span ng-hide="row.entity.estadoPagoComision">NO PAGADO</span></div>', width: 95},

          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Envio', width: 70},
          {field: "fechaDesembolso", cellFilter: "date : 'dd/MM/yyyy'", displayName: 'F.Desembolso', width: 70},
          {field: "estado", displayName: 'Estado', width: 95},
          {
            displayName: 'Edit',
            width: 135,
            cellTemplate:
            '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;">' +

            '<div class="btn-group dropdown">' +
              '<button type="button" class="btn btn-primary btn-xs dropdown-toggle" data-toggle="dropdown">' +
              'Button<span class="caret"></span>' +
              '</button>' +
              '<ul class="dropdown-menu">' +
                '<li><a href="#">Action</a></li>' +
                '<li><a href="#">Another action</a></li>' +
                '<li><a href="#">Something else here</a></li>' +
                '<li class="divider"></li>' +
                '<li><a href="#">Separated link</a></li>' +
              '</ul>' +
            '</div>' +

          '</div>'
          }]
      };

    }]);
});
