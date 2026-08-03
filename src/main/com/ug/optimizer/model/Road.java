package main.com.ug.optimizer.model;

/**
 * Represents a connection/road between two locations
 * Maps to the 'roads' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Road {
    private int roadId;
    private int fromLocationId;
    private int toLocationId;
    private double distance;           // in kilometers
    private int travelTime;             // in minutes
    private double roadConditionWeight; // 0.5 - 1.5 (poor to excellent)

    // Additional fields for convenience
    private Location fromLocation;
    private Location toLocation;

    /**
     * Default constructor
     */
    public Road() {
        this.roadConditionWeight = 1.0;
    }

    /**
     * Full constructor with ID
     */
    public Road(int roadId, int fromLocationId, int toLocationId,
                double distance, int travelTime, double roadConditionWeight) {
        this.roadId = roadId;
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distance = distance;
        this.travelTime = travelTime;
        this.roadConditionWeight = roadConditionWeight;
    }

    /**
     * Constructor without ID
     */
    public Road(int fromLocationId, int toLocationId,
                double distance, int travelTime, double roadConditionWeight) {
        this(0, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight);
    }

    /**
     * Constructor with default condition weight
     */
    public Road(int fromLocationId, int toLocationId,
                double distance, int travelTime) {
        this(0, fromLocationId, toLocationId, distance, travelTime, 1.0);
    }

    // ===== Getters =====
    public int getRoadId() { return roadId; }
    public int getFromLocationId() { return fromLocationId; }
    public int getToLocationId() { return toLocationId; }
    public double getDistance() { return distance; }
    public int getTravelTime() { return travelTime; }
    public double getRoadConditionWeight() { return roadConditionWeight; }
    public Location getFromLocation() { return fromLocation; }
    public Location getToLocation() { return toLocation; }

    // ===== Setters with Validation =====
    public void setRoadId(int roadId) {
        if (roadId < 0) {
            throw new IllegalArgumentException("Road ID cannot be negative");
        }
        this.roadId = roadId;
    }

    public void setFromLocationId(int fromLocationId) {
        if (fromLocationId <= 0) {
            throw new IllegalArgumentException("From location ID must be positive");
        }
        this.fromLocationId = fromLocationId;
    }

    public void setToLocationId(int toLocationId) {
        if (toLocationId <= 0) {
            throw new IllegalArgumentException("To location ID must be positive");
        }
        this.toLocationId = toLocationId;
    }

    public void setDistance(double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        this.distance = distance;
    }

    public void setTravelTime(int travelTime) {
        if (travelTime < 0) {
            throw new IllegalArgumentException("Travel time cannot be negative");
        }
        this.travelTime = travelTime;
    }

    public void setRoadConditionWeight(double roadConditionWeight) {
        if (roadConditionWeight < 0.1 || roadConditionWeight > 2.0) {
            throw new IllegalArgumentException("Road condition weight must be between 0.1 and 2.0");
        }
        this.roadConditionWeight = roadConditionWeight;
    }

    public void setFromLocation(Location fromLocation) { this.fromLocation = fromLocation; }
    public void setToLocation(Location toLocation) { this.toLocation = toLocation; }

    // ===== Helper Methods =====
    /**
     * Calculate effective weight considering distance and condition
     */
    public double getEffectiveWeight() {
        return distance * roadConditionWeight;
    }

    /**
     * Check if road connects two locations
     */
    public boolean connects(int locationId1, int locationId2) {
        return (fromLocationId == locationId1 && toLocationId == locationId2) ||
                (fromLocationId == locationId2 && toLocationId == locationId1);
    }

    /**
     * Get the other end of the road
     */
    public int getOtherEnd(int locationId) {
        if (fromLocationId == locationId) return toLocationId;
        if (toLocationId == locationId) return fromLocationId;
        throw new IllegalArgumentException("Location " + locationId + " is not on this road");
    }

    public String getConditionLabel() {
        if (roadConditionWeight >= 1.3) return "Excellent";
        if (roadConditionWeight >= 1.0) return "Good";
        if (roadConditionWeight >= 0.7) return "Fair";
        return "Poor";
    }

    @Override
    public String toString() {
        return String.format("Road{id=%d, from=%d -> to=%d, dist=%.2fkm, " +
                        "time=%dmin, condition=%.2f (%s)}",
                roadId, fromLocationId, toLocationId,
                distance, travelTime, roadConditionWeight,
                getConditionLabel());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return roadId == road.roadId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(roadId);
    }
}