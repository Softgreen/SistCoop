package org.sistemafinanciero.util;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.SessionContext;
import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TrabajadorCaja;
import org.sistemafinanciero.entity.TrabajadorUsuario;
import org.sistemafinanciero.entity.Usuario;

/**
 * 
 * @author adam-bien.com
 */
public class Guard {

	@Resource
	private SessionContext context;

	@Inject
	private DAO<Object, Usuario> usuarioDAO;

	@AroundInvoke
	public Object validatePermissions(InvocationContext ic) throws Exception {
		Method method = ic.getMethod();

		KeycloakPrincipal p = (KeycloakPrincipal) context.getCallerPrincipal();
		KeycloakSecurityContext kcSecurityContext = p.getKeycloakSecurityContext();                
		String username = kcSecurityContext.getToken().getPreferredUsername();
		
		Caja caja = null;
		Usuario usuario = null;
		Trabajador trabajador = null;
		QueryParameter queryParameter = QueryParameter.with("username",username);
		List<Usuario> result = usuarioDAO.findByNamedQuery(Usuario.findByUsername, queryParameter.parameters());
		for (Usuario u : result) {
			usuario = u;
			break;
		}
		Set<TrabajadorUsuario> listTrabajadores = usuario.getTrabajadorUsuarios();
		for (TrabajadorUsuario trabajadorUsuario : listTrabajadores) {
			trabajador = trabajadorUsuario.getTrabajador();
			break;
		}
		Set<TrabajadorCaja> cajas = trabajador.getTrabajadorCajas();
		for (TrabajadorCaja trabajadorCaja : cajas) {
			caja = trabajadorCaja.getCaja();
			break;
		}

		if (!isAllowed(method, caja)) {
			throw new SecurityException("Caja no tiene permitido hacer esta operacion, verifique su estado ABIERTO/CERRADO");
		}
		return ic.proceed();
	}

	boolean isAllowed(Method method, Caja caja) {
		AllowedTo annotation = method.getAnnotation(AllowedTo.class);
		if (annotation == null) {
			return true;
		}
		Permission[] permissions = annotation.value();
		for (Permission permission : permissions) {
			if (permission.equals(Permission.ABIERTO)) {
				return caja.getAbierto() == true;
			}
			if (permission.equals(Permission.CERRADO)) {
				return caja.getAbierto() == false;
			}
		}
		return false;
	}
}
