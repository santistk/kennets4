package edu.app.controlador;

import edu.app.modelo.Carrera;
import edu.app.servicio.CarreraService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet controlador para gestionar las operaciones CRUD de Carreras
 */
public class CarreraControlador extends HttpServlet {
    
    private CarreraService carreraService;

    @Override
    public void init() throws ServletException {
        super.init();
        carreraService = new CarreraService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if (accion == null || accion.isEmpty()) {
            accion = "listar";
        }

        switch (accion) {
            case "nuevo":
                mostrarFormulario(request, response, null);
                break;
            case "editar":
                editar(request, response);
                break;
            case "eliminar":
                eliminar(request, response);
                break;
            case "listar":
            default:
                listar(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");
        
        if ("guardar".equals(accion)) {
            guardar(request, response);
        } else if ("actualizar".equals(accion)) {
            actualizar(request, response);
        } else {
            listar(request, response);
        }
    }

    private void listar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Carrera> carreras = carreraService.listarTodas();
        request.setAttribute("carreras", carreras);
        request.getRequestDispatcher("/WEB-INF/vistas/carreras/listado-wrapper.jsp")
                .forward(request, response);
    }

    private void mostrarFormulario(HttpServletRequest request, HttpServletResponse response, Carrera carrera)
            throws ServletException, IOException {
        if (carrera != null) {
            request.setAttribute("carrera", carrera);
        }
        request.getRequestDispatcher("/WEB-INF/vistas/carreras/formulario-wrapper.jsp")
                .forward(request, response);
    }

    private void editar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Carrera carrera = carreraService.buscarPorId(id);
                if (carrera != null) {
                    mostrarFormulario(request, response, carrera);
                } else {
                    response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
        }
    }

    private void guardar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Carrera carrera = obtenerCarreraDesdeRequest(request);
        carreraService.crear(carrera);
        response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
    }

    private void actualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Carrera carrera = obtenerCarreraDesdeRequest(request);
                carrera.setId(id);
                carreraService.actualizar(carrera);
                response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
        }
    }

    private void eliminar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idStr = request.getParameter("id");
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                carreraService.eliminar(id);
            } catch (NumberFormatException e) {
                // Ignorar error
            }
        }
        response.sendRedirect(request.getContextPath() + "/carreras?accion=listar");
    }

    private Carrera obtenerCarreraDesdeRequest(HttpServletRequest request) {
        Carrera carrera = new Carrera();
        
        carrera.setNombre(request.getParameter("nombre") != null ? 
                request.getParameter("nombre").trim() : "");
        carrera.setCodigo(request.getParameter("codigo") != null ? 
                request.getParameter("codigo").trim() : "");
        carrera.setFacultad(request.getParameter("facultad") != null ? 
                request.getParameter("facultad").trim() : "");
        carrera.setNivel(request.getParameter("nivel") != null ? 
                request.getParameter("nivel").trim() : "");
        carrera.setDescripcion(request.getParameter("descripcion") != null ? 
                request.getParameter("descripcion").trim() : "");
        
        // Procesar modalidades (pueden venir múltiples valores)
        String[] modalidadesArray = request.getParameterValues("modalidades");
        List<String> modalidades = new ArrayList<>();
        if (modalidadesArray != null) {
            modalidades = new ArrayList<>(Arrays.asList(modalidadesArray));
        }
        carrera.setModalidades(modalidades);
        
        // Procesar estado activa (checkbox)
        String estadoActivaStr = request.getParameter("estadoActiva");
        carrera.setEstadoActiva("on".equals(estadoActivaStr) || "true".equals(estadoActivaStr));
        
        return carrera;
    }
}

