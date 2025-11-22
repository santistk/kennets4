package com.santistk.servicioswebapi.services;

import com.santistk.servicioswebapi.models.Product;
import com.santistk.servicioswebapi.repositories.ProductRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ProductService {
    
    @Inject
    private ProductRepository productRepository;

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.getAll();
    }

    public List<Product> findByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    public Optional<Product> save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {
        productRepository.delete(product);
    }
}
