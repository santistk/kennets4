package com.santistk.servicioswebapi.repositories;

import com.santistk.servicioswebapi.models.Product;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ProductRepository extends BaseRepository<Product, Long> {
    
    @Override
    protected Class<Product> entity() {
        return Product.class;
    }

    public List<Product> findByCategory(String category) {
        return entityManager.createQuery(
            "SELECT p FROM Product p WHERE p.category = :category AND p.active = true", Product.class)
            .setParameter("category", category)
            .getResultList();
    }
}
