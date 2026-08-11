package main.com.ug.optimizer.datastructures.graph;

import main.com.ug.optimizer.datastructures.graph.Graph;
import main.com.ug.optimizer.datastructures.graph.Graph.Edge;

/**
 * Unit tests for Graph
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class GraphTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("🧪 TESTING Graph");
        System.out.println("=".repeat(60));

        testConstructor();
        testAddVertex();
        testAddEdge();
        testGetNeighbors();
        testAdjacencyMatrix();
        testEdgeWeight();
        testHasEdge();
        testUndirectedGraph();
        testDirectedGraph();
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
        Graph graph = new Graph(5);

        assert graph.getVertexCount() == 5 : "Should have 5 vertices";
        assert graph.getEdgeCount() == 0 : "Should have 0 edges";
        assert !graph.isDirected() : "Should be undirected by default";

        Graph directedGraph = new Graph(3, true);
        assert directedGraph.isDirected() : "Should be directed";

        System.out.println("   ✅ Passed - Undirected: " + graph.getVertexCount() + " vertices");
        System.out.println("   ✅ Passed - Directed: " + directedGraph.getVertexCount() + " vertices");
        passed++;
    }

    private static void testAddVertex() {
        System.out.println("\n✅ Test: Add Vertex");
        Graph graph = new Graph(3);

        int newVertex = graph.addVertex();
        assert newVertex == 3 : "New vertex index should be 3";
        assert graph.getVertexCount() == 4 : "Should have 4 vertices";

        int anotherVertex = graph.addVertex();
        assert anotherVertex == 4 : "New vertex index should be 4";
        assert graph.getVertexCount() == 5 : "Should have 5 vertices";

        System.out.println("   ✅ Passed - Added vertices: " + graph.getVertexCount());
        passed++;
    }

    private static void testAddEdge() {
        System.out.println("\n✅ Test: Add Edge");
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 7);

        assert graph.getEdgeCount() == 3 : "Should have 3 edges";
        assert graph.hasEdge(0, 1) : "Edge 0-1 should exist";
        assert graph.hasEdge(1, 2) : "Edge 1-2 should exist";
        assert graph.hasEdge(2, 3) : "Edge 2-3 should exist";
        assert !graph.hasEdge(0, 2) : "Edge 0-2 should not exist";

        // In undirected graph, reverse edges should exist
        assert graph.hasEdge(1, 0) : "Reverse edge 1-0 should exist";
        assert graph.hasEdge(2, 1) : "Reverse edge 2-1 should exist";

        System.out.println("   ✅ Passed - Edges added: " + graph.getEdgeCount());
        passed++;
    }

    private static void testGetNeighbors() {
        System.out.println("\n✅ Test: Get Neighbors");
        Graph graph = new Graph(5);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 3);
        graph.addEdge(0, 3, 7);
        graph.addEdge(2, 4, 2);

        var neighbors0 = graph.getNeighbors(0);
        assert neighbors0.size() == 3 : "Vertex 0 should have 3 neighbors";

        var neighbors2 = graph.getNeighbors(2);
        assert neighbors2.size() == 2 : "Vertex 2 should have 2 neighbors (4 and 0)";

        var neighbors4 = graph.getNeighbors(4);
        assert neighbors4.size() == 1 : "Vertex 4 should have 1 neighbor (2)";

        System.out.println("   ✅ Passed - Neighbors of 0: " + neighbors0);
        passed++;
    }

    private static void testAdjacencyMatrix() {
        System.out.println("\n✅ Test: Adjacency Matrix");
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 7);

        int[][] matrix = graph.getAdjacencyMatrix();

        assert matrix[0][1] == 5 : "Matrix[0][1] should be 5";
        assert matrix[1][2] == 3 : "Matrix[1][2] should be 3";
        assert matrix[2][3] == 7 : "Matrix[2][3] should be 7";

        // Undirected: symmetric matrix
        assert matrix[1][0] == 5 : "Matrix[1][0] should be 5";
        assert matrix[2][1] == 3 : "Matrix[2][1] should be 3";

        System.out.println("   ✅ Passed - Matrix preview:");
        for (int i = 0; i < 4; i++) {
            System.out.print("      ");
            for (int j = 0; j < 4; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
        passed++;
    }

    private static void testEdgeWeight() {
        System.out.println("\n✅ Test: Edge Weight");
        Graph graph = new Graph(4);

        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 10);
        graph.addEdge(1, 2, 3);

        assert graph.getEdgeWeight(0, 1) == 5 : "Weight 0-1 should be 5";
        assert graph.getEdgeWeight(0, 2) == 10 : "Weight 0-2 should be 10";
        assert graph.getEdgeWeight(1, 2) == 3 : "Weight 1-2 should be 3";
        assert graph.getEdgeWeight(0, 3) == 0 : "Weight 0-3 should be 0";

        System.out.println("   ✅ Passed - Weights verified");
        passed++;
    }

    private static void testHasEdge() {
        System.out.println("\n✅ Test: Has Edge");
        Graph graph = new Graph(4);

        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 3);

        assert graph.hasEdge(0, 1) : "Edge 0-1 should exist";
        assert graph.hasEdge(1, 2) : "Edge 1-2 should exist";
        assert graph.hasEdge(2, 3) : "Edge 2-3 should exist";
        assert !graph.hasEdge(0, 2) : "Edge 0-2 should not exist";
        assert !graph.hasEdge(1, 3) : "Edge 1-3 should not exist";

        System.out.println("   ✅ Passed - Edge existence verified");
        passed++;
    }

    private static void testUndirectedGraph() {
        System.out.println("\n✅ Test: Undirected Graph");
        Graph graph = new Graph(3, false);

        graph.addEdge(0, 1, 5);

        // In undirected, adding edge 0-1 should create both 0-1 and 1-0
        assert graph.hasEdge(0, 1) : "Edge 0-1 should exist";
        assert graph.hasEdge(1, 0) : "Edge 1-0 should exist";

        var neighbors0 = graph.getNeighbors(0);
        var neighbors1 = graph.getNeighbors(1);

        assert neighbors0.size() == 1 : "Vertex 0 should have 1 neighbor";
        assert neighbors1.size() == 1 : "Vertex 1 should have 1 neighbor";
        assert neighbors0.get(0).getTo() == 1 : "Neighbor of 0 should be 1";
        assert neighbors1.get(0).getTo() == 0 : "Neighbor of 1 should be 0";

        System.out.println("   ✅ Passed - Undirected graph works correctly");
        passed++;
    }

    private static void testDirectedGraph() {
        System.out.println("\n✅ Test: Directed Graph");
        Graph graph = new Graph(3, true);

        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);

        // In directed, adding edge 0-1 should create ONLY 0-1
        assert graph.hasEdge(0, 1) : "Edge 0-1 should exist";
        assert !graph.hasEdge(1, 0) : "Edge 1-0 should NOT exist";
        assert graph.hasEdge(1, 2) : "Edge 1-2 should exist";
        assert !graph.hasEdge(2, 1) : "Edge 2-1 should NOT exist";

        var neighbors0 = graph.getNeighbors(0);
        var neighbors1 = graph.getNeighbors(1);

        assert neighbors0.size() == 1 : "Vertex 0 should have 1 neighbor";
        assert neighbors1.size() == 1 : "Vertex 1 should have 1 neighbor";

        System.out.println("   ✅ Passed - Directed graph works correctly");
        passed++;
    }

    private static void testEdgeCases() {
        System.out.println("\n✅ Test: Edge Cases");

        // Empty graph
        Graph graph = new Graph(0);
        assert graph.getVertexCount() == 0 : "Should have 0 vertices";
        assert graph.getEdgeCount() == 0 : "Should have 0 edges";

        // Single vertex
        graph = new Graph(1);
        assert graph.getVertexCount() == 1 : "Should have 1 vertex";
        graph.addVertex();
        assert graph.getVertexCount() == 2 : "Should have 2 vertices";

        // Self-loop
        graph.addEdge(0, 0, 1);
        assert graph.hasEdge(0, 0) : "Self-loop should exist";
        assert graph.getEdgeWeight(0, 0) == 1 : "Self-loop weight should be 1";

        System.out.println("   ✅ Passed");
        passed++;
    }

    private static void testInvalidInputs() {
        System.out.println("\n✅ Test: Invalid Inputs");
        Graph graph = new Graph(5);

        // Add edge with invalid vertices
        try {
            graph.addEdge(-1, 0);
            System.out.println("   ❌ Failed: addEdge(-1, 0) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        try {
            graph.addEdge(0, 10);
            System.out.println("   ❌ Failed: addEdge(0, 10) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Get neighbors with invalid vertex
        try {
            graph.getNeighbors(-1);
            System.out.println("   ❌ Failed: getNeighbors(-1) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Get edge weight with invalid vertices
        try {
            graph.getEdgeWeight(0, 10);
            System.out.println("   ❌ Failed: getEdgeWeight(0, 10) should throw exception");
            failed++;
            return;
        } catch (IllegalArgumentException e) {
            // Expected
        }

        passed++;
        System.out.println("   ✅ Passed - All invalid inputs handled correctly");
    }
}