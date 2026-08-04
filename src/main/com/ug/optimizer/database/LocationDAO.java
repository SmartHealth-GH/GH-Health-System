package main.com.ug.optimizer.Database;

import main.com.ug.optimizer.model.Location;
import main.com.ug.optimizer.model.enums.LocationType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Location entities
 * Handles all database operations for locations
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class LocationDAO {

    private DatabaseConnection dbConnection;

    public LocationDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Get all locations
     */
    public List<Location> getAllLocations() throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations ORDER BY locationId";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        }
        return locations;
    }

    /**
     * Get location by ID
     */
    public Location getLocationById(int id) throws SQLException {
        String sql = "SELECT * FROM locations WHERE locationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapLocation(rs);
            }
        }
        return null;
    }

    /**
     * Insert a new location
     */
    public int insertLocation(Location location) throws SQLException {
        String sql = "INSERT INTO locations (name, area, type, latitude, longitude) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, location.getName());
            pstmt.setString(2, location.getArea());
            pstmt.setString(3, location.getType().name());
            pstmt.setDouble(4, location.getLatitude());
            pstmt.setDouble(5, location.getLongitude());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    /**
     * Update an existing location
     */
    public void updateLocation(Location location) throws SQLException {
        String sql = "UPDATE locations SET name = ?, area = ?, type = ?, latitude = ?, longitude = ? WHERE locationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, location.getName());
            pstmt.setString(2, location.getArea());
            pstmt.setString(3, location.getType().name());
            pstmt.setDouble(4, location.getLatitude());
            pstmt.setDouble(5, location.getLongitude());
            pstmt.setInt(6, location.getLocationId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Delete a location by ID
     */
    public void deleteLocation(int id) throws SQLException {
        String sql = "DELETE FROM locations WHERE locationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Get locations by area
     */
    public List<Location> getLocationsByArea(String area) throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations WHERE area = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, area);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        }
        return locations;
    }

    /**
     * Get locations by type
     */
    public List<Location> getLocationsByType(LocationType type) throws SQLException {
        List<Location> locations = new ArrayList<>();
        String sql = "SELECT * FROM locations WHERE type = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, type.name());
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                locations.add(mapLocation(rs));
            }
        }
        return locations;
    }

    /**
     * Count total locations
     */
    public int countLocations() throws SQLException {
        String sql = "SELECT COUNT(*) FROM locations";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Map ResultSet to Location object
     */
    private Location mapLocation(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setLocationId(rs.getInt("locationId"));
        location.setName(rs.getString("name"));
        location.setArea(rs.getString("area"));
        location.setType(LocationType.fromString(rs.getString("type")));
        location.setLatitude(rs.getDouble("latitude"));
        location.setLongitude(rs.getDouble("longitude"));
        return location;
    }
}