define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('AbrirBovedaController', ['$scope','$state','$filter','BovedaService',
        function($scope,$state,$filter,BovedaService) {
    	
            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };
            
            $scope.detalle = [];
            
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
                BovedaService.getDetalle($scope.id).then(function(data){
                    angular.forEach(data, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        };
                    });
                    $scope.detalle = data;
                });
            };
            
            $scope.loadBoveda();
            $scope.loadDetalle();

            $scope.getTotal = function() {
                var total = 0;
                if(!angular.isUndefined($scope.detalle)){
                    for(var i = 0; i < $scope.detalle.length; i++){
                        total = total + $scope.detalle[i].subtotal();
                    }
                }
                return $filter('currency')(total," ");
            };

            $scope.gridOptions = {
                data: 'detalle',
                multiSelect: false,
                columnDefs: [
                    { field: "valor", cellFilter: "currency: ''",  displayName: "VALOR" },
                    { field: "cantidad", displayName: "CANTIDAD" },
                    { field: "subtotal()", displayName: "SUBTOTAL" }
                ]
            };

            $scope.abrirBoveda = function(){
                $scope.control.inProcess = true;
                BovedaService.abrirBoveda($scope.id).then(
                    function(data){
                        $state.go('app.boveda.buscarBoveda');
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