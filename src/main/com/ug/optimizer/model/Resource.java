package main.com.ug.optimizer.model; // This file belongs to the 'model' package

import main.com.ug.optimizer.model.enums.ResourceStatus; // Bring in the ResourceStatus enum (AVAILABLE, IN_USE, etc.)

/**
 * Represents a hospital resource (ambulance, doctor, bed, etc.)
 * Maps to the 'resources' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Resource {

    // =============================================
    // FIELDS - These are the data each Resource stores
    // =============================================

    private int resourceId;              // Unique ID number (like equipment ID)
    private String type;                 // What kind of resource? (e.g., "Ambulance", "Doctor", "Bed")
    private int homeLocationId;          // Where is it normally based? (Location ID)
    private int capacity;                // How many people/things it can handle
    private ResourceStatus availabilityStatus; // Is it available? (ENUM: AVAILABLE, IN_USE, etc.)

    // Additional field for convenience (holds the actual Location object)
    private Location homeLocation;       // The full Location object (not just ID)

    // =============================================
    // CONSTRUCTORS - Ways to create a Resource
    // =============================================

    /**
     * Default constructor - creates an empty Resource with default values
     * Default: AVAILABLE status, capacity = 1
     */
    public Resource() {
        this.availabilityStatus = ResourceStatus.AVAILABLE; // Start as available
        this.capacity = 1; // Most resources handle one thing at a time
    }

    /**
     * Full constructor - creates a Resource with ALL values provided
     */
    public Resource(int resourceId, String type, int homeLocationId,
                    int capacity, ResourceStatus availabilityStatus) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocationId = homeLocationId;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * Constructor without ID - for NEW records
     * Database will auto-generate the ID when saved
     * Calls the full constructor with ID = 0 (placeholder)
     */
    public Resource(String type, int homeLocationId,
                    int capacity, ResourceStatus availabilityStatus) {
        this(0, type, homeLocationId, capacity, availabilityStatus);
    }

    /**
     * Convenience constructor - accepts status as a STRING
     * Converts string to enum automatically
     * Useful for CSV loading where data comes as text
     */
    public Resource(String type, int homeLocationId,
                    int capacity, String availabilityStatus) {
        this(type, homeLocationId, capacity,
                ResourceStatus.fromString(availabilityStatus));
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public int getResourceId() { return resourceId; }
    public String getType() { return type; }
    public int getHomeLocationId() { return homeLocationId; }
    public int getCapacity() { return capacity; }
    public ResourceStatus getAvailabilityStatus() { return availabilityStatus; }
    public Location getHomeLocation() { return homeLocation; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // Each setter includes VALIDATION to prevent bad data
    // =============================================

    /**
     * Sets the resource ID
     * Validates: ID cannot be negative
     */
    public void setResourceId(int resourceId) {
        if (resourceId < 0) {
            throw new IllegalArgumentException("Resource ID cannot be negative");
        }
        this.resourceId = resourceId;
    }

    /**
     * Sets the resource type (e.g., "Ambulance", "Doctor")
     * Validates: Type cannot be null or empty
     */
    public void setType(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Resource type cannot be null or empty");
        }
        this.type = type.trim();
    }

    /**
     * Sets the home location ID (where this resource is based)
     * Validates: Location ID must be positive
     */
    public void setHomeLocationId(int homeLocationId) {
        if (homeLocationId <= 0) {
            throw new IllegalArgumentException("Home location ID must be positive");
        }
        this.homeLocationId = homeLocationId;
    }

    /**
     * Sets the capacity (how many patients/things it can handle)
     * Validates: Capacity cannot be negative
     */
    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

    /**
     * Sets the availability status using an ENUM directly
     * Validates: Status cannot be null
     */
    public void setAvailabilityStatus(ResourceStatus availabilityStatus) {
        if (availabilityStatus == null) {
            throw new IllegalArgumentException("Availability status cannot be null");
        }
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * Sets the availability status from a STRING
     * Converts string to enum using ResourceStatus.fromString()
     * Validates: String must be a valid status
     */
    public void setAvailabilityStatus(String availabilityStatus) {
        ResourceStatus status = ResourceStatus.fromString(availabilityStatus);
        if (status == null) {
            throw new IllegalArgumentException("Invalid status: " + availabilityStatus);
        }
        this.availabilityStatus = status;
    }

    /**
     * Sets the full Location object (convenience field)
     * No validation needed (can be null)
     */
    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    // =============================================
    // HELPER METHODS - Extra useful functions
    // =============================================

    /**
     * Checks if the resource is available
     * Returns true if status is AVAILABLE, false otherwise
     */
    public boolean isAvailable() {
        return availabilityStatus != null && availabilityStatus.isAvailable();
    }

    /**
     * Checks if the resource is currently in use
     */
    public boolean isInUse() {
        return availabilityStatus == ResourceStatus.IN_USE;
    }

    /**
     * Returns a user-friendly display name for the status
     * e.g., "Available" instead of "AVAILABLE"
     * Returns "Unknown" if status is null (safe fallback)
     */
    public String getStatusDisplayName() {
        return availabilityStatus != null ? availabilityStatus.getDisplayName() : "Unknown";
    }

    // =============================================
    // STANDARD METHODS - toString, equals, hashCode
    // =============================================

    /**
     * Returns a readable string representation of the Resource
     * Example: Resource{id=1, type='Ambulance', capacity=4, status='Available', locationId=10}
     * Useful for debugging and logging
     */
    @Override
    public String toString() {
        return String.format("Resource{id=%d, type='%s', capacity=%d, " +
                        "status='%s', locationId=%d}",
                resourceId, type, capacity,
                getStatusDisplayName(), homeLocationId);
    }

    /**
     * Compares two Resource objects for equality
     * Two Resources are equal if they have the SAME ID
     * This is how database entities are compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return resourceId == resource.resourceId;
    }

    /**
     * Returns a hash code for the Resource
     * Required when overriding equals()
     * Used in collections like HashMap and HashSet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(resourceId);
    }
}