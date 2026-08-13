package main.com.ug.optimizer.algorithms.graphs;

import main.com.ug.optimizer.datastructures.Graph;
import main.com.ug.optimizer.datastructures.MyQueue;
import main.com.ug.optimizer.model.Location;

/**
 * Breadth-First Search (BFS) Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Traverse a graph level by level from a source vertex
 * INPUT: Graph, source vertex
 * OUTPUT: Array of visited vertices in BFS order, distances from source
 *
 * HOW IT WORKS:
 * 1. Start at source vertex
 * 2. Visit all neighbors of current vertex
 * 3. Then visit neighbors of those neighbors
 * 4. Uses a Queue (FIFO) for traversal
 *
 * PRECONDITIONS:
 *   - Graph cannot be null
 *   - Source vertex must be valid (0 <= source < V)
 *
 * EDGE CASES:
 *   - Disconnected graph → Only visits reachable vertices
 *   - Single vertex → Returns just that vertex
 *   - Empty graph → Returns empty array
 *
 * TIME COMPLEXITY: O(V + E)
 * SPACE COMPLEXITY: O(V)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class BFS {

    /**
     * Perform BFS traversal from a source vertex
     *
     * @param graph The graph to traverse
     * @param source The starting vertex
     * @return Array of vertices in BFS order
     */
    public static int[] traverse(Graph graph, int source) {
        if (graph == null || graph.getVertexCount() == 0) {
            return new int[0];
        }
        if (source < 0 || source >= graph.getVertexCount()) {
            throw new IllegalArgumentException("Invalid source vertex: " + source);
        }

        int V = graph.getVertexCount();
        boolean[] visited = new boolean[V];
        int[] order = new int[V];
        int[] parent = new int[V];
        int index = 0;

        // Initialize parent array
        for (int i = 0; i < V; i++) {
            parent[i] = -1;
        }

        MyQueue<Integer> queue = new MyQueue<>();

        // Start with source
        visited[source] = true;
        queue.enqueue(source);
        order[index++] = source;

        while (!queue.isEmpty()) {
            int current = queue.dequeue();

            // Visit all neighbors
            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    parent[neighbor] = current;
                    queue.enqueue(neighbor);
                    order[index++] = neighbor;
                }
            }
        }

        // Trim array to actual size
        int[] result = new int[index];
        System.arraycopy(order, 0, result, 0, index);
        return result;
    }

    /**
     * Get distances from source (number of edges)
     */
    public static int[] getDistances(Graph graph, int source) {
        if (graph == null || graph.getVertexCount() == 0) {
            return new int[0];
        }

        int V = graph.getVertexCount();
        int[] distances = new int[V];
        for (int i = 0; i < V; i++) {
            distances[i] = -1;  // -1 = unreachable
        }
        distances[source] = 0;

        boolean[] visited = new boolean[V];
        MyQueue<Integer> queue = new MyQueue<>();

        visited[source] = true;
        queue.enqueue(source);

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    distances[neighbor] = distances[current] + 1;
                    queue.enqueue(neighbor);
                }
            }
        }

        return distances;
    }

    /**
     * Generate BFS trace for evidence
     */
    public static String generateTrace(Graph graph, int source) {
        StringBuilder trace = new StringBuilder();
        trace.append("BFS Traversal from vertex ").append(source).append("\n");
        trace.append("=".repeat(50)).append("\n");

        if (graph == null || graph.getVertexCount() == 0) {
            trace.append("Graph is empty\n");
            return trace.toString();
        }

        int V = graph.getVertexCount();
        boolean[] visited = new boolean[V];
        MyQueue<Integer> queue = new MyQueue<>();

        trace.append("Step | Queue (front→rear) | Visited | Action\n");
        trace.append("-".repeat(50)).append("\n");

        visited[source] = true;
        queue.enqueue(source);
        int step = 1;

        trace.append(String.format(" %2d  | %-18s | %-7s | Start at vertex %d\n",
                step++, queue.toString(), arrayToString(visited), source));

        while (!queue.isEmpty()) {
            int current = queue.dequeue();
            trace.append(String.format(" %2d  | %-18s | %-7s | Dequeue %d, visit neighbors\n",
                    step++, queue.toString(), arrayToString(visited), current));

            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.enqueue(neighbor);
                    trace.append(String.format("      | %-18s | %-7s |   → Enqueue %d\n",
                            queue.toString(), arrayToString(visited), neighbor));
                    step++;
                }
            }
        }

        trace.append("-".repeat(50)).append("\n");
        trace.append("BFS complete! ").append(countVisited(visited)).append(" vertices visited\n");
        return trace.toString();
    }

    private static String arrayToString(boolean[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i] ? "1" : "0");
        }
        return sb.toString();
    }

    private static int countVisited(boolean[] arr) {
        int count = 0;
        for (boolean b : arr) {
            if (b) count++;
        }
        return count;
    }
}