define(['./app'], function(app) {
    'use strict';
    return app.config(['$httpProvider','$stateProvider','$urlRouterProvider','RestangularProvider','localStorageServiceProvider',
        function($httpProvider, $stateProvider, $urlRouterProvider, RestangularProvider, localStorageServiceProvider) {

            RestangularProvider.setBaseUrl('http://localhost:8080/SistCoopREST/rest');

            $urlRouterProvider.when('', '/app/home');
            $urlRouterProvider.otherwise('/app/home');

            localStorageServiceProvider.setPrefix('softgreen');

            $stateProvider
                .state('home', {
                    url: '/app?redirect',
                    template: '' +
                        '</br><div class="center-block"><h2 class="text-center" style="font-weight: bold; color: seagreen;">Bienvenido al Sistema Financiero</h2></div></br></br>' +
                        '<h3 class="text-center"><img alt="Caja Ventura" src="images/logos_coop/logo_coop.png"></h3></br></br></br>'
                    ,
                    controller: function($scope, $stateParams) {
                        $scope.redirect = $stateParams.redirect;
                    }
                })
                .state('app', {
                    abstract: true,
                    url: '/app?redirect',
                    template: '' +
                        '<div class="row">'+

                        '<div class="col-sm-6 col-md-3">' +
                        '<div class="thumbnail" style="min-height: 500px;">' +
                        '<div class="caption sf-nav-bar-left-content ng-scope" ui-view="viewMenu" style="width: 88%;">'+
                        '<ul class="ng-scope">' +
                        '<li ng-repeat="menu in menus" class="sf-nav-bar-left-menuitem ng-scope sf-nav-bar-left-menuitem-selected">'+
                        '<a class="gwt-Anchor sf-nav-bar-left-menuitem-header ng-binding" style="background-color: #F7F7F7;width: 100%;">{{menu.name}}</a>'+
                        '<div class="sf-nav-bar-left-submenu">' +
                        '<ul class="sf-ul-submenu-position sf-ul-submenu-theme">' +
                        '<li ng-repeat="submenu in menu.submenus" ng-class="{GGLKX0UBOUD: $state.includes(&#39;{{submenu.state}}&#39;)}" class="sf-li-submenu ng-scope">'+
                        '<a ui-sref="{{submenu.state}}({redirect:true})" class="gwt-Anchor sf-link-submenu ng-binding">{{submenu.name}}</a>'+
                        '</li>' +
                        '</ul>' +
                        '</div>'+
                        '</li>' +
                        '</ul>'+
                        '</div>'+
                        '</div>' +
                        '</div>'+

                        '<div class="col-sm-6 col-md-9">'+
                        '<div class="thumbnail">'+

                        '<div ui-view="viewContent" class="caption">'+
                        '</div>'+

                        '</div>'+
                        '</div>'+

                        '</div>'+
                        '</div>'
                    ,
                    controller: function($scope, $stateParams) {
                        $scope.redirect = $stateParams.redirect;
                    }
                })
                .state('app.configuracion', {
                    abstract: true,
                    url: '/config',
                    template: '' +
                        '<div class="container" style="padding-top: 70px; min-height: 400px;">' +
                            '<div class="row">' +
                                '<div class="col-md-6 col-md-offset-3" ui-view></div>'+
                            '</div>'+
                        '</div>'
                })
                .state('app.configuracion.impresora', {
                    url: "/impresora",
                    templateUrl: "views/cajero/configuracion/impresora.html",
                    controller: 'ConfiguracionImpresoraController'
                })
                .state('app.boveda', {
                    url: "/boveda",
                    views: {
                        "viewMenu":{
                            controller: function($scope){
                                $scope.menus = [
                                    {'name':'Boveda', submenus:[
                                        { 'name':'Nuevo' , 'state':'app.boveda.nuevo'},
                                        { 'name':'Buscar' , 'state':'app.boveda.buscar'}
                                    ]}
                                ];
                            }
                        },
                        "viewContent":{
                            template: '<div ui-view style="min-height: 472px;"></br></br></div>'
                        }
                    }
                })

                .state('app.boveda.nuevo', {
                    url: "/nuevo",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/panelCaja.html"
                        }
                    }
                })
                .state('app.boveda.buscar', {
                    url: "/abrir",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/abrirCaja.html"
                        }
                    }
                })
                .state('app.caja.cerrarCaja', {
                    url: "/cerrar",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/cerrarCaja.html"
                        }
                    }
                })
                .state('app.caja.pendiente', {
                    url: "/pendiente/buscar",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/buscarPendiente.html"
                        }
                    }
                })
                .state('app.caja.pendienteCrear', {
                    url: "/pendiente/crear?idboveda&monto",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/crearPendiente.html",
                            controller: function($scope, $stateParams) {
                                $scope.idboveda = $stateParams.idboveda;
                                $scope.monto = $stateParams.monto;
                            }
                        }
                    }
                })
                .state('app.caja.pendienteVoucher', {
                    url: "/pendiente/:id/voucher",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/voucher/pendienteVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.caja.historial', {
                    url: "/historial",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/historialCaja.html"
                        }
                    }
                })
                .state('app.caja.voucherCerrarCaja', {
                    url: "/historial/:id/voucherCerrarCaja",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/voucher/cerrarCajaVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.caja.buscarTransaccionBovedaCaja', {
                    url: "/buscarBovedaCaja",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/buscarTransaccionBovedaCaja.html"
                        }
                    }
                })
                .state('app.caja.createTransaccionBovedaCaja', {
                    url: "/crearBovedaCaja",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/crearTransaccionBovedaCaja.html"
                        }
                    }
                })
                .state('app.caja.voucherTransaccionBovedaCaja', {
                    url: "/voucherTransaccionBovedaCaja/:id",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/voucher/transaccionBovedaCajaVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.caja.buscarTransaccionCajaCaja', {
                    url: "/transCajaCaja",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/buscarTransaccionCajaCaja.html"
                        }
                    }
                })
                .state('app.caja.createTransaccionCajaCaja', {
                    url: "/crearCajaCaja",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/caja/crearTransaccionCajaCaja.html"
                        }
                    }
                })
                .state('app.caja.voucherTransaccionCajaCaja', {
                    url: "/voucherTransaccionCajaCaja/:id",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/voucher/transaccionCajaCajaVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })



                .state('app.transaccion.aporte', {
                    url: "/aporte",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/transaccion/aporte.html"
                        }
                    }
                })
                .state('app.transaccion.aporteVoucher', {
                    url: "/aporte/:id/voucher",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/voucher/aporteVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.transaccion.depositoRetiro', {
                    url: "/depositoRetiro?numeroCuenta&tipoTransaccion&monto&referencia",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/transaccion/depositoRetiro.html",
                            controller: function($scope, $stateParams) {
                                $scope.params = {};
                                $scope.params.numeroCuenta = $stateParams.numeroCuenta;
                                $scope.params.tipoTransaccion = $stateParams.tipoTransaccion;
                                $scope.params.monto = $stateParams.monto;
                                $scope.params.referencia = $stateParams.referencia;
                            }
                        }
                    }
                })
                .state('app.transaccion.depositoRetiroVoucher', {
                    url: "/depositoRetiro/:id/voucher",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/voucher/transaccionBancariaVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.transaccion.transferencia', {
                    url: "/transferencia",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/transaccion/transferencia.html"
                        }
                    }
                })
                .state('app.transaccion.transferenciaVoucher', {
                    url: "/transferencia/:id/voucher",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/voucher/transferenciaVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.transaccion.compraVenta', {
                    url: "/compraVenta",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/transaccion/compraVenta.html"
                        }
                    }
                })
                .state('app.transaccion.compraVentaVoucher', {
                    url: "/compraVenta/:id/voucher",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/voucher/compraVentaVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.transaccion.buscarTransaccion', {
                    url: "/buscarTransaccion",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/transaccion/buscarTransaccion.html"
                        }
                    }
                })



                .state('app.socio.crearSocio', {
                    url: "/crearSocio",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/socio/crearSocio.html",
                            controller:"CrearSocioController"
                        }
                    }
                })
                .state('app.socio.buscarSocio', {
                    url: "/buscarSocio",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/socio/buscarSocio.html"
                        }
                    }
                })
                .state('app.socio.panelSocio', {
                    url: "/:id/panelSocio",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/socio/panelSocio.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.contratoInactivadoSocio', {
                    url: "/:id/contradoInactivadoSocio",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/socio/contratoInactivadoSocio.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.voucherCancelacionCuenta', {
                    url: "/:id/contradoInactivadoSocio/:idTransaccion",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/voucher/cancelacionCuentaAporteVoucher.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                                $scope.idTransaccion = $stateParams.idTransaccion;
                            }
                        }
                    }
                })

                .state('app.socio.crearCuentaBancaria', {
                    url: "/cuentaBancaria",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/crearCuentaBancaria.html"
                        }
                    }
                })
                .state('app.socio.editarCuentaBancaria', {
                    url: "/cuentaBancaria/:id",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/editarCuentaBancaria.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.firmasCuentaBancaria', {
                    url: "/cuentaBancaria/:id/firma",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/firmasCuentaBancaria.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.cancelarCuentaBancaria', {
                    url: "/cuentaBancaria/cancelarCuenta/:id",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/cancelarCuentaBancaria.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.recalcularPlazoFijo', {
                    url: "/cuentaBancaria/recalcularPlazoFijo/:id",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/recalcularPlazoFijo.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.renovarPlazoFijo', {
                    url: "/cuentaBancaria/renovarPlazoFijo/:id",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/renovarPlazoFijo.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.socio.buscarCuentaBancaria', {
                    url: "/buscarCuentaBancaria",
                    views: {
                        "viewContent": {
                            templateUrl: "views/cajero/cuentaBancaria/buscarCuentaBancaria.html"
                        }
                    }
                })

                .state('app.administracion.buscarPersonaNatural', {
                    url: '/personaNatural/buscar',
                    views: {
                        "viewContent":{
                            templateUrl: 'views/cajero/persona/natural/buscarPersonaNatural.html',
                            controller: 'BuscarPersonaNaturalController'
                        }
                    }
                })
                .state('app.administracion.crearPersonaNatural', {
                    url: "/personaNatural?tipoDocumento&numeroDocumento",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/persona/natural/crearPersonaNatural.html",
                            controller: function($scope, $stateParams) {
                                $scope.params = {};
                                $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                                $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                            }
                        }
                    }
                })
                .state('app.administracion.editarPersonaNatural', {
                    url: "/personaNatural/:id",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/persona/natural/editarPersonaNatural.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
                .state('app.administracion.buscarPersonaJuridica', {
                    url: "/personaJuridica/buscar",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/persona/juridica/buscarPersonaJuridica.html",
                            controller: 'BuscarPersonaJuridicaController'
                        }
                    }
                })
                .state('app.administracion.crearPersonaJuridica', {
                    url: "/personaJuridica?tipoDocumento&numeroDocumento",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/persona/juridica/crearPersonaJuridica.html",
                            controller: function($scope, $stateParams) {
                                $scope.params = {};
                                $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                                $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                            }
                        }
                    }
                })
                .state('app.administracion.editarPersonaJuridica', {
                    url: "/personaJuridica/:id",
                    views: {
                        "viewContent":{
                            templateUrl: "views/cajero/persona/juridica/editarPersonaJuridica.html",
                            controller: function($scope, $stateParams) {
                                $scope.id = $stateParams.id;
                            }
                        }
                    }
                })
        }
    ]).run(['$rootScope', '$state', '$stateParams',
            function ($rootScope,   $state,   $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
            }
        ]
    );

});
