define(['./app'], function(app) {
    'use strict';
    return app.config(['$httpProvider','$stateProvider','$urlRouterProvider','RestangularProvider','localStorageServiceProvider',
        function($httpProvider, $stateProvider, $urlRouterProvider, RestangularProvider, localStorageServiceProvider) {

            //RestangularProvider.setBaseUrl('http://localhost:8080/SistCoopREST/rest');
    		RestangularProvider.setBaseUrl('http://192.168.1.100:8080/SistCoopREST/rest');

            $urlRouterProvider.when('', '/home');
            $urlRouterProvider.otherwise('/home');

            localStorageServiceProvider.setPrefix('softgreen');

            $stateProvider
                .state('home', {
                    url: '/home?redirect',
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
                    url: '/app',
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

                .state('app.sucursal', {
                    url: "/sucursal",
                    views: {
                        "viewMenu":{
                            controller: function($scope){
                                $scope.menus = [
                                    {'name':'Sucursal', submenus:[
                                        { 'name':'Nuevo' , 'state':'app.sucursal.nuevaSucursal'},
                                        { 'name':'Buscar' , 'state':'app.sucursal.buscarSucursal'}
                                    ]}
                                ];
                            }
                        },
                        "viewContent":{
                        	template: '<div ui-view style="min-height: 472px;"><h4 class="text-center" style="font-weight: bold; color: blue;">ADMINISTRAR SUCURSALES</h4><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/sucursales.png"></h3></div>'
                        }
                    }
                })
                .state('app.trabajador', {
                    url: "/trabajador",
                    views: {
                        "viewMenu":{
                            controller: function($scope){
                                $scope.menus = [
                                    {'name':'Trabajador', submenus:[
                                        { 'name':'Nuevo' , 'state':'app.trabajador.nuevoTrabajador'},
                                        { 'name':'Buscar' , 'state':'app.trabajador.buscarTrabajador'}
                                    ]}
                                ];
                            }
                        },
                        "viewContent":{
                        	template: '<div ui-view style="min-height: 472px;"><h4 class="text-center" style="font-weight: bold; color: blue;">ADMINISTRAR TRABAJADORES</h4><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/trabajador.png"></h3></div>'
                        }
                    }
                })
                .state('app.administracion', {
                    url: "/administracion",
                    views: {
                        "viewMenu":{
                            controller: function($scope){
                                $scope.menus = [
                                    {'name':'Persona Natural', submenus:[
                                        { 'name':'Nuevo' , 'state':'app.administracion.crearPersonaNatural'},
                                        { 'name':'Buscar' , 'state':'app.administracion.buscarPersonaNatural'}
                                    ]},
                                    {'name':'Persona Jurídica', submenus:[
                                        { 'name':'Nuevo' , 'state':'app.administracion.crearPersonaJuridica'},
                                        { 'name':'Buscar' , 'state':'app.administracion.buscarPersonaJuridica'}
                                    ]}
                                ];
                            }
                        },
                        "viewContent":{
                        	template: '<div ui-view style="min-height: 472px;"><h4 class="text-center" style="font-weight: bold; color: blue;">ADMINISTRAR PERSONA</h4><h3 class="text-center"><img alt="Caja Ventura" src="images/modules/registrarPersona.png"></h3></div>'
                        }
                    }
                })

                .state('app.sucursal.nuevaSucursal', {
                    url: '/nuevo',
                    templateUrl: "views/administrador/sucursal/crearSucursal.html"
                })
                .state('app.sucursal.editarSucursal', {
                    url: "/editar/:id",
                    templateUrl: "views/administrador/sucursal/editarSucursal.html",
                    controller: function($scope, $stateParams) {
                        $scope.id = $stateParams.id;
                    }
                })
                .state('app.sucursal.buscarSucursal', {
                    url: "/buscar",
                    templateUrl: "views/administrador/sucursal/buscarSucursal.html"
                })


                .state('app.trabajador.nuevoTrabajador', {
                    url: '/nuevo',
                    templateUrl: "views/administrador/trabajador/crearTrabajador.html"
                })
                .state('app.trabajador.editarTrabajador', {
                    url: "/editar/:id",
                    templateUrl: "views/administrador/trabajador/editarTrabajador.html",
                    controller: function($scope, $stateParams) {
                        $scope.id = $stateParams.id;
                    }
                })
                .state('app.trabajador.buscarTrabajador', {
                    url: "/buscar",
                    templateUrl: "views/administrador/trabajador/buscarTrabajador.html"
                })


                .state('app.transaccion.nuevaTransaccionEntidadBoveda', {
                    url: "/buscar",
                    templateUrl: "views/jefeCaja/transaccion/nuevaTransaccionEntidadBoveda.html"
                })
                .state('app.transaccion.nuevaTransaccionBovedaBoveda', {
                    url: "/buscar",
                    templateUrl: "views/jefeCaja/transaccion/nuevaTransaccionBovedaBoveda.html"
                })
                .state('app.transaccion.nuevaTransaccionBovedaCaja', {
                    url: "/buscar",
                    templateUrl: "views/jefeCaja/transaccion/nuevaTransaccionBovedaCaja.html"
                })


                .state('app.administracion.buscarPersonaNatural', {
                    url: '/personaNatural/buscar',
                    templateUrl: 'views/administrador/persona/natural/buscarPersonaNatural.html',
                    controller: 'BuscarPersonaNaturalController'
                })
                .state('app.administracion.crearPersonaNatural', {
                    url: "/personaNatural?tipoDocumento&numeroDocumento",
                    templateUrl: "views/administrador/persona/natural/crearPersonaNatural.html",
                    controller: function($scope, $stateParams) {
                        $scope.params = {};
                        $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                        $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                    }
                })
                .state('app.administracion.editarPersonaNatural', {
                    url: "/personaNatural/:id",
                    templateUrl: "views/administrador/persona/natural/editarPersonaNatural.html",
                    controller: function($scope, $stateParams) {
                        $scope.id = $stateParams.id;
                    }
                })

                .state('app.administracion.buscarPersonaJuridica', {
                    url: "/personaJuridica/buscar",
                    templateUrl: "views/administrador/persona/juridica/buscarPersonaJuridica.html",
                    controller: 'BuscarPersonaJuridicaController'
                })
                .state('app.administracion.crearPersonaJuridica', {
                    url: "/personaJuridica?tipoDocumento&numeroDocumento",
                    templateUrl: "views/administrador/persona/juridica/crearPersonaJuridica.html",
                    controller: function($scope, $stateParams) {
                        $scope.params = {};
                        $scope.params.idTipoDocumento = $stateParams.tipoDocumento;
                        $scope.params.numeroDocumento = $stateParams.numeroDocumento;
                    }
                })
                .state('app.administracion.editarPersonaJuridica', {
                    url: "/personaJuridica/:id",
                    templateUrl: "views/administrador/persona/juridica/editarPersonaJuridica.html",
                    controller: function($scope, $stateParams) {
                        $scope.id = $stateParams.id;
                    }
                });
        }
    ]).config(function ($provide) {

        $provide.decorator('datepickerPopupDirective', function ($delegate) {
          var directive = $delegate[0];
          var link = directive.link;

          directive.compile = function () {
            return function (scope, element, attrs) {
              link.apply(this, arguments);
              element.mask("99/99/9999");
            };
          };

          return $delegate;
        });
      }).run(['$rootScope', '$state', '$stateParams',
            function ($rootScope,   $state,   $stateParams) {
                $rootScope.$state = $state;
                $rootScope.$stateParams = $stateParams;
            }
        ]
    );

});
