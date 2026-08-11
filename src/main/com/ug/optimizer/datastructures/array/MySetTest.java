package main.com.ug.optimizer.datastructures.setmap;

import main.com.ug.optimizer.datastructures.setmap.MySet;
import main.com.ug.optimizer.model.Location;

/**
 * Unit tests for MySet
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MySetTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MySet");
        System.out.println("=".repeat(60));

        testAddAndContains();
        testRemove();
        testSizeAndIsEmpty();
        testClear();
        testNoDuplicates();
        testWithModelObjects();
        testEdgeCases();
        testMembershipUseCase();

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

    private static void testAddAndContains() {
        System.out.println("\n✅ Test: Add and Contains");
        MySet<String> set = new MySet<>();

        boolean added = set.add("A");
        assert added : "A should be added";
        added = set.add("B");
        assert added : "B should be added";
        added = set.add("C");
        assert added : "C should be added";

        assert set.size() == 3 : "Size should be 3";
        assert set.contains("A") : "Should contain A";
        assert set.contains("B") : "Should contain B";
        assert set.contains("C") : "Should contain C";
        assert !set.contains("D") : "Should not contain D";

        System.out.println("   ✅ Passed - Set: " + set);
        passed++;
    }

    private static void testRemove() {
        System.out.println("\n✅ Test: Remove");
        MySet<String> set = new MySet<>();

        set.add("A");
        set.add("B");
        set.add("C");

        boolean removed = set.remove("B");
        assert removed : "B should be removed";
        assert set.size() == 2 : "Size should be 2";
        assert !set.contains("B") : "Should not contain B";
        assert set.contains("A") : "Should still contain A";
        assert set.contains("C") : "Should still contain C";

        removed = set.remove("X");
        assert !removed : "Removing non-existent should return false";
        assert set.size() == 2 : "Size should still be 2";

        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testSizeAndIsEmpty() {
        System.out.println("\n✅ Test: Size and IsEmpty");
        MySet<String> set = new MySet<>();

        assert set.isEmpty() : "Should be empty";
        assert set.size() == 0 : "Size should be 0";

        set.add("A");
        assert !set.isEmpty() : "Should not be empty";
        assert set.size() == 1 : "Size should be 1";

        set.remove("A");
        assert set.isEmpty() : "Should be empty again";
        assert set.size() == 0 : "Size should be 0";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MySet<String> set = new MySet<>();

        set.add("A");
        set.add("B");
        set.add("C");

        set.clear();
        assert set.isEmpty() : "Should be empty after clear";
        assert set.size() == 0 : "Size should be 0";
        assert !set.contains("A") : "Should not contain A";

        // Can add after clear
        set.add("X");
        assert set.size() == 1 : "Should be able to add after clear";
        assert set.contains("X") : "Should contain X";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testNoDuplicates() {
        System.out.println("\n✅ Test: No Duplicates");
        MySet<String> set = new MySet<>();

        boolean added = set.add("A");
        assert added : "First add should succeed";

        added = set.add("A");
        assert !added : "Second add should fail (duplicate)";

        added = set.add("B");
        assert added : "B should be added";

        assert set.size() == 2 : "Size should be 2";

        System.out.println("   ✅ Passed - No duplicates in set");
        passed++;
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (Location)");
        MySet<Location> set = new MySet<>();

        Location loc1 = new Location(1, "Emergency Unit", "Korle-Bu", "EMERGENCY", 5.5405, -0.2038);
        Location loc2 = new Location(2, "Pharmacy", "Korle-Bu", "PHARMACY", 5.5408, -0.2040);
        Location loc3 = new Location(3, "Maternity Ward", "Korle-Bu", "WARD", 5.5415, -0.2050);

        set.add(loc1);
        set.add(loc2);
        set.add(loc3);

        assert set.size() == 3 : "Should have 3 locations";
        assert set.contains(loc2) : "Should contain Pharmacy";

        // Duplicate
        set.add(loc2);
        assert set.size() == 3 : "Should still have 3 (no duplicates)";

        set.remove(loc2);
        assert set.size() == 2 : "Should have 2 after remove";
        assert !set.contains(loc2) : "Should not contain Pharmacy";

        System.out.println("   ✅ Passed - Stored Location objects in set");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MySet<String> set = new MySet<>();

        // Add null
        boolean added = set.add(null);
        assert added : "Should add null";
        assert set.contains(null) : "Should contain null";

        // Duplicate null
        added = set.add(null);
        assert !added : "Duplicate null should fail";
        assert set.size() == 1 : "Size should be 1";

        // Remove null
        boolean removed = set.remove(null);
        assert removed : "Should remove null";
        assert !set.contains(null) : "Should not contain null";
        assert set.isEmpty() : "Should be empty";

        // Add after remove
        set.add("A");
        assert set.size() == 1 : "Should have 1 element";

        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testMembershipUseCase() {
        System.out.println("\n✅ Test: Membership Use Case");
        System.out.println("   🏥 Hospital System: Tracking unique patient IDs");

        MySet<Integer> patientIds = new MySet<>();

        // Simulate assigning patient IDs
        patientIds.add(1001);
        patientIds.add(1002);
        patientIds.add(1003);

        assert patientIds.size() == 3 : "Should have 3 patients";
        assert patientIds.contains(1002) : "Patient 1002 exists";
        assert !patientIds.contains(1004) : "Patient 1004 does not exist";

        // Simulate duplicate check
        boolean duplicate = !patientIds.add(1002);
        assert duplicate : "1002 should be duplicate";
        assert patientIds.size() == 3 : "Size should still be 3";

        // Simulate patient checkout (remove)
        patientIds.remove(1001);
        assert patientIds.size() == 2 : "Should have 2 patients after checkout";
        assert !patientIds.contains(1001) : "Patient 1001 should be discharged";

        // Add new patient
        patientIds.add(1004);
        assert patientIds.size() == 3 : "Should have 3 patients";
        assert patientIds.contains(1004) : "Patient 1004 should exist";

        System.out.println("   ✅ Passed - Set ensures unique patient IDs!");
        System.out.println("   📋 Current patients: " + patientIds);
        passed++;
    }
}