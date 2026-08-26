package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.*;                     // All model classes
import main.com.ug.optimizer.model.enums.*;               // All enums

import java.sql.SQLException;        // For database errors
import java.util.List;               // For lists of data
import java.util.Scanner;            // For reading user input

/**
 * Utility class to print/export all data from the database
 * User is prompted to choose what to print
 *
 * This is the "view data" tool - shows what's in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DataExporter {

    // =============================================
    // FIELDS
    // =============================================

    private static Scanner scanner = new Scanner(System.in); // For user input

    // =============================================
    // MAIN MENU - Export options
    // =============================================

    /**
     * Show the export menu (called from DatabaseTest)
     * This is the entry point for viewing data
     */
    public static void showMenu() throws SQLException {
        while (true) {  // Infinite loop until user chooses "Back"
            System.out.println("\n" + "=".repeat(50));
            System.out.println("📋 EXPORT MENU");
            System.out.println("=".repeat(50));
            System.out.println("  [1] Print all Locations");
            System.out.println("  [2] Print all Roads");
            System.out.println("  [3] Print all Service Requests");
            System.out.println("  [4] Print all Resources");
            System.out.println("  [5] Print ALL data (everything)");
            System.out.println("  [6] Print Summary Statistics Only");
            System.out.println("  [7] Export to CSV (console preview)");
            System.out.println("  [0] Back to Main Menu");
            System.out.println("=".repeat(50));
            System.out.print("  Choose an option: ");

            String input = scanner.nextLine().trim();

            // =============================================
            // Handle each option
            // =============================================

            switch (input) {
                case "1":
                    printLocations();        // Show all locations
                    break;
                case "2":
                    printRoads();            // Show all roads
                    break;
                case "3":
                    printRequests();         // Show all requests
                    break;
                case "4":
                    printResources();        // Show all resources
                    break;
                case "5":
                    printAllData();          // Show EVERYTHING
                    break;
                case "6":
                    printSummary();          // Show summary only
                    break;
                case "7":
                    exportToCsvPreview();    // CSV preview
                    break;
                case "0":
                    System.out.println("   🔙 Returning to main menu...");
                    return;                  // Exit the menu
                default:
                    System.out.println("   ❌ Invalid option. Please try again.");
            }

            System.out.print("\n   Press Enter to continue...");
            scanner.nextLine();
        }
    }

    // =============================================
    // OPTION 1 - Print all Locations
    // =============================================

    /**
     * Print all locations in a formatted table
     * Shows: ID, NAME, AREA, TYPE
     */
    private static void printLocations() throws SQLException {
        LocationDAO locDAO = new LocationDAO();
        List<Location> locations = locDAO.getAllLocations();  // Get all locations

        // Print header
        System.out.println("\n📍 LOCATIONS (" + locations.size() + "):");
        System.out.println("-".repeat(70));
        System.out.printf("  %-4s | %-30s | %-15s | %-12s%n", "ID", "NAME", "AREA", "TYPE");
        System.out.println("-".repeat(70));

        // Print each location
        for (Location loc : locations) {
            System.out.printf("  %-4d | %-30s | %-15s | %-12s%n",
                    loc.getLocationId(),
                    truncate(loc.getName(), 30),       // Truncate long names
                    truncate(loc.getArea(), 15),       // Truncate long areas
                    loc.getType().getDisplayName()      // e.g., "emergency"
            );
        }
        System.out.println("-".repeat(70));
        System.out.println("  ✅ Total: " + locations.size() + " locations");
    }

    // =============================================
    // OPTION 2 - Print all Roads
    // =============================================

    /**
     * Print all roads in a formatted table
     * Shows: ID, FROM, TO, DISTANCE, TIME, CONDITION
     */
    private static void printRoads() throws SQLException {
        RoadDAO roadDAO = new RoadDAO();
        List<Road> roads = roadDAO.getAllRoads();  // Get all roads

        System.out.println("\n🛤️ ROADS (" + roads.size() + "):");
        System.out.println("-".repeat(60));
        System.out.printf("  %-4s | %-8s | %-8s | %-8s | %-6s | %-6s%n",
                "ID", "FROM", "TO", "DISTANCE", "TIME", "COND");
        System.out.println("-".repeat(60));

        for (Road road : roads) {
            System.out.printf("  %-4d | %-8d | %-8d | %8.2f | %6d | %6.2f%n",
                    road.getRoadId(),
                    road.getFromLocationId(),
                    road.getToLocationId(),
                    road.getDistance(),
                    road.getTravelTime(),
                    road.getRoadConditionWeight()
            );
        }
        System.out.println("-".repeat(60));
        System.out.println("  ✅ Total: " + roads.size() + " roads");
    }

    // =============================================
    // OPTION 3 - Print all Service Requests
    // =============================================

    /**
     * Print all service requests
     * Asks user how many to display (for large datasets)
     * Shows: ID, CATEGORY, URGENCY, STATUS, DEADLINE
     */
    private static void printRequests() throws SQLException {
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        List<ServiceRequest> requests = reqDAO.getAllRequests();  // Get all requests

        System.out.println("\n📋 SERVICE REQUESTS (" + requests.size() + "):");
        System.out.println("-".repeat(80));
        System.out.printf("  %-5s | %-18s | %-10s | %-12s | %-10s%n",
                "ID", "CATEGORY", "URGENCY", "STATUS", "DEADLINE");
        System.out.println("-".repeat(80));

        // =============================================
        // Ask user how many to display
        // =============================================
        int total = requests.size();
        int maxDisplay = total; // Default: show all

        if (total > 20) {  // If more than 20, ask the user
            System.out.print("  ⚠️ There are " + total + " requests. How many to display? (Enter number, or 'all'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("all")) {
                maxDisplay = total;  // Show all
            } else {
                try {
                    int userInput = Integer.parseInt(input);
                    maxDisplay = Math.min(userInput, total);  // Show limited number
                } catch (NumberFormatException e) {
                    System.out.println("     ⚠️ Invalid input. Showing all " + total + " requests.");
                    maxDisplay = total;
                }
            }
            System.out.println();
        }

        // Print the requests
        int count = 0;
        for (ServiceRequest req : requests) {
            if (count >= maxDisplay) break;
            System.out.printf("  %-5d | %-18s | %-10s | %-12s | %-10s%n",
                    req.getRequestId(),
                    truncate(req.getCategory(), 18),
                    req.getUrgency().getDisplayName(),    // e.g., "Emergency"
                    req.getStatus().getDisplayName(),     // e.g., "Pending"
                    req.getDeadline().toString().substring(0, 10)  // Show only date
            );
            count++;
        }

        System.out.println("-".repeat(80));
        if (maxDisplay < total) {
            System.out.println("  ✅ Showing " + maxDisplay + " of " + total + " requests");
        } else {
            System.out.println("  ✅ Total: " + total + " requests (ALL displayed)");
        }
    }

    // =============================================
    // OPTION 4 - Print all Resources
    // =============================================

    /**
     * Print all resources in a formatted table
     * Shows: ID, TYPE, STATUS, CAPACITY
     */
    private static void printResources() throws SQLException {
        ResourceDAO resDAO = new ResourceDAO();
        List<Resource> resources = resDAO.getAllResources();  // Get all resources

        System.out.println("\n🚑 RESOURCES (" + resources.size() + "):");
        System.out.println("-".repeat(60));
        System.out.printf("  %-4s | %-18s | %-12s | %-8s%n",
                "ID", "TYPE", "STATUS", "CAPACITY");
        System.out.println("-".repeat(60));

        for (Resource res : resources) {
            System.out.printf("  %-4d | %-18s | %-12s | %-8d%n",
                    res.getResourceId(),
                    truncate(res.getType(), 18),
                    res.getStatusDisplayName(),    // e.g., "Available"
                    res.getCapacity()
            );
        }
        System.out.println("-".repeat(60));
        System.out.println("  ✅ Total: " + resources.size() + " resources");
    }

    // =============================================
    // OPTION 5 - Print ALL Data
    // =============================================

    /**
     * Print ALL data (everything in the database)
     * Calls all the print methods in sequence
     */
    private static void printAllData() throws SQLException {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 FULL DATABASE EXPORT");
        System.out.println("=".repeat(70));

        printLocations();   // Show locations
        printRoads();       // Show roads
        printRequests();    // Show requests
        printResources();   // Show resources
        printSummary();     // Show summary
    }

    // =============================================
    // OPTION 6 - Print Summary Statistics
    // =============================================

    /**
     * Print summary statistics only
     * Shows counts of all entities
     * Does NOT show individual records
     */
    private static void printSummary() throws SQLException {
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
        System.out.printf("  📋 Pending Requests:   %d%n", reqDAO.countByStatus(RequestStatus.PENDING));
        System.out.printf("  📋 Completed Requests: %d%n", reqDAO.countByStatus(RequestStatus.COMPLETED));
        System.out.printf("  📋 In Progress:        %d%n", reqDAO.countByStatus(RequestStatus.IN_PROGRESS));
        System.out.printf("  🚑 Total Resources:    %d%n", resDAO.countResources());
        System.out.printf("  🚑 Available:          %d%n", resDAO.countAvailableResources());
        System.out.println("=".repeat(60));
    }

    // =============================================
    // OPTION 7 - Export to CSV Preview
    // =============================================

    /**
     * Export to CSV (console preview)
     * Shows the first 5 rows of each table in CSV format
     * This is a PREVIEW only - actual CSV files are in data/ folder
     */
    private static void exportToCsvPreview() throws SQLException {
        System.out.println("\n📤 EXPORTING DATA (console preview)...");
        System.out.println("-".repeat(60));

        // =============================================
        // Preview LOCATIONS (first 5 rows)
        // =============================================
        System.out.println("\n📍 LOCATIONS (first 5 rows):");
        LocationDAO locDAO = new LocationDAO();
        List<Location> locations = locDAO.getAllLocations();
        for (int i = 0; i < Math.min(5, locations.size()); i++) {
            Location loc = locations.get(i);
            System.out.printf("  %d,%s,%s,%s,%.4f,%.4f%n",
                    loc.getLocationId(),
                    loc.getName(),
                    loc.getArea(),
                    loc.getType().name(),       // ENUM name (e.g., "EMERGENCY")
                    loc.getLatitude(),
                    loc.getLongitude()
            );
        }
        if (locations.size() > 5) {
            System.out.println("  ... and " + (locations.size() - 5) + " more");
        }

        // =============================================
        // Preview REQUESTS (first 5 rows)
        // =============================================
        System.out.println("\n📋 REQUESTS (first 5 rows):");
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        List<ServiceRequest> requests = reqDAO.getAllRequests();
        for (int i = 0; i < Math.min(5, requests.size()); i++) {
            ServiceRequest req = requests.get(i);
            System.out.printf("  %d,%d,%d,%s,%d,%s,%s,%s%n",
                    req.getRequestId(),
                    req.getSourceId(),
                    req.getDestinationId(),
                    req.getCategory(),
                    req.getUrgency().getValue(),    // Numeric value (1-5)
                    req.getTimeSubmitted().toString(),
                    req.getDeadline().toString(),
                    req.getStatus().name()          // ENUM name
            );
        }
        if (requests.size() > 5) {
            System.out.println("  ... and " + (requests.size() - 5) + " more");
        }

        System.out.println("\n  ✅ Export preview complete!");
        System.out.println("  📁 Actual CSV files are in: data/ folder");
    }

    // =============================================
    // HELPER METHOD - Truncate strings
    // =============================================

    /**
     * Truncate a string to a maximum length
     * Adds "..." if the string is too long
     * Prevents table formatting from breaking
     *
     * @param str The string to truncate
     * @param maxLen Maximum length allowed
     * @return Truncated string
     */
    private static String truncate(String str, int maxLen) {
        if (str == null) return "null";           // Handle null values
        if (str.length() <= maxLen) return str;   // Return as-is if short
        return str.substring(0, maxLen - 3) + "...";  // Truncate and add "..."
    }
}