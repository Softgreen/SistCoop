define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('EditarTransaccionSobreGiroController', ["$scope","$state","$filter","PersonaNaturalService", "AgenciaService", "SessionService", "SobreGiroService",
    function ($scope, $state, $filter, PersonaNaturalService, AgenciaService, SessionService, SobreGiroService) {

      $scope.sobregiro = {};

      $scope.loadSobreGiro = function () {
        SobreGiroService.findById($scope.id).then(function (data) {
          $scope.sobregiro = data;
        });
      };
      $scope.loadSobreGiro();

    }]);
});
