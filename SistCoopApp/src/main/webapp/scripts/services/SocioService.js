define(['./module'], function (services) {
    'use strict';
    services.factory("SocioService",["Restangular",
        function(Restangular){

            var _socioService = Restangular.all("socios");
            var baseUrl = "socios";

            return {
                findById: function(id){
                    return Restangular.one(baseUrl, id).get();
                },
                findByFilterText: function(filterText,estadoCuentaAporte, estadoSocio, offset, limit){
                    if(arguments.length == 0){
                        return Restangular.all(baseUrl).getList({},{});
                    } else if(arguments.length == 1){
                        return Restangular.all(baseUrl).getList({filterText:filterText},{});
                    } else if(arguments.length == 2){
                        return Restangular.all(baseUrl).getList({filterText:filterText,cuentaAporte:estadoCuentaAporte},{});
                    } else if(arguments.length == 3){
                        return Restangular.all(baseUrl).getList({filterText:filterText, cuentaAporte:estadoCuentaAporte,"estadoSocio":estadoSocio},{});
                    } else if(arguments.length == 4){
                        return Restangular.all(baseUrl).getList({filterText:filterText, cuentaAporte:estadoCuentaAporte,estadoSocio:estadoSocio,offset:offset},{});
                    } else if(arguments.length == 5){
                        return Restangular.all(baseUrl).getList({filterText:filterText, cuentaAporte:estadoCuentaAporte,estadoSocio:estadoSocio,offset:offset,limit:limit},{});
                    }
                },
                find: function(tipoPersona, idTipoDocumento, numeroDocumento){
                  return Restangular.one(baseUrl).get({tipoPersona: tipoPersona, idTipoDocumento: idTipoDocumento, numeroDocumento: numeroDocumento});
                },
                getSocios: function(estadoCuentaAporte, estadoSocio, offset, limit){
                    if(arguments.length == 0){
                        return _socioService.getList({},{});
                    } else if(arguments.length == 1){
                        return _socioService.getList({"cuentaAporte":estadoCuentaAporte},{});
                    } else if(arguments.length == 2){
                        return _socioService.getList({"cuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio},{});
                    } else if(arguments.length == 3){
                        return _socioService.getList({"cuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio,"offset":offset},{});
                    } else if(arguments.length == 4){
                        return _socioService.getList({"cuentaAporte":estadoCuentaAporte,"estadoSocio":estadoSocio,"offset":offset,"limit":limit},{});
                    }
                },
                count: function(filterText){
                    if(arguments.length == 0){
                        return Restangular.one(baseUrl + "/count").get();
                    } else if(arguments.length == 1){
                        return Restangular.one(baseUrl + "/count").get({"filterText":filterText},{});
                    }
                },
                crear : function(tipoPersona, idTipoDocumentoSocio, numeroDocumentoSocio, idTipoDocumentoApoderado,numeroDocumentoApoderado){
                    var socio = {
                        "tipoPersona" : tipoPersona,
                        "idTipoDocumentoSocio" : idTipoDocumentoSocio,
                        "numeroDocumentoSocio" : numeroDocumentoSocio,
                        "idTipoDocumentoApoderado" : idTipoDocumentoApoderado,
                        "numeroDocumentoApoderado" : numeroDocumentoApoderado
                    };
                    return Restangular.all(baseUrl).post(socio);
                },
                getSocio: function(id){
                    return Restangular.one(baseUrl+"/"+id).get();
                },
                getCuentaAporte: function(id){
                    return Restangular.one(baseUrl+"/"+id+"/cuentaAporte").get();
                },
                getPersonaNatural: function(id){
                    return Restangular.one(baseUrl+"/"+id+"/personaNatural").get();
                },
                getPersonaJuridica: function(id){
                    return Restangular.one(baseUrl+"/"+id+"/personaJuridica").get();
                },
                getApoderado: function(id){
                    return Restangular.one(baseUrl+"/"+id+"/apoderado").get();
                },
                getCuentasBancarias: function(id){
                    return Restangular.all(baseUrl+"/"+id+"/cuentasBancarias").getList();
                },
                congelarCuentaAporte: function(id){
                    return Restangular.one(baseUrl +"/"+id+"/cuentaAporte/congelar").customPOST({},'',{},{});
                },
                descongelarCuentaAporte: function(id){
                    return Restangular.one(baseUrl +"/"+id+"/cuentaAporte/descongelar").customPOST({},'',{},{});
                },
                getBeneficiarios: function(id){
                    return Restangular.all(baseUrl+"/"+id+"/beneficiarios").getList();
                },
                getBeneficiario: function(idSocio, idBeneficiario){
                    return Restangular.one(baseUrl+"/"+idSocio+"/beneficiarios/"+idBeneficiario).get();
                },
                addBeneficiario : function(idSocio, beneficiario){
                    return Restangular.all(baseUrl+"/"+idSocio+"/beneficiarios").post(beneficiario);
                },
                actualizarBeneficiario : function(idSocio, idBeneficiario, beneficiario){
                    return Restangular.one(baseUrl + "/" + idSocio + "/beneficiarios/" + idBeneficiario).customPUT(beneficiario,'',{},{});
                },
                eliminarBeneficiario: function(idCuenta, idBeneficiario){
                    return Restangular.one(baseUrl+"/"+idCuenta+"/beneficiarios/"+idBeneficiario).remove();
                },
                inactivarSocio: function(id){
                    return Restangular.one(baseUrl +"/"+id).remove();
                },
                cambiarApoderado: function(idSocio, apoderado){
                    return Restangular.all(baseUrl+"/"+idSocio+"/apoderado/cambiar").customPOST(apoderado,'',{},{});
                },
                eliminarApoderado: function(idSocio){
                    return Restangular.all(baseUrl+"/"+idSocio+"/apoderado/eliminar").post();
                },
                getVoucherCuentaAporte: function(id) {
                    return Restangular.one(baseUrl+"/"+id+"/voucherCuentaAporte").get();
                },
                getHistorialAportes: function(idSocio,desde, hasta, offset, limit){
                    if(arguments.length == 1){
                        return Restangular.all(baseUrl+"/"+idSocio+"/historialAportes").getList({},{});
                    } else if(arguments.length == 2){
                        return Restangular.all(baseUrl+"/"+idSocio+"/historialAportes").getList({desde:desde},{});
                    } else if(arguments.length == 3){
                        return Restangular.all(baseUrl+"/"+idSocio+"/historialAportes").getList({desde:desde, hasta:hasta},{});
                    } else if(arguments.length == 4){
                        return Restangular.all(baseUrl+"/"+idSocio+"/historialAportes").getList({desde:desde, hasta:hasta,offset:offset},{});
                    } else if(arguments.length == 5){
                        return Restangular.all(baseUrl+"/"+idSocio+"/historialAportes").getList({desde:desde, hasta:hasta,offset:offset,limit:limit},{});
                    }
                }
            }
        }])
});
