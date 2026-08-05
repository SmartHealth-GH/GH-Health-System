package main.com.ug.optimizer.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Manages database connection and initialization
 * Singleton pattern ensures only one connection exists
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private String databaseUrl;
    private String databasePath;
    private boolean foreignKeysEnabled;

    private DatabaseConnection() {
        this.databasePath = "db/hospital.db";
        this.databaseUrl = "jdbc:sqlite:" + databasePath;
        this.foreignKeysEnabled = true;
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    /**
     * Ensures the database directory exists
     */
    private void ensureDatabaseDirectory() {
        File dbFile = new File(databasePath);
        File parentDir = dbFile.getParentFile();

        if (parentDir != null && !parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                System.out.println("✅ Created database directory: " + parentDir.getPath());
            }
        }
    }

    /**
     * Connect to the database
     */
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                ensureDatabaseDirectory();
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found!", e);
            }

            connection = DriverManager.getConnection(databaseUrl);

            // Enable foreign key constraints by default
            enableForeignKeys();

            System.out.println("✅ Connected to database: " + databaseUrl);
        }
    }

    /**
     * Enable foreign key constraints
     */
    public void enableForeignKeys() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
                foreignKeysEnabled = true;
            }
        }
    }

    /**
     * Disable foreign key constraints (for bulk import)
     */
    public void disableForeignKeys() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = OFF");
                foreignKeysEnabled = false;
                System.out.println("⚠️ Foreign key constraints disabled (for import)");
            }
        }
    }

    /**
     * Check if foreign keys are enabled
     */
    public boolean isForeignKeysEnabled() {
        return foreignKeysEnabled;
    }

    /**
     * Disconnect from the database
     */
    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            // Ensure foreign keys are re-enabled before closing
            if (!foreignKeysEnabled) {
                enableForeignKeys();
            }
            connection.close();
            System.out.println("✅ Disconnected from database");
        }
    }

    /**
     * Check if connection is active
     */
    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    /**
     * Get the connection object
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

    /**
     * Initialize database tables (called once during setup)
     */
    public void initializeDatabase() throws SQLException {
        connect();

        String[] createTables = {
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

        try (Statement stmt = connection.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
            System.out.println("✅ Database tables initialized");
        }
    }

    /**
     * Set custom database path
     */
    public void setDatabasePath(String databasePath) {
        this.databasePath = databasePath;
        this.databaseUrl = "jdbc:sqlite:" + databasePath;
    }

    /**
     * Get current database path
     */
    public String getDatabasePath() {
        return databasePath;
    }

    /**
     * Get current database URL
     */
    public String getDatabaseUrl() {
        return databaseUrl;
    }
}