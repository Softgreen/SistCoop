define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', ["$scope","$state", "$window", "hotkeys", "SessionService", "HotKeysFunctionsService", "RedirectService",
        function($scope,$state, $window, hotkeys,  SessionService, HotKeysFunctionsService, RedirectService) {

            $scope.$watch('redirect', function(newValue, oldvalue){
                if(newValue != oldvalue)
                    if($scope.redirect == true)
                        RedirectService.limpiar();
            }, true);

            $scope.cajaSession = {};
            $scope.agenciaSession = {};
            $scope.usuarioSession = {};

            $scope.loadCajaOfSession = function(){
                $scope.cajaSession = {
                    denominacion:"undefined",
                    abreviatura:"undefined",
                    abierto: false,
                    estadoMovimiento:false,
                    estado: false
                };
                SessionService.getCajaOfSession().then(function(data){
                    $scope.cajaSession = data;
                });
            };
            $scope.loadUsuarioOfSession = function(){
                SessionService.getUsuarioOfSession().then(function(data){
                    $scope.usuarioSession = data;
                });
            };
            $scope.loadAgenciaOfSession = function(){
                $scope.agenciaSession = {
                    "denominacion":"undefined",
                    "abreviatura":"undefined",
                    "ubigeo": "undefined",
                    "estado":false
                };
                SessionService.getAgenciaOfSession().then(function(data){
                    $scope.agenciaSession = data;
                });
            };

            $scope.loadCajaOfSession();
            $scope.loadUsuarioOfSession();
            $scope.loadAgenciaOfSession();

        }]);
});