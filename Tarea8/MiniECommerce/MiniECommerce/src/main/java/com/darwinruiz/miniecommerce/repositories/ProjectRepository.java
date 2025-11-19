package com.darwinruiz.miniecommerce.repositories;

import com.darwinruiz.miniecommerce.models.Project;
import com.darwinruiz.miniecommerce.models.ProjectStatus;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class ProjectRepository {
    private final Map<Long, Project> projects = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Datos semilla
        LocalDateTime now = LocalDateTime.now();
        
        Project p1 = new Project(idGenerator.getAndIncrement(), "Sistema de Gestión", "Juan Pérez", 
                ProjectStatus.ACTIVE, now.minusDays(10), "Sistema para gestionar inventarios");
        projects.put(p1.getId(), p1);
        
        Project p2 = new Project(idGenerator.getAndIncrement(), "App Móvil", "María García", 
                ProjectStatus.ACTIVE, now.minusDays(5), "Aplicación móvil para clientes");
        projects.put(p2.getId(), p2);
        
        Project p3 = new Project(idGenerator.getAndIncrement(), "Migración de Datos", "Carlos López", 
                ProjectStatus.ON_HOLD, now.minusDays(20), "Migración de base de datos legacy");
        projects.put(p3.getId(), p3);
    }

    public List<Project> findAll() {
        return new ArrayList<>(projects.values());
    }

    public Optional<Project> findById(Long id) {
        return Optional.ofNullable(projects.get(id));
    }

    public List<Project> findByNameOrOwner(String query) {
        if (query == null || query.trim().isEmpty()) {
            return findAll();
        }
        String lowerQuery = query.toLowerCase();
        return projects.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerQuery) ||
                           p.getOwner().toLowerCase().contains(lowerQuery))
                .collect(Collectors.toList());
    }

    public List<Project> findByStatus(ProjectStatus status) {
        if (status == null) {
            return findAll();
        }
        return projects.values().stream()
                .filter(p -> p.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Project> findByNameOrOwnerAndStatus(String query, ProjectStatus status) {
        List<Project> result = projects.values().stream()
                .filter(p -> {
                    boolean matchesQuery = true;
                    if (query != null && !query.trim().isEmpty()) {
                        String lowerQuery = query.toLowerCase();
                        matchesQuery = p.getName().toLowerCase().contains(lowerQuery) ||
                                     p.getOwner().toLowerCase().contains(lowerQuery);
                    }
                    boolean matchesStatus = status == null || p.getStatus() == status;
                    return matchesQuery && matchesStatus;
                })
                .collect(Collectors.toList());
        return result;
    }

    public boolean existsByName(String name, Long excludeId) {
        return projects.values().stream()
                .anyMatch(p -> p.getName().equalsIgnoreCase(name) && 
                              (excludeId == null || !p.getId().equals(excludeId)));
    }

    public Project save(Project project) {
        if (project.getId() == null) {
            project.setId(idGenerator.getAndIncrement());
            project.setCreatedAt(LocalDateTime.now());
        }
        projects.put(project.getId(), project);
        return project;
    }

    public void delete(Long id) {
        projects.remove(id);
    }
}

