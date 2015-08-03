define(['./module'], function (services) {
  'use strict';
  services.factory("SobreGiroService", ["Restangular",
    function (Restangular) {

      var _agenciaService = Restangular.all("sobreGiros");
      var baseUrl = "sobreGiros";

      return {
        findById: function (id) {
          return Restangular.one(baseUrl, id).get();
        },
        update: function(id, obj) {
          return Restangular.one(baseUrl + "/" + id).customPUT(Restangular.copy(obj),'',{},{});
        },
        getSobreGiros: function (queryParams) {
          return Restangular.all(baseUrl).getList(queryParams);
        },

        getHistoriales: function (idSobreGiro) {
          return Restangular.one(baseUrl, idSobreGiro).all('historiales').getList();
        }
      }
    }])
});
