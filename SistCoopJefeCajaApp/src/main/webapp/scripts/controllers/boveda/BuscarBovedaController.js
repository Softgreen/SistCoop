define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarBovedaController', ['$scope','$state','focus','BovedaService',
        function($scope,$state,focus,BovedaService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusFilterText');
            };
            $scope.setInitialFocus();


            $scope.nuevo = function(){
                $state.transitionTo('app.boveda.nuevaBoveda');
            };

            $scope.loadBovedas = function(){
                BovedaService.getBovedas($scope.agenciaSession.id).then(function(data){
                    $scope.bovedas = data;
                });
            };
            $scope.loadBovedas();

            $scope.gridOptions = {
                data: 'bovedas',
                multiSelect: false,
                columnDefs: [
                    { field: "denominacion", displayName: "DENOMINACION"},
                    { field: "abierto", displayName: "ABIERTO/CERRADO"},
                    { field: "estadoMovimiento", displayName: "MOVIMIENTO"},
                    { field: "moneda.denominacion", displayName: "MONEDA"},
                    { field: "estado", displayName: "ESTADO"},
                    {displayName: 'EDIT', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}
                ]
            };

            $scope.editar = function(boveda) {
                $state.transitionTo('app.boveda.editarBoveda', { id: boveda.id });
            };

        }]);
});