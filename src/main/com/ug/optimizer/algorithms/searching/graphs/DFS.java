package main.com.ug.optimizer.algorithms.graph;

import main.com.ug.optimizer.datastructures.Graph;
import main.com.ug.optimizer.datastructures.MyStack;
import main.com.ug.optimizer.model.Location;

/**
 * Depth-First Search (DFS) Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Traverse a graph depth-first from a source vertex
 * INPUT: Graph, source vertex
 * OUTPUT: Array of visited vertices in DFS order
 *
 * HOW IT WORKS:
 * 1. Start at source vertex
 * 2. Explore as far as possible along each branch
 * 3. Backtrack when no unvisited neighbors remain
 * 4. Uses a Stack (LIFO) for traversal
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
 * SPACE COMPLEXITY: O(V) - recursion stack
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DFS {

    /**
     * Perform DFS traversal from a source vertex (iterative)
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
        int index = 0;

        MyStack<Integer> stack = new MyStack<>();
        stack.push(source);

        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (visited[current]) {
                continue;
            }
            visited[current] = true;
            order[index++] = current;

            // Push neighbors in reverse order so they're processed in order
            MyStack<Integer> reverseStack = new MyStack<>();
            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    reverseStack.push(neighbor);
                }
            }
            while (!reverseStack.isEmpty()) {
                stack.push(reverseStack.pop());
            }
        }

        int[] result = new int[index];
        System.arraycopy(order, 0, result, 0, index);
        return result;
    }

    /**
     * Recursive DFS
     */
    public static void dfsRecursive(Graph graph, int source, boolean[] visited, MyStack<Integer> order) {
        if (graph == null || source < 0 || source >= graph.getVertexCount()) {
            return;
        }

        visited[source] = true;
        order.push(source);

        for (Graph.Edge edge : graph.getNeighbors(source)) {
            int neighbor = edge.getTo();
            if (!visited[neighbor]) {
                dfsRecursive(graph, neighbor, visited, order);
            }
        }
    }

    /**
     * Generate DFS trace for evidence
     */
    public static String generateTrace(Graph graph, int source) {
        StringBuilder trace = new StringBuilder();
        trace.append("DFS Traversal from vertex ").append(source).append("\n");
        trace.append("=".repeat(50)).append("\n");

        if (graph == null || graph.getVertexCount() == 0) {
            trace.append("Graph is empty\n");
            return trace.toString();
        }

        int V = graph.getVertexCount();
        boolean[] visited = new boolean[V];
        MyStack<Integer> stack = new MyStack<>();
        MyStack<Integer> output = new MyStack<>();

        trace.append("Step | Stack (top→bottom) | Visited | Action\n");
        trace.append("-".repeat(50)).append("\n");

        stack.push(source);
        int step = 1;

        while (!stack.isEmpty()) {
            int current = stack.pop();
            if (visited[current]) {
                trace.append(String.format(" %2d  | %-18s | %-7s | %d already visited (skip)\n",
                        step++, stack.toString(), arrayToString(visited), current));
                continue;
            }
            visited[current] = true;
            output.push(current);
            trace.append(String.format(" %2d  | %-18s | %-7s | Visit %d\n",
                    step++, stack.toString(), arrayToString(visited), current));

            // Push neighbors
            for (Graph.Edge edge : graph.getNeighbors(current)) {
                int neighbor = edge.getTo();
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                    trace.append(String.format("      | %-18s | %-7s |   → Push %d\n",
                            stack.toString(), arrayToString(visited), neighbor));
                    step++;
                }
            }
        }

        trace.append("-".repeat(50)).append("\n");
        trace.append("DFS complete!\n");
        trace.append("Traversal order: ");
        while (!output.isEmpty()) {
            trace.append(output.pop()).append(" ");
        }
        trace.append("\n");
        return trace.toString();
    }

    private static String arrayToString(boolean[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i] ? "1" : "0");
        }
        return sb.toString();
    }
}