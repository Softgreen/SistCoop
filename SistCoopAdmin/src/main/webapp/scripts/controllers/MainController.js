define(['./module'], function (controllers) {
    'use strict';

    controllers.controller('MainController', ['$scope','$state','$window','$timeout','localStorageService','SessionService',
        function($scope,$state,$window,$timeout,localStorageService,SessionService) {

            $scope.agenciaSession = {};
            $scope.usuarioSession = undefined;

            $scope.loadUsuarioOfSession = function(){
                SessionService.getUsuarioOfSession().then(
                    function(data){
                        $scope.usuarioSession = data;
                    },
                    function error(error){
                        $scope.usuarioSession = undefined;
                        if(error.status == 400){
                            $scope.loadUsuarioOfSession();
                        }
                    }
                );
            };

            if(angular.isUndefined($scope.usuarioSession)){
                $scope.loadUsuarioOfSession();
            };

        }]);
});