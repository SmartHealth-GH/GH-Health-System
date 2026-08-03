package main.com.ug.optimizer.model;

import main.com.ug.optimizer.model.enums.LocationType;
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
    private int locationId;
    private String name;
    private String area;
    private LocationType type;
    private double latitude;
    private double longitude;

    /**
     * Default constructor for ORM/database operations
     */
    public Location() {
    }

    /**
     * Full constructor with ID
     * Chains to the main constructor for consistency
     */
    public Location(int locationId, String name, String area, LocationType type,
                    double latitude, double longitude) {
        this.locationId = locationId;
        setName(name);
        setArea(area);
        setType(type);
        setLatitude(latitude);
        setLongitude(longitude);
    }

    /**
     * Constructor without ID (for new records)
     * Chains to the full constructor
     */
    public Location(String name, String area, LocationType type,
                    double latitude, double longitude) {
        this(0, name, area, type, latitude, longitude);
    }

    /**
     * Convenience constructor with type as String
     * Converts string to enum for consistency
     */
    public Location(String name, String area, String type,
                    double latitude, double longitude) {
        this(name, area, LocationType.fromString(type), latitude, longitude);
    }

    // ===== Getters =====
    public int getLocationId() { return locationId; }
    public String getName() { return name; }
    public String getArea() { return area; }
    public LocationType getType() { return type; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    // ===== Setters with Validation =====
    public void setLocationId(int locationId) {
        if (locationId < 0) {
            throw new IllegalArgumentException("Location ID cannot be negative");
        }
        this.locationId = locationId;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Location name cannot be null or empty");
        }
        this.name = name.trim();
    }

    public void setArea(String area) {
        if (area == null || area.isBlank()) {
            throw new IllegalArgumentException("Area cannot be null or empty");
        }
        this.area = area.trim();
    }

    public void setType(LocationType type) {
        if (type == null) {
            throw new IllegalArgumentException("Location type cannot be null");
        }
        this.type = type;
    }

    public void setType(String type) {
        LocationType enumType = LocationType.fromString(type);
        if (enumType == null) {
            throw new IllegalArgumentException("Invalid location type: " + type);
        }
        this.type = enumType;
    }

    public void setLatitude(double latitude) {
        if (latitude < -90 || latitude > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        if (longitude < -180 || longitude > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        this.longitude = longitude;
    }

    // ===== Helper Methods =====
    public String getTypeDisplayName() {
        return type != null ? type.getDisplayName() : "Unknown";
    }

    @Override
    public String toString() {
        return String.format("Location{id=%d, name='%s', area='%s', type='%s', " +
                        "lat=%.6f, lon=%.6f}",
                locationId, name, area,
                type != null ? type.getDisplayName() : "Unknown",
                latitude, longitude);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(locationId);
    }
}