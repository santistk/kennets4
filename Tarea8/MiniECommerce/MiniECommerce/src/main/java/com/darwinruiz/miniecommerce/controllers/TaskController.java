package com.darwinruiz.miniecommerce.controllers;

import com.darwinruiz.miniecommerce.models.Project;
import com.darwinruiz.miniecommerce.models.Task;
import com.darwinruiz.miniecommerce.models.TaskPriority;
import com.darwinruiz.miniecommerce.services.ProjectService;
import com.darwinruiz.miniecommerce.services.TaskService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Named
@ViewScoped
public class TaskController implements Serializable {

    @Inject
    private TaskService taskService;

    @Inject
    private ProjectService projectService;

    private Long currentProjectId;
    private Project currentProject;
    private List<Task> tasks;
    private Task editingTask;
    private TaskPriority filterPriority;
    private boolean showDialog;
    private long openTasksCount;
    private long completedTasksCount;

    @PostConstruct
    public void init() {
        editingTask = new Task();
        tasks = List.of();
    }

    public void loadTasksForProject(Long projectId) {
        this.currentProjectId = projectId;
        this.currentProject = projectService.findById(projectId);
        loadTasks();
        updateCounts();
    }

    public void loadTasks() {
        if (currentProjectId == null) {
            tasks = List.of();
            return;
        }
        tasks = taskService.findByProjectIdAndPriority(currentProjectId, filterPriority);
    }

    public void updateCounts() {
        if (currentProjectId == null) {
            openTasksCount = 0;
            completedTasksCount = 0;
            return;
        }
        openTasksCount = taskService.countOpenTasks(currentProjectId);
        completedTasksCount = taskService.countCompletedTasks(currentProjectId);
    }

    public void filterByPriority() {
        loadTasks();
    }

    public void newTask() {
        editingTask = new Task();
        editingTask.setProjectId(currentProjectId);
        editingTask.setPriority(TaskPriority.MEDIUM);
        editingTask.setDueDate(LocalDate.now());
        editingTask.setDone(false);
    }

    public void editTask(Task task) {
        editingTask = new Task();
        editingTask.setId(task.getId());
        editingTask.setProjectId(task.getProjectId());
        editingTask.setTitle(task.getTitle());
        editingTask.setPriority(task.getPriority());
        editingTask.setDueDate(task.getDueDate());
        editingTask.setDone(task.isDone());
        editingTask.setNotes(task.getNotes());
    }

    public void saveTask() {
        try {
            taskService.save(editingTask);
            loadTasks();
            updateCounts();
            editingTask = new Task();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarea guardada exitosamente"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void toggleDone(Task task) {
        try {
            taskService.toggleDone(task.getId());
            loadTasks();
            updateCounts();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Estado de tarea actualizado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo actualizar la tarea"));
        }
    }

    public void deleteTask(Task task) {
        try {
            taskService.delete(task.getId());
            loadTasks();
            updateCounts();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Tarea eliminada"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar la tarea"));
        }
    }

    public boolean isOverdue(Task task) {
        return !task.isDone() && task.getDueDate() != null && task.getDueDate().isBefore(LocalDate.now());
    }

    public void closeDialog() {
        showDialog = false;
        editingTask = new Task();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getEditingTask() {
        return editingTask;
    }

    public void setEditingTask(Task editingTask) {
        this.editingTask = editingTask;
    }

    public Long getCurrentProjectId() {
        return currentProjectId;
    }

    public void setCurrentProjectId(Long currentProjectId) {
        this.currentProjectId = currentProjectId;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public TaskPriority getFilterPriority() {
        return filterPriority;
    }

    public void setFilterPriority(TaskPriority filterPriority) {
        this.filterPriority = filterPriority;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public TaskPriority[] getPriorities() {
        return TaskPriority.values();
    }

    public long getOpenTasksCount() {
        return openTasksCount;
    }

    public long getCompletedTasksCount() {
        return completedTasksCount;
    }
}

