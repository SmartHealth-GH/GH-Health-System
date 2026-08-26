package main.com.ug.optimizer.database;
// This file belongs to the database package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.model.Location;           // For getting location IDs
import main.com.ug.optimizer.model.ServiceRequest;     // The model class we're creating
import main.com.ug.optimizer.model.enums.*;            // All enums (UrgencyLevel, RequestStatus)

import java.sql.SQLException;        // For database errors
import java.time.LocalDateTime;      // For date/time handling
import java.util.Random;             // For generating random values

/**
 * Automatically generates 300 service requests with realistic data
 *
 * This class creates random requests so you don't have to type them manually.
 * It's useful for populating the database with test data.
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class RequestGenerator {

    // =============================================
    // FIELDS
    // =============================================

    private ServiceRequestDAO requestDAO;  // For inserting requests into database
    private Random random;                 // For generating random values
    private int[] locationIds;             // Array of all location IDs (for random selection)

    // =============================================
    // CATEGORIES - Different types of requests
    // =============================================

    private String[] categories = {
            "Emergency", "Pharmacy", "Surgery", "Lab Test", "Radiology",
            "Maternity", "ICU Transfer", "Outpatient", "Admin", "Lab Results",
            "Blood Request", "Ambulance Dispatch", "Consultation", "Vaccination",
            "Physiotherapy", "Dialysis", "Endoscopy", "X-Ray", "CT Scan",
            "MRI Scan", "Ultrasound", "Blood Test", "Urine Test", "COVID Test",
            "Malaria Test", "TB Test", "HIV Test", "Pregnancy Test",
            "Child Vaccination", "Routine Checkup", "Emergency Surgery",
            "Heart Checkup", "Eye Checkup", "Dental Checkup", "Skin Checkup"
    };

    // =============================================
    // URGENCY LEVELS - Weighted distribution
    // =============================================

    // More MODERATE/LOW, fewer EMERGENCY/URGENT
    private UrgencyLevel[] urgencyLevels = {
            UrgencyLevel.VERY_LOW,    // 1
            UrgencyLevel.LOW,         // 2
            UrgencyLevel.LOW,         // 2
            UrgencyLevel.MODERATE,    // 3
            UrgencyLevel.MODERATE,    // 3
            UrgencyLevel.MODERATE,    // 3
            UrgencyLevel.URGENT,      // 4
            UrgencyLevel.URGENT,      // 4
            UrgencyLevel.EMERGENCY,   // 5
            UrgencyLevel.EMERGENCY    // 5
    };

    // =============================================
    // STATUSES - Weighted distribution
    // =============================================

    // More PENDING, fewer COMPLETED/IN_PROGRESS
    private String[] statuses = {
            "PENDING", "PENDING", "PENDING", "PENDING", "PENDING",
            "IN_PROGRESS", "IN_PROGRESS", "COMPLETED", "COMPLETED", "ON_HOLD"
    };

    // =============================================
    // CONSTRUCTORS
    // =============================================

    /**
     * Default constructor - creates a RequestGenerator
     * Uses System time as random seed (different each run)
     */
    public RequestGenerator() {
        this.requestDAO = new ServiceRequestDAO(); // For inserting requests
        this.random = new Random();                // Random number generator
    }

    /**
     * Constructor with seed - for reproducible results
     * Same seed → same "random" requests (useful for testing)
     *
     * @param seed The seed for the random generator
     */
    public RequestGenerator(long seed) {
        this();                                    // Call default constructor first
        this.random = new Random(seed);            // Create with specific seed
    }

    // =============================================
    // MAIN METHOD - Generate random requests
    // =============================================

    /**
     * Generate random requests and insert them into the database
     *
     * @param count How many requests to generate
     * @throws SQLException if there's a database error
     */
    public void generateRequests(int count) throws SQLException {
        // =============================================
        // Step 1: Load all location IDs
        // =============================================
        LocationDAO locDAO = new LocationDAO();
        this.locationIds = locDAO.getAllLocations().stream()
                .mapToInt(Location::getLocationId)  // Convert Location → ID
                .toArray();                         // Convert to int array

        // Need at least 2 locations (source and destination)
        if (locationIds.length < 2) {
            System.out.println("⚠️ Need at least 2 locations to generate requests!");
            return; // Exit if not enough locations
        }

        System.out.println("📋 Generating " + count + " service requests...");

        LocalDateTime now = LocalDateTime.now();    // Current time
        int batchSize = 50;                         // Progress update every 50

        // =============================================
        // Step 2: Generate each request
        // =============================================
        for (int i = 0; i < count; i++) {
            // --- Pick random source and destination (must be different) ---
            int sourceIdx = random.nextInt(locationIds.length);
            int destIdx;
            do {
                destIdx = random.nextInt(locationIds.length);
            } while (destIdx == sourceIdx);  // Make sure source ≠ destination

            int sourceId = locationIds[sourceIdx];
            int destId = locationIds[destIdx];

            // --- Pick random category ---
            String category = categories[random.nextInt(categories.length)];

            // --- Pick random urgency (weighted distribution) ---
            UrgencyLevel urgency = urgencyLevels[random.nextInt(urgencyLevels.length)];

            // --- Random submission time (past or future) ---
            int hoursOffset = random.nextInt(48) - 24; // -24 to +24 hours
            LocalDateTime submitted = now.plusHours(hoursOffset);

            // --- Random deadline (1-48 hours after submission) ---
            int deadlineHours = 1 + random.nextInt(48);
            LocalDateTime deadline = submitted.plusHours(deadlineHours);

            // --- Pick random status (weighted distribution) ---
            String status = statuses[random.nextInt(statuses.length)];

            // =============================================
            // Step 3: Create and insert the request
            // =============================================
            ServiceRequest request = new ServiceRequest();
            request.setSourceId(sourceId);
            request.setDestinationId(destId);
            request.setCategory(category);
            request.setUrgency(urgency);
            request.setTimeSubmitted(submitted);
            request.setDeadline(deadline);
            request.setStatus(status);

            requestDAO.insertRequest(request);  // Insert into database

            // --- Progress update ---
            if ((i + 1) % batchSize == 0) {
                System.out.println("   ✅ Generated " + (i + 1) + " requests");
            }
        }

        // =============================================
        // Step 4: Summary
        // =============================================
        System.out.println("✅ Generated " + count + " requests successfully!");
        System.out.println("   Total requests in database: " + requestDAO.countRequests());
    }

    // =============================================
    // ALTERNATIVE - Generate to CSV file
    // =============================================

    /**
     * Generate requests and save to CSV file (instead of database)
     * Useful for creating data files that can be imported later
     *
     * @param count How many requests to generate
     * @param filePath Where to save the CSV file
     * @throws Exception if file writing fails
     */
    public void generateRequestsToCsv(int count, String filePath) throws Exception {
        System.out.println("📋 Generating " + count + " requests for CSV...");

        // Load location IDs
        LocationDAO locDAO = new LocationDAO();
        this.locationIds = locDAO.getAllLocations().stream()
                .mapToInt(Location::getLocationId)
                .toArray();

        // =============================================
        // Build CSV content
        // =============================================
        StringBuilder csv = new StringBuilder();
        // Header row
        csv.append("sourceId,destinationId,category,urgency,timeSubmitted,deadline,status\n");

        LocalDateTime now = LocalDateTime.now();

        // Generate each row (same logic as above, but writes to CSV instead of database)
        for (int i = 0; i < count; i++) {
            int sourceIdx = random.nextInt(locationIds.length);
            int destIdx;
            do {
                destIdx = random.nextInt(locationIds.length);
            } while (destIdx == sourceIdx);

            int sourceId = locationIds[sourceIdx];
            int destId = locationIds[destIdx];

            String category = categories[random.nextInt(categories.length)];
            UrgencyLevel urgency = urgencyLevels[random.nextInt(urgencyLevels.length)];

            int hoursOffset = random.nextInt(48) - 24;
            LocalDateTime submitted = now.plusHours(hoursOffset);

            int deadlineHours = 1 + random.nextInt(48);
            LocalDateTime deadline = submitted.plusHours(deadlineHours);

            String status = statuses[random.nextInt(statuses.length)];

            // Add row to CSV
            csv.append(sourceId).append(",")
                    .append(destId).append(",")
                    .append(category).append(",")
                    .append(urgency.getValue()).append(",")
                    .append(submitted.toString()).append(",")
                    .append(deadline.toString()).append(",")
                    .append(status).append("\n");
        }

        // Write to file
        java.nio.file.Files.write(
                java.nio.file.Paths.get(filePath),
                csv.toString().getBytes()
        );

        System.out.println("✅ Generated " + count + " requests saved to: " + filePath);
    }
}