package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.repositories.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        productRepository = ProductRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Leer name y price desde el formulario
        String name = request.getParameter("name");
        String priceParam = request.getParameter("price");

        // Validar que el nombre no esté vacío y el precio sea mayor a 0
        if (name == null || name.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/admin?err=1");
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceParam);
            if (price <= 0) {
                response.sendRedirect(request.getContextPath() + "/admin?err=1");
                return;
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin?err=1");
            return;
        }

        // Usar repo.nextId() para generar el nuevo ID y guardar el producto
        int id = productRepository.nextId();
        Product product = new Product(id, name.trim(), price);
        productRepository.save(product);

        // Si es válido, redirigir a /home
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

