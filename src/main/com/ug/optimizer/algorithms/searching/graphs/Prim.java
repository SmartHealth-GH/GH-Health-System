package main.com.ug.optimizer.algorithms.graph;

import main.com.ug.optimizer.datastructures.Graph;
import main.com.ug.optimizer.datastructures.MyPriorityQueue;
import main.com.ug.optimizer.model.Location;
import main.com.ug.optimizer.model.Road;

/**
 * Prim's Minimum Spanning Tree Algorithm
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
 * 1. Start with any vertex
 * 2. Add the minimum weight edge connecting tree to a new vertex
 * 3. Repeat until all vertices are in the tree
 * 4. Uses Priority Queue to always pick the minimum edge
 *
 * PRECONDITIONS:
 *   - Graph must be connected
 *   - All edge weights must be non-negative
 *
 * EDGE CASES:
 *   - Disconnected graph → Cannot find MST for all vertices
 *   - Single vertex → Empty MST
 *
 * TIME COMPLEXITY: O((V + E) log V)
 * SPACE COMPLEXITY: O(V)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Prim {

    public static final int INF = Integer.MAX_VALUE;

    /**
     * Find MST using Prim's algorithm
     */
    public static MSTResult mst(Graph graph, int start) {
        if (graph == null || graph.getVertexCount() == 0) {
            return new MSTResult(new int[0][0], 0);
        }
        if (start < 0 || start >= graph.getVertexCount()) {
            throw new IllegalArgumentException("Invalid start vertex: " + start);
        }

        int V = graph.getVertexCount();
        boolean[] inMST = new boolean[V];
        int[] parent = new int[V];
        int[] key = new int[V];

        for (int i = 0; i < V; i++) {
            key[i] = INF;
            parent[i] = -1;
        }
        key[start] = 0;

        MyPriorityQueue<Node> pq = new MyPriorityQueue<>();
        pq.insert(new Node(start, 0));

        while (!pq.isEmpty()) {
            Node current = pq.extractMin();
            int u = current.vertex;
            if (inMST[u]) continue;
            inMST[u] = true;

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.getTo();
                int weight = edge.getWeight();
                if (!inMST[v] && weight < key[v]) {
                    key[v] = weight;
                    parent[v] = u;
                    pq.insert(new Node(v, weight));
                }
            }
        }

        int totalWeight = 0;
        int edgeCount = 0;
        for (int i = 0; i < V; i++) {
            if (parent[i] != -1) {
                totalWeight += key[i];
                edgeCount++;
            }
        }

        int[][] edges = new int[edgeCount][3];
        int idx = 0;
        for (int i = 0; i < V; i++) {
            if (parent[i] != -1) {
                edges[idx][0] = parent[i];
                edges[idx][1] = i;
                edges[idx][2] = key[i];
                idx++;
            }
        }

        return new MSTResult(edges, totalWeight);
    }

    /**
     * Generate MST evidence
     */
    public static String generateMSTEvidence(Graph graph, int start) {
        StringBuilder evidence = new StringBuilder();
        evidence.append("Prim's Algorithm - MST Evidence\n");
        evidence.append("=".repeat(60)).append("\n");
        evidence.append("Start vertex: ").append(start).append("\n\n");

        MSTResult result = mst(graph, start);

        evidence.append("MST Edge List:\n");
        evidence.append("-".repeat(40)).append("\n");
        evidence.append("  From → To   Weight\n");
        evidence.append("-".repeat(40)).append("\n");

        for (int[] edge : result.edges) {
            evidence.append(String.format("  %3d → %-3d   %4d\n",
                    edge[0], edge[1], edge[2]));
        }

        evidence.append("-".repeat(40)).append("\n");
        evidence.append("Total MST Weight: ").append(result.totalWeight).append("\n");
        evidence.append("Number of Edges: ").append(result.edges.length).append("\n");

        return evidence.toString();
    }

    static class Node implements Comparable<Node> {
        int vertex;
        int weight;

        Node(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.weight, other.weight);
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