/*jshint unused: vars */
define([
        'angular',
        'jquery',
        'jquery-maskedinput',
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
          });
    }
);
