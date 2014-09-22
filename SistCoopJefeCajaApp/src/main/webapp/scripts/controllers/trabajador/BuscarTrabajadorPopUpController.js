define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTrabajadorPopUpController', ['$scope','$timeout','$modalInstance','idAgencia','focus','TrabajadorService',
        function($scope,$timeout,$modalInstance,idAgencia,focus,TrabajadorService) {

            $scope.focusElements = {
                filterText: 'focusFilterText'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                $timeout(function() {
                    focus($scope.focusElements.filterText);
                }, 200);
            };
            $scope.setInitialFocus();

            $scope.filterOptions = {
                filterText: ''
            };
            //configurar tabla
            $scope.trabajadoresList = [];

            //cargar datos por primera vez
            $scope.getPagedDataInitial = function () {
                TrabajadorService.getTrabajadores(idAgencia).then(function(data){
                    $scope.trabajadoresList = data;
                });
            };
            $scope.getPagedDataInitial();

            //buscar con enter
            $scope.getPagedDataSearched = function () {
                if ($scope.filterOptions.filterText) {
                    var ft = $scope.filterOptions.filterText.toUpperCase();
                    TrabajadorService.getTrabajadores(idAgencia, ft).then(function (data){
                        $scope.trabajadoresList = data;
                    });
                } else {
                    $scope.getPagedDataInitial();
                }
                $scope.setInitialFocus();
            };

            var gridLayoutPlugin = new ngGridLayoutPlugin();
            $scope.gridOptions = {
                data: 'trabajadoresList',
                selectedItems: [],
                multiSelect: false,
                plugins: [gridLayoutPlugin],
                columnDefs: [
                    {field:"personaNatural.apellidoPaterno", displayName:'AP.PATERNO'},
                    {field:"personaNatural.apellidoMaterno", displayName:'AP.MATERNO'},
                    {field:"personaNatural.nombres", displayName:'NOMBRES'},
                    {field:"usuario", displayName:'USUARIO'},
                    {displayName: 'ESTADO', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">INACTIVO</span></div>'},
                    {displayName: 'Select', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="selectTrabajador(row.entity)"><span class="glyphicon glyphicon-share"></span>Select</button></div>'}
                ]
            };
            $scope.updateGridLayout = function(){
                gridLayoutPlugin.updateGridLayout();
            };
            setTimeout(function () {
                $scope.updateGridLayout();
            }, 500);

            $scope.selectTrabajador = function(row){
                $scope.trabajadorSelected = row;
                if ($scope.trabajadorSelected !== undefined && $scope.trabajadorSelected !== null) {
                    $modalInstance.close($scope.trabajadorSelected);
                }
            }

            $scope.ok = function () {
                var trabajador = $scope.gridOptions.selectedItems[0];
                if(trabajador !== undefined && trabajador !== null){
                    $scope.trabajadorSelected = trabajador;
                }
                if ($scope.trabajadorSelected !== undefined && $scope.trabajadorSelected !== null) {
                    $modalInstance.close($scope.trabajadorSelected);
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
});