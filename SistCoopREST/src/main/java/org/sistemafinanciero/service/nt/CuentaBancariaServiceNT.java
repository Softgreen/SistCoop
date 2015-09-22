package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.Cheque;
import org.sistemafinanciero.entity.Chequera;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.EstadocuentaBancariaView;
import org.sistemafinanciero.entity.Titular;
import org.sistemafinanciero.entity.type.EstadoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoCuentaBancaria;
import org.sistemafinanciero.entity.type.TipoPersona;

@Remote
public interface CuentaBancariaServiceNT extends AbstractServiceNT<CuentaBancariaView> {

	public CuentaBancariaView findByNumeroCuenta(String numeroCuenta);

	public List<CuentaBancariaView> findAllView();

	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta);

	public List<CuentaBancariaView> findAllView(TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta, Integer offset, Integer limit);

	public List<CuentaBancariaView> findAllView(String filterText);

	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta);

	public List<CuentaBancariaView> findAllView(String filterText, TipoCuentaBancaria[] tipoCuenta, TipoPersona[] persona, EstadoCuentaBancaria[] estadoCuenta, Integer offset, Integer limit);

	public Titular findTitularById(BigInteger id);

	public Beneficiario findBeneficiarioById(BigInteger id);

	public Set<Titular> getTitulares(BigInteger idCuentaBancaria, boolean mode);

	public Set<Beneficiario> getBeneficiarios(BigInteger idCuentaBancaria);

	public List<EstadocuentaBancariaView> getEstadoCuenta(BigInteger idCuenta, Date dateDesde, Date dateHasta, Boolean estado);

	/**
	 * Chequera*/	
	
	public Chequera getChequeraUltima(BigInteger idCuentaBancaria);

	public Chequera getChequera(BigInteger idChequera);
	
	public List<Chequera> getChequeras(BigInteger idCuentaBancaria);

	public List<Cheque> getCheques(BigInteger idChequera);

	public Cheque getCheque(BigInteger idCheque);

	public CuentaBancariaView findByNumeroCheque(BigInteger numeroChequeUnico);

	public Cheque getChequeByNumeroUnico(BigInteger numeroChequeUnico);
	
}
