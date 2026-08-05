package main.com.ug.optimizer.database;

import java.sql.*;

/**
 * Simplest possible database test
 */
public class SimpleDBTest {

    public static void main(String[] args) {
        System.out.println("=".repeat(50));
        System.out.println("SIMPLE DATABASE TEST");
        System.out.println("=".repeat(50));

        try {
            // Step 1: Test if driver exists
            System.out.println("\n1. Testing if SQLite driver is available...");
            try {
                Class.forName("org.sqlite.JDBC");
                System.out.println("   ✅ Driver found!");
            } catch (ClassNotFoundException e) {
                System.out.println("   ❌ Driver NOT found!");
                System.out.println("   Error: " + e.getMessage());
                return;
            }

            // Step 2: Try to connect
            System.out.println("\n2. Attempting to connect to database...");
            String url = "jdbc:sqlite:test.db";

            try (Connection conn = DriverManager.getConnection(url)) {
                System.out.println("   ✅ Connected successfully!");
                System.out.println("   Database: " + url);

                // Step 3: Create a test table
                System.out.println("\n3. Creating a test table...");
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("CREATE TABLE IF NOT EXISTS test (id INTEGER PRIMARY KEY, name TEXT)");
                    System.out.println("   ✅ Table created!");
                }

                // Step 4: Insert test data
                System.out.println("\n4. Inserting test data...");
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "INSERT INTO test (name) VALUES (?)")) {
                    pstmt.setString(1, "Test Entry");
                    pstmt.executeUpdate();
                    System.out.println("   ✅ Data inserted!");
                }

                // Step 5: Query test data
                System.out.println("\n5. Querying test data...");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT * FROM test")) {
                    while (rs.next()) {
                        System.out.println("   ID: " + rs.getInt("id") +
                                ", Name: " + rs.getString("name"));
                    }
                }

                System.out.println("\n" + "=".repeat(50));
                System.out.println("🎉 ALL TESTS PASSED! Database works!");
                System.out.println("=".repeat(50));

            } catch (SQLException e) {
                System.out.println("   ❌ Connection failed!");
                System.out.println("   Error: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("❌ Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}