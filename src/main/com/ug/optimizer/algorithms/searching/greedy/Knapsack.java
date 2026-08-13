package main.com.ug.optimizer.algorithms.dynamicprogramming;

import main.com.ug.optimizer.model.ServiceRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Dynamic Programming - 0/1 Knapsack Problem
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Given items with weights and values, maximize total value
 *          without exceeding capacity
 * INPUT: Weights[], Values[], Capacity
 * OUTPUT: Maximum total value, selected items
 *
 * HOW IT WORKS (DYNAMIC PROGRAMMING):
 * 1. Create DP table: dp[i][w] = max value using first i items, capacity w
 * 2. For each item, either include or exclude it
 * 3. Recurrence: dp[i][w] = max(dp[i-1][w], dp[i-1][w-weight[i]] + value[i])
 * 4. Reconstruct selected items from DP table
 *
 * =============================================
 * DYNAMIC PROGRAMMING PROPERTIES
 * =============================================
 *
 * OPTIMAL SUBSTRUCTURE:
 *   - Optimal solution for capacity W uses optimal solutions for subproblems
 *
 * OVERLAPPING SUBPROBLEMS:
 *   - Same subproblems are solved multiple times
 *   - Memoisation/tabulation avoids recomputation
 *
 * TIME COMPLEXITY: O(n * W)
 * SPACE COMPLEXITY: O(n * W)
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class Knapsack {

    /**
     * Solve 0/1 Knapsack using DP (Tabulation)
     *
     * @param weights Array of item weights
     * @param values Array of item values
     * @param capacity Maximum capacity
     * @return Result containing max value and selected items
     */
    public static KnapsackResult solve(int[] weights, int[] values, int capacity) {
        if (weights == null || values == null || weights.length == 0 || capacity <= 0) {
            return new KnapsackResult(0, new int[0]);
        }

        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        // Fill DP table
        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(
                            dp[i - 1][w],
                            dp[i - 1][w - weights[i - 1]] + values[i - 1]
                    );
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Reconstruct selected items
        int[] selected = reconstruct(dp, weights, capacity);
        return new KnapsackResult(dp[n][capacity], selected);
    }

    /**
     * Reconstruct selected items from DP table
     */
    private static int[] reconstruct(int[][] dp, int[] weights, int capacity) {
        int n = weights.length;
        int w = capacity;
        boolean[] selected = new boolean[n];
        int selectedCount = 0;

        for (int i = n; i > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected[i - 1] = true;
                w -= weights[i - 1];
                selectedCount++;
            }
        }

        int[] result = new int[selectedCount];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            if (selected[i]) {
                result[idx++] = i;
            }
        }
        return result;
    }

    /**
     * Generate DP table for evidence
     */
    public static String generateDPTable(int[] weights, int[] values, int capacity) {
        StringBuilder table = new StringBuilder();
        table.append("KNAPSACK DP TABLE\n");
        table.append("=".repeat(60)).append("\n");
        table.append("Items: ").append(weights.length).append("\n");
        table.append("Capacity: ").append(capacity).append("\n\n");

        // Show items
        table.append("Items:\n");
        for (int i = 0; i < weights.length; i++) {
            table.append("  Item ").append(i).append(": weight=")
                    .append(weights[i]).append(", value=").append(values[i]).append("\n");
        }
        table.append("\n");

        // Build DP table
        int n = weights.length;
        int[][] dp = new int[n + 1][capacity + 1];

        for (int i = 1; i <= n; i++) {
            for (int w = 0; w <= capacity; w++) {
                if (weights[i - 1] <= w) {
                    dp[i][w] = Math.max(dp[i - 1][w], dp[i - 1][w - weights[i - 1]] + values[i - 1]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Print table
        table.append("DP Table (rows=items, cols=capacity):\n");
        table.append("    ");
        for (int w = 0; w <= capacity; w++) {
            table.append(String.format("%3d ", w));
        }
        table.append("\n");
        table.append("   ");
        for (int w = 0; w <= capacity; w++) {
            table.append("----");
        }
        table.append("\n");

        for (int i = 0; i <= n; i++) {
            table.append(String.format("%2d |", i));
            for (int w = 0; w <= capacity; w++) {
                table.append(String.format("%3d ", dp[i][w]));
            }
            table.append("\n");
        }

        table.append("\n");
        table.append("Max Value: ").append(dp[n][capacity]).append("\n");

        // Show selected items
        KnapsackResult result = solve(weights, values, capacity);
        table.append("Selected Items: ");
        if (result.selected.length == 0) {
            table.append("none");
        } else {
            for (int idx : result.selected) {
                table.append(idx).append(" ");
            }
        }
        table.append("\n");

        return table.toString();
    }

    /**
     * Solve Knapsack with ServiceRequests (weight=urgency, value=priority)
     */
    public static KnapsackResult solveWithRequests(java.util.List<ServiceRequest> requests, int capacity) {
        if (requests == null || requests.isEmpty()) {
            return new KnapsackResult(0, new int[0]);
        }

        int n = requests.size();
        int[] weights = new int[n];
        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            weights[i] = requests.get(i).getUrgency().getValue();
            values[i] = requests.get(i).getUrgency().getValue() * 10;  // Value = urgency * 10
        }

        return solve(weights, values, capacity);
    }

    // =============================================
    // INNER CLASSES
    // =============================================

    public static class KnapsackResult {
        public int maxValue;
        public int[] selected;  // indices of selected items

        KnapsackResult(int maxValue, int[] selected) {
            this.maxValue = maxValue;
            this.selected = selected;
        }
    }
}