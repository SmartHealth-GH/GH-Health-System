package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.model.Location;  // Example usage

/**
 * Custom Red-Black Tree implementation (self-balancing BST)
 *
 * Properties:
 * 1. Every node is either RED or BLACK
 * 2. Root is always BLACK
 * 3. All leaves (null) are BLACK
 * 4. RED nodes have only BLACK children
 * 5. Every path from root to leaf has same number of BLACK nodes
 *
 * All methods must be implemented from scratch - NO built-in TreeMap!
 *
 * @param <K> The type of keys (must extend Comparable)
 * @param <V> The type of values
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyRedBlackTree<K extends Comparable<K>, V> {

    // =============================================
    // COLORS
    // =============================================

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    // =============================================
    // NODE CLASS
    // =============================================

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        boolean color;

        Node(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.left = null;
            this.right = null;
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

    public MyRedBlackTree() {
        this.root = null;
        this.size = 0;
    }

    // =============================================
    // HELPER METHODS
    // =============================================

    private boolean isRed(Node<K, V> node) {
        return node != null && node.color == RED;
    }

    private boolean isBlack(Node<K, V> node) {
        return node == null || node.color == BLACK;
    }

    // =============================================
    // ROTATIONS
    // =============================================

    /**
     * Left rotate around a node
     *
     * Before:    x
     *            / \
     *           a   y
     *              / \
     *             b   c
     *
     * After:        y
     *              / \
     *             x   c
     *            / \
     *           a   b
     */
    private Node<K, V> rotateLeft(Node<K, V> node) {
        Node<K, V> x = node.right;
        node.right = x.left;
        x.left = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    /**
     * Right rotate around a node
     *
     * Before:      y
     *             / \
     *            x   c
     *           / \
     *          a   b
     *
     * After:    x
     *          / \
     *         a   y
     *            / \
     *           b   c
     */
    private Node<K, V> rotateRight(Node<K, V> node) {
        Node<K, V> x = node.left;
        node.left = x.right;
        x.right = node;
        x.color = node.color;
        node.color = RED;
        return x;
    }

    /**
     * Flip colors of a node and its children
     */
    private void flipColors(Node<K, V> node) {
        node.color = RED;
        if (node.left != null) {
            node.left.color = BLACK;
        }
        if (node.right != null) {
            node.right.color = BLACK;
        }
    }

    // =============================================
    // INSERT
    // =============================================

    /**
     * Insert a key-value pair into the Red-Black Tree
     *
     * Time Complexity: O(log n)
     *
     * @param key The key to insert
     * @param value The value to associate with the key
     */
    public void insert(K key, V value) {
        root = insert(root, key, value);
        root.color = BLACK;  // Root must always be BLACK
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        // Standard BST insertion
        if (node == null) {
            size++;
            return new Node<>(key, value, RED);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            // Key already exists - update value
            node.value = value;
            return node;
        }

        // =============================================
        // RED-BLACK TREE FIX-UP
        // =============================================

        // Case 1: Right child is RED, left child is BLACK or null
        // → Left rotate
        if (isRed(node.right) && isBlack(node.left)) {
            node = rotateLeft(node);
        }

        // Case 2: Left child is RED, and left-left grandchild is RED
        // → Right rotate
        if (isRed(node.left) && isRed(node.left.left)) {
            node = rotateRight(node);
        }

        // Case 3: Both children are RED
        // → Flip colors
        if (isRed(node.left) && isRed(node.right)) {
            flipColors(node);
        }

        return node;
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
        Node<K, V> node = search(root, key);
        return node == null ? null : node.value;
    }

    private Node<K, V> search(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return search(node.left, key);
        } else if (cmp > 0) {
            return search(node.right, key);
        } else {
            return node;
        }
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
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the tree (for testing)
     */
    public int height() {
        return height(root);
    }

    private int height(Node<K, V> node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    /**
     * Returns the maximum number of BLACK nodes on any path
     * (Black height - for verification of property 5)
     */
    public int blackHeight() {
        return blackHeight(root);
    }

    private int blackHeight(Node<K, V> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = blackHeight(node.left);
        int rightHeight = blackHeight(node.right);
        if (leftHeight != rightHeight) {
            // Property 5 violated - different black heights
            // This shouldn't happen in a valid Red-Black Tree
            return -1;
        }
        return leftHeight + (isBlack(node) ? 1 : 0);
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
        inorder(node.left);
        System.out.print(node.key + " ");
        inorder(node.right);
    }

    /**
     * Display tree structure with colors (for debugging)
     */
    public void printTree() {
        printTree(root, "", true);
        System.out.println();
    }

    private void printTree(Node<K, V> node, String prefix, boolean isLeft) {
        if (node == null) {
            return;
        }

        System.out.println(prefix + (isLeft ? "├── " : "└── ") +
                node.key + (isRed(node) ? " 🔴" : " ⚫"));

        if (node.left != null || node.right != null) {
            if (node.left != null) {
                printTree(node.left, prefix + (isLeft ? "│   " : "    "), true);
            } else {
                System.out.println(prefix + (isLeft ? "│   " : "    ") + "├── null ⚫");
            }

            if (node.right != null) {
                printTree(node.right, prefix + (isLeft ? "│   " : "    "), false);
            } else {
                System.out.println(prefix + (isLeft ? "│   " : "    ") + "└── null ⚫");
            }
        }
    }
}