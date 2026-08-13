package main.com.ug.optimizer.algorithms.greedy;

import main.com.ug.optimizer.datastructures.MyPriorityQueue;
import main.com.ug.optimizer.model.Resource;
import main.com.ug.optimizer.model.ServiceRequest;
import main.com.ug.optimizer.model.enums.UrgencyLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Greedy Algorithm - Priority-Based Resource Assignment
 *
 * =============================================
 * FOUNDATIONS OF ALGORITHMS
 * =============================================
 *
 * PROBLEM: Assign resources to service requests based on priority
 * INPUT: List of ServiceRequests, List of Resources
 * OUTPUT: Assignment of resources to requests
 *
 * HOW IT WORKS (GREEDY):
 * 1. Sort requests by urgency (highest first) - Greedy choice
 * 2. For each request, assign the best available resource
 * 3. Greedy choice: Always process the most urgent request first
 *
 * =============================================
 * GREEDY CHOICE PROPERTY
 * =============================================
 *
 * Greedy Choice: Process the most urgent request first
 *
 * Justification:
 * - Urgent requests need immediate attention
 * - Delaying urgent requests could have serious consequences
 * - This is optimal for patient care (hospital context)
 *
 * =============================================
 * COUNTEREXAMPLE (Why Greedy Fails)
 * =============================================
 *
 * Greedy chooses: Most urgent first
 *
 * Counterexample:
 * - Request A: Urgency=5, Requires 3 resources
 * - Request B: Urgency=4, Requires 1 resource
 * - Request C: Urgency=4, Requires 1 resource
 * - Available resources: 2
 *
 * Greedy: Assigns 2 resources to A → B and C get NOTHING!
 * Optimal: Assign 1 resource to A, 1 to B, 1 to C
 *
 * Greedy fails because it doesn't consider resource constraints!
 *
 * @author GH-Health-System Team
 * @version 1.0
 */
public class GreedyAlgorithm {

    /**
     * Assign resources to requests using greedy approach
     *
     * @param requests List of service requests
     * @param resources List of available resources
     * @return Assignment map: requestId → resourceId
     */
    public static AssignmentResult assignResources(List<ServiceRequest> requests, List<Resource> resources) {
        if (requests == null || resources == null || requests.isEmpty() || resources.isEmpty()) {
            return new AssignmentResult(new int[0], new int[0], 0);
        }

        // Sort requests by urgency (GREEDY CHOICE: highest urgency first)
        MyPriorityQueue<ServiceRequest> pq = new MyPriorityQueue<>();
        for (ServiceRequest req : requests) {
            pq.insert(req);
        }

        // Track assignments
        int[] assignedRequestIds = new int[Math.min(requests.size(), resources.size())];
        int[] assignedResourceIds = new int[Math.min(requests.size(), resources.size())];
        int assignedCount = 0;

        // Track available resources
        List<Resource> availableResources = new ArrayList<>(resources);

        // Greedy assignment
        while (!pq.isEmpty() && !availableResources.isEmpty()) {
            ServiceRequest request = pq.extractMin();
            // Find the best resource for this request
            Resource bestResource = findBestResource(request, availableResources);
            if (bestResource != null) {
                assignedRequestIds[assignedCount] = request.getRequestId();
                assignedResourceIds[assignedCount] = bestResource.getResourceId();
                assignedCount++;
                availableResources.remove(bestResource);
            }
        }

        // Trim arrays
        int[] finalRequests = new int[assignedCount];
        int[] finalResources = new int[assignedCount];
        System.arraycopy(assignedRequestIds, 0, finalRequests, 0, assignedCount);
        System.arraycopy(assignedResourceIds, 0, finalResources, 0, assignedCount);

        return new AssignmentResult(finalRequests, finalResources, assignedCount);
    }

