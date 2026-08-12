package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.datastructures.MyArrayList;
import main.com.ug.optimizer.datastructures.MyLinkedList;
import main.com.ug.optimizer.model.Location;
import main.com.ug.optimizer.model.Road;

/**
 * Custom Graph implementation with both Adjacency List and Adjacency Matrix
 *
 * Supports:
 * - Adding vertices and edges
 * - Both adjacency list and matrix representations
 * - Getting neighbors of a vertex
 *
 * All methods must be implemented from scratch - NO built-in Graph classes!
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Graph {

    // =============================================
    // EDGE CLASS
    // =============================================

    public static class Edge {
        private int from;
        private int to;
        private int weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() { return from; }
        public int getTo() { return to; }
        public int getWeight() { return weight; }

        @Override
        public String toString() {
            return from + " → " + to + " (w=" + weight + ")";
        }
    }

    // =============================================
    // FIELDS
    // =============================================

    private int vertices;
    private MyArrayList<MyLinkedList<Edge>> adjacencyList;
    private int[][] adjacencyMatrix;
    private boolean directed;

    // =============================================
    // CONSTRUCTORS
    // =============================================

    /**
     * Creates a graph with a given number of vertices
     * Default: undirected
     *
     * @param vertices The number of vertices
     */
    public Graph(int vertices) {
        this(vertices, false);
    }

    /**
     * Creates a graph with a given number of vertices
     *
     * @param vertices The number of vertices
     * @param directed Whether the graph is directed
     */
    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.adjacencyMatrix = new int[vertices][vertices];
        this.adjacencyList = new MyArrayList<>();

        // Initialize adjacency list
        for (int i = 0; i < vertices; i++) {
            adjacencyList.add(new MyLinkedList<>());
        }

        // Initialize adjacency matrix
        for (int i = 0; i < vertices; i++) {
            for (int j = 0; j < vertices; j++) {
                adjacencyMatrix[i][j] = 0;
            }
        }
    }

    // =============================================
    // ADD METHODS
    // =============================================

    /**
     * Add a vertex to the graph
     *
     * Time Complexity: O(1)
     *
     * @return The index of the new vertex
     */
    public int addVertex() {
        int newVertex = vertices;
        vertices++;

        // Resize adjacency list
        adjacencyList.add(new MyLinkedList<>());

        // Resize adjacency matrix
        int[][] newMatrix = new int[vertices][vertices];
        for (int i = 0; i < vertices - 1; i++) {
            for (int j = 0; j < vertices - 1; j++) {
                newMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }
        adjacencyMatrix = newMatrix;

        return newVertex;
    }

    /**
     * Add an edge between two vertices
     *
     * Time Complexity: O(1) for adjacency list, O(1) for matrix
     *
     * @param from Source vertex
     * @param to Destination vertex
     * @param weight Edge weight
     */
    public void addEdge(int from, int to, int weight) {
        validateVertex(from);
        validateVertex(to);

        // Add to adjacency list
        adjacencyList.get(from).addLast(new Edge(from, to, weight));

        // Add to adjacency matrix
        adjacencyMatrix[from][to] = weight;

        // If undirected, add reverse edge
        if (!directed) {
            adjacencyList.get(to).addLast(new Edge(to, from, weight));
            adjacencyMatrix[to][from] = weight;
        }
    }

    /**
     * Add an edge with default weight 1
     */
    public void addEdge(int from, int to) {
        addEdge(from, to, 1);
    }

    // =============================================
    // GET METHODS
    // =============================================

    /**
     * Get the number of vertices
     */
    public int getVertexCount() {
        return vertices;
    }

    /**
     * Get the number of edges
     */
    public int getEdgeCount() {
        int count = 0;
        for (int i = 0; i < vertices; i++) {
            count += adjacencyList.get(i).size();
        }
        if (!directed) {
            count /= 2;
        }
        return count;
    }

    /**
     * Get the adjacency list representation
     */
    public MyArrayList<MyLinkedList<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    /**
     * Get the adjacency matrix representation
     */
    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    /**
     * Get neighbors of a vertex
     *
     * Time Complexity: O(1)
     *
     * @param vertex The vertex to get neighbors for
     * @return List of neighbor edges
     */
    public MyLinkedList<Edge> getNeighbors(int vertex) {
        validateVertex(vertex);
        return adjacencyList.get(vertex);
    }

    /**
     * Get the weight of an edge between two vertices
     *
     * Time Complexity: O(1)
     *
     * @param from Source vertex
     * @param to Destination vertex
     * @return The edge weight, or 0 if no edge
     */
    public int getEdgeWeight(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        return adjacencyMatrix[from][to];
    }

    /**
     * Check if there is an edge between two vertices
     */
    public boolean hasEdge(int from, int to) {
        validateVertex(from);
        validateVertex(to);
        return adjacencyMatrix[from][to] != 0;
    }

    /**
     * Check if the graph is directed
     */
    public boolean isDirected() {
        return directed;
    }

    // =============================================
    // VALIDATION
    // =============================================

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= vertices) {
            throw new IllegalArgumentException("Vertex " + vertex + " is out of bounds (0-" + (vertices - 1) + ")");
        }
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph (vertices=").append(vertices).append(", edges=").append(getEdgeCount())
                .append(", directed=").append(directed).append(")\n");

        sb.append("Adjacency List:\n");
        for (int i = 0; i < vertices; i++) {
            sb.append("  ").append(i).append(": ").append(adjacencyList.get(i)).append("\n");
        }

        sb.append("Adjacency Matrix:\n");
        for (int i = 0; i < vertices; i++) {
            sb.append("  ");
            for (int j = 0; j < vertices; j++) {
                sb.append(adjacencyMatrix[i][j]).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}