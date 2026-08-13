package main.com.ug.optimizer.algorithms.graph;

import main.com.ug.optimizer.datastructures.disjointset.DisjointSet;
import main.com.ug.optimizer.datastructures.Graph;
import main.com.ug.optimizer.datastructures.MyArrayList;
import main.com.ug.optimizer.model.Location;
import main.com.ug.optimizer.model.Road;

/**
 * Kruskal's Minimum Spanning Tree Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Find Minimum Spanning Tree (MST) of a weighted undirected graph
 * INPUT: Weighted undirected graph
 * OUTPUT: MST edges and total weight
 *
 * HOW IT WORKS:
 * 1. Sort all edges by weight (ascending)
 * 2. Add edge if it doesn't create a cycle (union-find)
 * 3. Repeat until V-1 edges added
 * 4. Uses Disjoint Set (Union-Find) for cycle detection
 *
 * PRECONDITIONS:
 *   - Graph must be connected
 *   - All edge weights must be non-negative
 *
 * EDGE CASES:
 *   - Disconnected graph → Cannot find MST for all vertices
 *   - Single vertex → Empty MST
 *
 * TIME COMPLEXITY: O(E log E)
 * SPACE COMPLEXITY: O(V + E)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Kruskal {

    /**
     * Find MST using Kruskal's algorithm
     */
    public static MSTResult mst(Graph graph) {
        if (graph == null || graph.getVertexCount() == 0) {
            return new MSTResult(new int[0][0], 0);
        }

        int V = graph.getVertexCount();

        // Collect all edges
        MyArrayList<Edge> edges = new MyArrayList<>();
        for (int i = 0; i < V; i++) {
            for (Graph.Edge edge : graph.getNeighbors(i)) {
                // For undirected graphs, only add each edge once
                if (i <= edge.getTo()) {
                    edges.add(new Edge(edge.getFrom(), edge.getTo(), edge.getWeight()));
                }
            }
        }

        // Sort edges by weight (using simple bubble sort for demonstration)
        // In production, use a proper sorting algorithm
        sortEdges(edges);

        DisjointSet ds = new DisjointSet(V);
        MyArrayList<Edge> mstEdges = new MyArrayList<>();
        int totalWeight = 0;
        int edgeCount = 0;
        int idx = 0;

        while (edgeCount < V - 1 && idx < edges.size()) {
            Edge edge = edges.get(idx++);
            int rootU = ds.find(edge.from);
            int rootV = ds.find(edge.to);

            if (rootU != rootV) {
                ds.union(edge.from, edge.to);
                mstEdges.add(edge);
                totalWeight += edge.weight;
                edgeCount++;
            }
        }

        int[][] resultEdges = new int[mstEdges.size()][3];
        for (int i = 0; i < mstEdges.size(); i++) {
            Edge e = mstEdges.get(i);
            resultEdges[i][0] = e.from;
            resultEdges[i][1] = e.to;
            resultEdges[i][2] = e.weight;
        }

        return new MSTResult(resultEdges, totalWeight);
    }

    /**
     * Sort edges by weight (bubble sort for simplicity)
     */
    private static void sortEdges(MyArrayList<Edge> edges) {
        int n = edges.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (edges.get(j).weight > edges.get(j + 1).weight) {
                    Edge temp = edges.get(j);
                    edges.set(j, edges.get(j + 1));
                    edges.set(j + 1, temp);
                }
            }
        }
    }

    /**
     * Generate MST evidence with connectivity trace
     */
    public static String generateMSTEvidence(Graph graph) {
        StringBuilder evidence = new StringBuilder();
        evidence.append("Kruskal's Algorithm - MST Evidence\n");
        evidence.append("=".repeat(60)).append("\n");

        int V = graph.getVertexCount();
        evidence.append("Vertices: ").append(V).append("\n");
        evidence.append("Edges in graph: ").append(graph.getEdgeCount()).append("\n\n");

        // Show edge processing order
        MyArrayList<Edge> allEdges = new MyArrayList<>();
        for (int i = 0; i < V; i++) {
            for (Graph.Edge edge : graph.getNeighbors(i)) {
                if (i <= edge.getTo()) {
                    allEdges.add(new Edge(edge.getFrom(), edge.getTo(), edge.getWeight()));
                }
            }
        }
        sortEdges(allEdges);

        evidence.append("Edges processed in order:\n");
        evidence.append("-".repeat(40)).append("\n");
        evidence.append("  From → To   Weight   Action\n");
        evidence.append("-".repeat(40)).append("\n");

        DisjointSet ds = new DisjointSet(V);
        MyArrayList<Edge> mstEdges = new MyArrayList<>();
        int totalWeight = 0;
        int edgeCount = 0;
        int idx = 0;

        while (edgeCount < V - 1 && idx < allEdges.size()) {
            Edge edge = allEdges.get(idx);
            int rootU = ds.find(edge.from);
            int rootV = ds.find(edge.to);

            String action;
            if (rootU != rootV) {
                ds.union(edge.from, edge.to);
                mstEdges.add(edge);
                totalWeight += edge.weight;
                edgeCount++;
                action = "✅ ADDED";
            } else {
                action = "⏭️ Skipped (cycle)";
            }
            evidence.append(String.format("  %3d → %-3d   %4d   %s\n",
                    edge.from, edge.to, edge.weight, action));
            idx++;
        }

        evidence.append("-".repeat(40)).append("\n");
        evidence.append("MST Edges: ").append(mstEdges.size()).append("\n");
        evidence.append("Total MST Weight: ").append(totalWeight).append("\n");

        return evidence.toString();
    }

    static class Edge {
        int from, to, weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static class MSTResult {
        public int[][] edges;  // [from, to, weight]
        public int totalWeight;

        MSTResult(int[][] edges, int totalWeight) {
            this.edges = edges;
            this.totalWeight = totalWeight;
        }
    }
}