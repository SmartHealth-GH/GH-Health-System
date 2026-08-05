package main.com.ug.optimizer.database;

import main.com.ug.optimizer.model.*;
import main.com.ug.optimizer.model.enums.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Imports data from CSV files into the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class CsvImporter {

    private LocationDAO locationDAO;
    private RoadDAO roadDAO;
    private ServiceRequestDAO requestDAO;
    private ResourceDAO resourceDAO;
    private String dataDirectory;
    private DatabaseConnection dbConnection;

    public CsvImporter() {
        this.locationDAO = new LocationDAO();
        this.roadDAO = new RoadDAO();
        this.requestDAO = new ServiceRequestDAO();
        this.resourceDAO = new ResourceDAO();
        this.dbConnection = DatabaseConnection.getInstance();

        // Try multiple possible paths
        String userDir = System.getProperty("user.dir");
        System.out.println("   Current directory: " + userDir);

        String[] possiblePaths = {
                "GH-Health-System/data/",
                "data/",
                "./GH-Health-System/data/",
                userDir + "/GH-Health-System/data/",
                userDir + "/data/"
        };

        this.dataDirectory = null;
        for (String path : possiblePaths) {
            try {
                java.nio.file.Path testPath = java.nio.file.Paths.get(path + "locations.csv");
                if (java.nio.file.Files.exists(testPath)) {
                    this.dataDirectory = path;
                    System.out.println("   ✅ Found data directory: " + path);
                    break;
                }
            } catch (Exception e) {
                // Ignore and try next
            }
        }

        if (this.dataDirectory == null) {
            this.dataDirectory = "GH-Health-System/data/";
            System.out.println("   ⚠️ Using fallback path: " + this.dataDirectory);
        }
    }

    public CsvImporter(String dataDirectory) {
        this();
        if (dataDirectory != null) {
            this.dataDirectory = dataDirectory;
        }
    }

    /**
     * Import all CSV files
     */
    public void importAll() throws SQLException, IOException {
        System.out.println("📂 Importing CSV data...");
        System.out.println("   Directory: " + dataDirectory);

        // Disable foreign key constraints for bulk import
        dbConnection.disableForeignKeys();

        try {
            importLocations();
            importRoads();
            importResources();
        } finally {
            // Always re-enable foreign keys
            dbConnection.enableForeignKeys();
        }

        System.out.println("✅ CSV import complete!");
        System.out.println("   " + locationDAO.countLocations() + " locations");
        System.out.println("   " + roadDAO.countRoads() + " roads");
        System.out.println("   " + requestDAO.countRequests() + " requests");
        System.out.println("   " + resourceDAO.countResources() + " resources");
    }

    /**
     * Import locations from CSV
     */
    public void importLocations() throws IOException, SQLException {
        String filePath = dataDirectory + "locations.csv";
        List<String[]> rows = readCsv(filePath);

        System.out.println("   📍 Importing locations from: " + filePath);
        int count = 0;
        int skipped = 0;

        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            if (cols.length >= 5) {
                try {
                    String name = cols[0].trim();
                    String area = cols[1].trim();
                    String type = cols[2].trim();
                    double lat = Double.parseDouble(cols[3].trim());
                    double lon = Double.parseDouble(cols[4].trim());

                    if (name.isEmpty()) {
                        skipped++;
                        continue;
                    }

                    Location location = new Location(name, area, type, lat, lon);
                    locationDAO.insertLocation(location);
                    count++;
                } catch (Exception e) {
                    System.err.println("      ⚠️ Error parsing row " + i + ": " + e.getMessage());
                    skipped++;
                }
            } else {
                skipped++;
            }
        }

        System.out.println("      ✅ Imported " + count + " locations");
        if (skipped > 0) {
            System.out.println("      ⚠️ Skipped " + skipped + " invalid rows");
        }
    }

    /**
     * Import roads from CSV
     */
    public void importRoads() throws IOException, SQLException {
        String filePath = dataDirectory + "roads.csv";
        List<String[]> rows = readCsv(filePath);

        System.out.println("   🛤️ Importing roads from: " + filePath);
        int count = 0;
        int skipped = 0;

        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            if (cols.length >= 5) {
                try {
                    int fromId = Integer.parseInt(cols[0].trim());
                    int toId = Integer.parseInt(cols[1].trim());
                    double distance = Double.parseDouble(cols[2].trim());
                    int travelTime = Integer.parseInt(cols[3].trim());
                    double condition = Double.parseDouble(cols[4].trim());

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

    /**
     * Import resources from CSV
     */
    public void importResources() throws IOException, SQLException {
        String filePath = dataDirectory + "resources.csv";
        List<String[]> rows = readCsv(filePath);

        System.out.println("   🚑 Importing resources from: " + filePath);
        int count = 0;
        int skipped = 0;

        for (int i = 1; i < rows.size(); i++) {
            String[] cols = rows.get(i);
            if (cols.length >= 4) {
                try {
                    String type = cols[0].trim();
                    int homeId = Integer.parseInt(cols[1].trim());
                    int capacity = Integer.parseInt(cols[2].trim());
                    String status = cols[3].trim();

                    if (type.isEmpty()) {
                        skipped++;
                        continue;
                    }

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

    /**
     * Read CSV file and return rows as String arrays
     */
    private List<String[]> readCsv(String filePath) throws IOException {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = parseCsvLine(line);
                rows.add(cols);
            }
        }

        return rows;
    }

    /**
     * Parse a CSV line, handling quoted values with commas
     */
    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (char c : line.toCharArray()) {
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString().trim());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString().trim());

        return values.toArray(new String[0]);
    }
}