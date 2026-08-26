package main.com.ug.optimizer.database.mapAlgorithm;
// This file belongs to the database.mapAlgorithm package

// =============================================
// IMPORTS - Bringing in other classes we need
// =============================================

import main.com.ug.optimizer.database.LocationDAO;        // For inserting locations into database
import main.com.ug.optimizer.database.ResourceDAO;        // For inserting resources into database
import main.com.ug.optimizer.database.RoadDAO;            // For inserting roads into database
import main.com.ug.optimizer.database.ServiceRequestDAO;  // For inserting requests into database
import main.com.ug.optimizer.model.*;                     // Import ALL model classes (Location, Road, ServiceRequest, Resource)
import main.com.ug.optimizer.model.enums.*;               // Import ALL enums (LocationType, UrgencyLevel, etc.)

import java.sql.SQLException;       // For handling database errors
import java.time.LocalDateTime;     // For handling date and time

/**
 * Seeds the database with sample data for testing
 * This class adds initial data to the database so the system has something to work with
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DataSeeder {

    // =============================================
    // FIELDS - DAO objects for each entity type
    // =============================================

    private LocationDAO locationDAO;           // Handles location database operations
    private RoadDAO roadDAO;                   // Handles road database operations
    private ServiceRequestDAO requestDAO;      // Handles service request database operations
    private ResourceDAO resourceDAO;           // Handles resource database operations

    // =============================================
    // CONSTRUCTOR - Initializes all DAO objects
    // =============================================

    /**
     * Constructor - creates all the DAO objects we need
     * These objects will be used to insert data into the database
     */
    public DataSeeder() {
        this.locationDAO = new LocationDAO();      // Create Location DAO
        this.roadDAO = new RoadDAO();              // Create Road DAO
        this.requestDAO = new ServiceRequestDAO(); // Create ServiceRequest DAO
        this.resourceDAO = new ResourceDAO();      // Create Resource DAO
    }

    /**
     * Seed all sample data - MAIN METHOD
     * This is the method you call to add all sample data to the database
     *
     * @throws SQLException if there's a database error
     */
    public void seedAll() throws SQLException {
        // Print a message to show seeding has started
        System.out.println("🌱 Seeding sample data...");

        // Check if data already exists in the database
        // We check locations count - if there are any, data already exists
        if (locationDAO.countLocations() > 0) {
            System.out.println("   ✅ Data already exists. Skipping seed.");
            return; // Exit early to avoid duplicate data
        }

        // STEP 1: Add locations to the database
        // Returns the ID of the Emergency Unit (used later)
        int emergencyId = seedLocations();

        // STEP 2: Add roads connecting locations
        seedRoads();

        // STEP 3: Add service requests between locations
        seedRequests();

        // STEP 4: Add hospital resources
        seedResources();

        // Print success message with counts of each entity added
        System.out.println("✅ Sample data seeded successfully!");
        System.out.println("   " + locationDAO.countLocations() + " locations");
        System.out.println("   " + roadDAO.countRoads() + " roads");
        System.out.println("   " + requestDAO.countRequests() + " requests");
        System.out.println("   " + resourceDAO.countResources() + " resources");
    }

    // =============================================
    // SEED LOCATIONS - Creates 15 sample locations
    // =============================================

    /**
     * Seed locations - adds all sample locations to the database
     *
     * @return The ID of the Emergency Unit (used for creating roads/requests)
     * @throws SQLException if there's a database error
     */
    private int seedLocations() throws SQLException {

        // =============================================
        // KORLE-BU TEACHING HOSPITAL - 10 LOCATIONS
        // =============================================

        // Insert Emergency Unit and store its ID (we'll return this)
        int emergencyId = locationDAO.insertLocation(
                new Location("Emergency Unit", "Korle-Bu", LocationType.EMERGENCY, 5.5405, -0.2038)
        );

        // Insert Pharmacy
        int pharmacyId = locationDAO.insertLocation(
                new Location("Pharmacy", "Korle-Bu", LocationType.PHARMACY, 5.5408, -0.2040)
        );

        // Insert Maternity Ward
        int maternityId = locationDAO.insertLocation(
                new Location("Maternity Ward", "Korle-Bu", LocationType.WARD, 5.5415, -0.2050)
        );

        // Insert Surgical Ward
        int surgicalId = locationDAO.insertLocation(
                new Location("Surgical Ward", "Korle-Bu", LocationType.WARD, 5.5410, -0.2045)
        );

        // Insert ICU (Intensive Care Unit)
        int icuId = locationDAO.insertLocation(
                new Location("ICU", "Korle-Bu", LocationType.ICU, 5.5402, -0.2035)
        );

        // Insert Outpatient Department
        int opdId = locationDAO.insertLocation(
                new Location("Outpatient Dept", "Korle-Bu", LocationType.OPD, 5.5418, -0.2042)
        );

        // Insert Laboratory
        int labId = locationDAO.insertLocation(
                new Location("Laboratory", "Korle-Bu", LocationType.LABORATORY, 5.5400, -0.2048)
        );

        // Insert Radiology (X-ray/imaging)
        int radiologyId = locationDAO.insertLocation(
                new Location("Radiology", "Korle-Bu", LocationType.RADIOLOGY, 5.5407, -0.2030)
        );

        // Insert Admin Building
        int adminId = locationDAO.insertLocation(
                new Location("Admin Building", "Korle-Bu", LocationType.ADMINISTRATION, 5.5420, -0.2055)
        );

        // Insert Ambulance Bay (where ambulances park)
        int ambulanceBayId = locationDAO.insertLocation(
                new Location("Ambulance Bay", "Korle-Bu", LocationType.TRANSPORT, 5.5410, -0.2060)
        );

        // =============================================
        // ADDITIONAL LOCATIONS - 5 more
        // =============================================

        // Insert Staff Quarters
        locationDAO.insertLocation(
                new Location("Staff Quarters", "Korle-Bu", LocationType.STAFF, 5.5425, -0.2058)
        );

        // Insert Records Office
        locationDAO.insertLocation(
                new Location("Records Office", "Korle-Bu", LocationType.RECORDS, 5.5409, -0.2059)
        );

        // Insert Blood Bank
        locationDAO.insertLocation(
                new Location("Blood Bank", "Korle-Bu", LocationType.BLOOD_BANK, 5.5403, -0.2049)
        );

        // Insert Dialysis Unit
        locationDAO.insertLocation(
                new Location("Dialysis Unit", "Korle-Bu", LocationType.DIALYSIS, 5.5412, -0.2041)
        );

        // Insert Endoscopy Unit
        locationDAO.insertLocation(
                new Location("Endoscopy Unit", "Korle-Bu", LocationType.ENDOSCOPY, 5.5406, -0.2053)
        );

        // Return the Emergency Unit ID so we can use it later
        return emergencyId;
    }

    // =============================================
    // SEED ROADS - Creates connections between locations
    // =============================================

    /**
     * Seed roads - adds all sample roads to the database
     *
     * @throws SQLException if there's a database error
     */
    private void seedRoads() throws SQLException {
        // Get ALL locations from the database
        var locations = locationDAO.getAllLocations();  // var = List<Location>

        // Extract just the IDs into an array
        var ids = locations.stream().mapToInt(Location::getLocationId).toArray();
        // ids = [1, 2, 3, 4, 5, ... 15]

        // =============================================
        // CREATE ROADS CONNECTING LOCATIONS IN ORDER
        // =============================================

        // Loop through each location except the last one
        // Creates a road from each location to the next one
        for (int i = 0; i < ids.length - 1; i++) {
            roadDAO.insertRoad(
                    new Road(
                            ids[i],                    // FROM: current location
                            ids[i + 1],                // TO: next location
                            0.3 + (i * 0.1),           // DISTANCE: increases slightly each time
                            5 + i,                     // TRAVEL TIME: increases each time
                            0.8 + (i * 0.05)           // CONDITION: varies between 0.8 and 1.5
                    )
            );
        }

        // =============================================
        // ADD EXTRA CONNECTIONS (SHORTCUTS)
        // =============================================

        // Emergency (ID 1) → Maternity (ID 3)
        roadDAO.insertRoad(
                new Road(ids[0], ids[2], 0.4, 6, 1.0)
        );

        // Pharmacy (ID 2) → ICU (ID 5)
        roadDAO.insertRoad(
                new Road(ids[1], ids[4], 0.5, 7, 0.9)
        );

        // Surgical (ID 4) → Lab (ID 7)
        roadDAO.insertRoad(
                new Road(ids[3], ids[6], 0.6, 8, 1.1)
        );
    }

    // =============================================
    // SEED REQUESTS - Creates service requests
    // =============================================

    /**
     * Seed service requests - adds all sample requests to the database
     *
     * @throws SQLException if there's a database error
     */
    private void seedRequests() throws SQLException {
        // Get ALL locations from the database
        var locations = locationDAO.getAllLocations();
        var ids = locations.stream().mapToInt(Location::getLocationId).toArray();
        // ids = [1, 2, 3, 4, 5, ... 15]

        // Current time - used to set realistic dates
        LocalDateTime now = LocalDateTime.now();

        // =============================================
        // CREATE 10 SERVICE REQUESTS
        // =============================================

        // Request 1: EMERGENCY - from Emergency Unit to Maternity Ward
        // Deadline: 1 hour from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[0], ids[2], "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1))
        );

        // Request 2: Pharmacy - from Pharmacy to Emergency Unit
        // Deadline: 8 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[1], ids[0], "Pharmacy", UrgencyLevel.MODERATE, now.plusHours(8))
        );

        // Request 3: Surgery - from Surgical Ward to ICU
        // Deadline: 4 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[3], ids[4], "Surgery", UrgencyLevel.URGENT, now.plusHours(4))
        );

        // Request 4: Lab Test - from Outpatient Dept to Radiology
        // Deadline: 24 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[5], ids[7], "Lab Test", UrgencyLevel.LOW, now.plusHours(24))
        );

        // Request 5: EMERGENCY - from Emergency Unit to Admin Building
        // Deadline: 30 minutes from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[0], ids[8], "Emergency", UrgencyLevel.EMERGENCY, now.plusMinutes(30))
        );

        // Request 6: Maternity - from Maternity Ward to Laboratory
        // Deadline: 5 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[2], ids[6], "Maternity", UrgencyLevel.MODERATE, now.plusHours(5))
        );

        // Request 7: Radiology - from Radiology to Surgical Ward
        // Deadline: 12 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[7], ids[3], "Radiology", UrgencyLevel.LOW, now.plusHours(12))
        );

        // Request 8: Admin - from Admin Building to Emergency Unit
        // Deadline: 16 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[8], ids[0], "Admin", UrgencyLevel.VERY_LOW, now.plusHours(16))
        );

        // Request 9: ICU Transfer - from ICU to Surgical Ward
        // Deadline: 30 minutes from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[4], ids[3], "ICU Transfer", UrgencyLevel.URGENT, now.plusMinutes(30))
        );

        // Request 10: Lab Results - from Laboratory to Pharmacy
        // Deadline: 2 hours from now
        requestDAO.insertRequest(
                new ServiceRequest(ids[6], ids[1], "Lab Results", UrgencyLevel.MODERATE, now.plusHours(2))
        );

        // =============================================
        // ADD ONE COMPLETED REQUEST
        // =============================================

        // This request is from the past and is already completed
        LocalDateTime pastTime = now.minusHours(3);  // 3 hours ago

        // Create a completed Lab Test request
        ServiceRequest completed = new ServiceRequest(
                ids[5], ids[7],          // Source: Outpatient, Destination: Radiology
                "Lab Test",              // Category
                UrgencyLevel.LOW,        // Low urgency
                pastTime.plusHours(2)    // Deadline was 2 hours after submission
        );

        // Set the submission time to the past
        completed.setTimeSubmitted(pastTime);

        // Mark it as COMPLETED
        completed.setStatus(RequestStatus.COMPLETED);

        // Insert it into the database
        requestDAO.insertRequest(completed);
    }

    // =============================================
    // SEED RESOURCES - Creates hospital resources
    // =============================================

    /**
     * Seed resources - adds all sample resources to the database
     * Resources include: Ambulances, Doctors, Nurses, Equipment, Beds
     *
     * @throws SQLException if there's a database error
     */
    private void seedResources() throws SQLException {
        // Get ALL locations from the database
        var locations = locationDAO.getAllLocations();
        var ids = locations.stream().mapToInt(Location::getLocationId).toArray();
        // ids = [1, 2, 3, 4, 5, ... 15]

        // =============================================
        // KORLE-BU TEACHING HOSPITAL RESOURCES
        // =============================================

        // Resource 1: Ambulance at Ambulance Bay (ID 10), capacity 4 patients, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Ambulance", ids[9], 4, ResourceStatus.AVAILABLE)
        );

        // Resource 2: Ambulance at Ambulance Bay (ID 10), capacity 4, IN USE
        resourceDAO.insertResource(
                new Resource("Ambulance", ids[9], 4, ResourceStatus.IN_USE)
        );

        // Resource 3: Doctor at Surgical Ward (ID 4), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Doctor", ids[3], 1, ResourceStatus.AVAILABLE)
        );

        // Resource 4: Nurse at Emergency Unit (ID 1), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Nurse", ids[0], 1, ResourceStatus.AVAILABLE)
        );

        // Resource 5: Operating Room at Surgical Ward (ID 4), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Operating Room", ids[3], 1, ResourceStatus.AVAILABLE)
        );

        // Resource 6: ICU Bed at ICU (ID 5), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("ICU Bed", ids[4], 1, ResourceStatus.AVAILABLE)
        );

        // Resource 7: ICU Bed at ICU (ID 5), capacity 1, IN USE
        resourceDAO.insertResource(
                new Resource("ICU Bed", ids[4], 1, ResourceStatus.IN_USE)
        );

        // Resource 8: X-Ray Machine at Radiology (ID 8), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("X-Ray Machine", ids[7], 1, ResourceStatus.AVAILABLE)
        );

        // Resource 9: Pharmacy Van at Pharmacy (ID 2), capacity 10, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Pharmacy Van", ids[1], 10, ResourceStatus.AVAILABLE)
        );

        // Resource 10: Ward Bed at Maternity Ward (ID 3), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Ward Bed", ids[2], 1, ResourceStatus.AVAILABLE)
        );

        // Resource 11: Ventilator at ICU (ID 5), capacity 1, UNDER MAINTENANCE
        resourceDAO.insertResource(
                new Resource("Ventilator", ids[4], 1, ResourceStatus.UNDER_MAINTENANCE)
        );

        // Resource 12: Ultrasound Machine at Maternity Ward (ID 3), capacity 1, AVAILABLE
        resourceDAO.insertResource(
                new Resource("Ultrasound Machine", ids[2], 1, ResourceStatus.AVAILABLE)
        );
    }
}