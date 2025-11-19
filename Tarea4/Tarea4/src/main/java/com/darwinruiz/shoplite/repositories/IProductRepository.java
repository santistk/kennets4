package com.darwinruiz.shoplite.repositories;

import com.darwinruiz.shoplite.models.Product;
import java.util.List;

public interface IProductRepository {
    List<Product> findAll();
    List<Product> findAll(int page, int size);
    Product findById(Integer id);
    void save(Product product);
    void update(Product product);
    void delete(Integer id);
    int getTotalCount();
}

