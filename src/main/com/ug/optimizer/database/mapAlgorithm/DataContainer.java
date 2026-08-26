package main.com.ug.optimizer.database.mapAlgorithm; // This file belongs to the database.mapAlgorithm package

import main.com.ug.optimizer.model.*; // Import ALL model classes (Location, Road, ServiceRequest, etc.)
// The * means "import everything from the model package"

import java.util.ArrayList; // For creating ArrayLists to store data
import java.util.List; // For the List interface (collection of items)

/**
 * Container class for holding all loaded data from the database
 * Allows bulk loading of all data with a single call
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DataContainer {

    // =============================================
    // FIELDS - These store ALL the data from the database
    // =============================================

    private List<Location> locations;          // All locations from the database
    private List<Road> roads;                  // All roads from the database
    private List<ServiceRequest> requests;     // All service requests from the database
    private List<Resource> resources;          // All resources from the database
    private List<AlgorithmRun> algorithmRuns;  // All algorithm performance records

    // =============================================
    // CONSTRUCTOR - Creates an empty DataContainer
    // =============================================

    /**
     * Default constructor - creates an empty container
     * Initializes all lists as empty ArrayLists
     * This is where data will be loaded into
     */
    public DataContainer() {
        this.locations = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.algorithmRuns = new ArrayList<>();
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public List<Location> getLocations() { return locations; }
    public List<Road> getRoads() { return roads; }
    public List<ServiceRequest> getRequests() { return requests; }
    public List<Resource> getResources() { return resources; }
    public List<AlgorithmRun> getAlgorithmRuns() { return algorithmRuns; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // =============================================

    public void setLocations(List<Location> locations) { this.locations = locations; }
    public void setRoads(List<Road> roads) { this.roads = roads; }
    public void setRequests(List<ServiceRequest> requests) { this.requests = requests; }
    public void setResources(List<Resource> resources) { this.resources = resources; }
    public void setAlgorithmRuns(List<AlgorithmRun> algorithmRuns) { this.algorithmRuns = algorithmRuns; }

    // =============================================
    // CONVENIENCE METHODS - Extra useful functions
    // =============================================

    /**
     * Checks if the container is completely empty
     * Returns true if ALL lists are empty
     */
    public boolean isEmpty() {
        return locations.isEmpty() && roads.isEmpty() &&
                requests.isEmpty() && resources.isEmpty() &&
                algorithmRuns.isEmpty();
    }

    /**
     * Removes ALL data from ALL lists
     * Useful for resetting the container
     */
    public void clear() {
        locations.clear();
        roads.clear();
        requests.clear();
        resources.clear();
        algorithmRuns.clear();
    }

    /**
     * Get total count of all entities combined
     * Example: if 50 locations + 100 roads + 300 requests = 450 total
     */
    public int getTotalCount() {
        return locations.size() + roads.size() + requests.size() +
                resources.size() + algorithmRuns.size();
    }

    // =============================================
    // TOSTRING - Display the container contents
    // =============================================

    /**
     * Returns a readable summary of the container
     * Example: DataContainer{locations=67, roads=117, requests=300, resources=42, algorithmRuns=5, total=531}
     * Useful for debugging and logging
     */
    @Override
    public String toString() {
        return String.format("DataContainer{locations=%d, roads=%d, requests=%d, " +
                        "resources=%d, algorithmRuns=%d, total=%d}",
                locations.size(), roads.size(), requests.size(),
                resources.size(), algorithmRuns.size(), getTotalCount());
    }

    // =============================================
    // BUILDER PATTERN - Helps create DataContainer with data
    // =============================================

    /**
     * Builder pattern for creating DataContainer with data
     * This is a design pattern that makes it easy to create objects
     * with many optional parameters
     */
    public static class Builder {
        private DataContainer container;

        /**
         * Creates a new Builder with an empty container
         */
        public Builder() {
            this.container = new DataContainer();
        }

        /**
         * Add locations to the container
         * Returns the Builder (for method chaining)
         */
        public Builder withLocations(List<Location> locations) {
            container.locations = locations;
            return this;
        }

        /**
         * Add roads to the container
         * Returns the Builder (for method chaining)
         */
        public Builder withRoads(List<Road> roads) {
            container.roads = roads;
            return this;
        }

        /**
         * Add service requests to the container
         * Returns the Builder (for method chaining)
         */
        public Builder withRequests(List<ServiceRequest> requests) {
            container.requests = requests;
            return this;
        }

        /**
         * Add resources to the container
         * Returns the Builder (for method chaining)
         */
        public Builder withResources(List<Resource> resources) {
            container.resources = resources;
            return this;
        }

        /**
         * Add algorithm runs to the container
         * Returns the Builder (for method chaining)
         */
        public Builder withAlgorithmRuns(List<AlgorithmRun> algorithmRuns) {
            container.algorithmRuns = algorithmRuns;
            return this;
        }

        /**
         * Build and return the final DataContainer
         */
        public DataContainer build() {
            return container;
        }
    }
}