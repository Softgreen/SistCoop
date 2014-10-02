define(['./module'], function (services) {
    'use strict';
    services.factory("AgenciaService",["Restangular",
        function(Restangular){

            var _agenciaService = Restangular.all("agencias");
            var baseUrl = "agencias";

            return {
                getCajas: function(idAgencia){
                    return Restangular.all(baseUrl+"/"+idAgencia+"/cajas").getList();
                }
            };
        }]);
});
