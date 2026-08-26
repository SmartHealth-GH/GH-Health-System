package main.com.ug.optimizer.model; // This file belongs to the 'model' package

import main.com.ug.optimizer.model.enums.LocationType; // Bring in the LocationType enum (EMERGENCY, ICU, etc.)

/**
 * Represents a location in the hospital system
 * e.g., Emergency Unit, Pharmacy, Operating Room
 *
 * Maps to the 'locations' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Location {

    // =============================================
    // FIELDS - These are the data each Location stores
    // =============================================

    private int locationId;      // Unique ID number (like a staff ID)
    private String name;         // Name of the place (e.g., "Emergency Unit")
    private String area;         // Area/region (e.g., "Korle-Bu")
    private LocationType type;   // What kind of place? (ENUM: EMERGENCY, ICU, etc.)
    private double latitude;     // GPS coordinate (up/down position)
    private double longitude;    // GPS coordinate (left/right position)

    // =============================================
    // CONSTRUCTORS - Ways to create a Location
    // =============================================

    /**
     * Default constructor - creates an empty Location
     * Values will be filled in later (e.g., when loading from database)
     */
    public Location() {
    }

    /**
     * Full constructor - creates a Location with ALL values provided
     * Uses setters so validation runs (ensures data is valid)
     */
    public Location(int locationId, String name, String area, LocationType type,
                    double latitude, double longitude) {
        this.locationId = locationId;
        setName(name);          // Validates name is not empty
        setArea(area);          // Validates area is not empty
        setType(type);          // Validates type is not null
        setLatitude(latitude);  // Validates latitude is within -90 to 90
        setLongitude(longitude);// Validates longitude is within -180 to 180
    }

    /**
     * Constructor without ID - for NEW records
     * Database will auto-generate the ID when saved
     * Calls the full constructor with ID = 0 (placeholder)
     */
    public Location(String name, String area, LocationType type,
                    double latitude, double longitude) {
        this(0, name, area, type, latitude, longitude);
    }

    /**
     * Convenience constructor - accepts type as a STRING
     * Converts string to enum automatically
     * Useful for CSV loading where data comes as text
     */
    public Location(String name, String area, String type,
                    double latitude, double longitude) {
        this(name, area, LocationType.fromString(type), latitude, longitude);
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public int getLocationId() { return locationId; }
    public String getName() { return name; }
    public String getArea() { return area; }
    public LocationType getType() { return type; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // Each setter includes VALIDATION to prevent bad data
    // =============================================

    /**
     * Sets the location ID
     * Validates: ID cannot be negative
     */
    public void setLocationId(int locationId) {
        if (locationId < 0) {
            throw new IllegalArgumentException("Location ID cannot be negative");
        }
        this.locationId = locationId;
    }

    /**
     * Sets the location name
     * Validates: Name cannot be null or empty
     * Also removes extra spaces with .trim()
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Location name cannot be null or empty");
        }
        this.name = name.trim();
    }

    /**
     * Sets the area
     * Validates: Area cannot be null or empty
     */
    public void setArea(String area) {
        if (area == null || area.isBlank()) {
            throw new IllegalArgumentException("Area cannot be null or empty");
        }
        this.area = area.trim();
    }

    /**
     * Sets the type using an ENUM directly
     * Validates: Type cannot be null
     */
    public void setType(LocationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Location type cannot be null");
        }
        this.type = type;
    }

    /**
     * Sets the type from a STRING
     * Converts string to enum using LocationType.fromString()
     * Validates: String must be a valid location type
     */
    public void setType(String type) {
        LocationType enumType = LocationType.fromString(type);
        if (enumType == null) {
            throw new IllegalArgumentException("Invalid location type: " + type);
        }
        this.type = enumType;
    }

    /**
     * Sets the latitude (GPS coordinate)
     * Validates: Must be between -90 and 90 (valid Earth coordinates)
     */
    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        this.latitude = latitude;
    }

    /**
     * Sets the longitude (GPS coordinate)
     * Validates: Must be between -180 and 180 (valid Earth coordinates)
     */
    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.longitude = longitude;
    }

    // =============================================
    // HELPER METHODS - Extra useful functions
    // =============================================

    /**
     * Returns a user-friendly display name for the type
     * e.g., "emergency" instead of "EMERGENCY"
     * Returns "Unknown" if type is null (safe fallback)
     */
    public String getTypeDisplayName() {
        return type != null ? type.getDisplayName() : "Unknown";
    }

    // =============================================
    // STANDARD METHODS - toString, equals, hashCode
    // =============================================

    /**
     * Returns a readable string representation of the Location
     * Example: Location{id=1, name='Emergency Unit', area='Korle-Bu', type='emergency', lat=5.540500, lon=-0.203800}
     * Useful for debugging and logging
     */
    @Override
    public String toString() {
        return String.format("Location{id=%d, name='%s', area='%s', type='%s', " +
                        "lat=%.6f, lon=%.6f}",
                locationId, name, area,
                type != null ? type.getDisplayName() : "Unknown",
                latitude, longitude);
    }

    /**
     * Compares two Location objects for equality
     * Two Locations are equal if they have the SAME ID
     * This is how database entities are compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId;
    }

    /**
     * Returns a hash code for the Location
     * Required when overriding equals()
     * Used in collections like HashMap and HashSet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(locationId);
    }
}