/*jshint unused: vars */
require.config({
    paths: {
        'domReady': '../bower_components/requirejs-domready/domReady',
        jquery: '../bower_components/jquery/dist/jquery',
        angular: '../bower_components/angular/angular',
        'angular-scenario': '../bower_components/angular-scenario/angular-scenario',
        'angular-mocks': '../bower_components/angular-mocks/angular-mocks',
        'angular-sanitize': "../bower_components/angular-sanitize/angular-sanitize",
        'angular-animate': "../bower_components/angular-animate/angular-animate",
        'angular-local-storage': "../bower_components/angular-local-storage/angular-local-storage",
        'uiRouter': "../bower_components/angular-ui-router/release/angular-ui-router",
        'restangular' : "../bower_components/restangular/dist/restangular",
        'underscore' : "../bower_components/underscore/underscore",
        'ui.bootstrap': "../bower_components/angular-bootstrap/ui-bootstrap-tpls",
        'ngGrid': "../bower_components/ng-grid/build/ng-grid",
        'ng-grid-layout': "../bower_components/ng-grid/plugins/ng-grid-layout",
        'blockUI': "../bower_components/angular-block-ui/dist/angular-block-ui",
        'flow': "../bower_components/ng-flow/dist/ng-flow-standalone",
        'ui-utils':"../bower_components/angular-ui-utils/ui-utils",
        'focusOn':"../bower_components/ng-focus-on/ng-focus-on",
        'ngProgress':"../bower_components/ngprogress/build/ngProgress",
        'cfp.hotkeys':"../bower_components/angular-hotkeys/build/hotkeys",
        'angular-ladda':"../bower_components/angular-ladda/dist/angular-ladda.min",
        'picklist':"picklist"
    },
    shim: {
        angular: {
            exports: 'angular'
        },
        jquery: {
            'exports' : 'jquery'
        },
        'angular-sanitize':{
            deps: ['angular']
        },
        'angular-animate':{
            deps: ['angular']
        },
        'angular-local-storage':{
            deps: ['angular']
        },
        'uiRouter' :{
            deps: ['angular']
        },
        'restangular' :{
            deps: ["underscore", "angular"]
        },
        underscore: {
            exports: '_'
        },
        'ui.bootstrap':{
            deps: ['angular']
        },
        'ngGrid':{
            deps: ['angular']
        },
        'ng-grid-layout':{
            deps: ['angular','ngGrid']
        },
        'blockUI':{
            deps: ['angular']
        },
        'flow':{
            deps: ['angular']
        },
        'ui-utils':{
            deps: ['angular']
        },
        'focusOn':{
            deps: ['angular']
        },
        'ngProgress':{
            deps: ['angular']
        },
        'cfp.hotkeys':{
            deps: ['angular']
        },
        'angular-ladda':{
            deps: [
                'angular'
            ]
        },
        'picklist':{
            deps: [
                'angular'
            ]
        },
        'angular-mocks': {
            deps: [
                'angular'
            ],
            exports: 'angular.mock'
        }
    },
    priority: [
        'angular'
    ]
});

//http://code.angularjs.org/1.2.1/docs/guide/bootstrap#overview_deferred-bootstrap
window.name = 'NG_DEFER_BOOTSTRAP!';

require([
    'angular',
    'app',
    'jquery',
    'routes',
    'angular-sanitize',
    'angular-animate',
    'angular-local-storage',
    'restangular',
    'ngProgress',
    'ui.bootstrap',
    'ngGrid',
    'ng-grid-layout',
    'ui-utils',
    'blockUI',
    'flow',
    'focusOn',
    'cfp.hotkeys',
    'angular-ladda',
    'picklist'
], function(angular, app) {
    'use strict';
    /* jshint ignore:start */
    var $html = angular.element(document.getElementsByTagName('html')[0]);
    /* jshint ignore:end */
    angular.element().ready(function() {
        angular.resumeBootstrap([app.name]);
    });
});

