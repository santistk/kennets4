package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = ProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Leer name, price y stock desde el formulario
        String name = request.getParameter("name");
        String priceParam = request.getParameter("price");
        String stockParam = request.getParameter("stock");

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

        int stock = 0;
        try {
            if (stockParam != null && !stockParam.isEmpty()) {
                stock = Integer.parseInt(stockParam);
                if (stock < 0) {
                    response.sendRedirect(request.getContextPath() + "/admin?err=1");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin?err=1");
            return;
        }

        // Crear y guardar el producto (el ID se genera automáticamente por la BD)
        Product product = new Product(name.trim(), price, stock);
        productService.save(product);

        // Si es válido, redirigir a /home
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

