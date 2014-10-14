package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.dto.GenericDetalle;
import org.sistemafinanciero.entity.dto.VoucherTransaccionBovedaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionCajaCaja;
import org.sistemafinanciero.entity.dto.VoucherTransaccionEntidadBoveda;


@Remote
public interface TransaccionInternaServiceNT {
	
	public VoucherTransaccionBovedaCaja getVoucherTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja);
	
	public VoucherTransaccionCajaCaja getVoucherTransaccionCajaCaja(BigInteger idTransaccionCajaCaja);

	public List<GenericDetalle> getDetalleTransaccionBovedaCaja(BigInteger idTransaccionBovedaCaja);

	public VoucherTransaccionEntidadBoveda getVoucherTransaccionEntidadBoveda(BigInteger idTransaccionEntidadBoveda);
}
