define(['./module'], function (services) {
    'use strict';
    services.factory("BovedaService",["Restangular",
        function(Restangular){

            var baseUrl = "bovedas";

            return {
                getBovedas: function(idAgencia) {
                    return Restangular.all(baseUrl).getList({idAgencia: idAgencia});
                },
                crear: function(idMoneda, denominacion) {
                    var data = $.param({
                        idMoneda: idMoneda,
                        denominacion: denominacion
                    });
                    return Restangular.one(baseUrl).customPOST(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );
                }
            }
        }])
});
