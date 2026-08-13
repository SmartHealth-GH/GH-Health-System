package main.com.ug.optimizer.algorithms.searching;

import main.com.ug.optimizer.model.ServiceRequest;

/**
 * Linear Search Algorithm
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Given an array and a target, find the index of target
 * INPUT: Array of elements, target element
 * OUTPUT: Index of target, or -1 if not found
 *
 * PRECONDITIONS:
 *   - Array cannot be null
 *   - Target cannot be null
 *
 * POSTCONDITIONS:
 *   - Returns index between 0 and n-1 if found
 *   - Returns -1 if not found
 *   - Array remains unchanged
 *
 * EDGE CASES:
 *   - Empty array → returns -1
 *   - Target at first position → returns 0
 *   - Target at last position → returns n-1
 *   - Target not in array → returns -1
 *   - Duplicate elements → returns first occurrence
 *
 * =============================================
 * EFFICIENCY ANALYSIS
 * =============================================
 *
 * PRIMITIVE OPERATIONS:
 *   1. Array access: O(1)
 *   2. Comparison: O(1)
 *   3. Increment: O(1)
 *
 * BEST CASE: O(1) - target found at first position
 * AVERAGE CASE: O(n) - target found in middle
 * WORST CASE: O(n) - target not found or at last position
 *
 * =============================================
 * ASYMPTOTIC NOTATION
 * =============================================
 *
 * Big-O: O(n) - Upper bound (worst case)
 * Big-Θ: Θ(n) - Tight bound
 * Big-Ω: Ω(1) - Lower bound (best case)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class LinearSearch {

    /**
     * Searches for a target in an array using linear search
     *
     * @param <T> The type of elements
     * @param array The array to search in
     * @param target The element to search for
     * @return The index of the target, or -1 if not found
     */
    public static <T> int search(T[] array, T target) {
        // PRECONDITIONS
        if (array == null || target == null) {
            return -1;  // Edge case: null input
        }

        // Core algorithm
        for (int i = 0; i < array.length; i++) {
            // Primitive operation: comparison + array access
            if (target.equals(array[i])) {
                return i;
            }
        }
        return -1;  // Edge case: target not found
    }

    /**
     * Searches for a target in an int array
     */
    public static int search(int[] array, int target) {
        if (array == null) {
            return -1;
        }

        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Searches for a ServiceRequest by ID
     */
    public static ServiceRequest searchById(ServiceRequest[] requests, int id) {
        if (requests == null) {
            return null;
        }

        for (ServiceRequest request : requests) {
            if (request != null && request.getRequestId() == id) {
                return request;
            }
        }
        return null;
    }
}