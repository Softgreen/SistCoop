package org.sistemafinanciero.service.nt;

import java.math.BigInteger;
import java.util.List;

import javax.ejb.Remote;

import org.sistemafinanciero.entity.Agencia;
import org.sistemafinanciero.entity.Caja;
import org.sistemafinanciero.entity.Trabajador;
import org.sistemafinanciero.exception.NonexistentEntityException;

@Remote
public interface TrabajadorServiceNT extends AbstractServiceNT<Trabajador> {

	public List<Trabajador> findAllByFilterTextAndAgencia(String filterText, BigInteger idAgencia);
	
	public Trabajador findByUsuario(String username);

	public Caja findByTrabajador(BigInteger idTrabajador) throws NonexistentEntityException;

	public Agencia getAgencia(BigInteger idTrabajador) throws NonexistentEntityException;

}
