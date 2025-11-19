package com.darwinruiz.miniecommerce.controllers;


import com.darwinruiz.miniecommerce.services.CartService;
import com.darwinruiz.miniecommerce.services.OrderService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;

@Named
@SessionScoped
public class CartController implements Serializable {

    @Inject
    CartService cartService;
    @Inject
    OrderService orderService;

    public CartService getCart() {
        return cartService;
    }

    public void confirmOrder() {
        orderService.confirm(cartService.getItems());
        cartService.clear();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Pedido confirmado", "Gracias por su compra"));
    }
}