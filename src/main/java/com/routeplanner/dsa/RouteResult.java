package com.routeplanner.dsa;

import java.util.List;
import com.routeplanner.model.City;

public class RouteResult {
    private List<City> path;
    private double totalDistance;
    private double estimatedTime; // in hours
    private int numberOfStops;

    public RouteResult(List<City> path, double totalDistance, double estimatedTime, int numberOfStops) {
        this.path = path;
        this.totalDistance = totalDistance;
        this.estimatedTime = estimatedTime;
        this.numberOfStops = numberOfStops;
    }

    public List<City> getPath() { return path; }
    public double getTotalDistance() { return totalDistance; }
    public double getEstimatedTime() { return estimatedTime; }
    public int getNumberOfStops() { return numberOfStops; }
    
    @Override
    public String toString() {
        return "Distance: " + String.format("%.2f", totalDistance) + " km\n" +
               "Time: " + String.format("%.2f", estimatedTime) + " hr\n" +
               "Stops: " + numberOfStops + "\n" +
               "Route: " + path;
    }
}
