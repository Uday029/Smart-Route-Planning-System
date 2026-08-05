package com.routeplanner.manager;

import com.routeplanner.model.Road;
import com.routeplanner.model.City;
import com.routeplanner.dsa.Graph;

import java.util.Random;
import java.util.List;

public class TrafficSimulator {

    public enum TrafficStatus {
        NORMAL(1.0),
        MEDIUM(1.5),
        HEAVY(2.5),
        CONSTRUCTION(3.0),
        ACCIDENT(4.0),
        CLOSED(Double.MAX_VALUE); // effectively breaks the route

        private final double timeMultiplier;

        TrafficStatus(double timeMultiplier) {
            this.timeMultiplier = timeMultiplier;
        }

        public double getTimeMultiplier() {
            return timeMultiplier;
        }
    }

    // Simulates traffic by adjusting edge distances (which represent time/cost in our graph)
    // distance / speed = time. If traffic is heavy, time increases, so effective weight increases.
    public static void applyTraffic(Graph graph) {
        Random random = new Random();
        TrafficStatus[] statuses = TrafficStatus.values();

        for (City city : graph.getCities()) {
            List<Graph.Edge> edges = graph.getAdjacencyList().get(city.getCityId());
            if (edges != null) {
                for (Graph.Edge edge : edges) {
                    // Randomly assign traffic (mostly Normal)
                    int rand = random.nextInt(100);
                    TrafficStatus status;
                    if (rand < 70) {
                        status = TrafficStatus.NORMAL;
                    } else if (rand < 85) {
                        status = TrafficStatus.MEDIUM;
                    } else if (rand < 95) {
                        status = TrafficStatus.HEAVY;
                    } else if (rand < 98) {
                        status = TrafficStatus.CONSTRUCTION;
                    } else {
                        status = TrafficStatus.ACCIDENT;
                    }

                    // Adjust the effective distance based on traffic time multiplier
                    // For routing algorithms to prefer normal traffic
                    if (status == TrafficStatus.CLOSED) {
                        edge.distance = Double.MAX_VALUE; // Road closed
                    } else {
                        edge.distance = edge.distance * status.getTimeMultiplier();
                    }
                }
            }
        }
    }
}
