package com.darwinruiz.miniecommerce.services;

import com.darwinruiz.miniecommerce.models.CartItem;
import com.darwinruiz.miniecommerce.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Collection;

@ApplicationScoped
public class OrderService {

    @Inject
    ProductRepository repo;

    // Al confirmar descuenta stock
    public void confirm(Collection<CartItem> items) {
        items.forEach(it -> repo.findById(it.getProduct().getId()).ifPresent(p -> {
            int newStock = p.getStock() - it.getQuantity();
            if (newStock < 0) throw new IllegalStateException("Stock insuficiente");
            p.setStock(newStock);
            repo.save(p);
        }));
    }
}