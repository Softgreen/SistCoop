define(['./module'], function (services) {
    'use strict';
    services.factory("SessionService",["Restangular",
        function(Restangular){

            var _cajaService = Restangular.all("session");
            var baseUrl = "session";

            return {
                getCajaOfSession: function(){
                    return Restangular.one(baseUrl+"/caja").get();
                },
                getUsuarioOfSession: function(){
                    return Restangular.one(baseUrl+"/usuario").get();
                },
                getAgenciaOfSession: function(){
                    return Restangular.one(baseUrl+"/agencia").get();
                }
            };
        }]);
});
