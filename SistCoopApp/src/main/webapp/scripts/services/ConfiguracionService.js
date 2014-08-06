define(['./module'], function (services) {
    'use strict';
    services.factory('ConfiguracionService',function(){

        var appName = 'SistCoop';
        var restApiUrl = 'http://localhost:8080/SistCoopREST/rest';

        return {
            getRestApiUrl: function(){
                return restApiUrl;
            },
            getAppName: function(){
                return appName;
            }
        }
    })
});