package main.com.ug.optimizer.datastructures.linkedlist;

import main.com.ug.optimizer.model.ServiceRequest;  // Example usage

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Custom Doubly Linked List implementation
 *
 * This is a doubly linked list where each node points to both next and previous.
 * All methods must be implemented from scratch - NO built-in LinkedList!
 *
 * @param <T> The type of elements in this list
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyLinkedList<T> implements Iterable<T> {
    public T get(int i) {
        return null;
    }

    // =============================================
    // NODE CLASS
    // =============================================

    /**
     * Inner Node class for the linked list
     */
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }

        Node(T data, Node<T> next, Node<T> prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    // =============================================
    // FIELDS
    // =============================================

    private Node<T> head;
    private Node<T> tail;
    private int size;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Add element to the front of the list
     *
     * Time Complexity: O(1)
     *
     * @param element The element to add
     */
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    /**
     * Add element to the end of the list
     *
     * Time Complexity: O(1)
     *
     * @param element The element to add
     */
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }

    /**
     * Insert a new element after a specific node
     *
     * Time Complexity: O(n) - needs to find the node
     *
     * @param target The element to insert after
     * @param element The new element to insert
     * @return true if successful, false if target not found
     */
    public boolean insertAfter(T target, T element) {
        Node<T> current = head;
        while (current != null && !current.data.equals(target)) {
            current = current.next;
        }

        if (current == null) {
            return false;  // Target not found
        }

        Node<T> newNode = new Node<>(element, current.next, current);
        if (current.next != null) {
            current.next.prev = newNode;
        } else {
            tail = newNode;  // Inserted at the end
        }
        current.next = newNode;
        size++;
        return true;
    }

    /**
     * Remove the first occurrence of an element
     *
     * Time Complexity: O(n) - needs to find the element
     *
     * @param element The element to remove
     * @return true if removed, false if not found
     */
    public boolean remove(T element) {
        Node<T> current = head;
        while (current != null && !current.data.equals(element)) {
            current = current.next;
        }

        if (current == null) {
            return false;  // Element not found
        }

        // Remove the node
        if (current == head) {
            head = current.next;
            if (head != null) {
                head.prev = null;
            }
        } else if (current == tail) {
            tail = current.prev;
            if (tail != null) {
                tail.next = null;
            }
        } else {
            current.prev.next = current.next;
            current.next.prev = current.prev;
        }

        size--;
        return true;
    }

    /**
     * Remove the first element of the list
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if list is empty
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        T data = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        return data;
    }

    /**
     * Remove the last element of the list
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if list is empty
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        T data = tail.data;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return data;
    }

    /**
     * Get the first element without removing
     *
     * Time Complexity: O(1)
     *
     * @return The first element
     * @throws IllegalStateException if list is empty
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return head.data;
    }

    /**
     * Get the last element without removing
     *
     * Time Complexity: O(1)
     *
     * @return The last element
     * @throws IllegalStateException if list is empty
     */
    public T getLast() {
        if (isEmpty()) {
            throw new IllegalStateException("List is empty");
        }
        return tail.data;
    }

    /**
     * Returns the number of elements
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the list is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the list
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    /**
     * Checks if the list contains a specific element
     *
     * Time Complexity: O(n)
     *
     * @param o The element to search for
     * @return true if found, false otherwise
     */
    public boolean contains(Object o) {
        Node<T> current = head;
        while (current != null) {
            if (o == null) {
                if (current.data == null) return true;
            } else {
                if (o.equals(current.data)) return true;
            }
            current = current.next;
        }
        return false;
    }

    // =============================================
    // ITERATOR
    // =============================================

    @Override
    public Iterator<T> iterator() {
        return new MyLinkedListIterator();
    }

    /**
     * Custom Iterator for traversing the list
     */
    private class MyLinkedListIterator implements Iterator<T> {
        private Node<T> current = head;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) sb.append(", ");
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}