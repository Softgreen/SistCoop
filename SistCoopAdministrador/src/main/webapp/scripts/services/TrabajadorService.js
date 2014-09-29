define(['./module'], function (services) {
    'use strict';
    services.factory("TrabajadorService",["Restangular",
        function(Restangular){

            var baseUrl = "trabajadores";

            return {
                getTrabajador: function(idTrabajador) {
                    return Restangular.one(baseUrl+"/"+idTrabajador).get();
                },
                getTrabajadores: function(idAgencia, filterText) {
                    return Restangular.one(baseUrl).get({idAgencia: idAgencia, filterText: filterText});
                },
                crear: function(trabajador) {
                    return Restangular.all(baseUrl).post(trabajador);
                }
            }
        }])
});
