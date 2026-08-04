package main.com.ug.optimizer.Database;

import main.com.ug.optimizer.model.Resource;
import main.com.ug.optimizer.model.enums.ResourceStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Resource entities
 * Handles all database operations for resources
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class ResourceDAO {

    private DatabaseConnection dbConnection;

    public ResourceDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Get all resources
     */
    public List<Resource> getAllResources() throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources ORDER BY resourceId";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    /**
     * Get resource by ID
     */
    public Resource getResourceById(int id) throws SQLException {
        String sql = "SELECT * FROM resources WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResource(rs);
            }
        }
        return null;
    }

    /**
     * Insert a new resource
     */
    public int insertResource(Resource resource) throws SQLException {
        String sql = "INSERT INTO resources (type, homeLocationId, capacity, availabilityStatus) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, resource.getType());
            pstmt.setInt(2, resource.getHomeLocationId());
            pstmt.setInt(3, resource.getCapacity());
            pstmt.setString(4, resource.getAvailabilityStatus().name());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    /**
     * Update resource status only
     */
    public void updateResourceStatus(int resourceId, ResourceStatus status) throws SQLException {
        String sql = "UPDATE resources SET availabilityStatus = ? WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            pstmt.setInt(2, resourceId);
            pstmt.executeUpdate();
        }
    }

    /**
     * Update full resource
     */
    public void updateResource(Resource resource) throws SQLException {
        String sql = "UPDATE resources SET type = ?, homeLocationId = ?, capacity = ?, availabilityStatus = ? WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, resource.getType());
            pstmt.setInt(2, resource.getHomeLocationId());
            pstmt.setInt(3, resource.getCapacity());
            pstmt.setString(4, resource.getAvailabilityStatus().name());
            pstmt.setInt(5, resource.getResourceId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Get only available resources
     */
    public List<Resource> getAvailableResources() throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE availabilityStatus = 'AVAILABLE'";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    /**
     * Get resources by type
     */
    public List<Resource> getResourcesByType(String type) throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE type = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    /**
     * Get resources by status
     */
    public List<Resource> getResourcesByStatus(ResourceStatus status) throws SQLException {
        List<Resource> resources = new ArrayList<>();
        String sql = "SELECT * FROM resources WHERE availabilityStatus = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                resources.add(mapResource(rs));
            }
        }
        return resources;
    }

    /**
     * Delete a resource by ID
     */
    public void deleteResource(int id) throws SQLException {
        String sql = "DELETE FROM resources WHERE resourceId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Count total resources
     */
    public int countResources() throws SQLException {
        String sql = "SELECT COUNT(*) FROM resources";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Count available resources
     */
    public int countAvailableResources() throws SQLException {
        String sql = "SELECT COUNT(*) FROM resources WHERE availabilityStatus = 'AVAILABLE'";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Map ResultSet to Resource object
     */
    private Resource mapResource(ResultSet rs) throws SQLException {
        Resource resource = new Resource();
        resource.setResourceId(rs.getInt("resourceId"));
        resource.setType(rs.getString("type"));
        resource.setHomeLocationId(rs.getInt("homeLocationId"));
        resource.setCapacity(rs.getInt("capacity"));
        resource.setAvailabilityStatus(ResourceStatus.fromString(rs.getString("availabilityStatus")));
        return resource;
    }
}