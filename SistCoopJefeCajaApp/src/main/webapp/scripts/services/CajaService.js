define(['./module'], function (services) {
    'use strict';
    services.factory("CajaService",["Restangular",
        function(Restangular){

            var baseUrl = "cajas";

            return {
                getCaja: function(idCaja) {
                    return Restangular.one(baseUrl+"/"+idCaja).get();
                },
                getCajas: function(idAgencia) {
                    return Restangular.all(baseUrl).getList({idAgencia: idAgencia});
                },
                getBovedas: function(idCaja) {
                    return Restangular.all(baseUrl+"/"+idCaja+"/bovedas").getList();
                },
                crear: function(caja) {
                    return Restangular.all(baseUrl).post(caja);
                },
                actualizar: function(idCaja, caja) {
                    return Restangular.one(baseUrl+"/"+idCaja).customPUT(caja,'',{},{});
                }
            }
        }])
});
