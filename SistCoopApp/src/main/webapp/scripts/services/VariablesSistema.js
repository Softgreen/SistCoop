define(['./module'], function (services) {
    'use strict';
    services.factory("VariablesSistema",["Restangular",
        function(Restangular){
            var baseUrl = "variable";

            return {
                findById: function(id){
                    return Restangular.one(baseUrl, id).get();
                },
                findMayorCuantiaByMoneda: function(idMoneda){
                    return Restangular.one(baseUrl+'/mayorCuantia'+'/'+idMoneda).get();
                }
            }

        }])
});
