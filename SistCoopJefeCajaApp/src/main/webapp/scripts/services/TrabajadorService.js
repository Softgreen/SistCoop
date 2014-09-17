define(['./module'], function (services) {
    'use strict';
    services.factory("TrabajadorService",["Restangular",
        function(Restangular){

            var baseUrl = "trabajadores";

            return {
                getTrabajador: function(idTrabajador) {
                    return Restangular.one(baseUrl+"/"+idTrabajador).get();
                }
            }
        }])
});
