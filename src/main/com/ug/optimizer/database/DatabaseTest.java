package main.com.ug.optimizer.database;

import main.com.ug.optimizer.database.mapAlgorithm.DataSeeder;
import main.com.ug.optimizer.model.*;
import main.com.ug.optimizer.model.enums.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Simple test class to verify database is working
 * Run this to test your database setup
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DatabaseTest {

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🏥 GHANA HOSPITAL SYSTEM - DATABASE TEST");
        System.out.println("=".repeat(60));

        try {
            // STEP 1: Get database connection
            System.out.println("\n📌 STEP 1: Connecting to database...");
            DatabaseConnection db = DatabaseConnection.getInstance();
            db.connect();
            System.out.println("✅ Connection successful!");

            // STEP 2: Initialize tables (if not already created)
            System.out.println("\n📌 STEP 2: Initializing database tables...");
            db.initializeDatabase();
            System.out.println("✅ Tables ready!");

            // STEP 3: Check if data exists
            System.out.println("\n📌 STEP 3: Checking for existing data...");
            LocationDAO locationDAO = new LocationDAO();
            int locationCount = locationDAO.countLocations();
            System.out.println("   Locations found: " + locationCount);

            // STEP 4: Seed sample data if empty
            if (locationCount == 0) {
                System.out.println("\n📌 STEP 4: Seeding sample data (first time setup)...");
                DataSeeder seeder = new DataSeeder();
                seeder.seedAll();
                System.out.println("✅ Sample data seeded!");
            } else {
                System.out.println("\n📌 STEP 4: Data already exists, skipping seed.");
            }

            // STEP 5: Load and display data
            System.out.println("\n📌 STEP 5: Loading and displaying data...");
            displayAllData();

            // STEP 6: Test CRUD operations
            System.out.println("\n📌 STEP 6: Testing CRUD operations...");
            testCrudOperations();

            // STEP 7: Disconnect
            System.out.println("\n📌 STEP 7: Disconnecting...");
            db.disconnect();
            System.out.println("✅ Disconnected!");

            System.out.println("\n" + "=".repeat(60));
            System.out.println("🎉 ALL TESTS PASSED! Database is working!");
            System.out.println("=".repeat(60));

        } catch (SQLException e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Display all data from the database
     */
    private static void displayAllData() throws SQLException {
        LocationDAO locDAO = new LocationDAO();
        RoadDAO roadDAO = new RoadDAO();
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        ResourceDAO resDAO = new ResourceDAO();

        // Display locations
        List<Location> locations = locDAO.getAllLocations();
        System.out.println("\n📍 LOCATIONS (" + locations.size() + "):");
        for (Location loc : locations) {
            System.out.println("   " + loc.getLocationId() + ". " + loc.getName() +
                    " (" + loc.getType().getDisplayName() + ")");
        }

        // Display roads
        List<Road> roads = roadDAO.getAllRoads();
        System.out.println("\n🛤️ ROADS (" + roads.size() + "):");
        for (Road road : roads) {
            System.out.println("   " + road.getRoadId() + ". " +
                    road.getFromLocationId() + " → " + road.getToLocationId() +
                    " (" + road.getDistance() + "km, " + road.getTravelTime() + "min)");
        }

        // Display requests
        List<ServiceRequest> requests = reqDAO.getAllRequests();
        System.out.println("\n📋 SERVICE REQUESTS (" + requests.size() + "):");
        for (ServiceRequest req : requests) {
            System.out.println("   " + req.getRequestId() + ". " + req.getCategory() +
                    " (Urgency: " + req.getUrgency().getDisplayName() +
                    ", Status: " + req.getStatus().getDisplayName() + ")");
        }

        // Display resources
        List<Resource> resources = resDAO.getAllResources();
        System.out.println("\n🚑 RESOURCES (" + resources.size() + "):");
        for (Resource res : resources) {
            System.out.println("   " + res.getResourceId() + ". " + res.getType() +
                    " (Status: " + res.getStatusDisplayName() +
                    ", Capacity: " + res.getCapacity() + ")");
        }
    }

    /**
     * Test basic CRUD operations
     */
    private static void testCrudOperations() throws SQLException {
        LocationDAO locDAO = new LocationDAO();

        // 1. INSERT a new location
        System.out.println("\n   🔹 Test 1: Inserting a new location...");
        Location newLoc = new Location("Test Ward", "Test Area", LocationType.WARD, 0.0, 0.0);
        int newId = locDAO.insertLocation(newLoc);
        System.out.println("   ✅ Inserted with ID: " + newId);

        // 2. SELECT the new location
        System.out.println("\n   🔹 Test 2: Retrieving the location...");
        Location retrieved = locDAO.getLocationById(newId);
        System.out.println("   ✅ Retrieved: " + retrieved.getName());

        // 3. UPDATE the location
        System.out.println("\n   🔹 Test 3: Updating the location...");
        retrieved.setName("Updated Test Ward");
        locDAO.updateLocation(retrieved);
        Location updated = locDAO.getLocationById(newId);
        System.out.println("   ✅ Updated name to: " + updated.getName());

        // 4. DELETE the location
        System.out.println("\n   🔹 Test 4: Deleting the location...");
        locDAO.deleteLocation(newId);
        Location deleted = locDAO.getLocationById(newId);
        if (deleted == null) {
            System.out.println("   ✅ Successfully deleted!");
        } else {
            System.out.println("   ❌ Delete failed!");
        }

        System.out.println("\n   ✅ All CRUD tests passed!");
    }
}