define(['./module'], function (services) {
    'use strict';
    services.factory('ConfiguracionService',function(){

        var appName = 'SistCoop';
        //var restApiUrl = 'http://192.168.1.12:8080/SistCoopREST/rest';
        var restApiUrl = 'http://localhost:8080/SistCoopREST/rest';

        var defaultPrinterName = "EPSON TM-U220";
        var cookiePrinterName = "DefaultPrinterName";

        return {
            getRestApiUrl: function(){
                return restApiUrl;
            },
            getAppName: function(){
                return appName;
            },
            getDefaultPrinterName: function(){
                return defaultPrinterName;
            },
            getCookiePrinterName: function(){
                return cookiePrinterName;
            }
        }
    })
});