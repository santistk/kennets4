package com.santistk.servicioswebapi.controllers;

import com.santistk.servicioswebapi.models.Customer;
import com.santistk.servicioswebapi.services.CustomerService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    @Inject
    private CustomerService customerService;

    @GET
    public Response getAll() {
        return Response.ok(customerService.getAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Cliente no encontrado").build();
        }
        return Response.ok(customer.get()).build();
    }

    @GET
    @Path("/taxid/{taxId}")
    public Response getByTaxId(@PathParam("taxId") String taxId) {
        Optional<Customer> customer = customerService.findByTaxId(taxId);
        if (!customer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Cliente no encontrado").build();
        }
        return Response.ok(customer.get()).build();
    }

    @POST
    public Response create(Customer customer) {
        if (customer.getFullName() == null || customer.getFullName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El nombre es requerido").build();
        }
        if (customer.getTaxId() == null || customer.getTaxId().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El NIT es requerido").build();
        }
        if (customer.getEmail() == null || customer.getEmail().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El email es requerido").build();
        }

        Optional<Customer> existing = customerService.findByTaxId(customer.getTaxId());
        if (existing.isPresent()) {
            return Response.status(Response.Status.CONFLICT)
                .entity("Ya existe un cliente con ese NIT").build();
        }

        Optional<Customer> saved = customerService.save(customer);
        if (!saved.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error al crear el cliente").build();
        }
        return Response.status(Response.Status.CREATED).entity(saved.get()).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Customer customer) {
        Optional<Customer> existing = customerService.findById(id);
        if (!existing.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Cliente no encontrado").build();
        }

        Customer toUpdate = existing.get();
        toUpdate.setFullName(customer.getFullName());
        toUpdate.setEmail(customer.getEmail());
        toUpdate.setAddress(customer.getAddress());
        toUpdate.setActive(customer.getActive());

        Optional<Customer> updated = customerService.save(toUpdate);
        return Response.ok(updated.get()).build();
    }

    @PATCH
    @Path("/{id}/deactivate")
    public Response deactivate(@PathParam("id") Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Cliente no encontrado").build();
        }

        Customer toDeactivate = customer.get();
        toDeactivate.setActive(false);
        customerService.save(toDeactivate);

        return Response.ok().entity("Cliente desactivado").build();
    }
}
