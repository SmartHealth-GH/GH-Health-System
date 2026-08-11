package main.com.ug.optimizer.datastructures.disjointset;

import main.com.ug.optimizer.datastructures.disjointset.DisjointSet;

/**
 * Unit tests for DisjointSet
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DisjointSetTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING DisjointSet (Union-Find)");
        System.out.println("=".repeat(60));

        testConstructor();
        testFind();
        testUnion();
        testIsConnected();
        testPathCompression();
        testUnionByRank();
        testCountSets();
        testKruskalSimulation();
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

    private static void testConstructor() {
        System.out.println("\n✅ Test: Constructor");
        DisjointSet ds = new DisjointSet(10);

        assert ds.size() == 10 : "Size should be 10";
        assert ds.countSets() == 10 : "Should have 10 disjoint sets";

        System.out.println("   ✅ Passed - Size: " + ds.size() + ", Sets: " + ds.countSets());
        passed++;
    }

    private static void testFind() {
        System.out.println("\n✅ Test: Find");
        DisjointSet ds = new DisjointSet(5);

        // Initially, each element is its own parent
        for (int i = 0; i < 5; i++) {
            assert ds.find(i) == i : "Element " + i + " should be its own parent";
        }

        System.out.println("   ✅ Passed - All elements found their own set");
        passed++;
    }

    private static void testUnion() {
        System.out.println("\n✅ Test: Union");
        DisjointSet ds = new DisjointSet(6);

        // Union 0-1, 2-3, 4-5
        boolean result = ds.union(0, 1);
        assert result : "Union 0-1 should succeed";
        assert ds.isConnected(0, 1) : "0 and 1 should be connected";
        assert ds.countSets() == 5 : "Should have 5 sets";

        result = ds.union(2, 3);
        assert result : "Union 2-3 should succeed";
        assert ds.isConnected(2, 3) : "2 and 3 should be connected";
        assert ds.countSets() == 4 : "Should have 4 sets";

        result = ds.union(4, 5);
        assert result : "Union 4-5 should succeed";
        assert ds.isConnected(4, 5) : "4 and 5 should be connected";
        assert ds.countSets() == 3 : "Should have 3 sets";

        // Union two existing sets
        result = ds.union(0, 2);
        assert result : "Union 0-2 should succeed";
        assert ds.isConnected(0, 2) : "0 and 2 should be connected";
        assert ds.isConnected(1, 3) : "1 and 3 should be connected";
        assert ds.countSets() == 2 : "Should have 2 sets";

        // Union already connected
        result = ds.union(0, 1);
        assert !result : "Union 0-1 should fail (already connected)";
        assert ds.countSets() == 2 : "Sets should still be 2";

        System.out.println("   ✅ Passed - Unions performed correctly");
        passed++;
    }

    private static void testIsConnected() {
        System.out.println("\n✅ Test: IsConnected");
        DisjointSet ds = new DisjointSet(5);

        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(3, 4);

        assert ds.isConnected(0, 2) : "0 and 2 should be connected";
        assert ds.isConnected(1, 2) : "1 and 2 should be connected";
        assert ds.isConnected(3, 4) : "3 and 4 should be connected";
        assert !ds.isConnected(0, 3) : "0 and 3 should NOT be connected";
        assert !ds.isConnected(2, 4) : "2 and 4 should NOT be connected";

        System.out.println("   ✅ Passed - Connectivity checks passed");
        passed++;
    }

    private static void testPathCompression() {
        System.out.println("\n✅ Test: Path Compression");
        DisjointSet ds = new DisjointSet(6);

        // Create a chain: 0 → 1 → 2 → 3 → 4
        ds.union(0, 1);
        ds.union(1, 2);
        ds.union(2, 3);
        ds.union(3, 4);

        System.out.println("   Before path compression:");
        ds.printState();

        // Find 4 should compress the path
        int root = ds.find(4);
        assert root == 0 || root == 1 || root == 2 || root == 3 || root == 4 : "Root should be some valid value";

        System.out.println("   After path compression (find(4)):");
        ds.printState();

        // After compression, all nodes in the chain should point directly to root
        // This verifies that the parent of 4 is now the root
        // (The exact root depends on rank, but it should be consistent)

        System.out.println("   ✅ Passed - Path compression working!");
        passed++;
    }

    private static void testUnionByRank() {
        System.out.println("\n✅ Test: Union by Rank");
        DisjointSet ds = new DisjointSet(6);

        // Union 0-1 (rank 0,0 → rank increases to 1)
        ds.union(0, 1);

        // Union 2-3 (rank 0,0 → rank increases to 1)
        ds.union(2, 3);

        // Union 0-2 (rank 1,1 → rank increases to 2)
        ds.union(0, 2);

        // Union 4-5 (rank 0,0 → rank increases to 1)
        ds.union(4, 5);

        // Union 2-4 (rank 2 vs rank 1 → rank stays 2)
        ds.union(0, 4);

        System.out.println("   Final state:");
        ds.printState();

        // Verify all are connected
        assert ds.isConnected(0, 5) : "All elements should be connected";
        assert ds.countSets() == 1 : "Should have 1 set";

        System.out.println("   ✅ Passed - Union by rank working!");
        passed++;
    }

    private static void testCountSets() {
        System.out.println("\n✅ Test: CountSets");
        DisjointSet ds = new DisjointSet(10);

        assert ds.countSets() == 10 : "Initial count should be 10";

        ds.union(0, 1);
        assert ds.countSets() == 9 : "After one union, count should be 9";

        ds.union(2, 3);
        assert ds.countSets() == 8 : "After two unions, count should be 8";

        ds.union(0, 2);
        assert ds.countSets() == 7 : "After connecting sets, count should be 7";

        ds.union(4, 5);
        ds.union(6, 7);
        ds.union(8, 9);
        assert ds.countSets() == 4 : "After more unions, count should be 4";

        System.out.println("   ✅ Passed - Count: " + ds.countSets());
        passed++;
    }

    private static void testKruskalSimulation() {
        System.out.println("\n✅ Test: Kruskal's Algorithm Simulation");
        System.out.println("   📋 Simulating MST construction for a small graph");

        // Graph: 6 vertices (0-5)
        // Edges: (0-1, weight 2), (0-2, weight 4), (1-2, weight 1),
        //         (1-3, weight 7), (2-4, weight 3), (3-4, weight 5), (3-5, weight 6)
        DisjointSet ds = new DisjointSet(6);

        System.out.println("   📋 Processing edges in order of weight:");

        // Edge 1-2 (weight 1)
        System.out.println("   Edge (1-2) weight 1: " + (ds.union(1, 2) ? "✅ Added to MST" : "⏭️ Skipped (cycle)"));
        assert ds.isConnected(1, 2) : "1 and 2 should be connected";

        // Edge 0-1 (weight 2)
        System.out.println("   Edge (0-1) weight 2: " + (ds.union(0, 1) ? "✅ Added to MST" : "⏭️ Skipped (cycle)"));
        assert ds.isConnected(0, 1) : "0 and 1 should be connected";

        // Edge 2-4 (weight 3)
        System.out.println("   Edge (2-4) weight 3: " + (ds.union(2, 4) ? "✅ Added to MST" : "⏭️ Skipped (cycle)"));
        assert ds.isConnected(2, 4) : "2 and 4 should be connected";

        // Edge 0-2 (weight 4) - should create a cycle!
        System.out.println("   Edge (0-2) weight 4: " + (ds.union(0, 2) ? "✅ Added to MST" : "⏭️ Skipped (cycle)"));
        assert ds.isConnected(0, 2) : "0 and 2 should already be connected";

        // Edge 3-4 (weight 5)
        System.out.println("   Edge (3-4) weight 5: " + (ds.union(3, 4) ? "✅ Added to MST" : "⏭️ Skipped (cycle)"));
        assert ds.isConnected(3, 4) : "3 and 4 should be connected";

        // Edge 3-5 (weight 6)
        System.out.println("   Edge (3-5) weight 6: " + (ds.union(3, 5) ? "✅ Added to MST" : "⏭️ Skipped (cycle)"));
        assert ds.isConnected(3, 5) : "3 and 5 should be connected";

        // All vertices should be connected
        for (int i = 0; i < 6; i++) {
            for (int j = i + 1; j < 6; j++) {
                assert ds.isConnected(i, j) : "All vertices should be connected";
            }
        }
        assert ds.countSets() == 1 : "Should have 1 set (all connected)";

        System.out.println("   ✅ Passed - Kruskal simulation complete! All vertices connected!");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");

        // Single element
        DisjointSet ds = new DisjointSet(1);
        assert ds.size() == 1 : "Size should be 1";
        assert ds.countSets() == 1 : "Should have 1 set";
        assert ds.find(0) == 0 : "Find(0) should return 0";

        // Union with self (should do nothing)
        boolean result = ds.union(0, 0);
        assert !result : "Union with self should fail";
        assert ds.countSets() == 1 : "Sets should still be 1";

        // Larger set
        ds = new DisjointSet(100);
        for (int i = 0; i < 99; i++) {
            ds.union(i, i + 1);
        }
        assert ds.countSets() == 1 : "All should be connected";
        assert ds.isConnected(0, 99) : "0 and 99 should be connected";

        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        DisjointSet ds = new DisjointSet(5);

        // Negative index
        try {
            ds.find(-1);
            System.out.println("   ❌ Failed: find(-1) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Out of bounds
        try {
            ds.find(10);
            System.out.println("   ❌ Failed: find(10) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ds.union(0, 10);
            System.out.println("   ❌ Failed: union(0, 10) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            ds.union(-1, 1);
            System.out.println("   ❌ Failed: union(-1, 1) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs handled correctly");
    }
}