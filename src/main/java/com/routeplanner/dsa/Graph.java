package com.routeplanner.dsa;

import com.routeplanner.model.City;
import com.routeplanner.model.Road;

import java.util.*;

public class Graph {
    private Map<Integer, City> cities;
    private Map<Integer, List<Edge>> adjacencyList;

    public static class Edge {
        public int destinationCityId;
        public double distance;
        public int speedLimit;
        
        public Edge(int destinationCityId, double distance, int speedLimit) {
            this.destinationCityId = destinationCityId;
            this.distance = distance;
            this.speedLimit = speedLimit;
        }
    }

    public Graph() {
        cities = new HashMap<>();
        adjacencyList = new HashMap<>();
    }

    public void addCity(City city) {
        cities.put(city.getCityId(), city);
        adjacencyList.putIfAbsent(city.getCityId(), new ArrayList<>());
    }

    public void addRoad(Road road) {
        int src = road.getSourceCityId();
        int dest = road.getDestinationCityId();
        
        adjacencyList.putIfAbsent(src, new ArrayList<>());
        adjacencyList.get(src).add(new Edge(dest, road.getDistance(), road.getSpeedLimit()));
        
        if (!road.isOneWay()) {
            adjacencyList.putIfAbsent(dest, new ArrayList<>());
            adjacencyList.get(dest).add(new Edge(src, road.getDistance(), road.getSpeedLimit()));
        }
    }

    public Map<Integer, List<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public City getCity(int id) {
        return cities.get(id);
    }
    
    public Collection<City> getCities() {
        return cities.values();
    }
}
