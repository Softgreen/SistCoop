define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('HistorialCajaController', ['$scope', "$state", '$filter', "CajaService",
        function($scope, $state, $filter, CajaService) {

            $scope.today = function() {
                $scope.desde = new Date();
                $scope.hasta = new Date();
            };
            $scope.today();
            $scope.clear = function () {
                $scope.desde = null;
                $scope.hasta = null;
            };
            $scope.openDesde = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedDesde = true;
            };
            $scope.openHasta = function($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openedHasta = true;
            };

            $scope.dateOptions = {
                formatYear: 'yy',
                startingDay: 1
            };

            $scope.formats = ['dd/MM/yyyy','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[0];

            $scope.buscar = function(){
                CajaService.getHistoriales($scope.cajaSession.id, $scope.desde.getTime(),$scope.hasta.getTime()).then(
                    function(historiales){
                        $scope.listHistoriales = historiales;
                    }
                );
            };

            $scope.getVoucher = function(cajaHistorial){
                $state.transitionTo('app.caja.voucherCerrarCaja', { id: cajaHistorial.id});
            };

            $scope.gridOptions = {
                data: 'listHistoriales',
                multiSelect: false,
                columnDefs: [
                    {field:"fechaApertura", cellFilter:"date : 'dd/MM/yyyy'" , displayName:'Fecha apertura'},
                    {field:"horaApertura", cellFilter:"date : 'hh:mm:ss a'", displayName:'Hora apertura'},
                    {field:"fechaCierre", cellFilter:"date : 'dd/MM/yyyy'", displayName:'Fecha cierre'},
                    {field:"horaCierre", cellFilter:"date : 'hh:mm:ss a'",  displayName:'Hora cierre'},
                    {displayName: 'Edit', cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><button type="button" class="btn btn-info btn-xs" ng-click="getVoucher(row.entity)"><span class="glyphicon glyphicon-share"></span>Voucher</button></div>'}]
            };

        }]);
});