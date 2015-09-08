define(['../module'], function (controllers) {
  'use strict';
  controllers.controller('BuscarCajaController', ['$scope', '$state', 'focus', 'CajaService',
    function ($scope, $state, focus, CajaService) {

      $scope.setInitialFocus = function ($event) {
        if (!angular.isUndefined($event))
          $event.preventDefault();
        focus('focusFilterText');
      };
      $scope.setInitialFocus();


      $scope.nuevo = function () {
        $state.transitionTo('app.caja.nuevaCaja');
      };

      $scope.loadCajas = function () {
        CajaService.getCajas($scope.agenciaSession.id).then(function (data) {
          $scope.cajas = data;
        });
      };
      $scope.loadCajas();

      $scope.gridOptions = {
        data: 'cajas',
        multiSelect: false,
        columnDefs: [
          {field: "denominacion", displayName: "DENOMINACION", width: '15%'},
          {
            displayName: 'ABIERTO/CERRADO',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.abierto">ABIERTO</span><span ng-hide="row.entity.abierto">CERRADO</span></div>',
            width: '12%'
          },
          {
            displayName: 'MOVIMIENTO',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoMovimiento">DESCONGELADO</span><span ng-hide="row.entity.estadoMovimiento">CONGELADO</span></div>',
            width: '12%'
          },
          {field: "bovedas", displayName: "BOVEDAS", width: '23%'},
          {field: "saldos", displayName: "SALDOS", width: '18%'},
          {
            displayName: 'ESTADO',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">INACTIVO</span></div>',
            width: '10%'
          },
          {
            displayName: 'EDIT',
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>',
            width: '9%'
          }
        ]
      };

      $scope.editar = function (boveda) {
        $state.transitionTo('app.caja.editarCaja', {id: boveda.id});
      };

    }]);
});
