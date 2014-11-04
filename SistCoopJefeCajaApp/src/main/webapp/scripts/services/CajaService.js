define(['./module'], function (services) {
    'use strict';
    services.factory("CajaService",["Restangular",
        function(Restangular){

            var baseUrl = "cajas";

            return {
                getDetalle: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/detalle").getList();
                },
                getCaja: function(idCaja) {
                    return Restangular.one(baseUrl+"/"+idCaja).get();
                },
                getCajas: function(idAgencia) {
                    return Restangular.all(baseUrl).getList({idAgencia: idAgencia});
                },
                getBovedas: function(idCaja) {
                    return Restangular.all(baseUrl+"/"+idCaja+"/bovedas").getList();
                },
                crear: function(caja) {
                    return Restangular.all(baseUrl).post(caja);
                },
                actualizar: function(idCaja, caja) {
                    return Restangular.one(baseUrl+"/"+idCaja).customPUT(caja,'',{},{});
                },
                crearTrabajador: function(idCaja, trabajador){
                    return Restangular.all(baseUrl+"/"+idCaja+"/trabajadores").post(trabajador);
                },
                getTrabajadorse: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/trabajadores").getList();
                },
                eliminarTrabajador: function(idCaja, idTrabajador){
                    return Restangular.all(baseUrl+"/"+idCaja+"/trabajadores/"+idTrabajador).remove();
                },
                abrirCaja: function(idCaja){
                    return Restangular.all(baseUrl+"/"+idCaja+"/abrir").post();
                }
            }
        }])
});
