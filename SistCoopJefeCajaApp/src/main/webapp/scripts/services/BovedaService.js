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
                cerrarBoveda: function(idBoveda) {
                    return Restangular.one(baseUrl+"/"+idBoveda+"/cerrar").post();
                },
                congelar: function(idBoveda) {
                    return Restangular.one(baseUrl+"/"+idBoveda+"/congelar").post();
                },
                descongelar: function(idBoveda) {
                    return Restangular.one(baseUrl+"/"+idBoveda+"/descongelar").post();
                },
                getDetalle: function(idBoveda) {
                    return Restangular.all(baseUrl+"/"+idBoveda+"/detalle").getList();
                },
                getDetallePenultimo: function(idBoveda) {
                    return Restangular.all(baseUrl+"/"+idBoveda+"/detalle/penultimo").getList();
                },
                getTransaccionBovedaCajaEnviadas: function(idAgencia){
                    return Restangular.all(baseUrl+"/"+idAgencia+"/transaccionBovedaCaja/enviados").getList();
                },
                getTransaccionBovedaCajaRecibidas: function(idAgencia){
                    return Restangular.all(baseUrl+"/"+idAgencia+"/transaccionBovedaCaja/recibidos").getList();
                },
                getVoucherTransaccionBovedaCaja: function(idTransaccionBovedaCaja){
                    return Restangular.one(baseUrl+"/voucherTransaccionBovedaCaja/"+idTransaccionBovedaCaja).get();
                },
				getDetalleTransaccionBovedaCaja: function(idTransaccionBovedaCaja){
                    return Restangular.one(baseUrl+"/detalleTransaccionBovedaCaja/"+idTransaccionBovedaCaja).get();
                },
                crearTransaccioEntidadBoveda: function(origen, idEntidad, idBoveda, detalle, observacion){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all(baseUrl+"/transaccionEntidadBoveda/"+origen+"/"+idEntidad+"/"+idBoveda).customPOST(detalle,'',{observacion:observacion},{});
                    //return Restangular.all(baseUrl+"/transaccionEntidadBoveda/"+origen+"/"+idEntidad+"/"+idBoveda).post(detalle);
                },
                crearTransaccioBovedaBoveda: function(idBovedaOrigen, idBovedaDestino, detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all(baseUrl+"/transaccionBovedaBoveda/crear/"+idBovedaOrigen+"/"+idBovedaDestino).post(detalle);
                },
                getVoucherTransaccionEntidadBoveda: function(idTransaccionEntidadBoveda){
                    return Restangular.one(baseUrl+"/voucherTransaccionEntidadBoveda/"+idTransaccionEntidadBoveda).get();
                },
                getVoucherTransaccionBovedaBoveda: function(idTransaccion){
                    return Restangular.one(baseUrl+"/voucherTransaccionBovedaBoveda/"+idTransaccion).get();
                },
                getVoucherCerrarBoveda: function(idHistorial){
                    return Restangular.one(baseUrl+"/voucherCerrarBoveda/"+idHistorial).get();
                },
                getDetalleTransaccionEntidadBoveda: function(idTransaccionBovedaCaja){
                    return Restangular.one(baseUrl+"/detalleTransaccionEntidadBoveda/"+idTransaccionBovedaCaja).get();
                },
                getDetalleTransaccionBovedaBoveda: function(idTransaccion){
                    return Restangular.one(baseUrl+"/detalleTransaccionBovedaBoveda/"+idTransaccion).get();
                },
                getTransaccionesEntidadBoveda: function(idAgencia){
                    return Restangular.all(baseUrl+"/transaccionEntidadBoveda/"+idAgencia).getList();
                },
                getTransaccionesBovedaBovedaEnviados: function(idAgencia){
                    return Restangular.all(baseUrl+"/transaccionBovedaBoveda/enviados/"+idAgencia).getList();
                },
                getTransaccionesBovedaBovedaRecibidos: function(idAgencia){
                    return Restangular.all(baseUrl+"/transaccionBovedaBoveda/recibidos/"+idAgencia).getList();
                },
                cancelarTransaccionBovedaBoveda: function(id){
                    return Restangular.all(baseUrl+"/transaccionBovedaBoveda/"+id+"/cancelar").post();
                },
                confirmarTransaccionBovedaBoveda: function(id){
                    return Restangular.all(baseUrl+"/transaccionBovedaBoveda/"+id+"/confirmar").post();
                },
            };
        }]);
});
