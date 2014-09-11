define(['./module'], function (services) {
    'use strict';
    services.factory("BovedaService",["Restangular",
        function(Restangular){

            var baseUrl = "bovedas";

            return {
                findById: function(id) {
                    return Restangular.one(baseUrl, id).get();
                },
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
                },
                actualizar: function(idBoveda, denominacion) {
                    var data = $.param({
                        id: idBoveda,
                        denominacion: denominacion
                    });
                    return Restangular.one(baseUrl+"/"+idBoveda).customPUT(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );
                },
                abrirBoveda: function(idBoveda) {
                    return Restangular.one(baseUrl+"/"+idBoveda+"/abrir").post();
                },
                congelar: function(idBoveda) {
                    return Restangular.one(baseUrl+"/"+idBoveda+"/congelar").post();
                },
                descongelar: function(idBoveda) {
                    return Restangular.one(baseUrl+"/"+idBoveda+"/congelar").post();
                },
                getDetalle: function(idBoveda) {
                    return Restangular.all(baseUrl+"/"+idBoveda+"/detalle").getList();
                }
            }
        }])
});
