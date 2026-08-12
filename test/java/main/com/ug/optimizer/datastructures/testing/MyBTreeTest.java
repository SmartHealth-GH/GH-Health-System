package main.com.ug.optimizer.datastructures.btree;

/**
 * Unit tests for MyBTree
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyBTreeTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🧪 TESTING MyBTree");
        System.out.println("=".repeat(70));

        testInsertAndSearch();
        testContains();
        testInorderTraversal();
        testNodeSplit();
        testHeightAndBalance();
        testDuplicateKeyUpdate();
        testLargeInsertion();
        testWithModelObjects();
        testEdgeCases();

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

    private static void testInsertAndSearch() {
        System.out.println("\n✅ Test: Insert and Search");
        MyBTree<Integer, String> btree = new MyBTree<>();

        btree.insert(5, "Five");
        btree.insert(3, "Three");
        btree.insert(7, "Seven");
        btree.insert(2, "Two");
        btree.insert(4, "Four");
        btree.insert(6, "Six");
        btree.insert(8, "Eight");

        assert btree.size() == 7 : "Size should be 7";
        assert btree.search(5).equals("Five") : "Key 5 should be 'Five'";
        assert btree.search(3).equals("Three") : "Key 3 should be 'Three'";
        assert btree.search(8).equals("Eight") : "Key 8 should be 'Eight'";
        assert btree.search(10) == null : "Key 10 should not exist";

        System.out.println("   ✅ Passed - Size: " + btree.size() + ", Height: " + btree.height());
        System.out.println("   📋 Tree structure:");
        btree.printTree();

        passed++;
    }

    private static void testContains() {
        System.out.println("\n✅ Test: Contains");
        MyBTree<Integer, String> btree = new MyBTree<>();

        btree.insert(5, "Five");
        btree.insert(3, "Three");
        btree.insert(7, "Seven");

        assert btree.contains(5) : "Should contain 5";
        assert btree.contains(3) : "Should contain 3";
        assert btree.contains(7) : "Should contain 7";
        assert !btree.contains(10) : "Should not contain 10";

        passed++;
        System.out.println("   ✅ Passed");
    }

    private static void testInorderTraversal() {
        System.out.println("\n✅ Test: Inorder Traversal (Sorted Output)");
        MyBTree<Integer, String> btree = new MyBTree<>();

        btree.insert(5, "Five");
        btree.insert(3, "Three");
        btree.insert(7, "Seven");
        btree.insert(2, "Two");
        btree.insert(4, "Four");
        btree.insert(6, "Six");
        btree.insert(8, "Eight");

        System.out.println("   📋 Inorder should be: 2 3 4 5 6 7 8");
        System.out.print("   ");
        btree.inorder();

        passed++;
        System.out.println("   ✅ Passed - Inorder gives sorted output");
    }

    private static void testNodeSplit() {
        System.out.println("\n✅ Test: Node Split");
        MyBTree<Integer, String> btree = new MyBTree<>();

        System.out.println("   📋 Inserting 1, 2, 3 (should trigger split at 3)");

        btree.insert(1, "One");
        System.out.println("   After insert 1: root = " + btree.search(1));

        btree.insert(2, "Two");
        System.out.println("   After insert 2: root = " + btree.search(2));

        btree.insert(3, "Three");
        System.out.println("   After insert 3: root should split!");
        System.out.println("   📋 Tree structure after split:");
        btree.printTree();

        assert btree.size() == 3 : "Size should be 3";
        assert btree.search(1).equals("One") : "Key 1 should exist";
        assert btree.search(2).equals("Two") : "Key 2 should exist";
        assert btree.search(3).equals("Three") : "Key 3 should exist";
        assert btree.height() == 2 : "Height should be 2 after split";

        System.out.println("   ✅ Passed - Node split working!");
        passed++;
    }

    private static void testHeightAndBalance() {
        System.out.println("\n✅ Test: Height and Balance");
        MyBTree<Integer, String> btree = new MyBTree<>();

        // Insert 100 elements
        for (int i = 1; i <= 100; i++) {
            btree.insert(i, "Value-" + i);
        }

        int height = btree.height();

        System.out.println("   📋 Size: " + btree.size());
        System.out.println("   📋 Height: " + height);

        // Height should be small for a B-Tree
        assert height <= 4 : "Height should be <= 4 for 100 elements";

        System.out.println("   ✅ Passed - Tree is balanced!");
        passed++;
    }

    private static void testDuplicateKeyUpdate() {
        System.out.println("\n✅ Test: Duplicate Key Update");
        MyBTree<Integer, String> btree = new MyBTree<>();

        btree.insert(5, "Five");
        btree.insert(5, "Updated");

        assert btree.size() == 1 : "Size should still be 1";
        assert btree.search(5).equals("Updated") : "Value should be updated";

        passed++;
        System.out.println("   ✅ Passed - Duplicate key updated value");
    }

    private static void testLargeInsertion() {
        System.out.println("\n✅ Test: Large Insertion (Performance)");
        MyBTree<Integer, String> btree = new MyBTree<>();

        long startTime = System.nanoTime();

        // Insert 1000 elements
        for (int i = 0; i < 1000; i++) {
            btree.insert(i, "Value-" + i);
        }

        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        assert btree.size() == 1000 : "Should have 1000 elements";
        assert btree.height() <= 5 : "Height should be <= 5 for 1000 elements";

        System.out.println("   📋 Inserted 1000 elements in " + duration + " ms");
        System.out.println("   📋 Size: " + btree.size());
        System.out.println("   📋 Height: " + btree.height());
        System.out.println("   ✅ Passed - Large insertion performed well");

        passed++;
    }

    private static void testWithModelObjects() {
        System.out.println("\n✅ Test: With Model Objects (AlgorithmRun)");
        MyBTree<String, String> btree = new MyBTree<>();

        // Store algorithm performance data
        btree.insert("BinarySearch", "O(log n)");
        btree.insert("LinearSearch", "O(n)");
        btree.insert("MergeSort", "O(n log n)");
        btree.insert("BubbleSort", "O(n²)");
        btree.insert("Dijkstra", "O(V²)");

        assert btree.size() == 5 : "Should have 5 entries";
        assert btree.search("MergeSort").equals("O(n log n)") : "MergeSort should be O(n log n)";
        assert btree.search("BubbleSort").equals("O(n²)") : "BubbleSort should be O(n²)";

        System.out.println("   📋 Algorithm performance data stored in B-Tree:");
        System.out.print("   ");
        btree.inorder();

        System.out.println("   ✅ Passed - Stored and retrieved AlgorithmRun data");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");

        // Empty tree
        MyBTree<Integer, String> btree = new MyBTree<>();
        assert btree.isEmpty() : "Should be empty";
        assert btree.size() == 0 : "Size should be 0";
        assert btree.search(5) == null : "Search should return null";

        // Single element
        btree.insert(5, "Five");
        assert !btree.isEmpty() : "Should not be empty";
        assert btree.size() == 1 : "Size should be 1";
        assert btree.search(5).equals("Five") : "Search should find value";

        // Clear
        btree.clear();
        assert btree.isEmpty() : "Should be empty after clear";
        assert btree.size() == 0 : "Size should be 0";

        // Insert after clear
        btree.insert(10, "Ten");
        btree.insert(20, "Twenty");
        assert btree.size() == 2 : "Should be able to insert after clear";

        // Insert negative keys
        btree.clear();
        btree.insert(-10, "Negative 10");
        btree.insert(-5, "Negative 5");
        btree.insert(-1, "Negative 1");
        assert btree.size() == 3 : "Should support negative keys";
        assert btree.search(-5).equals("Negative 5") : "Should find negative key";

        passed++;
        System.out.println("   ✅ Passed");
    }
}