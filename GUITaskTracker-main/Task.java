package com.taskmanager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private String id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;
    private String priority;
    private String dueDate;

    // Constructor to initialize a new task
    public Task(String description, String status, String priority, String dueDate) {
        this.id = generateUniqueId();
        this.description = description;
        this.status = status;
        this.createdAt = getCurrentTime();
        this.updatedAt = getCurrentTime();
        this.priority = priority;
        this.dueDate = dueDate;
    }
    public Task(String id, String description, String status, String createdAt, String updatedAt, String priority, String dueDate) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.priority = priority;
        this.dueDate = dueDate;
    }


    // Generate a unique ID for the task
    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString().substring(0,4);
    }

    // Get the current time in ISO 8601 format
    public String getCurrentTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        return LocalDate.now().format(formatter);
    }

    // Getter and Setter methods
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = getCurrentTime();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.updatedAt = getCurrentTime();
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
    // Convert task to JSON format (String representation)
    public String toJson() {
        return String.format("{\"id\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\",\"priority\":\"%s\",\"dueDate\":\"%s\"}",
                id, description, status, createdAt, updatedAt, priority, dueDate);
    }

    @Override
    public String toString() {
        return String.format("Task id: %s, description: '%s', status: '%s', created: %s, updated: %s, priority: '%s', dueDate: '%s'",
                id, description, status, createdAt, updatedAt, priority, dueDate);
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
