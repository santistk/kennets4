package com.darwinruiz.miniecommerce.services;

import com.darwinruiz.miniecommerce.models.Project;
import com.darwinruiz.miniecommerce.models.ProjectStatus;
import com.darwinruiz.miniecommerce.repositories.ProjectRepository;
import com.darwinruiz.miniecommerce.repositories.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ProjectService {

    @Inject
    ProjectRepository projectRepository;
    
    @Inject
    TaskRepository taskRepository;

    public List<Project> findAll() {
        return projectRepository.findAll();
    }

    public Project findById(Long id) {
        return projectRepository.findById(id).orElse(null);
    }

    public List<Project> search(String query, ProjectStatus status) {
        if ((query == null || query.trim().isEmpty()) && status == null) {
            return findAll();
        }
        return projectRepository.findByNameOrOwnerAndStatus(query, status);
    }

    public void validateUniqueName(String name, Long excludeId) {
        if (projectRepository.existsByName(name, excludeId)) {
            throw new IllegalArgumentException("Ya existe un proyecto con el nombre: " + name);
        }
    }

    public Project save(Project project) {
        // Validar nombre único (excepto si es el mismo proyecto en edición)
        validateUniqueName(project.getName(), project.getId());
        return projectRepository.save(project);
    }

    public void delete(Long id) {
        // Eliminar todas las tareas asociadas al proyecto
        List<com.darwinruiz.miniecommerce.models.Task> tasks = taskRepository.findByProjectId(id);
        for (com.darwinruiz.miniecommerce.models.Task task : tasks) {
            taskRepository.delete(task.getId());
        }
        // Eliminar el proyecto
        projectRepository.delete(id);
    }
}

