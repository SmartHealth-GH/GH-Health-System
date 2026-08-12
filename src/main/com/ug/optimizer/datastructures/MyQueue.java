package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.datastructures.MyLinkedList;
import main.com.ug.optimizer.model.ServiceRequest;  // For FIFO processing

/**
 * Custom Queue implementation (FIFO - First In, First Out)
 *
 * Uses MyLinkedList as the underlying storage.
 * All methods must be implemented from scratch - NO built-in Queue!
 *
 * @param <T> The type of elements in this queue
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyQueue<T> {

    // =============================================
    // FIELDS
    // =============================================

    private MyLinkedList<T> list;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MyQueue() {
        this.list = new MyLinkedList<>();
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Add an element to the back of the queue
     *
     * Time Complexity: O(1)
     *
     * @param element The element to enqueue
     */
    public void enqueue(T element) {
        list.addLast(element);
    }

    /**
     * Remove and return the front element of the queue
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if queue is empty
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty - cannot dequeue");
        }
        return list.removeFirst();
    }

    /**
     * Peek at the front element without removing it
     *
     * Time Complexity: O(1)
     *
     * @return The front element
     * @throws IllegalStateException if queue is empty
     */
    public T front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty - cannot peek");
        }
        return list.getFirst();
    }

    /**
     * Check if the queue is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the queue
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return list.size();
    }

    /**
     * Removes all elements from the queue
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        list.clear();
    }

    /**
     * Checks if the queue contains a specific element
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