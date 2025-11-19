package com.darwinruiz.miniecommerce.services;

import com.darwinruiz.miniecommerce.models.Task;
import com.darwinruiz.miniecommerce.models.TaskPriority;
import com.darwinruiz.miniecommerce.repositories.ProjectRepository;
import com.darwinruiz.miniecommerce.repositories.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class TaskService {

    @Inject
    TaskRepository taskRepository;

    @Inject
    ProjectRepository projectRepository;

    public List<Task> findByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    public List<Task> findByProjectIdAndPriority(Long projectId, TaskPriority priority) {
        return taskRepository.findByProjectIdAndPriority(projectId, priority);
    }

    public void validateDueDate(LocalDate dueDate) {
        if (dueDate != null && dueDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a hoy");
        }
    }

    public Task save(Task task) {
        // Validar que el proyecto existe
        if (task.getProjectId() == null || 
            projectRepository.findById(task.getProjectId()).isEmpty()) {
            throw new IllegalArgumentException("El proyecto especificado no existe");
        }
        
        // Validar fecha de vencimiento
        validateDueDate(task.getDueDate());
        
        return taskRepository.save(task);
    }

    public void toggleDone(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada"));
        task.setDone(!task.isDone());
        taskRepository.save(task);
    }

    public void delete(Long id) {
        taskRepository.delete(id);
    }

    public long countOpenTasks(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .filter(t -> !t.isDone())
                .count();
    }

    public long countCompletedTasks(Long projectId) {
        return taskRepository.findByProjectId(projectId).stream()
                .filter(Task::isDone)
                .count();
    }
}

