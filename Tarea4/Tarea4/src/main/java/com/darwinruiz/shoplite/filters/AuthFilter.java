package com.darwinruiz.shoplite.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());

        // Verificar si la URI corresponde a una página pública
        if (isPublicPage(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Si es una página privada, validar sesión
        HttpSession session = httpRequest.getSession(false);
        if (session == null || session.getAttribute("auth") == null || 
            !Boolean.TRUE.equals(session.getAttribute("auth"))) {
            httpResponse.sendRedirect(contextPath + "/login.jsp");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublicPage(String path) {
        return path.equals("/") || 
               path.equals("/index.jsp") || 
               path.equals("/login.jsp") || 
               path.equals("/auth/login") ||
               path.startsWith("/auth/login") ||
               path.equals("/auth/logout") ||
               path.startsWith("/auth/logout");
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

