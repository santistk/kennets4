package com.darwinruiz.miniecommerce.services;

import com.darwinruiz.miniecommerce.models.CartItem;
import com.darwinruiz.miniecommerce.models.Product;
import com.darwinruiz.miniecommerce.repositories.ProductRepository;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@SessionScoped
public class CartService implements Serializable {

    private final Map<Long, CartItem> items = new LinkedHashMap<>();

    @Inject
    ProductRepository repo;

    public Collection<CartItem> getItems() {
        return items.values();
    }

    public void add(Long productId) {
        Product p = repo.findById(productId).orElseThrow();
        if (p.getStock() <= 0) throw new IllegalStateException("Sin stock disponible");
        items.merge(productId, new CartItem(p, 1),
                (oldV, newV) -> {
                    oldV.setQuantity(oldV.getQuantity() + 1);
                    return oldV;
                });
    }

    public void remove(Long productId) {
        items.remove(productId);
    }

    public void changeQuantity(Long productId, int qty) {
        CartItem it = items.get(productId);
        if (it == null) return;
        if (qty <= 0) items.remove(productId);
        else it.setQuantity(qty);
    }

    public double subtotal() {
        return items.values().stream().mapToDouble(CartItem::getLineTotal).sum();
    }

    // Regla: 10% de descuento si subtotal > 500
    public double discount() {
        double sub = subtotal();
        return sub > 500 ? sub * 0.10 : 0.0;
    }

    public double total() {
        return subtotal() - discount();
    }

    public void clear() {
        items.clear();
    }
}
