package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.model.Location;

/**
 * Unit tests for MyMap
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyMapTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyMap");
        System.out.println("=".repeat(60));

        testPutAndGet();
        testContainsKey();
        testRemove();
        testSizeAndIsEmpty();
        testClear();
        testUpdate();
        testWithModelObjects();
        testEdgeCases();
        testInvalidInputs();
        testUseCase();

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

    private static void testPutAndGet() {
        System.out.println("\n✅ Test: Put and Get");
        MyMap<String, Integer> map = new MyMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        assert map.size() == 3 : "Size should be 3";
        assert map.get("A") == 1 : "A should be 1";
        assert map.get("B") == 2 : "B should be 2";
        assert map.get("C") == 3 : "C should be 3";
        assert map.get("D") == null : "D should not exist";

        System.out.println("   ✅ Passed - Map: " + map);
        passed++;
    }

    private static void testContainsKey() {
        System.out.println("\n✅ Test: ContainsKey");
        MyMap<String, Integer> map = new MyMap<>();

        map.put("A", 1);
        map.put("B", 2);

        assert map.containsKey("A") : "Should contain A";
        assert map.containsKey("B") : "Should contain B";
        assert !map.containsKey("C") : "Should not contain C";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testRemove() {
        System.out.println("\n✅ Test: Remove");
        MyMap<String, Integer> map = new MyMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        Integer removed = map.remove("B");
        assert removed == 2 : "Removed should be 2";
        assert map.size() == 2 : "Size should be 2";
        assert map.get("B") == null : "B should not exist";

        removed = map.remove("X");
        assert removed == null : "Removing non-existent should return null";
        assert map.size() == 2 : "Size should still be 2";

        System.out.println("   ✅ Passed - Map: " + map);
        passed++;
    }

    private static void testSizeAndIsEmpty() {
        System.out.println("\n✅ Test: Size and IsEmpty");
        MyMap<String, Integer> map = new MyMap<>();

        assert map.isEmpty() : "Should be empty";
        assert map.size() == 0 : "Size should be 0";

        map.put("A", 1);
        assert !map.isEmpty() : "Should not be empty";
        assert map.size() == 1 : "Size should be 1";

        map.remove("A");
        assert map.isEmpty() : "Should be empty again";
        assert map.size() == 0 : "Size should be 0 again";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyMap<String, Integer> map = new MyMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        map.clear();
        assert map.isEmpty() : "Should be empty after clear";
        assert map.size() == 0 : "Size should be 0 after clear";
        assert map.get("A") == null : "A should not exist";

        // Can add after clear
        map.put("X", 10);
        assert map.size() == 1 : "Should be able to add after clear";
        assert map.get("X") == 10 : "X should be 10";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testUpdate() {
        System.out.println("\n✅ Test: Update");
        MyMap<String, Integer> map = new MyMap<>();

        map.put("A", 1);
        assert map.get("A") == 1 : "A should be 1";

        map.put("A", 10);
        assert map.get("A") == 10 : "A should be updated to 10";
        assert map.size() == 1 : "Size should still be 1";

        map.put("A", 100);
        assert map.get("A") == 100 : "A should be updated to 100";
        assert map.size() == 1 : "Size should still be 1";

        System.out.println("   ✅ Passed - Map: " + map);
        passed++;
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (Location)");
        MyMap<Integer, Location> map = new MyMap<>();

        Location loc1 = new Location(1, "Emergency Unit", "Korle-Bu", "EMERGENCY", 5.5405, -0.2038);
        Location loc2 = new Location(2, "Pharmacy", "Korle-Bu", "PHARMACY", 5.5408, -0.2040);
        Location loc3 = new Location(3, "Maternity Ward", "Korle-Bu", "WARD", 5.5415, -0.2050);

        map.put(1, loc1);
        map.put(2, loc2);
        map.put(3, loc3);

        assert map.size() == 3 : "Should have 3 locations";
        assert map.get(2).getName().equals("Pharmacy") : "Key 2 should be Pharmacy";
        assert map.containsKey(3) : "Should contain key 3";

        map.remove(2);
        assert map.size() == 2 : "Should have 2 after remove";
        assert map.get(2) == null : "Key 2 should not exist";

        System.out.println("   ✅ Passed - Stored and retrieved Location objects");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyMap<String, Integer> map = new MyMap<>();

        // null key
        map.put(null, 100);
        assert map.size() == 1 : "Should support null key";
        assert map.get(null) == 100 : "null key should return 100";
        assert map.containsKey(null) : "Should contain null key";

        // null value
        map.put("NullValue", null);
        assert map.get("NullValue") == null : "Should support null value";
        assert map.containsKey("NullValue") : "Should contain key with null value";

        // Remove null key
        Integer removed = map.remove(null);
        assert removed == 100 : "Removed should be 100";
        assert !map.containsKey(null) : "Should not contain null key";
        assert map.size() == 1 : "Should have 1 element (NullValue)";

        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyMap<String, Integer> map = new MyMap<>();

        map.put("A", 1);

        // get(null) should return null
        assert map.get(null) == null : "get(null) should return null";

        // remove(null) should return null
        assert map.remove(null) == null : "remove(null) should return null";

        // containsKey(null) should return false
        assert !map.containsKey(null) : "containsKey(null) should return false";

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs handled correctly");
    }

    private static void testUseCase() {
        System.out.println("\n✅ Test: Real-World Use Case - Location Lookup");
        System.out.println("   🏥 Hospital System: Location ID to Location object mapping");

        MyMap<Integer, Location> locationMap = new MyMap<>();

        // Create locations
        Location loc1 = new Location(101, "Emergency Unit", "Korle-Bu", "EMERGENCY", 5.5405, -0.2038);
        Location loc2 = new Location(102, "Pharmacy", "Korle-Bu", "PHARMACY", 5.5408, -0.2040);
        Location loc3 = new Location(103, "Maternity Ward", "Korle-Bu", "WARD", 5.5415, -0.2050);
        Location loc4 = new Location(104, "Surgical Ward", "Korle-Bu", "WARD", 5.5410, -0.2045);

        // Store in map
        locationMap.put(101, loc1);
        locationMap.put(102, loc2);
        locationMap.put(103, loc3);
        locationMap.put(104, loc4);

        assert locationMap.size() == 4 : "Should have 4 locations";

        // Retrieve a location by ID
        Location found = locationMap.get(102);
        assert found.getName().equals("Pharmacy") : "Should find Pharmacy";

        // Check if a location exists
        assert locationMap.containsKey(103) : "Location 103 should exist";
        assert !locationMap.containsKey(105) : "Location 105 should not exist";

        // Remove a location
        locationMap.remove(103);
        assert locationMap.size() == 3 : "Should have 3 locations after removal";
        assert !locationMap.containsKey(103) : "Location 103 should not exist";

        System.out.println("   ✅ Passed - Location lookup system working!");
        System.out.println("   📋 Current locations: " + locationMap);
        passed++;
    }
}