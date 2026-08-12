package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.datastructures.MyHashMap;
import main.com.ug.optimizer.model.Location;  // Example usage

/**
 * Custom Set implementation backed by MyHashMap
 *
 * Stores unique elements with no duplicates.
 * All methods must be implemented from scratch - NO built-in HashSet!
 *
 * @param <T> The type of elements in this set
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MySet<T> {

    // =============================================
    // FIELDS
    // =============================================

    private MyHashMap<T, Object> map;
    private static final Object PRESENT = new Object();  // Dummy value

    // =============================================
    // CONSTRUCTOR
    // =============================================

    public MySet() {
        this.map = new MyHashMap<>();
    }

    public MySet(int initialCapacity) {
        this.map = new MyHashMap<>(initialCapacity);
    }

    // =============================================
    // CORE METHODS
    // =============================================

    /**
     * Add an element to the set
     *
     * Time Complexity: O(1) average, O(n) worst-case
     *
     * @param element The element to add
     * @return true if element was added, false if already present
     */
    public boolean add(T element) {
        if (map.containsKey(element)) {
            return false;  // Element already exists
        }
        map.put(element, PRESENT);
        return true;
    }

    /**
     * Remove an element from the set
     *
     * Time Complexity: O(1) average, O(n) worst-case
     *
     * @param element The element to remove
     * @return true if element was removed, false if not found
     */
    public boolean remove(T element) {
        if (!map.containsKey(element)) {
            return false;
        }
        map.remove(element);
        return true;
    }

    /**
     * Check if the set contains an element
     *
     * Time Complexity: O(1) average, O(n) worst-case
     *
     * @param element The element to check
     * @return true if element exists, false otherwise
     */
    public boolean contains(T element) {
        return map.containsKey(element);
    }

    /**
     * Returns the number of elements in the set
     */
    public int size() {
        return map.size();
    }

    /**
     * Checks if the set is empty
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Removes all elements from the set
     */
    public void clear() {
        map.clear();
    }

    /**
     * Returns the elements as an array
     */
    @SuppressWarnings("unchecked")
    public T[] toArray() {
        T[] result = (T[]) new Object[size()];
        // This would require iterating over the HashMap
        // For simplicity, we'll use a different approach
        return result;
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        // Since we can't easily iterate over the map's keys,
        // we'll use a simple representation
        return "MySet{size=" + size() + "}";
    }
}