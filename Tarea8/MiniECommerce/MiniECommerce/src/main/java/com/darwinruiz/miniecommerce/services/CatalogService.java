package com.darwinruiz.miniecommerce.services;

import com.darwinruiz.miniecommerce.models.Product;
import com.darwinruiz.miniecommerce.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CatalogService {

    @Inject
    ProductRepository repo;

    public List<Product> list() {
        return repo.findAll();
    }

    public List<Product> filterByName(String query) {
        if (query == null || query.isBlank()) return list();
        final String q = query.toLowerCase();
        return list().stream()
                .filter(p -> p.getName().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }
}