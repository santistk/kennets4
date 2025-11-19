package com.darwinruiz.miniecommerce.repositories;

import com.darwinruiz.miniecommerce.models.Product;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ProductRepository {

    private final Map<Long, Product> data = new ConcurrentHashMap<>();
    private final AtomicLong seq = new AtomicLong(0);

    @PostConstruct
    void seed() {
        save(new Product(null, "Laptop Pro 14", "CPU 8-core, 16GB RAM, 512GB SSD", 1299.00, 7));
        save(new Product(null, "Monitor 27''", "QHD 144Hz, IPS", 289.00, 20));
        save(new Product(null, "Mouse Inalámbrico", "BT 5.0, silencio, 1600dpi", 19.90, 80));
        save(new Product(null, "Teclado Mecánico", "85%, switches brown, RGB", 84.50, 35));
    }

    public List<Product> findAll() {
        ArrayList<Product> list = new ArrayList<>(data.values());
        list.sort(Comparator.comparing(Product::getId));
        return list;
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }

    public Product save(Product p) {
        if (p.getId() == null) p.setId(seq.incrementAndGet());
        data.put(p.getId(), p);
        return p;
    }

    public void deleteById(Long id) {
        data.remove(id);
    }
}