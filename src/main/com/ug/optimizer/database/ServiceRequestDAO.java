package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.ServiceRequest;           // The ServiceRequest model class
import main.com.ug.optimizer.model.enums.RequestStatus;      // RequestStatus enum
import main.com.ug.optimizer.model.enums.UrgencyLevel;       // UrgencyLevel enum

import java.sql.*;                    // For JDBC (Connection, PreparedStatement, ResultSet)
import java.time.LocalDateTime;       // For handling date/time
import java.util.ArrayList;           // For creating lists of ServiceRequest objects
import java.util.List;                // For the List interface

/**
 * Data Access Object for ServiceRequest entities
 * Handles all database operations for service requests
 *
 * DAO = Data Access Object - handles database operations for a specific entity
 * This class is responsible for all CRUD operations on the service_requests table
 * This is the MOST IMPORTANT DAO - requests are the core of the hospital system
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class ServiceRequestDAO {

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
    public ServiceRequestDAO() {
        this.dbConnection = DatabaseConnection.getInstance(); // Get the single connection
    }

    // =============================================
    // CREATE - Insert a new service request
    // =============================================

    /**
     * Insert a new service request into the database
     * This is the CREATE operation (C in CRUD)
     *
     * @param request The ServiceRequest object to insert
     * @return The auto-generated ID of the new request
     * @throws SQLException if there's a database error
     */
    public int insertRequest(ServiceRequest request) throws SQLException {
        // SQL INSERT statement with placeholders (?)
        String sql = "INSERT INTO service_requests (sourceId, destinationId, category, urgency, timeSubmitted, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        // Try-with-resources: automatically closes PreparedStatement
        // Statement.RETURN_GENERATED_KEYS tells JDBC to return the auto-generated ID
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set the values for each placeholder
            pstmt.setInt(1, request.getSourceId());                // Where the request comes from
            pstmt.setInt(2, request.getDestinationId());           // Where it needs to go
            pstmt.setString(3, request.getCategory());             // Type of request (e.g., "Emergency")
            pstmt.setInt(4, request.getUrgency().getValue());      // Urgency (1-5) - convert enum to int
            pstmt.setString(5, request.getTimeSubmitted().toString()); // When it was submitted
            pstmt.setString(6, request.getDeadline().toString());  // When it must be completed
            pstmt.setString(7, request.getStatus().name());        // Status (PENDING, etc.)

            pstmt.executeUpdate(); // Execute the INSERT

            // Get the auto-generated ID
            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;  // Return ID or -1 if failed
        }
    }

    // =============================================
    // READ - Get all service requests
    // =============================================

    /**
     * Get all service requests from the database
     * Ordered by urgency DESC (highest first) then timeSubmitted
     * This is the READ operation (R in CRUD)
     *
     * @return List of all ServiceRequest objects
     * @throws SQLException if there's a database error
     */
    public List<ServiceRequest> getAllRequests() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>(); // Create empty list

        // SQL SELECT statement - ordered by urgency (highest first) then time
        String sql = "SELECT * FROM service_requests ORDER BY urgency DESC, timeSubmitted";

        // Try-with-resources: automatically closes Statement and ResultSet
        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through each row in the result set
            while (rs.next()) {
                requests.add(mapRequest(rs)); // Convert row to ServiceRequest object
            }
        }
        return requests; // Return the list of all requests
    }

    // =============================================
    // READ - Get request by ID
    // =============================================

    /**
     * Get a specific service request by its ID
     *
     * @param id The request ID to search for
     * @return The ServiceRequest object, or null if not found
     * @throws SQLException if there's a database error
     */
    public ServiceRequest getRequestById(int id) throws SQLException {
        // SQL SELECT with WHERE clause for specific ID
        String sql = "SELECT * FROM service_requests WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapRequest(rs); // Return the ServiceRequest object
            }
        }
        return null; // Not found
    }

    // =============================================
    // UPDATE - Update request status only
    // =============================================

    /**
     * Update a request's status only
     * This is a specialized update (doesn't change other fields)
     *
     * @param requestId The ID of the request to update
     * @param status The new status
     * @throws SQLException if there's a database error
     */
    public void updateRequestStatus(int requestId, RequestStatus status) throws SQLException {
        // SQL UPDATE statement - only updates the status
        String sql = "UPDATE service_requests SET status = ? WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name()); // New status (e.g., "IN_PROGRESS")
            pstmt.setInt(2, requestId);        // Which request to update
            pstmt.executeUpdate(); // Execute the UPDATE
        }
    }

    // =============================================
    // UPDATE - Update full request
    // =============================================

    /**
     * Update an existing service request in the database (all fields)
     * This is the UPDATE operation (U in CRUD)
     *
     * @param request The ServiceRequest object with updated values
     * @throws SQLException if there's a database error
     */
    public void updateRequest(ServiceRequest request) throws SQLException {
        // SQL UPDATE statement - updates all fields
        String sql = "UPDATE service_requests SET sourceId = ?, destinationId = ?, category = ?, urgency = ?, timeSubmitted = ?, deadline = ?, status = ? WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            // Set all the updated values
            pstmt.setInt(1, request.getSourceId());                // Updated source
            pstmt.setInt(2, request.getDestinationId());           // Updated destination
            pstmt.setString(3, request.getCategory());             // Updated category
            pstmt.setInt(4, request.getUrgency().getValue());      // Updated urgency
            pstmt.setString(5, request.getTimeSubmitted().toString()); // Updated submission time
            pstmt.setString(6, request.getDeadline().toString());  // Updated deadline
            pstmt.setString(7, request.getStatus().name());        // Updated status
            pstmt.setInt(8, request.getRequestId());               // Which request to update (WHERE clause)

            pstmt.executeUpdate(); // Execute the UPDATE
        }
    }

    // =============================================
    // READ - Get only pending requests
    // =============================================

    /**
     * Get only requests that are PENDING
     * Useful for showing what needs to be done
     *
     * @return List of pending ServiceRequest objects
     * @throws SQLException if there's a database error
     */
    public List<ServiceRequest> getPendingRequests() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        // SQL SELECT with WHERE clause for PENDING status, ordered by urgency
        String sql = "SELECT * FROM service_requests WHERE status = 'PENDING' ORDER BY urgency DESC";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    // =============================================
    // READ - Get requests by urgency level
    // =============================================

    /**
     * Get requests by urgency level (e.g., all EMERGENCY requests)
     *
     * @param urgency The UrgencyLevel to search for
     * @return List of ServiceRequest objects with that urgency
     * @throws SQLException if there's a database error
     */
    public List<ServiceRequest> getRequestsByUrgency(UrgencyLevel urgency) throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests WHERE urgency = ? ORDER BY timeSubmitted";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, urgency.getValue()); // Set the urgency value (1-5)
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    // =============================================
    // READ - Get requests by status
    // =============================================

    /**
     * Get requests by status (e.g., all COMPLETED requests)
     *
     * @param status The RequestStatus to search for
     * @return List of ServiceRequest objects with that status
     * @throws SQLException if there's a database error
     */
    public List<ServiceRequest> getRequestsByStatus(RequestStatus status) throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests WHERE status = ? ORDER BY urgency DESC";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name()); // Set the status name (e.g., "COMPLETED")
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    // =============================================
    // DELETE - Delete a request by ID
    // =============================================

    /**
     * Delete a service request by its ID
     * This is the DELETE operation (D in CRUD)
     *
     * @param id The ID of the request to delete
     * @throws SQLException if there's a database error
     */
    public void deleteRequest(int id) throws SQLException {
        // SQL DELETE statement
        String sql = "DELETE FROM service_requests WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id); // Set the ID parameter
            pstmt.executeUpdate(); // Execute the DELETE
        }
    }

    // =============================================
    // UTILITY - Count total requests
    // =============================================

    /**
     * Count the total number of service requests in the database
     * Useful for statistics
     *
     * @return The total count of requests
     * @throws SQLException if there's a database error
     */
    public int countRequests() throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // =============================================
    // UTILITY - Count requests by status
    // =============================================

    /**
     * Count requests by status (e.g., how many are PENDING?)
     * Useful for dashboard statistics
     *
     * @param status The RequestStatus to count
     * @return The count of requests with that status
     * @throws SQLException if there's a database error
     */
    public int countByStatus(RequestStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests WHERE status = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name()); // Set the status name
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    // =============================================
    // MAPPER - Convert ResultSet to ServiceRequest object
    // =============================================

    /**
     * Map a database row (ResultSet) to a ServiceRequest object
     * This is a helper method used by all READ operations
     *
     * @param rs The ResultSet containing database row data
     * @return A ServiceRequest object populated with the data
     * @throws SQLException if there's a database error
     */
    private ServiceRequest mapRequest(ResultSet rs) throws SQLException {
        ServiceRequest request = new ServiceRequest(); // Create a new ServiceRequest object

        // Set all fields from the ResultSet
        request.setRequestId(rs.getInt("requestId"));                    // Database auto-generated ID
        request.setSourceId(rs.getInt("sourceId"));                      // Source location ID
        request.setDestinationId(rs.getInt("destinationId"));            // Destination location ID
        request.setCategory(rs.getString("category"));                   // Category (e.g., "Emergency")

        // Convert database int to enum
        request.setUrgency(rs.getInt("urgency"));                        // Urgency (1-5) → UrgencyLevel

        // Parse date/time strings
        request.setTimeSubmitted(LocalDateTime.parse(rs.getString("timeSubmitted"))); // Submission time
        request.setDeadline(LocalDateTime.parse(rs.getString("deadline")));           // Deadline

        // Convert database string to enum
        request.setStatus(RequestStatus.fromString(rs.getString("status"))); // Status

        return request; // Return the populated object
    }
}