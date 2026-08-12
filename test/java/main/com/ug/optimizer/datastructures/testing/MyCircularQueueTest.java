package main.com.ug.optimizer.datastructures.queue;

import main.com.ug.optimizer.datastructures.MyCircularQueue;

/**
 * Unit tests for MyCircularQueue
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyCircularQueueTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyCircularQueue");
        System.out.println("=".repeat(60));

        testEnqueueAndDequeue();
        testFront();
        testIsEmptyAndIsFull();
        testWrapAround();
        testClear();
        testContains();
        testEdgeCases();
        testInvalidInputs();

        // 🔥 NEW: Front/Rear Movement Trace Evidence
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
        MyCircularQueue<String> queue = new MyCircularQueue<>(5);

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assert queue.size() == 3 : "Size should be 3";
        assert !queue.isFull() : "Should not be full";
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
        MyCircularQueue<String> queue = new MyCircularQueue<>(5);

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

    private static void testIsEmptyAndIsFull() {
        System.out.println("\n✅ Test: IsEmpty and IsFull");
        MyCircularQueue<Integer> queue = new MyCircularQueue<>(3);

        assert queue.isEmpty() : "Should be empty";
        assert !queue.isFull() : "Should not be full";

        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assert !queue.isEmpty() : "Should not be empty";
        assert queue.isFull() : "Should be full";

        queue.dequeue();
        assert !queue.isFull() : "Should not be full after dequeue";
        assert queue.size() == 2 : "Size should be 2";

        queue.enqueue(4);
        assert queue.isFull() : "Should be full again";
        assert queue.size() == 3 : "Size should be 3";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testWrapAround() {
        System.out.println("\n✅ Test: Wrap Around");
        MyCircularQueue<Integer> queue = new MyCircularQueue<>(4);

        for (int i = 1; i <= 4; i++) {
            queue.enqueue(i);
        }

        System.out.println("   Initial: " + queue.debugString());
        assert queue.isFull() : "Queue should be full";
        assert queue.toString().equals("[1, 2, 3, 4]") : "Queue should be [1, 2, 3, 4]";

        queue.dequeue();
        queue.dequeue();

        System.out.println("   After 2 dequeues: " + queue.debugString());
        assert queue.size() == 2 : "Size should be 2";
        assert queue.toString().equals("[3, 4]") : "Queue should be [3, 4]";
        assert !queue.isFull() : "Should not be full";

        queue.enqueue(5);
        queue.enqueue(6);

        System.out.println("   After wrap around: " + queue.debugString());
        assert queue.isFull() : "Queue should be full again";
        assert queue.size() == 4 : "Size should be 4";
        assert queue.toString().equals("[3, 4, 5, 6]") : "Queue should be [3, 4, 5, 6]";

        assert queue.front() == 3 : "Front should be 3";

        passed++;
        System.out.println("   ✅ Passed - Wrap around working!");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyCircularQueue<String> queue = new MyCircularQueue<>(5);

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        queue.clear();
        assert queue.isEmpty() : "Should be empty after clear";
        assert queue.size() == 0 : "Size should be 0 after clear";
        assert queue.toString().equals("[]") : "Queue should be [] after clear";
        assert queue.getCapacity() == 5 : "Capacity should remain 5";

        queue.enqueue("X");
        assert queue.size() == 1 : "Should be able to add after clear";
        assert queue.front().equals("X") : "Front should be 'X'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyCircularQueue<String> queue = new MyCircularQueue<>(5);

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");

        assert queue.contains("A") : "Should contain 'A'";
        assert queue.contains("B") : "Should contain 'B'";
        assert queue.contains("C") : "Should contain 'C'";
        assert !queue.contains("X") : "Should not contain 'X'";

        queue.dequeue();
        assert !queue.contains("A") : "Should not contain 'A' after dequeue";
        assert queue.contains("B") : "Should still contain 'B'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");

        MyCircularQueue<String> queue = new MyCircularQueue<>(5);
        queue.enqueue("Only");
        assert queue.size() == 1 : "Size should be 1";
        assert queue.front().equals("Only") : "Front should be 'Only'";
        assert queue.dequeue().equals("Only") : "Dequeue should be 'Only'";
        assert queue.isEmpty() : "Should be empty after dequeue";

        queue.enqueue(null);
        assert queue.size() == 1 : "Size should be 1";
        assert queue.front() == null : "Front should be null";
        assert queue.contains(null) : "Should contain null";
        queue.dequeue();
        assert queue.isEmpty() : "Should be empty after removing null";

        MyCircularQueue<Integer> q = new MyCircularQueue<>(3);
        for (int i = 0; i < 100; i++) {
            q.enqueue(i);
            assert q.size() == 3 : "Size should be 3";
            assert q.front() == i - 2 : "Front should be " + (i - 2);
            q.dequeue();
            assert q.size() == 2 : "Size should be 2";
        }

        passed++;
        System.out.println("   ✅ Passed - Multiple wrap arounds working!");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");

        try {
            MyCircularQueue<String> queue = new MyCircularQueue<>(0);
            System.out.println("   ❌ Failed: Should throw exception for capacity 0");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            MyCircularQueue<String> queue = new MyCircularQueue<>(-5);
            System.out.println("   ❌ Failed: Should throw exception for negative capacity");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        MyCircularQueue<String> queue = new MyCircularQueue<>(3);
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

        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        try {
            queue.enqueue("D");
            System.out.println("   ❌ Failed: enqueue() on full should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs threw exceptions");
    }

    // =============================================
    // 📋 FRONT/REAR MOVEMENT TRACE EVIDENCE (For Report)
    // =============================================

    private static void testFrontRearTrace() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 MyCircularQueue FRONT/REAR MOVEMENT TRACE");
        System.out.println("=".repeat(60));

        MyCircularQueue<Integer> queue = new MyCircularQueue<>(4);

        System.out.println("📊 Tracking front/rear movement with wrap-around:");
        System.out.println("-".repeat(50));

        System.out.println("Initial: " + queue.debugString());

        queue.enqueue(1);
        System.out.println("enqueue(1) → " + queue.debugString());

        queue.enqueue(2);
        System.out.println("enqueue(2) → " + queue.debugString());

        queue.enqueue(3);
        System.out.println("enqueue(3) → " + queue.debugString());

        queue.enqueue(4);
        System.out.println("enqueue(4) → " + queue.debugString() + " (FULL)");

        queue.dequeue();
        System.out.println("dequeue()  → " + queue.debugString() + " (front moved)");

        queue.dequeue();
        System.out.println("dequeue()  → " + queue.debugString() + " (front moved)");

        queue.enqueue(5);
        System.out.println("enqueue(5) → " + queue.debugString() + " (rear wrapped to 0)");

        queue.enqueue(6);
        System.out.println("enqueue(6) → " + queue.debugString() + " (rear wrapped to 1, FULL)");

        System.out.println("-".repeat(50));
        System.out.println("📊 Final state:");
        System.out.println("   Queue: " + queue);
        System.out.println("   Size: " + queue.size());
        System.out.println("   Front: " + queue.front());
        System.out.println("   Capacity: " + queue.getCapacity());
        System.out.println("✅ Circular queue front/rear movement trace complete!");
        System.out.println("📋 Copy this output to your report as evidence.");
    }
}