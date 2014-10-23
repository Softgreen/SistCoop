define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('BuscarTransaccionEntidadBovedaController', ['$scope', '$state', '$modal', 'SessionService', 'BovedaService',
        function($scope, $state, $modal, SessionService, BovedaService) {
    		
            $scope.nuevo = function(){
                $state.transitionTo('app.transaccion.nuevaTransaccionEntidadBoveda');
            };

            $scope.loadTransacciones = function(){
                BovedaService.getTransaccionesEntidadBoveda($scope.agenciaSession.id).then(
                    function(enviados){
                        $scope.transacciones = enviados;
                    }
                );
            };
            $scope.loadTransacciones();

            $scope.gridOptions = {
                data: 'transacciones',
                multiSelect: false,
                columnDefs: [
                    {field:"fecha", displayName:'Fecha', cellFilter:'date : "dd/MM/yyyy"', width: 70},
                    {field:"hora", displayName:'Hora', cellFilter:'date : "HH:mm:ss"', width: 60},
                    {displayName: 'Estado solicitud', width: 100, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estadoSolicitud" style="color: blue; font-weight: bold;">SOLICITADO</span><span ng-hide="row.entity.estadoSolicitud" style="color: red; font-weight: bold;">CANCELADO</span></div>'},
                    {displayName: 'Estado confirmación',  width: 125, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 110px;"><span ng-show="row.entity.estadoConfirmacion" style="color: blue; font-weight: bold;">CONFIRMADO</span><span ng-hide="row.entity.estadoConfirmacion" style="color: red; font-weight: bold;">NO CONFIRMADO</span></div>'},
                    {field:"caja", displayName:'Origen', width: 80},
                    {field:"boveda", displayName:'Destino'},
                    {field:"monto", displayName:'Monto', cellFilter:'currency :""', width: 80},
                    {displayName: 'Edit', width: 240, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.transaccion.voucherTransaccionEntidadBoveda', { id: row.id });
            };

        }]);
});