define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTrabajadorController', ['$scope','$state','focus','TrabajadorService','AgenciaService',
        function($scope,$state,focus,TrabajadorService,AgenciaService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusFilterText');
            };
            $scope.setInitialFocus();

            $scope.view = {
                filterText: undefined,
                idAgencia: undefined
            };

            $scope.nuevo = function(){
                $state.transitionTo('app.trabajador.nuevoTrabajador');
            };

            $scope.loadTrabajadores = function(){
                TrabajadorService.getTrabajadores().then(function(data){
                    $scope.trabajadores = data;
                });
            };
            $scope.loadAgencias = function(){
                AgenciaService.getAgencias().then(function(data){
                   $scope.agencias = data;
                });
            };
            $scope.loadTrabajadores();
            $scope.loadAgencias();

            $scope.gridOptions = {
                data: 'trabajadores',
                multiSelect: false,
                columnDefs: [
                    { field: "personaNatural.tipoDocumento.abreviatura", displayName: "TIPO DOC.", width:80},
                    { field: "personaNatural.numeroDocumento", displayName: "NUM. DOC.", width:100},
                    { field: "personaNatural.apellidoPaterno", displayName: "AP. PATERNO", width:130},
                    { field: "personaNatural.apellidoMaterno", displayName: "AP. MATERNO", width:130},
                    { field: "personaNatural.nombres", displayName: "NOMBRES"},
                    { field: "usuario", displayName: "USUARIO", width:110},
                    { field: "agencia.abreviatura", displayName: "AGENCIA", width:100},
                    {displayName: 'ESTADO', width:70, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">INACTIVO</span></div>'},
                    {displayName: 'EDIT', width:70, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="editar(row.entity)"><span class="glyphicon glyphicon-share"></span>Edit</button></div>'}
                ]
            };

            $scope.search = function(){
                TrabajadorService.getTrabajadores($scope.view.idAgencia, $scope.view.filterText).then(function(data){
                    $scope.trabajadores = data;
                });
            };

            $scope.editar = function(trabajador) {
                $state.transitionTo('app.trabajador.editarTrabajador', { id: trabajador.id });
            };

        }]);
});