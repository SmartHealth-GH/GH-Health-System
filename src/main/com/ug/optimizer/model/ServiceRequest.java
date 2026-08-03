package main.com.ug.optimizer.model;

import main.com.ug.optimizer.model.enums.RequestStatus;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a service request in the hospital
 * Maps to the 'service_requests' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class ServiceRequest {
    private int requestId;
    private int sourceId;
    private int destinationId;
    private String category;
    private UrgencyLevel urgency;
    private LocalDateTime timeSubmitted;
    private LocalDateTime deadline;
    private RequestStatus status;

    // Additional fields for convenience (denormalized)
    private Location source;
    private Location destination;

    /**
     * Default constructor
     */
    public ServiceRequest() {
        this.timeSubmitted = LocalDateTime.now();
        this.status = RequestStatus.PENDING;
    }

    /**
     * Full constructor with ID
     */
    public ServiceRequest(int requestId, int sourceId, int destinationId,
                          String category, UrgencyLevel urgency,
                          LocalDateTime timeSubmitted, LocalDateTime deadline,
                          RequestStatus status) {
        this.requestId = requestId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    /**
     * Constructor without ID
     */
    public ServiceRequest(int sourceId, int destinationId,
                          String category, UrgencyLevel urgency,
                          LocalDateTime deadline) {
        this(0, sourceId, destinationId, category, urgency,
                LocalDateTime.now(), deadline, RequestStatus.PENDING);
    }

    /**
     * Convenience constructor with urgency as int
     */
    public ServiceRequest(int sourceId, int destinationId,
                          String category, int urgency,
                          LocalDateTime deadline) {
        this(sourceId, destinationId, category,
                UrgencyLevel.fromValue(urgency), deadline);
    }

    // ===== Getters =====
    public int getRequestId() { return requestId; }
    public int getSourceId() { return sourceId; }
    public int getDestinationId() { return destinationId; }
    public String getCategory() { return category; }
    public UrgencyLevel getUrgency() { return urgency; }
    public LocalDateTime getTimeSubmitted() { return timeSubmitted; }
    public LocalDateTime getDeadline() { return deadline; }
    public RequestStatus getStatus() { return status; }
    public Location getSource() { return source; }
    public Location getDestination() { return destination; }

    // ===== Setters with Validation =====
    public void setRequestId(int requestId) {
        if (requestId < 0) {
            throw new IllegalArgumentException("Request ID cannot be negative");
        }
        this.requestId = requestId;
    }

    public void setSourceId(int sourceId) {
        if (sourceId <= 0) {
            throw new IllegalArgumentException("Source ID must be positive");
        }
        this.sourceId = sourceId;
    }

    public void setDestinationId(int destinationId) {
        if (destinationId <= 0) {
            throw new IllegalArgumentException("Destination ID must be positive");
        }
        this.destinationId = destinationId;
    }

    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        this.category = category.trim();
    }

    public void setUrgency(UrgencyLevel urgency) {
        if (urgency == null) {
            throw new IllegalArgumentException("Urgency cannot be null");
        }
        this.urgency = urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = UrgencyLevel.fromValue(urgency);
    }

    public void setTimeSubmitted(LocalDateTime timeSubmitted) {
        if (timeSubmitted == null) {
            throw new IllegalArgumentException("Time submitted cannot be null");
        }
        this.timeSubmitted = timeSubmitted;
    }

    public void setDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException("Deadline cannot be null");
        }
        if (deadline.isBefore(timeSubmitted)) {
            throw new IllegalArgumentException("Deadline cannot be before submission time");
        }
        this.deadline = deadline;
    }

    public void setStatus(RequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public void setStatus(String status) {
        RequestStatus enumStatus = RequestStatus.fromString(status);
        if (enumStatus == null) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = enumStatus;
    }

    public void setSource(Location source) { this.source = source; }
    public void setDestination(Location destination) { this.destination = destination; }

    // ===== Helper Methods =====
    public boolean isPending() {
        return status != null && status.isPending();
    }

    public boolean isCompleted() {
        return status != null && status.isCompleted();
    }

    public boolean isUrgent() {
        return urgency != null && urgency.isUrgent();
    }

    public boolean isEmergency() {
        return urgency != null && urgency.isEmergency();
    }

    public String getUrgencyLabel() {
        return urgency != null ? urgency.getDisplayName() : "Unknown";
    }

    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    /**
     * Check if request is overdue
     */
    public boolean isOverdue() {
        return !isCompleted() &&
                deadline != null &&
                LocalDateTime.now().isAfter(deadline);
    }

    @Override
    public String toString() {
        return String.format("ServiceRequest{id=%d, category='%s', urgency='%s', " +
                        "status='%s', overdue=%s}",
                requestId, category,
                urgency != null ? urgency.getDisplayName() : "Unknown",
                status != null ? status.getDisplayName() : "Unknown",
                isOverdue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRequest that = (ServiceRequest) o;
        return requestId == that.requestId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(requestId);
    }
}