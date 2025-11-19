package com.darwinruiz.shoplite.controllers;

import com.darwinruiz.shoplite.repositories.UserRepository;
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

    private UserRepository userRepository;

    @Override
    public void init() throws ServletException {
        userRepository = UserRepository.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Verificar las credenciales usando UserRepository
        if (!userRepository.validateCredentials(email, password)) {
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
        User user = userRepository.getUserByEmail(email);
        
        session.setAttribute("auth", true);
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("role", user.getRole());

        // Configurar maxInactiveInterval a 30 minutos
        session.setMaxInactiveInterval(30 * 60);

        // Redirigir a /home
        response.sendRedirect(request.getContextPath() + "/home");
    }
}

