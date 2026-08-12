package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.model.ServiceRequest;  // Example usage

/**
 * Custom Circular Queue implementation (array-based with wrap-around)
 *
 * This is a fixed-size queue that reuses space when elements are dequeued.
 * All methods must be implemented from scratch - NO built-in Queue!
 *
 * @param <T> The type of elements in this queue
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyCircularQueue<T> {

    // =============================================
    // FIELDS
    // =============================================

    private Object[] array;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    // =============================================
    // CONSTRUCTORS
    // =============================================

    public MyCircularQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive. Got: " + capacity);
        }
        this.capacity = capacity;
        this.array = new Object[capacity];
        this.front = 0;
        this.rear = -1;
        this.size = 0;
    }

    public MyCircularQueue() {
        this(10);
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
     * @throws IllegalStateException if queue is full
     */
    public void enqueue(T element) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full - cannot enqueue");
        }
        rear = (rear + 1) % capacity;
        array[rear] = element;
        size++;
    }

    /**
     * Remove and return the front element of the queue
     *
     * Time Complexity: O(1)
     *
     * @return The removed element
     * @throws IllegalStateException if queue is empty
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty - cannot dequeue");
        }
        T element = (T) array[front];
        array[front] = null;  // Help garbage collection
        front = (front + 1) % capacity;
        size--;
        return element;
    }

    /**
     * Peek at the front element without removing it
     *
     * Time Complexity: O(1)
     *
     * @return The front element
     * @throws IllegalStateException if queue is empty
     */
    @SuppressWarnings("unchecked")
    public T front() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty - cannot peek");
        }
        return (T) array[front];
    }

    /**
     * Check if the queue is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Check if the queue is full
     *
     * Time Complexity: O(1)
     *
     * @return true if full, false otherwise
     */
    public boolean isFull() {
        return size == capacity;
    }

    /**
     * Returns the number of elements in the queue
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return size;
    }

    /**
     * Returns the maximum capacity of the queue
     *
     * Time Complexity: O(1)
     *
     * @return The capacity
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Removes all elements from the queue
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            array[i] = null;
        }
        front = 0;
        rear = -1;
        size = 0;
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
        if (isEmpty()) return false;
        int current = front;
        for (int i = 0; i < size; i++) {
            if (o == null) {
                if (array[current] == null) return true;
            } else {
                if (o.equals(array[current])) return true;
            }
            current = (current + 1) % capacity;
        }
        return false;
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        if (isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        int current = front;
        for (int i = 0; i < size; i++) {
            sb.append(array[current]);
            if (i < size - 1) sb.append(", ");
            current = (current + 1) % capacity;
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Returns a string showing the internal array state (for debugging)
     *
     * @return String showing array contents with front/rear markers
     */
    public String debugString() {
        if (isEmpty()) return "[] (empty)";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < capacity; i++) {
            if (i == front && i == rear) {
                sb.append("F/R:").append(array[i]);
            } else if (i == front) {
                sb.append("F:").append(array[i]);
            } else if (i == rear) {
                sb.append("R:").append(array[i]);
            } else {
                sb.append(array[i]);
            }
            if (i < capacity - 1) sb.append(", ");
        }
        sb.append("] (front=").append(front).append(", rear=").append(rear)
                .append(", size=").append(size).append(", capacity=").append(capacity).append(")");
        return sb.toString();
    }
}