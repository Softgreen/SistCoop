define(['../../module'], function (controllers) {
  'use strict';
  controllers.controller('CrearTransaccionGiroController', ["$scope", "$state", "PersonaNaturalService", "AgenciaService", "SessionService", "MonedaService",
    function ($scope, $state, PersonaNaturalService, AgenciaService, SessionService, MonedaService) {

      $scope.control = {
        success: false,
        inProcess: false,
        submitted : false
      };

      $scope.view = {
        "numeroDocumentoEmisor": undefined,
        "nombreClienteEmisor": undefined,
        "numeroDocumentoReceptor": undefined,
        "nombreClienteReceptor": undefined,
        idAgenciaOrigen: undefined,
        idAgenciaDestino: undefined,
        idMoneda: undefined,
        monto: '0',
        comision: '10',
        tipoComision: 'FIJO',//PORCENTUAL
        modoPagoComision: 'ANADIR',//REDUCIR
        lugarPagoComision: 'AL_ENVIAR',//AL_COBRAR
        total: '0'
      };

      $scope.transaccion = {};

      $scope.$watch('view.monto', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.$watch('view.comision', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.$watch('view.tipoComision', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.$watch('view.modoPagoComision', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);
      $scope.$watch('view.lugarPagoComision', function (newValue, oldValue) {
        $scope.updateTotal();
      }, true);

      $scope.updateTotal = function () {

        var monto = 0;
        var comision = 0;
        var total = 0;
        var estadoPagoComision = false;

        //hallando monto
        if ($scope.view.modoPagoComision == 'ANADIR') {
          monto = parseFloat($scope.view.monto);
          //redondeo a un decimal y hacia arriba
          monto = Math.round(monto * 10) / 10;
        } else {
          if ($scope.view.tipoComision == 'PORCENTURAL') {
            monto = (100 * parseFloat($scope.view.monto)) / (100 + parseFloat($scope.view.comision));
            //redondeo a un decimal y hacia abajo
            monto = Math.floor(monto * 10) / 10;
          } else {
            monto = parseFloat($scope.view.monto) - parseFloat($scope.view.comision);
          }
        }

        //hallando comision
        if ($scope.view.modoPagoComision == 'ANADIR') {
          if ($scope.view.tipoComision == 'PORCENTURAL') {
            comision = parseFloat($scope.view.monto) * parseFloat($scope.view.comision) / 100;
            //redondeo a un decimal y hacia arriba
            comision = Math.round(comision * 10) / 10;
          } else {
            comision = parseFloat($scope.view.comision);
          }
        } else {
          comision = parseFloat($scope.view.monto).toFixed(2) - parseFloat(monto).toFixed(2);
          comision = comision.toFixed(2);
        }

        //estado de pago comision
        if ($scope.view.lugarPagoComision == 'AL_ENVIAR') {
          estadoPagoComision = true;

          total = parseFloat(monto) + parseFloat(comision);
          total = total.toFixed(2);
        } else {
          estadoPagoComision = false;

          total = parseFloat(monto).toFixed(2);
        }

        $scope.transaccion = {
          "monto": monto,
          "comision": comision,
          "total": total,
          "estadoPagoComision": estadoPagoComision
        };

      };

      $scope.objetosCargados = {
        agenciaOrigen: undefined
      };

      $scope.combo = {
        moneda: undefined,
        agencia: undefined
      };

      $scope.loadAgenciaOrigen = function () {
        $scope.objetosCargados.agenciaOrigen = $scope.agenciaSession;
        $scope.view.idAgenciaOrigen = $scope.agenciaSession.id;
      };
      $scope.loadAgenciasDestino = function () {
        AgenciaService.getAgencias(true).then(function (data) {
          $scope.combo.agencia = data;
        });
      };
      $scope.loadAgenciaOrigen();
      $scope.loadAgenciasDestino();

      $scope.loadMonedas = function () {
        MonedaService.getMonedas().then(function (data) {
          $scope.combo.moneda = data;
        });
      };
      $scope.loadMonedas();

      //
      $scope.getTipoMoneda = function () {
        if (!angular.isUndefined($scope.view.idMoneda) && !angular.isUndefined($scope.combo.moneda)) {
          for (var i = 0; i < $scope.combo.moneda.length; i++)
            if ($scope.view.idMoneda == $scope.combo.moneda[i].id)
              return $scope.combo.moneda[i];
        } else {
          return undefined;
        }
      };

      $scope.buscarClienteEmisor = function ($event) {
        if (!angular.isUndefined($event)) {
          $event.preventDefault();
        }
        PersonaNaturalService.findByTipoNumeroDocumento(1, $scope.view.numeroDocumentoEmisor).then(function (data) {
          $scope.view.nombreClienteEmisor = data.apellidoPaterno + " " + data.apellidoMaterno + " " + data.nombres;
        });
      };
      $scope.buscarClienteReceptor = function ($event) {
        if (!angular.isUndefined($event)) {
          $event.preventDefault();
        }
        PersonaNaturalService.findByTipoNumeroDocumento(1, $scope.view.numeroDocumentoReceptor).then(function (data) {
          $scope.view.nombreClienteReceptor = data.apellidoPaterno + " " + data.apellidoMaterno + " " + data.nombres;
        });
      };

      //transaccion
      $scope.crearTransaccion = function () {

        if ($scope.form.$valid) {

          var transaccion = {
            "idAgenciaOrigen": $scope.view.idAgenciaOrigen,
            "idAgenciaDestino": $scope.view.idAgenciaDestino,

            "numeroDocumentoEmisor": $scope.view.numeroDocumentoEmisor,
            "clienteEmisor": $scope.view.nombreClienteEmisor,
            "numeroDocumentoReceptor": $scope.view.numeroDocumentoReceptor,
            "clienteReceptor": $scope.view.nombreClienteReceptor,

            "idMoneda": $scope.view.idMoneda,
            "lugarPagoComision": $scope.view.lugarPagoComision,
            "monto": $scope.transaccion.monto,
            "comision": $scope.transaccion.comision,
            "estadoPagoComision": $scope.transaccion.estadoPagoComision
          };

          SessionService.crearTransaccionGiro(transaccion).then(
            function (data) {
              $state.transitionTo('app.transaccion.editarGiro', {id: data.id});
            },
            function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error: " + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }
          );
        } else {
          $scope.control.submitted = true;
        }

      };

      $scope.cancelar = function () {
        $state.transitionTo("app.transaccion.buscarGiros");
      };

    }]);
});
