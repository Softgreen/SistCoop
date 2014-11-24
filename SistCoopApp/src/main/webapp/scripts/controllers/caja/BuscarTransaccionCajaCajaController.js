define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionCajaCajaController', ['$scope', "$state", '$filter', '$modal', "CajaService", 'SessionService',
        function($scope, $state, $filter, $modal, CajaService, SessionService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionCajaCaja');
            };

            $scope.loadTransaccionesEnviadas = function(){
            	CajaService.getTransaccionCajaCajaEnviadas($scope.cajaSession.id).then(
                        function(enviados){
                            $scope.transaccionesEnviadas = enviados;
                        }
                    );
            };
            
            $scope.loadTransaccionesRecibidas = function(){
            	CajaService.getTransaccionCajaCajaRecibidas($scope.cajaSession.id).then(
            			function(recibidos){
            				$scope.transaccionesRecibidas = recibidos;
            			}
            	);
            };
            
            $scope.loadTransaccionesEnviadas();
            $scope.loadTransaccionesRecibidas();

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha", cellFilter: "date : 'dd/MM/yyyy'", displayName:'Fecha', width: 70},
                    {field:"hora", cellFilter: "date : 'HH:mm:ss'", displayName:'Hora', width: 60},
                    {displayName:'Estado Solicitud', width: 100, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                    {displayName:'Estado Confirmación', width: 125, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                    {field:"historialCajaOrigen.caja.denominacion", displayName:'Origen', width: 75},
                    {field:"historialCajaDestino.caja.denominacion", displayName:'Destino', width: 75},
                    {field:'moneda.denominacion', displayName:'Moneda'},
                    {field:"monto", cellFilter:"currency :''", displayName:'Monto', width: 70},
                    {displayName: 'Edit', width: 240, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button>&nbsp;<button type="button" class="btn btn-success btn-xs" ng-click="confirmarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-ok"></span>Confirmar</button></div>'}]
            };
            
            $scope.gridOptionsEnviados = {
                    data: 'transaccionesEnviadas',
                    multiSelect: false,
                    columnDefs: [
                        {field:"fecha", cellFilter:"date : 'dd/MM/yyyy'", displayName:'Fecha', width:70},
                        {field:"hora", cellFilter:"date : 'HH:mm:ss'", displayName:'Hora', width:60},
                        {displayName:'Estado Solicitud', width: 100, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                        {displayName:'Estado Confirmación', width: 125, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                        {field:"historialCajaOrigen.caja.denominacion", displayName:'Origen', width:85},
                        {field:"historialCajaDestino.caja.denominacion", displayName:'Destino', width:85},
                        {field:'moneda.denominacion', displayName:'Moneda'},
                        {field:"monto", cellFilter: "currency : ''", displayName:'Monto', width:70},
                        {displayName: 'Edit', width: 165, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateButton(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}]
                };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherTransaccionCajaCaja', { id: row.id });
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
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SessionService.cancelarTransaccionCajaCaja(row.id).then(
                            function(data){
                            	$scope.loadTransaccionesEnviadas();
                            	$scope.loadTransaccionesRecibidas();
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
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SessionService.confirmarTransaccionCajaCaja(row.id).then(
                            function(data){
                                $scope.loadTransaccionesEnviadas();
                            	$scope.loadTransaccionesRecibidas();
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