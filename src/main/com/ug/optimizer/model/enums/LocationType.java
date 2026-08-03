package main.com.ug.optimizer.model.enums;

/**
 * Enum representing the type of hospital location
 * Using enums prevents inconsistent string values across the codebase
 */
public enum LocationType {
    EMERGENCY,
    ICU,
    LABORATORY,
    PHARMACY,
    RADIOLOGY,
    OPERATING_THEATRE,
    WARD,
    OPD,
    ADMINISTRATION,
    PARKING,
    STAFF,
    FACILITY,
    TRANSPORT,
    DIAGNOSTIC,
    CLINIC,
    CARDIOLOGY,
    NEUROLOGY,
    ORTHOPEDIC,
    PEDIATRICS,
    DERMATOLOGY,
    ENT,
    OPHTHALMOLOGY,
    UROLOGY,
    ENDOSCOPY,
    DIALYSIS,
    BLOOD_BANK,
    RECORDS,
    OFFICE;

    /**
     * Helper method to convert string to enum (case insensitive)
     */
    public static LocationType fromString(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        try {
            return LocationType.valueOf(text.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Get display name for UI
     */
    public String getDisplayName() {
        return this.name().replace("_", " ").toLowerCase();
    }
}