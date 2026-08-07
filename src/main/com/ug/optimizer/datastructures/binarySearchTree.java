package main.com.ug.optimizer.datastructures;

public class binarySearchTree {
    Node root;

    // Constructor for the tree
    public binarySearchTree() {
        root = null;
    }

    public void insert(int data) {
        root = insertRecursive(root, data);
    }

    private Node insertRecursive(Node current, int data) {
        // If the spot is empty, create a new node here!
        if (current == null) {
            return new Node(data);
        }

        // If the data is smaller, go to the left subtree
        if (data < current.data) {
            current.left = insertRecursive(current.left, data);
        } 
        // If the data is larger, go to the right subtree
        else if (data > current.data) {
            current.right = insertRecursive(current.right, data);
        }

        // If data is already there, we do nothing (no duplicates allowed)
        return current;
    }

    // --- 2. SEARCH METHOD ---
    public boolean search(int data) {
        return searchRecursive(root, data);
    }

    // Helper method for searching (recursion)
    private boolean searchRecursive(Node current, int data) {
        // Base case: If current is null, the value isn't here
        if (current == null) {
            return false;
        }

        // If we found the value, return true!
        if (data == current.data) {
            return true;
        }

        // If the value is smaller, look left. Otherwise, look right.
        return data < current.data 
            ? searchRecursive(current.left, data) 
            : searchRecursive(current.right, data);
    }

    // --- 3. IN-ORDER PRINT (to see the sorted tree) ---
    public void inorder() {
        inorderRecursive(root);
        System.out.println();
    }

    private void inorderRecursive(Node current) {
        if (current != null) {
            inorderRecursive(current.left);
            System.out.print(current.data + " ");
            inorderRecursive(current.right);
        }
    }

    
    
}

