package main.com.ug.optimizer.datastructures.array;

import main.com.ug.optimizer.datastructures.MyArrayList;
import main.com.ug.optimizer.model.Location;

/**
 * Unit tests for MyArrayList
 *
 * Tests cover:
 * - Normal cases (adding, getting, removing)
 * - Edge cases (empty list, single element, resizing)
 * - Invalid inputs (out of bounds, null)
 * - Resize trace evidence (for report)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyArrayListTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyArrayList");
        System.out.println("=".repeat(60));

        testDefaultConstructor();
        testAddAndGet();
        testSet();
        testRemove();
        testSizeAndIsEmpty();
        testResize();
        testClear();
        testContains();
        testIndexOf();
        testWithModelObjects();
        testEdgeCases();
        testInvalidInputs();

        // 🔥 NEW: Resize Trace Evidence
        testResizeTrace();

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

    private static void testDefaultConstructor() {
        System.out.println("\n✅ Test: Default Constructor");
        MyArrayList<String> list = new MyArrayList<>();
        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty";
        assert list.getCapacity() == 10 : "Default capacity should be 10";
        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testAddAndGet() {
        System.out.println("\n✅ Test: Add and Get");
        MyArrayList<String> list = new MyArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");

        assert list.size() == 3 : "Size should be 3";
        assert "A".equals(list.get(0)) : "Index 0 should be 'A'";
        assert "B".equals(list.get(1)) : "Index 1 should be 'B'";
        assert "C".equals(list.get(2)) : "Index 2 should be 'C'";
        assert list.toString().equals("[A, B, C]") : "toString() should be '[A, B, C]'";

        passed++;
        System.out.println("   ✅ Passed - List: " + list);
    }

    private static void testSet() {
        System.out.println("\n✅ Test: Set");
        MyArrayList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        String old = list.set(1, "X");
        assert old.equals("B") : "Old value should be 'B'";
        assert list.get(1).equals("X") : "Index 1 should now be 'X'";
        assert list.toString().equals("[A, X, C]") : "List should be '[A, X, C]'";

        passed++;
        System.out.println("   ✅ Passed - List: " + list);
    }

    private static void testRemove() {
        System.out.println("\n✅ Test: Remove");
        MyArrayList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        list.add("D");

        String removed = list.remove(1);
        assert removed.equals("B") : "Removed should be 'B'";
        assert list.size() == 3 : "Size should be 3";
        assert list.toString().equals("[A, C, D]") : "List should be '[A, C, D]'";

        // Remove from end
        removed = list.remove(2);
        assert removed.equals("D") : "Removed should be 'D'";
        assert list.size() == 2 : "Size should be 2";
        assert list.toString().equals("[A, C]") : "List should be '[A, C]'";

        // Remove from beginning
        removed = list.remove(0);
        assert removed.equals("A") : "Removed should be 'A'";
        assert list.size() == 1 : "Size should be 1";
        assert list.toString().equals("[C]") : "List should be '[C]'";

        passed++;
        System.out.println("   ✅ Passed - Final List: " + list);
    }

    private static void testSizeAndIsEmpty() {
        System.out.println("\n✅ Test: Size and IsEmpty");
        MyArrayList<String> list = new MyArrayList<>();

        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty";

        list.add("A");
        assert list.size() == 1 : "Size should be 1";
        assert !list.isEmpty() : "Should not be empty";

        list.remove(0);
        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty again";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testResize() {
        System.out.println("\n✅ Test: Resize");
        MyArrayList<Integer> list = new MyArrayList<>(2);

        // Add elements to force resize
        for (int i = 0; i < 5; i++) {
            list.add(i);
        }

        assert list.size() == 5 : "Size should be 5";
        assert list.getCapacity() >= 4 : "Capacity should be at least 4";
        assert list.get(0) == 0 : "Index 0 should be 0";
        assert list.get(4) == 4 : "Index 4 should be 4";

        // Add more to trigger another resize
        for (int i = 5; i < 10; i++) {
            list.add(i);
        }
        assert list.size() == 10 : "Size should be 10";

        passed++;
        System.out.println("   ✅ Passed - Size: " + list.size());
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyArrayList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");

        list.clear();
        assert list.size() == 0 : "Size should be 0";
        assert list.isEmpty() : "Should be empty";
        assert list.toString().equals("[]") : "toString() should be '[]'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyArrayList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");
        list.add(null);
        list.add("C");

        assert list.contains("A") : "Should contain 'A'";
        assert list.contains("B") : "Should contain 'B'";
        assert list.contains("C") : "Should contain 'C'";
        assert list.contains(null) : "Should contain null";
        assert !list.contains("X") : "Should not contain 'X'";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testIndexOf() {
        System.out.println("\n✅ Test: IndexOf");
        MyArrayList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");
        list.add("A");
        list.add("C");

        assert list.indexOf("A") == 0 : "First 'A' should be at index 0";
        assert list.indexOf("B") == 1 : "'B' should be at index 1";
        assert list.indexOf("C") == 3 : "'C' should be at index 3";
        assert list.indexOf("X") == -1 : "'X' should not be found";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (Location)");
        MyArrayList<Location> locations = new MyArrayList<>();

        Location loc1 = new Location("Emergency Unit", "Korle-Bu", "EMERGENCY", 5.5405, -0.2038);
        Location loc2 = new Location("Pharmacy", "Korle-Bu", "PHARMACY", 5.5408, -0.2040);
        Location loc3 = new Location("Maternity Ward", "Korle-Bu", "WARD", 5.5415, -0.2050);

        locations.add(loc1);
        locations.add(loc2);
        locations.add(loc3);

        assert locations.size() == 3 : "Should have 3 locations";
        assert locations.get(0).getName().equals("Emergency Unit") : "First should be Emergency Unit";
        assert locations.contains(loc2) : "Should contain Pharmacy";

        locations.remove(1);
        assert locations.size() == 2 : "Should have 2 locations after remove";
        assert locations.get(1).getName().equals("Maternity Ward") : "Second should be Maternity Ward";

        passed++;
        System.out.println("   ✅ Passed - Stored and manipulated Location objects");
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyArrayList<String> list = new MyArrayList<>();

        // Empty list
        assert list.size() == 0 : "Empty list size should be 0";
        assert list.isEmpty() : "Empty list should be empty";
        assert list.toString().equals("[]") : "Empty list toString() should be '[]'";

        // Single element
        list.add("Only");
        assert list.size() == 1 : "Single element size should be 1";
        assert !list.isEmpty() : "Single element should not be empty";
        assert list.get(0).equals("Only") : "Single element should be 'Only'";
        assert list.toString().equals("[Only]") : "Single element toString() should be '[Only]'";

        // Null element
        list.add(null);
        assert list.contains(null) : "Should contain null";
        assert list.indexOf(null) == 1 : "Null should be at index 1";

        // Removing last element
        list.remove(1);
        assert list.size() == 1 : "Should have 1 element after removing null";

        passed++;
        System.out.println("   ✅ Passed - Empty, Single, Null elements tested");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyArrayList<String> list = new MyArrayList<>();
        list.add("A");
        list.add("B");

        try {
            list.get(-1);
            System.out.println("   ❌ Failed: get(-1) should throw exception");
            failed++;
            return;
        } catch (IndexOutOfBoundsException e) {
            // Expected - test passes
        }

        try {
            list.get(10);
            System.out.println("   ❌ Failed: get(10) should throw exception");
            failed++;
            return;
        } catch (IndexOutOfBoundsException e) {
            // Expected - test passes
        }

        try {
            list.remove(-1);
            System.out.println("   ❌ Failed: remove(-1) should throw exception");
            failed++;
            return;
        } catch (IndexOutOfBoundsException e) {
            // Expected - test passes
        }

        try {
            list.remove(10);
            System.out.println("   ❌ Failed: remove(10) should throw exception");
            failed++;
            return;
        } catch (IndexOutOfBoundsException e) {
            // Expected - test passes
        }

        try {
            list.set(-1, "X");
            System.out.println("   ❌ Failed: set(-1, X) should throw exception");
            failed++;
            return;
        } catch (IndexOutOfBoundsException e) {
            // Expected - test passes
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs threw exceptions");
    }

    // =============================================
    // 📋 RESIZE TRACE EVIDENCE (For Report)
    // =============================================

    private static void testResizeTrace() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("📋 MyArrayList RESIZE TRACE");
        System.out.println("=".repeat(60));

        // Create with small capacity to force multiple resizes
        MyArrayList<Integer> list = new MyArrayList<>(2);

        System.out.println("📊 Starting with capacity: " + list.getCapacity());
        System.out.println("-".repeat(50));

        for (int i = 0; i < 10; i++) {
            int oldCapacity = list.getCapacity();
            list.add(i);
            int newCapacity = list.getCapacity();

            if (newCapacity > oldCapacity) {
                System.out.println("🔄 RESIZE TRIGGERED! Added element " + i);
                System.out.println("   Capacity: " + oldCapacity + " → " + newCapacity);
                System.out.println("   Size: " + list.size());
                System.out.println("   Elements: " + list);
            } else {
                System.out.println("   Added " + i + " → Size: " + list.size() + ", Capacity: " + list.getCapacity());
            }
        }

        System.out.println("-".repeat(50));
        System.out.println("📊 Final state:");
        System.out.println("   Size: " + list.size());
        System.out.println("   Capacity: " + list.getCapacity());
        System.out.println("   Elements: " + list);
        System.out.println("✅ Resize trace complete!");
        System.out.println("📋 Copy this output to your report as evidence.");
    }
}