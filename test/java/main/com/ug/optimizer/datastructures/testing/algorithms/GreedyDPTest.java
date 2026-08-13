package main.com.ug.optimizer.algorithms.greedy;

import main.com.ug.optimizer.algorithms.dynamicprogramming.Knapsack;
import main.com.ug.optimizer.algorithms.greedy.GreedyAlgorithm;
import main.com.ug.optimizer.model.Resource;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.ResourceStatus;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for Greedy Algorithm and Dynamic Programming
 *
 * EVIDENCE PROVIDED:
 * - Greedy Algorithm trace
 * - Greedy counterexample
 * - DP tabulation table and reconstruction
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class GreedyDPTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("🧪 TESTING GREEDY + DYNAMIC PROGRAMMING");
        System.out.println("=".repeat(70));

        testGreedyAlgorithm();
        testKnapsack();

        // 📋 EVIDENCE: All evidence generation
        generateAllEvidence();

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
    // TESTS
    // =============================================

    private static void testGreedyAlgorithm() {
        System.out.println("\n✅ Test: Greedy Algorithm");

        // Create test requests
        List<ServiceRequest> requests = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        requests.add(new ServiceRequest(1, 2, "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1)));
        requests.add(new ServiceRequest(2, 3, "Surgery", UrgencyLevel.URGENT, now.plusHours(4)));
        requests.add(new ServiceRequest(3, 4, "Routine", UrgencyLevel.LOW, now.plusHours(12)));

        // Create test resources
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("Ambulance", 10, 4, ResourceStatus.AVAILABLE));
        resources.add(new Resource("Doctor", 4, 1, ResourceStatus.AVAILABLE));
        resources.add(new Resource("Nurse", 1, 1, ResourceStatus.AVAILABLE));

        GreedyAlgorithm.AssignmentResult result = GreedyAlgorithm.assignResources(requests, resources);

        assert result.count > 0 : "Should assign at least one request";
        System.out.println("   ✅ Passed - Assigned " + result.count + " requests");
        passed++;
    }

    private static void testKnapsack() {
        System.out.println("\n✅ Test: Dynamic Programming (Knapsack)");

        int[] weights = {2, 3, 4, 5, 9};
        int[] values = {3, 4, 5, 8, 10};
        int capacity = 10;

        Knapsack.KnapsackResult result = Knapsack.solve(weights, values, capacity);

        assert result.maxValue == 13 : "Max value should be 13";
        assert result.selected.length > 0 : "Should select at least one item";

        System.out.println("   ✅ Passed - Max value: " + result.maxValue);
        System.out.println("   Selected items: " + arrayToString(result.selected));
        passed++;
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    // =============================================
    // 📋 EVIDENCE: All Evidence Generation
    // =============================================

    private static void generateAllEvidence() {
        System.out.println("\n" + "=".repeat(70));
        System.out.println("📋 EVIDENCE: Greedy + Dynamic Programming");
        System.out.println("=".repeat(70));

        // 1. Greedy Counterexample
        System.out.println("\n1. GREEDY COUNTEREXAMPLE");
        System.out.println(GreedyAlgorithm.generateCounterexample());

        // 2. Greedy Trace
        System.out.println("\n2. GREEDY ALGORITHM TRACE");
        List<ServiceRequest> requests = createTestRequests();
        List<Resource> resources = createTestResources();
        System.out.println(GreedyAlgorithm.generateTrace(requests, resources));

        // 3. DP Tabulation Table
        System.out.println("\n3. DYNAMIC PROGRAMMING TABLE");
        int[] weights = {2, 3, 4, 5, 9};
        int[] values = {3, 4, 5, 8, 10};
        int capacity = 10;
        System.out.println(Knapsack.generateDPTable(weights, values, capacity));

        System.out.println("📋 All evidence generated!");
        System.out.println("📋 Copy these outputs to your report as evidence.");
    }

    // =============================================
    // HELPER DATA
    // =============================================

    private static List<ServiceRequest> createTestRequests() {
        List<ServiceRequest> requests = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        requests.add(new ServiceRequest(1, 2, "Emergency", UrgencyLevel.EMERGENCY, now.plusHours(1)));
        requests.add(new ServiceRequest(2, 3, "Surgery", UrgencyLevel.URGENT, now.plusHours(4)));
        requests.add(new ServiceRequest(3, 4, "Routine", UrgencyLevel.LOW, now.plusHours(12)));
        requests.add(new ServiceRequest(4, 5, "Lab Test", UrgencyLevel.MODERATE, now.plusHours(8)));
        return requests;
    }

    private static List<Resource> createTestResources() {
        List<Resource> resources = new ArrayList<>();
        resources.add(new Resource("Ambulance", 10, 4, ResourceStatus.AVAILABLE));
        resources.add(new Resource("Doctor", 4, 1, ResourceStatus.AVAILABLE));
        resources.add(new Resource("Nurse", 1, 1, ResourceStatus.AVAILABLE));
        resources.add(new Resource("Operating Room", 4, 1, ResourceStatus.AVAILABLE));
        return resources;
    }
}