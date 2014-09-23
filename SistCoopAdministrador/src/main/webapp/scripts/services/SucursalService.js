define(['./module'], function (services) {
    'use strict';
    services.factory("SucursalService",["Restangular",
        function(Restangular){

            var baseUrl = "sucursales";

            return {
                crear: function(sucursal) {
                    return Restangular.all(baseUrl).post(sucursal);
                }
            }
        }])
});
