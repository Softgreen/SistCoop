/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sistemafinanciero.rest;

import java.math.BigInteger;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/bovedas")
public interface BovedaREST {

	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findById(@PathParam("id") BigInteger id);

	@GET
	@Produces({ "application/xml", "application/json" })
	public Response listAll(@QueryParam("idAgencia") BigInteger idAgencia);

	@GET
	@Path("/{id}/detalle")
	@Produces({ "application/xml", "application/json" })
	public Response getDetalleBoveda(@PathParam("id") BigInteger id, @QueryParam("idHistorial") BigInteger idHistorial);

	@PUT
	@Path("/{id}")
	@Produces({ "application/xml", "application/json" })
	public Response update(@PathParam("id") BigInteger id, @FormParam("denominacion") String denominacion);

	@POST
	@Produces({ "application/xml", "application/json" })
	public Response create(@Context SecurityContext context, @FormParam("idMoneda") BigInteger idMoneda, @FormParam("denominacion") String denominacion);

	@POST
	@Path("/{id}/abrir")
	@Produces({ "application/xml", "application/json" })
	public Response abrir(@PathParam("id") BigInteger id);
	
	@POST
	@Path("/{id}/congelar")
	@Produces({ "application/xml", "application/json" })
	public Response congelar(@PathParam("id") BigInteger id);
	
	@POST
	@Path("/{id}/descongelar")
	@Produces({ "application/xml", "application/json" })
	public Response descongelar(@PathParam("id") BigInteger id);
	
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") BigInteger id);

}
