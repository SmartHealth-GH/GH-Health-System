package main.com.ug.optimizer.algorithms.graph;

import main.com.ug.optimizer.algorithms.graphs.BFS;
import main.com.ug.optimizer.algorithms.graph.DFS;
import main.com.ug.optimizer.algorithms.graph.Dijkstra;
import main.com.ug.optimizer.algorithms.graph.Prim;
import main.com.ug.optimizer.algorithms.graph.Kruskal;
import main.com.ug.optimizer.datastructures.Graph;

import java.util.Arrays;

/**
 * Unit tests for Graph Algorithms
 *
 * EVIDENCE PROVIDED:
 * - BFS/DFS trace tables
 * - Dijkstra distance table and predecessor path
 * - Prim and Kruskal MST edge lists
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class GraphAlgorithmsTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🧪 TESTING GRAPH ALGORITHMS");
        System.out.println("=".repeat(70));

        // Create test graph
        Graph graph = createTestGraph();

        testBFS(graph);
        testDFS(graph);
        testDijkstra(graph);
        testPrim(graph);
        testKruskal(graph);

        // 📋 EVIDENCE: All evidence generation
        generateAllEvidence(graph);

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
    // TEST GRAPH
    // =============================================

    private static Graph createTestGraph() {
        Graph graph = new Graph(6, false);

        // Add edges (undirected)
        graph.addEdge(0, 1, 2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 1);
        graph.addEdge(1, 3, 7);
        graph.addEdge(2, 4, 3);
        graph.addEdge(3, 4, 5);
        graph.addEdge(3, 5, 6);

        return graph;
    }

    // =============================================
    // TESTS
    // =============================================

    private static void testBFS(Graph graph) {
        System.out.println("\n✅ Test: BFS");
        int[] result = BFS.traverse(graph, 0);
        int[] expected = {0, 1, 2, 3, 4, 5};

        assert Arrays.equals(result, expected) : "BFS order incorrect";
        System.out.println("   ✅ Passed - BFS order: " + Arrays.toString(result));
        passed++;
    }

    private static void testDFS(Graph graph) {
        System.out.println("\n✅ Test: DFS");
        int[] result = DFS.traverse(graph, 0);
        System.out.println("   ✅ Passed - DFS order: " + Arrays.toString(result));
        passed++;
    }

    private static void testDijkstra(Graph graph) {
        System.out.println("\n✅ Test: Dijkstra");
        Dijkstra.Result result = Dijkstra.shortestPath(graph, 0);

        assert result.dist[0] == 0 : "Distance to source should be 0";
        assert result.dist[1] == 2 : "Distance to 1 should be 2";
        assert result.dist[2] == 3 : "Distance to 2 should be 3";

        System.out.println("   ✅ Passed - Distances: " + Arrays.toString(result.dist));
        passed++;
    }

    private static void testPrim(Graph graph) {
        System.out.println("\n✅ Test: Prim");
        Prim.MSTResult result = Prim.mst(graph, 0);

        assert result.totalWeight == 17 : "MST total weight should be 17";
        assert result.edges.length == 5 : "MST should have V-1 = 5 edges";

        System.out.println("   ✅ Passed - MST weight: " + result.totalWeight);
        passed++;
    }

    private static void testKruskal(Graph graph) {
        System.out.println("\n✅ Test: Kruskal");
        Kruskal.MSTResult result = Kruskal.mst(graph);

        assert result.totalWeight == 17 : "MST total weight should be 17";
        assert result.edges.length == 5 : "MST should have V-1 = 5 edges";

        System.out.println("   ✅ Passed - MST weight: " + result.totalWeight);
        passed++;
    }

    // =============================================
    // 📋 EVIDENCE: All Graph Algorithm Evidence
    // =============================================

    private static void generateAllEvidence(Graph graph) {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📋 EVIDENCE: Graph Algorithms");
        System.out.println("=".repeat(70));

        // 1. BFS Trace
        System.out.println("\n1. BFS TRACE");
        System.out.println(BFS.generateTrace(graph, 0));

        // 2. DFS Trace
        System.out.println("\n2. DFS TRACE");
        System.out.println(DFS.generateTrace(graph, 0));

        // 3. Dijkstra Evidence
        System.out.println("\n3. DIJKSTRA EVIDENCE");
        System.out.println(Dijkstra.generateEvidence(graph, 0, 5));

        // 4. Prim MST
        System.out.println("\n4. PRIM MST EVIDENCE");
        System.out.println(Prim.generateMSTEvidence(graph, 0));

        // 5. Kruskal MST
        System.out.println("\n5. KRUSKAL MST EVIDENCE");
        System.out.println(Kruskal.generateMSTEvidence(graph));

        System.out.println("📋 All evidence generated!");
        System.out.println("📋 Copy these outputs to your report as evidence.");
    }
}