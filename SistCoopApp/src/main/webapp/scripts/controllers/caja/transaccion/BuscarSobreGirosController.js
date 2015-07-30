define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('BuscarSobreGirosController', ['$scope', '$state', '$filter', '$modal', 'CajaService', 'SobreGiroService', 'SessionService',
    function ($scope, $state, $filter, $modal, CajaService, SobreGiroService, SessionService) {

      $scope.view = {
        filterText: ""
      };

      $scope.filterOptions = {
        filterText: "",
        useExternalFilter: true
      };
      $scope.totalServerItems = 0;
      $scope.pagingOptions = {
        pageSizes: [10, 20, 40],
        pageSize: 10,
        currentPage: 1
      };
      $scope.setPagingData = function (data, page, pageSize) {
        $scope.transacciones = data;
        if (!$scope.$$phase) {
          $scope.$apply();
        }
      };
      $scope.getDesde = function () {
        return ($scope.pagingOptions.pageSize * $scope.pagingOptions.currentPage) - $scope.pagingOptions.pageSize;
      };
      $scope.getHasta = function () {
        return ($scope.pagingOptions.pageSize);
      };
      //carga inicial de datos
      $scope.getPagedDataInitial = function () {
        $scope.pagingOptions.currentPage = 1;
        SobreGiroService.getSobreGiros({
          estado: ['ACTIVO'],
          filterText: '',
          offset: $scope.getDesde(),
          limit: $scope.getHasta()
        }).then(function (data) {
          $scope.transacciones = data;
          $scope.setPagingData($scope.transacciones, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
        });

        $scope.totalServerItems = 10000;
      };
      $scope.getPagedDataInitial();
      $scope.getPagedDataSearched = function () {
        if ($scope.filterOptions.filterText) {
          var ft = $scope.filterOptions.filterText.toUpperCase();
          SobreGiroService.getSobreGiros({
            estado: ['ACTIVO'],
            filterText: ft,
            offset: $scope.getDesde(),
            limit: $scope.getHasta()
          }).then(function (data) {
            $scope.view.filterText = ft;
            $scope.pagingOptions.currentPage = 1;
            $scope.setPagingData(data, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
          });
          $scope.totalServerItems = 10000;
        } else {
          $scope.getPagedDataInitial();
        }
        $scope.setInitialFocus();
      };

      $scope.$watch(
        function () {
          return {
            currentPage: $scope.pagingOptions.currentPage,
            pageSize: $scope.pagingOptions.pageSize
          };
        },
        function (newVal, oldVal) {
          if (newVal.pageSize !== oldVal.pageSize) {
            $scope.pagingOptions.currentPage = 1;
          }

          var ft = $scope.filterOptions.filterText.toUpperCase();
          SobreGiroService.getSobreGiros({
            estado: ['ACTIVO'],
            filterText: ft,
            offset: $scope.getDesde(),
            limit: $scope.getHasta()
          }).then(function (data) {
            $scope.transacciones = data;
            $scope.setPagingData($scope.transacciones, $scope.pagingOptions.currentPage, $scope.pagingOptions.pageSize);
          });
        }, true);


      $scope.nuevo = function () {
        $state.transitionTo('app.transaccion.crearSobreGiro');
      };

      $scope.loadSobreGiros = function () {
        //ACA PONER PARAMETROS
        SobreGiroService.getSobreGiros().then(function (data) {
          $scope.transacciones = data;
        });
      };


      $scope.gridOptions = {
        data: 'transacciones',
        multiSelect: false,
        enablePaging: true,
        showFooter: true,
        totalServerItems: 'totalServerItems',
        pagingOptions: $scope.pagingOptions,
        filterOptions: $scope.filterOptions,
        columnDefs: [
          {
            displayName: 'Doc.',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText" style="text-align: left;">' +
            '<span ng-show="row.entity.socio.personaNatural" ng-bind-template="{{row.entity.socio.personaNatural.tipoDocumento.abreviatura}}:{{row.entity.socio.personaNatural.numeroDocumento}}"></span>' +
            '<span ng-show="row.entity.socio.personaJuridica" ng-bind-template="{{row.entity.socio.personaJuridica.tipoDocumento.abreviatura}}:{{row.entity.socio.personaJuridica.numeroDocumento}}"></span>' +
            '</div>',
            width: 90
          },
          {
            displayName: 'Cliente',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText" style="text-align: left;">' +
            '<span ng-show="row.entity.socio.personaNatural" ng-bind-template="{{row.entity.socio.personaNatural.apellidoPaterno}} {{row.entity.socio.personaNatural.apellidoMaterno}}, {{row.entity.socio.personaNatural.nombres}}"></span>' +
            '<span ng-show="row.entity.socio.personaJuridica" ng-bind-template="{{row.entity.socio.personaJuridica.razonSocial}}"></span>' +
            '</div>',
            width: 210
          },
          {field: "fechaCreacion", displayName: "F.Crea", cellFilter: "date: 'dd/MM/yyyy'", width: 80},
          {field: "fechaLimitePago", displayName: "F.Pago", cellFilter: "date: 'dd/MM/yyyy'", width: 80},
          {field: "moneda.simbolo", displayName: "M.", width: 30},
          {field: "monto", displayName: "MONTO", cellFilter: "currency: ''", width: 100},
          {field: "interes", displayName: "INTERES", cellFilter: "currency: ''", width: 100},
          {field: "estado", displayName: "ESTADO", width: 90},
          {
            displayName: 'EDITAR',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Pagar</button></div>',
            width: 80
          }]
      };

      $scope.editar = function (row) {
        $state.transitionTo("app.transaccion.editarSobreGiro", {id: row.id});
      };

    }]);
});