    /**
     * Find the best resource for a request (greedy choice)
     */
    private static Resource findBestResource(ServiceRequest request, List<Resource> resources) {
        if (resources.isEmpty()) return null;

        // Greedy: Choose the resource with highest capacity
        Resource best = resources.get(0);
        for (Resource r : resources) {
            if (r.getCapacity() > best.getCapacity()) {
                best = r;
            }
        }
        return best;
    }

    /**
     * Generate counterexample for greedy failure
     */
    public static String generateCounterexample() {
        StringBuilder evidence = new StringBuilder();
        evidence.append("GREEDY ALGORITHM - COUNTEREXAMPLE\n");
        evidence.append("=".repeat(50)).append("\n\n");

        evidence.append("Scenario: Hospital Resource Assignment\n");
        evidence.append("-" .repeat(40)).append("\n");
        evidence.append("Available Resources: 2\n\n");

        evidence.append("Requests:\n");
        evidence.append("  A: Urgency=5 (EMERGENCY), Needs 3 resources\n");
        evidence.append("  B: Urgency=4 (URGENT), Needs 1 resource\n");
        evidence.append("  C: Urgency=4 (URGENT), Needs 1 resource\n\n");

        evidence.append("GREEDY APPROACH:\n");
        evidence.append("  1. Process A first (highest urgency)\n");
        evidence.append("  2. Assign 2 resources to A\n");
        evidence.append("  3. Result: A processed, B and C get NOTHING!\n\n");

        evidence.append("OPTIMAL APPROACH:\n");
        evidence.append("  1. Assign 1 resource to A\n");
        evidence.append("  2. Assign 1 resource to B\n");
        evidence.append("  3. Assign 1 resource to C\n");
        evidence.append("  4. Result: All requests get SOME resources!\n\n");

        evidence.append("-".repeat(40)).append("\n");
        evidence.append("CONCLUSION: Greedy fails when it doesn't consider ");
        evidence.append("resource constraints and allocation optimization.\n");
        evidence.append("Greedy is NOT always optimal!\n");

        return evidence.toString();
    }

    /**
     * Generate greedy algorithm trace
     */
    public static String generateTrace(List<ServiceRequest> requests, List<Resource> resources) {
        StringBuilder trace = new StringBuilder();
        trace.append("GREEDY ALGORITHM TRACE\n");
        trace.append("=".repeat(50)).append("\n\n");

        trace.append("Requests (sorted by urgency):\n");
        MyPriorityQueue<ServiceRequest> pq = new MyPriorityQueue<>();
        for (ServiceRequest req : requests) {
            pq.insert(req);
        }

        List<ServiceRequest> sorted = new ArrayList<>();
        while (!pq.isEmpty()) {
            sorted.add(pq.extractMin());
        }

        for (int i = 0; i < sorted.size(); i++) {
            ServiceRequest req = sorted.get(i);
            trace.append("  ").append(i + 1).append(". ID:").append(req.getRequestId())
                    .append(" Category:").append(req.getCategory())
                    .append(" Urgency:").append(req.getUrgency().getDisplayName())
                    .append("\n");
        }

        trace.append("\nResources:\n");
        for (Resource r : resources) {
            trace.append("  ID:").append(r.getResourceId())
                    .append(" Type:").append(r.getType())
                    .append(" Capacity:").append(r.getCapacity())
                    .append("\n");
        }

        AssignmentResult result = assignResources(requests, resources);

        trace.append("\nAssignments:\n");
        for (int i = 0; i < result.count; i++) {
            trace.append("  Request ").append(result.requestIds[i])
                    .append(" → Resource ").append(result.resourceIds[i])
                    .append("\n");
        }

        trace.append("\nTotal assigned: ").append(result.count).append(" requests\n");
        return trace.toString();
    }

    // =============================================
    // INNER CLASSES
    // =============================================

    public static class AssignmentResult {
        public int[] requestIds;
        public int[] resourceIds;
        public int count;

        AssignmentResult(int[] requestIds, int[] resourceIds, int count) {
            this.requestIds = requestIds;
            this.resourceIds = resourceIds;
            this.count = count;
        }
    }
}