package main.com.ug.optimizer.algorithms.graph;

import main.com.ug.optimizer.datastructures.Graph;
import main.com.ug.optimizer.datastructures.MyPriorityQueue;
import main.com.ug.optimizer.datastructures.MyStack;
import main.com.ug.optimizer.model.Location;
import main.com.ug.optimizer.model.Road;

/**
 * Dijkstra's Shortest Path Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Find shortest path from source to all vertices
 * INPUT: Weighted graph, source vertex
 * OUTPUT: Distances array and predecessor array for path reconstruction
 *
 * HOW IT WORKS:
 * 1. Initialize distances to infinity, source distance = 0
 * 2. Use priority queue to extract vertex with minimum distance
 * 3. Relax all edges: update distance if shorter path found
 * 4. Repeat until all vertices processed
 *
 * PRECONDITIONS:
 *   - Graph cannot be null
 *   - All edge weights must be non-negative
 *   - Source must be valid
 *
 * EDGE CASES:
 *   - Disconnected graph → Some vertices unreachable (distance = INF)
 *   - Single vertex → Returns 0
 *   - Negative weights → Not supported (use Bellman-Ford)
 *
 * TIME COMPLEXITY: O((V + E) log V)
 * SPACE COMPLEXITY: O(V)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Dijkstra {

    public static final int INF = Integer.MAX_VALUE;

    /**
     * Run Dijkstra's algorithm
     */
    public static Result shortestPath(Graph graph, int source) {
        if (graph == null || graph.getVertexCount() == 0) {
            return new Result(new int[0], new int[0]);
        }
        if (source < 0 || source >= graph.getVertexCount()) {
            throw new IllegalArgumentException("Invalid source: " + source);
        }

        int V = graph.getVertexCount();
        int[] dist = new int[V];
        int[] prev = new int[V];
        boolean[] visited = new boolean[V];

        for (int i = 0; i < V; i++) {
            dist[i] = INF;
            prev[i] = -1;
        }
        dist[source] = 0;

        MyPriorityQueue<Node> pq = new MyPriorityQueue<>();
        pq.insert(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node current = pq.extractMin();
            int u = current.vertex;

            if (visited[u]) continue;
            visited[u] = true;

            for (Graph.Edge edge : graph.getNeighbors(u)) {
                int v = edge.getTo();
                int weight = edge.getWeight();

                if (!visited[v] && dist[u] != INF && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    prev[v] = u;
                    pq.insert(new Node(v, dist[v]));
                }
            }
        }

        return new Result(dist, prev);
    }

    /**
     * Reconstruct path from source to target
     */
    public static int[] reconstructPath(int[] prev, int source, int target) {
        if (prev == null || target < 0 || target >= prev.length) {
            return new int[0];
        }

        MyStack<Integer> path = new MyStack<>();
        int current = target;

        while (current != -1) {
            path.push(current);
            if (current == source) break;
            current = prev[current];
        }

        if (path.peek() != source) {
            return new int[0];  // No path
        }

        int[] result = new int[path.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = path.pop();
        }
        return result;
    }

    /**
     * Generate distance table for evidence
     */
    public static String generateDistanceTable(Graph graph, int source) {
        StringBuilder table = new StringBuilder();
        table.append("Dijkstra's Algorithm - Distance Table\n");
        table.append("Source: ").append(source).append("\n");
        table.append("=".repeat(60)).append("\n");
        table.append("Vertex | Distance | Predecessor\n");
        table.append("-".repeat(60)).append("\n");

        Result result = shortestPath(graph, source);
        int V = graph.getVertexCount();

        for (int i = 0; i < V; i++) {
            String distStr = (result.dist[i] == INF) ? "∞" : String.valueOf(result.dist[i]);
            String predStr = (result.prev[i] == -1) ? "—" : String.valueOf(result.prev[i]);
            table.append(String.format(" %4d  | %8s | %11s\n", i, distStr, predStr));
        }

        table.append("-".repeat(60)).append("\n");
        return table.toString();
    }

    /**
     * Generate full evidence with path reconstruction
     */
    public static String generateEvidence(Graph graph, int source, int target) {
        StringBuilder evidence = new StringBuilder();
        evidence.append("Dijkstra's Algorithm - Complete Evidence\n");
        evidence.append("=".repeat(60)).append("\n");
        evidence.append("Source: ").append(source).append(", Target: ").append(target).append("\n\n");

        evidence.append(generateDistanceTable(graph, source));

        Result result = shortestPath(graph, source);
        int[] path = reconstructPath(result.prev, source, target);

        evidence.append("\nPath from ").append(source).append(" to ").append(target).append(": ");
        if (path.length == 0) {
            evidence.append("NO PATH EXISTS\n");
        } else {
            for (int i = 0; i < path.length; i++) {
                evidence.append(path[i]);
                if (i < path.length - 1) evidence.append(" → ");
            }
            evidence.append("\nTotal Distance: ").append(result.dist[target]);
            if (result.dist[target] == INF) {
                evidence.append(" (unreachable)");
            }
            evidence.append("\n");
        }

        return evidence.toString();
    }

    // =============================================
    // INNER CLASSES
    // =============================================

    static class Node implements Comparable<Node> {
        int vertex;
        int distance;

        Node(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.distance, other.distance);
        }
    }

    public static class Result {
        public int[] dist;
        public int[] prev;

        Result(int[] dist, int[] prev) {
            this.dist = dist;
            this.prev = prev;
        }
    }
}