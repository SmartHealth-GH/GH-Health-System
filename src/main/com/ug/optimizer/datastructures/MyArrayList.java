package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.model.Location;  // Example usage

/**
 * Custom Dynamic Array implementation (ArrayList from scratch)
 *
 * This is a resizable array implementation that grows when full.
 * All methods must be implemented from scratch - NO built-in ArrayList!
 *
 * @param <T> The type of elements in this list
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyArrayList<T> {

    // =============================================
    // FIELDS
    // =============================================

    /**
     * Internal array to store elements
     */
    private Object[] elements;

    /**
     * Number of elements currently in the list
     */
    private int size;

    /**
     * Default initial capacity
     */
    private static final int DEFAULT_CAPACITY = 10;

    // =============================================
    // CONSTRUCTORS
    // =============================================

    /**
     * Default constructor - creates list with capacity 10
     */
    public MyArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }

    /**
     * Constructor with custom initial capacity
     *
     * @param initialCapacity The starting capacity
     * @throws IllegalArgumentException if capacity <= 0
     */
    public MyArrayList(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive. Got: " + initialCapacity);
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Add an element to the end of the list
     *
     * Time Complexity: O(1) amortized, O(n) when resizing
     *
     * @param element The element to add
     */
    public void add(T element) {
        // If array is full, double the capacity
        if (size == elements.length) {
            resize();
        }
        elements[size] = element;
        size++;
    }

    /**
     * Get element at specified index
     *
     * Time Complexity: O(1)
     *
     * @param index The position to retrieve
     * @return The element at that position
     * @throws IndexOutOfBoundsException if index is invalid
     */
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }

    /**
     * Replace element at specified index
     *
     * Time Complexity: O(1)
     *
     * @param index The position to update
     * @param element The new element
     * @return The old element that was replaced
     * @throws IndexOutOfBoundsException if index is invalid
     */
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T old = (T) elements[index];
        elements[index] = element;
        return old;
    }

    /**
     * Remove element at specified index
     *
     * Time Complexity: O(n) - shifts elements left
     *
     * @param index The position to remove
     * @return The removed element
     * @throws IndexOutOfBoundsException if index is invalid
     */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) elements[index];

        // Shift all elements left by one position
        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        // Clear the last position to help garbage collection
        elements[size - 1] = null;
        size--;

        return removed;
    }

    /**
     * Returns the number of elements in the list
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

    // =============================================
    // PRIVATE HELPER METHODS
    // =============================================

    /**
     * Doubles the capacity of the internal array
     *
     * Time Complexity: O(n) - copies all elements
     */
    private void resize() {
        int newCapacity = elements.length * 2;
        Object[] newArray = new Object[newCapacity];

        // Copy elements from old array to new array
        for (int i = 0; i < size; i++) {
            newArray[i] = elements[i];
        }
        elements = newArray;
    }

    /**
     * Validates that the index is within bounds
     *
     * @param index The index to check
     * @throws IndexOutOfBoundsException if index is invalid
     */
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }

    // =============================================
    // ADDITIONAL USEFUL METHODS
    // =============================================

    /**
     * Removes all elements from the list
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        // Clear references to help garbage collection
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
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
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (elements[i] == null) return true;
            } else {
                if (o.equals(elements[i])) return true;
            }
        }
        return false;
    }

    /**
     * Returns the index of the first occurrence of an element
     *
     * Time Complexity: O(n)
     *
     * @param o The element to search for
     * @return The index, or -1 if not found
     */
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (elements[i] == null) return i;
            } else {
                if (o.equals(elements[i])) return i;
            }
        }
        return -1;
    }

    /**
     * Returns the current capacity of the internal array
     * (Useful for testing resize behavior)
     *
     * @return The current capacity
     */
    public int getCapacity() {
        return elements.length;
    }

    // =============================================
    // TOSTRING & ITERATOR
    // =============================================

    /**
     * Returns a string representation of the list
     *
     * @return String like [elem1, elem2, elem3]
     */
    @Override
    public String toString() {
        if (size == 0) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}