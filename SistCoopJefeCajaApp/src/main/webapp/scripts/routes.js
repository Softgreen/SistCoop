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

                .state('app.boveda', {
                    url: "/boveda",
                    views: {
                        "viewMenu":{
                            controller: function($scope){
                                $scope.menus = [
                                    {'name':'Boveda', submenus:[
                                        { 'name':'Nuevo' , 'state':'app.boveda.nuevaBoveda'},
                                        { 'name':'Buscar' , 'state':'app.boveda.buscarBoveda'}
                                    ]}
                                ];
                            }
                        },
                        "viewContent":{
                            template: '<div ui-view style="min-height: 472px;"></br></br></div>'
                        }
                    }
                })

                .state('app.boveda.nuevaBoveda', {
                    url: '/boveda',
                    templateUrl: "views/jefeCaja/boveda/crearBoveda.html"
                })
                .state('app.boveda.editarBoveda', {
                    url: "/boveda/:id",
                    templateUrl: "views/jefeCaja/boveda/editarBoveda.html",
                    controller: function($scope, $stateParams) {
                        $scope.id = $stateParams.id;
                    }
                })
                .state('app.boveda.buscarBoveda', {
                    url: "/abrir",
                    templateUrl: "views/jefeCaja/boveda/buscarBoveda.html"
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
