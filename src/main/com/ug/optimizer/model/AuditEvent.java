package main.com.ug.optimizer.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an audit event for tracking system actions
 * Used with the Stack data structure for undo operations
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class AuditEvent {
    private String action;
    private String details;
    private LocalDateTime timestamp;

    /**
     * Constructor with action and details
     */
    public AuditEvent(String action, String details) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        this.action = action.trim();
        this.details = details != null ? details.trim() : "";
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Constructor with action only
     */
    public AuditEvent(String action) {
        this(action, "");
    }

    // ===== Getters =====
    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // ===== Setters =====
    public void setAction(String action) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        this.action = action.trim();
    }

    public void setDetails(String details) {
        this.details = details != null ? details.trim() : "";
    }

    public void setTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        this.timestamp = timestamp;
    }

    // ===== Helper Methods =====
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s",
                getFormattedTimestamp(),
                action,
                details.isEmpty() ? "" : "- " + details);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditEvent that = (AuditEvent) o;
        return action.equals(that.action) &&
                timestamp.equals(that.timestamp);
    }

    @Override
    public int hashCode() {
        return action.hashCode() + timestamp.hashCode();
    }
}