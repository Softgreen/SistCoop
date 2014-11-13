define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionBovedaBovedaController', ['$scope', '$state', '$modal', 'SessionService', 'BovedaService',
        function($scope, $state, $modal, SessionService, BovedaService) {
    		
            $scope.nuevo = function(){
                $state.transitionTo('app.transaccion.nuevaTransaccionBovedaBoveda');
            };

            $scope.loadTransaccionEnviadas = function(){
                BovedaService.getTransaccionesBovedaBovedaEnviados($scope.agenciaSession.id).then(
                    function(enviados){
                        $scope.transaccionesEnviadas = enviados;
                    }
                );
            };

            $scope.loadTransaccionRecibidas = function(){
                BovedaService.getTransaccionesBovedaBovedaRecibidos($scope.agenciaSession.id).then(
                    function(recibidos){
                        $scope.transaccionesRecibidas = recibidos;
                    }
                );
            };

            $scope.loadTransaccionEnviadas();
            $scope.loadTransaccionRecibidas();

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha", displayName:'Fecha', cellFilter:'date : "dd/MM/yyyy"', width: 70},
                    {field:"hora", displayName:'Hora', cellFilter:'date : "HH:mm:ss"', width: 60},
                    {displayName: 'Est. Solicitud', width: 85, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                    {displayName: 'Est. Confirmación',  width: 115, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                    {field:"agenciaOrigenDenominacion", displayName:'Origen'},
                    {field:"agenciaDestinoDenominacion", displayName:'Destino'},
                    {field:"monedaSimbolo", displayName:'M', width: 25},
                    {field:"monto", displayName:'Monto', cellFilter:'currency :""', width: 80},
                    {displayName: 'Voucher', width: 240, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-success btn-xs" ng-click="confirmarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-ok"></span>Confirmar</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}]
            };

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha", displayName:'Fecha', cellFilter:'date : "dd/MM/yyyy"', width: 70},
                    {field:"hora", displayName:'Hora', cellFilter:'date : "HH:mm:ss"', width: 60},
                    {displayName: 'Est. Solicitud', width: 85, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                    {displayName: 'Est. Confirmación',  width: 115, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                    {field:"agenciaOrigenDenominacion", displayName:'Origen'},
                    {field:"agenciaDestinoDenominacion", displayName:'Destino'},
                    {field:"monedaSimbolo", displayName:'M', width: 25},
                    {field: "monto", displayName: 'Monto', cellFilter: 'currency :""', width: 80},
                    {displayName: 'Voucher', width: 170, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}
                ]
            };
            
            $scope.getDisabledStateVoucher = function(row){
                if(row.estadoSolicitud == false)
                    return true;
                if(row.estadoConfirmacion == false)
                    return true;
                return false;
            };
            
            $scope.getDisabledStateButton = function(row){
                if(row.estadoSolicitud == false)
                    return true;
                if(row.estadoConfirmacion == true)
                    return true;
                return false;
            };
            
            $scope.cancelarTransaccion = function(row){
                if(!angular.isUndefined(row)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/jefeCaja/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        BovedaService.cancelarTransaccionBovedaBoveda(row.id).then(
                            function(data){
                            	$scope.loadTransaccionEnviadas();
                                $scope.loadTransaccionRecibidas();
                            }
                            ,function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }
                        );
                    }, function () {
                    });
                }
            };
            
            $scope.confirmarTransaccion = function(row){
                if(!angular.isUndefined(row)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/jefeCaja/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        BovedaService.confirmarTransaccionBovedaBoveda(row.id).then(
                            function(data){
                            	$scope.loadTransaccionEnviadas();
                                $scope.loadTransaccionRecibidas();
                            }
                            ,function error(error){
                                $scope.alerts = [{ type: "danger", msg: "Error: " + error.data.message + "."}];
                                $scope.closeAlert = function(index) {$scope.alerts.splice(index, 1);};
                            }
                        );
                    }, function () {
                    });
                }
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.transaccion.voucherTransaccionBovedaBoveda', { id: row.id });
            };

        }]);
});
