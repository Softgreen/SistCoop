define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('TransaccionMayorCuantiaController', ["$scope", "$state", "$filter", "CajaService","CuentaBancariaService","PersonaNaturalService", "MaestroService", "RedirectService",
        function($scope, $state, $filter, CajaService, CuentaBancariaService, PersonaNaturalService, MaestroService, RedirectService) {

            $scope.view = {
                origenImporte: undefined,
                propositoOperacion: undefined,

                tipoDocumentoSolicitante: undefined,
                numeroDocumentoSolicitante: undefined,
                codigoPaisSolicitante: undefined,
                apellidosNombresSolicitante: undefined,
                direccionSolicitante: undefined,
                codigoDepartamentoSolicitante: undefined,
                codigoProvinciaSolicitante: undefined,
                codigoDistritoSolicitante: undefined,
                telefonoSolicitante: undefined,
                fechaNacimientoSolicitante: undefined,
                ocupacionSolicitante: undefined,

                tipoDocumentoBeneficiario: undefined,
                numeroDocumentoBeneficiario: undefined,
                codigoPaisBeneficiario: undefined,
                apellidosNombresBeneficiario: undefined,
                codigoDepartamentoBeneficiario: undefined,
                codigoProvinciaBeneficiario: undefined,
                codigoDistritoBeneficiario: undefined,
                telefonoBeneficiario: undefined,
                fechaNacimientoBeneficiaro: undefined,
                ocupacionBeneficiario: undefined,

                tipoDocumentoOrdenante: undefined,
                numeroDocumentoOrdenante: undefined,
                codigoPaisOrdenante: undefined,
                apellidosNombresOrdenante: undefined,
                codigoDepartamentoOrdenante: undefined,
                codigoProvinciaOrdenante: undefined,
                codigoDistritoOrdenante: undefined,
                telefonoOrdenante: undefined,
                fechaNacimientoOrdenante: undefined,
                ocupacionOrdenante: undefined,

                fecha: new Date(),
                hora: new Date()
            };

            $scope.combo = {
                pais: undefined,
                tipoDocumentoPN: undefined,
                tipoDocumentoPJ: undefined,

                departamento: undefined,
                provinciaSolicitante: undefined,
                distritoSolicitante: undefined
            };

            $scope.loadVoucher = function(){
                CajaService.getVoucherTransaccionBancaria($scope.id).then(
                    function(data){
                        $scope.transaccion = data;
                    },
                    function error(error){
                        alert("Transaccion Cuenta Bancaria no encontrada");
                    }
                );
            };
            $scope.loadVoucher();

            $scope.loadCombo = function(){
                MaestroService.getPaises().then(function(data){
                    $scope.combo.pais = data;
                });
                PersonaNaturalService.getTipoDocumentos().then(function(data){
                    $scope.combo.tipoDocumentoPN = data;
                });
            };
            $scope.loadCombo();


            $scope.buscarSolicitante = function($event){
                if(!angular.isUndefined($event)){$event.preventDefault();}
                if(!angular.isUndefined($scope.view.tipoDocumentoSolicitante) && !angular.isUndefined($scope.view.numeroDocumentoSolicitante)){
                    var tipoDoc = $scope.view.tipoDocumentoSolicitante.id;
                    var numDoc = $scope.view.numeroDocumentoSolicitante;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(data){
                        $scope.view.codigoPaisSolicitante = data.codigoPais;
                        $scope.view.apellidosNombresSolicitante = data.apellidoPaterno+' '+data.apellidoMaterno+','+data.nombres;
                        $scope.view.direccionSolicitante = data.direccion;
                        $scope.view.telefonoSolicitante = data.telefono;
                        $scope.view.fechaNacimientoSolicitante = data.fechaNacimiento;
                        $scope.view.ocupacionSolicitante = data.ocupacion;

                        $scope.view.codigoDepartamentoSolicitante = data.ubigeo.substr(0,2);
                        $scope.view.codigoProvinciaSolicitante = data.ubigeo.substr(2,2);
                        $scope.view.codigoDistritoSolicitante = data.ubigeo.substr(4,2);
                        $scope.loadProvinciasSolicitante();
                        $scope.loadDistritosSolicitante();
                    });
                }
            };
            $scope.buscarBeneficiario = function($event){
                if(!angular.isUndefined($event)){$event.preventDefault();}
                if(!angular.isUndefined($scope.view.tipoDocumentoBeneficiario) && !angular.isUndefined($scope.view.numeroDocumentoBeneficiario)){
                    var tipoDoc = $scope.view.tipoDocumentoBeneficiario.id;
                    var numDoc = $scope.view.numeroDocumentoBeneficiario;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(data){
                        $scope.view.codigoPaisBeneficiario = data.codigoPais;
                        $scope.view.apellidosNombresBeneficiario = data.apellidoPaterno+' '+data.apellidoMaterno+','+data.nombres;
                        $scope.view.direccionBeneficiario = data.direccion;
                        $scope.view.telefonoBeneficiario = data.telefono;
                        $scope.view.fechaNacimientoBeneficiario = data.fechaNacimiento;
                        $scope.view.ocupacionBeneficiario = data.ocupacion;

                        $scope.view.codigoDepartamentoBeneficiario = data.ubigeo.substr(0,2);
                        $scope.view.codigoProvinciaBeneficiario = data.ubigeo.substr(2,2);
                        $scope.view.codigoDistritoBeneficiario = data.ubigeo.substr(4,2);
                        $scope.loadProvinciasBeneficiario();
                        $scope.loadDistritosBeneficiario();
                    });
                }
            };
            $scope.buscarOrdenante = function($event){
                if(!angular.isUndefined($event)){$event.preventDefault();}
                if(!angular.isUndefined($scope.view.tipoDocumentoOrdenante) && !angular.isUndefined($scope.view.numeroDocumentoOrdenante)){
                    var tipoDoc = $scope.view.tipoDocumentoOrdenante.id;
                    var numDoc = $scope.view.numeroDocumentoOrdenante;
                    PersonaNaturalService.findByTipoNumeroDocumento(tipoDoc, numDoc).then(function(data){
                        $scope.view.codigoPaisOrdenante = data.codigoPais;
                        $scope.view.apellidosNombresOrdenante = data.apellidoPaterno+' '+data.apellidoMaterno+','+data.nombres;
                        $scope.view.direccionOrdenante = data.direccion;
                        $scope.view.telefonoOrdenante = data.telefono;
                        $scope.view.fechaNacimientoOrdenante = data.fechaNacimiento;
                        $scope.view.ocupacionOrdenante = data.ocupacion;

                        $scope.view.codigoDepartamentoOrdenante = data.ubigeo.substr(0,2);
                        $scope.view.codigoProvinciaOrdenante = data.ubigeo.substr(2,2);
                        $scope.view.codigoDistritoOrdenante = data.ubigeo.substr(4,2);
                        $scope.loadProvinciasOrdenante();
                        $scope.loadDistritosOrdenante();
                    });
                }
            };


            //load combos
            $scope.loadDepartamentos = function(){
                MaestroService.getDepartamentos().then(function(data){
                    $scope.combo.departamento = data;
                });
            };
            $scope.loadDepartamentos();

            //solicitante
            $scope.loadProvinciasSolicitante = function(){
                if(!angular.isUndefined($scope.view.codigoDepartamentoSolicitante)){
                    MaestroService.getProvinciasByCodigo($scope.view.codigoDepartamentoSolicitante).then(function(data){
                        $scope.combo.provinciaSolicitante = data;
                    });
                } else {
                    $scope.combo.provinciaSolicitante = [];
                }
            };
            $scope.loadDistritosSolicitante = function(){
                if(!angular.isUndefined($scope.view.codigoProvinciaSolicitante)){
                    MaestroService.getDistritosByCodigo($scope.view.codigoDepartamentoSolicitante, $scope.view.codigoProvinciaSolicitante).then(function(data){
                        $scope.combo.distritoSolicitante = data;
                    });
                } else {
                    $scope.combo.distritoSolicitante = [];
                }
            };
            $scope.changeDepartamentoSolicitante = function(){
                $scope.loadProvinciasSolicitante();
                $scope.view.codigoProvinciaSolicitante = undefined;
                $scope.view.codigoDistritoSolicitante = undefined;
            };
            $scope.changeProvinciaSolicitante = function(){
                $scope.loadDistritosSolicitante();
                $scope.view.codigoDistritoSolicitante = undefined;
            };

            //beneficiario
            $scope.loadProvinciasBeneficiario = function(){
                if(!angular.isUndefined($scope.view.codigoDepartamentoBeneficiario)){
                    MaestroService.getProvinciasByCodigo($scope.view.codigoDepartamentoBeneficiario).then(function(data){
                        $scope.combo.provinciaBeneficiario = data;
                    });
                } else {
                    $scope.combo.provinciaBeneficiario = [];
                }
            };
            $scope.loadDistritosBeneficiario = function(){
                if(!angular.isUndefined($scope.view.codigoProvinciaBeneficiario)){
                    MaestroService.getDistritosByCodigo($scope.view.codigoDepartamentoBeneficiario, $scope.view.codigoProvinciaBeneficiario).then(function(data){
                        $scope.combo.distritoBeneficiario = data;
                    });
                } else {
                    $scope.combo.distritoBeneficiario = [];
                }
            };
            $scope.changeDepartamentoBeneficiario = function(){
                $scope.loadProvinciasBeneficiario();
                $scope.view.codigoProvinciaBeneficiario = undefined;
                $scope.view.codigoDistritoBeneficiario = undefined;
            };
            $scope.changeProvinciaBeneficiario = function(){
                $scope.loadDistritosBeneficiario();
                $scope.view.codigoDistritoBeneficiario = undefined;
            };

            //Ordenante
            $scope.loadProvinciasOrdenante = function(){
                if(!angular.isUndefined($scope.view.codigoDepartamentoOrdenante)){
                    MaestroService.getProvinciasByCodigo($scope.view.codigoDepartamentoOrdenante).then(function(data){
                        $scope.combo.provinciaOrdenante = data;
                    });
                } else {
                    $scope.combo.provinciaOrdenante = [];
                }
            };
            $scope.loadDistritosOrdenante = function(){
                if(!angular.isUndefined($scope.view.codigoProvinciaOrdenante)){
                    MaestroService.getDistritosByCodigo($scope.view.codigoDepartamentoOrdenante, $scope.view.codigoProvinciaOrdenante).then(function(data){
                        $scope.combo.distritoOrdenante = data;
                    });
                } else {
                    $scope.combo.distritoOrdenante = [];
                }
            };
            $scope.changeDepartamentoOrdenante = function(){
                $scope.loadProvinciasOrdenante();
                $scope.view.codigoProvinciaOrdenante = undefined;
                $scope.view.codigoDistritoOrdenante = undefined;
            };
            $scope.changeProvinciaOrdenante = function(){
                $scope.loadDistritosOrdenante();
                $scope.view.codigoDistritoOrdenante = undefined;
            };




            //getter
            $scope.getPais = function(codigoPais){
                if(!angular.isUndefined($scope.combo.pais)){
                    for(var i=0; i<$scope.combo.pais.length; i++){
                        if($scope.combo.pais[i].abreviatura == codigoPais)
                            return $scope.combo.pais[i];
                    }
                }
                return undefined;
            };
            $scope.getDepartamento = function(codigo){
                if(!angular.isUndefined($scope.combo.departamento)){
                    for(var i=0; i<$scope.combo.departamento.length; i++){
                        if($scope.combo.departamento[i].codigo == codigo)
                            return $scope.combo.departamento[i];
                    }
                }
                return undefined;
            };
            $scope.getProvincia = function(codigo, origen){
                var combo;
                if(origen == 'solicitante')
                    combo = $scope.combo.provinciaSolicitante;
                else if(origen == 'beneficiario')
                    combo = $scope.combo.provinciaBeneficiario;
                else if(origen == 'ordenante')
                    combo = $scope.combo.provinciaOrdenante;
                else
                    combo = undefined;
                if(!angular.isUndefined(combo)){
                    for(var i=0; i<combo.length; i++){
                        if(combo[i].codigo == codigo)
                            return combo[i];
                    }
                }
                return undefined;
            };
            $scope.getDistrito = function(codigo, origen){
                var combo;
                if(origen == 'solicitante')
                    combo = $scope.combo.distritoSolicitante;
                else if(origen == 'beneficiario')
                    combo = $scope.combo.distritoBeneficiario;
                else if(origen == 'ordenante')
                    combo = $scope.combo.distritoOrdenante;
                else
                    combo = undefined;
                if(!angular.isUndefined(combo)){
                    for(var i=0; i<combo.length; i++){
                        if(combo[i].codigo == codigo)
                            return combo[i];
                    }
                }
                return undefined;
            };




            $scope.salir = function(){
                $scope.redireccion();
            };

            $scope.redireccion = function(){
                if(RedirectService.haveNext()){
                    var nextState = RedirectService.getNextState();
                    var parametros = RedirectService.getNextParamsState();
                    $state.transitionTo(nextState,parametros);
                } else {
                    $state.transitionTo('app.transaccion.depositoRetiro');
                }
            };

        }]);
});