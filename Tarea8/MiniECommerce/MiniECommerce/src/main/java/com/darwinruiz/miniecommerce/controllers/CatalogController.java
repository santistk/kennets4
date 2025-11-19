package com.darwinruiz.miniecommerce.controllers;


import com.darwinruiz.miniecommerce.models.Product;
import com.darwinruiz.miniecommerce.services.CartService;
import com.darwinruiz.miniecommerce.services.CatalogService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CatalogController implements Serializable {

    @Inject
    CatalogService catalogService;
    @Inject
    CartService cartService;

    private List<Product> products;
    private String query;

    @PostConstruct
    public void init() {
        products = catalogService.list();
    }

    public void search() {
        products = catalogService.filterByName(query);
    }

    public void addToCart(Long id) {
        cartService.add(id);
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Agregado", "Producto agregado al carrito"));
    }

    public List<Product> getProducts() {
        return products;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}