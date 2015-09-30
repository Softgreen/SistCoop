define(['./module'], function (services) {
  'use strict';
  services.factory("AgenciaService", ["Restangular",
    function (Restangular) {

      var _agenciaService = Restangular.all("agencias");
      var baseUrl = "agencias";

      return {
        getAgencias: function (estado) {
          return Restangular.all(baseUrl).getList({estado: estado});
        },
        getCajas: function (idAgencia) {
          return Restangular.all(baseUrl + "/" + idAgencia + "/cajas").getList();
        },
        getGirosEnviados: function (idAgencia, estado, filterText, desde, hasta) {
          return Restangular.all(baseUrl + "/" + idAgencia + "/giros/enviados").getList({
            estado: estado,
            filterText: filterText,
            offset: desde,
            limit: hasta
          });
        },
        getGirosRecibidos: function (idAgencia, estado, filterText, desde, hasta) {
          return Restangular.all(baseUrl + "/" + idAgencia + "/giros/recibidos").getList({
            estado: estado,
            filterText: filterText,
            offset: desde,
            limit: hasta
          });
        },
        countEnviados: function (idAgencia) {
          return Restangular.one(baseUrl + "/" + idAgencia + "/giros/enviados/count").get();
        },
        countRecibidos: function (idAgencia) {
          return Restangular.one(baseUrl + "/" + idAgencia + "/giros/recibidos/count").get();
        }
      }
    }])
});
