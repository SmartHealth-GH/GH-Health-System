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

    private final int value; //numeric value(1-5) stored in the database
    private final String displayName; //user-friendly name for ui display

    //constructor
    UrgencyLevel(int value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }
    //getters
    public int getValue() { return value; }
    public String getDisplayName() { return displayName; }
    //boolean
    public boolean isUrgent() {
        return this == URGENT || this == EMERGENCY;
    }

    public boolean isEmergency() {
        return this == EMERGENCY;
    }

    //convert value to Enum
    public static UrgencyLevel fromValue(int value) {
        for (UrgencyLevel level : values()) {
            if (level.value == value) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid urgency value: " + value);
    }
}