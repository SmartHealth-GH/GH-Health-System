package main.com.ug.optimizer.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a performance test run for an algorithm
 * Maps to the 'algorithm_runs' table in the database
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class AlgorithmRun {
    private int runId;
    private String algorithmName;
    private int inputSize;
    private long timeNs;    // Execution time in nanoseconds
    private long memoryKb;  // Memory used in KB
    private LocalDateTime dateRun;

    /**
     * Default constructor
     */
    public AlgorithmRun() {
        this.dateRun = LocalDateTime.now();
    }

    /**
     * Full constructor with ID
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
     * Constructor without ID
     */
    public AlgorithmRun(String algorithmName, int inputSize,
                        long timeNs, long memoryKb) {
        this(0, algorithmName, inputSize, timeNs, memoryKb, LocalDateTime.now());
    }

    // ===== Getters =====
    public int getRunId() { return runId; }
    public String getAlgorithmName() { return algorithmName; }
    public int getInputSize() { return inputSize; }
    public long getTimeNs() { return timeNs; }
    public long getMemoryKb() { return memoryKb; }
    public LocalDateTime getDateRun() { return dateRun; }

    // ===== Setters with Validation =====
    public void setRunId(int runId) {
        if (runId < 0) {
            throw new IllegalArgumentException("Run ID cannot be negative");
        }
        this.runId = runId;
    }

    public void setAlgorithmName(String algorithmName) {
        if (algorithmName == null || algorithmName.isBlank()) {
            throw new IllegalArgumentException("Algorithm name cannot be null or empty");
        }
        this.algorithmName = algorithmName.trim();
    }

    public void setInputSize(int inputSize) {
        if (inputSize < 0) {
            throw new IllegalArgumentException("Input size cannot be negative");
        }
        this.inputSize = inputSize;
    }

    public void setTimeNs(long timeNs) {
        if (timeNs < 0) {
            throw new IllegalArgumentException("Time cannot be negative");
        }
        this.timeNs = timeNs;
    }

    public void setMemoryKb(long memoryKb) {
        if (memoryKb < 0) {
            throw new IllegalArgumentException("Memory cannot be negative");
        }
        this.memoryKb = memoryKb;
    }

    public void setDateRun(LocalDateTime dateRun) {
        if (dateRun == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.dateRun = dateRun;
    }

    // ===== Helper Methods =====
    public double getTimeMs() {
        return timeNs / 1_000_000.0;
    }

    public double getTimeSeconds() {
        return timeNs / 1_000_000_000.0;
    }

    public double getMemoryMb() {
        return memoryKb / 1024.0;
    }

    public String getFormattedDate() {
        return dateRun != null ?
                dateRun.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) :
                "Unknown";
    }

    @Override
    public String toString() {
        return String.format("AlgorithmRun{id=%d, algorithm='%s', size=%d, " +
                        "time=%.3fms, memory=%.2fKB, date='%s'}",
                runId, algorithmName, inputSize,
                getTimeMs(), (double) memoryKb,
                getFormattedDate());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlgorithmRun that = (AlgorithmRun) o;
        return runId == that.runId;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(runId);
    }
}