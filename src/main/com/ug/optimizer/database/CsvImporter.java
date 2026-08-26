package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.*;                     // All model classes (Location, Road, etc.)
import main.com.ug.optimizer.model.enums.*;               // All enums (LocationType, ResourceStatus, etc.)

import java.io.BufferedReader;      // For reading files line by line
import java.io.FileReader;         // For reading text files
import java.io.IOException;        // For file I/O errors
import java.sql.SQLException;      // For database errors
import java.util.ArrayList;        // For creating lists
import java.util.List;             // For the List interface

/**
 * Imports data from CSV files into the database
 * CSV = Comma Separated Values (like Excel files saved as text)
 *
 * This class is the bridge between your CSV data files and the database.
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class CsvImporter {

    // =============================================
    // FIELDS - DAO objects for each entity type
    // =============================================

    private LocationDAO locationDAO;           // For inserting locations
    private RoadDAO roadDAO;                   // For inserting roads
    private ServiceRequestDAO requestDAO;      // For inserting service requests
    private ResourceDAO resourceDAO;           // For inserting resources
    private String dataDirectory;              // Where are the CSV files stored?
    private DatabaseConnection dbConnection;   // Database connection

    // =============================================
    // CONSTRUCTOR - Finds the CSV files automatically
    // =============================================

    /**
     * Constructor - tries to find where the CSV files are stored
     * Tries multiple possible paths until it finds the files
     */
    public CsvImporter() {
        // Create all DAO objects
        this.locationDAO = new LocationDAO();
        this.roadDAO = new RoadDAO();
        this.requestDAO = new ServiceRequestDAO();
        this.resourceDAO = new ResourceDAO();
        this.dbConnection = DatabaseConnection.getInstance();

        // Get the current working directory (where the program is running)
        String userDir = System.getProperty("user.dir");
        System.out.println("   Current directory: " + userDir);

        // List of possible places where the data folder might be
        String[] possiblePaths = {
                "GH-Health-System/data/",           // Standard project structure
                "data/",                            // If running from root
                "./GH-Health-System/data/",         // Relative path
                userDir + "/GH-Health-System/data/", // Absolute path with project name
                userDir + "/data/"                  // Absolute path without project name
        };

        // Try each path until we find the locations.csv file
        this.dataDirectory = null;
        for (String path : possiblePaths) {
            try {
                java.nio.file.Path testPath = java.nio.file.Paths.get(path + "locations.csv");
                if (java.nio.file.Files.exists(testPath)) {
                    this.dataDirectory = path;
                    System.out.println("   ✅ Found data directory: " + path);
                    break; // Stop searching - we found it!
                }
            } catch (Exception e) {
                // Ignore and try the next path
            }
        }

        // If we couldn't find any path, use a fallback
        if (this.dataDirectory == null) {
            this.dataDirectory = "GH-Health-System/data/";
            System.out.println("   ⚠️ Using fallback path: " + this.dataDirectory);
        }
    }

    /**
     * Constructor with custom data directory path
     * Allows you to specify exactly where the CSV files are
     *
     * @param dataDirectory The path to the folder containing CSV files
     */
    public CsvImporter(String dataDirectory) {
        this(); // Call the default constructor first
        if (dataDirectory != null) {
            this.dataDirectory = dataDirectory;
        }
    }

    // =============================================
    // MAIN METHOD - Import all CSV files
    // =============================================

    /**
     * Import all CSV files into the database
     * This is the main method you call to load everything
     *
     * @throws SQLException if there's a database error
     * @throws IOException if there's a file reading error
     */
    public void importAll() throws SQLException, IOException {
        System.out.println("📂 Importing CSV data...");
        System.out.println("   Directory: " + dataDirectory);

        // Disable foreign key constraints for faster bulk import
        // This prevents "foreign key constraint failed" errors during import
        dbConnection.disableForeignKeys();

        try {
            // Import each CSV file in order
            importLocations();   // First: locations (so roads can reference them)
            importRoads();       // Second: roads (references locations)
            importResources();   // Third: resources (references locations)
            // Note: Requests are handled separately (generated, not imported from CSV)
        } finally {
            // Always re-enable foreign keys when done
            // This is important for data integrity!
            dbConnection.enableForeignKeys();
        }

        // Print summary of what was imported
        System.out.println("✅ CSV import complete!");
        System.out.println("   " + locationDAO.countLocations() + " locations");
        System.out.println("   " + roadDAO.countRoads() + " roads");
        System.out.println("   " + requestDAO.countRequests() + " requests");
        System.out.println("   " + resourceDAO.countResources() + " resources");
    }

    // =============================================
    // IMPORT LOCATIONS - Reads locations.csv
    // =============================================

    /**
     * Import locations from CSV file
     * Reads locations.csv and inserts each row into the database
     *
     * @throws IOException if the file can't be read
     * @throws SQLException if there's a database error
     */
    public void importLocations() throws IOException, SQLException {
        // Build the full file path
        String filePath = dataDirectory + "locations.csv";
        List<String[]> rows = readCsv(filePath); // Read all rows from the file

        System.out.println("   📍 Importing locations from: " + filePath);
        int count = 0;     // How many rows were imported successfully
        int skipped = 0;   // How many rows were skipped (due to errors)

        // Start from index 1 (skip the header row)
        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);

            // Check if we have at least 5 columns (name, area, type, lat, lon)
            if (cols.length >= 5) {
                try {
                    // Extract data from each column
                    String name = cols[0].trim();        // Location name
                    String area = cols[1].trim();        // Area (e.g., "Korle-Bu")
                    String type = cols[2].trim();        // Type (e.g., "EMERGENCY")
                    double lat = Double.parseDouble(cols[3].trim());   // Latitude
                    double lon = Double.parseDouble(cols[4].trim());   // Longitude

                    // Skip if name is empty
                    if (name.isEmpty()) {
                        skipped++;
                        continue;
                    }

                    // Create a Location object and insert it
                    Location location = new Location(name, area, type, lat, lon);
                    locationDAO.insertLocation(location);
                    count++;
                } catch (Exception e) {
                    // If any row has invalid data, print error and skip it
                    System.err.println("      ⚠️ Error parsing row " + i + ": " + e.getMessage());
                    skipped++;
                }
            } else {
                skipped++; // Not enough columns
            }
        }

        System.out.println("      ✅ Imported " + count + " locations");
        if (skipped > 0) {
            System.out.println("      ⚠️ Skipped " + skipped + " invalid rows");
        }
    }

    // =============================================
    // IMPORT ROADS - Reads roads.csv
    // =============================================

    /**
     * Import roads from CSV file
     * Reads roads.csv and inserts each row into the database
     *
     * @throws IOException if the file can't be read
     * @throws SQLException if there's a database error
     */
    public void importRoads() throws IOException, SQLException {
        String filePath = dataDirectory + "roads.csv";
        List<String[]> rows = readCsv(filePath);

        System.out.println("   🛤️ Importing roads from: " + filePath);
        int count = 0;
        int skipped = 0;

        // Start from index 1 (skip header row)
        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);

            // Need 5 columns: fromId, toId, distance, travelTime, condition
            if (cols.length >= 5) {
                try {
                    int fromId = Integer.parseInt(cols[0].trim());       // Starting location ID
                    int toId = Integer.parseInt(cols[1].trim());         // Ending location ID
                    double distance = Double.parseDouble(cols[2].trim()); // Distance in km
                    int travelTime = Integer.parseInt(cols[3].trim());   // Time in minutes
                    double condition = Double.parseDouble(cols[4].trim()); // Condition weight

                    // Create a Road object and insert it
                    Road road = new Road(fromId, toId, distance, travelTime, condition);
                    roadDAO.insertRoad(road);
                    count++;
                } catch (Exception e) {
                    System.err.println("      ⚠️ Error parsing row " + i + ": " + e.getMessage());
                    skipped++;
                }
            } else {
                skipped++;
            }
        }

        System.out.println("      ✅ Imported " + count + " roads");
        if (skipped > 0) {
            System.out.println("      ⚠️ Skipped " + skipped + " invalid rows");
        }
    }

    // =============================================
    // IMPORT RESOURCES - Reads resources.csv
    // =============================================

    /**
     * Import resources from CSV file
     * Reads resources.csv and inserts each row into the database
     *
     * @throws IOException if the file can't be read
     * @throws SQLException if there's a database error
     */
    public void importResources() throws IOException, SQLException {
        String filePath = dataDirectory + "resources.csv";
        List<String[]> rows = readCsv(filePath);

        System.out.println("   🚑 Importing resources from: " + filePath);
        int count = 0;
        int skipped = 0;

        // Start from index 1 (skip header row)
        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);

            // Need 4 columns: type, homeLocationId, capacity, status
            if (cols.length >= 4) {
                try {
                    String type = cols[0].trim();                     // Resource type
                    int homeId = Integer.parseInt(cols[1].trim());    // Home location ID
                    int capacity = Integer.parseInt(cols[2].trim());  // Capacity
                    String status = cols[3].trim();                  // Status (AVAILABLE, etc.)

                    // Skip if type is empty
                    if (type.isEmpty()) {
                        skipped++;
                        continue;
                    }

                    // Create a Resource object and insert it
                    Resource resource = new Resource(type, homeId, capacity, status);
                    resourceDAO.insertResource(resource);
                    count++;
                } catch (Exception e) {
                    System.err.println("      ⚠️ Error parsing row " + i + ": " + e.getMessage());
                    skipped++;
                }
            } else {
                skipped++;
            }
        }

        System.out.println("      ✅ Imported " + count + " resources");
        if (skipped > 0) {
            System.out.println("      ⚠️ Skipped " + skipped + " invalid rows");
        }
    }

    // =============================================
    // HELPER METHODS - Reading CSV files
    // =============================================

    /**
     * Read a CSV file and return all rows as String arrays
     * Each row is split into columns based on commas
     *
     * @param filePath The path to the CSV file
     * @return A list of String arrays, each array is one row
     * @throws IOException if the file can't be read
     */
    private List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();

        // Try-with-resources - automatically closes the file when done
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) continue;

                // Parse the line into columns
                String[] cols = parseCsvLine(line);
                rows.add(cols);
            }
        }

        return rows;
    }

    /**
     * Parse a CSV line, handling quoted values with commas
     *
     * Example: "Emergency Unit","Korle-Bu","EMERGENCY",5.5405,-0.2038
     *
     * This method correctly handles values inside quotes
     * so that "Emergency Unit" is treated as one value, not two
     *
     * @param line One line from the CSV file
     * @return Array of column values
     */
    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        // Loop through each character
        for (char c : line.toCharArray()) {
            if (c == '"') {
                // Toggle quote mode
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                // End of a column - save the value
                values.add(current.toString().trim());
                current = new StringBuilder(); // Reset for next column
            } else {
                // Add character to current value
                current.append(c);
            }
        }
        // Add the last column
        values.add(current.toString().trim());

        return values.toArray(new String[0]);
    }
}