define(['./module'], function (services) {
    'use strict';
    services.factory("EntidadService",["Restangular",
        function(Restangular){

            var baseUrl = "entidades";

            return {
                findById: function(id) {
                    return Restangular.one(baseUrl, id).get();
                },
                getEntidades: function() {
                    return Restangular.all(baseUrl).getList();
                }
            }
        }])
});
