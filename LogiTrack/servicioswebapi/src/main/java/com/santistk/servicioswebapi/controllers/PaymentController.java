package com.santistk.servicioswebapi.controllers;

import com.santistk.servicioswebapi.dtos.PaymentDto;
import com.santistk.servicioswebapi.models.Payment;
import com.santistk.servicioswebapi.services.PaymentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/payments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentController {

    @Inject
    private PaymentService paymentService;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Payment> payment = paymentService.findById(id);
        if (!payment.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Pago no encontrado").build();
        }
        return Response.ok(payment.get()).build();
    }

    @GET
    @Path("/order/{orderId}")
    public Response getByOrder(@PathParam("orderId") Long orderId) {
        return Response.ok(paymentService.findByOrderId(orderId)).build();
    }

    @POST
    public Response create(PaymentDto paymentDto) {
        if (paymentDto.getOrderId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El ID de la orden es requerido").build();
        }
        if (paymentDto.getAmount() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El monto es requerido").build();
        }
        if (paymentDto.getMethod() == null || paymentDto.getMethod().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El método de pago es requerido").build();
        }

        Optional<Payment> payment = paymentService.createPayment(paymentDto);
        if (!payment.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Error al crear el pago. Verifique que la orden exista y el monto no exceda el total pendiente").build();
        }

        return Response.status(Response.Status.CREATED).entity(payment.get()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Optional<Payment> payment = paymentService.findById(id);
        if (!payment.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Pago no encontrado").build();
        }

        paymentService.delete(payment.get());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
