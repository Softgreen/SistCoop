define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarCajaController', ['$scope','$state','focus','CajaService',
        function($scope,$state,focus,CajaService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusFilterText');
            };
            $scope.setInitialFocus();


            $scope.nuevo = function(){
                $state.transitionTo('app.boveda.nuevaCaja');
            };

            $scope.loadCajas = function(){
                CajaService.getCajas($scope.agenciaSession.id).then(function(data){
                    $scope.cajas = data;
                });
            };
            $scope.loadCajas();

            $scope.gridOptions = {
                data: 'cajas',
                multiSelect: false,
                columnDefs: [
                    { field: "denominacion", displayName: "DENOMINACION"},
                    { field: "abierto", displayName: "ABIERTO/CERRADO"},
                    { field: "estadoMovimiento", displayName: "MOVIMIENTO"},
                    { field: "estado", displayName: "ESTADO"},
                    {displayName: 'EDIT', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}
                ]
            };

            $scope.editar = function(boveda) {
                $state.transitionTo('app.boveda.editarCaja', { id: boveda.id });
            };

        }]);
});