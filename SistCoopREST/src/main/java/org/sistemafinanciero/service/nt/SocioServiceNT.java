package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Beneficiario;
import org.sistemafinanciero.entity.CuentaAporte;
import org.sistemafinanciero.entity.CuentaBancariaView;
import org.sistemafinanciero.entity.HistorialAportesSP;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.SocioView;
import org.sistemafinanciero.entity.type.TipoPersona;

@Remote
public interface SocioServiceNT extends AbstractServiceNT<SocioView> {

	public List<SocioView> findAllView(Boolean estadoCuentaAporte, Boolean estadoSocio);

	public List<SocioView> findAllView(Boolean estadoCuentaAporte, Boolean estadoSocio, Integer offset, Integer limit);

	public List<SocioView> findAllView(String filterText);

	public List<SocioView> findAllView(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio);

	public List<SocioView> findAllView(String filterText, Integer offset, Integer limit);

	public List<SocioView> findAllView(String filterText, Boolean estadoCuentaAporte, Boolean estadoSocio, Integer offset, Integer limit);

	public SocioView find(TipoPersona tipoPersona, BigInteger idTipoDocumento, String numeroDocumento);

	public PersonaNatural getApoderado(BigInteger idSocio);

	public CuentaAporte getCuentaAporte(BigInteger idSocio);

	public List<CuentaBancariaView> getCuentasBancarias(BigInteger idSocio);
	
	public Set<Beneficiario> getBeneficiarios(BigInteger idSocio);
	
	public Beneficiario findBeneficiarioById(BigInteger id);

	public List<HistorialAportesSP> getHistorialAportes(BigInteger idSocio, Date desde, Date hasta, BigInteger offset, BigInteger limit);

}
