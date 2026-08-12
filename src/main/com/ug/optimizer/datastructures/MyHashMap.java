package main.com.ug.optimizer.datastructures;

/**
 * Custom HashMap implementation with chaining (separate chaining)
 *
 * Uses an array of linked lists (Entry chains) to handle collisions.
 * All methods must be implemented from scratch - NO built-in HashMap!
 *
 * ✅ NOW SUPPORTS NULL KEYS!
 *
 * @param <K> The type of keys
 * @param <V> The type of values
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyHashMap<K, V> {

    // =============================================
    // CONSTANTS
    // =============================================

    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    // =============================================
    // ENTRY CLASS
    // =============================================

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    // =============================================
    // FIELDS
    // =============================================

    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private double loadFactor;
    private int collisions;

    // =============================================
    // CONSTRUCTORS
    // =============================================

    @SuppressWarnings("unchecked")
    public MyHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Entry[capacity];
        this.size = 0;
        this.collisions = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = initialCapacity;
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.table = new Entry[capacity];
        this.size = 0;
        this.collisions = 0;
    }

    @SuppressWarnings("unchecked")
    public MyHashMap(int initialCapacity, double loadFactor) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        if (loadFactor <= 0 || loadFactor > 1) {
            throw new IllegalArgumentException("Load factor must be between 0 and 1");
        }
        this.capacity = initialCapacity;
        this.loadFactor = loadFactor;
        this.table = new Entry[capacity];
        this.size = 0;
        this.collisions = 0;
    }

    // =============================================
    // HASHING (SUPPORTS NULL KEYS)
    // =============================================

    private int hash(K key) {
        if (key == null) {
            return 0;  // null keys always go to bucket 0
        }
        return Math.abs(key.hashCode()) % capacity;
    }

    // =============================================
    // CORE METHODS
    // =============================================

    public void put(K key, V value) {
        int index = hash(key);
        Entry<K, V> current = table[index];

        // Check if key already exists
        while (current != null) {
            if (key == null) {
                if (current.key == null) {
                    current.value = value;
                    return;
                }
            } else if (key.equals(current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }

        // Insert new entry at the beginning of the chain
        Entry<K, V> newEntry = new Entry<>(key, value);
        if (table[index] != null) {
            collisions++;
        }
        newEntry.next = table[index];
        table[index] = newEntry;
        size++;

        if ((double) size / capacity > loadFactor) {
            resize();
        }
    }

    public V get(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];

        while (current != null) {
            if (key == null) {
                if (current.key == null) {
                    return current.value;
                }
            } else if (key.equals(current.key)) {
                return current.value;
            }
            current = current.next;
        }
        return null;
    }

    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> current = table[index];
        Entry<K, V> prev = null;

        while (current != null) {
            boolean matches;
            if (key == null) {
                matches = (current.key == null);
            } else {
                matches = key.equals(current.key);
            }

            if (matches) {
                if (prev == null) {
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return current.value;
            }
            prev = current;
            current = current.next;
        }
        return null;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        table = new Entry[capacity];
        size = 0;
        collisions = 0;
    }

    // =============================================
    // RESIZE
    // =============================================

    @SuppressWarnings("unchecked")
    private void resize() {
        int oldCapacity = capacity;
        capacity *= 2;
        Entry<K, V>[] oldTable = table;
        table = new Entry[capacity];
        size = 0;
        collisions = 0;

        for (Entry<K, V> entry : oldTable) {
            Entry<K, V> current = entry;
            while (current != null) {
                put(current.key, current.value);
                current = current.next;
            }
        }
    }

    // =============================================
    // STATISTICS
    // =============================================

    public int getCollisionCount() {
        return collisions;
    }

    public int getCapacity() {
        return capacity;
    }

    public double getCurrentLoadFactor() {
        return (double) size / capacity;
    }

    public int getMaxChainLength() {
        int max = 0;
        for (Entry<K, V> entry : table) {
            int count = 0;
            Entry<K, V> current = entry;
            while (current != null) {
                count++;
                current = current.next;
            }
            if (count > max) {
                max = count;
            }
        }
        return max;
    }

    public String getStats() {
        return String.format(
                "MyHashMap Stats:\n" +
                        "  Size: %d\n" +
                        "  Capacity: %d\n" +
                        "  Load Factor: %.2f\n" +
                        "  Collisions: %d\n" +
                        "  Max Chain Length: %d",
                size, capacity, getCurrentLoadFactor(), collisions, getMaxChainLength()
        );
    }

    // =============================================
    // TOSTRING
    // =============================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Entry<K, V> entry : table) {
            Entry<K, V> current = entry;
            while (current != null) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(current.key).append("=").append(current.value);
                first = false;
                current = current.next;
            }
        }
        sb.append("}");
        return sb.toString();
    }
}