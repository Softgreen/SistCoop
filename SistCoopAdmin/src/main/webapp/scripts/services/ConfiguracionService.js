define(['./module'], function (services) {
    'use strict';
    services.factory('ConfiguracionService',function(){

        var appName = 'SistCoop';
        var restApiUrl = 'http://192.168.1.100:8080/SistCoopREST/rest';
        //var restApiUrl = 'http://localhost:8080/SistCoopREST/rest';

        //var keycloakApiUrl = 'http://localhost:8080/auth/admin'; 
        var keycloakApiUrl = 'http://192.168.1.100:8080/auth/admin';

        var defaultPrinterName = "EPSON TM-U220";
        var cookiePrinterName = "DefaultPrinterName";

        return {
            getRestApiUrl: function(){
                return restApiUrl;
            },
            getKeycloakApiUrl: function(){
                return keycloakApiUrl;
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
