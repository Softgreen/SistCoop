define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionCajaCajaController', ['$scope', "$state", '$filter', "CajaService",
        function($scope, $state, $filter, CajaService) {

            $scope.nuevo = function(){
                $state.transitionTo('app.caja.createTransaccionCajaCaja');
            };

            CajaService.getTransaccionCajaCajaEnviadas($scope.cajaSession.id).then(
                function(enviados){
                    $scope.transaccionesEnviadas = enviados;
                }
            );
            CajaService.getTransaccionCajaCajaRecibidas($scope.cajaSession.id).then(
                function(recibidos){
                    $scope.transaccionesRecibidas = recibidos;
                }
            );

            $scope.gridOptionsEnviados = {
                data: 'transaccionesEnviadas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha', width: "80"},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora', width: "65"},
                    {displayName:'Estado Solicitud', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoSolicitud">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud">NO SOLICITADO</span></div>'},
                    {displayName:'Estado Confirmación', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoConfirmacion">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion">NO CONFIRMADO</span></div>'},
                    {field:"origen", displayName:'Origen'},
                    {field:'moneda.denominacion', displayName:'Moneda'},
                    {field:"monto | currency : ''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.gridOptionsRecibidos = {
                data: 'transaccionesRecibidas',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha | date : 'dd/MM/yyyy'", displayName:'Fecha', width: "80"},
                    {field:"hora | date : 'HH:mm:ss'", displayName:'Hora', width: "65"},
                    {displayName:'Estado Solicitud', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoSolicitud">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud">NO SOLICITADO</span></div>'},
                    {displayName:'Estado Confirmación', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span ng-show="row.entity.estadoConfirmacion">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion">NO CONFIRMADO</span></div>'},
                    {field:"origen", displayName:'Origen'},
                    {field:'moneda.denominacion', displayName:'Moneda'},
                    {field:"monto | currency :''", displayName:'Monto'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.caja.voucherTransaccionCajaCaja', { id: row.id });
            };
        }]);
});