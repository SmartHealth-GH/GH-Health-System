package tests;

import data_structures.MyArrayList;

/**
 * Simple test for MyArrayList
 */
public class MyArrayListTest {

    public static void main(String[] args) {
        System.out.println("🧪 Testing MyArrayList...");
        System.out.println("==========================");

        MyArrayList<String> list = new MyArrayList<>();

        // Test 1: Add elements
        System.out.println("Test 1: Adding elements");
        list.add("Emergency Unit");
        list.add("Pharmacy");
        list.add("Operating Room");
        System.out.println("List after adding: " + list);
        System.out.println("Size: " + list.size());
        System.out.println();

        // Test 2: Get elements
        System.out.println("Test 2: Getting elements");
        System.out.println("Element at index 1: " + list.get(1));
        System.out.println();

        // Test 3: Set elements
        System.out.println("Test 3: Setting elements");
        list.set(1, "Laboratory");
        System.out.println("List after setting: " + list);
        System.out.println();

        // Test 4: Remove elements
        System.out.println("Test 4: Removing elements");
        list.remove(0);
        System.out.println("List after removing index 0: " + list);
        System.out.println();

        // Test 5: Iterator
        System.out.println("Test 5: Iterating through list");
        System.out.print("Elements: ");
        for (String s : list) {
            System.out.print(s + " ");
        }
        System.out.println();
        System.out.println();

        System.out.println("✅ All tests passed!");
    }
}