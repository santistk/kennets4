package com.darwinruiz.miniecommerce.controllers;

import com.darwinruiz.miniecommerce.models.Project;
import com.darwinruiz.miniecommerce.models.ProjectStatus;
import com.darwinruiz.miniecommerce.services.ProjectService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Named
@ViewScoped
public class ProjectController implements Serializable {

    @Inject
    private ProjectService projectService;
    
    @Inject
    private TaskController taskController;

    private List<Project> projects;
    private Project selectedProject;
    private Project editingProject;
    private String searchQuery;
    private ProjectStatus filterStatus;
    private boolean showDialog;

    @PostConstruct
    public void init() {
        editingProject = new Project();
        loadProjects();
    }

    public void loadProjects() {
        projects = projectService.search(searchQuery, filterStatus);
    }

    public void search() {
        loadProjects();
    }

    public void clearFilters() {
        searchQuery = null;
        filterStatus = null;
        loadProjects();
    }

    public void newProject() {
        editingProject = new Project();
        editingProject.setStatus(ProjectStatus.ACTIVE);
        editingProject.setCreatedAt(LocalDateTime.now());
    }

    public void editProject(Project project) {
        editingProject = new Project();
        editingProject.setId(project.getId());
        editingProject.setName(project.getName());
        editingProject.setOwner(project.getOwner());
        editingProject.setStatus(project.getStatus());
        editingProject.setCreatedAt(project.getCreatedAt());
        editingProject.setDescription(project.getDescription());
    }

    public void saveProject() {
        try {
            projectService.save(editingProject);
            loadProjects();
            editingProject = new Project();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Proyecto guardado exitosamente"));
        } catch (IllegalArgumentException e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", e.getMessage()));
        }
    }

    public void deleteProject(Project project) {
        try {
            projectService.delete(project.getId());
            if (selectedProject != null && selectedProject.getId().equals(project.getId())) {
                selectedProject = null;
            }
            loadProjects();
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Éxito", "Proyecto eliminado"));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "No se pudo eliminar el proyecto"));
        }
    }

    public void selectProject(Project project) {
        selectedProject = project;
        if (taskController != null && project != null) {
            taskController.loadTasksForProject(project.getId());
        }
    }

    public void closeDialog() {
        showDialog = false;
        editingProject = new Project();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public Project getSelectedProject() {
        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public Project getEditingProject() {
        return editingProject;
    }

    public void setEditingProject(Project editingProject) {
        this.editingProject = editingProject;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }

    public ProjectStatus getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(ProjectStatus filterStatus) {
        this.filterStatus = filterStatus;
    }

    public boolean isShowDialog() {
        return showDialog;
    }

    public void setShowDialog(boolean showDialog) {
        this.showDialog = showDialog;
    }

    public ProjectStatus[] getStatuses() {
        return ProjectStatus.values();
    }
}

