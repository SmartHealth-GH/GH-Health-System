package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.Road;  // The Road model class

import java.sql.*;                    // For JDBC (Connection, PreparedStatement, ResultSet)
import java.util.ArrayList;           // For creating lists of Road objects
import java.util.List;                // For the List interface

/**
 * Data Access Object for Road entities
 * Handles all database operations for roads
 *
 * DAO = Data Access Object - handles database operations for a specific entity
 * This class is responsible for all CRUD operations on the roads table
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class RoadDAO {

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
    public RoadDAO() {
        this.dbConnection = DatabaseConnection.getInstance(); // Get the single connection
    }

    // =============================================
    // CREATE - Insert a new road
    // =============================================

    /**
     * Insert a new road into the database
     * This is the CREATE operation (C in CRUD)
     *
     * @param road The Road object to insert
     * @return The auto-generated ID of the new road
     * @throws SQLException if there's a database error
     */
    public int insertRoad(Road road) throws SQLException {
        // SQL INSERT statement with placeholders (?)
        String sql = "INSERT INTO roads (fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES (?, ?, ?, ?, ?)";

        // Try-with-resources: automatically closes PreparedStatement
        // Statement.RETURN_GENERATED_KEYS tells JDBC to return the auto-generated ID
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the values for each placeholder
            pstmt.setInt(1, road.getFromLocationId());        // Starting location ID
            pstmt.setInt(2, road.getToLocationId());          // Ending location ID
            pstmt.setDouble(3, road.getDistance());           // Distance in kilometers
            pstmt.setInt(4, road.getTravelTime());            // Travel time in minutes
            pstmt.setDouble(5, road.getRoadConditionWeight()); // Road condition weight

            pstmt.executeUpdate(); // Execute the INSERT

            // Get the auto-generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;  // Return ID or -1 if failed
        }
    }

    // =============================================
    // READ - Get all roads
    // =============================================

    /**
     * Get all roads from the database
     * Ordered by roadId (ascending)
     * This is the READ operation (R in CRUD)
     *
     * @return List of all Road objects
     * @throws SQLException if there's a database error
     */
    public List<Road> getAllRoads() throws SQLException {
        List<Road> roads = new ArrayList<>(); // Create empty list

        // SQL SELECT statement - ordered by ID
        String sql = "SELECT * FROM roads ORDER BY roadId";

        // Try-with-resources: automatically closes Statement and ResultSet
        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through each row in the result set
            while (rs.next()) {
                roads.add(mapRoad(rs)); // Convert row to Road object
            }
        }
        return roads; // Return the list of all roads
    }

    // =============================================
    // READ - Get road by ID
    // =============================================

    /**
     * Get a specific road by its ID
     *
     * @param id The road ID to search for
     * @return The Road object, or null if not found
     * @throws SQLException if there's a database error
     */
    public Road getRoadById(int id) throws SQLException {
        // SQL SELECT with WHERE clause for specific ID
        String sql = "SELECT * FROM roads WHERE roadId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRoad(rs); // Return the Road object
            }
        }
        return null; // Not found
    }

    // =============================================
    // UPDATE - Update an existing road
    // =============================================

    /**
     * Update an existing road in the database
     * This is the UPDATE operation (U in CRUD)
     *
     * @param road The Road object with updated values
     * @throws SQLException if there's a database error
     */
    public void updateRoad(Road road) throws SQLException {
        // SQL UPDATE statement - updates all fields
        String sql = "UPDATE roads SET fromLocationId = ?, toLocationId = ?, distance = ?, travelTime = ?, roadConditionWeight = ? WHERE roadId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            // Set all the updated values
            pstmt.setInt(1, road.getFromLocationId());        // Updated starting location
            pstmt.setInt(2, road.getToLocationId());          // Updated ending location
            pstmt.setDouble(3, road.getDistance());           // Updated distance
            pstmt.setInt(4, road.getTravelTime());            // Updated travel time
            pstmt.setDouble(5, road.getRoadConditionWeight()); // Updated condition
            pstmt.setInt(6, road.getRoadId());                // Which road to update (WHERE clause)

            pstmt.executeUpdate(); // Execute the UPDATE
        }
    }

    // =============================================
    // DELETE - Delete a road by ID
    // =============================================

    /**
     * Delete a road by its ID
     * This is the DELETE operation (D in CRUD)
     *
     * @param id The ID of the road to delete
     * @throws SQLException if there's a database error
     */
    public void deleteRoad(int id) throws SQLException {
        // SQL DELETE statement
        String sql = "DELETE FROM roads WHERE roadId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            pstmt.executeUpdate(); // Execute the DELETE
        }
    }

    // =============================================
    // READ - Get roads from a specific location
    // =============================================

    /**
     * Get all roads that connect to a specific location
     * This is useful for finding possible routes from a location
     *
     * @param locationId The location to search for roads from
     * @return List of Road objects connected to that location
     * @throws SQLException if there's a database error
     */
    public List<Road> getRoadsFromLocation(int locationId) throws SQLException {
        List<Road> roads = new ArrayList<>();
        // SQL SELECT with WHERE clause for either from OR to location
        String sql = "SELECT * FROM roads WHERE fromLocationId = ? OR toLocationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, locationId); // Check fromLocationId
            pstmt.setInt(2, locationId); // Check toLocationId
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                roads.add(mapRoad(rs));
            }
        }
        return roads;
    }

    // =============================================
    // UTILITY - Count total roads
    // =============================================

    /**
     * Count the total number of roads in the database
     * Useful for statistics
     *
     * @return The total count of roads
     * @throws SQLException if there's a database error
     */
    public int countRoads() throws SQLException {
        // SQL COUNT query
        String sql = "SELECT COUNT(*) FROM roads";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0; // Return the count, or 0 if no results
        }
    }

    // =============================================
    // MAPPER - Convert ResultSet to Road object
    // =============================================

    /**
     * Map a database row (ResultSet) to a Road object
     * This is a helper method used by all READ operations
     *
     * @param rs The ResultSet containing database row data
     * @return A Road object populated with the data
     * @throws SQLException if there's a database error
     */
    private Road mapRoad(ResultSet rs) throws SQLException {
        Road road = new Road(); // Create a new Road object

        // Set all fields from the ResultSet
        road.setRoadId(rs.getInt("roadId"));                    // Database auto-generated ID
        road.setFromLocationId(rs.getInt("fromLocationId"));    // Starting location ID
        road.setToLocationId(rs.getInt("toLocationId"));        // Ending location ID
        road.setDistance(rs.getDouble("distance"));             // Distance in km
        road.setTravelTime(rs.getInt("travelTime"));            // Travel time in minutes
        road.setRoadConditionWeight(rs.getDouble("roadConditionWeight")); // Condition weight

        return road; // Return the populated object
    }
}