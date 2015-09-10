define(['./module'], function (services) {
  'use strict';
  services.factory("ReportesService", ["Restangular",
    function (Restangular) {

      var baseUrl = "reportes";

      return {
        getDebeHaber: function (queryParams) {
          return Restangular.all(baseUrl+'/debeHaber').getList(queryParams);
        }

      }
    }])
});
