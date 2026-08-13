package main.com.ug.optimizer.algorithms.sorting;

import main.com.ug.optimizer.model.ServiceRequest;

/**
 * Quick Sort Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Sort an array in ascending order
 * INPUT: Array of comparable elements
 * OUTPUT: Sorted array (in-place)
 *
 * HOW IT WORKS (Divide and Conquer):
 * 1. Choose a pivot element
 * 2. PARTITION: Rearrange array so pivot is in correct position
 * 3. RECURSE: Sort left and right subarrays
 *
 * =============================================
 * RECURRENCE & DECOMPOSITION NOTES
 * =============================================
 *
 * RECURRENCE RELATION: T(n) = T(k) + T(n-k-1) + O(n)
 *   - k: elements less than pivot
 *   - n-k-1: elements greater than pivot
 *   - O(n): partitioning
 *
 * DECOMPOSITION:
 *   - Base case: n <= 1 (already sorted)
 *   - Choose pivot, partition, recurse on both sides
 *
 * BEST CASE: O(n log n) - when pivot splits array evenly
 * AVERAGE CASE: O(n log n)
 * WORST CASE: O(n²) - when pivot is min or max (sorted array)
 * SPACE COMPLEXITY: O(log n) - recursion stack
 *
 * STABILITY: NO - swaps elements across the array
 * IN-PLACE: YES - modifies original array directly
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class QuickSort {

    /**
     * Sorts an array using Quick Sort
     *
     * @param <T> The type of elements (must extend Comparable)
     * @param array The array to sort (modified in-place)
     */
    public static <T extends Comparable<T>> void sort(T[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private static <T extends Comparable<T>> void quickSort(T[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private static <T extends Comparable<T>> int partition(T[] array, int low, int high) {
        T pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j].compareTo(pivot) <= 0) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Sorts an int array using Quick Sort
     */
    public static void sort(int[] array) {
        if (array == null || array.length <= 1) {
            return;
        }
        quickSort(array, 0, array.length - 1);
    }

    private static void quickSort(int[] array, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(array, low, high);
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private static int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }

    /**
     * For EVIDENCE: Trace Table Generation
     */
    public static String generateTrace(int[] array) {
        StringBuilder trace = new StringBuilder();
        trace.append("Quick Sort Trace\n");
        trace.append("Initial: ");
        for (int n : array) trace.append(n).append(" ");
        trace.append("\n");
        trace.append("-".repeat(50)).append("\n");

        int[] arr = array.clone();
        quickSortTrace(arr, 0, arr.length - 1, trace, 0);
        return trace.toString();
    }

    private static void quickSortTrace(int[] array, int low, int high, StringBuilder trace, int depth) {
        if (low >= high) {
            if (low == high) {
                trace.append("  ".repeat(depth)).append("Base: [").append(array[low]).append("]\n");
            }
            return;
        }

        trace.append("  ".repeat(depth)).append("Pivot: ").append(array[high]).append(" (index ").append(high).append(")\n");
        int pivotIndex = partitionTrace(array, low, high, trace, depth);
        trace.append("  ".repeat(depth)).append("After partition: ");
        for (int i = low; i <= high; i++) {
            trace.append(array[i]).append(" ");
        }
        trace.append(" | Pivot at index ").append(pivotIndex).append("\n");

        quickSortTrace(array, low, pivotIndex - 1, trace, depth + 1);
        quickSortTrace(array, pivotIndex + 1, high, trace, depth + 1);
    }

    private static int partitionTrace(int[] array, int low, int high, StringBuilder trace, int depth) {
        int pivot = array[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (array[j] <= pivot) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;
        return i + 1;
    }
}