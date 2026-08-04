package main.com.ug.optimizer.database;

import main.com.ug.optimizer.model.Road;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Road entities
 * Handles all database operations for roads
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class RoadDAO {

    private DatabaseConnection dbConnection;

    public RoadDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Get all roads
     */
    public List<Road> getAllRoads() throws SQLException {
        List<Road> roads = new ArrayList<>();
        String sql = "SELECT * FROM roads ORDER BY roadId";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roads.add(mapRoad(rs));
            }
        }
        return roads;
    }

    /**
     * Get road by ID
     */
    public Road getRoadById(int id) throws SQLException {
        String sql = "SELECT * FROM roads WHERE roadId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapRoad(rs);
            }
        }
        return null;
    }

    /**
     * Insert a new road
     */
    public int insertRoad(Road road) throws SQLException {
        String sql = "INSERT INTO roads (fromLocationId, toLocationId, distance, travelTime, roadConditionWeight) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, road.getFromLocationId());
            pstmt.setInt(2, road.getToLocationId());
            pstmt.setDouble(3, road.getDistance());
            pstmt.setInt(4, road.getTravelTime());
            pstmt.setDouble(5, road.getRoadConditionWeight());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    /**
     * Update an existing road
     */
    public void updateRoad(Road road) throws SQLException {
        String sql = "UPDATE roads SET fromLocationId = ?, toLocationId = ?, distance = ?, travelTime = ?, roadConditionWeight = ? WHERE roadId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, road.getFromLocationId());
            pstmt.setInt(2, road.getToLocationId());
            pstmt.setDouble(3, road.getDistance());
            pstmt.setInt(4, road.getTravelTime());
            pstmt.setDouble(5, road.getRoadConditionWeight());
            pstmt.setInt(6, road.getRoadId());
            pstmt.executeUpdate();
        }
    }

    /**
     * Delete a road by ID
     */
    public void deleteRoad(int id) throws SQLException {
        String sql = "DELETE FROM roads WHERE roadId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    /**
     * Get roads from a location
     */
    public List<Road> getRoadsFromLocation(int locationId) throws SQLException {
        List<Road> roads = new ArrayList<>();
        String sql = "SELECT * FROM roads WHERE fromLocationId = ? OR toLocationId = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, locationId);
            pstmt.setInt(2, locationId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                roads.add(mapRoad(rs));
            }
        }
        return roads;
    }

    /**
     * Count total roads
     */
    public int countRoads() throws SQLException {
        String sql = "SELECT COUNT(*) FROM roads";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Map ResultSet to Road object
     */
    private Road mapRoad(ResultSet rs) throws SQLException {
        Road road = new Road();
        road.setRoadId(rs.getInt("roadId"));
        road.setFromLocationId(rs.getInt("fromLocationId"));
        road.setToLocationId(rs.getInt("toLocationId"));
        road.setDistance(rs.getDouble("distance"));
        road.setTravelTime(rs.getInt("travelTime"));
        road.setRoadConditionWeight(rs.getDouble("roadConditionWeight"));
        return road;
    }
}