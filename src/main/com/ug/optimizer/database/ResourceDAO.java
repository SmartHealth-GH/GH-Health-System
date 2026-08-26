package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.Resource;           // The Resource model class
import main.com.ug.optimizer.model.enums.ResourceStatus; // The ResourceStatus enum

import java.sql.*;                    // For JDBC (Connection, PreparedStatement, ResultSet)
import java.util.ArrayList;           // For creating lists of Resource objects
import java.util.List;                // For the List interface

/**
 * Data Access Object for Resource entities
 * Handles all database operations for resources
 *
 * DAO = Data Access Object - handles database operations for a specific entity
 * This class is responsible for all CRUD operations on the resources table
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class ResourceDAO {

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
    public ResourceDAO() {
        this.dbConnection = DatabaseConnection.getInstance(); // Get the single connection
    }

    // =============================================
    // CREATE - Insert a new resource
    // =============================================

    /**
     * Insert a new resource into the database
     * This is the CREATE operation (C in CRUD)
     *
     * @param resource The Resource object to insert
     * @return The auto-generated ID of the new resource
     * @throws SQLException if there's a database error
     */
    public int insertResource(Resource resource) throws SQLException {
        // SQL INSERT statement with placeholders (?)
        String sql = "INSERT INTO resources (type, homeLocationId, capacity, availabilityStatus) VALUES (?, ?, ?, ?)";

        // Try-with-resources: automatically closes PreparedStatement
        // Statement.RETURN_GENERATED_KEYS tells JDBC to return the auto-generated ID
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the values for each placeholder
            pstmt.setString(1, resource.getType());                     // Resource type (e.g., "Ambulance")
            pstmt.setInt(2, resource.getHomeLocationId());              // Where it's based (Location ID)
            pstmt.setInt(3, resource.getCapacity());                   // How many it can handle
            pstmt.setString(4, resource.getAvailabilityStatus().name()); // Status (e.g., "AVAILABLE")

            pstmt.executeUpdate(); // Execute the INSERT

            // Get the auto-generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;  // Return ID or -1 if failed
        }
    }

    // =============================================
    // READ - Get all resources
    // =============================================

    /**
     * Get all resources from the database
     * Ordered by resourceId (ascending)
     * This is the READ operation (R in CRUD)
     *
     * @return List of all Resource objects
     * @throws SQLException if there's a database error
     */
    public List<Resource> getAllResources() throws SQLException {
        List<Resource> resources = new ArrayList<>(); // Create empty list

        // SQL SELECT statement - ordered by ID
        String sql = "SELECT * FROM resources ORDER BY resourceId";

        // Try-with-resources: automatically closes Statement and ResultSet
        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through each row in the result set
            while (rs.next()) {
                resources.add(mapResource(rs)); // Convert row to Resource object
            }
        }
        return resources; // Return the list of all resources
    }

    // =============================================
    // READ - Get resource by ID
    // =============================================

    /**
     * Get a specific resource by its ID
     *
     * @param id The resource ID to search for
     * @return The Resource object, or null if not found
     * @throws SQLException if there's a database error
     */
    public Resource getResourceById(int id) throws SQLException {
        // SQL SELECT with WHERE clause for specific ID
        String sql = "SELECT * FROM resources WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResource(rs); // Return the Resource object
            }
        }
        return null; // Not found
    }

    // =============================================
    // UPDATE - Update resource status only
    // =============================================

    /**
     * Update a resource's availability status only
     * This is a specialized update (doesn't change other fields)
     *
     * @param resourceId The ID of the resource to update
     * @param status The new status
     * @throws SQLException if there's a database error
     */
    public void updateResourceStatus(int resourceId, ResourceStatus status) throws SQLException {
        // SQL UPDATE statement - only updates the status
        String sql = "UPDATE resources SET availabilityStatus = ? WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name()); // New status (e.g., "IN_USE")
            pstmt.setInt(2, resourceId);       // Which resource to update
            pstmt.executeUpdate(); // Execute the UPDATE
        }
    }

    // =============================================
    // UPDATE - Update full resource
    // =============================================

    /**
     * Update an existing resource in the database (all fields)
     * This is the UPDATE operation (U in CRUD)
     *
     * @param resource The Resource object with updated values
     * @throws SQLException if there's a database error
     */
    public void updateResource(Resource resource) throws SQLException {
        // SQL UPDATE statement - updates all fields
        String sql = "UPDATE resources SET type = ?, homeLocationId = ?, capacity = ?, availabilityStatus = ? WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            // Set all the updated values
            pstmt.setString(1, resource.getType());                     // Updated type
            pstmt.setInt(2, resource.getHomeLocationId());              // Updated home location
            pstmt.setInt(3, resource.getCapacity());                   // Updated capacity
            pstmt.setString(4, resource.getAvailabilityStatus().name()); // Updated status
            pstmt.setInt(5, resource.getResourceId());                 // Which resource to update (WHERE clause)

            pstmt.executeUpdate(); // Execute the UPDATE
        }
    }

    // =============================================
    // READ - Get only available resources
    // =============================================

    /**
     * Get only resources that are AVAILABLE
     * Useful for finding resources that can be assigned
     *
     * @return List of available Resource objects
     * @throws SQLException if there's a database error
     */
    public List<Resource> getAvailableResources() throws SQLException {
        List<Resource> resources = new ArrayList<>();
        // SQL SELECT with WHERE clause for AVAILABLE status
        String sql = "SELECT * FROM resources WHERE availabilityStatus = 'AVAILABLE'";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    // =============================================
    // READ - Get resources by type
    // =============================================

    /**
     * Get resources by type (e.g., all "Ambulance" resources)
     *
     * @param type The resource type to search for
     * @return List of Resource objects of that type
     * @throws SQLException if there's a database error
     */
    public List<Resource> getResourcesByType(String type) throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE type = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, type); // Set the type parameter
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    // =============================================
    // READ - Get resources by status
    // =============================================

    /**
     * Get resources by status (e.g., all IN_USE resources)
     *
     * @param status The ResourceStatus to search for
     * @return List of Resource objects with that status
     * @throws SQLException if there's a database error
     */
    public List<Resource> getResourcesByStatus(ResourceStatus status) throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE availabilityStatus = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name()); // Set the status name (e.g., "IN_USE")
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    // =============================================
    // DELETE - Delete a resource by ID
    // =============================================

    /**
     * Delete a resource by its ID
     * This is the DELETE operation (D in CRUD)
     *
     * @param id The ID of the resource to delete
     * @throws SQLException if there's a database error
     */
    public void deleteResource(int id) throws SQLException {
        // SQL DELETE statement
        String sql = "DELETE FROM resources WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            pstmt.executeUpdate(); // Execute the DELETE
        }
    }

    // =============================================
    // UTILITY - Count total resources
    // =============================================

    /**
     * Count the total number of resources in the database
     * Useful for statistics
     *
     * @return The total count of resources
     * @throws SQLException if there's a database error
     */
    public int countResources() throws SQLException {
        String sql = "SELECT COUNT(*) FROM resources";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // =============================================
    // UTILITY - Count available resources
    // =============================================

    /**
     * Count only resources that are AVAILABLE
     * Useful for capacity planning
     *
     * @return The count of available resources
     * @throws SQLException if there's a database error
     */
    public int countAvailableResources() throws SQLException {
        String sql = "SELECT COUNT(*) FROM resources WHERE availabilityStatus = 'AVAILABLE'";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // =============================================
    // MAPPER - Convert ResultSet to Resource object
    // =============================================

    /**
     * Map a database row (ResultSet) to a Resource object
     * This is a helper method used by all READ operations
     *
     * @param rs The ResultSet containing database row data
     * @return A Resource object populated with the data
     * @throws SQLException if there's a database error
     */
    private Resource mapResource(ResultSet rs) throws SQLException {
        Resource resource = new Resource(); // Create a new Resource object

        // Set all fields from the ResultSet
        resource.setResourceId(rs.getInt("resourceId"));              // Database auto-generated ID
        resource.setType(rs.getString("type"));                      // Resource type
        resource.setHomeLocationId(rs.getInt("homeLocationId"));     // Home location ID
        resource.setCapacity(rs.getInt("capacity"));                 // Capacity

        // Convert database string to enum
        resource.setAvailabilityStatus(ResourceStatus.fromString(rs.getString("availabilityStatus")));

        return resource; // Return the populated object
    }
}