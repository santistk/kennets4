package com.darwinruiz.miniecommerce.repositories;

import com.darwinruiz.miniecommerce.models.Task;
import com.darwinruiz.miniecommerce.models.TaskPriority;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@ApplicationScoped
public class TaskRepository {
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @PostConstruct
    public void init() {
        // Datos semilla - 5-8 tareas distribuidas en los proyectos
        LocalDate today = LocalDate.now();
        
        // Tareas para proyecto 1
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 1L, "Diseñar base de datos", 
                TaskPriority.HIGH, today.plusDays(5), false, "Crear esquema ER"));
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 1L, "Implementar API REST", 
                TaskPriority.MEDIUM, today.plusDays(10), false, "Endpoints CRUD"));
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 1L, "Configurar servidor", 
                TaskPriority.LOW, today.minusDays(2), true, "Completado"));
        
        // Tareas para proyecto 2
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 2L, "Diseño UI/UX", 
                TaskPriority.HIGH, today.plusDays(3), false, "Wireframes"));
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 2L, "Desarrollo frontend", 
                TaskPriority.MEDIUM, today.plusDays(15), false, "React Native"));
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 2L, "Testing", 
                TaskPriority.MEDIUM, today.plusDays(20), false, null));
        
        // Tareas para proyecto 3
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 3L, "Análisis de datos legacy", 
                TaskPriority.HIGH, today.minusDays(5), false, "Revisar estructura"));
        tasks.put(idGenerator.get(), new Task(idGenerator.get(), 3L, "Script de migración", 
                TaskPriority.MEDIUM, today.plusDays(30), false, null));
    }

    public List<Task> findAll() {
        return new ArrayList<>(tasks.values());
    }

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public List<Task> findByProjectId(Long projectId) {
        return tasks.values().stream()
                .filter(t -> t.getProjectId().equals(projectId))
                .collect(Collectors.toList());
    }

    public List<Task> findByProjectIdAndPriority(Long projectId, TaskPriority priority) {
        return tasks.values().stream()
                .filter(t -> t.getProjectId().equals(projectId) &&
                           (priority == null || t.getPriority() == priority))
                .collect(Collectors.toList());
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idGenerator.getAndIncrement());
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public void delete(Long id) {
        tasks.remove(id);
    }

    public void deleteByProjectId(Long projectId) {
        tasks.values().removeIf(t -> t.getProjectId().equals(projectId));
    }
}

