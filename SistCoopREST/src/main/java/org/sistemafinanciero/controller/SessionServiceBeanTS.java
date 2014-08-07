package org.sistemafinanciero.controller;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Query;

import org.sistemafinanciero.dao.DAO;
import org.sistemafinanciero.dao.QueryParameter;
import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.BovedaCaja;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.Socio;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TransaccionCuentaAporte;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.entity.type.Tipotransaccionbancaria;
import org.sistemafinanciero.entity.type.Tipotransaccioncompraventa;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.service.ts.SessionServiceTS;
import org.sistemafinanciero.util.EntityManagerProducer;
import org.sistemafinanciero.util.UsuarioSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Named
@Remote(SessionServiceTS.class)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class SessionServiceBeanTS implements SessionServiceTS {

	@Inject
	private DAO<Object, Socio> socioDAO;

	@Inject
	private DAO<Object, Caja> cajaDAO;

	@Inject
	private DAO<Object, HistorialCaja> historialCajaDAO;

	@Inject
	private DAO<Object, Trabajador> trabajadorDAO;

	@Inject
	private DAO<Object, CuentaAporte> cuentaAporteDAO;

	@Inject
	private DAO<Object, TransaccionCuentaAporte> transaccionCuentaAporteDAO;

	@Inject
	private DAO<Object, Moneda> monedaDAO;

	@Inject
	private DAO<Object, BovedaCaja> bovedaCajaDAO;

	@Inject
	private EntityManagerProducer em;

	@Inject
	private UsuarioSession usuarioSession;

	private Logger LOGGER = LoggerFactory.getLogger(SessionServiceBeanTS.class);

	private Trabajador getTrabajador() {
		String username = usuarioSession.getUsername();

		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Trabajador> list = trabajadorDAO.findByNamedQuery(Trabajador.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Trabajador trabajador = null;
			for (Trabajador t : list) {
				trabajador = t;
			}
			return trabajador;
		} else {
			System.out.println("Error: mas de un usuario registrado");
			return null;
		}
	}

	private Caja getCaja() {
		String username = usuarioSession.getUsername();
		QueryParameter queryParameter = QueryParameter.with("username", username);
		List<Caja> list = cajaDAO.findByNamedQuery(Caja.findByUsername, queryParameter.parameters());
		if (list.size() <= 1) {
			Caja caja = null;
			for (Caja c : list) {
				caja = c;
			}
			return caja;
		} else {
			System.out.println("Error: mas de un usuario registrado");
			return null;
		}
	}

	private HistorialCaja getHistorialActivo() {
		Caja caja = getCaja();
		HistorialCaja cajaHistorial = null;
		QueryParameter queryParameter = QueryParameter.with("idcaja", caja.getIdCaja());
		List<HistorialCaja> list = historialCajaDAO.findByNamedQuery(HistorialCaja.findByHistorialActivo, queryParameter.parameters());
		for (HistorialCaja c : list) {
			cajaHistorial = c;
			break;
		}
		return cajaHistorial;
	}

	private BigInteger getNumeroOperacion() {
		Agencia agencia = new Agencia();
		Caja caja = this.getCaja();
		Set<BovedaCaja> lisBC = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : lisBC) {
			agencia = bovedaCaja.getBoveda().getAgencia();
		}

		Query query = em.getEm().createNativeQuery(
				"SELECT T.Numero_Operacion AS numero_operacion FROM Transaccion_Bancaria T Inner Join Historial_Caja HC on (HC.id_historial_caja = T.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and T.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) " + "union "
						+ "select Tcv.Numero_Operacion AS numero_operacion From Transaccion_Compra_Venta TCV Inner Join Historial_Caja HC on (HC.id_historial_caja = Tcv.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and Tcv.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) " + "union "
						+ "select Tb.Numero_Operacion AS numero_operacion From Transferencia_Bancaria TB Inner Join Historial_Caja HC on (HC.id_historial_caja = Tb.Id_Historial_Caja) Inner Join Caja C on (C.Id_Caja = Hc.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.Id_Caja) Inner Join Boveda B on (B.id_boveda = Bc.Id_Boveda) Where B.Id_Agencia = :idagencia and Tb.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) " + "union "
						+ "select Ap.Numero_Operacion AS numero_operacion from Transaccion_Cuenta_Aporte AP Inner Join Historial_Caja HC on (HC.id_historial_caja = AP.id_historial_caja) Inner Join Caja C on (HC.Id_Caja = C.Id_Caja) Inner Join Boveda_Caja BC on (BC.id_caja = C.id_caja) Inner Join Boveda B on (B.id_boveda = BC.id_boveda) where B.Id_Agencia = :idagencia and ap.Fecha = (select to_char(systimestamp,'dd/mm/yy') from dual) ORDER BY numero_operacion DESC");
		query.setParameter("idagencia", agencia.getIdAgencia());

		List<BigDecimal> list = query.getResultList();
		if (list.size() == 0) {
			return BigInteger.ONE;
		} else {
			BigDecimal op = list.get(0);
			BigInteger numero_operacion = op.toBigInteger();
			return numero_operacion.add(BigInteger.ONE);
		}
	}

	private void actualizarSaldoCaja(BigDecimal monto, BigInteger idMoneda) throws RollbackFailureException {
		Moneda monedaTransaccion = monedaDAO.find(idMoneda);
		Caja caja = this.getCaja();
		Set<BovedaCaja> bovedasCajas = caja.getBovedaCajas();
		for (BovedaCaja bovedaCaja : bovedasCajas) {
			Moneda monedaBoveda = bovedaCaja.getBoveda().getMoneda();
			if (monedaTransaccion.equals(monedaBoveda)) {
				BigDecimal saldoActual = bovedaCaja.getSaldo();
				BigDecimal saldoFinal = saldoActual.add(monto);
				if (saldoFinal.compareTo(BigDecimal.ZERO) >= 0) {
					bovedaCaja.setSaldo(saldoFinal);
					bovedaCajaDAO.update(bovedaCaja);
				} else {
					throw new RollbackFailureException("Saldo menor a cero, no se puede modificar saldo de caja");
				}
				break;
			}
		}
	}

	@Override
	public BigInteger abrirCaja() throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger cerrarCaja(Set<GenericMonedaDetalle> detalleCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearAporte(BigInteger idSocio, BigDecimal monto, int mes, int anio, String referencia) throws RollbackFailureException {		
		Socio socio = socioDAO.find(idSocio);
		if (socio == null)
			throw new RollbackFailureException("Socio no encontrado");
		CuentaAporte cuentaAporte = socio.getCuentaAporte();
		if (cuentaAporte == null)
			throw new RollbackFailureException("Socio no tiene cuenta de aportes");

		if (monto.compareTo(BigDecimal.ZERO) != 1) {
			throw new RollbackFailureException("Monto invalido para transaccion");
		}

		switch (cuentaAporte.getEstadoCuenta()) {
		case CONGELADO:
			throw new RollbackFailureException("Cuenta CONGELADA, no se pueden realizar transacciones");
		case INACTIVO:
			throw new RollbackFailureException("Cuenta INACTIVO, no se pueden realizar transacciones");
		default:
			break;
		}

		// obteniendo datos de caja en session
		HistorialCaja historialCaja = this.getHistorialActivo();
		Trabajador trabajador = this.getTrabajador();
		PersonaNatural natural = trabajador.getPersonaNatural();

		// obteniendo saldo disponible de cuenta
		BigDecimal saldoDisponible = cuentaAporte.getSaldo().add(monto);
		cuentaAporte.setSaldo(saldoDisponible);
		cuentaAporteDAO.update(cuentaAporte);

		Calendar calendar = Calendar.getInstance();

		TransaccionCuentaAporte transaccionCuentaAporte = new TransaccionCuentaAporte();
		transaccionCuentaAporte.setAnioAfecta(anio);
		transaccionCuentaAporte.setMesAfecta(mes);
		transaccionCuentaAporte.setCuentaAporte(cuentaAporte);
		transaccionCuentaAporte.setEstado(true);
		transaccionCuentaAporte.setFecha(calendar.getTime());
		transaccionCuentaAporte.setHistorialCaja(historialCaja);
		transaccionCuentaAporte.setHora(calendar.getTime());
		transaccionCuentaAporte.setMonto(monto);
		transaccionCuentaAporte.setNumeroOperacion(this.getNumeroOperacion());
		transaccionCuentaAporte.setReferencia(referencia);
		transaccionCuentaAporte.setObservacion("Doc:" + natural.getTipoDocumento().getAbreviatura() + "/" + natural.getNumeroDocumento() + "Trabajador:" + natural.getApellidoPaterno() + " " + natural.getApellidoMaterno() + "," + natural.getNombres());
		transaccionCuentaAporte.setSaldoDisponible(saldoDisponible);
		transaccionCuentaAporte.setTipoTransaccion(Tipotransaccionbancaria.DEPOSITO);

		transaccionCuentaAporteDAO.create(transaccionCuentaAporte);
		// actualizar saldo caja
		this.actualizarSaldoCaja(monto, cuentaAporte.getMoneda().getIdMoneda());
		return transaccionCuentaAporte.getIdTransaccionCuentaAporte();
	}

	@Override
	public BigInteger retiroCuentaAporte(BigInteger idSocio) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionBancaria(String numeroCuenta, BigDecimal monto, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionCompraVenta(Tipotransaccioncompraventa tipoTransaccion, BigInteger idMonedaRecibido, BigInteger idMonedaEntregado, BigDecimal montoRecibido, BigDecimal montoEntregado, BigDecimal tasaCambio, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransferenciaBancaria(String numeroCuentaOrigen, String numeroCuentaDestino, BigDecimal monto, String referencia) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void extornarTransaccion(BigInteger idTransaccion) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public BigInteger[] crearCuentaBancariaPlazoFijoConDeposito(String codigo, BigInteger idMoneda, TipoPersona tipoPersona, BigInteger idPersona, int cantRetirantes, BigDecimal monto, int periodo, BigDecimal tasaInteres, List<BigInteger> titulares, List<Beneficiario> beneficiarios) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger cancelarCuentaBancariaConRetiro(BigInteger id) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger cancelarSocioConRetiro(BigInteger idSocio) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearPendiente(BigInteger idBoveda, BigDecimal monto, String observacion) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionBovedaCaja(BigInteger idBoveda, Set<GenericDetalle> detalleTransaccion) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigInteger crearTransaccionCajaCaja(BigInteger idCajadestino, BigInteger idMoneda, BigDecimal monto, String observacion) throws RollbackFailureException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void confirmarTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancelarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

	@Override
	public void confirmarTransaccionCajaCaja(BigInteger idTransaccionCajaCaja) throws RollbackFailureException {
		// TODO Auto-generated method stub

	}

}