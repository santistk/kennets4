package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.models.Product;
import com.darwinruiz.shoplite.repositories.ProductRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {

    private ProductRepository productRepository;

    @Override
    public void init() throws ServletException {
        productRepository = ProductRepository.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Obtener todos los productos desde ProductRepository
        List<Product> allProducts = productRepository.findAll();

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

        // Calcular el rango de productos para la página actual
        int total = allProducts.size();
        int start = (page - 1) * size;
        int end = Math.min(start + size, total);

        List<Product> items = allProducts.stream()
                .skip(start)
                .limit(size)
                .collect(Collectors.toList());

        // Enviar a la vista (home.jsp)
        request.setAttribute("items", items);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("total", total);

        request.getRequestDispatcher("/home.jsp").forward(request, response);
    }
}

