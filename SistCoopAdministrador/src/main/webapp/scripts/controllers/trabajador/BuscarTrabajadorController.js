define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTrabajadorController', ['$scope','$state','focus','TrabajadorService',
        function($scope,$state,focus,TrabajadorService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusFilterText');
            };
            $scope.setInitialFocus();


            $scope.nuevo = function(){
                $state.transitionTo('app.trabajador.nuevoTrabajador');
            };

            $scope.loadTrabajadores = function(){
                TrabajadorService.getTrabajadores().then(function(data){
                    $scope.trabajadores = data;
                });
            };
            $scope.loadTrabajadores();

            $scope.gridOptions = {
                data: 'trabajadores',
                multiSelect: false,
                columnDefs: [
                    { field: "personaNatural.tipoDocumento.abreviatura", displayName: "DOCUMENTO"},
                    { field: "personaNatural.numeroDocumento", displayName: "NRO DOC."},
                    { field: "personaNatural.apellidoPaterno", displayName: "AP.PATERNO"},
                    { field: "personaNatural.apellidoMaterno", displayName: "AP.MATERNO"},
                    { field: "personaNatural.nombres", displayName: "NOMBRES"},
                    { field: "usuario", displayName: "USUARIO"},
                    {displayName: 'ESTADO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">INACTIVO</span></div>'},
                    {displayName: 'EDIT', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}
                ]
            };

            $scope.editar = function(trabajador) {
                $state.transitionTo('app.trabajador.editarTrabajador', { id: trabajador.id });
            };

        }]);
});