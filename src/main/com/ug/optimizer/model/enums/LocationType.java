package main.com.ug.optimizer.model.enums;

/**
 * Enum representing the type of hospital location
 * Using enums prevents inconsistent string values across the codebase

 */
public enum LocationType {
    EMERGENCY,        // Emergency Unit
    ICU,              // Intensive Care Unit
    LABORATORY,       // All labs (pathology, hematology, etc.)
    PHARMACY,         // Pharmacy and dispensary
    RADIOLOGY,        // X-ray, CT, MRI, ultrasound
    OPERATING_THEATRE,// Surgical suites
    WARD,             // General patient wards
    OPD,              // Outpatient Department
    ADMINISTRATION,   // Admin offices
    PARKING,          // Parking areas
    STAFF,            // Staff quarters, rest areas
    TRANSPORT,        // Ambulance bay, vehicle depot
    CLINIC,           // Specialty clinics (cardiology, neuro, etc.)
    BLOOD_BANK,       // Blood storage and transfusion
    RECORDS,          // Medical records
    OFFICE,           // General offices
    DIALYSIS,         // Dialysis unit
    ENDOSCOPY;        // Endoscopy unit

    /**
     * Helper method to convert string to enum (case-insensitive)
     */
    public static LocationType fromString(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }
        //part2
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