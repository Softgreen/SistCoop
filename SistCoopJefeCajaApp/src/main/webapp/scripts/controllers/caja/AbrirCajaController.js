define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('AbrirCajaController', ["$scope", "$state", '$filter', "CajaService",
        function($scope, $state, $filter, CajaService) {

            $scope.control = {"success":false, "inProcess": false};

            CajaService.getDetalle($scope.id).then(function(detalleCaja){
                for(var i = 0; i<detalleCaja.length; i++){
                    angular.forEach(detalleCaja[i].detalle, function(row){
                        row.subtotal = function(){
                            return this.valor * this.cantidad;
                        };
                    });
                }
                $scope.detalleCaja = angular.copy(detalleCaja);
            });

            $scope.loadCaja = function(){
                CajaService.getCaja($scope.id).then(function(data){
                    $scope.caja = data;
                });
            };
            $scope.loadCaja();

            $scope.myData = [];
            $scope.gridOptions = [];
            $scope.total = [];
            var gridLayoutPlugin = [];
            $scope.updateLayout = [];

            $scope.getTemplate = function(index, simbolo){
                gridLayoutPlugin[index] = new ngGridLayoutPlugin();
                $scope.updateLayout[index] = function(){
                    gridLayoutPlugin[index].updateGridLayout();
                };
                $scope.myData[index] = $scope.detalleCaja[index].detalle;
                $scope.gridOptions[index] = {
                    data: 'myData['+index+']',
                    plugins: [gridLayoutPlugin[index]],
                    multiSelect: false,
                    columnDefs: [
                        { field: "valor", cellFilter: "currency : '"+simbolo+" '", displayName: "Valor" },
                        { field: "cantidad", cellFilter: "number", displayName: "Cantidad" },
                        { field: "subtotal()", cellFilter: "currency : '' ", displayName: "Subtotal" }
                    ]
                };
                $scope.total[index] = function(){
                    var total = 0;
                    for(var i = 0; i < $scope.myData[index].length; i++){
                        total = total + ($scope.myData[index][i].valor * $scope.myData[index][i].cantidad);
                    }
                    return $filter('currency')(total, simbolo);
                };
                return $scope.gridOptions[index];
            };

            $scope.abrirCaja = function () {
                $scope.control.inProcess = true;

                CajaService.abrirCaja($scope.id).then(
                    function(data){
                        $scope.control.inProcess = false;
                        $scope.control.success = true;

                        $state.go("app.caja.buscarCaja");
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;
                        $scope.alerts = [
                            { type: 'danger', msg: 'Error:' + error.data + "." }
                        ];
                        $scope.closeAlert = function(index) {
                            $scope.alerts.splice(index, 1);
                        };
                    }
                );
            };

            $scope.cancelar = function(){
                $state.go("app.caja", null, { reload: true });
            };

            $scope.alertMessageDisplay = function(){
                if(!angular.isUndefined($scope.caja)){
                    if($scope.caja.denominacion == "undefined")
                        return true;
                    if($scope.caja.abierto == true)
                        return true;
                    else
                        return false;
                } else {
                    return false;
                }
            };

            $scope.buttonDisableState = function(){
                return $scope.alertMessageDisplay() || $scope.control.inProcess;
            };
        }]);
});
