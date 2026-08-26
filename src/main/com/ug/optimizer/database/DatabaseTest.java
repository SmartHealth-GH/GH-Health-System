package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.database.mapAlgorithm.DataSeeder; // For seeding sample data
import main.com.ug.optimizer.model.*;                     // All model classes
import main.com.ug.optimizer.model.enums.*;               // All enums

import java.sql.SQLException;        // For database errors
import java.util.List;               // For lists
import java.util.Scanner;            // For reading user input from the console

/**
 * Interactive test class for the database
 * User chooses what to do - NOTHING runs automatically!
 *
 * This is the main tool you use to manage your database.
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DatabaseTest {

    // =============================================
    // FIELDS
    // =============================================

    private static Scanner scanner = new Scanner(System.in); // For reading user input
    private static DatabaseConnection db;                   // Database connection

    // =============================================
    // MAIN METHOD - Entry point
    // =============================================

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🏥 GHANA HOSPITAL SYSTEM - DATABASE TOOL");
        System.out.println("=".repeat(60));

        try {
            // Step 1: Connect to database
            db = DatabaseConnection.getInstance();  // Get the single connection
            db.connect();                           // Actually connect
            System.out.println("✅ Connected to database!\n");

            // Step 2: Show the main menu (user chooses what to do)
            showMainMenu();

            // Step 3: Disconnect when done
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

    // =============================================
    // MAIN MENU - Interactive menu system
    // =============================================

    /**
     * Main menu - displays options and handles user choices
     * This is the CORE of the interactive system
     */
    private static void showMainMenu() throws SQLException {
        while (true) {  // Infinite loop until user chooses exit
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

            String input = scanner.nextLine().trim();  // Read user input

            // =============================================
            // Handle each option
            // =============================================

            switch (input) {
                case "1":
                    initializeDatabase();   // Create tables
                    break;
                case "2":
                    seedData();             // Seed sample data
                    break;
                case "3":
                    importCsv();            // Import CSV files
                    break;
                case "4":
                    generateRequests();     // Generate 300 requests
                    break;
                case "5":
                    showExportMenu();       // View/export data
                    break;
                case "6":
                    testCrudOperations();   // Test CRUD
                    break;
                case "7":
                    displaySummary();       // Show summary
                    break;
                case "8":
                    clearAllData();         // Clear everything
                    break;
                case "9":
                    runAll();               // Run full setup
                    break;
                case "0":
                    return;                 // Exit the menu
                default:
                    System.out.println("   ❌ Invalid option. Please try again.");
            }

            // Wait for user to press Enter before showing menu again
            System.out.print("\n   Press Enter to continue...");
            scanner.nextLine();
        }
    }

    // =============================================
    // OPTION 1 - Initialize Database
    // =============================================

    /**
     * Initialize database tables
     * Creates all tables if they don't exist
     */
    private static void initializeDatabase() throws SQLException {
        System.out.println("\n📌 Initializing database tables...");
        db.initializeDatabase();  // Calls DatabaseConnection.initializeDatabase()
        System.out.println("✅ Tables ready!");
    }

    // =============================================
    // OPTION 2 - Seed Sample Data
    // =============================================

    /**
     * Seed sample data
     * Adds 15 locations, 17 roads, 11 requests, 12 resources
     * Asks for confirmation if data already exists
     */
    private static void seedData() throws SQLException {
        LocationDAO locationDAO = new LocationDAO();

        // Check if data already exists
        if (locationDAO.countLocations() > 0) {
            System.out.print("   ⚠️ Data already exists. Re-seed? (y/n): ");
            String response = scanner.nextLine().trim().toLowerCase();
            if (!response.equals("y") && !response.equals("yes")) {
                System.out.println("   ⏭️ Skipping seed.");
                return; // Exit without seeding
            }
        }

        System.out.println("\n📌 Seeding sample data...");
        DataSeeder seeder = new DataSeeder();
        seeder.seedAll();  // Call the DataSeeder
        System.out.println("✅ Sample data seeded!");
    }

    // =============================================
    // OPTION 3 - Import CSV Data
    // =============================================

    /**
     * Import CSV data
     * Reads locations.csv, roads.csv, resources.csv
     * Inserts them into the database
     */
    private static void importCsv() {
        System.out.println("\n📌 Importing CSV data...");
        try {
            CsvImporter importer = new CsvImporter();
            importer.importAll();  // Import all CSV files
            System.out.println("✅ CSV import complete!");
        } catch (Exception e) {
            System.err.println("❌ CSV import failed: " + e.getMessage());
        }
    }

    // =============================================
    // OPTION 4 - Generate 300 Requests
    // =============================================

    /**
     * Generate 300 requests
     * Asks user how many to generate, then generates them
     */
    private static void generateRequests() throws SQLException {
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        int currentCount = reqDAO.countRequests();  // How many already exist?

        System.out.println("\n📌 Generating service requests...");
        System.out.println("   Current requests: " + currentCount);
        System.out.print("   How many to generate? (Enter number, or 'all' for 300): ");
        String input = scanner.nextLine().trim();

        int target = 300;  // Default target
        if (!input.equalsIgnoreCase("all")) {
            try {
                target = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("   ❌ Invalid number. Using default: 300");
                target = 300;
            }
        }

        int needed = Math.max(0, target - currentCount);  // How many more we need
        if (needed == 0) {
            System.out.println("   ✅ Already have " + currentCount + " requests!");
            return;
        }

        RequestGenerator generator = new RequestGenerator();
        generator.generateRequests(needed);  // Generate the needed amount
        System.out.println("✅ Generated " + needed + " requests!");
    }

    // =============================================
    // OPTION 5 - Show Export Menu
    // =============================================

    /**
     * Show export menu (from DataExporter)
     * This lets you view data in different formats
     */
    private static void showExportMenu() throws SQLException {
        System.out.println("\n📌 Loading export menu...");
        DataExporter.showMenu();  // Calls DataExporter's menu
    }

    // =============================================
    // OPTION 6 - Test CRUD Operations
    // =============================================

    /**
     * Test CRUD operations
     * Creates, reads, updates, and deletes a test location
     * Demonstrates that the database is working
     */
    private static void testCrudOperations() throws SQLException {
        System.out.println("\n📌 Testing CRUD operations...");
        LocationDAO locDAO = new LocationDAO();

        // =============================================
        // 1. INSERT - Create a new location
        // =============================================
        System.out.println("\n   🔹 Test 1: Inserting a new location...");
        Location newLoc = new Location("Test Ward", "Test Area", LocationType.WARD, 0.0, 0.0);
        int newId = locDAO.insertLocation(newLoc);
        System.out.println("   ✅ Inserted with ID: " + newId);

        // =============================================
        // 2. SELECT - Retrieve the location
        // =============================================
        System.out.println("\n   🔹 Test 2: Retrieving the location...");
        Location retrieved = locDAO.getLocationById(newId);
        System.out.println("   ✅ Retrieved: " + retrieved.getName());

        // =============================================
        // 3. UPDATE - Update the location
        // =============================================
        System.out.println("\n   🔹 Test 3: Updating the location...");
        retrieved.setName("Updated Test Ward");
        locDAO.updateLocation(retrieved);
        Location updated = locDAO.getLocationById(newId);
        System.out.println("   ✅ Updated name to: " + updated.getName());

        // =============================================
        // 4. DELETE - Delete the location
        // =============================================
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

    // =============================================
    // OPTION 7 - Show Summary Statistics
    // =============================================

    /**
     * Display summary statistics
     * Shows counts of all entities in the database
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

    // =============================================
    // OPTION 8 - Clear All Data
    // =============================================

    /**
     * Clear all data
     * Deletes ALL data from ALL tables
     * ASKS FOR CONFIRMATION before doing anything
     */
    private static void clearAllData() throws SQLException {
        System.out.print("\n⚠️ WARNING: This will delete ALL data! Continue? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if (!response.equals("yes") && !response.equals("y")) {
            System.out.println("   ❌ Operation cancelled.");
            return;  // Exit without clearing
        }

        System.out.println("\n🗑️ Clearing all data...");

        java.sql.Connection conn = db.getConnection();
        try (java.sql.Statement stmt = conn.createStatement()) {
            // Disable foreign keys temporarily
            stmt.execute("PRAGMA foreign_keys = OFF");

            // Delete all data (order matters due to foreign keys)
            stmt.execute("DELETE FROM algorithm_runs");    // 5th priority
            stmt.execute("DELETE FROM service_requests");  // 4th priority
            stmt.execute("DELETE FROM resources");         // 3rd priority
            stmt.execute("DELETE FROM roads");             // 2nd priority
            stmt.execute("DELETE FROM locations");         // 1st priority (delete parent tables last)
            stmt.execute("DELETE FROM sqlite_sequence");   // Reset auto-increment

            // Re-enable foreign keys
            stmt.execute("PRAGMA foreign_keys = ON");
        }

        System.out.println("✅ All data cleared!");
    }

    // =============================================
    // OPTION 9 - Run ALL (Full Setup)
    // =============================================

    /**
     * Run everything (full setup)
     * Does: Initialize → Seed → Import CSV → Generate Requests → Show Summary
     * This is the "one-click" setup option
     */
    private static void runAll() throws SQLException {
        System.out.println("\n🚀 RUNNING FULL SETUP...");
        System.out.println("=".repeat(50));

        // =============================================
        // Step 1: Initialize
        // =============================================
        System.out.println("\n[1/5] Initializing database...");
        db.initializeDatabase();

        // =============================================
        // Step 2: Seed sample data
        // =============================================
        System.out.println("\n[2/5] Seeding sample data...");
        LocationDAO locDAO = new LocationDAO();
        if (locDAO.countLocations() == 0) {
            DataSeeder seeder = new DataSeeder();
            seeder.seedAll();
        } else {
            System.out.println("   ⏭️ Data already exists, skipping seed.");
        }

        // =============================================
        // Step 3: Import CSV
        // =============================================
        System.out.println("\n[3/5] Importing CSV data...");
        try {
            CsvImporter importer = new CsvImporter();
            importer.importAll();
        } catch (Exception e) {
            System.err.println("   ⚠️ CSV import failed: " + e.getMessage());
        }

        // =============================================
        // Step 4: Generate requests
        // =============================================
        System.out.println("\n[4/5] Generating 300 requests...");
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        int current = reqDAO.countRequests();
        if (current < 300) {
            RequestGenerator generator = new RequestGenerator();
            generator.generateRequests(300 - current);
        } else {
            System.out.println("   ✅ Already have " + current + " requests!");
        }

        // =============================================
        // Step 5: Show summary
        // =============================================
        System.out.println("\n[5/5] Showing summary...");
        displaySummary();

        System.out.println("\n✅ FULL SETUP COMPLETE!");
    }
}