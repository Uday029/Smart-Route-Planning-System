package com.routeplanner.manager;

import com.routeplanner.model.Road;
import com.routeplanner.model.City;
import com.routeplanner.dsa.Graph;

import java.util.Random;
import java.util.List;

public class WeatherSimulator {

    public enum WeatherStatus {
        NORMAL(1.0),
        RAIN(1.2),
        FOG(1.5),
        FLOOD(2.5),
        BLOCKED(Double.MAX_VALUE);

        private final double timeMultiplier;

        WeatherStatus(double timeMultiplier) {
            this.timeMultiplier = timeMultiplier;
        }

        public double getTimeMultiplier() {
            return timeMultiplier;
        }
    }

    // Applies weather penalties to the graph edges temporarily
    public static void applyWeather(Graph graph, boolean avoidBlocked) {
        Random random = new Random();

        for (City city : graph.getCities()) {
            List<Graph.Edge> edges = graph.getAdjacencyList().get(city.getCityId());
            if (edges != null) {
                for (Graph.Edge edge : edges) {
                    // Randomly assign weather (mostly Normal)
                    int rand = random.nextInt(100);
                    WeatherStatus status;
                    if (rand < 80) {
                        status = WeatherStatus.NORMAL;
                    } else if (rand < 90) {
                        status = WeatherStatus.RAIN;
                    } else if (rand < 95) {
                        status = WeatherStatus.FOG;
                    } else if (rand < 98) {
                        status = WeatherStatus.FLOOD;
                    } else {
                        status = WeatherStatus.BLOCKED;
                    }

                    if (status == WeatherStatus.BLOCKED && avoidBlocked) {
                        edge.distance = Double.MAX_VALUE; // Road blocked by weather
                    } else if (edge.distance != Double.MAX_VALUE) {
                        edge.distance = edge.distance * status.getTimeMultiplier();
                    }
                }
            }
        }
    }
}
