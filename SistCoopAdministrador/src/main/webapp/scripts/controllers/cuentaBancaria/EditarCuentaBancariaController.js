define(['../module'], function (controllers) {
  'use strict';
  controllers.controller('EditarCuentaBancariaController', ["$scope", "$state", "$location", "$filter", "$window", "focus", "$modal",
    "MaestroService", "PersonaNaturalService", "PersonaJuridicaService",
    "SocioService", "CuentaBancariaService", "BeneficiarioService", "TitularService", "ConfiguracionService",
    function ($scope, $state, $location, $filter, $window, focus, $modal,
              MaestroService, PersonaNaturalService, PersonaJuridicaService,
              SocioService, CuentaBancariaService, BeneficiarioService, TitularService, ConfiguracionService) {

      $scope.viewState = "app.socio.editarCuentaBancaria";

      $scope.alerts = [];
      $scope.closeAlert = function (index) {
        $scope.alerts.splice(index, 1);
      };

      $scope.control = {
        "beneficiario": {"success": false, "message": undefined},
        "titular": {"success": false, "message": undefined}
      };

      $scope.login = {
        result: false
      };

      //cargar datos
      $scope.loadCuentaBancaria = function () {
        if (!angular.isUndefined($scope.id)) {
          CuentaBancariaService.getCuentasBancaria($scope.id).then(
            function (data) {
              $scope.cuentaBancaria = data;
              if (!angular.isUndefined($scope.cuentaBancaria)) {
                SocioService.findById($scope.cuentaBancaria.idSocio).then(
                  function (data) {
                    $scope.socio = data;
                  }, function error(error) {
                    $scope.socio = undefined;
                    $scope.alerts.push({type: "danger", msg: "Socio no encontrado."});
                  }
                );
              }
              ;
            }, function error(error) {
              $scope.cuentaBancaria = undefined;
              $scope.alerts.push({type: "danger", msg: "Cuenta bancaria no encontrada."});
            }
          );
        }
      };

      $scope.loadBeneficiarios = function () {
        if (!angular.isUndefined($scope.id)) {
          CuentaBancariaService.getBeneficiarios($scope.id).then(
            function (data) {
              $scope.beneficiarios = data;
            }, function error(error) {
              $scope.beneficiarios = undefined;
              $scope.alerts.push({type: "danger", msg: "Beneficiarios no encontrados."});
            }
          );
        }
      };
      $scope.loadTitulares = function () {
        if (!angular.isUndefined($scope.id)) {
          CuentaBancariaService.getTitulares($scope.id).then(
            function (data) {
              $scope.titulares = data;
            }, function error(error) {
              $scope.titulares = undefined;
              $scope.alerts.push({type: "danger", msg: "Titulares no encontrados."});
            }
          );
        }
      };

      /*cargar chequeras*/
      $scope.loadChequeras = function () {
        if (!angular.isUndefined($scope.id)) {
          CuentaBancariaService.getChequeras($scope.id).then(
            function (data) {
              $scope.chequeras = data;
            }, function error(error) {
              $scope.chequeras = undefined;
              $scope.alerts.push({type: "danger", msg: "Chequeras no encontrados."});
            }
          );
        }
        ;
      };
      $scope.loadChequeras();

      $scope.editChequera = function (chequera) {
        $state.transitionTo("app.socio.editarChequera", {idCuentaBancaria: $scope.id, id: chequera.id});
      };
      $scope.anularChequera = function (chequera) {
        var modalInstance = $modal.open({
          templateUrl: 'views/util/confirmPopUp.html',
          controller: "ConfirmPopUpController"
        });
        modalInstance.result.then(function (result) {
          CuentaBancariaService.anularChequera($scope.id, chequera.id).then(
            function (data) {
              $scope.alerts = [{type: "success", msg: "Chequera anulada."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }, function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }
          );
        }, function () {
        });
      };

      $scope.transacciones = [];
      $scope.loadEstadoCuenta = function () {
        if (!angular.isUndefined($scope.id)) {
          CuentaBancariaService.getEstadoCuenta($scope.id).then(
            function (data) {
              $scope.transacciones = data;
            }, function error(error) {
              $scope.transacciones = [];
              $scope.alerts.push({type: "danger", msg: "Estado de cuenta no encontrado."});
            }
          );
        }
      };

      $scope.gridOptions = {
        data: 'transacciones',
        multiSelect: false,
        enablePaging: true,
        columnDefs: [
          {field: "fecha", cellFilter: "date:'dd/MM/yyyy'", displayName: 'FECHA', width: 70},
          {field: "hora", cellFilter: "date : 'HH:mm:ss'", displayName: 'HORA', width: 60},
          {field: "tipoTransaccion", displayName: 'TIPO TRANS.', width: 80},
          {field: "idTransaccionTransferencia", displayName: 'Nº TRANS.', width: 70},
          {field: "numeroOperacion", displayName: 'Nº OP.', width: 50},
          {field: "referencia", displayName: 'REFERENCIA'},
          {
            displayName: 'MONEDA',
            width: 60,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6" style="text-align: center;"><span>{{cuentaBancaria.moneda}}</span></div>'
          },
          {field: "monto", cellFilter: "currency: ''", displayName: 'MONTO', width: 70},
          {field: "saldoDisponible", cellFilter: "currency: ''", displayName: 'DISPONIBLE', width: 90},
          {
            displayName: 'ESTADO',
            width: 80,
            cellTemplate: '<div ng-class="col.colIndex()" class="ngCellText ng-scope col6 colt6"><span ng-show="row.entity.estado" style="color: blue; font-weight: bold;">ACTIVO</span><span ng-hide="row.entity.estado" style="color: red; font-weight: bold;">EXTORNADO</span></div>'
          }
        ]
      };

      $scope.loadCuentaBancaria();
      $scope.loadBeneficiarios();
      $scope.loadTitulares();
      $scope.loadEstadoCuenta();

      $scope.estadoCuentaSearcher = false;
      $scope.today = function () {
        $scope.desde = new Date();
        $scope.hasta = new Date();
      };
      $scope.today();
      $scope.openDesde = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.openedDesde = true;
      };
      $scope.openHasta = function ($event) {
        $event.preventDefault();
        $event.stopPropagation();
        $scope.openedHasta = true;
      };
      $scope.dateOptions = {formatYear: 'yy', startingDay: 1};
      $scope.formats = ['dd/MM/yyyy', 'dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
      $scope.format = $scope.formats[0];

      $scope.changeEstadoCuentaSearcher = function () {
        $scope.estadoCuentaSearcher = !$scope.estadoCuentaSearcher;
      };
      $scope.buscarEstadoCuenta = function () {
        CuentaBancariaService.getEstadoCuenta($scope.id, $scope.desde.getTime(), $scope.hasta.getTime()).then(
          function (data) {
            $scope.transacciones = data;
          }, function error(error) {
            $scope.transacciones = undefined;
            $scope.alerts.push({type: "danger", msg: "Estado de cuenta no encontrado."});
          }
        );
      };
      $scope.enviarEmail = function (formato) {
        CuentaBancariaService.enviarEstadoCuentaByEmail($scope.id, formato, $scope.desde.getTime(), $scope.hasta.getTime()).then(function () {
          alert("El estado de cuenta ha sido enviado al correo correspondiente...");
        });
      };

      $scope.imprimirEstadoCuentaPdf = function () {
        var restApiUrl = ConfiguracionService.getRestApiUrl();
        $window.open(restApiUrl + '/cuentasBancarias/' + $scope.id + '/estadoCuenta/pdf?desde='+$scope.desde.getTime()+'&hasta='+$scope.hasta.getTime());
      };


      //cuenta bancaria
      $scope.congelarCuentaBancaria = function () {
        if (!angular.isUndefined($scope.cuentaBancaria)) {
          CuentaBancariaService.congelarCuentaBancaria($scope.cuentaBancaria.id).then(
            function (data) {
              $scope.loadCuentaBancaria();
              $scope.alerts = [{type: "success", msg: "Cuenta bancaria congelada."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            },
            function error(error) {
              $scope.alerts = [{type: "warning", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }
          );
        }
      };
      $scope.descongelarCuentaBancaria = function () {
        if (!angular.isUndefined($scope.cuentaBancaria)) {
          CuentaBancariaService.descongelarCuentaBancaria($scope.cuentaBancaria.id).then(
            function (data) {
              $scope.loadCuentaBancaria();
              $scope.alerts = [{type: "success", msg: "Cuenta bancaria descongelada."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            },
            function error(error) {
              $scope.alerts = [{type: "warning", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
            }
          );
        }
      };
      //cuenta a plazo fijo carlos
      $scope.recalcularPlazoFijo = function () {
        $scope.openLoginPopUp('app.socio.recalcularPlazoFijo');
      };

      $scope.renovarPlazoFijo = function () {
        $scope.openLoginPopUp('app.socio.renovarPlazoFijo');
      };

      $scope.openLoginPopUp = function (paginaSiguiente) {
        var modalInstance = $modal.open({
          templateUrl: 'views/cajero/util/loginPopUp.html',
          controller: "LoginPopUpController"
        });
        modalInstance.result.then(function (result) {
          $scope.login.result = result;

          //redireccionar
          var savedParameters = {
            id: $scope.id
          };
          var sendParameters = {
            id: $scope.id
          };
          var nextState = $scope.viewState;
          $state.transitionTo(paginaSiguiente, sendParameters);
        }, function () {
        });
      };

      $scope.cancelarCuentaBancaria = function () {
        if (!angular.isUndefined($scope.cuentaBancaria)) {
          if ($scope.cuentaBancaria.estadoCuenta != 'ACTIVO') {
            $scope.alerts = [{type: "warning", msg: "Error: La cuenta debe de estar activa."}];
            $scope.closeAlert = function (index) {
              $scope.alerts.splice(index, 1);
            };
            return;
          }
          $state.transitionTo("app.socio.cancelarCuentaBancaria", {id: $scope.id});
        }
      };

      //titulares
      $scope.goToFirmas = function () {
        $state.transitionTo("app.socio.firmasCuentaBancaria", {id: $scope.id});
      };

      $scope.editTitular = function (index) {
        var savedParameters = {
          id: $scope.id
        };
        var sendParameters = {
          id: $scope.titulares[index].personaNatural.id
        };
        var nextState = $scope.viewState;
        $state.transitionTo('app.administracion.editarPersonaNatural', sendParameters);
      };

      $scope.addTitular = function () {
        var modalInstance = $modal.open({
          templateUrl: 'views/cajero/cuentaBancaria/titularPopUp.html',
          controller: "TitularPopUpController"
        });
        modalInstance.result.then(function (result) {
          var titular = {"idTipoDocumento": result.tipoDocumento.id, "numeroDocumento": result.numeroDocumento};

          CuentaBancariaService.addTitular($scope.id, titular).then(
            function (data) {
              CuentaBancariaService.getTitular($scope.id, data.id).then(function (titular) {
                $scope.titulares.push(titular);
              });
              $scope.alerts = [{type: "success", msg: "Titular creado."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.titular.success = true;
              $scope.control.titular.message = '<span class="label label-success">Creado</span>';
            }, function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.titular.success = false;
              $scope.control.titular.message = '';
              $window.scrollTo(0, 0);
            }
          );
        }, function () {
        });
      };
      $scope.deleteTitular = function (index) {
        var modalInstance = $modal.open({
          templateUrl: 'views/cajero/util/confirmPopUp.html',
          controller: "ConfirmPopUpController"
        });
        modalInstance.result.then(function (result) {
          CuentaBancariaService.eliminarTitular($scope.id, $scope.titulares[index].id).then(
            function (data) {
              $scope.alerts = [{type: "success", msg: "Titular eliminado."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.titular.success = true;
              $scope.titulares.splice(index, 1);
              $scope.control.titular.message = '<span class="label label-success">Eliminado</span>';
            }, function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.titular.success = false;
              $scope.control.titular.message = '';
              $window.scrollTo(0, 0);
            }
          );
        }, function () {
        });
      };

      //beneficiarios
      $scope.addBeneficiario = function () {
        var modalInstance = $modal.open({
          templateUrl: 'views/cajero/cuentaBancaria/beneficiarioPopUp.html',
          controller: "BeneficiarioPopUpController",
          resolve: {
            total: function () {
              var tot = 0;
              if (!angular.isUndefined($scope.beneficiarios))
                for (var i = 0; i < $scope.beneficiarios.length; i++)
                  tot = tot + $scope.beneficiarios[i].porcentajeBeneficio;
              return tot;
            },
            obj: function () {
              return undefined;
            }
          }
        });
        modalInstance.result.then(function (result) {
          CuentaBancariaService.addBeneficiario($scope.id, result).then(
            function (data) {
              CuentaBancariaService.getBeneficiario($scope.id, data.id).then(function (beneficiario) {
                $scope.beneficiarios.push(beneficiario);
              });
              $scope.alerts = [{type: "success", msg: "Beneficiario Creado."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.beneficiario.success = true;
              $scope.control.beneficiario.message = '<span class="label label-success">Creado</span>';
            }, function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.beneficiario.success = false;
              $scope.control.beneficiario.message = '';
              $window.scrollTo(0, 0);
            }
          );
        }, function () {
        });
      };
      $scope.deleteBeneficiario = function (index) {
        var modalInstance = $modal.open({
          templateUrl: 'views/cajero/util/confirmPopUp.html',
          controller: "ConfirmPopUpController"
        });
        modalInstance.result.then(function (result) {
          CuentaBancariaService.eliminarBeneficiario($scope.id, $scope.beneficiarios[index].id).then(
            function (data) {
              $scope.alerts = [{type: "success", msg: "Beneficiario Eliminado."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.beneficiario.success = true;
              $scope.beneficiarios.splice(index, 1);
              $scope.control.beneficiario.message = '<span class="label label-success">Eliminado</span>';
            }, function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.beneficiario.success = false;
              $scope.control.beneficiario.message = '';
              $window.scrollTo(0, 0);
            }
          );
        }, function () {
        });
      };
      $scope.editBeneficiario = function (index) {
        var modalInstance = $modal.open({
          templateUrl: 'views/cajero/cuentaBancaria/beneficiarioPopUp.html',
          controller: "BeneficiarioPopUpController",
          resolve: {
            total: function () {
              var tot = 0;
              if (!angular.isUndefined($scope.beneficiarios))
                for (var i = 0; i < $scope.beneficiarios.length; i++)
                  tot = tot + $scope.beneficiarios[i].porcentajeBeneficio;
              return tot;
            },
            obj: function () {
              return $scope.beneficiarios[index];
            }
          }
        });
        modalInstance.result.then(function (result) {
          CuentaBancariaService.actualizarBeneficiario($scope.id, $scope.beneficiarios[index].id, result).then(
            function (data) {
              $scope.beneficiarios.splice(index, 1);
              $scope.beneficiarios.push(result);
              $scope.alerts = [{type: "success", msg: "Beneficiario Actualizado."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.beneficiario.success = true;
              $scope.control.beneficiario.message = '<span class="label label-success">Actualizado</span>';
            }, function error(error) {
              $scope.alerts = [{type: "danger", msg: "Error:" + error.data.message + "."}];
              $scope.closeAlert = function (index) {
                $scope.alerts.splice(index, 1);
              };
              $scope.control.beneficiario.success = false;
              $scope.control.beneficiario.message = '';
              $window.scrollTo(0, 0);
            }
          );
        }, function () {
        });
      };


      $scope.goToChequera = function () {
        $state.transitionTo('app.socio.crearChequera', {id: $scope.id});
      };

      $scope.salir = function () {
        $scope.redireccion();
      };

      $scope.redireccion = function () {
        $state.transitionTo('app.socio.buscarCuentaBancaria');
      };

      $scope.imprimirCertificado = function () {
        var restApiUrl = ConfiguracionService.getRestApiUrl();
        $window.open(restApiUrl + '/cuentasBancarias/' + $scope.id + '/certificado');
      };

      $scope.imprimirCartilla = function () {
        var restApiUrl = ConfiguracionService.getRestApiUrl();
        $window.open(restApiUrl + '/cuentasBancarias/' + $scope.id + '/cartilla');
      };

    }]);
});
