package org.sistemafinanciero.service.nt;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.PersonaNatural;

@Remote
public interface SessionServiceNT {

	public PersonaNatural getPersonaOfSession();

	public Caja getCajaOfSession();

	public Agencia getAgenciaOfSession();

}
