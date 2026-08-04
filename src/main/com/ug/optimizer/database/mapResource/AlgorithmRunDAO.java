package main.com.ug.optimizer.database.mapResource;


import main.com.ug.optimizer.database.DatabaseConnection;
import main.com.ug.optimizer.model.AlgorithmRun;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for AlgorithmRun entities
 * Handles all database operations for algorithm performance runs
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class AlgorithmRunDAO {

    private DatabaseConnection dbConnection;

    public AlgorithmRunDAO() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    /**
     * Save an algorithm run result
     */
    public void saveAlgorithmRun(AlgorithmRun run) throws SQLException {
        String sql = "INSERT INTO algorithm_runs (algorithmName, inputSize, timeNs, memoryKb, dateRun) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, run.getAlgorithmName());
            pstmt.setInt(2, run.getInputSize());
            pstmt.setLong(3, run.getTimeNs());
            pstmt.setLong(4, run.getMemoryKb());
            pstmt.setString(5, run.getDateRun().toString());
            pstmt.executeUpdate();
        }
    }

    /**
     * Get all algorithm runs
     */
    public List<AlgorithmRun> getAllAlgorithmRuns() throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs ORDER BY dateRun";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    /**
     * Get algorithm runs by name
     */
    public List<AlgorithmRun> getRunsByAlgorithm(String algorithmName) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs WHERE algorithmName = ? ORDER BY inputSize";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, algorithmName);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    /**
     * Get runs by input size
     */
    public List<AlgorithmRun> getRunsByInputSize(int inputSize) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs WHERE inputSize = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, inputSize);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    /**
     * Get latest runs (most recent first)
     */
    public List<AlgorithmRun> getLatestRuns(int limit) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();
        String sql = "SELECT * FROM algorithm_runs ORDER BY dateRun DESC LIMIT ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    /**
     * Delete all runs for an algorithm
     */
    public void deleteRunsByAlgorithm(String algorithmName) throws SQLException {
        String sql = "DELETE FROM algorithm_runs WHERE algorithmName = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, algorithmName);
            pstmt.executeUpdate();
        }
    }

    /**
     * Count total runs
     */
    public int countRuns() throws SQLException {
        String sql = "SELECT COUNT(*) FROM algorithm_runs";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    /**
     * Map ResultSet to AlgorithmRun object
     */
    private AlgorithmRun mapAlgorithmRun(ResultSet rs) throws SQLException {
        AlgorithmRun run = new AlgorithmRun();
        run.setRunId(rs.getInt("runId"));
        run.setAlgorithmName(rs.getString("algorithmName"));
        run.setInputSize(rs.getInt("inputSize"));
        run.setTimeNs(rs.getLong("timeNs"));
        run.setMemoryKb(rs.getLong("memoryKb"));
        run.setDateRun(LocalDateTime.parse(rs.getString("dateRun")));
        return run;
    }
}