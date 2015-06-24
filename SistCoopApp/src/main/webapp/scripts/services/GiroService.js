define(['./module'], function (services) {
  'use strict';
  services.factory("GiroService", ["Restangular",
    function (Restangular) {

      var _agenciaService = Restangular.all("giros");
      var baseUrl = "giros";

      return {
        findById: function (id) {
          return Restangular.one(baseUrl, id).get();
        },
        update: function(id, obj) {
          return Restangular.one(baseUrl + "/" + id).customPUT(Restangular.copy(obj),'',{},{});
        },
        getGiros: function (queryParams) {
          return Restangular.all(baseUrl).getList(queryParams);
        },
        getGirosEnviados: function (idAgencia) {
          return Restangular.all(baseUrl+'/enviados').getList({idAgencia: idAgencia});
        },
        getGirosRecibidos: function (idAgencia) {
          return Restangular.all(baseUrl+'/recibidos').getList({idAgencia: idAgencia});
        }
      }
    }])
});
