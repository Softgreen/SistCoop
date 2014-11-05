define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearSucursalController', ['$scope','$state','focus','SucursalService',
        function($scope,$state,focus,SucursalService) {

            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                focus('focusDenominacion');
            };
            $scope.setInitialFocus();

            $scope.control = {
                success:false,
                inProcess: false,
                submitted : false
            };

            $scope.view = {
                denominacion: undefined,
                abreviatura: undefined
            };

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.crearSucursalForm.$valid) {
                    $scope.control.inProcess = true;

                    var sucursal = {
                        denominacion: $scope.view.denominacion,
                        abreviatura: $scope.view.abreviatura
                    };

                    SucursalService.crear(sucursal).then(
                        function(data){
                            $scope.redireccion();
                            $scope.control.inProcess = false;
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.redireccion = function(){
                $state.transitionTo('app.sucursal.buscarSucursal');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };


        }]);
});