define(['./module'], function (services) {
    'use strict';
    services.factory("SucursalService",["Restangular",
        function(Restangular){

            var baseUrl = "sucursales";

            return {
                crear: function(sucursal) {
                    return Restangular.all(baseUrl).post(sucursal);
                },
                getSucursal: function(idSucursal) {
                    return Restangular.one(baseUrl+"/"+idSucursal).get();
                },
                getSucursales: function() {
                    return Restangular.all(baseUrl).getList();
                },
                getAgencias: function(idSucursal) {
                    return Restangular.all(baseUrl+"/"+idSucursal+"/agencias").getList();
                },
                crearAgencia: function(idSucursal, agencia) {
                    return Restangular.all(baseUrl+"/"+idSucursal+"/agencias").post(agencia);
                }
            }
        }])
});
