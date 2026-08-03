package main.com.ug.optimizer.model.enums;

/**
 * Enum for resource availability status
 */
public enum ResourceStatus {
    AVAILABLE("Available"),
    IN_USE("In Use"),
    OUT_OF_SERVICE("Out of Service"),
    RESERVED("Reserved"),
    UNDER_MAINTENANCE("Under Maintenance");

    private final String displayName;

    ResourceStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() { return displayName; }

    public boolean isAvailable() {
        return this == AVAILABLE;
    }

    public static ResourceStatus fromString(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return ResourceStatus.valueOf(text.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}