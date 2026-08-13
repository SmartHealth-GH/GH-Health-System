package main.com.ug.optimizer.algorithms.sorting;

import main.com.ug.optimizer.model.ServiceRequest;

/**
 * Insertion Sort Algorithm
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
 * 1. Start with the second element
 * 2. Compare it with elements before it
 * 3. Shift larger elements one position right
 * 4. Insert the element in the correct position
 * 5. Repeat for all elements
 *
 * =============================================
 * STABILITY & IN-PLACE DISCUSSION
 * =============================================
 *
 * IN-PLACE: YES
 *   - Uses only O(1) extra space
 *   - Modifies the original array directly
 *
 * STABILITY: YES
 *   - Only shifts elements one position at a time
 *   - Equal elements maintain their relative order
 *   - Example: [5a, 3, 5b, 2] → [2, 3, 5a, 5b]
 *   - The two 5's maintain their original order
 *
 * BEST CASE: O(n) - when array is already sorted
 * AVERAGE CASE: O(n²)
 * WORST CASE: O(n²) - when array is reverse sorted
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class InsertionSort {

    /**
     * Sorts an array using Insertion Sort
     *
     * @param <T> The type of elements (must extend Comparable)
     * @param array The array to sort (modified in-place)
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        for (int i = 1; i < n; i++) {
            T key = array[i];
            int j = i - 1;

            // Shift elements greater than key to the right
            while (j >= 0 && array[j].compareTo(key) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            // Insert key in the correct position
            array[j + 1] = key;
        }
    }

    /**
     * Sorts an int array using Insertion Sort
     */
    public static void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }

        int n = array.length;

        for (int i = 1; i < n; i++) {
            int key = array[i];
            int j = i - 1;

            while (j >= 0 && array[j] > key) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = key;
        }
    }

    /**
     * For EVIDENCE: Trace Table Generation
     */
    public static String generateTrace(int[] array) {
        StringBuilder trace = new StringBuilder();
        trace.append("Insertion Sort Trace\n");
        trace.append("Initial: ");
        for (int n : array) trace.append(n).append(" ");
        trace.append("\n");
        trace.append("-".repeat(50)).append("\n");

        int[] arr = array.clone();
        int n = arr.length;

        for (int i = 1; i < n; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
            trace.append("Step ").append(i).append(": ");
            for (int val : arr) trace.append(val).append(" ");
            trace.append("(inserted ").append(key).append(")\n");
        }
        return trace.toString();
    }
}