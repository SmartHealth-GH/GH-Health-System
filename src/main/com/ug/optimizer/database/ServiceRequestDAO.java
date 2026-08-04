package main.com.ug.optimizer.database;

import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.RequestStatus;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for ServiceRequest entities
 * Handles all database operations for service requests
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class ServiceRequestDAO {

    private DatabaseConnection dbConnection;

    public ServiceRequestDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Get all service requests
     */
    public List<ServiceRequest> getAllRequests() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests ORDER BY urgency DESC, timeSubmitted";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    /**
     * Get request by ID
     */
    public ServiceRequest getRequestById(int id) throws SQLException {
        String sql = "SELECT * FROM service_requests WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRequest(rs);
            }
        }
        return null;
    }

    /**
     * Insert a new service request
     */
    public int insertRequest(ServiceRequest request) throws SQLException {
        String sql = "INSERT INTO service_requests (sourceId, destinationId, category, urgency, timeSubmitted, deadline, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, request.getSourceId());
            pstmt.setInt(2, request.getDestinationId());
            pstmt.setString(3, request.getCategory());
            pstmt.setInt(4, request.getUrgency().getValue());
            pstmt.setString(5, request.getTimeSubmitted().toString());
            pstmt.setString(6, request.getDeadline().toString());
            pstmt.setString(7, request.getStatus().name());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    /**
     * Update request status only
     */
    public void updateRequestStatus(int requestId, RequestStatus status) throws SQLException {
        String sql = "UPDATE service_requests SET status = ? WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, requestId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Update full request
     */
    public void updateRequest(ServiceRequest request) throws SQLException {
        String sql = "UPDATE service_requests SET sourceId = ?, destinationId = ?, category = ?, urgency = ?, timeSubmitted = ?, deadline = ?, status = ? WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, request.getSourceId());
            pstmt.setInt(2, request.getDestinationId());
            pstmt.setString(3, request.getCategory());
            pstmt.setInt(4, request.getUrgency().getValue());
            pstmt.setString(5, request.getTimeSubmitted().toString());
            pstmt.setString(6, request.getDeadline().toString());
            pstmt.setString(7, request.getStatus().name());
            pstmt.setInt(8, request.getRequestId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Get only pending requests
     */
    public List<ServiceRequest> getPendingRequests() throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests WHERE status = 'PENDING' ORDER BY urgency DESC";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    /**
     * Get requests by urgency level
     */
    public List<ServiceRequest> getRequestsByUrgency(UrgencyLevel urgency) throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests WHERE urgency = ? ORDER BY timeSubmitted";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, urgency.getValue());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    /**
     * Get requests by status
     */
    public List<ServiceRequest> getRequestsByStatus(RequestStatus status) throws SQLException {
        List<ServiceRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM service_requests WHERE status = ? ORDER BY urgency DESC";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                requests.add(mapRequest(rs));
            }
        }
        return requests;
    }

    /**
     * Delete a request by ID
     */
    public void deleteRequest(int id) throws SQLException {
        String sql = "DELETE FROM service_requests WHERE requestId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Count total requests
     */
    public int countRequests() throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Count requests by status
     */
    public int countByStatus(RequestStatus status) throws SQLException {
        String sql = "SELECT COUNT(*) FROM service_requests WHERE status = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Map ResultSet to ServiceRequest object
     */
    private ServiceRequest mapRequest(ResultSet rs) throws SQLException {
        ServiceRequest request = new ServiceRequest();
        request.setRequestId(rs.getInt("requestId"));
        request.setSourceId(rs.getInt("sourceId"));
        request.setDestinationId(rs.getInt("destinationId"));
        request.setCategory(rs.getString("category"));
        request.setUrgency(rs.getInt("urgency"));
        request.setTimeSubmitted(LocalDateTime.parse(rs.getString("timeSubmitted")));
        request.setDeadline(LocalDateTime.parse(rs.getString("deadline")));
        request.setStatus(RequestStatus.fromString(rs.getString("status")));
        return request;
    }
}