package com.darwinruiz.miniecommerce.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Project implements Serializable {
    private Long id;
    private String name;
    private String owner;
    private ProjectStatus status;
    private LocalDateTime createdAt;
    private String description;

    public Project() {
    }

    public Project(Long id, String name, String owner, ProjectStatus status, LocalDateTime createdAt, String description) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.status = status;
        this.createdAt = createdAt;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        return Objects.equals(id, ((Project) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

