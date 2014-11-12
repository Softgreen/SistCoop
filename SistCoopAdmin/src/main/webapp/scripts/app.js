/*jshint unused: vars */
define([
        'angular',
        './controllers/main',
        './directives/main',
        './filters/main',
        './services/main'
    ]/*deps*/,
    function (angular, MainCtrl)/*invoke*/ {
        'use strict';

        return angular.module('cajaApp', [
            'cajaApp.services',
            'cajaApp.controllers',
            'cajaApp.filters',
            'cajaApp.directives',

            /*angJSDeps*/
            'ui.router',
            'restangular',
            'ngProgress',
            'ui.bootstrap',
            'ngGrid',
            'ngSanitize',
            'ngAnimate',
            'LocalStorageModule',
            'ui.utils',
            'blockUI',
            'flow',
            'focusOn',
            'cfp.hotkeys',
            'angular-ladda',
            'ui.select',
            'fxpicklist'
        ]);
    }
);
