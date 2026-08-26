package main.com.ug.optimizer.model; // This file belongs to the 'model' package

import main.com.ug.optimizer.model.enums.RequestStatus; // Bring in RequestStatus enum (PENDING, COMPLETED, etc.)
import main.com.ug.optimizer.model.enums.UrgencyLevel; // Bring in UrgencyLevel enum (EMERGENCY, URGENT, etc.)

import java.time.LocalDateTime; // For date/time handling
import java.time.format.DateTimeFormatter; // For formatting dates (optional)

/**
 * Represents a service request in the hospital
 * Maps to the 'service_requests' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class ServiceRequest implements Comparable<ServiceRequest> { // Allows sorting by urgency in PriorityQueue

    // =============================================
    // FIELDS - These are the data each ServiceRequest stores
    // =============================================

    private int requestId;                   // Unique ID for this request
    private int sourceId;                    // Where is the request coming from? (Location ID)
    private int destinationId;               // Where does it need to go? (Location ID)
    private String category;                 // What type of request? (e.g., "Emergency", "Pharmacy")
    private UrgencyLevel urgency;            // How urgent is this? (ENUM: EMERGENCY, URGENT, etc.)
    private LocalDateTime timeSubmitted;     // When was this request created?
    private LocalDateTime deadline;           // When must it be completed by?
    private RequestStatus status;             // What is the current status? (ENUM: PENDING, COMPLETED, etc.)

    // Additional fields for convenience (hold actual Location objects)
    private Location source;                  // Full Location object for the source
    private Location destination;             // Full Location object for the destination

    // =============================================
    // CONSTRUCTORS - Ways to create a ServiceRequest
    // =============================================

    /**
     * Default constructor - creates an empty request with default values
     * Default: timeSubmitted = now, status = PENDING
     */
    public ServiceRequest() {
        this.timeSubmitted = LocalDateTime.now(); // Set submission time to current time
        this.status = RequestStatus.PENDING; // New requests start as PENDING
    }

    /**
     * Full constructor - creates a ServiceRequest with ALL values provided
     */
    public ServiceRequest(int requestId, int sourceId, int destinationId,
                          String category, UrgencyLevel urgency,
                          LocalDateTime timeSubmitted, LocalDateTime deadline,
                          RequestStatus status) {
        this.requestId = requestId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    /**
     * Constructor without ID - for NEW records
     * Database will auto-generate the ID when saved
     * Status defaults to PENDING, timeSubmitted defaults to now
     */
    public ServiceRequest(int sourceId, int destinationId,
                          String category, UrgencyLevel urgency,
                          LocalDateTime deadline) {
        this(0, sourceId, destinationId, category, urgency,
                LocalDateTime.now(), deadline, RequestStatus.PENDING);
    }

    /**
     * Convenience constructor - accepts urgency as an int
     * Converts int to enum using UrgencyLevel.fromValue()
     * Useful when loading from database where urgency is stored as number
     */
    public ServiceRequest(int sourceId, int destinationId,
                          String category, int urgency,
                          LocalDateTime deadline) {
        this(sourceId, destinationId, category,
                UrgencyLevel.fromValue(urgency), deadline);
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public int getRequestId() { return requestId; }
    public int getSourceId() { return sourceId; }
    public int getDestinationId() { return destinationId; }
    public String getCategory() { return category; }
    public UrgencyLevel getUrgency() { return urgency; }
    public LocalDateTime getTimeSubmitted() { return timeSubmitted; }
    public LocalDateTime getDeadline() { return deadline; }
    public RequestStatus getStatus() { return status; }
    public Location getSource() { return source; }
    public Location getDestination() { return destination; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // Each setter includes VALIDATION to prevent bad data
    // =============================================

    /**
     * Sets the request ID
     * Validates: ID cannot be negative
     */
    public void setRequestId(int requestId) {
        if (requestId < 0) {
            throw new IllegalArgumentException("Request ID cannot be negative");
        }
        this.requestId = requestId;
    }

    /**
     * Sets the source location ID
     * Validates: Source ID must be positive (valid location)
     */
    public void setSourceId(int sourceId) {
        if (sourceId <= 0) {
            throw new IllegalArgumentException("Source ID must be positive");
        }
        this.sourceId = sourceId;
    }

    /**
     * Sets the destination location ID
     * Validates: Destination ID must be positive (valid location)
     */
    public void setDestinationId(int destinationId) {
        if (destinationId <= 0) {
            throw new IllegalArgumentException("Destination ID must be positive");
        }
        this.destinationId = destinationId;
    }

    /**
     * Sets the category (e.g., "Emergency", "Pharmacy")
     * Validates: Category cannot be null or empty
     */
    public void setCategory(String category) {
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("Category cannot be null or empty");
        }
        this.category = category.trim();
    }

    /**
     * Sets the urgency using an ENUM directly
     * Validates: Urgency cannot be null
     */
    public void setUrgency(UrgencyLevel urgency) {
        if (urgency == null) {
            throw new IllegalArgumentException("Urgency cannot be null");
        }
        this.urgency = urgency;
    }

    /**
     * Sets the urgency from an INT (converts to enum)
     * Uses UrgencyLevel.fromValue() to convert
     */
    public void setUrgency(int urgency) {
        this.urgency = UrgencyLevel.fromValue(urgency);
    }

    /**
     * Sets the submission time
     * Validates: Time cannot be null
     */
    public void setTimeSubmitted(LocalDateTime timeSubmitted) {
        if (timeSubmitted == null) {
            throw new IllegalArgumentException("Time submitted cannot be null");
        }
        this.timeSubmitted = timeSubmitted;
    }

    /**
     * Sets the deadline
     * Validates: Deadline cannot be null
     * Note: Deadline validation is separate (validateDeadline() method)
     */
    public void setDeadline(LocalDateTime deadline) {
        if (deadline == null) {
            throw new IllegalArgumentException("Deadline cannot be null");
        }
        this.deadline = deadline;
    }

    /**
     * Sets the status using an ENUM directly
     * Validates: Status cannot be null
     */
    public void setStatus(RequestStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    /**
     * Sets the status from a STRING
     * Converts string to enum using RequestStatus.fromString()
     * Validates: String must be a valid status
     */
    public void setStatus(String status) {
        RequestStatus enumStatus = RequestStatus.fromString(status);
        if (enumStatus == null) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
        this.status = enumStatus;
    }

    /**
     * Sets the full Location objects (convenience fields)
     * No validation needed (can be null if not set yet)
     */
    public void setSource(Location source) { this.source = source; }
    public void setDestination(Location destination) { this.destination = destination; }

    // =============================================
    // HELPER METHODS - Extra useful functions
    // =============================================

    /**
     * Checks if the request is pending (PENDING or ON_HOLD)
     * Returns true if status is PENDING or ON_HOLD
     */
    public boolean isPending() {
        return status != null && status.isPending();
    }

    /**
     * Checks if the request is completed
     * Returns true if status is COMPLETED
     */
    public boolean isCompleted() {
        return status != null && status.isCompleted();
    }

    /**
     * Checks if the request is urgent (URGENT or EMERGENCY)
     * Returns true if urgency is URGENT or EMERGENCY
     */
    public boolean isUrgent() {
        return urgency != null && urgency.isUrgent();
    }

    /**
     * Checks if the request is an emergency
     * Returns true if urgency is EMERGENCY
     */
    public boolean isEmergency() {
        return urgency != null && urgency.isEmergency();
    }

    /**
     * Returns a user-friendly label for urgency
     * e.g., "Emergency" instead of "EMERGENCY"
     * Returns "Unknown" if urgency is null
     */
    public String getUrgencyLabel() {
        return urgency != null ? urgency.getDisplayName() : "Unknown";
    }

    /**
     * Returns a user-friendly label for status
     * e.g., "Pending" instead of "PENDING"
     * Returns "Unknown" if status is null
     */
    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : "Unknown";
    }

    /**
     * Checks if the request is overdue
     * A request is overdue if:
     * 1. It is NOT completed
     * 2. The deadline has passed (now > deadline)
     */
    public boolean isOverdue() {
        return !isCompleted() &&
                deadline != null &&
                LocalDateTime.now().isAfter(deadline);
    }

    /**
     * Validates that the deadline is after the submission time
     * Throws exception if deadline is before submission time
     * This ensures logical consistency
     */
    public void validateDeadline() {
        if (deadline != null && timeSubmitted != null &&
                deadline.isBefore(timeSubmitted)) {
            throw new IllegalArgumentException("Deadline cannot be before submission time");
        }
    }

    // =============================================
    // 🔥 COMPARABLE IMPLEMENTATION FOR PRIORITY QUEUE
    // =============================================

    /**
     * Compares this ServiceRequest with another by urgency level.
     * Higher urgency (EMERGENCY) should come first (smaller value).
     *
     * This is used by MyPriorityQueue to sort requests by urgency.
     *
     * @param other The other ServiceRequest to compare with
     * @return negative if this is more urgent, positive if less urgent
     */
    @Override
    public int compareTo(ServiceRequest other) {
        // EMERGENCY = 5, URGENT = 4, MODERATE = 3, LOW = 2, VERY_LOW = 1
        // We want EMERGENCY (5) to come before VERY_LOW (1)
        // So we compare in REVERSE order: higher urgency = smaller value
        return Integer.compare(this.urgency.getValue(), other.urgency.getValue());
    }

    // =============================================
    // STANDARD METHODS - toString, equals, hashCode
    // =============================================

    /**
     * Returns a readable string representation of the ServiceRequest
     * Example: ServiceRequest{id=1, category='Emergency', urgency='Emergency', status='Pending', overdue=false}
     * Useful for debugging and logging
     */
    @Override
    public String toString() {
        return String.format("ServiceRequest{id=%d, category='%s', urgency='%s', " +
                        "status='%s', overdue=%s}",
                requestId, category,
                urgency != null ? urgency.getDisplayName() : "Unknown",
                status != null ? status.getDisplayName() : "Unknown",
                isOverdue());
    }

    /**
     * Compares two ServiceRequest objects for equality
     * Two ServiceRequests are equal if they have the SAME ID
     * This is how database entities are compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceRequest that = (ServiceRequest) o;
        return requestId == that.requestId;
    }

    /**
     * Returns a hash code for the ServiceRequest
     * Required when overriding equals()
     * Used in collections like HashMap and HashSet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(requestId);
    }
}