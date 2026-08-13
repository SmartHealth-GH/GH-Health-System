package main.com.ug.optimizer.algorithms.sorting;

import main.com.ug.optimizer.model.ServiceRequest;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Merge Sort Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Sort an array in ascending order
 * INPUT: Array of comparable elements
 * OUTPUT: Sorted array (returns new array, or modifies in-place with helper)
 *
 * HOW IT WORKS (Divide and Conquer):
 * 1. DIVIDE: Split the array into two halves
 * 2. CONQUER: Recursively sort each half
 * 3. COMBINE: Merge the two sorted halves
 *
 * =============================================
 * RECURRENCE & DECOMPOSITION NOTES
 * =============================================
 *
 * RECURRENCE RELATION: T(n) = 2T(n/2) + O(n)
 *   - 2T(n/2): Two recursive calls on halves
 *   - O(n): Merging the two halves
 *
 * DECOMPOSITION:
 *   - Base case: n <= 1 (already sorted)
 *   - Recursive case: split, sort halves, merge
 *
 * BEST CASE: O(n log n)
 * AVERAGE CASE: O(n log n)
 * WORST CASE: O(n log n)
 * SPACE COMPLEXITY: O(n) - needs extra array for merging
 *
 * STABILITY: YES - maintains relative order of equal elements
 * IN-PLACE: NO - requires O(n) extra space
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class MergeSort {

    /**
     * Sorts an array using Merge Sort (modifies original array)
     *
     * @param <T> The type of elements (must extend Comparable)
     * @param array The array to sort (modified in-place)
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        mergeSort(array, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void mergeSort(T[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Comparable<T>> void merge(T[] array, int left, int mid, int right) {
        // Create temporary arrays using the same type as the original
        T[] leftArray = (T[]) new Comparable[mid - left + 1];
        T[] rightArray = (T[]) new Comparable[right - mid];

        // Copy data to temp arrays
        for (int i = 0; i < leftArray.length; i++) {
            leftArray[i] = array[left + i];
        }
        for (int i = 0; i < rightArray.length; i++) {
            rightArray[i] = array[mid + 1 + i];
        }

        // Merge the two arrays
        int i = 0, j = 0, k = left;
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i].compareTo(rightArray[j]) <= 0) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }

        // Copy remaining elements
        while (i < leftArray.length) {
            array[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            array[k++] = rightArray[j++];
        }
    }

    /**
     * Sorts an int array using Merge Sort (returns new sorted array)
     */
    public static int[] sort(int[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }

        int[] result = array.clone();
        mergeSort(result, 0, result.length - 1);
        return result;
    }

    private static void mergeSort(int[] array, int left, int right) {
        if (left >= right) {
            return;
        }

        int mid = left + (right - left) / 2;
        mergeSort(array, left, mid);
        mergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }

    private static void merge(int[] array, int left, int mid, int right) {
        int[] leftArray = new int[mid - left + 1];
        int[] rightArray = new int[right - mid];

        System.arraycopy(array, left, leftArray, 0, leftArray.length);
        System.arraycopy(array, mid + 1, rightArray, 0, rightArray.length);

        int i = 0, j = 0, k = left;
        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }

        while (i < leftArray.length) {
            array[k++] = leftArray[i++];
        }
        while (j < rightArray.length) {
            array[k++] = rightArray[j++];
        }
    }

    /**
     * For EVIDENCE: Trace Table Generation
     */
    public static String generateTrace(int[] array) {
        StringBuilder trace = new StringBuilder();
        trace.append("Merge Sort Trace\n");
        trace.append("Initial: ");
        for (int n : array) trace.append(n).append(" ");
        trace.append("\n");
        trace.append("-".repeat(50)).append("\n");

        int[] arr = array.clone();
        mergeSortTrace(arr, 0, arr.length - 1, trace, 0);
        return trace.toString();
    }

    private static void mergeSortTrace(int[] array, int left, int right, StringBuilder trace, int depth) {
        if (left >= right) {
            trace.append("  ".repeat(depth)).append("Base: [").append(array[left]).append("]\n");
            return;
        }

        int mid = left + (right - left) / 2;
        trace.append("  ".repeat(depth)).append("Split: ").append(left).append("-").append(mid).append(" | ").append(mid + 1).append("-").append(right).append("\n");

        mergeSortTrace(array, left, mid, trace, depth + 1);
        mergeSortTrace(array, mid + 1, right, trace, depth + 1);

        // Show merge step
        trace.append("  ".repeat(depth)).append("Merge: ");
        for (int i = left; i <= right; i++) {
            trace.append(array[i]).append(" ");
        }
        trace.append("\n");
    }
}