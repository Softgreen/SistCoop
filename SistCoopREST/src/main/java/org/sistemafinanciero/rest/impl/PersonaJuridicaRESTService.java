package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ws.rs.core.Response;

import org.sistemafinanciero.entity.Accionista;
import org.sistemafinanciero.entity.PersonaJuridica;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.PersonaJuridicaREST;
import org.sistemafinanciero.rest.dto.PersonaJuridicaDTO;
import org.sistemafinanciero.service.nt.MaestroServiceNT;
import org.sistemafinanciero.service.nt.PersonaJuridicaServiceNT;
import org.sistemafinanciero.service.ts.PersonaJuridicaServiceTS;

public class PersonaJuridicaRESTService implements PersonaJuridicaREST {

	private final static String baseUrl = "/personas/juridicas";
	
	@EJB
	private PersonaJuridicaServiceNT personaJuridicaServiceNT;

	@EJB
	private PersonaJuridicaServiceTS personaJuridicaServiceTS;

	@EJB
	private MaestroServiceNT maestroServiceNT;

	@Override
	public Response getTipoDocumentoPersonaJuridica() {
		Response response;
		List<TipoDocumento> list = maestroServiceNT.getTipoDocumento(TipoPersona.JURIDICA);
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findAll(String filterText, Integer offset, Integer limit) {
		List<PersonaJuridica> list = personaJuridicaServiceNT.findAll(filterText, offset, limit);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response findByTipoNumeroDocumento(BigInteger idTipoDocumento, String numeroDocumento) {
		PersonaJuridica personaJuridica = personaJuridicaServiceNT.find(idTipoDocumento, numeroDocumento);
		Response response = Response.status(Response.Status.OK).entity(personaJuridica).build();
		return response;
	}

	@Override
	public Response count() {
		int count = personaJuridicaServiceNT.count();
		Response response = Response.status(Response.Status.OK).entity(count).build();
		return response;
	}

	@Override
	public Response findById(BigInteger id) {
		PersonaJuridica personaJuridica = personaJuridicaServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(personaJuridica).build();
		return response;
	}

	@Override
	public Response create(PersonaJuridicaDTO persona) {
		Response response;
		try {
			PersonaJuridica personaJuridica = new PersonaJuridica();
			personaJuridica.setIdPersonaJuridica(null);
			personaJuridica.setActividadPrincipal(persona.getActividadPrincipal());
			personaJuridica.setCelular(persona.getCelular());
			personaJuridica.setTelefono(persona.getTelefono());
			personaJuridica.setDireccion(persona.getDireccion());
			personaJuridica.setReferencia(persona.getReferencia());
			personaJuridica.setEmail(persona.getEmail());
			personaJuridica.setFechaConstitucion(persona.getFechaConstitucion());
			personaJuridica.setFinLucro(persona.isFinLucro());
			personaJuridica.setNombreComercial(persona.getNombreComercial());
			personaJuridica.setNumeroDocumento(persona.getNumeroDocumento());
			personaJuridica.setRazonSocial(persona.getRazonSocial());
			personaJuridica.setTipoDocumento(persona.getTipoDocumento());
			personaJuridica.setTipoEmpresa(persona.getTipoEmpresa());
			personaJuridica.setUbigeo(persona.getUbigeo());

			PersonaNatural representante = new PersonaNatural();
			representante.setIdPersonaNatural(persona.getIdRepresentanteLegal());
			personaJuridica.setRepresentanteLegal(representante);

			Set<Accionista> accionistasFinal = new HashSet<Accionista>();
			Set<org.sistemafinanciero.rest.dto.PersonaJuridicaDTO.Accionista> accionistas = persona.getAccionistas();
			for (org.sistemafinanciero.rest.dto.PersonaJuridicaDTO.Accionista accionista : accionistas) {
				Accionista accionistaFinal = new Accionista();
				accionistaFinal.setIdAccionista(null);
				PersonaNatural person = new PersonaNatural();
				person.setIdPersonaNatural(accionista.getIdPersona());
				accionistaFinal.setPersonaNatural(person);
				accionistaFinal.setPorcentajeParticipacion(accionista.getPorcentaje());

				accionistasFinal.add(accionistaFinal);
			}
			personaJuridica.setAccionistas(accionistasFinal);
			BigInteger idPersona = personaJuridicaServiceTS.create(personaJuridica);
			URI resource = new URI(baseUrl + "/" + idPersona.toString());
			response = Response.created(resource).entity(Jsend.getSuccessJSend(idPersona)).build();			
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (EJBException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (URISyntaxException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response update(BigInteger id, PersonaJuridicaDTO persona) {
		Response response;
		try {
			PersonaJuridica personaJuridica = new PersonaJuridica();
			personaJuridica.setIdPersonaJuridica(null);
			personaJuridica.setActividadPrincipal(persona.getActividadPrincipal());
			personaJuridica.setCelular(persona.getCelular());
			personaJuridica.setTelefono(persona.getTelefono());
			personaJuridica.setDireccion(persona.getDireccion());
			personaJuridica.setReferencia(persona.getReferencia());
			personaJuridica.setEmail(persona.getEmail());
			personaJuridica.setFechaConstitucion(persona.getFechaConstitucion());
			personaJuridica.setFinLucro(persona.isFinLucro());
			personaJuridica.setNombreComercial(persona.getNombreComercial());
			personaJuridica.setNumeroDocumento(persona.getNumeroDocumento());
			personaJuridica.setRazonSocial(persona.getRazonSocial());
			personaJuridica.setTipoDocumento(persona.getTipoDocumento());
			personaJuridica.setTipoEmpresa(persona.getTipoEmpresa());
			personaJuridica.setUbigeo(persona.getUbigeo());

			PersonaNatural representante = new PersonaNatural();
			representante.setIdPersonaNatural(persona.getIdRepresentanteLegal());
			personaJuridica.setRepresentanteLegal(representante);

			Set<Accionista> accionistasFinal = new HashSet<Accionista>();
			Set<org.sistemafinanciero.rest.dto.PersonaJuridicaDTO.Accionista> accionistas = persona.getAccionistas();
			for (org.sistemafinanciero.rest.dto.PersonaJuridicaDTO.Accionista accionista : accionistas) {
				Accionista accionistaFinal = new Accionista();
				accionistaFinal.setIdAccionista(null);
				PersonaNatural person = new PersonaNatural();
				person.setIdPersonaNatural(accionista.getIdPersona());
				accionistaFinal.setPersonaNatural(person);
				accionistaFinal.setPorcentajeParticipacion(accionista.getPorcentaje());
				
				accionistasFinal.add(accionistaFinal);
			}
			personaJuridica.setAccionistas(accionistasFinal);

			personaJuridicaServiceTS.update(id, personaJuridica);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

}
