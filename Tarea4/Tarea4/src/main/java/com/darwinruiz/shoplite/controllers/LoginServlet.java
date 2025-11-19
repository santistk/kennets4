package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.services.UserService;
import com.darwinruiz.shoplite.models.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/auth/login")
public class LoginServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        userService = UserService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Verificar las credenciales usando UserService
        if (!userService.validateCredentials(username, password)) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?err=1");
            return;
        }

        // Si son válidas
        // Invalidar la sesión anterior
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        // Crear una nueva sesión y asignar atributos
        HttpSession session = request.getSession(true);
        User user = userService.getUserByUsername(username);
        
        session.setAttribute("auth", true);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());

        // Configurar maxInactiveInterval a 30 minutos
        session.setMaxInactiveInterval(30 * 60);

        // Redirigir a /home
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

