package main.com.ug.optimizer.Database.mapAlgorithm;

import main.com.ug.optimizer.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Container class for holding all loaded data from the database
 * Allows bulk loading of all data with a single call
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DataContainer {

    private List<Location> locations;
    private List<Road> roads;
    private List<ServiceRequest> requests;
    private List<Resource> resources;
    private List<AlgorithmRun> algorithmRuns;

    public DataContainer() {
        this.locations = new ArrayList<>();
        this.roads = new ArrayList<>();
        this.requests = new ArrayList<>();
        this.resources = new ArrayList<>();
        this.algorithmRuns = new ArrayList<>();
    }

    // ===== Getters =====
    public List<Location> getLocations() { return locations; }
    public List<Road> getRoads() { return roads; }
    public List<ServiceRequest> getRequests() { return requests; }
    public List<Resource> getResources() { return resources; }
    public List<AlgorithmRun> getAlgorithmRuns() { return algorithmRuns; }

    // ===== Setters =====
    public void setLocations(List<Location> locations) { this.locations = locations; }
    public void setRoads(List<Road> roads) { this.roads = roads; }
    public void setRequests(List<ServiceRequest> requests) { this.requests = requests; }
    public void setResources(List<Resource> resources) { this.resources = resources; }
    public void setAlgorithmRuns(List<AlgorithmRun> algorithmRuns) { this.algorithmRuns = algorithmRuns; }

    // ===== Convenience Methods =====

    public boolean isEmpty() {
        return locations.isEmpty() && roads.isEmpty() &&
                requests.isEmpty() && resources.isEmpty() &&
                algorithmRuns.isEmpty();
    }

    public void clear() {
        locations.clear();
        roads.clear();
        requests.clear();
        resources.clear();
        algorithmRuns.clear();
    }

    /**
     * Get total count of all entities
     */
    public int getTotalCount() {
        return locations.size() + roads.size() + requests.size() +
                resources.size() + algorithmRuns.size();
    }

    @Override
    public String toString() {
        return String.format("DataContainer{locations=%d, roads=%d, requests=%d, " +
                        "resources=%d, algorithmRuns=%d, total=%d}",
                locations.size(), roads.size(), requests.size(),
                resources.size(), algorithmRuns.size(), getTotalCount());
    }

    /**
     * Builder pattern for creating DataContainer with data
     */
    public static class Builder {
        private DataContainer container;

        public Builder() {
            this.container = new DataContainer();
        }

        public Builder withLocations(List<Location> locations) {
            container.locations = locations;
            return this;
        }

        public Builder withRoads(List<Road> roads) {
            container.roads = roads;
            return this;
        }

        public Builder withRequests(List<ServiceRequest> requests) {
            container.requests = requests;
            return this;
        }

        public Builder withResources(List<Resource> resources) {
            container.resources = resources;
            return this;
        }

        public Builder withAlgorithmRuns(List<AlgorithmRun> algorithmRuns) {
            container.algorithmRuns = algorithmRuns;
            return this;
        }

        public DataContainer build() {
            return container;
        }
    }
}