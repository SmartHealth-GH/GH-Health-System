package main.com.ug.optimizer.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;
    private String databaseUrl;

    private DatabaseConnection() {
        this.databaseUrl = "jdbc:sqlite:db/hospital.db";
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // ⭐ FORCE LOAD THE DRIVER ⭐
                Class.forName("org.sqlite.JDBC");
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found! Please add the driver to your classpath.", e);
            }

            connection = DriverManager.getConnection(databaseUrl);

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            System.out.println("✅ Connected to database: " + databaseUrl);
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
            System.out.println("✅ Disconnected from database");
        }
    }

    public boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connect();
        }
        return connection;
    }

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

    public void setDatabaseUrl(String databasePath) {
        this.databaseUrl = "jdbc:sqlite:" + databasePath;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }
}