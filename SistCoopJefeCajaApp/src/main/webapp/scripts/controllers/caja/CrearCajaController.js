define([ '../module' ], function(controllers) {
	'use strict';
	controllers.controller('CrearCajaController', [
			'$scope',
			'$state',
			'focus',
			'BovedaService',
			'CajaService',
			function($scope, $state, focus, BovedaService, CajaService) {

				$scope.setInitialFocus = function($event) {
					if (!angular.isUndefined($event))
						$event.preventDefault();
					focus('focusDenominacion');
				};
				$scope.setInitialFocus();

				$scope.control = {
					success : false,
					inProcess : false,
					submitted : false
				};

				$scope.picklist = {
					boveda : undefined
				};

				$scope.view = {
					denominacion : undefined,
					abreviatura : undefined,
					bovedas : []
				};

				$scope.loadBovedas = function() {
					BovedaService.getBovedas($scope.agenciaSession.id).then(
							function(data) {
								$scope.picklist.boveda = data;
							});
				};

				// logic
				$scope.crearTransaccion = function() {
					if ($scope.crearCajaForm.$valid) {
						if ($scope.view.bovedas.length == 0) {
							$scope.alerts = [ {
								type : "danger",
								msg : "Error: Seleccione al menos una boveda."
							} ];
							$scope.closeAlert = function(index) {
								$scope.alerts.splice(index, 1);
							};
							return;
						}

						$scope.control.inProcess = true;

						var caja = {
							denominacion : $scope.view.denominacion,
							abreviatura : $scope.view.abreviatura,
							bovedas : $scope.view.bovedas
						};

						CajaService.crear(caja).then(function(data) {
							$scope.redireccion();
							$scope.control.inProcess = false;
						}, function error(error) {
							$scope.control.inProcess = false;
							$scope.control.success = false;
							$scope.alerts = [ {
								type : "danger",
								msg : "Error: " + error.data.message + "."
							} ];
							$scope.closeAlert = function(index) {
								$scope.alerts.splice(index, 1);
							};
						});
					} else {
						$scope.control.submitted = true;
					}
				};

				$scope.redireccion = function() {
					$state.transitionTo('app.caja.buscarCaja');
				};

				$scope.cancelar = function() {
					$scope.redireccion();
				};

				$scope.loadBovedas();

			} ]);
});