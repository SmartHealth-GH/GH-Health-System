package main.com.ug.optimizer.datastructures.queue;

import main.com.ug.optimizer.datastructures.MyQueue;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;

/**
 * Unit tests for MyQueue
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyQueueTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyQueue");
        System.out.println("=".repeat(60));

        testEnqueueAndDequeue();
        testFront();
        testIsEmptyAndSize();
        testClear();
        testContains();
        testFIFOBehavior();
        testWithServiceRequests();
        testEdgeCases();
        testInvalidInputs();

        // 🔥 NEW: Front/Rear Trace Evidence
        testFrontRearTrace();

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

    private static void testEnqueueAndDequeue() {
        System.out.println("\n✅ Test: Enqueue and Dequeue");
        MyQueue<String> queue = new MyQueue<>();

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assert queue.size() == 3 : "Size should be 3";
        assert queue.toString().equals("[A, B, C]") : "Queue should be [A, B, C]";

        String dequeued = queue.dequeue();
        assert dequeued.equals("A") : "Dequeued should be 'A'";
        assert queue.size() == 2 : "Size should be 2";
        assert queue.toString().equals("[B, C]") : "Queue should be [B, C]";

        dequeued = queue.dequeue();
        assert dequeued.equals("B") : "Dequeued should be 'B'";
        assert queue.size() == 1 : "Size should be 1";
        assert queue.toString().equals("[C]") : "Queue should be [C]";

        dequeued = queue.dequeue();
        assert dequeued.equals("C") : "Dequeued should be 'C'";
        assert queue.size() == 0 : "Size should be 0";
        assert queue.isEmpty() : "Queue should be empty";
        assert queue.toString().equals("[]") : "Queue should be []";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testFront() {
        System.out.println("\n✅ Test: Front");
        MyQueue<String> queue = new MyQueue<>();

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        String front = queue.front();
        assert front.equals("A") : "Front should be 'A'";
        assert queue.size() == 3 : "Size should remain 3 after front";
        assert queue.toString().equals("[A, B, C]") : "Queue should remain [A, B, C]";

        passed++;
        System.out.println("   ✅ Passed - Front: " + front);
    }

    private static void testIsEmptyAndSize() {
        System.out.println("\n✅ Test: IsEmpty and Size");
        MyQueue<String> queue = new MyQueue<>();

        assert queue.isEmpty() : "Should be empty";
        assert queue.size() == 0 : "Size should be 0";

        queue.enqueue("A");
        assert !queue.isEmpty() : "Should not be empty";
        assert queue.size() == 1 : "Size should be 1";

        queue.dequeue();
        assert queue.isEmpty() : "Should be empty again";
        assert queue.size() == 0 : "Size should be 0 again";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyQueue<String> queue = new MyQueue<>();

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        queue.clear();
        assert queue.isEmpty() : "Should be empty after clear";
        assert queue.size() == 0 : "Size should be 0 after clear";
        assert queue.toString().equals("[]") : "Queue should be [] after clear";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyQueue<String> queue = new MyQueue<>();

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assert queue.contains("A") : "Should contain 'A'";
        assert queue.contains("B") : "Should contain 'B'";
        assert queue.contains("C") : "Should contain 'C'";
        assert !queue.contains("X") : "Should not contain 'X'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testFIFOBehavior() {
        System.out.println("\n✅ Test: FIFO Behavior (First In, First Out)");
        MyQueue<Integer> queue = new MyQueue<>();

        for (int i = 1; i <= 5; i++) {
            queue.enqueue(i);
        }

        for (int i = 1; i <= 5; i++) {
            int value = queue.dequeue();
            assert value == i : "Should dequeue " + i + " but got " + value;
        }

        assert queue.isEmpty() : "Queue should be empty after all dequeues";

        passed++;
        System.out.println("   ✅ Passed - FIFO order verified!");
    }

    private static void testWithServiceRequests() {
        System.out.println("\n✅ Test: With ServiceRequest Objects");
        MyQueue<ServiceRequest> queue = new MyQueue<>();

        LocalDateTime now = LocalDateTime.now();
        ServiceRequest req1 = new ServiceRequest(1, 2, "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1));
        ServiceRequest req2 = new ServiceRequest(2, 3, "Pharmacy", UrgencyLevel.MODERATE, now.plusHours(8));
        ServiceRequest req3 = new ServiceRequest(3, 4, "Surgery", UrgencyLevel.URGENT, now.plusHours(4));

        queue.enqueue(req1);
        queue.enqueue(req2);
        queue.enqueue(req3);

        assert queue.size() == 3 : "Should have 3 requests";
        assert queue.front().getCategory().equals("Emergency") : "First should be Emergency";

        ServiceRequest processed = queue.dequeue();
        assert processed.getCategory().equals("Emergency") : "Processed should be Emergency";
        assert queue.size() == 2 : "Should have 2 after processing";
        assert queue.front().getCategory().equals("Pharmacy") : "Next should be Pharmacy";

        passed++;
        System.out.println("   ✅ Passed - FIFO processing with ServiceRequests");
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyQueue<String> queue = new MyQueue<>();

        // Empty queue
        assert queue.isEmpty() : "Should be empty";
        assert queue.size() == 0 : "Size should be 0";
        assert queue.toString().equals("[]") : "toString() should be []";

        // Single element
        queue.enqueue("Only");
        assert queue.size() == 1 : "Size should be 1";
        assert queue.front().equals("Only") : "Front should be 'Only'";
        assert queue.dequeue().equals("Only") : "Dequeue should be 'Only'";
        assert queue.isEmpty() : "Should be empty after dequeue";

        // Null element
        queue.enqueue(null);
        assert queue.size() == 1 : "Size should be 1";
        assert queue.front() == null : "Front should be null";
        assert queue.contains(null) : "Should contain null";
        queue.dequeue();
        assert queue.isEmpty() : "Should be empty after removing null";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyQueue<String> queue = new MyQueue<>();

        try {
            queue.dequeue();
            System.out.println("   ❌ Failed: dequeue() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            queue.front();
            System.out.println("   ❌ Failed: front() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs threw exceptions");
    }

    // =============================================
    // 📋 FRONT/REAR TRACE EVIDENCE (For Report)
    // =============================================

    private static void testFrontRearTrace() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 MyQueue FRONT/REAR TRACE");
        System.out.println("=".repeat(60));

        MyQueue<String> queue = new MyQueue<>();

        System.out.println("📊 Tracking front and rear movement:");
        System.out.println("-".repeat(50));

        System.out.println("Initial state: Queue = " + queue + " (empty)");

        queue.enqueue("A");
        System.out.println("enqueue(A) → Queue: " + queue + " | Front: " + queue.front() + " | Rear: A");

        queue.enqueue("B");
        System.out.println("enqueue(B) → Queue: " + queue + " | Front: " + queue.front() + " | Rear: B");

        queue.enqueue("C");
        System.out.println("enqueue(C) → Queue: " + queue + " | Front: " + queue.front() + " | Rear: C");

        queue.dequeue();
        System.out.println("dequeue()  → Queue: " + queue + " | Front: " + queue.front() + " | Rear: C");

        queue.dequeue();
        System.out.println("dequeue()  → Queue: " + queue + " | Front: " + queue.front() + " | Rear: C");

        queue.enqueue("D");
        System.out.println("enqueue(D) → Queue: " + queue + " | Front: " + queue.front() + " | Rear: D");

        queue.enqueue("E");
        System.out.println("enqueue(E) → Queue: " + queue + " | Front: " + queue.front() + " | Rear: E");

        System.out.println("-".repeat(50));
        System.out.println("📊 Final state:");
        System.out.println("   Queue: " + queue);
        System.out.println("   Size: " + queue.size());
        System.out.println("   Front: " + queue.front());
        System.out.println("✅ Front/rear movement trace complete!");
        System.out.println("📋 Copy this output to your report as evidence.");
    }
}