import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    private String id;
    private String description;
    private String status;
    private String createdAt;
    private String updatedAt;

    // Constructor to initialize a new task
    public Task(String description, String status) {
        this.id = generateUniqueId();
        this.description = description;
        this.status = status;
        this.createdAt = getCurrentTime();
        this.updatedAt = getCurrentTime();
    }
    public Task(String id, String description, String status, String createdAt, String updatedAt) {
        this.id = id;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    // Convert task to JSON format (String representation)
    public String toJson() {
        return String.format("{\"id\":\"%s\",\"description\":\"%s\",\"status\":\"%s\",\"createdAt\":\"%s\",\"updatedAt\":\"%s\"}",
                id, description, status, createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return String.format("id: %s, description: '%s', status: '%s', createdAt: %s, updatedAt: %s",
                id, description, status, createdAt, updatedAt);
    }
}
