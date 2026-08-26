package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import java.io.File;              // For checking/creating the database directory
import java.sql.Connection;       // The actual database connection object
import java.sql.DriverManager;    // For getting the database connection
import java.sql.SQLException;     // For database errors
import java.sql.Statement;        // For executing SQL commands

/**
 * Manages database connection and initialization
 * Singleton pattern ensures only one connection exists
 *
 * Singleton = only ONE instance of this class can exist
 * This prevents multiple connections from opening at once
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DatabaseConnection {

    // =============================================
    // FIELDS
    // =============================================

    private static DatabaseConnection instance;  // The SINGLE instance (Singleton)
    private Connection connection;              // The actual database connection
    private String databaseUrl;                  // JDBC URL (jdbc:sqlite:db/hospital.db)
    private String databasePath;                 // File path (db/hospital.db)
    private boolean foreignKeysEnabled;          // Are foreign keys currently enabled?

    // =============================================
    // CONSTRUCTOR - Private (Singleton pattern)
    // =============================================

    /**
     * Private constructor - only called once from getInstance()
     * Sets up default database path and URL
     */
    private DatabaseConnection() {
        this.databasePath = "db/hospital.db";      // Where the database file is stored
        this.databaseUrl = "jdbc:sqlite:" + databasePath; // JDBC URL format
        this.foreignKeysEnabled = true;            // Foreign keys enabled by default
    }

    // =============================================
    // SINGLETON - Get the single instance
    // =============================================

    /**
     * Get the single instance of DatabaseConnection
     * This is the ONLY way to get a DatabaseConnection object
     *
     * @return The single DatabaseConnection instance
     */
    public static DatabaseConnection getInstance() {
        if (instance == null) {           // If no instance exists yet...
            instance = new DatabaseConnection(); // Create it
        }
        return instance;                  // Return the instance
    }

    // =============================================
    // UTILITY - Create the database directory
    // =============================================

    /**
     * Ensures the database directory exists
     * If the 'db' folder doesn't exist, create it
     * This prevents "path does not exist" errors
     */
    private void ensureDatabaseDirectory() {
        File dbFile = new File(databasePath);      // Create File object for database file
        File parentDir = dbFile.getParentFile();   // Get the parent directory ('db')

        if (parentDir != null && !parentDir.exists()) {  // If directory doesn't exist...
            boolean created = parentDir.mkdirs();        // Create the directory
            if (created) {
                System.out.println("✅ Created database directory: " + parentDir.getPath());
            }
        }
    }

    // =============================================
    // CONNECT - Open the database connection
    // =============================================

    /**
     * Connect to the database
     * If already connected, does nothing
     *
     * @throws SQLException if connection fails
     */
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {  // If not connected...
            try {
                // Step 1: Create the 'db' folder if it doesn't exist
                ensureDatabaseDirectory();

                // Step 2: Load the SQLite driver
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found!", e);
            }

            // Step 3: Actually connect to the database
            connection = DriverManager.getConnection(databaseUrl);

            // Step 4: Enable foreign key constraints
            enableForeignKeys();

            System.out.println("✅ Connected to database: " + databaseUrl);
        }
    }

    // =============================================
    // FOREIGN KEY MANAGEMENT
    // =============================================

    /**
     * Enable foreign key constraints
     * This ensures data integrity (e.g., no orphaned roads)
     *
     * @throws SQLException if the command fails
     */
    public void enableForeignKeys() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");  // SQLite command
                foreignKeysEnabled = true;
            }
        }
    }

    /**
     * Disable foreign key constraints (for bulk import)
     * This makes imports faster by skipping constraint checks
     *
     * @throws SQLException if the command fails
     */
    public void disableForeignKeys() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = OFF");  // SQLite command
                foreignKeysEnabled = false;
                System.out.println("⚠️ Foreign key constraints disabled (for import)");
            }
        }
    }

    /**
     * Check if foreign keys are enabled
     *
     * @return true if foreign keys are enabled, false otherwise
     */
    public boolean isForeignKeysEnabled() {
        return foreignKeysEnabled;
    }

    // =============================================
    // DISCONNECT - Close the database connection
    // =============================================

    /**
     * Disconnect from the database
     * Ensures foreign keys are re-enabled before closing
     *
     * @throws SQLException if disconnection fails
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Re-enable foreign keys if they were disabled
            if (!foreignKeysEnabled) {
                enableForeignKeys();
            }
            connection.close();  // Close the connection
            System.out.println("✅ Disconnected from database");
        }
    }

    // =============================================
    // UTILITY METHODS
    // =============================================

    /**
     * Check if connection is active
     *
     * @return true if connected, false otherwise
     * @throws SQLException if the check fails
     */
    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Get the connection object
     * Automatically connects if not already connected
     *
     * @return The Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();  // Auto-connect if not connected
        }
        return connection;
    }

    // =============================================
    // INITIALIZE DATABASE - Create all tables
    // =============================================

    /**
     * Initialize database tables (called once during setup)
     * Creates all tables if they don't already exist
     * This is called when you first set up the database
     *
     * @throws SQLException if table creation fails
     */
    public void initializeDatabase() throws SQLException {
        connect(); // Ensure we're connected

        // =============================================
        // CREATE TABLES (5 tables)
        // =============================================

        String[] createTables = {
                // TABLE 1: locations
                """
            CREATE TABLE IF NOT EXISTS locations (
                locationId INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                area TEXT,
                type TEXT,
                latitude REAL,
                longitude REAL
            )
            """,
                // TABLE 2: roads (connects locations)
                """
            CREATE TABLE IF NOT EXISTS roads (
                roadId INTEGER PRIMARY KEY AUTOINCREMENT,
                fromLocationId INTEGER NOT NULL,
                toLocationId INTEGER NOT NULL,
                distance REAL,
                travelTime INTEGER,
                roadConditionWeight REAL,
                FOREIGN KEY (fromLocationId) REFERENCES locations(locationId),
                FOREIGN KEY (toLocationId) REFERENCES locations(locationId)
            )
            """,
                // TABLE 3: service_requests
                """
            CREATE TABLE IF NOT EXISTS service_requests (
                requestId INTEGER PRIMARY KEY AUTOINCREMENT,
                sourceId INTEGER NOT NULL,
                destinationId INTEGER NOT NULL,
                category TEXT,
                urgency INTEGER CHECK (urgency BETWEEN 1 AND 5),
                timeSubmitted TEXT,
                deadline TEXT,
                status TEXT,
                FOREIGN KEY (sourceId) REFERENCES locations(locationId),
                FOREIGN KEY (destinationId) REFERENCES locations(locationId)
            )
            """,
                // TABLE 4: resources
                """
            CREATE TABLE IF NOT EXISTS resources (
                resourceId INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT,
                homeLocationId INTEGER,
                capacity INTEGER,
                availabilityStatus TEXT,
                FOREIGN KEY (homeLocationId) REFERENCES locations(locationId)
            )
            """,
                // TABLE 5: algorithm_runs
                """
            CREATE TABLE IF NOT EXISTS algorithm_runs (
                runId INTEGER PRIMARY KEY AUTOINCREMENT,
                algorithmName TEXT,
                inputSize INTEGER,
                timeNs INTEGER,
                memoryKb INTEGER,
                dateRun TEXT
            )
            """
        };

        // Execute each CREATE TABLE statement
        try (Statement stmt = connection.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
            System.out.println("✅ Database tables initialized");
        }
    }

    // =============================================
    // CONFIGURATION - Set custom database path
    // =============================================

    /**
     * Set custom database path
     * Use this if you want to store the database in a different location
     *
     * @param databasePath The new path (e.g., "C:/my/database.db")
     */
    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
        this.databaseUrl = "jdbc:sqlite:" + databasePath;
    }

    /**
     * Get current database path
     *
     * @return The current database path
     */
    public String getDatabasePath() {
        return databasePath;
    }

    /**
     * Get current database URL
     *
     * @return The current JDBC URL
     */
    public String getDatabaseUrl() {
        return databaseUrl;
    }
}