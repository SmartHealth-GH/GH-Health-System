package main.com.ug.optimizer.datastructures.bst;

import main.com.ug.optimizer.model.Location;  // Example usage

/**
 * Custom Binary Search Tree implementation
 *
 * All methods must be implemented from scratch - NO built-in TreeMap!
 *
 * @param <K> The type of keys (must extend Comparable)
 * @param <V> The type of values
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyBST<K extends Comparable<K>, V> {

    // =============================================
    // NODE CLASS
    // =============================================

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
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

    public MyBST() {
        this.root = null;
        this.size = 0;
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Insert a key-value pair into the BST
     *
     * Time Complexity: O(log n) average, O(n) worst-case
     *
     * @param key The key to insert
     * @param value The value to associate with the key
     */
    public void insert(K key, V value) {
        root = insert(root, key, value);
    }

    private Node<K, V> insert(Node<K, V> node, K key, V value) {
        if (node == null) {
            size++;
            return new Node<>(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            // Key already exists - update value
            node.value = value;
        }
        return node;
    }

    /**
     * Search for a value by key
     *
     * Time Complexity: O(log n) average, O(n) worst-case
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
     * Check if the BST contains a key
     *
     * Time Complexity: O(log n) average, O(n) worst-case
     *
     * @param key The key to check
     * @return true if key exists, false otherwise
     */
    public boolean contains(K key) {
        return search(key) != null;
    }

    /**
     * Get the minimum key in the BST
     *
     * Time Complexity: O(log n) average, O(n) worst-case
     *
     * @return The minimum key
     * @throws IllegalStateException if tree is empty
     */
    public K getMinKey() {
        if (isEmpty()) {
            throw new IllegalStateException("Tree is empty");
        }
        Node<K, V> node = root;
        while (node.left != null) {
            node = node.left;
        }
        return node.key;
    }

    /**
     * Get the maximum key in the BST
     *
     * Time Complexity: O(log n) average, O(n) worst-case
     *
     * @return The maximum key
     * @throws IllegalStateException if tree is empty
     */
    public K getMaxKey() {
        if (isEmpty()) {
            throw new IllegalStateException("Tree is empty");
        }
        Node<K, V> node = root;
        while (node.right != null) {
            node = node.right;
        }
        return node.key;
    }

    /**
     * Returns the number of elements in the BST
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the BST is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the BST
     */
    public void clear() {
        root = null;
        size = 0;
    }

    // =============================================
    // TRAVERSALS
    // =============================================

    /**
     * Inorder traversal (left → root → right)
     * Returns keys in sorted order
     *
     * Time Complexity: O(n)
     */
    public void inorder() {
        System.out.print("Inorder: ");
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
     * Preorder traversal (root → left → right)
     *
     * Time Complexity: O(n)
     */
    public void preorder() {
        System.out.print("Preorder: ");
        preorder(root);
        System.out.println();
    }

    private void preorder(Node<K, V> node) {
        if (node == null) {
            return;
        }
        System.out.print(node.key + " ");
        preorder(node.left);
        preorder(node.right);
    }

    /**
     * Postorder traversal (left → right → root)
     *
     * Time Complexity: O(n)
     */
    public void postorder() {
        System.out.print("Postorder: ");
        postorder(root);
        System.out.println();
    }

    private void postorder(Node<K, V> node) {
        if (node == null) {
            return;
        }
        postorder(node.left);
        postorder(node.right);
        System.out.print(node.key + " ");
    }

    /**
     * Returns the height of the tree
     *
     * Time Complexity: O(n)
     *
     * @return The height (number of edges on longest path)
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

    // =============================================
    // DELETE (Optional but useful)
    // =============================================

    /**
     * Delete a key from the BST
     *
     * Time Complexity: O(log n) average, O(n) worst-case
     *
     * @param key The key to delete
     * @return true if deleted, false if key not found
     */
    public boolean delete(K key) {
        if (!contains(key)) {
            return false;
        }
        root = delete(root, key);
        size--;
        return true;
    }

    private Node<K, V> delete(Node<K, V> node, K key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = delete(node.left, key);
        } else if (cmp > 0) {
            node.right = delete(node.right, key);
        } else {
            // Found the node to delete
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Node has two children - replace with inorder successor
            Node<K, V> successor = getMinNode(node.right);
            node.key = successor.key;
            node.value = successor.value;
            node.right = delete(node.right, successor.key);
        }
        return node;
    }

    private Node<K, V> getMinNode(Node<K, V> node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toString(root, sb);
        return sb.toString();
    }

    private void toString(Node<K, V> node, StringBuilder sb) {
        if (node == null) {
            sb.append("null");
            return;
        }
        sb.append(node.key).append(":[");
        toString(node.left, sb);
        sb.append(",");
        toString(node.right, sb);
        sb.append("]");
    }
}