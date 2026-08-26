package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.Location;           // The Location model class
import main.com.ug.optimizer.model.enums.LocationType; // The LocationType enum

import java.sql.*;                    // For JDBC (Connection, PreparedStatement, ResultSet)
import java.util.ArrayList;           // For creating lists of Location objects
import java.util.List;                // For the List interface

/**
 * Data Access Object for Location entities
 * Handles all database operations for locations
 *
 * DAO = Data Access Object - handles database operations for a specific entity
 * This class is responsible for all CRUD operations on the locations table
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class LocationDAO {

    // =============================================
    // FIELDS
    // =============================================

    private DatabaseConnection dbConnection; // Manages database connection

    // =============================================
    // CONSTRUCTOR
    // =============================================

    /**
     * Constructor - gets the database connection instance
     * Uses Singleton pattern to ensure only one connection exists
     */
    public LocationDAO() {
        this.dbConnection = DatabaseConnection.getInstance(); // Get the single connection
    }

    // =============================================
    // CREATE - Insert a new location
    // =============================================

    /**
     * Insert a new location into the database
     * This is the CREATE operation (C in CRUD)
     *
     * @param location The Location object to insert
     * @return The auto-generated ID of the new location
     * @throws SQLException if there's a database error
     */
    public int insertLocation(Location location) throws SQLException {
        // SQL INSERT statement with placeholders (?)
        String sql = "INSERT INTO locations (name, area, type, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        // Try-with-resources: automatically closes PreparedStatement
        // Statement.RETURN_GENERATED_KEYS tells JDBC to return the auto-generated ID
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the values for each placeholder
            pstmt.setString(1, location.getName());              // Location name
            pstmt.setString(2, location.getArea());              // Area (e.g., "Korle-Bu")
            pstmt.setString(3, location.getType().name());       // Type (e.g., "EMERGENCY")
            pstmt.setDouble(4, location.getLatitude());          // GPS latitude
            pstmt.setDouble(5, location.getLongitude());         // GPS longitude

            pstmt.executeUpdate(); // Execute the INSERT

            // Get the auto-generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;  // Return ID or -1 if failed
        }
    }

    // =============================================
    // READ - Get all locations
    // =============================================

    /**
     * Get all locations from the database
     * Ordered by locationId (ascending)
     * This is the READ operation (R in CRUD)
     *
     * @return List of all Location objects
     * @throws SQLException if there's a database error
     */
    public List<Location> getAllLocations() throws SQLException {
        List<Location> locations = new ArrayList<>(); // Create empty list

        // SQL SELECT statement - ordered by ID
        String sql = "SELECT * FROM locations ORDER BY locationId";

        // Try-with-resources: automatically closes Statement and ResultSet
        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through each row in the result set
            while (rs.next()) {
                locations.add(mapLocation(rs)); // Convert row to Location object
            }
        }
        return locations; // Return the list of all locations
    }

    // =============================================
    // READ - Get location by ID
    // =============================================

    /**
     * Get a specific location by its ID
     *
     * @param id The location ID to search for
     * @return The Location object, or null if not found
     * @throws SQLException if there's a database error
     */
    public Location getLocationById(int id) throws SQLException {
        // SQL SELECT with WHERE clause for specific ID
        String sql = "SELECT * FROM locations WHERE locationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapLocation(rs); // Return the Location object
            }
        }
        return null; // Not found
    }

    // =============================================
    // UPDATE - Update an existing location
    // =============================================

    /**
     * Update an existing location in the database
     * This is the UPDATE operation (U in CRUD)
     *
     * @param location The Location object with updated values
     * @throws SQLException if there's a database error
     */
    public void updateLocation(Location location) throws SQLException {
        // SQL UPDATE statement
        String sql = "UPDATE locations SET name = ?, area = ?, type = ?, latitude = ?, longitude = ? WHERE locationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            // Set all the updated values
            pstmt.setString(1, location.getName());           // Updated name
            pstmt.setString(2, location.getArea());           // Updated area
            pstmt.setString(3, location.getType().name());    // Updated type
            pstmt.setDouble(4, location.getLatitude());       // Updated latitude
            pstmt.setDouble(5, location.getLongitude());      // Updated longitude
            pstmt.setInt(6, location.getLocationId());        // Which location to update (WHERE clause)

            pstmt.executeUpdate(); // Execute the UPDATE
        }
    }

    // =============================================
    // DELETE - Delete a location by ID
    // =============================================

    /**
     * Delete a location by its ID
     * This is the DELETE operation (D in CRUD)
     *
     * @param id The ID of the location to delete
     * @throws SQLException if there's a database error
     */
    public void deleteLocation(int id) throws SQLException {
        // SQL DELETE statement
        String sql = "DELETE FROM locations WHERE locationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            pstmt.executeUpdate(); // Execute the DELETE
        }
    }

    // =============================================
    // READ - Get locations by area
    // =============================================

    /**
     * Get locations by area (e.g., all locations in "Korle-Bu")
     *
     * @param area The area to search for
     * @return List of Location objects in that area
     * @throws SQLException if there's a database error
     */
    public List<Location> getLocationsByArea(String area) throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations WHERE area = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, area); // Set the area parameter
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        }
        return locations;
    }

    // =============================================
    // READ - Get locations by type
    // =============================================

    /**
     * Get locations by type (e.g., all EMERGENCY locations)
     *
     * @param type The LocationType to search for
     * @return List of Location objects of that type
     * @throws SQLException if there's a database error
     */
    public List<Location> getLocationsByType(LocationType type) throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations WHERE type = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, type.name()); // Set the type name (e.g., "EMERGENCY")
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        }
        return locations;
    }

    // =============================================
    // UTILITY - Count total locations
    // =============================================

    /**
     * Count the total number of locations in the database
     * Useful for statistics and checking if data exists
     *
     * @return The total count of locations
     * @throws SQLException if there's a database error
     */
    public int countLocations() throws SQLException {
        // SQL COUNT query
        String sql = "SELECT COUNT(*) FROM locations";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0; // Return the count, or 0 if no results
        }
    }

    // =============================================
    // MAPPER - Convert ResultSet to Location object
    // =============================================

    /**
     * Map a database row (ResultSet) to a Location object
     * This is a helper method used by all READ operations
     *
     * @param rs The ResultSet containing database row data
     * @return A Location object populated with the data
     * @throws SQLException if there's a database error
     */
    private Location mapLocation(ResultSet rs) throws SQLException {
        Location location = new Location(); // Create a new Location object

        // Set all fields from the ResultSet
        location.setLocationId(rs.getInt("locationId"));          // Database auto-generated ID
        location.setName(rs.getString("name"));                  // Location name
        location.setArea(rs.getString("area"));                  // Area
        location.setType(LocationType.fromString(rs.getString("type"))); // Type (String → ENUM)
        location.setLatitude(rs.getDouble("latitude"));          // GPS latitude
        location.setLongitude(rs.getDouble("longitude"));        // GPS longitude

        return location; // Return the populated object
    }
}