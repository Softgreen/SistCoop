define(['./module'], function (services) {
    'use strict';
    services.factory("BovedaService",["Restangular",
        function(Restangular){

            var _monedaService = Restangular.all('monedas');
            var baseUrl = "bovedas";

            return {
                getMonedas: function() {
                    return Restangular.all(baseUrl).getList();
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
