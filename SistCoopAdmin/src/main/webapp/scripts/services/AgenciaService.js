define(['./module'], function (services) {
    'use strict';
    services.factory("AgenciaService",["Restangular",
        function(Restangular){

            var baseUrl = "agencias";

            return {
                crear: function(agencia) {
                    return Restangular.all(baseUrl).post(agencia);
                },
                getAgencia: function(idAgencia) {
                    return Restangular.one(baseUrl+"/"+idAgencia).get();
                }

            }
        }])
});
