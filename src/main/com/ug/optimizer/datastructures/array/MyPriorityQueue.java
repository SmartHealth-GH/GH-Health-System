package main.com.ug.optimizer.datastructures.priorityqueue;

import main.com.ug.optimizer.datastructures.array.MyArrayList;
import main.com.ug.optimizer.model.ServiceRequest;  // For urgency-based priority

/**
 * Custom Priority Queue implementation using Min-Heap
 *
 * Elements are ordered by their natural ordering (Comparable).
 * The smallest element is always at the front.
 * Uses MyArrayList as the underlying storage.
 * All methods must be implemented from scratch - NO built-in PriorityQueue!
 *
 * @param <T> The type of elements (must extend Comparable)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyPriorityQueue<T extends Comparable<T>> {

    // =============================================
    // FIELDS
    // =============================================

    private MyArrayList<T> heap;

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MyPriorityQueue() {
        this.heap = new MyArrayList<>();
    }

    public MyPriorityQueue(int initialCapacity) {
        this.heap = new MyArrayList<>(initialCapacity);
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Insert an element into the priority queue
     *
     * Time Complexity: O(log n)
     *
     * @param element The element to insert
     */
    public void insert(T element) {
        heap.add(element);
        siftUp(heap.size() - 1);
    }

    /**
     * Extract and return the minimum element
     *
     * Time Complexity: O(log n)
     *
     * @return The minimum element
     * @throws IllegalStateException if queue is empty
     */
    public T extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty - cannot extract");
        }
        T min = heap.get(0);
        T last = heap.get(heap.size() - 1);
        heap.set(0, last);
        heap.remove(heap.size() - 1);
        if (!isEmpty()) {
            siftDown(0);
        }
        return min;
    }

    /**
     * Peek at the minimum element without removing it
     *
     * Time Complexity: O(1)
     *
     * @return The minimum element
     * @throws IllegalStateException if queue is empty
     */
    public T peekMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Priority queue is empty - cannot peek");
        }
        return heap.get(0);
    }

    /**
     * Check if the priority queue is empty
     *
     * Time Complexity: O(1)
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Returns the number of elements in the priority queue
     *
     * Time Complexity: O(1)
     *
     * @return The current size
     */
    public int size() {
        return heap.size();
    }

    /**
     * Removes all elements from the priority queue
     *
     * Time Complexity: O(n)
     */
    public void clear() {
        heap.clear();
    }

    /**
     * Checks if the priority queue contains a specific element
     *
     * Time Complexity: O(n)
     *
     * @param o The element to search for
     * @return true if found, false otherwise
     */
    public boolean contains(Object o) {
        return heap.contains(o);
    }

    // =============================================
    // PRIVATE HELPER METHODS
    // =============================================

    /**
     * Sift up an element to maintain heap property
     *
     * Time Complexity: O(log n)
     *
     * @param index The index to sift up
     */
    private void siftUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap.get(index).compareTo(heap.get(parent)) >= 0) {
                break;
            }
            swap(index, parent);
            index = parent;
        }
    }

    /**
     * Sift down an element to maintain heap property
     *
     * Time Complexity: O(log n)
     *
     * @param index The index to sift down
     */
    private void siftDown(int index) {
        int left = 2 * index + 1;
        int right = 2 * index + 2;
        int smallest = index;

        if (left < heap.size() && heap.get(left).compareTo(heap.get(smallest)) < 0) {
            smallest = left;
        }
        if (right < heap.size() && heap.get(right).compareTo(heap.get(smallest)) < 0) {
            smallest = right;
        }
        if (smallest != index) {
            swap(index, smallest);
            siftDown(smallest);
        }
    }

    /**
     * Swap two elements in the heap
     */
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        return heap.toString();
    }

    /**
     * Returns the internal heap array (for debugging)
     */
    public MyArrayList<T> getHeap() {
        return heap;
    }
}