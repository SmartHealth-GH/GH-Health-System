package main.com.ug.optimizer.database;

import main.com.ug.optimizer.model.Location;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * Automatically generates 300 service requests with realistic data
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class RequestGenerator {

    private ServiceRequestDAO requestDAO;
    private Random random;
    private int[] locationIds;

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

    private UrgencyLevel[] urgencyLevels = {
            UrgencyLevel.VERY_LOW,
            UrgencyLevel.LOW,
            UrgencyLevel.LOW,
            UrgencyLevel.MODERATE,
            UrgencyLevel.MODERATE,
            UrgencyLevel.MODERATE,
            UrgencyLevel.URGENT,
            UrgencyLevel.URGENT,
            UrgencyLevel.EMERGENCY,
            UrgencyLevel.EMERGENCY
    };

    private String[] statuses = {
            "PENDING", "PENDING", "PENDING", "PENDING", "PENDING",
            "IN_PROGRESS", "IN_PROGRESS", "COMPLETED", "COMPLETED", "ON_HOLD"
    };

    public RequestGenerator() {
        this.requestDAO = new ServiceRequestDAO();
        this.random = new Random();
    }

    public RequestGenerator(long seed) {
        this();
        this.random = new Random(seed);
    }

    /**
     * Generate random requests
     */
    public void generateRequests(int count) throws SQLException {
        // Load location IDs
        LocationDAO locDAO = new LocationDAO();
        this.locationIds = locDAO.getAllLocations().stream()
                .mapToInt(Location::getLocationId)
                .toArray();

        if (locationIds.length < 2) {
            System.out.println("⚠️ Need at least 2 locations to generate requests!");
            return;
        }

        System.out.println("📋 Generating " + count + " service requests...");

        LocalDateTime now = LocalDateTime.now();
        int batchSize = 50;

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

            // Random time in the past or future
            int hoursOffset = random.nextInt(48) - 24; // -24 to +24 hours
            LocalDateTime submitted = now.plusHours(hoursOffset);

            // Deadline is 1-48 hours after submission
            int deadlineHours = 1 + random.nextInt(48);
            LocalDateTime deadline = submitted.plusHours(deadlineHours);

            String status = statuses[random.nextInt(statuses.length)];

            ServiceRequest request = new ServiceRequest();
            request.setSourceId(sourceId);
            request.setDestinationId(destId);
            request.setCategory(category);
            request.setUrgency(urgency);
            request.setTimeSubmitted(submitted);
            request.setDeadline(deadline);
            request.setStatus(status);

            requestDAO.insertRequest(request);

            // Progress update
            if ((i + 1) % batchSize == 0) {
                System.out.println("   ✅ Generated " + (i + 1) + " requests");
            }
        }

        System.out.println("✅ Generated " + count + " requests successfully!");
        System.out.println("   Total requests in database: " + requestDAO.countRequests());
    }

    /**
     * Generate requests and save to CSV file
     */
    public void generateRequestsToCsv(int count, String filePath) throws Exception {
        // First generate in memory
        System.out.println("📋 Generating " + count + " requests for CSV...");

        // Load location IDs
        LocationDAO locDAO = new LocationDAO();
        this.locationIds = locDAO.getAllLocations().stream()
                .mapToInt(Location::getLocationId)
                .toArray();

        StringBuilder csv = new StringBuilder();
        csv.append("sourceId,destinationId,category,urgency,timeSubmitted,deadline,status\n");

        LocalDateTime now = LocalDateTime.now();

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