package main.com.ug.optimizer.model.enums;

/**
 * Enum for urgency levels (1-5)
 * Prevents magic numbers and provides semantic meaning
 */
public enum UrgencyLevel {
    VERY_LOW(1, "Very Low"),
    LOW(2, "Low"),
    MODERATE(3, "Moderate"),
    URGENT(4, "Urgent"),
    EMERGENCY(5, "Emergency");

    private final int value;
    private final String displayName;

    UrgencyLevel(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public int getValue() { return value; }
    public String getDisplayName() { return displayName; }

    public boolean isUrgent() {
        return this == URGENT || this == EMERGENCY;
    }

    public boolean isEmergency() {
        return this == EMERGENCY;
    }

    public static UrgencyLevel fromValue(int value) {
        for (UrgencyLevel level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid urgency value: " + value);
    }
}