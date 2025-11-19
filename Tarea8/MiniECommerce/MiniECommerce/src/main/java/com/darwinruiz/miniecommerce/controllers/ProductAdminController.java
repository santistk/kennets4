package com.darwinruiz.miniecommerce.controllers;


import com.darwinruiz.miniecommerce.models.Product;
import com.darwinruiz.miniecommerce.repositories.ProductRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProductAdminController implements Serializable {

    @Inject
    ProductRepository repo;

    private List<Product> products;
    private Product selected;
    private boolean editMode;

    @PostConstruct
    public void init() {
        products = repo.findAll();
        selected = new Product();
        editMode = false;
    }

    public void prepareCreate() {
        selected = new Product();
        editMode = false;
    }

    public void prepareEdit(Product p) {
        // Copia simple para no mutar directo
        selected = new Product(p.getId(), p.getName(), p.getDescription(), p.getPrice(), p.getStock());
        editMode = true;
    }

    public void save() {
        repo.save(selected);
        products = repo.findAll();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, editMode ? "Producto actualizado" : "Producto creado",
                        selected.getName()));
    }

    public void delete(Product p) {
        repo.deleteById(p.getId());
        products = repo.findAll();
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Producto eliminado", p.getName()));
    }

    public List<Product> getProducts() {
        return products;
    }

    public Product getSelected() {
        return selected;
    }

    public boolean isEditMode() {
        return editMode;
    }
}
