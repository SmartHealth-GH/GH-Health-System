package main.com.ug.optimizer.datastructures.priorityqueue;

import main.com.ug.optimizer.datastructures.MyPriorityQueue;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;

/**
 * Unit tests for MyPriorityQueue
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyPriorityQueueTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyPriorityQueue");
        System.out.println("=".repeat(60));

        testInsertAndExtractMin();
        testPeekMin();
        testIsEmptyAndSize();
        testClear();
        testContains();
        testHeapProperty();
        testWithServiceRequests();
        testDispatchOrderTrace();
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

    private static void testInsertAndExtractMin() {
        System.out.println("\n✅ Test: Insert and ExtractMin");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        pq.insert(5);
        pq.insert(3);
        pq.insert(8);
        pq.insert(1);
        pq.insert(2);

        assert pq.size() == 5 : "Size should be 5";
        assert pq.peekMin() == 1 : "Min should be 1";

        int extracted = pq.extractMin();
        assert extracted == 1 : "Extracted should be 1";
        assert pq.size() == 4 : "Size should be 4";
        assert pq.peekMin() == 2 : "Min should be 2";

        extracted = pq.extractMin();
        assert extracted == 2 : "Extracted should be 2";

        extracted = pq.extractMin();
        assert extracted == 3 : "Extracted should be 3";

        extracted = pq.extractMin();
        assert extracted == 5 : "Extracted should be 5";

        extracted = pq.extractMin();
        assert extracted == 8 : "Extracted should be 8";

        assert pq.isEmpty() : "Queue should be empty";

        passed++;
        System.out.println("   ✅ Passed - Extracted in correct order: 1, 2, 3, 5, 8");
    }

    private static void testPeekMin() {
        System.out.println("\n✅ Test: PeekMin");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        pq.insert(5);
        pq.insert(3);
        pq.insert(8);

        int peeked = pq.peekMin();
        assert peeked == 3 : "Peek should be 3";
        assert pq.size() == 3 : "Size should remain 3 after peek";

        passed++;
        System.out.println("   ✅ Passed - Peek: " + peeked);
    }

    private static void testIsEmptyAndSize() {
        System.out.println("\n✅ Test: IsEmpty and Size");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        assert pq.isEmpty() : "Should be empty";
        assert pq.size() == 0 : "Size should be 0";

        pq.insert(1);
        assert !pq.isEmpty() : "Should not be empty";
        assert pq.size() == 1 : "Size should be 1";

        pq.extractMin();
        assert pq.isEmpty() : "Should be empty again";
        assert pq.size() == 0 : "Size should be 0";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        pq.insert(5);
        pq.insert(3);
        pq.insert(8);

        pq.clear();
        assert pq.isEmpty() : "Should be empty after clear";
        assert pq.size() == 0 : "Size should be 0";
        assert pq.toString().equals("[]") : "Queue should be [] after clear";

        // Can add after clear
        pq.insert(1);
        assert pq.size() == 1 : "Should be able to add after clear";
        assert pq.peekMin() == 1 : "Peek should be 1";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        pq.insert(5);
        pq.insert(3);
        pq.insert(8);

        assert pq.contains(3) : "Should contain 3";
        assert pq.contains(5) : "Should contain 5";
        assert pq.contains(8) : "Should contain 8";
        assert !pq.contains(1) : "Should not contain 1";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testHeapProperty() {
        System.out.println("\n✅ Test: Heap Property");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        // Insert random numbers
        int[] numbers = {10, 20, 5, 15, 3, 25, 7};
        for (int n : numbers) {
            pq.insert(n);
        }

        // Extract should return sorted order
        int[] expected = {3, 5, 7, 10, 15, 20, 25};
        for (int expectedValue : expected) {
            int extracted = pq.extractMin();
            assert extracted == expectedValue : "Expected " + expectedValue + " but got " + extracted;
        }

        assert pq.isEmpty() : "Queue should be empty after all extractions";

        passed++;
        System.out.println("   ✅ Passed - Heap maintains sorted order");
    }

    private static void testWithServiceRequests() {
        System.out.println("\n✅ Test: With ServiceRequest Objects");
        MyPriorityQueue<ServiceRequest> pq = new MyPriorityQueue<>();

        LocalDateTime now = LocalDateTime.now();

        // Create ServiceRequests with different urgency levels
        // ServiceRequest implements Comparable by urgency
        ServiceRequest emergency = new ServiceRequest(1, 2, "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1));
        ServiceRequest urgent = new ServiceRequest(2, 3, "Urgent Surgery", UrgencyLevel.URGENT, now.plusHours(4));
        ServiceRequest moderate = new ServiceRequest(3, 4, "Routine Checkup", UrgencyLevel.MODERATE, now.plusHours(8));
        ServiceRequest low = new ServiceRequest(4, 5, "Pharmacy Refill", UrgencyLevel.LOW, now.plusHours(24));

        // Insert in random order
        pq.insert(moderate);
        pq.insert(emergency);
        pq.insert(low);
        pq.insert(urgent);

        assert pq.size() == 4 : "Should have 4 requests";

        // Should extract in order of urgency: EMERGENCY, URGENT, MODERATE, LOW
        ServiceRequest extracted = pq.extractMin();
        assert extracted.getUrgency() == UrgencyLevel.EMERGENCY : "Emergency should be first";

        extracted = pq.extractMin();
        assert extracted.getUrgency() == UrgencyLevel.URGENT : "Urgent should be second";

        extracted = pq.extractMin();
        assert extracted.getUrgency() == UrgencyLevel.MODERATE : "Moderate should be third";

        extracted = pq.extractMin();
        assert extracted.getUrgency() == UrgencyLevel.LOW : "Low should be last";

        assert pq.isEmpty() : "Queue should be empty";

        passed++;
        System.out.println("   ✅ Passed - ServiceRequests ordered by urgency");
    }

    private static void testDispatchOrderTrace() {
        System.out.println("\n✅ Test: Dispatch Order Trace");
        System.out.println("   📋 Hospital Patient Dispatch Simulation");

        MyPriorityQueue<ServiceRequest> pq = new MyPriorityQueue<>();
        LocalDateTime now = LocalDateTime.now();

        // Simulate incoming patients
        System.out.println("   📥 Incoming patients:");
        ServiceRequest[] incoming = {
                new ServiceRequest(1, 2, "Chest Pain", UrgencyLevel.URGENT, now.plusHours(1)),
                new ServiceRequest(2, 3, "Broken Arm", UrgencyLevel.MODERATE, now.plusHours(2)),
                new ServiceRequest(3, 4, "Heart Attack", UrgencyLevel.EMERGENCY, now.plusMinutes(30)),
                new ServiceRequest(4, 5, "Headache", UrgencyLevel.LOW, now.plusHours(6)),
                new ServiceRequest(5, 6, "Stroke", UrgencyLevel.EMERGENCY, now.plusMinutes(15)),
                new ServiceRequest(6, 7, "Fever", UrgencyLevel.MODERATE, now.plusHours(4)),
        };

        for (ServiceRequest req : incoming) {
            System.out.println("      " + req.getCategory() + " (" + req.getUrgency().getDisplayName() + ")");
            pq.insert(req);
        }

        System.out.println("\n   🚑 Dispatch Order (sorted by urgency):");
        String[] expectedOrder = {
                "Heart Attack (Emergency)",
                "Stroke (Emergency)",
                "Chest Pain (Urgent)",
                "Broken Arm (Moderate)",
                "Fever (Moderate)",
                "Headache (Low)"
        };

        int i = 0;
        while (!pq.isEmpty()) {
            ServiceRequest dispatched = pq.extractMin();
            String actual = dispatched.getCategory() + " (" + dispatched.getUrgency().getDisplayName() + ")";
            System.out.println("      " + (i + 1) + ". " + actual);
            assert actual.equals(expectedOrder[i]) : "Expected: " + expectedOrder[i] + " but got: " + actual;
            i++;
        }

        assert i == 6 : "Should have dispatched 6 patients";

        passed++;
        System.out.println("   ✅ Passed - Dispatch order trace verified!");
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        // Single element
        pq.insert(5);
        assert pq.size() == 1 : "Size should be 1";
        assert pq.peekMin() == 5 : "Peek should be 5";
        assert pq.extractMin() == 5 : "Extract should be 5";
        assert pq.isEmpty() : "Should be empty after extract";

        // Duplicate elements
        pq.insert(3);
        pq.insert(3);
        pq.insert(3);
        assert pq.size() == 3 : "Size should be 3";
        assert pq.extractMin() == 3 : "First extract should be 3";
        assert pq.extractMin() == 3 : "Second extract should be 3";
        assert pq.extractMin() == 3 : "Third extract should be 3";
        assert pq.isEmpty() : "Should be empty after all extractions";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyPriorityQueue<Integer> pq = new MyPriorityQueue<>();

        // Extract from empty
        try {
            pq.extractMin();
            System.out.println("   ❌ Failed: extractMin() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        // Peek from empty
        try {
            pq.peekMin();
            System.out.println("   ❌ Failed: peekMin() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs threw exceptions");
    }
}