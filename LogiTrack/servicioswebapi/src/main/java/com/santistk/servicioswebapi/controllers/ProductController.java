package com.santistk.servicioswebapi.controllers;

import com.santistk.servicioswebapi.models.Product;
import com.santistk.servicioswebapi.services.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Optional;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    private ProductService productService;

    @GET
    public Response getAll() {
        return Response.ok(productService.getAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Producto no encontrado").build();
        }
        return Response.ok(product.get()).build();
    }

    @GET
    @Path("/category/{category}")
    public Response getByCategory(@PathParam("category") String category) {
        return Response.ok(productService.findByCategory(category)).build();
    }

    @POST
    public Response create(Product product) {
        if (product.getName() == null || product.getName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El nombre del producto es requerido").build();
        }
        if (product.getPrice() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El precio es requerido").build();
        }

        Optional<Product> saved = productService.save(product);
        if (!saved.isPresent()) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error al crear el producto").build();
        }
        return Response.status(Response.Status.CREATED).entity(saved.get()).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, Product product) {
        Optional<Product> existing = productService.findById(id);
        if (!existing.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Producto no encontrado").build();
        }

        Product toUpdate = existing.get();
        toUpdate.setName(product.getName());
        toUpdate.setDescription(product.getDescription());
        toUpdate.setPrice(product.getPrice());
        toUpdate.setCategory(product.getCategory());
        toUpdate.setActive(product.getActive());

        Optional<Product> updated = productService.save(toUpdate);
        return Response.ok(updated.get()).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        Optional<Product> product = productService.findById(id);
        if (!product.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Producto no encontrado").build();
        }

        productService.delete(product.get());
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
