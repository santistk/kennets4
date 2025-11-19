package com.darwinruiz.shoplite.services;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.repositories.IProductRepository;
import com.darwinruiz.shoplite.repositories.ProductRepository;
import java.util.List;

public class ProductService {
    private static ProductService instance;
    private final IProductRepository productRepository;

    private ProductService() {
        this.productRepository = ProductRepository.getInstance();
    }

    public static synchronized ProductService getInstance() {
        if (instance == null) {
            instance = new ProductService();
        }
        return instance;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAll(int page, int size) {
        return productRepository.findAll(page, size);
    }

    public Product findById(Integer id) {
        return productRepository.findById(id);
    }

    public void save(Product product) {
        productRepository.save(product);
    }

    public void update(Product product) {
        productRepository.update(product);
    }

    public void delete(Integer id) {
        productRepository.delete(id);
    }

    public int getTotalCount() {
        return productRepository.getTotalCount();
    }
}

