define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionBovedaCajaController', ['$scope', '$state', '$modal', 'SessionService', 'BovedaService',
        function($scope, $state, $modal, SessionService, BovedaService) {
    		
            $scope.nuevo = function(){
                $state.transitionTo('app.transaccion.nuevaTransaccionBovedaCaja');
            };

            $scope.loadTransaccionEnviadas = function(){
                BovedaService.getTransaccionBovedaCajaEnviadas($scope.agenciaSession.id).then(
                    function(enviados){
                        $scope.transaccionesEnviadas = enviados;
                    }
                );
            };
            
            $scope.loadTransaccionRecibidas = function(){
                BovedaService.getTransaccionBovedaCajaRecibidas($scope.agenciaSession.id).then(
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
                    {displayName: 'Estado solicitud', width: 100, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                    {displayName: 'Estado confirmación',  width: 125, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                    {field:"caja", displayName:'Origen', width: 80},
                    {field:"boveda", displayName:'Destino'},
                    {field:"monto", displayName:'Monto', cellFilter:'currency :""', width: 80},
                    {displayName: 'Voucher', width: 240, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-success btn-xs" ng-click="confirmarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-ok"></span>Confirmar</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}]
            };

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha", displayName:'Fecha', cellFilter:'date : "dd/MM/yyyy"', width: 80},
                    {field:"hora", displayName:'Hora', cellFilter:'date : "HH:mm:ss"', width: 70},
                    {displayName: 'Estado Solicitud', width: 100, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                    {displayName: 'Estado Confirmación', width: 125, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                    {field:"boveda", displayName:'Origen'},
                    {field:"caja", displayName:'Destino', width: 90},
                    {field:"monto", displayName:'Monto', cellFilter:'currency :""', width: 90},
                    {displayName: 'Voucher', width: 180, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}]
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

            $scope.getVoucher = function(row){
                $state.transitionTo('app.transaccion.voucherTransaccionBovedaCaja', { id: row.id });
            };

            $scope.cancelarTransaccion = function(row){
                if(!angular.isUndefined(row)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/jefeCaja/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SessionService.cancelarTransaccionBovedaCaja(row.id).then(
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
                        SessionService.confirmarTransaccionBovedaCaja(row.id).then(
                            function(data){
                                $scope.loadTransaccionRecibidas();
                                $scope.loadTransaccionEnviadas();
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

        }]);
});