define(['./module'], function (services) {
    'use strict';
    services.factory("CajaService",["Restangular",
        function(Restangular){
            var _cajaService = Restangular.all("cajas");
            var baseUrl = "cajas";
            return {
                getDetalle: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/detalle").getList();
                },
                getBovedas: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/bovedas").getList();
                },
                getPendientes: function(idCaja, idHistorial){
                    if(arguments.length == 1)
                        return Restangular.all(baseUrl+"/"+idCaja+"/pendientes").getList();
                    else if(arguments.length == 2)
                        return Restangular.all(baseUrl+"/"+idCaja+"/pendientes").getList({idHistorial:idHistorial});
                },
                getVoucherCuentaAporte: function(idTransaccion){
                    return Restangular.one(baseUrl+"/voucherCuentaAporte/"+idTransaccion).get();
                },
                getVoucherTransaccionBancaria: function(idTransaccion){
                    return Restangular.one(baseUrl+"/voucherTransaccionBancaria/"+idTransaccion).get();
                },
                getVoucherTransferenciaBancaria: function(idTransferencia){
                    return Restangular.one(baseUrl+"/voucherTransferenciaBancaria/"+idTransferencia).get();
                },
                getVoucherCompraVenta: function(idCompraVenta){
                    return Restangular.one(baseUrl+"/voucherCompraVenta/"+idCompraVenta).get();
                }
            }
        }])
});
