package com.darwinruiz.shoplite.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/app/users/*")
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);

        // Validar que exista una sesión activa
        if (session == null || session.getAttribute("auth") == null || 
            !Boolean.TRUE.equals(session.getAttribute("auth"))) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login.jsp");
            return;
        }

        // Revisar que el atributo role tenga el valor "ADMIN"
        String role = (String) session.getAttribute("role");
        if (role == null || !role.equals("ADMIN")) {
            RequestDispatcher dispatcher = httpRequest.getRequestDispatcher("/403.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Si cumple, permitir el acceso
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // No initialization needed
    }

    @Override
    public void destroy() {
        // No cleanup needed
    }
}

