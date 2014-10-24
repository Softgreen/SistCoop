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
                    {displayName: 'Estado', width: 100, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center; width: 85px;"><span ng-show="row.entity.estado">ACTIVO</span><span ng-hide="row.entity.estado">CANCELADO</span></div>'},
                    {field:"entidad", displayName:'Entidad', width: 200},
                    {field:"boveda", displayName:'Boveda', width: 200},
                    {field:"monto", displayName:'Monto', cellFilter:'currency :""', width: 100},
                    {displayName: 'Edit', width: 120, cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)" ng-disabled="getDisabledStateVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

            $scope.getVoucher = function(row){
                $state.transitionTo('app.transaccion.voucherTransaccionEntidadBoveda', { id: row.id });
            };

        }]);
});