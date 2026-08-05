package main.com.ug.optimizer.database;

import main.com.ug.optimizer.model.*;
import main.com.ug.optimizer.model.enums.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 * Utility class to print/export all data from the database
 * User is prompted to choose what to print
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DataExporter {

    private static Scanner scanner = new Scanner(System.in);

    /**
     * Show the export menu (called from DatabaseTest)
     */
    public static void showMenu() throws SQLException {
        while (true) {
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

            switch (input) {
                case "1":
                    printLocations();
                    break;
                case "2":
                    printRoads();
                    break;
                case "3":
                    printRequests();
                    break;
                case "4":
                    printResources();
                    break;
                case "5":
                    printAllData();
                    break;
                case "6":
                    printSummary();
                    break;
                case "7":
                    exportToCsvPreview();
                    break;
                case "0":
                    System.out.println("   🔙 Returning to main menu...");
                    return;
                default:
                    System.out.println("   ❌ Invalid option. Please try again.");
            }

            System.out.print("\n   Press Enter to continue...");
            scanner.nextLine();
        }
    }

    /**
     * Print all locations
     */
    private static void printLocations() throws SQLException {
        LocationDAO locDAO = new LocationDAO();
        List<Location> locations = locDAO.getAllLocations();

        System.out.println("\n📍 LOCATIONS (" + locations.size() + "):");
        System.out.println("-".repeat(70));
        System.out.printf("  %-4s | %-30s | %-15s | %-12s%n", "ID", "NAME", "AREA", "TYPE");
        System.out.println("-".repeat(70));

        for (Location loc : locations) {
            System.out.printf("  %-4d | %-30s | %-15s | %-12s%n",
                    loc.getLocationId(),
                    truncate(loc.getName(), 30),
                    truncate(loc.getArea(), 15),
                    loc.getType().getDisplayName()
            );
        }
        System.out.println("-".repeat(70));
        System.out.println("  ✅ Total: " + locations.size() + " locations");
    }

    /**
     * Print all roads
     */
    private static void printRoads() throws SQLException {
        RoadDAO roadDAO = new RoadDAO();
        List<Road> roads = roadDAO.getAllRoads();

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

    /**
     * Print all service requests (with option to limit)
     */
    private static void printRequests() throws SQLException {
        ServiceRequestDAO reqDAO = new ServiceRequestDAO();
        List<ServiceRequest> requests = reqDAO.getAllRequests();

        System.out.println("\n📋 SERVICE REQUESTS (" + requests.size() + "):");
        System.out.println("-".repeat(80));
        System.out.printf("  %-5s | %-18s | %-10s | %-12s | %-10s%n",
                "ID", "CATEGORY", "URGENCY", "STATUS", "DEADLINE");
        System.out.println("-".repeat(80));

        // Ask user how many to display
        int total = requests.size();
        int maxDisplay = total; // Default: show all

        if (total > 20) {
            System.out.print("  ⚠️ There are " + total + " requests. How many to display? (Enter number, or 'all'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("all")) {
                maxDisplay = total;
            } else {
                try {
                    int userInput = Integer.parseInt(input);
                    maxDisplay = Math.min(userInput, total);
                } catch (NumberFormatException e) {
                    System.out.println("     ⚠️ Invalid input. Showing all " + total + " requests.");
                    maxDisplay = total;
                }
            }
            System.out.println();
        }

        int count = 0;
        for (ServiceRequest req : requests) {
            if (count >= maxDisplay) break;
            System.out.printf("  %-5d | %-18s | %-10s | %-12s | %-10s%n",
                    req.getRequestId(),
                    truncate(req.getCategory(), 18),
                    req.getUrgency().getDisplayName(),
                    req.getStatus().getDisplayName(),
                    req.getDeadline().toString().substring(0, 10)
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

    /**
     * Print all resources
     */
    private static void printResources() throws SQLException {
        ResourceDAO resDAO = new ResourceDAO();
        List<Resource> resources = resDAO.getAllResources();

        System.out.println("\n🚑 RESOURCES (" + resources.size() + "):");
        System.out.println("-".repeat(60));
        System.out.printf("  %-4s | %-18s | %-12s | %-8s%n",
                "ID", "TYPE", "STATUS", "CAPACITY");
        System.out.println("-".repeat(60));

        for (Resource res : resources) {
            System.out.printf("  %-4d | %-18s | %-12s | %-8d%n",
                    res.getResourceId(),
                    truncate(res.getType(), 18),
                    res.getStatusDisplayName(),
                    res.getCapacity()
            );
        }
        System.out.println("-".repeat(60));
        System.out.println("  ✅ Total: " + resources.size() + " resources");
    }

    /**
     * Print ALL data
     */
    private static void printAllData() throws SQLException {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 FULL DATABASE EXPORT");
        System.out.println("=".repeat(70));

        printLocations();
        printRoads();
        printRequests();
        printResources();
        printSummary();
    }

    /**
     * Print summary statistics only
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

    /**
     * Export to CSV (console preview)
     */
    private static void exportToCsvPreview() throws SQLException {
        System.out.println("\n📤 EXPORTING DATA (console preview)...");
        System.out.println("-".repeat(60));

        // Preview first 5 rows of each table
        System.out.println("\n📍 LOCATIONS (first 5 rows):");
        LocationDAO locDAO = new LocationDAO();
        List<Location> locations = locDAO.getAllLocations();
        for (int i = 0; i < Math.min(5, locations.size()); i++) {
            Location loc = locations.get(i);
            System.out.printf("  %d,%s,%s,%s,%.4f,%.4f%n",
                    loc.getLocationId(),
                    loc.getName(),
                    loc.getArea(),
                    loc.getType().name(),
                    loc.getLatitude(),
                    loc.getLongitude()
            );
        }
        if (locations.size() > 5) {
            System.out.println("  ... and " + (locations.size() - 5) + " more");
        }

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
                    req.getUrgency().getValue(),
                    req.getTimeSubmitted().toString(),
                    req.getDeadline().toString(),
                    req.getStatus().name()
            );
        }
        if (requests.size() > 5) {
            System.out.println("  ... and " + (requests.size() - 5) + " more");
        }

        System.out.println("\n  ✅ Export preview complete!");
        System.out.println("  📁 Actual CSV files are in: data/ folder");
    }

    /**
     * Truncate a string to a maximum length
     */
    private static String truncate(String str, int maxLen) {
        if (str == null) return "null";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }
}