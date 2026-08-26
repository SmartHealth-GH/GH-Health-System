package main.com.ug.optimizer.model; // This file belongs to the 'model' package

import java.time.LocalDateTime; // For date/time handling
import java.time.format.DateTimeFormatter; // For formatting dates in a readable way

/**
 * Represents a performance test run for an algorithm
 * Maps to the 'algorithm_runs' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class AlgorithmRun {

    // =============================================
    // FIELDS - These are the data each AlgorithmRun stores
    // =============================================

    private int runId;                   // Unique ID for this performance test
    private String algorithmName;        // Which algorithm was tested? (e.g., "BinarySearch", "MergeSort")
    private int inputSize;               // How big was the input? (e.g., 1000 elements)
    private long timeNs;                 // How long did it take? (in nanoseconds)
    private long memoryKb;               // How much memory did it use? (in KB)
    private LocalDateTime dateRun;       // When was the test run?

    // =============================================
    // CONSTRUCTORS - Ways to create an AlgorithmRun
    // =============================================

    /**
     * Default constructor - creates an empty AlgorithmRun
     * Sets dateRun to current time automatically
     */
    public AlgorithmRun() {
        this.dateRun = LocalDateTime.now(); // Default to current time
    }

    /**
     * Full constructor - creates an AlgorithmRun with ALL values provided
     */
    public AlgorithmRun(int runId, String algorithmName, int inputSize,
                        long timeNs, long memoryKb, LocalDateTime dateRun) {
        this.runId = runId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeNs = timeNs;
        this.memoryKb = memoryKb;
        this.dateRun = dateRun;
    }

    /**
     * Constructor without ID - for NEW records
     * Database will auto-generate the ID when saved
     * DateRun defaults to current time
     */
    public AlgorithmRun(String algorithmName, int inputSize,
                        long timeNs, long memoryKb) {
        this(0, algorithmName, inputSize, timeNs, memoryKb, LocalDateTime.now());
    }

    // =============================================
    // GETTERS - Allow other classes to READ the data
    // =============================================

    public int getRunId() { return runId; }
    public String getAlgorithmName() { return algorithmName; }
    public int getInputSize() { return inputSize; }
    public long getTimeNs() { return timeNs; }
    public long getMemoryKb() { return memoryKb; }
    public LocalDateTime getDateRun() { return dateRun; }

    // =============================================
    // SETTERS - Allow other classes to UPDATE the data
    // =============================================

    /**
     * Sets the run ID
     * Validates: ID cannot be negative
     */
    public void setRunId(int runId) {
        if (runId < 0) {
            throw new IllegalArgumentException("Run ID cannot be negative");
        }
        this.runId = runId;
    }

    /**
     * Sets the algorithm name
     * Validates: Name cannot be null or empty
     */
    public void setAlgorithmName(String algorithmName) {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException("Algorithm name cannot be null or empty");
        }
        this.algorithmName = algorithmName.trim();
    }

    /**
     * Sets the input size
     * Validates: Size cannot be negative
     */
    public void setInputSize(int inputSize) {
        if (inputSize < 0) {
            throw new IllegalArgumentException("Input size cannot be negative");
        }
        this.inputSize = inputSize;
    }

    /**
     * Sets the execution time
     * Validates: Time cannot be negative
     */
    public void setTimeNs(long timeNs) {
        if (timeNs < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        this.timeNs = timeNs;
    }

    /**
     * Sets the memory usage
     * Validates: Memory cannot be negative
     */
    public void setMemoryKb(long memoryKb) {
        if (memoryKb < 0) {
            throw new IllegalArgumentException("Memory cannot be negative");
        }
        this.memoryKb = memoryKb;
    }

    /**
     * Sets the run date
     * Validates: Date cannot be null
     */
    public void setDateRun(LocalDateTime dateRun) {
        if (dateRun == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.dateRun = dateRun;
    }

    // =============================================
    // HELPER METHODS - Extra useful functions
    // =============================================

    /**
     * Converts time from nanoseconds to milliseconds
     * Useful for displaying time in a more readable format
     */
    public double getTimeMs() {
        return timeNs / 1_000_000.0; // 1 ms = 1,000,000 ns
    }

    /**
     * Converts time from nanoseconds to seconds
     * Useful for larger time measurements
     */
    public double getTimeSeconds() {
        return timeNs / 1_000_000_000.0; // 1 second = 1,000,000,000 ns
    }

    /**
     * Converts memory from KB to MB
     * Useful for displaying memory in a more readable format
     */
    public double getMemoryMb() {
        return memoryKb / 1024.0; // 1 MB = 1024 KB
    }

    /**
     * Returns a formatted date string
     * Example: "2026-08-13 14:30:45"
     * Returns "Unknown" if date is null
     */
    public String getFormattedDate() {
        return dateRun != null ?
                dateRun.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :
                "Unknown";
    }

    // =============================================
    // STANDARD METHODS - toString, equals, hashCode
    // =============================================

    /**
     * Returns a readable string representation of the AlgorithmRun
     * Example: AlgorithmRun{id=1, algorithm='BinarySearch', size=10000, time=15.234ms, memory=512.00KB, date='2026-08-13 14:30:45'}
     * Useful for debugging and reporting
     */
    @Override
    public String toString() {
        return String.format("AlgorithmRun{id=%d, algorithm='%s', size=%d, " +
                        "time=%.3fms, memory=%.2fKB, date='%s'}",
                runId, algorithmName, inputSize,
                getTimeMs(), (double) memoryKb,
                getFormattedDate());
    }

    /**
     * Compares two AlgorithmRun objects for equality
     * Two AlgorithmRuns are equal if they have the SAME ID
     * This is how database entities are compared
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmRun that = (AlgorithmRun) o;
        return runId == that.runId;
    }

    /**
     * Returns a hash code for the AlgorithmRun
     * Required when overriding equals()
     * Used in collections like HashMap and HashSet
     */
    @Override
    public int hashCode() {
        return Integer.hashCode(runId);
    }
}