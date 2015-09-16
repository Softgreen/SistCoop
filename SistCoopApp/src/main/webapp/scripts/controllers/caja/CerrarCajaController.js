define(['../module'], function (controllers) {
  'use strict';
  controllers.controller('CerrarCajaController', ["$scope", "$state", "$window", "$location", "$filter", "SessionService", "CajaService",
    function ($scope, $state, $window, $location, $filter, SessionService, CajaService) {

      $scope.control = {"success": false, "inProcess": false};

      //console.log("caja en session:"+JSON.stringify($scope.cajaSession));
      //cargar los datos del web service
      CajaService.getDetalle($scope.cajaSession.id).then(function (detalleCaja) {
        for (var i = 0; i < detalleCaja.length; i++) {
          angular.forEach(detalleCaja[i].detalle, function (row) {
            row.subtotal = function () {
              return this.valor * this.cantidad;
            };
          });
        }
        $scope.detalleCajaInicial = angular.copy(detalleCaja);
        $scope.detalleCajaFinal = angular.copy(detalleCaja);

        for (var i = 0; i < $scope.detalleCajaFinal.length; i++) {
          for (var j = 0; j < $scope.detalleCajaFinal[i].detalle.length; j++) {
            $scope.detalleCajaFinal[i].detalle[j].cantidad = 0;
          }
        }
      });


      //update table grid
      $scope.updateLayout = function (index) {
        $scope.updateLayoutInicial[index];
        $scope.updateLayoutFinal[index];
      };

      //configurar las tablas
      $scope.myDataInicial = [];
      $scope.gridOptionsInicial = [];
      $scope.totalInicial = [];
      var gridLayoutPluginInicial = [];
      $scope.updateLayoutInicial = [];
      $scope.getTemplateInicial = function (index, simbolo) {
        gridLayoutPluginInicial[index] = new ngGridLayoutPlugin();
        $scope.updateLayoutInicial[index] = function () {
          gridLayoutPluginInicial[index].updateGridLayout();
        };

        $scope.myDataInicial[index] = $scope.detalleCajaInicial[index].detalle;
        $scope.gridOptionsInicial[index] = {
          data: 'myDataInicial[' + index + ']',
          plugins: [gridLayoutPluginInicial[index]],
          enableRowSelection: false,
          columnDefs: [
            {field: "valor", displayName: "Valor", cellFilter: "currency : ''"},
            {field: "cantidad", displayName: "Cantidad"},
            {field: "subtotal()", cellFilter: "currency : ''", displayName: "Subtotal"}
            //{ field: "subtotal()", cellFilter:"currency : '" + simbolo + "'", displayName: "Subtotal" }
          ]
        };
        $scope.totalInicial[index] = function () {
          var total = 0;
          for (var i = 0; i < $scope.myDataInicial[index].length; i++) {
            total = total + ($scope.myDataInicial[index][i].valor * $scope.myDataInicial[index][i].cantidad);
          }
          return $filter('currency')(total, simbolo);
        };
        return $scope.gridOptionsInicial[index];
      };

      $scope.myDataFinal = [];
      $scope.gridOptionsFinal = [];
      $scope.totalFinal = [];
      var gridLayoutPluginFinal = [];
      $scope.updateLayoutFinal = [];
      $scope.getTemplateFinal = function (index, simbolo) {
        gridLayoutPluginFinal[index] = new ngGridLayoutPlugin();
        $scope.updateLayoutFinal[index] = function () {
          gridLayoutPluginFinal[index].updateGridLayout();
        };
        $scope.myDataFinal[index] = $scope.detalleCajaFinal[index].detalle;
        $scope.gridOptionsFinal[index] = {
          data: 'myDataFinal[' + index + ']',
          plugins: [gridLayoutPluginFinal[index]],
          enableRowSelection: true,
          enableCellEditOnFocus: true,
          columnDefs: [
            {field: "valor", displayName: "Valor", cellFilter: "currency : ''"},
            {
              displayName: 'Cant',
              enableCellEdit: false,

              cellTemplate: '<input type="text" ng-class="\'colt\' + col.index" ng-model="row.entity.cantidad" />',
              width: 80
            },
            {field: "subtotal()", displayName: "Subtotal", cellFilter: "currency : ''"}
            //{ field: "subtotal() | currency : '" + simbolo + "' ", displayName: "Subtotal", enableCellEdit: false }
          ]
        };
        $scope.totalFinal[index] = function () {
          var total = 0;
          for (var i = 0; i < $scope.myDataFinal[index].length; i++) {
            total = total + ($scope.myDataFinal[index][i].valor * $scope.myDataFinal[index][i].cantidad);
          }
          return $filter('currency')(total, simbolo);
        };
        return $scope.gridOptionsFinal[index];
      };

      //cerrar caja
      $scope.cerrarCaja = function () {
        $scope.control.inProcess = true;

        SessionService.cerrarCajaOfSession($scope.detalleCajaFinal).then(
          function (data) {
            $scope.control.inProcess = false;
            $scope.control.success = true;

            $scope.cajaSession.abierto = false;
            $scope.cajaSession.estadoMovimiento = false;
            //redireccion
            $state.transitionTo('app.caja.voucherCerrarCaja', {id: data.id});
          },
          function error(error) {
            $scope.control.inProcess = false;
            $scope.control.success = false;

            if (error.status == 400) {
              $scope.pendiente = [];
              var mensajes = [];
              for (var i = 0; i < error.data.length; i++) {
                $scope.pendiente[i] = {
                  "idboveda": error.data[i].idboveda,
                  "boveda": error.data[i].boveda,
                  "monto": error.data[i].monto
                };
                mensajes[i] = {
                  type: "danger",
                  msg: 'Monto de cierre invalido en ' + error.data[i].boveda + ' necesita:'
                };
              }
              $scope.alerts = mensajes;

              $scope.crearPendiente = function (index) {
                //$scope.closeAlert(index);
                var baseLen = $location.absUrl().length - $location.url().length;
                var url = $location.absUrl().substring(0, baseLen);
                $window.open(url + "/app/caja/pendiente/crear" + "?idBoveda=" + $scope.pendiente[index].idboveda + "&monto=" + $scope.pendiente[index].monto + "&tipoPendiente=" + ($scope.pendiente[index].monto <= 0 ? "SOBRANTE" : "FALTANTE"));
              };

              $window.scrollTo(0, 0);

            } else {
              $scope.alerts = [{type: "danger", msg: "Error: " + error.data + "."}];
            }
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };
          }
        );
      };

      $scope.cancelar = function () {
        $state.go("app.caja", null, {reload: true});
      };

      $scope.alertMessageDisplay = function () {
        if ($scope.cajaSession.denominacion == "undefined")
          return true;
        if ($scope.cajaSession.abierto == false)
          return true;
        else
          return false;
      };

      $scope.buttonDisableState = function () {
        return $scope.alertMessageDisplay() || $scope.control.inProcess;
      };

    }]);
});
