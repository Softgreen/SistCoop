package org.sistemafinanciero.rest.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.apache.commons.io.IOUtils;
import org.jboss.resteasy.annotations.cache.NoCache;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.sistemafinanciero.entity.PersonaNatural;
import org.sistemafinanciero.entity.TipoDocumento;
import org.sistemafinanciero.entity.type.TipoPersona;
import org.sistemafinanciero.exception.NonexistentEntityException;
import org.sistemafinanciero.exception.PreexistingEntityException;
import org.sistemafinanciero.exception.RollbackFailureException;
import org.sistemafinanciero.rest.Jsend;
import org.sistemafinanciero.rest.PersonaNaturalREST;
import org.sistemafinanciero.service.nt.MaestroServiceNT;
import org.sistemafinanciero.service.nt.PersonaNaturalServiceNT;
import org.sistemafinanciero.service.ts.PersonaNaturalServiceTS;

public class PersonaNaturalRESTService implements PersonaNaturalREST {

	private final static String baseUrl = "/personas/naturales";
	//private final String UPLOADED_FIRMA_PATH = "d:\\firmas\\";
	private final String UPLOADED_FIRMA_PATH = "//usr//share//jboss//archivos//firmas//";
	//private final String UPLOADED_FOTO_PATH = "d:\\fotos\\";
	private final String UPLOADED_FOTO_PATH = "//usr//share//jboss//archivos//fotos//";

	@Inject
	private Validator validator;

	@EJB
	private PersonaNaturalServiceNT personaNaturalServiceNT;

	@EJB
	private PersonaNaturalServiceTS personaNaturalServiceTS;

	@EJB
	private MaestroServiceNT maestroServiceNT;

	@Override
	public Response getTipoDocumentoPersonaNatural() {
		Response response;
		List<TipoDocumento> list = maestroServiceNT.getTipoDocumento(TipoPersona.NATURAL);
		response = Response.status(Response.Status.OK).entity(list).build();
		return response;
	}

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

	@NoCache
	@Override
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
	public Response update(BigInteger id, PersonaNatural personaNatural) {
		Response response;
		try {
			Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(personaNatural);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
			personaNatural.setIdPersonaNatural(null);
			personaNaturalServiceTS.update(id, personaNatural);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (ConstraintViolationException e) {
			Jsend jsend = Jsend.getErrorJSend("datos invalidos");
			for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
				jsend.addMessage(violation.getPropertyPath().toString() + " " + violation.getMessage());
			}
			response = Response.status(Response.Status.BAD_REQUEST).entity(jsend).build();
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

	@Override
	public Response create(PersonaNatural personaNatural) {
		Response response;
		try {
			Set<ConstraintViolation<PersonaNatural>> violations = validator.validate(personaNatural);
			if (!violations.isEmpty()) {
				throw new ConstraintViolationException(new HashSet<ConstraintViolation<?>>(violations));
			}
			BigInteger idPersona = personaNaturalServiceTS.create(personaNatural);
			URI resource = new URI(baseUrl + "/" + idPersona.toString());
			response = Response.created(resource).entity(Jsend.getSuccessJSend(idPersona)).build();
		} catch (ConstraintViolationException e) {
			Jsend jsend = Jsend.getErrorJSend("datos invalidos");
			for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
				jsend.addMessage(violation.getPropertyPath().toString() + " " + violation.getMessage());
			}
			response = Response.status(Response.Status.BAD_REQUEST).entity(jsend).build();
		} catch (PreexistingEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.CONFLICT).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		} catch (URISyntaxException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response delete(BigInteger id) {
		Response response;
		try {
			personaNaturalServiceTS.delete(id);
			response = Response.status(Response.Status.NO_CONTENT).build();
		} catch (NonexistentEntityException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.NOT_FOUND).entity(jsend).build();
		} catch (RollbackFailureException e) {
			Jsend jsend = Jsend.getErrorJSend(e.getMessage());
			response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(jsend).build();
		}
		return response;
	}

	@Override
	public Response getFirma(String id, int flowChunkNumber, int flowChunkSize, int flowCurrentChunkSize, String flowFilename, String flowIdentifier, String flowRelativePath, int flowTotalChunks, int flowTotalSize) {
		if (flowFilename != null)
			return Response.status(Response.Status.NOT_FOUND).build();
		File file = new File(this.UPLOADED_FIRMA_PATH + id);
		if (!file.exists())
			file = new File(this.UPLOADED_FIRMA_PATH + "default.gif");
		ResponseBuilder response = Response.status(Response.Status.OK).entity((Object) file);
		response.header("Content-Disposition", "attachment; filename=image" + id + ".png");
		return response.build();
	}

	@Override
	public Response getFoto(String id, int flowChunkNumber, int flowChunkSize, int flowCurrentChunkSize, String flowFilename, String flowIdentifier, String flowRelativePath, int flowTotalChunks, int flowTotalSize) {
		if (flowFilename != null)
			return Response.status(Response.Status.NOT_FOUND).build();
		File file = new File(this.UPLOADED_FOTO_PATH + id);
		if (!file.exists())
			file = new File(this.UPLOADED_FOTO_PATH + "default.gif");
		ResponseBuilder response = Response.status(Response.Status.OK).entity((Object) file);
		response.header("Content-Disposition", "attachment; filename=image" + id + ".png");
		return response.build();
	}

	@Override
	public Response uploadFirma(BigInteger id, MultipartFormDataInput input) {
		String fileName = "";
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		fileName = id.toString();
		for (InputPart inputPart : inputParts) {
			try {
				// convert the uploaded file to inputstream
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path
				fileName = UPLOADED_FIRMA_PATH + fileName;
				writeFile(bytes, fileName);
			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
			}
		}
		return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();
	}

	@Override
	public Response uploadFoto(BigInteger id, MultipartFormDataInput input) {
		String fileName = "";
		Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
		List<InputPart> inputParts = uploadForm.get("file");
		fileName = id.toString();
		for (InputPart inputPart : inputParts) {
			try {
				InputStream inputStream = inputPart.getBody(InputStream.class, null);
				byte[] bytes = IOUtils.toByteArray(inputStream);
				// constructs upload file path
				fileName = UPLOADED_FOTO_PATH + fileName;
				writeFile(bytes, fileName);
			} catch (IOException e) {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Internal error").build();
			}
		}
		return Response.status(200).entity("uploadFile is called, Uploaded file name : " + fileName).build();
	}

	// save to somewhere
	private void writeFile(byte[] content, String filename) throws IOException {
		File file = new File(filename);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream fop = new FileOutputStream(file);
		fop.write(content);
		fop.flush();
		fop.close();
	}
}
