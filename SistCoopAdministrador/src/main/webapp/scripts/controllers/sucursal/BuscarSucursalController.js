define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarSucursalController', ['$scope','$state','focus','SucursalService',
        function($scope,$state,focus,SucursalService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusFilterText');
            };
            $scope.setInitialFocus();


            $scope.nuevo = function(){
                $state.transitionTo('app.sucursal.nuevaSucursal');
            };

            $scope.loadSucursales = function(){
                SucursalService.getSucursales().then(function(data){
                    $scope.sucursales = data;
                });
            };
            $scope.loadSucursales();

            $scope.gridOptions = {
                data: 'sucursales',
                multiSelect: false,
                columnDefs: [
                    { field: "denominacion", displayName: "DENOMINACION"},
                    { field: "abreviatura", displayName: "ABREVIATURA"},
                    {displayName: 'ESTADO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">INACTIVO</span></div>'},
                    {displayName: 'EDIT', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}
                ]
            };

            $scope.editar = function(sucursal) {
                $state.transitionTo('app.sucursal.editarSucursal', { id: sucursal.id });
            };

        }]);
});