package main.com.ug.optimizer.database;

import main.com.ug.optimizer.database.mapAlgorithm.DataSeeder;
import main.com.ug.optimizer.model.*;
import main.com.ug.optimizer.model.enums.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive test class for the database
 * User chooses what to do
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DatabaseTest {

    private static Scanner scanner = new Scanner(System.in);
    private static DatabaseConnection db;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🏥 GHANA HOSPITAL SYSTEM - DATABASE TOOL");
        System.out.println("=".repeat(60));

        try {
            // Connect to database
            db = DatabaseConnection.getInstance();
            db.connect();
            System.out.println("✅ Connected to database!\n");

            // Show main menu
            showMainMenu();

            // Disconnect
            db.disconnect();
            System.out.println("\n✅ Disconnected!");
            System.out.println("\n" + "=".repeat(60));
            System.out.println("👋 Goodbye!");
            System.out.println("=".repeat(60));

        } catch (SQLException e) {
            System.err.println("\n❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Main menu
     */
    private static void showMainMenu() throws SQLException {
        while (true) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📋 MAIN MENU");
            System.out.println("=".repeat(50));
            System.out.println("  [1] Initialize Database (create tables)");
            System.out.println("  [2] Seed Sample Data");
            System.out.println("  [3] Import CSV Data");
            System.out.println("  [4] Generate 300 Requests");
            System.out.println("  [5] View Data (Export Menu)");
            System.out.println("  [6] Test CRUD Operations");
            System.out.println("  [7] Show Summary Statistics");
            System.out.println("  [8] Clear All Data");
            System.out.println("  [9] Run ALL (full setup)");
            System.out.println("  [0] Exit");
            System.out.println("=".repeat(50));
            System.out.print("  Choose an option: ");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    initializeDatabase();
                    break;
                case "2":
                    seedData();
                    break;
                case "3":
                    importCsv();
                    break;
                case "4":
                    generateRequests();
                    break;
                case "5":
                    showExportMenu();
                    break;
                case "6":
                    testCrudOperations();
                    break;
                case "7":
                    displaySummary();
                    break;
                case "8":
                    clearAllData();
                    break;
                case "9":
                    runAll();
                    break;
                case "0":
                    return;
                default:
                    System.out.println("   ❌ Invalid option. Please try again.");
            }

            System.out.print("\n   Press Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Initialize database tables
     */
    private static void initializeDatabase() throws SQLException {
        System.out.println("\n📌 Initializing database tables...");
        db.initializeDatabase();
        System.out.println("✅ Tables ready!");
    }

    /**
     * Seed sample data
     */
    private static void seedData() throws SQLException {
        LocationDAO locationDAO = new LocationDAO();
        if (locationDAO.countLocations() > 0) {
            System.out.print("   ⚠️ Data already exists. Re-seed? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("y") && !response.equals("yes")) {
                System.out.println("   ⏭️ Skipping seed.");
                return;
            }
        }

        System.out.println("\n📌 Seeding sample data...");
        DataSeeder seeder = new DataSeeder();
        seeder.seedAll();
        System.out.println("✅ Sample data seeded!");
    }

    /**
     * Import CSV data
     */
    private static void importCsv() {
        System.out.println("\n📌 Importing CSV data...");
        try {
            CsvImporter importer = new CsvImporter();
            importer.importAll();
            System.out.println("✅ CSV import complete!");
        } catch (Exception e) {
            System.err.println("❌ CSV import failed: " + e.getMessage());
        }
    }

    /**
     * Generate 300 requests
     */
    private static void generateRequests() throws SQLException {
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        int currentCount = reqDAO.countRequests();

        System.out.println("\n📌 Generating service requests...");
        System.out.println("   Current requests: " + currentCount);
        System.out.print("   How many to generate? (Enter number, or 'all' for 300): ");
        String input = scanner.nextLine().trim();

        int target = 300;
        if (!input.equalsIgnoreCase("all")) {
            try {
                target = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("   ❌ Invalid number. Using default: 300");
                target = 300;
            }
        }

        int needed = Math.max(0, target - currentCount);
        if (needed == 0) {
            System.out.println("   ✅ Already have " + currentCount + " requests!");
            return;
        }

        RequestGenerator generator = new RequestGenerator();
        generator.generateRequests(needed);
        System.out.println("✅ Generated " + needed + " requests!");
    }

    /**
     * Show export menu (from DataExporter)
     */
    private static void showExportMenu() throws SQLException {
        System.out.println("\n📌 Loading export menu...");
        DataExporter.showMenu();
    }

    /**
     * Test CRUD operations
     */
    private static void testCrudOperations() throws SQLException {
        System.out.println("\n📌 Testing CRUD operations...");
        LocationDAO locDAO = new LocationDAO();

        // 1. INSERT
        System.out.println("\n   🔹 Test 1: Inserting a new location...");
        Location newLoc = new Location("Test Ward", "Test Area", LocationType.WARD, 0.0, 0.0);
        int newId = locDAO.insertLocation(newLoc);
        System.out.println("   ✅ Inserted with ID: " + newId);

        // 2. SELECT
        System.out.println("\n   🔹 Test 2: Retrieving the location...");
        Location retrieved = locDAO.getLocationById(newId);
        System.out.println("   ✅ Retrieved: " + retrieved.getName());

        // 3. UPDATE
        System.out.println("\n   🔹 Test 3: Updating the location...");
        retrieved.setName("Updated Test Ward");
        locDAO.updateLocation(retrieved);
        Location updated = locDAO.getLocationById(newId);
        System.out.println("   ✅ Updated name to: " + updated.getName());

        // 4. DELETE
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

    /**
     * Display summary statistics
     */
    private static void displaySummary() throws SQLException {
        LocationDAO locDAO = new LocationDAO();
        RoadDAO roadDAO = new RoadDAO();
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        ResourceDAO resDAO = new ResourceDAO();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 DATABASE SUMMARY");
        System.out.println("=".repeat(60));
        System.out.printf("  🏥 Locations:          %d%n", locDAO.countLocations());
        System.out.printf("  🛤️ Roads:              %d%n", roadDAO.countRoads());
        System.out.printf("  📋 Total Requests:     %d%n", reqDAO.countRequests());
        System.out.printf("  📋 Pending:            %d%n", reqDAO.countByStatus(RequestStatus.PENDING));
        System.out.printf("  📋 Completed:          %d%n", reqDAO.countByStatus(RequestStatus.COMPLETED));
        System.out.printf("  📋 In Progress:        %d%n", reqDAO.countByStatus(RequestStatus.IN_PROGRESS));
        System.out.printf("  🚑 Total Resources:    %d%n", resDAO.countResources());
        System.out.printf("  🚑 Available:          %d%n", resDAO.countAvailableResources());
        System.out.println("=".repeat(60));
    }

    /**
     * Clear all data
     */
    private static void clearAllData() throws SQLException {
        System.out.print("\n⚠️ WARNING: This will delete ALL data! Continue? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (!response.equals("yes") && !response.equals("y")) {
            System.out.println("   ❌ Operation cancelled.");
            return;
        }

        System.out.println("\n🗑️ Clearing all data...");

        java.sql.Connection conn = db.getConnection();
        try (java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = OFF");
            stmt.execute("DELETE FROM algorithm_runs");
            stmt.execute("DELETE FROM service_requests");
            stmt.execute("DELETE FROM resources");
            stmt.execute("DELETE FROM roads");
            stmt.execute("DELETE FROM locations");
            stmt.execute("DELETE FROM sqlite_sequence");
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        System.out.println("✅ All data cleared!");
    }

    /**
     * Run everything (full setup)
     */
    private static void runAll() throws SQLException {
        System.out.println("\n🚀 RUNNING FULL SETUP...");
        System.out.println("=".repeat(50));

        // Step 1: Initialize
        System.out.println("\n[1/5] Initializing database...");
        db.initializeDatabase();

        // Step 2: Seed sample data
        System.out.println("\n[2/5] Seeding sample data...");
        LocationDAO locDAO = new LocationDAO();
        if (locDAO.countLocations() == 0) {
            DataSeeder seeder = new DataSeeder();
            seeder.seedAll();
        } else {
            System.out.println("   ⏭️ Data already exists, skipping seed.");
        }

        // Step 3: Import CSV
        System.out.println("\n[3/5] Importing CSV data...");
        try {
            CsvImporter importer = new CsvImporter();
            importer.importAll();
        } catch (Exception e) {
            System.err.println("   ⚠️ CSV import failed: " + e.getMessage());
        }

        // Step 4: Generate requests
        System.out.println("\n[4/5] Generating 300 requests...");
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        int current = reqDAO.countRequests();
        if (current < 300) {
            RequestGenerator generator = new RequestGenerator();
            generator.generateRequests(300 - current);
        } else {
            System.out.println("   ✅ Already have " + current + " requests!");
        }

        // Step 5: Show summary
        System.out.println("\n[5/5] Showing summary...");
        displaySummary();

        System.out.println("\n✅ FULL SETUP COMPLETE!");
    }
}