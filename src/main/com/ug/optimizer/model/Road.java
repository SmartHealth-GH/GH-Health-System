package main.com.ug.optimizer.model; // This file belongs to the 'model' package

// No imports needed - Road uses only primitive types and Location (in same package)

/**
 * Represents a connection/road between two locations
 * Maps to the 'roads' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Road {

    // =============================================
    // FIELDS - These are the data each Road stores
    // =============================================

    private int roadId;                   // Unique ID number for this road
    private int fromLocationId;           // Where does this road start? (Location ID)
    private int toLocationId;             // Where does this road end? (Location ID)
    private double distance;              // How long is the road? (in kilometers)
    private int travelTime;               // How long does it take to travel? (in minutes)
    private double roadConditionWeight;   // How good is the road? (0.5 = poor, 1.5 = excellent)

    // Additional fields for convenience (hold actual Location objects)
    private Location fromLocation;        // Full Location object for the starting point
    private Location toLocation;          // Full Location object for the ending point

    // =============================================
    // CONSTRUCTORS - Ways to create a Road
    // =============================================

    /**
     * Default constructor - creates an empty Road with default condition
     * Default: roadConditionWeight = 1.0 (Good condition)
     */
    public Road() {
        this.roadConditionWeight = 1.0; // Default: good condition
    }

    /**
     * Full constructor - creates a Road with ALL values provided
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
     * Constructor without ID - for NEW records
     * Database will auto-generate the ID when saved
     * Calls the full constructor with ID = 0 (placeholder)
     */
    public Road(int fromLocationId, int toLocationId,
                double distance, int travelTime, double roadConditionWeight) {
        this(0, fromLocationId, toLocationId, distance, travelTime, roadConditionWeight);
    }

    /**
     * Convenience constructor - uses default condition weight (1.0)
     * Useful when you don't know the exact condition
     */
    public Road(int fromLocationId, int toLocationId,
                double distance, int travelTime) {
        this(0, fromLocationId, toLocationId, distance, travelTime, 1.0);
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public int getRoadId() { return roadId; }
    public int getFromLocationId() { return fromLocationId; }
    public int getToLocationId() { return toLocationId; }
    public double getDistance() { return distance; }
    public int getTravelTime() { return travelTime; }
    public double getRoadConditionWeight() { return roadConditionWeight; }
    public Location getFromLocation() { return fromLocation; }
    public Location getToLocation() { return toLocation; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // Each setter includes VALIDATION to prevent bad data
    // =============================================

    /**
     * Sets the road ID
     * Validates: ID cannot be negative
     */
    public void setRoadId(int roadId) {
        if (roadId < 0) {
            throw new IllegalArgumentException("Road ID cannot be negative");
        }
        this.roadId = roadId;
    }

    /**
     * Sets the starting location ID
     * Validates: Location ID must be positive (valid location)
     */
    public void setFromLocationId(int fromLocationId) {
        if (fromLocationId <= 0) {
            throw new IllegalArgumentException("From location ID must be positive");
        }
        this.fromLocationId = fromLocationId;
    }

    /**
     * Sets the ending location ID
     * Validates: Location ID must be positive (valid location)
     */
    public void setToLocationId(int toLocationId) {
        if (toLocationId <= 0) {
            throw new IllegalArgumentException("To location ID must be positive");
        }
        this.toLocationId = toLocationId;
    }

    /**
     * Sets the distance
     * Validates: Distance cannot be negative
     */
    public void setDistance(double distance) {
        if (distance < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
        this.distance = distance;
    }

    /**
     * Sets the travel time
     * Validates: Travel time cannot be negative
     */
    public void setTravelTime(int travelTime) {
        if (travelTime < 0) {
            throw new IllegalArgumentException("Travel time cannot be negative");
        }
        this.travelTime = travelTime;
    }

    /**
     * Sets the road condition weight
     * Validates: Must be between 0.1 (very poor) and 2.0 (excellent)
     */
    public void setRoadConditionWeight(double roadConditionWeight) {
        if (roadConditionWeight < 0.1 || roadConditionWeight > 2.0) {
            throw new IllegalArgumentException("Road condition weight must be between 0.1 and 2.0");
        }
        this.roadConditionWeight = roadConditionWeight;
    }

    /**
     * Sets the full Location objects (convenience fields)
     * No validation needed (can be null if not set yet)
     */
    public void setFromLocation(Location fromLocation) { this.fromLocation = fromLocation; }
    public void setToLocation(Location toLocation) { this.toLocation = toLocation; }

    // =============================================
    // HELPER METHODS - Extra useful functions
    // =============================================

    /**
     * Calculate effective weight considering distance and condition
     * Effective weight = distance × roadConditionWeight
     * Used by routing algorithms (Dijkstra) to find the best path
     */
    public double getEffectiveWeight() {
        return distance * roadConditionWeight;
    }

    /**
     * Check if this road connects two locations
     * Returns true if the road has both location IDs
     *
     * Example: connects(1, 2) returns true if road goes 1→2 or 2→1
     */
    public boolean connects(int locationId1, int locationId2) {
        return (fromLocationId == locationId1 && toLocationId == locationId2) ||
                (fromLocationId == locationId2 && toLocationId == locationId1);
    }

    /**
     * Get the other end of the road
     * If you're at location X, what's the location on the other side?
     *
     * Example: If road is 1→2, getOtherEnd(1) returns 2
     *          getOtherEnd(2) returns 1
     *
     * Throws exception if location is not on this road
     */
    public int getOtherEnd(int locationId) {
        if (fromLocationId == locationId) return toLocationId;
        if (toLocationId == locationId) return fromLocationId;
        throw new IllegalArgumentException("Location " + locationId + " is not on this road");
    }

    /**
     * Returns a human-readable label for the road condition
     * Based on roadConditionWeight:
     * - ≥ 1.3: Excellent
     * - ≥ 1.0: Good
     * - ≥ 0.7: Fair
     * - < 0.7: Poor
     */
    public String getConditionLabel() {
        if (roadConditionWeight >= 1.3) return "Excellent";
        if (roadConditionWeight >= 1.0) return "Good";
        if (roadConditionWeight >= 0.7) return "Fair";
        return "Poor";
    }

    // =============================================
    // STANDARD METHODS - toString, equals, hashCode
    // =============================================

    /**
     * Returns a readable string representation of the Road
     * Example: Road{id=1, from=1 -> to=2, dist=0.30km, time=5min, condition=1.00 (Good)}
     * Useful for debugging and logging
     */
    @Override
    public String toString() {
        return String.format("Road{id=%d, from=%d -> to=%d, dist=%.2fkm, " +
                        "time=%dmin, condition=%.2f (%s)}",
                roadId, fromLocationId, toLocationId,
                distance, travelTime, roadConditionWeight,
                getConditionLabel());
    }

    /**
     * Compares two Road objects for equality
     * Two Roads are equal if they have the SAME ID
     * This is how database entities are compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Road road = (Road) o;
        return roadId == road.roadId;
    }

    /**
     * Returns a hash code for the Road
     * Required when overriding equals()
     * Used in collections like HashMap and HashSet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(roadId);
    }
}