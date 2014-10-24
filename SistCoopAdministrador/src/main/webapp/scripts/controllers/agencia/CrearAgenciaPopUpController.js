define(['../module'], function (controllers) {
    'use strict';
    controllers.controller('CrearAgenciaPopUpController', ['$scope','$state','$timeout','$modalInstance','focus',
        function($scope,$state,$timeout,$modalInstance,focus) {

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
                "id" : undefined,
                "codigo" : undefined,
                "denominacion" : undefined,
                "abreviatura": undefined,
                "estado": undefined
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
            	alert("hola " + $scope.formCrearAgencia.$valid);
            	
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