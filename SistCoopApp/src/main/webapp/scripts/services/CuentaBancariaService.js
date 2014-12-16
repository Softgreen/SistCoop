define(['./module'], function (services) {
    'use strict';
    services.factory("CuentaBancariaService",["Restangular",
        function(Restangular){
            var _historialCajaService = Restangular.all("cuentasBancarias");
            var baseUrl = "cuentasBancarias";
            return {
                getCuentasBancariasView: function(tipoCuentaList,tipoPersonaList,estadoCuentaList,offset,limit){
                    if(arguments.length == 0){
                        return Restangular.all(baseUrl).getList();
                    }else if (arguments.length == 1) {
                        return Restangular.all(baseUrl).getList({"tipoCuenta":tipoCuentaList},{});
                    } else if (arguments.length == 2) {
                        return Restangular.all(baseUrl).getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList},{});
                    } else if (arguments.length == 3) {
                        return Restangular.all(baseUrl).getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList},{});
                    } else if (arguments.length == 4) {
                        return Restangular.all(baseUrl).getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset},{});
                    } else if (arguments.length == 5) {
                        return Restangular.all(baseUrl).getList({"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset,"limit":limit},{});
                    }
                },
                findByFilterTextView: function(filterText,tipoCuentaList,tipoPersonaList,estadoCuentaList,offset,limit){
                    if(arguments.length == 0){
                        return Restangular.all(baseUrl).getList();
                    }else if (arguments.length == 1) {
                        return Restangular.all(baseUrl).getList({"filterText":filterText},{});
                    } else if (arguments.length == 2) {
                        return Restangular.all(baseUrl).getList({"filterText":filterText,"tipoCuenta":tipoCuentaList},{});
                    } else if (arguments.length == 3) {
                        return Restangular.all(baseUrl).getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList},{});
                    } else if (arguments.length == 4) {
                        return Restangular.all(baseUrl).getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList},{});
                    } else if (arguments.length == 5) {
                        return Restangular.all(baseUrl).getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset},{});
                    } else if (arguments.length == 6) {
                        return Restangular.all(baseUrl).getList({"filterText":filterText,"tipoCuenta":tipoCuentaList,"tipoPersona":tipoPersonaList,"tipoEstadoCuenta":estadoCuentaList,"offset":offset,"limit":limit},{});
                    }
                },
                count: function(){
                    return Restangular.one(baseUrl+"/count").get();
                },
                getCuentasBancaria: function(id){
                    return Restangular.one(baseUrl+"/"+id).get();
                },
                findCuentaByNumeroCuenta: function(numeroCuenta){
                    return Restangular.one("cuentaBancaria/view/buscar").get({numeroCuenta:numeroCuenta},{});
                },
                crearCuentaAhorro: function(transaccion){
                    return Restangular.all(baseUrl).post(transaccion);
                },
                crearCuentaCorriente: function(transaccion){
                    return Restangular.all(baseUrl).post(transaccion);
                },
                crearCuentaPlazoFijo: function(transaccion){
                    return Restangular.all(baseUrl).post(transaccion);
                },
                getSocio: function(idCuenta){
                    return Restangular.one("cuentaBancaria/"+idCuenta+"/socio").get();
                },
                getTitulares: function(idCuenta){
                    return Restangular.all(baseUrl+"/"+idCuenta+"/titulares").getList();
                },
                getTitular: function(idCuenta, idTitular){
                    return Restangular.one(baseUrl+"/"+idCuenta+"/titulares/"+idTitular).get();
                },
                addTitular: function(idCuenta, titular){
                    return Restangular.all(baseUrl+"/"+idCuenta+"/titulares").post(titular);
                },
                eliminarTitular: function(idCuenta, idTitular){
                    return Restangular.one(baseUrl + "/"+idCuenta+"/titulares/"+idTitular).remove();
                },
                getBeneficiarios: function(idCuenta){
                    return Restangular.all(baseUrl + "/" + idCuenta + "/beneficiarios").getList();
                },
                getBeneficiario: function(idCuenta, idBeneficiario){
                    return Restangular.one(baseUrl + "/" + idCuenta + "/beneficiarios/"+idBeneficiario).get();
                },
                addBeneficiario : function(idCuenta, beneficiario){
                    return Restangular.all(baseUrl + "/" + idCuenta + "/beneficiarios").post(beneficiario);
                },
                actualizarBeneficiario : function(idCuenta, idBeneficiario, beneficiario){
                    return Restangular.one(baseUrl + "/" + idCuenta + "/beneficiarios/" + idBeneficiario).customPUT(beneficiario,'',{},{});
                },
                eliminarBeneficiario: function(idCuenta, idBeneficiario){
                    return Restangular.one(baseUrl + "/"+idCuenta+"/beneficiarios/"+idBeneficiario).remove();
                },
                getVoucherCuentaBancaria: function(id) {
                    return Restangular.one("cuentaBancaria/"+id+"/voucherCuentaBancaria").get();
                },
                getVoucherTransferenciaBancaria: function(idTransferencia){
                    return Restangular.one("cuentaBancaria/"+idTransferencia+"/voucherTransferenciaBancaria").get();
                },
                getEstadoCuenta: function(id, desde, hasta) {
                    return Restangular.all(baseUrl+"/"+id+"/estadoCuenta").getList({"desde":desde,"hasta":hasta},{});
                },
                congelarCuentaBancaria: function(id){
                    return Restangular.one(baseUrl+"/"+id+"/congelar").customPOST({},'',{},{});
                },
                descongelarCuentaBancaria: function(id){
                    return Restangular.one(baseUrl+"/"+id+"/descongelar").customPOST({},'',{},{});
                },
                cancelarCuenta: function(id){
                    return Restangular.all(baseUrl+"/"+id+"/cancelar").post();
                },
                recalcularPlazoFijo: function(idCuenta, data){
                    return Restangular.one(baseUrl+"/"+idCuenta+"/recalcular").customPOST(data,'',{},{});
                },
                renovarPlazoFijo: function(idCuenta, data){
                    return Restangular.one("cuentaBancaria/"+idCuenta+"/renovarPlazoFijo").customPUT(data,'',{},{});
                },

                getChequeraUltima: function(idCuentaBancaria){
                    return Restangular.one(baseUrl+"/"+idCuentaBancaria+"/chequeras/ultimo").get();
                },
                getChequeras: function(idCuentaBancaria){
                    return Restangular.all(baseUrl+"/"+idCuentaBancaria+"/chequeras").getList();
                },
                crearChequera: function(idCuentaBancaria, cantidad){
                    var chequera = {
                        cantidad: cantidad
                    };
                    return Restangular.all(baseUrl+"/"+idCuentaBancaria+"/chequeras").post(chequera);
                }
            }
        }])
});
