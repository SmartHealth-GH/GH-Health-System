package main.com.ug.optimizer.model.enums;

/**
 * Enum for service request status
 */
public enum RequestStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled"),
    ON_HOLD("On Hold");

    private final String displayName;

    RequestStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    public boolean isPending() {
        return this == PENDING || this == ON_HOLD;
    }

    public boolean isCompleted() {
        return this == COMPLETED;
    }

    public static RequestStatus fromString(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return RequestStatus.valueOf(text.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}