package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.datastructures.MyArrayList;
import main.com.ug.optimizer.model.AuditEvent;  // For undo log

/**
 * Custom Stack implementation (LIFO - Last In, First Out)
 *
 * Uses MyArrayList as the underlying storage.
 * All methods must be implemented from scratch - NO built-in Stack!
 *
 * @param <T> The type of elements in this stack
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyStack<T> {

    // =============================================
    // FIELDS
    // =============================================

    private MyArrayList<T> list;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MyStack() {
        this.list = new MyArrayList<>();
    }

    public MyStack(int initialCapacity) {
        this.list = new MyArrayList<>(initialCapacity);
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Push an element onto the top of the stack
     *
     * Time Complexity: O(1) amortized
     *
     * @param element The element to push
     */
    public void push(T element) {
        list.add(element);
    }

    /**
     * Pop the top element from the stack
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if stack is empty
     */
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty - cannot pop");
        }
        return list.remove(list.size() - 1);
    }

    /**
     * Peek at the top element without removing it
     *
     * Time Complexity: O(1)
     *
     * @return The top element
     * @throws IllegalStateException if stack is empty
     */
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty - cannot peek");
        }
        return list.get(list.size() - 1);
    }

    /**
     * Check if the stack is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Returns the number of elements in the stack
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return list.size();
    }

    /**
     * Removes all elements from the stack
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        list.clear();
    }

    /**
     * Checks if the stack contains a specific element
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