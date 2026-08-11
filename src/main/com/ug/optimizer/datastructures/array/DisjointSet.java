package main.com.ug.optimizer.datastructures.disjointset;

import main.com.ug.optimizer.model.Location;  // Example usage

/**
 * Custom Disjoint Set (Union-Find) implementation
 *
 * Used for tracking connected components in a graph.
 * Features:
 * - Path compression for efficient finds
 * - Union by rank for balanced trees
 *
 * All methods must be implemented from scratch - NO built-in classes!
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class DisjointSet {

    // =============================================
    // FIELDS
    // =============================================

    private int[] parent;
    private int[] rank;
    private int size;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    /**
     * Creates a Disjoint Set with a given number of elements
     * Initially, each element is in its own set
     *
     * @param n The number of elements
     */
    public DisjointSet(int n) {
        this.size = n;
        this.parent = new int[n];
        this.rank = new int[n];

        // Initialize: each element is its own parent
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Make a set for a single element
     * (Already done in constructor, but kept for completeness)
     *
     * Time Complexity: O(1)
     *
     * @param x The element to create a set for
     */
    public void makeSet(int x) {
        if (x < 0 || x >= size) {
            throw new IllegalArgumentException("Index out of bounds: " + x);
        }
        parent[x] = x;
        rank[x] = 0;
    }

    /**
     * Find the root/representative of the set containing x
     * Uses path compression for efficiency
     *
     * Time Complexity: O(α(n)) ≈ O(1) amortized
     *
     * @param x The element to find
     * @return The root of the set containing x
     */
    public int find(int x) {
        if (x < 0 || x >= size) {
            throw new IllegalArgumentException("Index out of bounds: " + x);
        }

        // Path compression: make every node point directly to root
        if (parent[x] != x) {
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }

    /**
     * Union two sets containing x and y
     * Uses union by rank for balanced trees
     *
     * Time Complexity: O(α(n)) ≈ O(1) amortized
     *
     * @param x First element
     * @param y Second element
     * @return true if union was performed, false if already connected
     */
    public boolean union(int x, int y) {
        if (x < 0 || x >= size || y < 0 || y >= size) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        int rootX = find(x);
        int rootY = find(y);

        // Already in the same set
        if (rootX == rootY) {
            return false;
        }

        // Union by rank: attach smaller rank tree under larger rank tree
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // Same rank: make one root the parent of the other
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        return true;
    }

    /**
     * Check if two elements are in the same set
     *
     * Time Complexity: O(α(n)) ≈ O(1) amortized
     *
     * @param x First element
     * @param y Second element
     * @return true if connected, false otherwise
     */
    public boolean isConnected(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * Returns the number of elements in the set
     */
    public int size() {
        return size;
    }

    /**
     * Returns the number of disjoint sets
     */
    public int countSets() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (parent[i] == i) {
                count++;
            }
        }
        return count;
    }

    /**
     * Prints the current state of the parent array (for debugging)
     */
    public void printState() {
        System.out.print("Parent: [");
        for (int i = 0; i < size; i++) {
            System.out.print(parent[i]);
            if (i < size - 1) System.out.print(", ");
        }
        System.out.println("]");

        System.out.print("Rank:   [");
        for (int i = 0; i < size; i++) {
            System.out.print(rank[i]);
            if (i < size - 1) System.out.print(", ");
        }
        System.out.println("]");
    }
}