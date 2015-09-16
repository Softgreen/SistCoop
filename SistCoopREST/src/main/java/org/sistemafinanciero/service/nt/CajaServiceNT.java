package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.HistorialCaja;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.Moneda;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PendienteCajaFaltanteView;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.entity.TransaccionBovedaCajaView;
import org.sistemafinanciero.entity.TransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.CajaCierreMoneda;
import org.sistemafinanciero.entity.dto.CajaView;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;
import org.sistemafinanciero.entity.dto.ResumenOperacionesCaja;
import org.sistemafinanciero.entity.dto.VoucherCompraVenta;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBancaria;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCheque;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCuentaAporte;
import org.sistemafinanciero.entity.dto.VoucherTransferenciaBancaria;

@Remote
public interface CajaServiceNT extends AbstractServiceNT<Caja> {

	public List<CajaView> findAllView(BigInteger idAgencia);
	
	public Set<Boveda> getBovedas(BigInteger idCaja);

	public Set<Moneda> getMonedas(BigInteger idCaja);

	public Set<HistorialCaja> getHistorialCaja(BigInteger idCaja, Date dateDesde, Date dateHasta);

	public Set<GenericMonedaDetalle> getDetalleCaja(BigInteger idCaja);

	public Set<GenericMonedaDetalle> getDetalleCaja(BigInteger idCaja, BigInteger idHistorialCaja);

	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idCaja);

	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idCaja);

	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja(BigInteger idCaja);

	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja(BigInteger idCaja);

	public List<TransaccionBovedaCajaView> getTransaccionesEnviadasBovedaCaja(BigInteger idCaja, BigInteger idHistorialCaja);

	public List<TransaccionBovedaCajaView> getTransaccionesRecibidasBovedaCaja(BigInteger idCaja, BigInteger idHistorialCaja);

	public Set<TransaccionCajaCaja> getTransaccionesEnviadasCajaCaja(BigInteger idCaja, BigInteger idHistorialCaja);

	public Set<TransaccionCajaCaja> getTransaccionesRecibidasCajaCaja(BigInteger idCaja, BigInteger idHistorialCaja);

	public Set<CajaCierreMoneda> getVoucherCierreCaja(BigInteger idHistorial);

	public ResumenOperacionesCaja getResumenOperacionesCaja(BigInteger idHistorial);

	public VoucherTransaccionCuentaAporte getVoucherCuentaAporte(BigInteger idTransaccion);

	public VoucherTransaccionBancaria getVoucherTransaccionBancaria(BigInteger idTransaccionBancaria);
	
	public VoucherTransferenciaBancaria getVoucherTransferenciaBancaria(BigInteger idTransferencia);

	public VoucherCompraVenta getVoucherCompraVenta(BigInteger idTransaccionCompraVenta);

	public VoucherTransaccionCheque getVoucherTransaccionCheque(BigInteger idTransaccionCheque);
	
	public Set<PendienteCaja> getPendientes(BigInteger idCaja, BigInteger idHistorialCaja);
	
	public List<PendienteCajaFaltanteView> getPendientesPorPagar(BigInteger idCaja);

	public List<HistorialTransaccionCaja> getHistorialTransaccion(BigInteger idCaja, BigInteger idHistorial, String filterText);
	
	public List<Trabajador> getTrabajadores(BigInteger idCaja);	

}
