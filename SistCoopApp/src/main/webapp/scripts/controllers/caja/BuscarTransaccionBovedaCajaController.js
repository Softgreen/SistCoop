define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionBovedaCajaController', ['$scope', '$state', '$filter','$modal','CajaService','SessionService',
        function($scope, $state, $filter,$modal, CajaService, SessionService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionBovedaCaja');
            };

            $scope.loadTransaccionEnviadas = function(){
                CajaService.getTransaccionBovedaCajaEnviadas($scope.cajaSession.id).then(
                    function(enviados){
                        $scope.transaccionesEnviadas = enviados;
                    }
                );
            };
            $scope.loadTransaccionRecibidas = function(){
                CajaService.getTransaccionBovedaCajaRecibidas($scope.cajaSession.id).then(
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
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha', width: 80},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora', width: 65},
                    {displayName: 'Estado solicitud', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoSolicitud">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud">NO SOLICITADO</span></div>'},
                    {displayName: 'Estado confirmacion', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoConfirmacion">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion">NO CONFIRMADO</span></div>'},
                    {field:"origen", displayName:'Origen', width: 80},
                    {field:'boveda', displayName:'Destino'},
                    {field:"monto | currency :''", displayName:'Monto', width: 80},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="confirmarTransaccion(row.entity)"><span class="glyphicon glyphicon-remove"></span>Confirmar</button></div>'}]
            };

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha', width: 80},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora', width: 65},
                    {displayName: 'Estado Solicitud', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoSolicitud">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud">NO SOLICITADO</span></div>'},
                    {displayName: 'Estado Confirmacion', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 120px;"><span ng-show="row.entity.estadoConfirmacion">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion">NO CONFIRMADO</span></div>'},
                    {field:"caja", displayName:'Origen', width: 80},
                    {field:'boveda', displayName:'Destino'},
                    {field:"monto | currency :''", displayName:'Monto', width: 80},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button>&nbsp;<button type="button" class="btn btn-danger btn-xs" ng-click="cancelarTransaccion(row.entity)" ng-disabled="getDisabledStateExtornar(row.entity)"><span class="glyphicon glyphicon-remove"></span>Cancelar</button></div>'}]
            };

            $scope.getDisabledStateExtornar = function(row){
                if(row.estadoSolicitud == false)
                    return true;
                if(row.estadoConfirmacion == true)
                    return true;
                return false;
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherTransaccionBovedaCaja', { id: row.id });
            };

            $scope.cancelarTransaccion = function(row){
                if(!angular.isUndefined(row)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SessionService.cancelarTransaccionBovedaCaja(row.id).then(
                            function(data){
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
            $scope.confirmarTransaccion = function(row){
                if(!angular.isUndefined(row)){
                    var modalInstance = $modal.open({
                        templateUrl: 'views/cajero/util/confirmPopUp.html',
                        controller: "ConfirmPopUpController"
                    });
                    modalInstance.result.then(function (result) {
                        SessionService.confirmarTransaccionBovedaCaja(row.id).then(
                            function(data){
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

        }]);
});