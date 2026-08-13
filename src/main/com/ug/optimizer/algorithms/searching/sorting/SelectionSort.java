package main.com.ug.optimizer.algorithms.sorting;

import main.com.ug.optimizer.model.ServiceRequest;

/**
 * Selection Sort Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Sort an array in ascending order
 * INPUT: Array of comparable elements
 * OUTPUT: Sorted array (in-place)
 *
 * HOW IT WORKS:
 * 1. Find the minimum element in the unsorted portion
 * 2. Swap it with the first element of the unsorted portion
 * 3. Move the boundary of the sorted portion one step right
 * 4. Repeat until entire array is sorted
 *
 * =============================================
 * STABILITY & IN-PLACE DISCUSSION
 * =============================================
 *
 * IN-PLACE: YES
 *   - Uses only O(1) extra space (only a few variables)
 *   - Modifies the original array directly
 *
 * STABILITY: NO
 *   - Swaps elements that are far apart
 *   - Can change the relative order of equal elements
 *   - Example: [5a, 3, 5b, 2] → [2, 3, 5b, 5a]
 *   - The two 5's swapped order
 *
 * BEST CASE: O(n²) - even if array is already sorted
 * AVERAGE CASE: O(n²)
 * WORST CASE: O(n²)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class SelectionSort {

    /**
     * Sorts an array using Selection Sort
     *
     * @param <T> The type of elements (must extend Comparable)
     * @param array The array to sort (modified in-place)
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted portion
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j].compareTo(array[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            // Swap the found minimum with the first element of unsorted portion
            if (minIndex != i) {
                swap(array, i, minIndex);
            }
        }
    }

    /**
     * Sorts an int array using Selection Sort
     */
    public static void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (array[j] < array[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
        }
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * For EVIDENCE: Trace Table Generation
     */
    public static String generateTrace(int[] array) {
        StringBuilder trace = new StringBuilder();
        trace.append("Selection Sort Trace\n");
        trace.append("Initial: ");
        for (int n : array) trace.append(n).append(" ");
        trace.append("\n");
        trace.append("-".repeat(50)).append("\n");

        int n = array.length;
        int[] arr = array.clone();

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[minIndex]) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = arr[i];
                arr[i] = arr[minIndex];
                arr[minIndex] = temp;
            }
            trace.append("Step ").append(i + 1).append(": ");
            for (int val : arr) trace.append(val).append(" ");
            trace.append("(sorted: 0-").append(i).append(")\n");
        }
        return trace.toString();
    }
}