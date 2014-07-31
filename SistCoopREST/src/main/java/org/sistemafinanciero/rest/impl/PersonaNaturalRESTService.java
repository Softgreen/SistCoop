package org.sistemafinanciero.rest.impl;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.xml.ws.soap.Addressing;

import org.jboss.resteasy.links.AddLinks;
import org.jboss.resteasy.links.LinkResource;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.PersonaNaturalREST;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.ts.PersonaNaturalServiceTS;

public class PersonaNaturalRESTService implements PersonaNaturalREST {

	@Inject
	private Validator validator;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private PersonaNaturalServiceTS personaNaturalServiceTS;

	@Override
	public Response findById(BigInteger id) {
		PersonaNatural personaNatural = personaNaturalServiceNT.findById(id);
		Response response = Response.status(Response.Status.OK).entity(personaNatural).build();
		return response;
	}

	@Override
	public Response findByTipoNumeroDocumento(BigInteger idTipoDocumento, String numeroDocumento) {
		PersonaNatural personaNatural = personaNaturalServiceNT.find(idTipoDocumento, numeroDocumento);
		Response response = Response.status(Response.Status.OK).entity(personaNatural).build();
		return response;
	}

	@Override
	@AddLinks
	@LinkResource(value = PersonaNatural.class)
	public Response listAll(String filterText, Integer offset, Integer limit) {
		List<PersonaNatural> list = personaNaturalServiceNT.findAll(filterText, offset, limit);
		Response response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

	@Override
	public Response countAll() {
		int count = personaNaturalServiceNT.count();
		Response response = Response.status(Response.Status.OK).entity(count).build();
		return response;
	}

	@Override
	public Response update(BigInteger id, PersonaNatural personanatural) {
		personanatural.setIdPersonaNatural(null);
		Response response = Response.status(Response.Status.OK).build();
		return response;
	}

	@Override
	// @AddLinks
	@LinkResource
	public Response create(PersonaNatural personaNatural) {
		Response response;
		try {
			Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(personaNatural);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
			BigInteger idPersona = personaNaturalServiceTS.create(personaNatural);
			personaNaturalServiceTS.create(personaNatural);
			response = Response.status(Response.Status.CREATED).build();
		} catch (ConstraintViolationException e) {
			/*
			 * for (ConstraintViolation<?> violation :
			 * e.getConstraintViolations()) {
			 * jsend.addMessage(violation.getPropertyPath().toString() + " " +
			 * violation.getMessage()); }
			 */
		} catch (PreexistingEntityException e) {
			response = Response.status(Response.Status.CONFLICT).build();
		} catch (RollbackFailureException e) {
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return null;
	}

	@Override
	public Response delete(BigInteger id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getFirma(String id, int flowChunkNumber, int flowChunkSize, int flowCurrentChunkSize, String flowFilename, String flowIdentifier, String flowRelativePath, int flowTotalChunks, int flowTotalSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getFoto(String id, int flowChunkNumber, int flowChunkSize, int flowCurrentChunkSize, String flowFilename, String flowIdentifier, String flowRelativePath, int flowTotalChunks, int flowTotalSize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response uploadFirma(BigInteger id, MultipartFormDataInput input) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response uploadFoto(BigInteger id, MultipartFormDataInput input) {
		// TODO Auto-generated method stub
		return null;
	}

}
