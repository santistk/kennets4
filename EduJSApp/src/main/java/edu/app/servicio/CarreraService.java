package edu.app.servicio;

import edu.app.modelo.Carrera;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar carreras universitarias en memoria
 */
public class CarreraService {
    // Lista estática compartida de carreras
    private static final List<Carrera> carreras = new ArrayList<>();
    private static int contadorId = 1;

    // Inicialización con datos de ejemplo
    static {
        List<String> modalidades1 = new ArrayList<>();
        modalidades1.add("Presencial");
        modalidades1.add("Virtual");
        carreras.add(new Carrera(contadorId++, "Ingeniería en Sistemas", "SIS01", 
                "Ingeniería", "Licenciatura", modalidades1, true, 
                "Carrera enfocada en el desarrollo de software y sistemas informáticos"));

        List<String> modalidades2 = new ArrayList<>();
        modalidades2.add("Presencial");
        carreras.add(new Carrera(contadorId++, "Arquitectura", "ARQ02", 
                "Arquitectura y Diseño", "Licenciatura", modalidades2, true, 
                "Carrera de diseño arquitectónico y planificación urbana"));

        List<String> modalidades3 = new ArrayList<>();
        modalidades3.add("Presencial");
        modalidades3.add("Semipresencial");
        carreras.add(new Carrera(contadorId++, "Administración de Empresas", "ADE03", 
                "Ciencias Económicas", "Licenciatura", modalidades3, true, 
                "Carrera orientada a la gestión empresarial"));
    }

    /**
     * Obtiene todas las carreras
     */
    public List<Carrera> listarTodas() {
        return new ArrayList<>(carreras);
    }

    /**
     * Busca una carrera por ID
     */
    public Carrera buscarPorId(int id) {
        return carreras.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Crea una nueva carrera
     */
    public Carrera crear(Carrera carrera) {
        carrera.setId(contadorId++);
        carreras.add(carrera);
        return carrera;
    }

    /**
     * Actualiza una carrera existente
     */
    public boolean actualizar(Carrera carreraActualizada) {
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getId() == carreraActualizada.getId()) {
                carreras.set(i, carreraActualizada);
                return true;
            }
        }
        return false;
    }

    /**
     * Elimina una carrera por ID
     */
    public boolean eliminar(int id) {
        return carreras.removeIf(c -> c.getId() == id);
    }

    /**
     * Obtiene carreras activas
     */
    public List<Carrera> listarActivas() {
        return carreras.stream()
                .filter(Carrera::isEstadoActiva)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene carreras por facultad
     */
    public List<Carrera> listarPorFacultad(String facultad) {
        return carreras.stream()
                .filter(c -> c.getFacultad().equalsIgnoreCase(facultad))
                .collect(Collectors.toList());
    }
}

