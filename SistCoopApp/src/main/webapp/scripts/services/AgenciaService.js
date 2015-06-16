define(['./module'], function (services) {
    'use strict';
    services.factory("AgenciaService",["Restangular",
        function(Restangular){

            var _agenciaService = Restangular.all("agencias");
            var baseUrl = "agencias";

            return {
                getAgencias: function(estado){
                  return Restangular.all(baseUrl).getList({estado:estado});
                },
                getCajas: function(idAgencia){
                    return Restangular.all(baseUrl+"/"+idAgencia+"/cajas").getList();
                }
            }
        }])
});
