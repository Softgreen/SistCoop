package org.sistemafinanciero.service.nt;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Boveda;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.HistorialTransaccionCaja;
import org.sistemafinanciero.entity.PendienteCaja;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.dto.GenericMonedaDetalle;

@Remote
public interface CajaSessionServiceNT {

	public PersonaNatural getPersonaOfSession();

	public Caja getCajaOfSession();

	public Agencia getAgenciaOfSession();

	public Map<Boveda, BigDecimal> getDiferenciaSaldoCaja(Set<GenericMonedaDetalle> detalleCaja);

	public Set<PendienteCaja> getPendientesCaja();

	public List<HistorialTransaccionCaja> getHistorialTransaccion();

	public List<HistorialTransaccionCaja> getHistorialTransaccion(String filterText);

}
