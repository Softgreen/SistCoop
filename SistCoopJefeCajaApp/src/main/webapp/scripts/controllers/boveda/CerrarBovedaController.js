define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CerrarBovedaController', ['$scope','$state','$filter','BovedaService',
        function($scope,$state,$filter,BovedaService) {

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.boveda = {};
            $scope.detalleOld = [];
            $scope.detalleNew = [];

            $scope.loadBoveda = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.findById($scope.id).then(
                        function(data){
                            $scope.boveda = data;
                        }, function error(error){
                            $scope.alerts = [{ type: "danger", msg: "Error: No se pudo cargar la boveda."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.loadDetalle = function(){
                if(!angular.isUndefined($scope.id)){
                    BovedaService.getDetallePenultimo($scope.id).then(function(data){
                        angular.forEach(data, function(row){
                            row.subtotal = function(){
                                return this.valor * this.cantidad;
                            }
                        });
                        $scope.detalleOld = data;
                    });
                    BovedaService.getDetalle($scope.id).then(function(data){
                        angular.forEach(data, function(row){
                            row.subtotal = function(){
                                return this.valor * this.cantidad;
                            }
                        });
                        $scope.detalleNew = data;
                    });
                }
            };

            $scope.loadBoveda();
            $scope.loadDetalle();

            $scope.getTotalOld = function() {
                var total = 0;
                if(!angular.isUndefined($scope.detalleOld)){
                    for(var i = 0; i < $scope.detalleOld.length; i++){
                        total = total + $scope.detalleOld[i].subtotal();
                    }
                }
                return $filter('currency')(total," ");
            };
            $scope.getTotalNew = function() {
                var total = 0;
                if(!angular.isUndefined($scope.detalleNew)){
                    for(var i = 0; i < $scope.detalleNew.length; i++){
                        total = total + $scope.detalleNew[i].subtotal();
                    }
                }
                return $filter('currency')(total," ");
            };

            var gridLayoutPluginOld = new ngGridLayoutPlugin();
            var gridLayoutPluginNew = new ngGridLayoutPlugin();
            $scope.gridOptionsOld = {
                plugins: [gridLayoutPluginOld],
                data: 'detalleOld',
                multiSelect: false,
                columnDefs: [
                    { field: "valor", cellFilter: "currency: ''", displayName: "VALOR" },
                    { field: "cantidad", displayName: "CANTIDAD" },
                    { field: "subtotal()", cellFilter: "currency: ''", displayName: "SUBTOTAL" }
                ]
            };
            $scope.gridOptionsNew = {
                plugins: [gridLayoutPluginNew],
                data: 'detalleNew',
                multiSelect: false,
                enableCellSelection: true,
                enableRowSelection: false,
                enableCellEditOnFocus: true,
                columnDefs: [
                    { field: "valor", cellFilter: "currency: ''", displayName: "VALOR", enableCellEdit: false  },
                    { field: "cantidad", displayName: "CANTIDAD", enableCellEdit: true  },
                    { field: "subtotal()",cellFilter: "currency: ''", displayName: "SUBTOTAL", enableCellEdit: false  }
                ]
            };

            $scope.cerrarBoveda = function(){
                $scope.control.inProcess = true;
                BovedaService.cerrarBoveda($scope.id).then(
                    function(data){
                        $state.go('app.boveda.voucherCerrarBoveda', {id: data.id})
                        $scope.control.inProcess = false;
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;
                        $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            };

            $scope.cancelar = function(){
                $state.go('app.boveda.editarBoveda', {id: $scope.id});
            };

        }]);
});