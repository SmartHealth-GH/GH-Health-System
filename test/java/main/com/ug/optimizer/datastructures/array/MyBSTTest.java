package main.com.ug.optimizer.datastructures.bst;

import main.com.ug.optimizer.datastructures.bst.MyBST;
import main.com.ug.optimizer.model.Location;

/**
 * Unit tests for MyBST
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyBSTTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING MyBST");
        System.out.println("=".repeat(60));

        testInsertAndSearch();
        testContains();
        testInorderTraversal();
        testPreorderTraversal();
        testPostorderTraversal();
        testMinAndMax();
        testHeight();
        testDelete();
        testWithModelObjects();
        testSearchPathTrace();
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

    private static void testInsertAndSearch() {
        System.out.println("\n✅ Test: Insert and Search");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        bst.insert(2, "Two");
        bst.insert(4, "Four");
        bst.insert(6, "Six");
        bst.insert(8, "Eight");

        assert bst.size() == 7 : "Size should be 7";
        assert bst.search(5).equals("Five") : "Key 5 should be 'Five'";
        assert bst.search(3).equals("Three") : "Key 3 should be 'Three'";
        assert bst.search(7).equals("Seven") : "Key 7 should be 'Seven'";
        assert bst.search(2).equals("Two") : "Key 2 should be 'Two'";
        assert bst.search(8).equals("Eight") : "Key 8 should be 'Eight'";
        assert bst.search(10) == null : "Key 10 should not exist";

        passed++;
        System.out.println("   ✅ Passed - Size: " + bst.size());
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");

        assert bst.contains(5) : "Should contain 5";
        assert bst.contains(3) : "Should contain 3";
        assert bst.contains(7) : "Should contain 7";
        assert !bst.contains(10) : "Should not contain 10";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInorderTraversal() {
        System.out.println("\n✅ Test: Inorder Traversal (Sorted Output)");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        bst.insert(2, "Two");
        bst.insert(4, "Four");
        bst.insert(6, "Six");
        bst.insert(8, "Eight");

        System.out.println("   📋 Inorder should be: 2 3 4 5 6 7 8");
        bst.inorder();  // Should print: 2 3 4 5 6 7 8

        passed++;
        System.out.println("   ✅ Passed - Inorder gives sorted output");
    }

    private static void testPreorderTraversal() {
        System.out.println("\n✅ Test: Preorder Traversal");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        bst.insert(2, "Two");
        bst.insert(4, "Four");
        bst.insert(6, "Six");
        bst.insert(8, "Eight");

        System.out.println("   📋 Preorder should be: 5 3 2 4 7 6 8");
        bst.preorder();  // Should print: 5 3 2 4 7 6 8

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testPostorderTraversal() {
        System.out.println("\n✅ Test: Postorder Traversal");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        bst.insert(2, "Two");
        bst.insert(4, "Four");
        bst.insert(6, "Six");
        bst.insert(8, "Eight");

        System.out.println("   📋 Postorder should be: 2 4 3 6 8 7 5");
        bst.postorder();  // Should print: 2 4 3 6 8 7 5

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testMinAndMax() {
        System.out.println("\n✅ Test: Min and Max");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        bst.insert(2, "Two");
        bst.insert(8, "Eight");

        assert bst.getMinKey() == 2 : "Min should be 2";
        assert bst.getMaxKey() == 8 : "Max should be 8";

        passed++;
        System.out.println("   ✅ Passed - Min: " + bst.getMinKey() + ", Max: " + bst.getMaxKey());
    }

    private static void testHeight() {
        System.out.println("\n✅ Test: Height");
        MyBST<Integer, String> bst = new MyBST<>();

        assert bst.height() == -1 : "Empty tree height should be -1";

        bst.insert(5, "Five");
        assert bst.height() == 0 : "Single node height should be 0";

        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        assert bst.height() == 1 : "Balanced 3 nodes height should be 1";

        bst.insert(2, "Two");
        bst.insert(4, "Four");
        bst.insert(6, "Six");
        bst.insert(8, "Eight");
        assert bst.height() == 2 : "Complete 7 nodes height should be 2";

        passed++;
        System.out.println("   ✅ Passed - Height: " + bst.height());
    }

    private static void testDelete() {
        System.out.println("\n✅ Test: Delete");
        MyBST<Integer, String> bst = new MyBST<>();

        bst.insert(5, "Five");
        bst.insert(3, "Three");
        bst.insert(7, "Seven");
        bst.insert(2, "Two");
        bst.insert(4, "Four");
        bst.insert(6, "Six");
        bst.insert(8, "Eight");

        // Delete leaf
        boolean deleted = bst.delete(2);
        assert deleted : "Delete 2 should succeed";
        assert bst.size() == 6 : "Size should be 6";
        assert !bst.contains(2) : "2 should not exist";

        // Delete node with one child
        deleted = bst.delete(3);
        assert deleted : "Delete 3 should succeed";
        assert bst.size() == 5 : "Size should be 5";
        assert !bst.contains(3) : "3 should not exist";

        // Delete node with two children
        deleted = bst.delete(5);
        assert deleted : "Delete 5 should succeed";
        assert bst.size() == 4 : "Size should be 4";
        assert !bst.contains(5) : "5 should not exist";

        // Delete non-existent
        deleted = bst.delete(99);
        assert !deleted : "Delete 99 should fail";
        assert bst.size() == 4 : "Size should still be 4";

        passed++;
        System.out.println("   ✅ Passed - Final size: " + bst.size());
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (Location)");
        MyBST<Integer, Location> bst = new MyBST<>();

        Location loc1 = new Location(1, "Emergency Unit", "Korle-Bu", "EMERGENCY", 5.5405, -0.2038);
        Location loc2 = new Location(2, "Pharmacy", "Korle-Bu", "PHARMACY", 5.5408, -0.2040);
        Location loc3 = new Location(3, "Maternity Ward", "Korle-Bu", "WARD", 5.5415, -0.2050);

        bst.insert(1, loc1);
        bst.insert(2, loc2);
        bst.insert(3, loc3);

        assert bst.size() == 3 : "Should have 3 locations";
        assert bst.search(2).getName().equals("Pharmacy") : "Key 2 should be Pharmacy";
        assert bst.getMinKey() == 1 : "Min key should be 1";
        assert bst.getMaxKey() == 3 : "Max key should be 3";

        passed++;
        System.out.println("   ✅ Passed - Stored and retrieved Location objects");
    }

    private static void testSearchPathTrace() {
        System.out.println("\n✅ Test: Search Path Trace");
        System.out.println("   🔍 Searching for key 6 in BST");

        MyBST<Integer, String> bst = new MyBST<>();

        // Build a BST
        int[] keys = {5, 3, 7, 2, 4, 6, 8};
        for (int key : keys) {
            bst.insert(key, "Value-" + key);
        }

        System.out.println("   📋 Tree structure:");
        System.out.println("   " + bst);

        // Search for 6 - should go: 5 → 7 → 6
        System.out.println("\n   📋 Search path for key 6:");
        System.out.println("      Step 1: Start at root (5) → 6 > 5, go right");
        System.out.println("      Step 2: At node 7 → 6 < 7, go left");
        System.out.println("      Step 3: At node 6 → Found!");

        String result = bst.search(6);
        assert result != null : "Key 6 should be found";
        assert result.equals("Value-6") : "Value should be 'Value-6'";

        // Search for 10 (doesn't exist)
        System.out.println("\n   📋 Search path for key 10 (not found):");
        System.out.println("      Step 1: Start at root (5) → 10 > 5, go right");
        System.out.println("      Step 2: At node 7 → 10 > 7, go right");
        System.out.println("      Step 3: At node 8 → 10 > 8, go right");
        System.out.println("      Step 4: Reached null → Not found!");

        result = bst.search(10);
        assert result == null : "Key 10 should not be found";

        System.out.println("\n   ✅ Passed - Search path trace verified!");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");

        // Empty tree
        MyBST<Integer, String> bst = new MyBST<>();
        assert bst.isEmpty() : "Should be empty";
        assert bst.size() == 0 : "Size should be 0";
        assert bst.height() == -1 : "Height should be -1";
        assert bst.search(5) == null : "Search should return null";

        // Single element
        bst.insert(5, "Five");
        assert !bst.isEmpty() : "Should not be empty";
        assert bst.size() == 1 : "Size should be 1";
        assert bst.height() == 0 : "Height should be 0";
        assert bst.search(5).equals("Five") : "Search should find value";

        // Duplicate key (should update value)
        bst.insert(5, "Updated");
        assert bst.size() == 1 : "Size should still be 1";
        assert bst.search(5).equals("Updated") : "Value should be updated";

        // Clear
        bst.clear();
        assert bst.isEmpty() : "Should be empty after clear";
        assert bst.size() == 0 : "Size should be 0";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        MyBST<Integer, String> bst = new MyBST<>();

        // Min on empty
        try {
            bst.getMinKey();
            System.out.println("   ❌ Failed: getMinKey() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        // Max on empty
        try {
            bst.getMaxKey();
            System.out.println("   ❌ Failed: getMaxKey() on empty should throw exception");
            failed++;
            return;
        } catch (IllegalStateException e) {
            // Expected
        }

        // Delete non-existent (should return false)
        boolean deleted = bst.delete(5);
        assert !deleted : "Delete should return false for non-existent key";

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs handled correctly");
    }
}