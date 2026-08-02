package models;

import java.time.LocalDateTime;

/**
 * Represents a service request in the hospital
 */
public class ServiceRequest {
    private int id;
    private int sourceId;
    private int destinationId;
    private String category;
    private int urgency; // 1-5 (5 = most urgent)
    private LocalDateTime timeSubmitted;
    private LocalDateTime deadline;
    private String status; // Pending, In Progress, Completed

    // Constructors
    public ServiceRequest() {}

    public ServiceRequest(int id, int sourceId, int destinationId,
                          String category, int urgency,
                          LocalDateTime timeSubmitted,
                          LocalDateTime deadline, String status) {
        this.id = id;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSourceId() { return sourceId; }
    public void setSourceId(int sourceId) { this.sourceId = sourceId; }

    public int getDestinationId() { return destinationId; }
    public void setDestinationId(int destinationId) { this.destinationId = destinationId; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getUrgency() { return urgency; }
    public void setUrgency(int urgency) { this.urgency = urgency; }

    public LocalDateTime getTimeSubmitted() { return timeSubmitted; }
    public void setTimeSubmitted(LocalDateTime timeSubmitted) { this.timeSubmitted = timeSubmitted; }

    public LocalDateTime getDeadline() { return deadline; }
    public void setDeadline(LocalDateTime deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", urgency=" + urgency +
                ", status='" + status + '\'' +
                '}';
    }
}