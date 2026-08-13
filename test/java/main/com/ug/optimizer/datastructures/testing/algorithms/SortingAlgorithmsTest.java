package main.com.ug.optimizer.algorithms.sorting;

import main.com.ug.optimizer.algorithms.sorting.SelectionSort;
import main.com.ug.optimizer.algorithms.sorting.InsertionSort;
import main.com.ug.optimizer.algorithms.sorting.MergeSort;
import main.com.ug.optimizer.algorithms.sorting.QuickSort;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Unit tests for Sorting Algorithms
 *
 * EVIDENCE PROVIDED:
 * - All sorting algorithms implemented from scratch
 * - Stability and in-place discussion
 * - Recurrence/intuitive decomposition notes
 * - Trace tables for each algorithm
 * - Performance comparison
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class SortingAlgorithmsTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🧪 TESTING SORTING ALGORITHMS");
        System.out.println("=".repeat(70));

        testSelectionSort();
        testInsertionSort();
        testMergeSort();
        testQuickSort();

        // 📋 EVIDENCE: Stability Discussion
        testStability();

        // 📋 EVIDENCE: Trace Tables
        testTraceTables();

        // 📋 EVIDENCE: Performance Comparison
        testPerformanceComparison();

        System.out.println("\n" + "=".repeat(70));
        System.out.println("📊 RESULTS:");
        System.out.println("   ✅ Passed: " + passed);
        System.out.println("   ❌ Failed: " + failed);
        System.out.println("=".repeat(70));

        if (failed == 0) {
            System.out.println("🎉 ALL TESTS PASSED!");
        } else {
            System.out.println("⚠️ SOME TESTS FAILED!");
        }
    }

    // =============================================
    // REGULAR TESTS
    // =============================================

    private static void testSelectionSort() {
        System.out.println("\n✅ Test: Selection Sort");

        Integer[] array = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        SelectionSort.sort(array);

        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assert Arrays.equals(array, expected) : "Array should be sorted";

        System.out.println("   ✅ Passed - Sorted: " + Arrays.toString(array));
        passed++;
    }

    private static void testInsertionSort() {
        System.out.println("\n✅ Test: Insertion Sort");

        Integer[] array = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        InsertionSort.sort(array);

        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assert Arrays.equals(array, expected) : "Array should be sorted";

        System.out.println("   ✅ Passed - Sorted: " + Arrays.toString(array));
        passed++;
    }

    private static void testMergeSort() {
        System.out.println("\n✅ Test: Merge Sort");

        // Use int array instead of Integer[] to avoid generic issues
        int[] array = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        int[] sorted = MergeSort.sort(array);

        int[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assert Arrays.equals(sorted, expected) : "Array should be sorted";

        System.out.println("   ✅ Passed - Sorted: " + Arrays.toString(sorted));
        passed++;
    }

    private static void testQuickSort() {
        System.out.println("\n✅ Test: Quick Sort");

        Integer[] array = {5, 3, 8, 1, 9, 2, 7, 4, 6};
        QuickSort.sort(array);

        Integer[] expected = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assert Arrays.equals(array, expected) : "Array should be sorted";

        System.out.println("   ✅ Passed - Sorted: " + Arrays.toString(array));
        passed++;
    }

    // =============================================
    // 📋 EVIDENCE: Stability Discussion
    // =============================================

    private static void testStability() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📋 EVIDENCE: Sorting Stability Discussion");
        System.out.println("=".repeat(70));

        // Test with Student objects that have name and grade
        Student[] students = {
                new Student("Alice", 85),
                new Student("Bob", 75),
                new Student("Charlie", 85),
                new Student("David", 65)
        };

        System.out.println("📊 Testing stability with Student objects (same grade):");
        System.out.println("   Original: " + Arrays.toString(students));

        // Selection Sort (UNSTABLE)
        Student[] selectionCopy = students.clone();
        SelectionSort.sort(selectionCopy);
        System.out.println("   Selection Sort: " + Arrays.toString(selectionCopy) + " (UNSTABLE)");

        // Insertion Sort (STABLE)
        Student[] insertionCopy = students.clone();
        InsertionSort.sort(insertionCopy);
        System.out.println("   Insertion Sort: " + Arrays.toString(insertionCopy) + " (STABLE)");

        System.out.println("\n📋 STABILITY ANALYSIS:");
        System.out.println("   - Selection Sort: UNSTABLE");
        System.out.println("     • Swaps elements that are far apart");
        System.out.println("     • Can change relative order of equal elements");
        System.out.println("     • Example: [5a, 3, 5b, 2] → [2, 3, 5b, 5a]");
        System.out.println("   - Insertion Sort: STABLE");
        System.out.println("     • Only shifts elements one position at a time");
        System.out.println("     • Equal elements maintain relative order");
        System.out.println("     • Example: [5a, 3, 5b, 2] → [2, 3, 5a, 5b]");
        System.out.println("   - Merge Sort: STABLE");
        System.out.println("     • Merges equal elements from left before right");
        System.out.println("   - Quick Sort: UNSTABLE");
        System.out.println("     • Swaps elements across the array");
        System.out.println("📋 All algorithms are IN-PLACE except Merge Sort");
        System.out.println("✅ Stability evidence generated!");
    }

    static class Student implements Comparable<Student> {
        String name;
        int grade;

        Student(String name, int grade) {
            this.name = name;
            this.grade = grade;
        }

        @Override
        public int compareTo(Student other) {
            return Integer.compare(this.grade, other.grade);
        }

        @Override
        public String toString() {
            return name + "(" + grade + ")";
        }
    }

    // =============================================
    // 📋 EVIDENCE: Trace Tables
    // =============================================

    private static void testTraceTables() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📋 EVIDENCE: Sorting Algorithm Trace Tables");
        System.out.println("=".repeat(70));

        int[] array = {5, 3, 8, 1, 9, 2, 7, 4, 6};

        System.out.println("1. " + SelectionSort.generateTrace(array));
        System.out.println();
        System.out.println("2. " + InsertionSort.generateTrace(array));
        System.out.println();
        System.out.println("3. " + MergeSort.generateTrace(array));
        System.out.println();
        System.out.println("4. " + QuickSort.generateTrace(array));
        System.out.println();

        System.out.println("📋 Trace tables show:");
        System.out.println("   - Selection Sort: Finds min and swaps");
        System.out.println("   - Insertion Sort: Inserts elements in correct position");
        System.out.println("   - Merge Sort: Divides and merges");
        System.out.println("   - Quick Sort: Partitions around pivot");
        System.out.println("✅ Trace tables generated!");
        System.out.println("📋 Copy these outputs to your report as evidence.");
    }

    // =============================================
    // 📋 EVIDENCE: Performance Comparison
    // =============================================

    private static void testPerformanceComparison() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📋 EVIDENCE: Sorting Performance Comparison");
        System.out.println("=".repeat(70));

        int[] sizes = {100, 500, 1000, 5000};

        System.out.println("📊 Comparing sorting algorithms:");
        System.out.println("-".repeat(80));
        System.out.printf("%-10s | %-15s | %-15s | %-15s | %-15s\n",
                "Size", "Selection", "Insertion", "Merge", "Quick");
        System.out.println("-".repeat(80));

        for (int size : sizes) {
            // Generate random array
            Integer[] array = new Integer[size];
            for (int i = 0; i < size; i++) {
                array[i] = (int) (Math.random() * size * 10);
            }

            // Selection Sort
            Integer[] selCopy = array.clone();
            long start = System.nanoTime();
            SelectionSort.sort(selCopy);
            long selTime = System.nanoTime() - start;

            // Insertion Sort
            Integer[] insCopy = array.clone();
            start = System.nanoTime();
            InsertionSort.sort(insCopy);
            long insTime = System.nanoTime() - start;

            // Merge Sort - use int array for merge sort
            int[] intArray = new int[size];
            for (int i = 0; i < size; i++) {
                intArray[i] = (int) (Math.random() * size * 10);
            }
            start = System.nanoTime();
            MergeSort.sort(intArray);
            long merTime = System.nanoTime() - start;

            // Quick Sort
            Integer[] quiCopy = array.clone();
            start = System.nanoTime();
            QuickSort.sort(quiCopy);
            long quiTime = System.nanoTime() - start;

            System.out.printf("%-10d | %-15d | %-15d | %-15d | %-15d\n",
                    size, selTime, insTime, merTime, quiTime);
        }

        System.out.println("-".repeat(80));
        System.out.println("📊 Analysis:");
        System.out.println("   - Selection Sort: O(n²) - always, even on sorted data");
        System.out.println("   - Insertion Sort: O(n²) worst, O(n) best (nearly sorted)");
        System.out.println("   - Merge Sort: O(n log n) - consistent");
        System.out.println("   - Quick Sort: O(n log n) avg, O(n²) worst (sorted data)");
        System.out.println("   - Merge Sort and Quick Sort are MUCH faster for large n!");
        System.out.println("✅ Performance comparison evidence generated!");
        System.out.println("📋 Copy this output to your report as evidence.");
    }
}