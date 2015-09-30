define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('BuscarGirosController', ['$scope', '$state', '$filter', '$modal', 'CajaService', 'AgenciaService', 'SessionService',
    function ($scope, $state, $filter, $modal, CajaService, AgenciaService, SessionService) {

      $scope.estadoGiro = "ENVIADO";

      $scope.nuevo = function () {
        $state.transitionTo('app.transaccion.crearGiro');
      };

      /*$scope.loadGirosEnviados = function () {
        AgenciaService.getGirosEnviados($scope.agenciaSession.id, $scope.estadoGiro).then(function (enviados) {
          $scope.transaccionesEnviadas = enviados;
        });
      };
      $scope.loadGirosRecibidos = function () {
        AgenciaService.getGirosRecibidos($scope.agenciaSession.id, $scope.estadoGiro).then(function (recibidos) {
          $scope.transaccionesRecibidas = recibidos;
        });
      };*/

      //$scope.loadGirosEnviados();
     // $scope.loadGirosRecibidos();

      /*$scope.$watch("estadoGiro", function (newValue, oldValue) {
        if (newValue != oldValue) {
          $scope.loadGirosEnviados();
          $scope.loadGirosRecibidos();
        }
      }, true);*/

      $scope.filterOptionsEnviados = {
        filterText: "",
        useExternalFilter: true
      };
      $scope.pagingOptionsEnviados = {
        pageSizes: [10, 20, 50],
        pageSize: 10,
        currentPage: 1
      };
      $scope.totalServerItemsEnviados = 0;

      $scope.searchEnviados = function () {
        var ft = $scope.filterOptionsEnviados.filterText;
        var desde = ($scope.pagingOptionsEnviados.pageSize * $scope.pagingOptionsEnviados.currentPage) - $scope.pagingOptionsEnviados.pageSize;
        var hasta = $scope.pagingOptionsEnviados.pageSize;
        AgenciaService.getGirosEnviados($scope.agenciaSession.id, $scope.estadoGiro, ft, desde, hasta).then(function (enviados) {
          $scope.transaccionesEnviadas = enviados;
        });
        AgenciaService.countEnviados($scope.agenciaSession.id).then(function (data) {
          $scope.totalServerItemsEnviados = data;
        });
      };
      //$scope.searchEnviados();

      $scope.$watch(function () {
        return {
          currentPage: $scope.pagingOptionsEnviados.currentPage,
          pageSize: $scope.pagingOptionsEnviados.pageSize
        };
      }, function (newVal, oldVal) {
        if (newVal.pageSize !== oldVal.pageSize) {
          $scope.pagingOptionsEnviados.currentPage = 1;
        }
        $scope.searchEnviados();
      }, true);

      $scope.gridOptionsEnviados = {
        data: 'transaccionesEnviadas',
        multiSelect: false,
        enablePaging: true,
        showFooter: true,
        totalServerItems: 'totalServerItemsEnviados',
        pagingOptions: $scope.pagingOptionsEnviados,
        filterOptions: $scope.filterOptionsEnviados,
        columnDefs: [
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy HH:mm:ss'", displayName: 'FECHA ENVIO', width: 115},
          {displayName: 'CLIENTE EMISOR', cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteEmisor}}</span></div>'},
          {
            displayName: 'CLIENTE RECEPTOR',
            cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteReceptor}}</span></div>',
            width: 160
          },
          {
            displayName: 'AG. DESTINO',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col8 colt8"><span>{{row.entity.agenciaDestino.abreviatura}}</span></div>',
            width: 90
          },
          {
            displayName: 'MONEDA',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col2 colt2" style="text-align: center;"><span>{{row.entity.moneda.simbolo}}</span></div>',
            width: 30
          },
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



      $scope.filterOptionsRecibidos = {
        filterText: "",
        useExternalFilter: true
      };
      $scope.pagingOptionsRecibidos = {
        pageSizes: [10, 20, 50],
        pageSize: 10,
        currentPage: 1
      };
      $scope.totalServerItemsRecibidos = 0;

      $scope.searchRecibidos = function () {
        var ft = $scope.filterOptionsRecibidos.filterText;
        var desde = ($scope.pagingOptionsRecibidos.pageSize * $scope.pagingOptionsRecibidos.currentPage) - $scope.pagingOptionsRecibidos.pageSize;
        var hasta = $scope.pagingOptionsRecibidos.pageSize;
        AgenciaService.getGirosRecibidos($scope.agenciaSession.id, $scope.estadoGiro, ft, desde, hasta).then(function (recibidos) {
          $scope.transaccionesRecibidas = recibidos;
        });
        AgenciaService.countRecibidos($scope.agenciaSession.id).then(function (data) {
          $scope.totalServerItemsRecibidos = data;
        });
      };
      //$scope.searchRecibidos();

      $scope.$watch(function () {
        return {
          currentPage: $scope.pagingOptionsRecibidos.currentPage,
          pageSize: $scope.pagingOptionsRecibidos.pageSize
        };
      }, function (newVal, oldVal) {
        if (newVal.pageSize !== oldVal.pageSize) {
          $scope.pagingOptionsRecibidos.currentPage = 1;
        }
        $scope.searchRecibidos();
      }, true);
      $scope.gridOptionsRecibidos = {
        data: 'transaccionesRecibidas',
        multiSelect: false,
        columnDefs: [
          {field: "fechaEnvio", cellFilter: "date : 'dd/MM/yyyy HH:mm:ss'", displayName: 'FECHA ENVIO', width: 115},
          {
            displayName: 'CLIENTE RECEPTOR',
            cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteReceptor}}</span></div>'
          },
          {
            displayName: 'CLIENTE EMISOR',
            cellTemplate: '<div ng-class="col.colIndex()"><span>{{row.entity.clienteEmisor}}</span></div>',
            width: 160
          },
          {
            displayName: 'AG. ORIGEN',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col8 colt8"><span>{{row.entity.agenciaOrigen.abreviatura}}</span></div>',
            width: 90
          },
          {
            displayName: 'MONEDA',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col2 colt2" style="text-align: center;"><span>{{row.entity.moneda.simbolo}}</span></div>',
            width: 30
          },
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

      $scope.$watch("estadoGiro", function (newValue, oldValue) {
        if (newValue != oldValue) {
          $scope.searchEnviados();
          $scope.searchRecibidos();
        }
      }, true);

      $scope.verGiro = function (giro) {
        $state.transitionTo("app.transaccion.editarGiro", {id: giro.id});
      };

    }]);
});
