package main.com.ug.optimizer.Database.mapAlgorithm;

import main.com.ug.optimizer.Database.LocationDAO;
import main.com.ug.optimizer.Database.ResourceDAO;
import main.com.ug.optimizer.Database.RoadDAO;
import main.com.ug.optimizer.Database.ServiceRequestDAO;
import main.com.ug.optimizer.model.*;
import main.com.ug.optimizer.model.enums.*;

import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Seeds the database with sample data for testing
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DataSeeder {

    private LocationDAO locationDAO;
    private RoadDAO roadDAO;
    private ServiceRequestDAO requestDAO;
    private ResourceDAO resourceDAO;

    public DataSeeder() {
        this.locationDAO = new LocationDAO();
        this.roadDAO = new RoadDAO();
        this.requestDAO = new ServiceRequestDAO();
        this.resourceDAO = new ResourceDAO();
    }

    /**
     * Seed all sample data
     */
    public void seedAll() throws SQLException {
        System.out.println("🌱 Seeding sample data...");

        // Step 1: Seed locations
        int emergencyId = seedLocations();

        // Step 2: Seed roads
        seedRoads(emergencyId);

        // Step 3: Seed service requests
        seedRequests(emergencyId);

        // Step 4: Seed resources
        seedResources(emergencyId);

        System.out.println("✅ Sample data seeded successfully!");
        System.out.println("   " + locationDAO.countLocations() + " locations");
        System.out.println("   " + roadDAO.countRoads() + " roads");
        System.out.println("   " + requestDAO.countRequests() + " requests");
        System.out.println("   " + resourceDAO.countResources() + " resources");
    }

    /**
     * Seed locations
     */
    private int seedLocations() throws SQLException {
        // Insert locations and store their IDs
        int emergencyId = locationDAO.insertLocation(new Location("Emergency Unit", "Korle-Bu", LocationType.EMERGENCY, 5.5405, -0.2038));
        int pharmacyId = locationDAO.insertLocation(new Location("Pharmacy", "Korle-Bu", LocationType.PHARMACY, 5.5408, -0.2040));
        int maternityId = locationDAO.insertLocation(new Location("Maternity Ward", "Korle-Bu", LocationType.WARD, 5.5415, -0.2050));
        int surgicalId = locationDAO.insertLocation(new Location("Surgical Ward", "Korle-Bu", LocationType.WARD, 5.5410, -0.2045));
        int icuId = locationDAO.insertLocation(new Location("ICU", "Korle-Bu", LocationType.ICU, 5.5402, -0.2035));
        int opdId = locationDAO.insertLocation(new Location("Outpatient Dept", "Korle-Bu", LocationType.OPD, 5.5418, -0.2042));
        int labId = locationDAO.insertLocation(new Location("Laboratory", "Korle-Bu", LocationType.LABORATORY, 5.5400, -0.2048));
        int radiologyId = locationDAO.insertLocation(new Location("Radiology", "Korle-Bu", LocationType.RADIOLOGY, 5.5407, -0.2030));
        int adminId = locationDAO.insertLocation(new Location("Admin Building", "Korle-Bu", LocationType.ADMINISTRATION, 5.5420, -0.2055));
        int ambulanceBayId = locationDAO.insertLocation(new Location("Ambulance Bay", "Korle-Bu", LocationType.TRANSPORT, 5.5410, -0.2060));

        locationDAO.insertLocation(new Location("Staff Quarters", "Korle-Bu", LocationType.STAFF, 5.5425, -0.2058));
        locationDAO.insertLocation(new Location("Records Office", "Korle-Bu", LocationType.RECORDS, 5.5409, -0.2059));
        locationDAO.insertLocation(new Location("Blood Bank", "Korle-Bu", LocationType.BLOOD_BANK, 5.5403, -0.2049));
        locationDAO.insertLocation(new Location("Dialysis Unit", "Korle-Bu", LocationType.DIALYSIS, 5.5412, -0.2041));
        locationDAO.insertLocation(new Location("Endoscopy Unit", "Korle-Bu", LocationType.ENDOSCOPY, 5.5406, -0.2053));

        return emergencyId;
    }

    /**
     * Seed roads
     */
    private void seedRoads(int emergencyId) throws SQLException {
        // Get all location IDs
        var locations = locationDAO.getAllLocations();
        var ids = locations.stream().mapToInt(Location::getLocationId).toArray();

        // Create connections between locations
        for (int i = 0; i < ids.length - 1; i++) {
            roadDAO.insertRoad(new Road(ids[i], ids[i + 1], 0.3 + (i * 0.1), 5 + i, 0.8 + (i * 0.05)));
        }

        // Add some extra connections
        roadDAO.insertRoad(new Road(ids[0], ids[2], 0.4, 6, 1.0));  // Emergency -> Maternity
        roadDAO.insertRoad(new Road(ids[1], ids[4], 0.5, 7, 0.9));  // Pharmacy -> ICU
        roadDAO.insertRoad(new Road(ids[3], ids[6], 0.6, 8, 1.1));  // Surgical -> Lab
    }

    /**
     * Seed service requests
     */
    private void seedRequests(int emergencyId) throws SQLException {
        LocalDateTime now = LocalDateTime.now();

        // Get location IDs
        var locations = locationDAO.getAllLocations();
        var ids = locations.stream().mapToInt(Location::getLocationId).toArray();
        int lastIndex = ids.length - 1;

        requestDAO.insertRequest(new ServiceRequest(ids[0], ids[2], "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1)));
        requestDAO.insertRequest(new ServiceRequest(ids[1], ids[0], "Pharmacy", UrgencyLevel.MODERATE, now.plusHours(8)));
        requestDAO.insertRequest(new ServiceRequest(ids[3], ids[4], "Surgery", UrgencyLevel.URGENT, now.plusHours(4)));
        requestDAO.insertRequest(new ServiceRequest(ids[5], ids[7], "Lab Test", UrgencyLevel.LOW, now.plusHours(24)));
        requestDAO.insertRequest(new ServiceRequest(ids[0], ids[8], "Emergency", UrgencyLevel.EMERGENCY, now.plusMinutes(30)));
        requestDAO.insertRequest(new ServiceRequest(ids[2], ids[6], "Maternity", UrgencyLevel.MODERATE, now.plusHours(5)));
        requestDAO.insertRequest(new ServiceRequest(ids[7], ids[3], "Radiology", UrgencyLevel.LOW, now.plusHours(12)));
        requestDAO.insertRequest(new ServiceRequest(ids[8], ids[0], "Admin", UrgencyLevel.VERY_LOW, now.plusHours(16)));
        requestDAO.insertRequest(new ServiceRequest(ids[4], ids[3], "ICU Transfer", UrgencyLevel.URGENT, now.plusMinutes(30)));
        requestDAO.insertRequest(new ServiceRequest(ids[6], ids[1], "Lab Results", UrgencyLevel.MODERATE, now.plusHours(2)));

        // Add one completed request
        ServiceRequest completed = new ServiceRequest(ids[5], ids[7], "Lab Test", UrgencyLevel.LOW, now.minusHours(3));
        completed.setStatus(RequestStatus.COMPLETED);
        requestDAO.insertRequest(completed);
    }

    /**
     * Seed resources
     */
    private void seedResources(int emergencyId) throws SQLException {
        // Get location IDs
        var locations = locationDAO.getAllLocations();
        var ids = locations.stream().mapToInt(Location::getLocationId).toArray();

        resourceDAO.insertResource(new Resource("Ambulance", ids[9], 4, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("Ambulance", ids[9], 4, ResourceStatus.IN_USE));
        resourceDAO.insertResource(new Resource("Doctor", ids[3], 1, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("Nurse", ids[0], 1, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("Operating Room", ids[3], 1, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("ICU Bed", ids[4], 1, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("ICU Bed", ids[4], 1, ResourceStatus.IN_USE));
        resourceDAO.insertResource(new Resource("X-Ray Machine", ids[7], 1, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("Pharmacy Van", ids[1], 10, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("Ward Bed", ids[2], 1, ResourceStatus.AVAILABLE));
        resourceDAO.insertResource(new Resource("Ventilator", ids[4], 1, ResourceStatus.UNDER_MAINTENANCE));
        resourceDAO.insertResource(new Resource("Ultrasound Machine", ids[2], 1, ResourceStatus.AVAILABLE));
    }
}