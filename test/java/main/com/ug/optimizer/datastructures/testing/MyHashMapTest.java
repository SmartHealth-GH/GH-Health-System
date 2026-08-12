package main.com.ug.optimizer.datastructures.hashtable;

import main.com.ug.optimizer.datastructures.MyHashMap;
import main.com.ug.optimizer.model.Location;

/**
 * Unit tests for MyHashMap
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyHashMapTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🧪 TESTING MyHashMap");
        System.out.println("=".repeat(70));

        testPutAndGet();
        testContainsKey();
        testRemove();
        testSizeAndIsEmpty();
        testClear();
        testResize();
        testCollisionHandling();
        testCollisionStatistics();
        testWithModelObjects();
        testEdgeCases();
        testInvalidInputs();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 RESULTS:");
        System.out.println("   ✅ Passed: " + passed);
        System.out.println("   ❌ Failed: " + failed);
        System.out.println("=".repeat(70));

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
        MyHashMap<String, Integer> map = new MyHashMap<>();

        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        assert map.size() == 3 : "Size should be 3";
        assert map.get("A") == 1 : "A should be 1";
        assert map.get("B") == 2 : "B should be 2";
        assert map.get("C") == 3 : "C should be 3";
        assert map.get("D") == null : "D should not exist";

        // Update existing key
        map.put("B", 20);
        assert map.get("B") == 20 : "B should be updated to 20";
        assert map.size() == 3 : "Size should still be 3";

        System.out.println("   ✅ Passed - Map: " + map);
        passed++;
    }

    private static void testContainsKey() {
        System.out.println("\n✅ Test: ContainsKey");
        MyHashMap<String, Integer> map = new MyHashMap<>();

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
        MyHashMap<String, Integer> map = new MyHashMap<>();

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
        MyHashMap<String, Integer> map = new MyHashMap<>();

        assert map.isEmpty() : "Should be empty";
        assert map.size() == 0 : "Size should be 0";

        map.put("A", 1);
        assert !map.isEmpty() : "Should not be empty";
        assert map.size() == 1 : "Size should be 1";

        map.remove("A");
        assert map.isEmpty() : "Should be empty again";
        assert map.size() == 0 : "Size should be 0";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testClear() {
        System.out.println("\n✅ Test: Clear");
        MyHashMap<String, Integer> map = new MyHashMap<>();

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

    private static void testResize() {
        System.out.println("\n✅ Test: Resize");
        MyHashMap<String, Integer> map = new MyHashMap<>(4);

        // Insert enough elements to trigger resize
        for (int i = 0; i < 10; i++) {
            map.put("Key" + i, i);
        }

        assert map.size() == 10 : "Should have 10 elements";
        assert map.getCapacity() > 4 : "Capacity should have increased";
        assert map.get("Key5") == 5 : "Key5 should be 5";
        assert map.get("Key9") == 9 : "Key9 should be 9";

        System.out.println("   📋 Final capacity: " + map.getCapacity());
        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testCollisionHandling() {
        System.out.println("\n✅ Test: Collision Handling");
        MyHashMap<String, Integer> map = new MyHashMap<>(10);

        // Insert keys that might collide (using hash bucket)
        // We can't force collisions easily in Java, but we can test that
        // different keys with same hash code are handled correctly
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        map.put("D", 4);
        map.put("E", 5);

        assert map.size() == 5 : "Should have 5 elements";
        assert map.get("A") == 1 : "A should be 1";
        assert map.get("E") == 5 : "E should be 5";

        System.out.println("   📋 Collisions: " + map.getCollisionCount());
        System.out.println("   📋 Max Chain Length: " + map.getMaxChainLength());
        System.out.println(map.getStats());

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testCollisionStatistics() {
        System.out.println("\n✅ Test: Collision Statistics (Evidence)");
        System.out.println("   📋 Testing different load factors");

        // Test 1: Low load factor (0.5)
        System.out.println("\n   📍 Test 1: Load Factor = 0.5");
        MyHashMap<String, Integer> map1 = new MyHashMap<>(20, 0.5);
        for (int i = 0; i < 20; i++) {
            map1.put("Key" + i, i);
        }
        System.out.println("   Stats: " + map1.getStats());

        // Test 2: Default load factor (0.75)
        System.out.println("\n   📍 Test 2: Load Factor = 0.75 (Default)");
        MyHashMap<String, Integer> map2 = new MyHashMap<>(20);
        for (int i = 0; i < 20; i++) {
            map2.put("Key" + i, i);
        }
        System.out.println("   Stats: " + map2.getStats());

        // Test 3: High load factor (0.9)
        System.out.println("\n   📍 Test 3: Load Factor = 0.9");
        MyHashMap<String, Integer> map3 = new MyHashMap<>(20, 0.9);
        for (int i = 0; i < 20; i++) {
            map3.put("Key" + i, i);
        }
        System.out.println("   Stats: " + map3.getStats());

        // Comparison
        System.out.println("\n   📊 Comparison:");
        System.out.println("   Load Factor 0.5 → Collisions: " + map1.getCollisionCount() +
                ", Max Chain: " + map1.getMaxChainLength());
        System.out.println("   Load Factor 0.75 → Collisions: " + map2.getCollisionCount() +
                ", Max Chain: " + map2.getMaxChainLength());
        System.out.println("   Load Factor 0.9 → Collisions: " + map3.getCollisionCount() +
                ", Max Chain: " + map3.getMaxChainLength());

        System.out.println("\n   📋 Lower load factor = fewer collisions, more memory usage");
        System.out.println("   📋 Higher load factor = more collisions, less memory usage");

        passed++;
        System.out.println("   ✅ Passed - Collision statistics collected!");
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (Location)");
        MyHashMap<Integer, Location> map = new MyHashMap<>();

        Location loc1 = new Location(1, "Emergency Unit", "Korle-Bu", "EMERGENCY", 5.5405, -0.2038);
        Location loc2 = new Location(2, "Pharmacy", "Korle-Bu", "PHARMACY", 5.5408, -0.2040);
        Location loc3 = new Location(3, "Maternity Ward", "Korle-Bu", "WARD", 5.5415, -0.2050);

        map.put(1, loc1);
        map.put(2, loc2);
        map.put(3, loc3);

        assert map.size() == 3 : "Should have 3 locations";
        assert map.get(2).getName().equals("Pharmacy") : "Key 2 should be Pharmacy";

        map.remove(2);
        assert map.size() == 2 : "Should have 2 after remove";
        assert map.get(2) == null : "Key 2 should not exist";

        System.out.println("   ✅ Passed - Stored and retrieved Location objects");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");
        MyHashMap<String, Integer> map = new MyHashMap<>();

        // Multiple updates
        map.put("A", 1);
        map.put("A", 2);
        map.put("A", 3);
        assert map.size() == 1 : "Size should be 1";
        assert map.get("A") == 3 : "A should be 3";

        // Remove then re-add
        map.remove("A");
        assert map.isEmpty() : "Should be empty";
        map.put("A", 10);
        assert map.get("A") == 10 : "A should be 10";

        // Different keys with same hash
        // This is hard to test directly, but HashMap handles it
        map.clear();
        map.put("Aa", 1);
        map.put("BB", 2);
        assert map.size() == 2 : "Should have 2 elements";

        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyHashMap<String, Integer> map = new MyHashMap<>();

        try {
            map.put(null, 1);
            System.out.println("   ❌ Failed: put(null) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // get(null) should return null
        assert map.get(null) == null : "get(null) should return null";

        // remove(null) should return null
        assert map.remove(null) == null : "remove(null) should return null";

        // Negative capacity
        try {
            MyHashMap<String, Integer> badMap = new MyHashMap<>(-5);
            System.out.println("   ❌ Failed: negative capacity should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Invalid load factor
        try {
            MyHashMap<String, Integer> badMap = new MyHashMap<>(10, 2.0);
            System.out.println("   ❌ Failed: load factor > 1 should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs handled correctly");
    }
}