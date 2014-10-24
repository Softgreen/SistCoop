define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('EditarAgenciaPopUpController', ['$scope','$state','$timeout','$modalInstance','agencia','focus',
        function($scope,$state,$timeout,$modalInstance,agencia,focus) {

            $scope.focusElements = {
                codigo: 'focusCodigo'
            };
            $scope.setInitialFocus = function($event){
                if(!angular.isUndefined($event))
                    $event.preventDefault();
                $timeout(function() {
                    focus($scope.focusElements.codigo);
                }, 100);
            };
            $scope.setInitialFocus();

            $scope.control = {"submitted" : false};

            $scope.agencia = {
                "id" : agencia.id,
                "codigo" : agencia.codigo,
                "denominacion" : agencia.denominacion,
                "abreviatura": agencia.abreviatura,
                "estado": agencia.estado
            };

            $scope.formCrearAgencia = {};
            
            $scope.addAgencia = function() {
                if($scope.formCrearAgencia.$valid){
                    $scope.ok();
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.ok = function () {
                if($scope.formCrearAgencia.$valid){
                    $modalInstance.close($scope.agencia);
                } else {
                    $scope.control.submitted = true;
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };


        }]);
});