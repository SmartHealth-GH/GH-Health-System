package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.datastructures.MyLinkedList;
import main.com.ug.optimizer.model.ServiceRequest;  // For urgent insertion

/**
 * Custom Deque (Double-Ended Queue) implementation
 *
 * Allows adding/removing from both ends.
 * Uses MyLinkedList as the underlying storage.
 * All methods must be implemented from scratch - NO built-in Deque!
 *
 * @param <T> The type of elements in this deque
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyDeque<T> {

    // =============================================
    // FIELDS
    // =============================================

    private MyLinkedList<T> list;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MyDeque() {
        this.list = new MyLinkedList<>();
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Add an element to the front of the deque
     *
     * Time Complexity: O(1)
     *
     * @param element The element to add
     */
    public void addFront(T element) {
        list.addFirst(element);
    }

    /**
     * Add an element to the rear of the deque
     *
     * Time Complexity: O(1)
     *
     * @param element The element to add
     */
    public void addRear(T element) {
        list.addLast(element);
    }

    /**
     * Remove and return the front element
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if deque is empty
     */
    public T removeFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty - cannot remove front");
        }
        return list.removeFirst();
    }

    /**
     * Remove and return the rear element
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if deque is empty
     */
    public T removeRear() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty - cannot remove rear");
        }
        return list.removeLast();
    }

    /**
     * Peek at the front element without removing it
     *
     * Time Complexity: O(1)
     *
     * @return The front element
     * @throws IllegalStateException if deque is empty
     */
    public T peekFront() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty - cannot peek front");
        }
        return list.getFirst();
    }

    /**
     * Peek at the rear element without removing it
     *
     * Time Complexity: O(1)
     *
     * @return The rear element
     * @throws IllegalStateException if deque is empty
     */
    public T peekRear() {
        if (isEmpty()) {
            throw new IllegalStateException("Deque is empty - cannot peek rear");
        }
        return list.getLast();
    }

    /**
     * Check if the deque is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the deque
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return list.size();
    }

    /**
     * Removes all elements from the deque
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        list.clear();
    }

    /**
     * Checks if the deque contains a specific element
     *
     * Time Complexity: O(n)
     *
     * @param o The element to search for
     * @return true if found, false otherwise
     */
    public boolean contains(Object o) {
        return list.contains(o);
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        return list.toString();
    }
}