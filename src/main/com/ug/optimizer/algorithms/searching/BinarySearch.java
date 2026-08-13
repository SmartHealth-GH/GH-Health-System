package main.com.ug.optimizer.algorithms.searching;

import main.com.ug.optimizer.model.ServiceRequest;

/**
 * Binary Search Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Given a SORTED array and a target, find the index of target
 * INPUT: Sorted array of elements, target element
 * OUTPUT: Index of target, or -1 if not found
 *
 * PRECONDITIONS (CRITICAL!):
 *   - Array MUST BE SORTED in ascending order
 *   - Array cannot be null
 *   - Target cannot be null
 *   - Elements must be Comparable
 *
 * POSTCONDITIONS:
 *   - Returns index between 0 and n-1 if found
 *   - Returns -1 if not found
 *   - Array remains unchanged
 *
 * EDGE CASES:
 *   - Empty array → returns -1
 *   - Single element array → works correctly
 *   - Target smaller than all elements → returns -1
 *   - Target larger than all elements → returns -1
 *   - Duplicate elements → returns any occurrence
 *
 * =============================================
 * EFFICIENCY ANALYSIS
 * =============================================
 *
 * PRIMITIVE OPERATIONS:
 *   1. Array access: O(1)
 *   2. Comparison: O(1)
 *   3. Mid calculation: O(1)
 *   4. Assignment: O(1)
 *
 * BEST CASE: O(1) - target found at middle
 * AVERAGE CASE: O(log n) - target found after ~log n steps
 * WORST CASE: O(log n) - target not found
 *
 * =============================================
 * ASYMPTOTIC NOTATION
 * =============================================
 *
 * Big-O: O(log n) - Upper bound
 * Big-Θ: Θ(log n) - Tight bound
 * Big-Ω: Ω(1) - Lower bound (best case)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class BinarySearch {

    /**
     * Iterative binary search
     *
     * PRECONDITION: array MUST BE SORTED!
     *
     * @param array The sorted array to search in
     * @param target The element to search for
     * @return The index of the target, or -1 if not found
     */
    public static <T extends Comparable<T>> int search(T[] array, T target) {
        // PRECONDITIONS
        if (array == null || target == null) {
            return -1;  // Edge case
        }

        // EDGE CASE: Check if array is sorted (for demo purposes)
        // In production, this would be O(n) so we don't do it here

        int low = 0;
        int high = array.length - 1;

        while (low <= high) {
            // Primitive operation: mid calculation
            int mid = low + (high - low) / 2;

            // Primitive operation: comparison
            int cmp = target.compareTo(array[mid]);

            if (cmp < 0) {
                high = mid - 1;
            } else if (cmp > 0) {
                low = mid + 1;
            } else {
                return mid;  // Found!
            }
        }
        return -1;  // Not found
    }

    /**
     * Recursive binary search
     */
    public static <T extends Comparable<T>> int searchRecursive(T[] array, T target, int low, int high) {
        if (array == null || target == null || low > high) {
            return -1;
        }

        int mid = low + (high - low) / 2;
        int cmp = target.compareTo(array[mid]);

        if (cmp < 0) {
            return searchRecursive(array, target, low, mid - 1);
        } else if (cmp > 0) {
            return searchRecursive(array, target, mid + 1, high);
        } else {
            return mid;
        }
    }

    /**
     * Search ServiceRequest by ID (requires sorted by ID)
     */
    public static ServiceRequest searchById(ServiceRequest[] requests, int id) {
        if (requests == null) {
            return null;
        }

        int low = 0;
        int high = requests.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;

            if (requests[mid] == null) {
                return null;
            }

            if (requests[mid].getRequestId() == id) {
                return requests[mid];
            } else if (requests[mid].getRequestId() < id) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    /**
     * For EVIDENCE: Trace Table Generation
     */
    public static String generateTrace(int[] array, int target) {
        StringBuilder trace = new StringBuilder();
        trace.append("Binary Search Trace for target = ").append(target).append("\n");
        trace.append("Array: ");
        for (int n : array) trace.append(n).append(" ");
        trace.append("\n");
        trace.append("-".repeat(40)).append("\n");
        trace.append("Step | Low | High | Mid | arr[mid] | Action\n");
        trace.append("-".repeat(40)).append("\n");

        int low = 0;
        int high = array.length - 1;
        int step = 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            String action;
            if (array[mid] == target) {
                action = "FOUND!";
            } else if (array[mid] < target) {
                action = "Search right";
            } else {
                action = "Search left";
            }
            trace.append(String.format(" %2d   | %3d | %3d | %3d | %7d | %s\n",
                    step, low, high, mid, array[mid], action));
            step++;

            if (array[mid] == target) {
                break;
            } else if (array[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return trace.toString();
    }
}