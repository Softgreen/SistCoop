define(['./module'], function (services) {
    'use strict';
    services.factory("CajaService",["Restangular",
        function(Restangular){

            var baseUrl = "cajas";

            return {
                getCajas: function(idAgencia) {
                    return Restangular.all(baseUrl).getList({idAgencia: idAgencia});
                },
                crear: function(caja) {
                    return Restangular.all(baseUrl).post(caja);
                }
            }
        }])
});
