define([
    'angular',
    './MainController',
    './NavBarController',

    './caja/AbrirCajaController',
    './caja/CerrarCajaController',
    './caja/VoucherCerrarCajaController',
    './caja/BuscarPendienteController',
    './caja/CrearPendienteController',
    './caja/VoucherPendienteController',
    './caja/BuscarTransaccionBovedaCajaController',
    './caja/CrearTransaccionBovedaCajaController',
    './caja/VoucherTransaccionBovedaCajaController',
    './caja/BuscarTransaccionCajaCajaController',
    './caja/CrearTransaccionCajaCajaController',
    './caja/TransaccionMayorCuantiaController',
    './caja/VoucherTransaccionCajaCajaController',
    './caja/VoucherTransaccionBancariaController',
    './caja/VoucherTransaccionAporteController',
    './caja/VoucherTransferenciaBancariaController',
    './caja/VoucherCompraVentaController',
    './caja/VoucherTransaccionChequeController',
    './caja/HistorialCajaController',

    './caja/transaccion/CrearTransaccionDepositoRetiroController',
    './caja/transaccion/CrearAporteController',
    './caja/transaccion/CrearTransferenciaController',
    './caja/transaccion/CompraVentaController',
    './caja/transaccion/BuscarTransaccionHistorialController',
    './caja/transaccion/CrearTransaccionChequeController',
    './caja/transaccion/CrearTransaccionGiroController',
    './caja/transaccion/EditarTransaccionGiroController',
    './caja/transaccion/BuscarGirosController',
    './caja/transaccion/CrearTransaccionSobreGiroController',
    './caja/transaccion/BuscarSobreGirosController',
    './caja/transaccion/EditarTransaccionSobreGiroController',

    './cuentaBancaria/BeneficiarioController',
    './cuentaBancaria/TitularController',
    './cuentaBancaria/BuscarCuentaBancariaController',
    './cuentaBancaria/CrearCuentaBancariaController',
    './cuentaBancaria/EditarCuentaBancariaController',
    './cuentaBancaria/CrearCuentaAhorroController',
    './cuentaBancaria/CrearCuentaCorrienteController',
    './cuentaBancaria/CrearCuentaPlazoFijoController',
    './cuentaBancaria/BuscarCuentaBancariaPopUpController',
    './cuentaBancaria/BeneficiarioPopUpController',
    './cuentaBancaria/TitularPopUpController',
    './cuentaBancaria/FirmasCuentaBancariaController',
    './cuentaBancaria/CancelarCuentaBancariaController',
    './cuentaBancaria/RecalcularPlazoFijoController',
    './cuentaBancaria/RenovarPlazoFijoController',
    './cuentaBancaria/CrearChequeraController',
    './cuentaBancaria/EditarChequeraController',

    './persona/natural/BuscarPersonaNaturalController',
    './persona/natural/CrearPersonaNaturalController',
    './persona/natural/EditarPersonaNaturalController',
    './persona/juridica/BuscarPersonaJuridicaController',
    './persona/juridica/AccionistaController',
    './persona/juridica/CrearPersonaJuridicaController',
    './persona/juridica/EditarPersonaJuridicaController',

    './socio/CrearSocioController',
    './socio/BuscarSocioController',
    './socio/PanelSocioController',
    './socio/BuscarSocioPopUpController',
    './socio/ApoderadoPopUpController',
    './socio/ContratoInactivadoSocioController',
    './socio/HistorialAportesPopUpController',

    './util/CalculadoraController',
    './util/LoginPopUpController',
    './util/ConfirmPopUpController',
    './util/LoadImageController',
    './util/FirmaPopUpController',
    './util/UbigeoController',

    './configuracion/ConfiguracionImpresoraController'

], function () {

});


/*
define([
    'angular'
], function (angular) {
  'use strict';

  angular.module('cajaApp.controllers.MainCtrl', [])
    .controller('MainCtrl', function ($scope) {
      $scope.awesomeThings = [
        'HTML5 Boilerplate',
        'AngularJS',
        'Karma'
      ];
    });
});*/
