define(['./module'], function (services) {
    'use strict';
    services.factory('ConfiguracionService',function(){

        var appName = 'SistCoop';
        var restApiUrl = 'http://localhost:8080/SistCoopREST/rest';

        var defaultPrinterName = "EPSON TM-U220";

        return {
            getRestApiUrl: function(){
                return restApiUrl;
            },
            getAppName: function(){
                return appName;
            },
            getDefaultPrinterName: function(){
                return defaultPrinterName;
            }
        }
    })
});