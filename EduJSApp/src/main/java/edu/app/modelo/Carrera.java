package edu.app.modelo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase modelo que representa una Carrera Universitaria
 */
public class Carrera {
    private int id;
    private String nombre;
    private String codigo;
    private String facultad;
    private String nivel;
    private List<String> modalidades;
    private boolean estadoActiva;
    private String descripcion;

    // Constructor vacío
    public Carrera() {
        this.modalidades = new ArrayList<>();
    }

    // Constructor completo
    public Carrera(int id, String nombre, String codigo, String facultad, 
                   String nivel, List<String> modalidades, boolean estadoActiva, 
                   String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.codigo = codigo;
        this.facultad = facultad;
        this.nivel = nivel;
        this.modalidades = modalidades != null ? new ArrayList<>(modalidades) : new ArrayList<>();
        this.estadoActiva = estadoActiva;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public String getNivel() {
        return nivel;
    }

    public void setNivel(String nivel) {
        this.nivel = nivel;
    }

    public List<String> getModalidades() {
        return modalidades;
    }

    public void setModalidades(List<String> modalidades) {
        this.modalidades = modalidades != null ? new ArrayList<>(modalidades) : new ArrayList<>();
    }

    public boolean isEstadoActiva() {
        return estadoActiva;
    }

    public void setEstadoActiva(boolean estadoActiva) {
        this.estadoActiva = estadoActiva;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Carrera{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", facultad='" + facultad + '\'' +
                ", nivel='" + nivel + '\'' +
                ", modalidades=" + modalidades +
                ", estadoActiva=" + estadoActiva +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}

