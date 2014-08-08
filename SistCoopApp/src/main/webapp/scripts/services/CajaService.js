define(['./module'], function (services) {
    'use strict';
    services.factory("CajaService",["Restangular",
        function(Restangular){
            var _cajaService = Restangular.all("cajas");
            var baseUrl = "cajas";
            return {
                getVoucherCompraVenta: function(idCompraVenta){
                    return Restangular.one("caja/"+idCompraVenta+"/voucherCompraVenta").get();
                },
                getDetalle: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/detalle").getList();
                },
                getBovedas: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/bovedas").getList();
                },
                getPendientes: function(idCaja, idHistorial){
                    
                }
            }
        }])
});
