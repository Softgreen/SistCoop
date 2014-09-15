define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarCajaController', ['$scope','$state','focus','BovedaService','CajaService',
        function($scope,$state,focus,BovedaService,CajaService) {

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

            $scope.picklist = {
                boveda: undefined
            };

            $scope.view = {
                id: undefined,
                denominacion: undefined,
                abreviatura: undefined,
                bovedas: []
            };

            $scope.loadBovedas = function(){
                BovedaService.getBovedas($scope.agenciaSession.id).then(function(data){
                    $scope.picklist.boveda = data;
                    $scope.loadBovedasOfCaja();
                });
            };

            $scope.loadCaja = function(){
                if(!angular.isUndefined($scope.id)){
                    CajaService.getCaja($scope.id).then(
                        function(data){
                            $scope.view.id= data.id;
                            $scope.view.denominacion = data.denominacion;
                            $scope.view.abreviatura = data.abreviatura;
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }
            };
            $scope.loadBovedasOfCaja = function(){
              if(!angular.isUndefined($scope.id)){
                  CajaService.getBovedas($scope.id).then(
                      function(data){
                          for(var i = 0; i<data.length; i++){
                              for(var j = 0; j<$scope.picklist.boveda.length; j++){
                                  if(data[i].id == $scope.picklist.boveda[j].id){
                                      $scope.view.bovedas.push($scope.picklist.boveda[j].id);
                                  }
                              }
                          }
                      },
                      function error(error){
                          $scope.control.inProcess = false;
                          $scope.control.success = false;
                          $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                          $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                      }
                  );
              }
            };

            //logic
            $scope.crearTransaccion = function(){
                if ($scope.crearCajaForm.$valid) {
                    $scope.control.inProcess = true;

                    var caja = {
                        denominacion: $scope.view.denominacion,
                        abreviatura: $scope.view.abreviatura,
                        bovedas: $scope.view.bovedas
                    };

                    CajaService.actualizar($scope.id, caja).then(
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
                $state.transitionTo('app.caja.buscarCaja');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };

            $scope.loadBovedas();
            $scope.loadCaja();

        }]);
});