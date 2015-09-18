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
                },
                abrirCajaOfSession: function(){
                    return Restangular.all(baseUrl+"/caja/abrir").post();
                },
                cerrarCajaOfSession: function(detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all(baseUrl+"/caja/cerrar").post(copy);
                },
                crearPendiente: function(boveda,monto,observacion){
                    var data = $.param({idboveda:boveda,monto:monto,observacion:observacion});
                    return Restangular.one(baseUrl+"/transaccionPendiente").customPOST(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );
                },
                inactivarSocioConRetiro: function(idSocio){
                    return Restangular.all(baseUrl+"/socio/"+idSocio+"/desactivar").post();
                },
                cancelarCuentaBancariaConRetiro: function(id){
                    return Restangular.all(baseUrl+"/cuentasBancarias/"+id+"/cancelar").post();
                },
                crearAporte: function(transaccion){
                    return Restangular.all(baseUrl+"/transaccionCuentaAporte").post(transaccion);
                },
                crearTransaccionBancaria: function(transaccion){
                    return Restangular.all(baseUrl+"/transaccionBancaria").post(transaccion);
                },
                crearTransferenciaBancaria: function(transaccion){
                    return Restangular.all(baseUrl+"/transferenciaBancaria").post(transaccion);
                },
                crearTransaccionCompraVenta: function(transaccion){
                    return Restangular.all(baseUrl+"/transaccionCompraVenta").post(transaccion);
                },

                crearTransaccionBovedaCajaOrigenCaja: function(boveda, detalle){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all(baseUrl+"/transaccionBovedaCaja/CAJA").post(copy ,{"boveda":boveda});
                },
                crearTransaccionBovedaCajaOrigenBoveda: function(boveda, detalle, caja){
                    var copy = Restangular.copy(detalle);
                    return Restangular.all(baseUrl+"/transaccionBovedaCaja/BOVEDA").post(copy ,{"boveda":boveda, "caja":caja});
                },
                crearTransaccionCajaCaja: function(idCaja, idMoneda, monto, observacion){
                    var data = $.param({idCaja:idCaja,idMoneda:idMoneda,monto:monto,observacion:observacion});
                    return Restangular.one(baseUrl+"/transaccionCajaCaja").customPOST(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );

                    /*var data = $.param({idboveda:boveda,monto:monto,observacion:observacion});
                    return Restangular.one(baseUrl+"/transaccionPendiente").customPOST(
                        data,
                        '',{},{
                            "Content-Type":"application/x-www-form-urlencoded"}
                    );*/

                },
                cancelarTransaccionBovedaCaja: function(id){
                    return Restangular.all(baseUrl+"/transaccionBovedaCaja/"+id+"/cancelar").post();
                },
                confirmarTransaccionBovedaCaja: function(id){
                    return Restangular.all(baseUrl+"/transaccionBovedaCaja/"+id+"/confirmar").post();
                },

                crearCuentaPlazoFijo: function(transaccion){
                    return Restangular.all(baseUrl+"/cuentasBancarias").post(transaccion);
                },

                extornarTransaccion: function(idTransaccion){
                    return Restangular.all(baseUrl+"/transaccion/"+idTransaccion+"/extornar").post();
                }
            };
        }]);
});
