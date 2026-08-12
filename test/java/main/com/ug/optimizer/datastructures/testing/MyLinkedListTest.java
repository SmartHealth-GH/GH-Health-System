package main.com.ug.optimizer.datastructures.array;

import main.com.ug.optimizer.datastructures.MyLinkedList;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;
import java.util.Iterator;

/**
 * Unit tests for MyLinkedList
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyLinkedListTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyLinkedList");
        System.out.println("=".repeat(60));

        testAddFirstAndAddLast();
        testInsertAfter();
        testRemove();
        testRemoveFirstAndLast();
        testGetFirstAndLast();
        testSizeAndIsEmpty();
        testClear();
        testContains();
        testIterator();
        testWithModelObjects();
        testEdgeCases();
        testInvalidInputs();

        // 🔥 NEW: Iterator Demo Evidence
        testIteratorDemo();

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

    private static void testAddFirstAndAddLast() {
        System.out.println("\n✅ Test: AddFirst and AddLast");
        MyLinkedList<String> list = new MyLinkedList<>();

        list.addFirst("A");
        list.addFirst("B");
        list.addLast("C");
        list.addLast("D");

        assert list.size() == 4 : "Size should be 4";
        assert list.getFirst().equals("B") : "First should be 'B'";
        assert list.getLast().equals("D") : "Last should be 'D'";
        assert list.toString().equals("[B, A, C, D]") : "List should be '[B, A, C, D]'";

        passed++;
        System.out.println("   ✅ Passed - List: " + list);
    }

    private static void testInsertAfter() {
        System.out.println("\n✅ Test: InsertAfter");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("D");

        boolean result = list.insertAfter("B", "C");
        assert result : "Insert should succeed";
        assert list.size() == 4 : "Size should be 4";
        assert list.toString().equals("[A, B, C, D]") : "List should be '[A, B, C, D]'";

        // Try inserting after non-existent element
        result = list.insertAfter("X", "Z");
        assert !result : "Insert should fail for non-existent target";
        assert list.size() == 4 : "Size should still be 4";

        passed++;
        System.out.println("   ✅ Passed - List: " + list);
    }

    private static void testRemove() {
        System.out.println("\n✅ Test: Remove");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.addLast("D");

        // Remove from middle
        boolean result = list.remove("B");
        assert result : "Remove should succeed";
        assert list.size() == 3 : "Size should be 3";
        assert list.toString().equals("[A, C, D]") : "List should be '[A, C, D]'";

        // Remove from beginning
        result = list.remove("A");
        assert result : "Remove should succeed";
        assert list.size() == 2 : "Size should be 2";
        assert list.toString().equals("[C, D]") : "List should be '[C, D]'";

        // Remove from end
        result = list.remove("D");
        assert result : "Remove should succeed";
        assert list.size() == 1 : "Size should be 1";
        assert list.toString().equals("[C]") : "List should be '[C]'";

        // Try removing non-existent element
        result = list.remove("X");
        assert !result : "Remove should fail for non-existent element";

        passed++;
        System.out.println("   ✅ Passed - Final List: " + list);
    }

    private static void testRemoveFirstAndLast() {
        System.out.println("\n✅ Test: RemoveFirst and RemoveLast");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");

        String removed = list.removeFirst();
        assert removed.equals("A") : "Removed should be 'A'";
        assert list.size() == 2 : "Size should be 2";
        assert list.toString().equals("[B, C]") : "List should be '[B, C]'";

        removed = list.removeLast();
        assert removed.equals("C") : "Removed should be 'C'";
        assert list.size() == 1 : "Size should be 1";
        assert list.toString().equals("[B]") : "List should be '[B]'";

        // Remove last element
        removed = list.removeFirst();
        assert removed.equals("B") : "Removed should be 'B'";
        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "List should be empty";
        assert list.toString().equals("[]") : "List should be '[]'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testGetFirstAndLast() {
        System.out.println("\n✅ Test: GetFirst and GetLast");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");

        assert list.getFirst().equals("A") : "First should be 'A'";
        assert list.getLast().equals("C") : "Last should be 'C'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testSizeAndIsEmpty() {
        System.out.println("\n✅ Test: Size and IsEmpty");
        MyLinkedList<String> list = new MyLinkedList<>();

        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty";

        list.addFirst("A");
        assert list.size() == 1 : "Size should be 1";
        assert !list.isEmpty() : "Should not be empty";

        list.removeFirst();
        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty again";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");

        list.clear();
        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty";
        assert list.toString().equals("[]") : "List should be '[]'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast(null);
        list.addLast("C");

        assert list.contains("A") : "Should contain 'A'";
        assert list.contains("B") : "Should contain 'B'";
        assert list.contains("C") : "Should contain 'C'";
        assert list.contains(null) : "Should contain null";
        assert !list.contains("X") : "Should not contain 'X'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testIterator() {
        System.out.println("\n✅ Test: Iterator");
        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");

        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        assert sb.toString().equals("ABC") : "Iterator should traverse in order";

        // Test iterator removal (should not be supported)
        Iterator<String> it = list.iterator();
        it.next();
        try {
            it.remove();
            System.out.println("   ❌ Failed: remove() should throw exception");
            failed++;
            return;
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - Traversed: " + sb.toString());
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (ServiceRequest)");
        MyLinkedList<ServiceRequest> requests = new MyLinkedList<>();

        LocalDateTime now = LocalDateTime.now();
        ServiceRequest req1 = new ServiceRequest(1, 2, "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1));
        ServiceRequest req2 = new ServiceRequest(2, 3, "Pharmacy", UrgencyLevel.MODERATE, now.plusHours(8));
        ServiceRequest req3 = new ServiceRequest(3, 4, "Surgery", UrgencyLevel.URGENT, now.plusHours(4));

        requests.addLast(req1);
        requests.addLast(req2);
        requests.addLast(req3);

        assert requests.size() == 3 : "Should have 3 requests";
        assert requests.getFirst().getCategory().equals("Emergency") : "First should be Emergency";
        assert requests.contains(req2) : "Should contain Pharmacy request";

        requests.removeFirst();
        assert requests.size() == 2 : "Should have 2 after remove";
        assert requests.getFirst().getCategory().equals("Pharmacy") : "First should be Pharmacy";

        passed++;
        System.out.println("   ✅ Passed - Stored and manipulated ServiceRequest objects");
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyLinkedList<String> list = new MyLinkedList<>();

        // Empty list operations
        try {
            list.removeFirst();
            System.out.println("   ❌ Failed: removeFirst() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        try {
            list.removeLast();
            System.out.println("   ❌ Failed: removeLast() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        // Single element
        list.addLast("Only");
        assert list.getFirst().equals("Only") : "First should be 'Only'";
        assert list.getLast().equals("Only") : "Last should be 'Only'";

        list.removeFirst();
        assert list.isEmpty() : "Should be empty after removing single element";

        // Null element handling
        list.addLast(null);
        assert list.contains(null) : "Should contain null";

        passed++;
        System.out.println("   ✅ Passed - Empty and single element cases tested");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyLinkedList<String> list = new MyLinkedList<>();

        // Insert after on empty list
        boolean result = list.insertAfter("A", "B");
        assert !result : "InsertAfter should fail on empty list";

        // Insert after non-existent
        list.addLast("X");
        result = list.insertAfter("A", "B");
        assert !result : "InsertAfter should fail when target not found";

        // Remove non-existent
        result = list.remove("A");
        assert !result : "Remove should fail when element not found";

        passed++;
        System.out.println("   ✅ Passed");
    }

    // =============================================
    // 📋 ITERATOR DEMO EVIDENCE (For Report)
    // =============================================

    private static void testIteratorDemo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 MyLinkedList ITERATOR DEMO");
        System.out.println("=".repeat(60));

        MyLinkedList<String> list = new MyLinkedList<>();
        list.addLast("A");
        list.addLast("B");
        list.addLast("C");
        list.addLast("D");
        list.addLast("E");

        System.out.println("📊 List: " + list);
        System.out.println("-".repeat(50));
        System.out.println("🔄 Traversing with Iterator:");

        int index = 0;
        for (String element : list) {
            System.out.println("   Element at index " + index + ": " + element);
            index++;
        }

        System.out.println("-".repeat(50));
        System.out.println("✅ Iterator successfully traversed all " + list.size() + " elements!");
        System.out.println("📋 Copy this output to your report as evidence.");
        System.out.println("📋 For the diagram, draw: null ← [A] ⇄ [B] ⇄ [C] ⇄ [D] ⇄ [E] → null");
    }
}