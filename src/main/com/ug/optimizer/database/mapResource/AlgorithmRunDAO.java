package main.com.ug.optimizer.database.mapResource;
// This file belongs to the database.mapResource package
// (Note: Should ideally be in main.com.ug.optimizer.database)

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.database.DatabaseConnection; // For database connection
import main.com.ug.optimizer.model.AlgorithmRun;          // The model class this DAO manages

import java.sql.*;                       // For JDBC (Connection, PreparedStatement, ResultSet)
import java.time.LocalDateTime;          // For handling date/time
import java.util.ArrayList;              // For creating lists of AlgorithmRun objects
import java.util.List;                   // For the List interface

/**
 * Data Access Object for AlgorithmRun entities
 * Handles all database operations for algorithm performance runs
 * This class is responsible for CRUD operations on the algorithm_runs table
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class AlgorithmRunDAO {

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
    public AlgorithmRunDAO() {
        this.dbConnection = DatabaseConnection.getInstance(); // Get the single database connection
    }

    // =============================================
    // CREATE - Save an algorithm run
    // =============================================

    /**
     * Save an algorithm run result to the database
     * This is the CREATE operation (C in CRUD)
     *
     * @param run The AlgorithmRun object to save
     * @throws SQLException if there's a database error
     */
    public void saveAlgorithmRun(AlgorithmRun run) throws SQLException {
        // SQL INSERT statement with placeholders (?)
        String sql = "INSERT INTO algorithm_runs (algorithmName, inputSize, timeNs, memoryKb, dateRun) VALUES (?, ?, ?, ?, ?)";

        // Try-with-resources: automatically closes PreparedStatement
        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {

            // Set the values for each placeholder
            pstmt.setString(1, run.getAlgorithmName());  // Algorithm name (e.g., "BinarySearch")
            pstmt.setInt(2, run.getInputSize());         // Input size (e.g., 1000)
            pstmt.setLong(3, run.getTimeNs());           // Execution time in nanoseconds
            pstmt.setLong(4, run.getMemoryKb());         // Memory used in KB
            pstmt.setString(5, run.getDateRun().toString()); // Date and time of the run

            pstmt.executeUpdate(); // Execute the INSERT
        }
        // PreparedStatement is automatically closed by try-with-resources
    }

    // =============================================
    // READ - Get all algorithm runs
    // =============================================

    /**
     * Get all algorithm runs from the database
     * Ordered by date (oldest to newest)
     * This is the READ operation (R in CRUD)
     *
     * @return List of all AlgorithmRun objects
     * @throws SQLException if there's a database error
     */
    public List<AlgorithmRun> getAllAlgorithmRuns() throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>(); // Create empty list to store results

        // SQL SELECT statement - ordered by date
        String sql = "SELECT * FROM algorithm_runs ORDER BY dateRun";

        // Try-with-resources: automatically closes Statement and ResultSet
        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Loop through each row in the result set
            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs)); // Convert row to AlgorithmRun object and add to list
            }
        }
        return runs; // Return the list of all runs
    }

    // =============================================
    // READ - Get runs by algorithm name
    // =============================================

    /**
     * Get algorithm runs by algorithm name
     * Ordered by input size (smallest to largest)
     * Useful for comparing performance of a specific algorithm
     *
     * @param algorithmName The name of the algorithm (e.g., "BinarySearch")
     * @return List of AlgorithmRun objects for that algorithm
     * @throws SQLException if there's a database error
     */
    public List<AlgorithmRun> getRunsByAlgorithm(String algorithmName) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();

        // SQL SELECT with WHERE clause for algorithm name
        // Ordered by input size to see performance growth
        String sql = "SELECT * FROM algorithm_runs WHERE algorithmName = ? ORDER BY inputSize";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, algorithmName); // Set the algorithm name parameter
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    // =============================================
    // READ - Get runs by input size
    // =============================================

    /**
     * Get algorithm runs by input size
     * Useful for comparing different algorithms at the same input size
     *
     * @param inputSize The input size (e.g., 1000)
     * @return List of AlgorithmRun objects with that input size
     * @throws SQLException if there's a database error
     */
    public List<AlgorithmRun> getRunsByInputSize(int inputSize) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();

        // SQL SELECT with WHERE clause for input size
        String sql = "SELECT * FROM algorithm_runs WHERE inputSize = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, inputSize); // Set the input size parameter
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    // =============================================
    // READ - Get latest runs (most recent first)
    // =============================================

    /**
     * Get the latest algorithm runs (most recent first)
     * Limited to a specified number of results
     * Useful for showing recent performance tests
     *
     * @param limit The maximum number of runs to return
     * @return List of the most recent AlgorithmRun objects
     * @throws SQLException if there's a database error
     */
    public List<AlgorithmRun> getLatestRuns(int limit) throws SQLException {
        List<AlgorithmRun> runs = new ArrayList<>();

        // SQL SELECT ordered by date descending (newest first) with limit
        String sql = "SELECT * FROM algorithm_runs ORDER BY dateRun DESC LIMIT ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setInt(1, limit); // Set the limit parameter
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                runs.add(mapAlgorithmRun(rs));
            }
        }
        return runs;
    }

    // =============================================
    // DELETE - Delete runs by algorithm name
    // =============================================

    /**
     * Delete all runs for a specific algorithm
     * This is the DELETE operation (D in CRUD)
     * Useful for clearing old test data
     *
     * @param algorithmName The name of the algorithm to delete
     * @throws SQLException if there's a database error
     */
    public void deleteRunsByAlgorithm(String algorithmName) throws SQLException {
        // SQL DELETE statement with WHERE clause
        String sql = "DELETE FROM algorithm_runs WHERE algorithmName = ?";

        try (PreparedStatement pstmt = dbConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, algorithmName); // Set the algorithm name parameter
            pstmt.executeUpdate(); // Execute the DELETE
        }
    }

    // =============================================
    // UTILITY - Count total runs
    // =============================================

    /**
     * Count the total number of algorithm runs in the database
     * Useful for statistics and reporting
     *
     * @return The total count of runs
     * @throws SQLException if there's a database error
     */
    public int countRuns() throws SQLException {
        // SQL COUNT query
        String sql = "SELECT COUNT(*) FROM algorithm_runs";

        try (Statement stmt = dbConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getInt(1) : 0; // Return the count, or 0 if no results
        }
    }

    // =============================================
    // MAPPER - Convert ResultSet to AlgorithmRun object
    // =============================================

    /**
     * Map a database row (ResultSet) to an AlgorithmRun object
     * This is a helper method used by all READ operations
     *
     * @param rs The ResultSet containing database row data
     * @return An AlgorithmRun object populated with the data
     * @throws SQLException if there's a database error
     */
    private AlgorithmRun mapAlgorithmRun(ResultSet rs) throws SQLException {
        AlgorithmRun run = new AlgorithmRun(); // Create a new AlgorithmRun object

        // Set all fields from the ResultSet
        run.setRunId(rs.getInt("runId"));                 // Database auto-generated ID
        run.setAlgorithmName(rs.getString("algorithmName")); // Name of the algorithm
        run.setInputSize(rs.getInt("inputSize"));         // Input size used
        run.setTimeNs(rs.getLong("timeNs"));              // Execution time
        run.setMemoryKb(rs.getLong("memoryKb"));          // Memory usage
        run.setDateRun(LocalDateTime.parse(rs.getString("dateRun"))); // Date and time

        return run; // Return the populated object
    }
}