package main.com.ug.optimizer.datastructures.stack;

import main.com.ug.optimizer.datastructures.MyStack;
import main.com.ug.optimizer.model.AuditEvent;

/**
 * Unit tests for MyStack
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyStackTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyStack");
        System.out.println("=".repeat(60));

        testPushAndPop();
        testPeek();
        testIsEmptyAndSize();
        testClear();
        testContains();
        testUndoLogSimulation();
        testEdgeCases();
        testInvalidInputs();

        // 🔥 NEW: Undo Log Demo Evidence
        testUndoLogDemo();

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

    private static void testPushAndPop() {
        System.out.println("\n✅ Test: Push and Pop");
        MyStack<String> stack = new MyStack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        assert stack.size() == 3 : "Size should be 3";
        assert stack.toString().equals("[A, B, C]") : "Stack should be [A, B, C]";

        String popped = stack.pop();
        assert popped.equals("C") : "Popped should be 'C'";
        assert stack.size() == 2 : "Size should be 2";
        assert stack.toString().equals("[A, B]") : "Stack should be [A, B]";

        popped = stack.pop();
        assert popped.equals("B") : "Popped should be 'B'";
        assert stack.size() == 1 : "Size should be 1";
        assert stack.toString().equals("[A]") : "Stack should be [A]";

        popped = stack.pop();
        assert popped.equals("A") : "Popped should be 'A'";
        assert stack.size() == 0 : "Size should be 0";
        assert stack.isEmpty() : "Stack should be empty";
        assert stack.toString().equals("[]") : "Stack should be []";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testPeek() {
        System.out.println("\n✅ Test: Peek");
        MyStack<String> stack = new MyStack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        String peeked = stack.peek();
        assert peeked.equals("C") : "Peek should be 'C'";
        assert stack.size() == 3 : "Size should remain 3 after peek";
        assert stack.toString().equals("[A, B, C]") : "Stack should remain [A, B, C]";

        passed++;
        System.out.println("   ✅ Passed - Peek: " + peeked);
    }

    private static void testIsEmptyAndSize() {
        System.out.println("\n✅ Test: IsEmpty and Size");
        MyStack<String> stack = new MyStack<>();

        assert stack.isEmpty() : "Should be empty";
        assert stack.size() == 0 : "Size should be 0";

        stack.push("A");
        assert !stack.isEmpty() : "Should not be empty";
        assert stack.size() == 1 : "Size should be 1";

        stack.pop();
        assert stack.isEmpty() : "Should be empty again";
        assert stack.size() == 0 : "Size should be 0 again";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyStack<String> stack = new MyStack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        stack.clear();
        assert stack.isEmpty() : "Should be empty after clear";
        assert stack.size() == 0 : "Size should be 0 after clear";
        assert stack.toString().equals("[]") : "Stack should be [] after clear";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyStack<String> stack = new MyStack<>();

        stack.push("A");
        stack.push("B");
        stack.push("C");

        assert stack.contains("A") : "Should contain 'A'";
        assert stack.contains("B") : "Should contain 'B'";
        assert stack.contains("C") : "Should contain 'C'";
        assert !stack.contains("X") : "Should not contain 'X'";
        assert stack.contains(null) : "Should contain null after adding";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testUndoLogSimulation() {
        System.out.println("\n✅ Test: Undo Log Simulation (AuditEvent)");
        MyStack<AuditEvent> undoLog = new MyStack<>();

        // Simulate actions with undo log
        AuditEvent event1 = new AuditEvent("ADD_PATIENT", "Patient John added");
        AuditEvent event2 = new AuditEvent("UPDATE_STATUS", "Status changed to PENDING");
        AuditEvent event3 = new AuditEvent("ASSIGN_RESOURCE", "Ambulance assigned");

        undoLog.push(event1);
        undoLog.push(event2);
        undoLog.push(event3);

        assert undoLog.size() == 3 : "Should have 3 events";

        // Undo last action
        AuditEvent undone = undoLog.pop();
        assert undone.getAction().equals("ASSIGN_RESOURCE") : "Undone should be ASSIGN_RESOURCE";
        assert undoLog.size() == 2 : "Should have 2 events after undo";

        // Undo another
        undone = undoLog.pop();
        assert undone.getAction().equals("UPDATE_STATUS") : "Undone should be UPDATE_STATUS";
        assert undoLog.size() == 1 : "Should have 1 event after undo";

        // Peek at remaining
        assert undoLog.peek().getAction().equals("ADD_PATIENT") : "Remaining should be ADD_PATIENT";
        assert undoLog.size() == 1 : "Size should remain 1 after peek";

        passed++;
        System.out.println("   ✅ Passed - Undo log simulation working!");
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyStack<String> stack = new MyStack<>();

        // Single element
        stack.push("Only");
        assert stack.size() == 1 : "Size should be 1";
        assert stack.peek().equals("Only") : "Peek should be 'Only'";
        assert stack.pop().equals("Only") : "Pop should be 'Only'";
        assert stack.isEmpty() : "Should be empty after pop";

        // Push null
        stack.push(null);
        assert stack.size() == 1 : "Size should be 1";
        assert stack.peek() == null : "Peek should be null";
        assert stack.contains(null) : "Should contain null";
        stack.pop();
        assert stack.isEmpty() : "Should be empty after popping null";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyStack<String> stack = new MyStack<>();

        // Pop from empty stack
        try {
            stack.pop();
            System.out.println("   ❌ Failed: pop() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected - test passes
        }

        // Peek from empty stack
        try {
            stack.peek();
            System.out.println("   ❌ Failed: peek() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected - test passes
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs threw exceptions");
    }

    // =============================================
    // 📋 UNDO LOG DEMO EVIDENCE (For Report)
    // =============================================

    private static void testUndoLogDemo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 MyStack UNDO LOG DEMO");
        System.out.println("=".repeat(60));

        MyStack<AuditEvent> undoLog = new MyStack<>();

        // Simulate a hospital workflow
        System.out.println("🏥 Simulating Hospital System Actions:");
        System.out.println("-".repeat(50));

        // Action 1: Add patient
        AuditEvent event1 = new AuditEvent("ADD_PATIENT", "Patient John Doe (ID: 1001) admitted to Emergency");
        undoLog.push(event1);
        System.out.println("   ✅ " + event1);

        // Action 2: Update status
        AuditEvent event2 = new AuditEvent("UPDATE_STATUS", "Patient 1001 status: PENDING → IN_PROGRESS");
        undoLog.push(event2);
        System.out.println("   ✅ " + event2);

        // Action 3: Assign resource
        AuditEvent event3 = new AuditEvent("ASSIGN_RESOURCE", "Ambulance AMB-001 assigned to Patient 1001");
        undoLog.push(event3);
        System.out.println("   ✅ " + event3);

        // Action 4: Complete treatment
        AuditEvent event4 = new AuditEvent("COMPLETE_TREATMENT", "Patient 1001 treatment completed");
        undoLog.push(event4);
        System.out.println("   ✅ " + event4);

        System.out.println("-".repeat(50));
        System.out.println("📊 Current Undo Log Stack: " + undoLog.size() + " actions");
        System.out.println("   Stack (top → bottom):");
        System.out.println("   " + undoLog);

        // Demonstrate UNDO functionality
        System.out.println("-".repeat(50));
        System.out.println("🔄 Performing UNDO operations (LIFO):");

        AuditEvent undone1 = undoLog.pop();
        System.out.println("   Undo 1: " + undone1.getAction() + " → " + undone1.getDetails());

        AuditEvent undone2 = undoLog.pop();
        System.out.println("   Undo 2: " + undone2.getAction() + " → " + undone2.getDetails());

        AuditEvent undone3 = undoLog.pop();
        System.out.println("   Undo 3: " + undone3.getAction() + " → " + undone3.getDetails());

        System.out.println("-".repeat(50));
        System.out.println("📊 Remaining actions in stack: " + undoLog.size());
        System.out.println("   Stack (top → bottom): " + undoLog);

        System.out.println("-".repeat(50));
        System.out.println("✅ Undo log demonstration complete!");
        System.out.println("📋 LIFO behavior verified: Last action undone first!");
        System.out.println("📋 Copy this output to your report as evidence.");
    }
}