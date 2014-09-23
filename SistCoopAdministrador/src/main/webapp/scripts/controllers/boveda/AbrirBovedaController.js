define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('AbrirBovedaController', ['$scope','$state','BovedaService',
        function($scope,$state,BovedaService) {

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.detalle = [];

            $scope.loadDetalle = function(){
                BovedaService.getDetalle($scope.id).then(function(data){
                    angular.forEach(data, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        }
                    });
                    $scope.detalle = data;
                });
            };
            $scope.loadDetalle();

            $scope.getTotal = function() {
                var total = 0;
                if(!angular.isUndefined($scope.detalle)){
                    for(var i = 0; i < $scope.detalle.length; i++){
                        total = total + $scope.detalle[i].subtotal();
                    }
                }
                return total;
            };

            $scope.gridOptions = {
                data: 'detalle',
                multiSelect: false,
                columnDefs: [
                    { field: "valor", displayName: "VALOR" },
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