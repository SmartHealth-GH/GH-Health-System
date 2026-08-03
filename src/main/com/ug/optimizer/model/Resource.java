package main.com.ug.optimizer.model;

import main.com.ug.optimizer.model.enums.ResourceStatus;

/**
 * Represents a hospital resource (ambulance, doctor, bed, etc.)
 * Maps to the 'resources' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Resource {
    private int resourceId;
    private String type;
    private int homeLocationId;
    private int capacity;
    private ResourceStatus availabilityStatus;

    // Additional fields for convenience
    private Location homeLocation;

    /**
     * Default constructor
     */
    public Resource() {
        this.availabilityStatus = ResourceStatus.AVAILABLE;
        this.capacity = 1;
    }

    /**
     * Full constructor with ID
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
     * Constructor without ID
     */
    public Resource(String type, int homeLocationId,
                    int capacity, ResourceStatus availabilityStatus) {
        this(0, type, homeLocationId, capacity, availabilityStatus);
    }

    /**
     * Constructor with string status
     */
    public Resource(String type, int homeLocationId,
                    int capacity, String availabilityStatus) {
        this(type, homeLocationId, capacity,
                ResourceStatus.fromString(availabilityStatus));
    }

    // ===== Getters =====
    public int getResourceId() { return resourceId; }
    public String getType() { return type; }
    public int getHomeLocationId() { return homeLocationId; }
    public int getCapacity() { return capacity; }
    public ResourceStatus getAvailabilityStatus() { return availabilityStatus; }
    public Location getHomeLocation() { return homeLocation; }

    // ===== Setters with Validation =====
    public void setResourceId(int resourceId) {
        if (resourceId < 0) {
            throw new IllegalArgumentException("Resource ID cannot be negative");
        }
        this.resourceId = resourceId;
    }

    public void setType(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Resource type cannot be null or empty");
        }
        this.type = type.trim();
    }

    public void setHomeLocationId(int homeLocationId) {
        if (homeLocationId <= 0) {
            throw new IllegalArgumentException("Home location ID must be positive");
        }
        this.homeLocationId = homeLocationId;
    }

    public void setCapacity(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative");
        }
        this.capacity = capacity;
    }

    public void setAvailabilityStatus(ResourceStatus availabilityStatus) {
        if (availabilityStatus == null) {
            throw new IllegalArgumentException("Availability status cannot be null");
        }
        this.availabilityStatus = availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        ResourceStatus status = ResourceStatus.fromString(availabilityStatus);
        if (status == null) {
            throw new IllegalArgumentException("Invalid status: " + availabilityStatus);
        }
        this.availabilityStatus = status;
    }

    public void setHomeLocation(Location homeLocation) {
        this.homeLocation = homeLocation;
    }

    // ===== Helper Methods =====
    public boolean isAvailable() {
        return availabilityStatus != null && availabilityStatus.isAvailable();
    }

    public boolean isInUse() {
        return availabilityStatus == ResourceStatus.IN_USE;
    }

    public String getStatusDisplayName() {
        return availabilityStatus != null ? availabilityStatus.getDisplayName() : "Unknown";
    }

    @Override
    public String toString() {
        return String.format("Resource{id=%d, type='%s', capacity=%d, " +
                        "status='%s', locationId=%d}",
                resourceId, type, capacity,
                getStatusDisplayName(), homeLocationId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return resourceId == resource.resourceId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(resourceId);
    }
}