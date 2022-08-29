package com.kylemilner.takehome.lp.pricing;

import static java.util.Collections.emptyMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class TransitGraph {

    /*
     * In Memory Map based graph of nodes, with weights equivalent to prices in cents
     */

    private static final Map<String, Map<String, Integer>> graph = new HashMap<>();

    static {
        graph.put("Stop1", Map.of("Stop2", 325, "Stop3", 730));
        graph.put("Stop2", Map.of("Stop1", 325, "Stop3", 550));
        graph.put("Stop3", Map.of("Stop1", 730, "Stop3", 550));
    }

    public Optional<Integer> weightOfEdge(String fromNodeId, String toNodeId) {
        return Optional.ofNullable(graph.getOrDefault(fromNodeId, emptyMap()).get(toNodeId));
    }

    public Set<Integer> edgeWeightsFrom(String nodeId) {
        return Set.copyOf(graph.getOrDefault(nodeId, emptyMap()).values());
    }



}
