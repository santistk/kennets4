package com.santistk.servicioswebapi.controllers;

import com.santistk.servicioswebapi.dtos.CreateOrderDto;
import com.santistk.servicioswebapi.dtos.OrderDto;
import com.santistk.servicioswebapi.models.Order;
import com.santistk.servicioswebapi.services.OrderService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderController {

    @Inject
    private OrderService orderService;

    @GET
    public Response getAll() {
        return Response.ok(orderService.getAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Order> order = orderService.findById(id);
        if (!order.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Orden no encontrada").build();
        }
        return Response.ok(order.get()).build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getByCustomer(@PathParam("customerId") Long customerId) {
        return Response.ok(orderService.findByCustomerId(customerId)).build();
    }

    @GET
    @Path("/status/{status}")
    public Response getByStatus(@PathParam("status") String status) {
        try {
            Order.OrderStatus orderStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            return Response.ok(orderService.findByStatus(orderStatus)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Estado inválido").build();
        }
    }

    @POST
    public Response create(CreateOrderDto createOrderDto) {
        if (createOrderDto.getCustomerId() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El ID del cliente es requerido").build();
        }
        if (createOrderDto.getItems() == null || createOrderDto.getItems().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("La orden debe tener al menos un item").build();
        }

        Optional<OrderDto> order = orderService.createOrder(createOrderDto);
        if (!order.isPresent()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Error al crear la orden. Verifique que el cliente y productos existan y estén activos").build();
        }

        return Response.status(Response.Status.CREATED).entity(order.get()).build();
    }

    @PATCH
    @Path("/{id}/status")
    public Response updateStatus(@PathParam("id") Long id, @QueryParam("status") String status) {
        try {
            Order.OrderStatus newStatus = Order.OrderStatus.valueOf(status.toUpperCase());
            Optional<Order> updated = orderService.updateOrderStatus(id, newStatus);
            
            if (!updated.isPresent()) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Orden no encontrada").build();
            }
            
            return Response.ok(updated.get()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Estado inválido").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Optional<Order> order = orderService.findById(id);
        if (!order.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Orden no encontrada").build();
        }

        orderService.delete(order.get());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
