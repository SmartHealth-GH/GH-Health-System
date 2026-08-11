package main.com.ug.optimizer.datastructures.deque;

import main.com.ug.optimizer.datastructures.deque.MyDeque;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;

/**
 * Unit tests for MyDeque
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyDequeTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyDeque");
        System.out.println("=".repeat(60));

        testAddFrontAndRemoveFront();
        testAddRearAndRemoveRear();
        testAddFrontAndRemoveRear();
        testAddRearAndRemoveFront();
        testPeekFrontAndRear();
        testIsEmptyAndSize();
        testClear();
        testContains();
        testUrgentRequestExample();
        testEdgeCases();
        testInvalidInputs();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("📊 RESULTS:");
        System.out.println("   ✅ Passed: " + passed);
        System.out.println("   ❌ Failed: " + failed);
        System.out.println("=".repeat(60));

        if (failed == 0) {
            System.out.println("🎉 ALL TESTS PASSED!");
        } else {
            System.out.println("⚠️ SOME TESTS FAILED!");
        }
    }

    // =============================================
    // TEST METHODS
    // =============================================

    private static void testAddFrontAndRemoveFront() {
        System.out.println("\n✅ Test: AddFront and RemoveFront");
        MyDeque<String> deque = new MyDeque<>();

        deque.addFront("A");
        deque.addFront("B");
        deque.addFront("C");

        assert deque.size() == 3 : "Size should be 3";
        assert deque.toString().equals("[C, B, A]") : "Deque should be [C, B, A]";

        String removed = deque.removeFront();
        assert removed.equals("C") : "Removed should be 'C'";
        assert deque.size() == 2 : "Size should be 2";
        assert deque.toString().equals("[B, A]") : "Deque should be [B, A]";

        removed = deque.removeFront();
        assert removed.equals("B") : "Removed should be 'B'";
        assert deque.size() == 1 : "Size should be 1";
        assert deque.toString().equals("[A]") : "Deque should be [A]";

        removed = deque.removeFront();
        assert removed.equals("A") : "Removed should be 'A'";
        assert deque.size() == 0 : "Size should be 0";
        assert deque.isEmpty() : "Deque should be empty";
        assert deque.toString().equals("[]") : "Deque should be []";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testAddRearAndRemoveRear() {
        System.out.println("\n✅ Test: AddRear and RemoveRear");
        MyDeque<String> deque = new MyDeque<>();

        deque.addRear("A");
        deque.addRear("B");
        deque.addRear("C");

        assert deque.size() == 3 : "Size should be 3";
        assert deque.toString().equals("[A, B, C]") : "Deque should be [A, B, C]";

        String removed = deque.removeRear();
        assert removed.equals("C") : "Removed should be 'C'";
        assert deque.size() == 2 : "Size should be 2";
        assert deque.toString().equals("[A, B]") : "Deque should be [A, B]";

        removed = deque.removeRear();
        assert removed.equals("B") : "Removed should be 'B'";
        assert deque.size() == 1 : "Size should be 1";
        assert deque.toString().equals("[A]") : "Deque should be [A]";

        removed = deque.removeRear();
        assert removed.equals("A") : "Removed should be 'A'";
        assert deque.size() == 0 : "Size should be 0";
        assert deque.isEmpty() : "Deque should be empty";
        assert deque.toString().equals("[]") : "Deque should be []";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testAddFrontAndRemoveRear() {
        System.out.println("\n✅ Test: AddFront and RemoveRear (Mixed)");
        MyDeque<String> deque = new MyDeque<>();

        deque.addFront("A");
        deque.addFront("B");
        deque.addFront("C");

        assert deque.toString().equals("[C, B, A]") : "Deque should be [C, B, A]";

        String removed = deque.removeRear();
        assert removed.equals("A") : "Removed should be 'A'";
        assert deque.toString().equals("[C, B]") : "Deque should be [C, B]";

        removed = deque.removeRear();
        assert removed.equals("B") : "Removed should be 'B'";
        assert deque.toString().equals("[C]") : "Deque should be [C]";

        removed = deque.removeRear();
        assert removed.equals("C") : "Removed should be 'C'";
        assert deque.isEmpty() : "Deque should be empty";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testAddRearAndRemoveFront() {
        System.out.println("\n✅ Test: AddRear and RemoveFront (Mixed)");
        MyDeque<String> deque = new MyDeque<>();

        deque.addRear("A");
        deque.addRear("B");
        deque.addRear("C");

        assert deque.toString().equals("[A, B, C]") : "Deque should be [A, B, C]";

        String removed = deque.removeFront();
        assert removed.equals("A") : "Removed should be 'A'";
        assert deque.toString().equals("[B, C]") : "Deque should be [B, C]";

        removed = deque.removeFront();
        assert removed.equals("B") : "Removed should be 'B'";
        assert deque.toString().equals("[C]") : "Deque should be [C]";

        removed = deque.removeFront();
        assert removed.equals("C") : "Removed should be 'C'";
        assert deque.isEmpty() : "Deque should be empty";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testPeekFrontAndRear() {
        System.out.println("\n✅ Test: PeekFront and PeekRear");
        MyDeque<String> deque = new MyDeque<>();

        deque.addRear("A");
        deque.addRear("B");
        deque.addRear("C");

        assert deque.peekFront().equals("A") : "Front should be 'A'";
        assert deque.peekRear().equals("C") : "Rear should be 'C'";
        assert deque.size() == 3 : "Size should remain 3";

        deque.addFront("X");
        assert deque.peekFront().equals("X") : "Front should be 'X' after adding front";
        assert deque.peekRear().equals("C") : "Rear should still be 'C'";
        assert deque.size() == 4 : "Size should be 4";

        passed++;
        System.out.println("   ✅ Passed - Front: " + deque.peekFront() + ", Rear: " + deque.peekRear());
    }

    private static void testIsEmptyAndSize() {
        System.out.println("\n✅ Test: IsEmpty and Size");
        MyDeque<String> deque = new MyDeque<>();

        assert deque.isEmpty() : "Should be empty";
        assert deque.size() == 0 : "Size should be 0";

        deque.addFront("A");
        assert !deque.isEmpty() : "Should not be empty";
        assert deque.size() == 1 : "Size should be 1";

        deque.removeFront();
        assert deque.isEmpty() : "Should be empty again";
        assert deque.size() == 0 : "Size should be 0 again";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyDeque<String> deque = new MyDeque<>();

        deque.addFront("A");
        deque.addRear("B");
        deque.addFront("C");

        deque.clear();
        assert deque.isEmpty() : "Should be empty after clear";
        assert deque.size() == 0 : "Size should be 0 after clear";
        assert deque.toString().equals("[]") : "Deque should be [] after clear";

        // Can add after clear
        deque.addFront("X");
        assert deque.size() == 1 : "Should be able to add after clear";
        assert deque.peekFront().equals("X") : "Front should be 'X'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyDeque<String> deque = new MyDeque<>();

        deque.addRear("A");
        deque.addRear("B");
        deque.addRear("C");

        assert deque.contains("A") : "Should contain 'A'";
        assert deque.contains("B") : "Should contain 'B'";
        assert deque.contains("C") : "Should contain 'C'";
        assert !deque.contains("X") : "Should not contain 'X'";

        deque.removeFront();  // Remove A
        assert !deque.contains("A") : "Should not contain 'A' after removal";
        assert deque.contains("B") : "Should still contain 'B'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testUrgentRequestExample() {
        System.out.println("\n✅ Test: Urgent Request Example (ServiceRequest)");
        MyDeque<ServiceRequest> requestQueue = new MyDeque<>();

        LocalDateTime now = LocalDateTime.now();

        // Normal requests go to the rear
        ServiceRequest normal1 = new ServiceRequest(1, 2, "Routine Checkup", UrgencyLevel.LOW, now.plusHours(24));
        ServiceRequest normal2 = new ServiceRequest(2, 3, "Pharmacy Refill", UrgencyLevel.LOW, now.plusHours(12));

        requestQueue.addRear(normal1);
        requestQueue.addRear(normal2);

        assert requestQueue.size() == 2 : "Should have 2 normal requests";
        assert requestQueue.peekFront().getCategory().equals("Routine Checkup") : "First should be Routine Checkup";

        // URGENT request goes to the front!
        ServiceRequest urgent = new ServiceRequest(3, 4, "Emergency Surgery", UrgencyLevel.EMERGENCY, now.plusHours(1));
        requestQueue.addFront(urgent);

        assert requestQueue.size() == 3 : "Should have 3 total requests";
        assert requestQueue.peekFront().getCategory().equals("Emergency Surgery") : "URGENT should be at front!";

        // Process urgent first
        ServiceRequest processed = requestQueue.removeFront();
        assert processed.getCategory().equals("Emergency Surgery") : "Urgent request processed first!";

        // Then normal requests
        assert requestQueue.peekFront().getCategory().equals("Routine Checkup") : "Routine should be next";

        // Add another urgent
        ServiceRequest urgent2 = new ServiceRequest(4, 5, "Heart Attack", UrgencyLevel.EMERGENCY, now.plusMinutes(30));
        requestQueue.addFront(urgent2);
        assert requestQueue.peekFront().getCategory().equals("Heart Attack") : "New urgent should be at front!";

        System.out.println("   ✅ Passed - Urgent requests jump to front!");
        System.out.println("   📋 Deque used as priority system: URGENT → Normal → Normal");

        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyDeque<String> deque = new MyDeque<>();

        // Empty deque
        assert deque.isEmpty() : "Should be empty";
        assert deque.size() == 0 : "Size should be 0";
        assert deque.toString().equals("[]") : "toString() should be []";

        // Single element
        deque.addFront("Only");
        assert deque.size() == 1 : "Size should be 1";
        assert deque.peekFront().equals("Only") : "Front should be 'Only'";
        assert deque.peekRear().equals("Only") : "Rear should be 'Only'";
        assert deque.removeFront().equals("Only") : "RemoveFront should be 'Only'";
        assert deque.isEmpty() : "Should be empty after remove";

        // Null element
        deque.addFront(null);
        assert deque.size() == 1 : "Size should be 1";
        assert deque.peekFront() == null : "Front should be null";
        assert deque.contains(null) : "Should contain null";
        deque.removeFront();
        assert deque.isEmpty() : "Should be empty after removing null";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyDeque<String> deque = new MyDeque<>();

        // Remove from empty
        try {
            deque.removeFront();
            System.out.println("   ❌ Failed: removeFront() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            deque.removeRear();
            System.out.println("   ❌ Failed: removeRear() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        // Peek from empty
        try {
            deque.peekFront();
            System.out.println("   ❌ Failed: peekFront() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            deque.peekRear();
            System.out.println("   ❌ Failed: peekRear() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs threw exceptions");
    }
}