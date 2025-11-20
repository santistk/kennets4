package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.services.ProductService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/app/home")
public class HomeServlet extends HttpServlet {

    private ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = ProductService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Implementar parámetros page y size para paginación
        int page = 1;
        int size = 5;

        try {
            String pageParam = request.getParameter("page");
            if (pageParam != null && !pageParam.isEmpty()) {
                page = Integer.parseInt(pageParam);
            }
        } catch (NumberFormatException e) {
            page = 1;
        }

        try {
            String sizeParam = request.getParameter("size");
            if (sizeParam != null && !sizeParam.isEmpty()) {
                size = Integer.parseInt(sizeParam);
            }
        } catch (NumberFormatException e) {
            size = 5;
        }

        // Obtener productos paginados desde ProductService
        List<Product> items = productService.findAll(page, size);
        int total = productService.getTotalCount();

        // Calcular total de páginas
        int totalPages = (int) Math.ceil((double) total / size);

        // Enviar a la vista (home.jsp)
        request.setAttribute("items", items);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("total", total);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}

