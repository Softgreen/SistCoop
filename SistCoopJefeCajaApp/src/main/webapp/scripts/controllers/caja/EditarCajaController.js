define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarCajaController', ['$scope','$state','$modal','focus','BovedaService','CajaService',
        function($scope,$state,$modal,focus,BovedaService,CajaService) {

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

            $scope.loadTrabajadoresCaja = function(){
                CajaService.getTrabajadorse($scope.id).then(
                    function(data){
                        $scope.trabajadores = data;
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;
                        $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
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

            $scope.openBuscarTrabajador = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'views/jefeCaja/trabajador/buscarTrabajadorPopUp.html',
                    controller: "BuscarTrabajadorPopUpController",
                    size: 'lg',
                    resolve: {
                        idAgencia: function () {
                            return $scope.agenciaSession.id;
                        }
                    }
                });
                modalInstance.result.then(function (trabajador) {
                    CajaService.crearTrabajador($scope.id, trabajador).then(
                        function(data){
                            $scope.trabajadores.push(trabajador);

                            $scope.control.inProcess = false;
                            $scope.alerts = [{ type: "success", msg: "Trabajador asignado."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        },
                        function error(error){
                            $scope.control.inProcess = false;
                            $scope.control.success = false;
                            $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                            $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                        }
                    );
                }, function () {

                });
            };

            $scope.eliminarTrabajador = function(index){
                CajaService.eliminarTrabajador($scope.id, $scope.trabajadores[index].id).then(
                    function(data){
                        $scope.trabajadores.splice(index, 1);
                        $scope.control.inProcess = false;
                        $scope.alerts = [{ type: "success", msg: "Trabajador eliminado."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    },
                    function error(error){
                        $scope.control.inProcess = false;
                        $scope.control.success = false;
                        $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                        $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                    }
                );
            };

            $scope.redireccion = function(){
                $state.transitionTo('app.caja.buscarCaja');
            };

            $scope.cancelar = function () {
                $scope.redireccion();
            };

            $scope.loadBovedas();
            $scope.loadCaja();
            $scope.loadTrabajadoresCaja();

        }]);
});