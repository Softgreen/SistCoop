package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.TreeSet;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.TransaccionBovedaOtroView;
import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaBoveda;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionEntidadBoveda;

@Remote
public interface TransaccionInternaServiceNT {

	public VoucherTransaccionBovedaCaja getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja);

	public VoucherTransaccionCajaCaja getVoucherTransaccionCajaCaja(BigInteger idTransaccionCajaCaja);

	public TreeSet<GenericDetalle> getDetalleTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja);

	public VoucherTransaccionEntidadBoveda getVoucherTransaccionEntidadBoveda(BigInteger idTransaccionEntidadBoveda);

	public TreeSet<GenericDetalle> getDetalleTransaccionEntidadBoveda(BigInteger idTransaccionEntidadBoveda);

	public VoucherTransaccionBovedaBoveda getVoucherTransaccionBovedaBoveda(BigInteger idTransaccionBovedaBoveda);

	public TransaccionBovedaOtroView getTransaccionesEntidadBoveda(BigInteger idAgencia, Integer offset, Integer limit);

}
