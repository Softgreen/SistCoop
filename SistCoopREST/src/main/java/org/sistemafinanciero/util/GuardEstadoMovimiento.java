package org.sistemafinanciero.util;

import java.lang.reflect.Method;
import java.util.List;

import javax.inject.Inject;
import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Caja;

/**
 * 
 * @author adam-bien.com
 */
public class GuardEstadoMovimiento {

	@Inject
	private UsuarioSession usuarioSession;

	@Inject
	private DAO<Object, Caja> cajaDAO;

	@AroundInvoke
	public Object validatePermissions(InvocationContext ic) throws Exception {
		Method method = ic.getMethod();

		Caja caja = null;
		String username = usuarioSession.getUsername();
		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Caja> list = cajaDAO.findByNamedQuery(Caja.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			for (Caja c : list) {
				caja = c;
			}
		} else {
			throw new SecurityException("se encontro mas de un usuario para la caja seleccionada");
		}

		if (!isAllowed(method, caja)) {
			throw new SecurityException("Caja no tiene permitido hacer esta operacion, verifique su estado CONGELADO/DESCONGELADO");
		}
		return ic.proceed();
	}

	boolean isAllowed(Method method, Caja caja) {
		AllowedToEstadoMovimiento annotation = method.getAnnotation(AllowedToEstadoMovimiento.class);
		if (annotation == null) {
			return true;
		}
		EstadoMovimiento[] permissions = annotation.value();
		for (EstadoMovimiento permission : permissions) {
			if (permission.equals(EstadoMovimiento.CONGELADO)) {
				return caja.getEstadoMovimiento() == false;
			}
			if (permission.equals(EstadoMovimiento.DESCONGELADO)) {
				return caja.getEstadoMovimiento() == true;
			}
		}
		return false;
	}
}
