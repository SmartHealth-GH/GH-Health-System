package main.com.ug.optimizer.datastructures.btree;

import main.com.ug.optimizer.model.AlgorithmRun;  // Example usage

import java.util.ArrayList;
import java.util.List;

/**
 * Custom B-Tree implementation
 *
 * A B-Tree is a self-balancing tree data structure that maintains sorted data
 * and allows searches, sequential access, insertions, and deletions in logarithmic time.
 *
 * Minimum degree t = 2
 * - Max keys per node: 2t - 1 = 3
 * - Min keys per node (except root): t - 1 = 1
 * - Max children per node: 2t = 4
 *
 * All methods must be implemented from scratch - NO built-in TreeMap!
 *
 * @param <K> The type of keys (must extend Comparable)
 * @param <V> The type of values
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyBTree<K extends Comparable<K>, V> {

    // =============================================
    // CONSTANTS
    // =============================================

    private static final int T = 2;  // Minimum degree
    private static final int MAX_KEYS = 2 * T - 1;  // 3
    private static final int MIN_KEYS = T - 1;      // 1

    // =============================================
    // NODE CLASS
    // =============================================

    private static class Node<K, V> {
        List<K> keys;
        List<V> values;
        List<Node<K, V>> children;
        boolean isLeaf;

        Node(boolean isLeaf) {
            this.keys = new ArrayList<>();
            this.values = new ArrayList<>();
            this.children = new ArrayList<>();
            this.isLeaf = isLeaf;
        }

        boolean isFull() {
            return keys.size() == MAX_KEYS;
        }

        boolean isUnderflow() {
            return keys.size() < MIN_KEYS;
        }

        @Override
        public String toString() {
            return keys.toString();
        }
    }

    // =============================================
    // FIELDS
    // =============================================

    private Node<K, V> root;
    private int size;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MyBTree() {
        this.root = new Node<>(true);
        this.size = 0;
    }

    // =============================================
    // INSERT
    // =============================================

    /**
     * Insert a key-value pair into the B-Tree
     *
     * Time Complexity: O(log n)
     *
     * @param key The key to insert
     * @param value The value to associate with the key
     */
    public void insert(K key, V value) {
        Node<K, V> r = root;

        // If root is full, split it
        if (r.isFull()) {
            Node<K, V> newRoot = new Node<>(false);
            newRoot.children.add(r);
            splitChild(newRoot, 0);
            root = newRoot;
            insertNonFull(root, key, value);
        } else {
            insertNonFull(r, key, value);
        }
    }

    /**
     * Insert into a non-full node
     */
    private void insertNonFull(Node<K, V> node, K key, V value) {
        int i = node.keys.size() - 1;

        if (node.isLeaf) {
            // Find position and insert
            while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            node.keys.add(i + 1, key);
            node.values.add(i + 1, value);
            size++;
        } else {
            // Find child to insert into
            while (i >= 0 && key.compareTo(node.keys.get(i)) < 0) {
                i--;
            }
            i++;

            // If child is full, split it
            if (node.children.get(i).isFull()) {
                splitChild(node, i);
                // Determine which child to go to
                if (key.compareTo(node.keys.get(i)) > 0) {
                    i++;
                }
            }
            insertNonFull(node.children.get(i), key, value);
        }
    }

    /**
     * Split a child node during insertion
     */
    private void splitChild(Node<K, V> parent, int index) {
        Node<K, V> child = parent.children.get(index);
        Node<K, V> newChild = new Node<>(child.isLeaf);

        // Move median key up to parent
        // For T=2, median is at index 1 (0-based)
        // Keys: [0, 1, 2] → median = 1
        int medianIndex = T - 1;  // 1

        // Move median key to parent
        parent.keys.add(index, child.keys.get(medianIndex));
        parent.values.add(index, child.values.get(medianIndex));
        parent.children.add(index + 1, newChild);

        // Move keys > median to new child
        for (int i = medianIndex + 1; i < child.keys.size(); i++) {
            newChild.keys.add(child.keys.get(i));
            newChild.values.add(child.values.get(i));
        }

        // Remove keys > median from child
        for (int i = child.keys.size() - 1; i >= medianIndex; i--) {
            child.keys.remove(i);
            child.values.remove(i);
        }

        // Move children if not leaf
        if (!child.isLeaf) {
            for (int i = medianIndex + 1; i < child.children.size(); i++) {
                newChild.children.add(child.children.get(i));
            }
            for (int i = child.children.size() - 1; i > medianIndex; i--) {
                child.children.remove(i);
            }
        }
    }

    // =============================================
    // SEARCH
    // =============================================

    /**
     * Search for a value by key
     *
     * Time Complexity: O(log n)
     *
     * @param key The key to search for
     * @return The value associated with the key, or null if not found
     */
    public V search(K key) {
        return search(root, key);
    }

    private V search(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int i = 0;
        while (i < node.keys.size() && key.compareTo(node.keys.get(i)) > 0) {
            i++;
        }

        // Found the key
        if (i < node.keys.size() && key.compareTo(node.keys.get(i)) == 0) {
            return node.values.get(i);
        }

        // Key not found and this is a leaf
        if (node.isLeaf) {
            return null;
        }

        // Continue searching in child
        return search(node.children.get(i), key);
    }

    /**
     * Check if the tree contains a key
     *
     * Time Complexity: O(log n)
     *
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    public boolean contains(K key) {
        return search(key) != null;
    }

    // =============================================
    // TREE PROPERTIES
    // =============================================

    /**
     * Returns the number of elements in the tree
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the tree is empty
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the tree
     */
    public void clear() {
        root = new Node<>(true);
        size = 0;
    }

    /**
     * Returns the height of the tree
     */
    public int height() {
        return height(root);
    }

    private int height(Node<K, V> node) {
        if (node == null) {
            return 0;
        }
        if (node.isLeaf) {
            return 1;
        }
        return 1 + height(node.children.get(0));
    }

    // =============================================
    // TRAVERSALS
    // =============================================

    /**
     * Inorder traversal (sorted order)
     */
    public void inorder() {
        inorder(root);
        System.out.println();
    }

    private void inorder(Node<K, V> node) {
        if (node == null) {
            return;
        }

        for (int i = 0; i < node.keys.size(); i++) {
            if (!node.isLeaf) {
                inorder(node.children.get(i));
            }
            System.out.print(node.keys.get(i) + " ");
        }

        if (!node.isLeaf) {
            inorder(node.children.get(node.keys.size()));
        }
    }

    /**
     * Print tree structure
     */
    public void printTree() {
        printTree(root, "", true);
        System.out.println();
    }

    private void printTree(Node<K, V> node, String prefix, boolean isLeft) {
        if (node == null) {
            return;
        }

        String nodeStr = node.keys.toString();
        System.out.println(prefix + (isLeft ? "├── " : "└── ") + nodeStr);

        if (!node.isLeaf) {
            for (int i = 0; i < node.children.size(); i++) {
                String newPrefix = prefix + (isLeft ? "│   " : "    ");
                printTree(node.children.get(i), newPrefix, i < node.children.size() - 1);
            }
        }
    }
}