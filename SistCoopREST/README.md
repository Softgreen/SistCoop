keycloack configuration
1. crear datasource KEYCLOAKDS
2. configurar wildfly
3. copiar y pegar .war de keycloak al wildfly
4. configurar persistence.xml indicando hibernate.dialect si es necesario.
5. en caso de usar oracle 12c, usar hibernate-core-4.3.5.Final.jar y usar Oracle12cDialect
6. una vez configurado todo ya se puede acceder a http://localhost:8080/auth/admin/index.html 
7. crear un nuevo Reaml "SistemaFinanciero".
8. crear un nuevo Rol "cajero".
9. crear un nuevo Usuarrio "caja" y cambiar el password.
10. asignar el Rol "cajero" al usuario "caja".

SistCoopApp
11. crear una nueva aplicacion "SistCoopApp" con los datos:
	access type: confidential
	redirect url: http://localhost:8080/SistCoopApp/*
	base url: http://localhost:8080/SistCoopApp/index.caja.html	
	admin url: http://localhost:8080/SistCoopApp/index.caja.html
	web origin: http://localhost:8080
	scope: "cajero".

SistCoopREST	
	access type: confidential
	redirect url: http://localhost:8080/SistCoopREST/*
	base url: 	
	admin url: 
	web origin: http://localhost:8080
	scope: "cajero".
	
SistCoopJefeCajaApp
redirect url: /SistCoopJefeCajaApp/*
base url: /SistCoopJefeCajaApp/index.html
	
12. crear web.xml y poner el contenido siguiente:

	<module-name>SistCoopApp</module-name>
	<security-constraint>
		<web-resource-collection>
			<web-resource-name>Cajeros</web-resource-name>
			<url-pattern>/*</url-pattern>
		</web-resource-collection>
		<auth-constraint>
			<role-name>cajero</role-name>
		</auth-constraint>
	</security-constraint>
	<login-config>
		<auth-method>KEYCLOAK</auth-method>
		<realm-name>this is ignored currently</realm-name>
	</login-config>
	<security-role>
		<role-name>cajero</role-name>
	</security-role>
	
13. crear un archivo keycloak.json:
	{
	  "realm": "SistemaFinanciero",
	  "realm-public-key": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCfIWy7oLXemo73S73QqaSCX/vIXZF7hL8BJ4CIlTZmB9yYgiUAMDe+7dyZXT2EuaFrsbZIzjiBryA87/iNeR04eAkuQ89P0GvFzQgSduSKh2xIYQRQZc/vyBgRk7eV3HBLq/4evIGK5GubFPxUHP7JLiTOsH8zilSPtePdeo3KzQIDAQAB",
	  "auth-server-url": "http://localhost:8080/auth",
	  "ssl-not-required": true,
	  "resource": "SistCoopApp",
	  "credentials": {
	    "secret": "15276201-f7ba-4fee-a9e8-24757e936fe5"
	  }
	}

14. copiar y pegar el contenido de keycloak-wildfly-adapter-dist-1.0-beta-3.zip en %JBOSS_HOME%/modules
15. editar el archivo standalone.xml de wildfly y poner

	<server xmlns="urn:jboss:domain:1.4">
		<extensions>
			<extension module="org.keycloak.keycloak-wildfly-subsystem"/>
	 		...
	 	</extensions>
	 	<profile>
 			<subsystem xmlns="urn:jboss:domain:keycloak:1.0"/>
 		...
 		</profile>
