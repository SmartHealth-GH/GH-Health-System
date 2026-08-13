package main.com.ug.optimizer.datastructures;

import main.com.ug.optimizer.datastructures.MyHashMap;

/**
 * Custom Map wrapper for MyHashMap
 *
 * @param <K> The type of keys
 * @param <V> The type of values
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MyMap<K, V> {

    private MyHashMap<K, V> map;

    public MyMap() {
        this.map = new MyHashMap<>();
    }

    public MyMap(int initialCapacity) {
        this.map = new MyHashMap<>(initialCapacity);
    }

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V get(K key) {
        return map.get(key);
    }

    public V remove(K key) {
        return map.remove(key);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}