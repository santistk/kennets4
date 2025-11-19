package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.models.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ProductRepository {
    private static ProductRepository instance;
    private List<Product> products;
    private AtomicInteger nextId;

    private ProductRepository() {
        products = new ArrayList<>();
        nextId = new AtomicInteger(1);
        // Productos de ejemplo
        products.add(new Product(nextId.getAndIncrement(), "Laptop", 999.99));
        products.add(new Product(nextId.getAndIncrement(), "Mouse", 29.99));
        products.add(new Product(nextId.getAndIncrement(), "Teclado", 79.99));
        products.add(new Product(nextId.getAndIncrement(), "Monitor", 299.99));
        products.add(new Product(nextId.getAndIncrement(), "Auriculares", 149.99));
    }

    public static synchronized ProductRepository getInstance() {
        if (instance == null) {
            instance = new ProductRepository();
        }
        return instance;
    }

    public List<Product> findAll() {
        return new ArrayList<>(products);
    }

    public int nextId() {
        return nextId.getAndIncrement();
    }

    public void save(Product product) {
        products.add(product);
    }

    public int getTotalCount() {
        return products.size();
    }
}

