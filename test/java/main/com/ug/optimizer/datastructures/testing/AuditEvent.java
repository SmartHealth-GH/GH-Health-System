package main.com.ug.optimizer.model; // This file belongs to the 'model' package

import java.time.LocalDateTime; // For date/time handling
import java.time.format.DateTimeFormatter; // For formatting dates in a readable way

/**
 * Represents an audit event for tracking system actions
 * Used with the Stack data structure for undo operations
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class AuditEvent {

    // =============================================
    // FIELDS - These are the data each AuditEvent stores
    // =============================================

    private String action;           // What action was performed? (e.g., "ADD_PATIENT")
    private String details;          // Additional information about the action
    private LocalDateTime timestamp; // When did this action happen?

    // =============================================
    // CONSTRUCTORS - Ways to create an AuditEvent
    // =============================================

    /**
     * Constructor with action and details
     * Creates an audit event with the current timestamp
     */
    public AuditEvent(String action, String details) {
        // Validate: action cannot be null or empty
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        this.action = action.trim(); // Remove extra spaces
        this.details = details != null ? details.trim() : ""; // Convert null to empty string
        this.timestamp = LocalDateTime.now(); // Set to current time automatically
    }

    /**
     * Constructor with action only (no details)
     * Calls the full constructor with empty details
     */
    public AuditEvent(String action) {
        this(action, ""); // Chain to the main constructor
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public String getAction() { return action; }
    public String getDetails() { return details; }
    public LocalDateTime getTimestamp() { return timestamp; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // =============================================

    /**
     * Sets the action
     * Validates: Action cannot be null or empty
     */
    public void setAction(String action) {
        if (action == null || action.isBlank()) {
            throw new IllegalArgumentException("Action cannot be null or empty");
        }
        this.action = action.trim();
    }

    /**
     * Sets the details
     * Converts null to empty string, removes extra spaces
     */
    public void setDetails(String details) {
        this.details = details != null ? details.trim() : "";
    }

    /**
     * Sets the timestamp
     * Validates: Timestamp cannot be null
     */
    public void setTimestamp(LocalDateTime timestamp) {
        if (timestamp == null) {
            throw new IllegalArgumentException("Timestamp cannot be null");
        }
        this.timestamp = timestamp;
    }

    // =============================================
    // HELPER METHODS - Extra useful functions
    // =============================================

    /**
     * Returns a formatted timestamp string
     * Example: "2026-08-13 14:30:45"
     * This is more readable than the default LocalDateTime format
     */
    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // =============================================
    // STANDARD METHODS - toString, equals, hashCode
    // =============================================

    /**
     * Returns a readable string representation of the AuditEvent
     * Example: "[2026-08-13 14:30:45] ADD_PATIENT - Patient John added"
     * Useful for displaying audit logs in the console
     */
    @Override
    public String toString() {
        return String.format("[%s] %s %s",
                getFormattedTimestamp(),
                action,
                details.isEmpty() ? "" : "- " + details);
    }

    /**
     * Compares two AuditEvent objects for equality
     * Two AuditEvents are equal if they have the SAME action AND timestamp
     * (Since events are unique, this is a reasonable comparison)
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuditEvent that = (AuditEvent) o;
        return action.equals(that.action) &&
                timestamp.equals(that.timestamp);
    }

    /**
     * Returns a hash code for the AuditEvent
     * Required when overriding equals()
     * Used in collections like HashMap and HashSet
     */
    @Override
    public int hashCode() {
        return action.hashCode() + timestamp.hashCode();
    }
}