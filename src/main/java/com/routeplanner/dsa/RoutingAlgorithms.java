package com.routeplanner.dsa;

import com.routeplanner.model.City;
import java.util.*;

public class RoutingAlgorithms {

    // Dijkstra's Algorithm for Shortest Distance
    public static RouteResult dijkstra(Graph graph, int sourceId, int destinationId) {
        Map<Integer, Double> distances = new HashMap<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distance));
        
        for (City city : graph.getCities()) {
            distances.put(city.getCityId(), Double.MAX_VALUE);
        }
        
        distances.put(sourceId, 0.0);
        pq.add(new NodeDistance(sourceId, 0.0));
        
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            int currentId = current.nodeId;
            
            if (currentId == destinationId) break;
            if (current.distance > distances.get(currentId)) continue;
            
            List<Graph.Edge> neighbors = graph.getAdjacencyList().getOrDefault(currentId, new ArrayList<>());
            for (Graph.Edge edge : neighbors) {
                double newDist = distances.get(currentId) + edge.distance;
                if (newDist < distances.get(edge.destinationCityId)) {
                    distances.put(edge.destinationCityId, newDist);
                    previousNodes.put(edge.destinationCityId, currentId);
                    pq.add(new NodeDistance(edge.destinationCityId, newDist));
                }
            }
        }
        
        return buildRouteResult(graph, sourceId, destinationId, previousNodes, distances);
    }

    // A* Search for Fastest Search
    public static RouteResult aStar(Graph graph, int sourceId, int destinationId) {
        Map<Integer, Double> gScore = new HashMap<>();
        Map<Integer, Double> fScore = new HashMap<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(Comparator.comparingDouble(nd -> nd.distance));
        
        for (City city : graph.getCities()) {
            gScore.put(city.getCityId(), Double.MAX_VALUE);
            fScore.put(city.getCityId(), Double.MAX_VALUE);
        }
        
        gScore.put(sourceId, 0.0);
        fScore.put(sourceId, heuristic(graph, sourceId, destinationId));
        pq.add(new NodeDistance(sourceId, fScore.get(sourceId)));
        
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            int currentId = current.nodeId;
            
            if (currentId == destinationId) break;
            if (fScore.get(currentId) < current.distance) continue;
            
            List<Graph.Edge> neighbors = graph.getAdjacencyList().getOrDefault(currentId, new ArrayList<>());
            for (Graph.Edge edge : neighbors) {
                double tentativeGScore = gScore.get(currentId) + edge.distance;
                if (tentativeGScore < gScore.get(edge.destinationCityId)) {
                    previousNodes.put(edge.destinationCityId, currentId);
                    gScore.put(edge.destinationCityId, tentativeGScore);
                    fScore.put(edge.destinationCityId, tentativeGScore + heuristic(graph, edge.destinationCityId, destinationId));
                    pq.add(new NodeDistance(edge.destinationCityId, fScore.get(edge.destinationCityId)));
                }
            }
        }
        
        return buildRouteResult(graph, sourceId, destinationId, previousNodes, gScore);
    }

    private static double heuristic(Graph graph, int cityId1, int cityId2) {
        City c1 = graph.getCity(cityId1);
        City c2 = graph.getCity(cityId2);
        if(c1 == null || c2 == null) return 0;
        return NearbyPlacesFinder.calculateDistance(c1.getLatitude(), c1.getLongitude(), c2.getLatitude(), c2.getLongitude());
    }

    // BFS for Minimum Stops
    public static RouteResult bfs(Graph graph, int sourceId, int destinationId) {
        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> previousNodes = new HashMap<>();
        Map<Integer, Double> distances = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        
        queue.add(sourceId);
        visited.add(sourceId);
        distances.put(sourceId, 0.0);
        
        while (!queue.isEmpty()) {
            int currentId = queue.poll();
            
            if (currentId == destinationId) break;
            
            List<Graph.Edge> neighbors = graph.getAdjacencyList().getOrDefault(currentId, new ArrayList<>());
            for (Graph.Edge edge : neighbors) {
                if (!visited.contains(edge.destinationCityId)) {
                    visited.add(edge.destinationCityId);
                    previousNodes.put(edge.destinationCityId, currentId);
                    distances.put(edge.destinationCityId, distances.get(currentId) + edge.distance);
                    queue.add(edge.destinationCityId);
                }
            }
        }
        
        return buildRouteResult(graph, sourceId, destinationId, previousNodes, distances);
    }

    // DFS for Any Alternate Route
    public static RouteResult dfs(Graph graph, int sourceId, int destinationId) {
        Set<Integer> visited = new HashSet<>();
        List<Integer> currentPath = new ArrayList<>();
        List<Integer> resultPath = new ArrayList<>();
        double[] totalDistance = new double[]{0.0};
        
        if(dfsHelper(graph, sourceId, destinationId, visited, currentPath, resultPath, totalDistance, 0.0)) {
            Map<Integer, Integer> previousNodes = new HashMap<>(); // Not used here directly, mock for formatting
            
            if(resultPath.size() < 2) return null;
            
            List<City> path = new ArrayList<>();
            for(int id : resultPath) {
                path.add(graph.getCity(id));
            }
            int numberOfStops = path.size() - 2;
            if(numberOfStops < 0) numberOfStops = 0;
            double estimatedTime = totalDistance[0] / 60.0;
            
            return new RouteResult(path, totalDistance[0], estimatedTime, numberOfStops);
        }
        return null;
    }

    private static boolean dfsHelper(Graph graph, int currentId, int destinationId, Set<Integer> visited, 
                                     List<Integer> currentPath, List<Integer> resultPath, 
                                     double[] totalDistance, double currentDist) {
        visited.add(currentId);
        currentPath.add(currentId);
        
        if (currentId == destinationId) {
            resultPath.addAll(currentPath);
            totalDistance[0] = currentDist;
            return true;
        }
        
        List<Graph.Edge> neighbors = graph.getAdjacencyList().getOrDefault(currentId, new ArrayList<>());
        for (Graph.Edge edge : neighbors) {
            if (!visited.contains(edge.destinationCityId)) {
                if (dfsHelper(graph, edge.destinationCityId, destinationId, visited, currentPath, resultPath, totalDistance, currentDist + edge.distance)) {
                    return true;
                }
            }
        }
        
        currentPath.remove(currentPath.size() - 1);
        return false;
    }

    private static RouteResult buildRouteResult(Graph graph, int sourceId, int destinationId, 
                                                Map<Integer, Integer> previousNodes, Map<Integer, Double> distances) {
        if (!previousNodes.containsKey(destinationId) && sourceId != destinationId) {
            return null; // No path found
        }
        
        List<City> path = new ArrayList<>();
        int curr = destinationId;
        while (curr != sourceId) {
            path.add(graph.getCity(curr));
            curr = previousNodes.get(curr);
        }
        path.add(graph.getCity(sourceId));
        Collections.reverse(path);
        
        double totalDistance = distances.get(destinationId);
        int numberOfStops = path.size() - 2; // excluding source and dest
        if(numberOfStops < 0) numberOfStops = 0;
        
        // Simple time estimation (assume 60km/h avg speed)
        double estimatedTime = totalDistance / 60.0;
        
        return new RouteResult(path, totalDistance, estimatedTime, numberOfStops);
    }

    // Floyd-Warshall for All Pairs Shortest Paths
    public static RouteResult floydWarshall(Graph graph, int sourceId, int destinationId) {
        int v = graph.getCities().size();
        List<City> cityList = new ArrayList<>(graph.getCities());
        Map<Integer, Integer> idToIndex = new HashMap<>();
        Map<Integer, Integer> indexToId = new HashMap<>();
        for(int i=0; i<cityList.size(); i++) {
            int id = cityList.get(i).getCityId();
            idToIndex.put(id, i);
            indexToId.put(i, id);
        }
        
        double[][] dist = new double[v][v];
        int[][] next = new int[v][v];
        
        for (int i = 0; i < v; i++) {
            Arrays.fill(dist[i], Double.MAX_VALUE);
            Arrays.fill(next[i], -1);
            dist[i][i] = 0;
            next[i][i] = i;
        }
        
        for (City city : cityList) {
            int u = idToIndex.get(city.getCityId());
            List<Graph.Edge> neighbors = graph.getAdjacencyList().getOrDefault(city.getCityId(), new ArrayList<>());
            for (Graph.Edge edge : neighbors) {
                if(!idToIndex.containsKey(edge.destinationCityId)) continue;
                int w = idToIndex.get(edge.destinationCityId);
                dist[u][w] = edge.distance;
                next[u][w] = w;
            }
        }
        
        for (int k = 0; k < v; k++) {
            for (int i = 0; i < v; i++) {
                for (int j = 0; j < v; j++) {
                    if (dist[i][k] != Double.MAX_VALUE && dist[k][j] != Double.MAX_VALUE && 
                        dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                        next[i][j] = next[i][k];
                    }
                }
            }
        }
        
        int srcIdx = idToIndex.getOrDefault(sourceId, -1);
        int dstIdx = idToIndex.getOrDefault(destinationId, -1);
        if(srcIdx == -1 || dstIdx == -1 || next[srcIdx][dstIdx] == -1) return null;
        
        List<City> path = new ArrayList<>();
        int currIdx = srcIdx;
        while(currIdx != dstIdx) {
            path.add(graph.getCity(indexToId.get(currIdx)));
            currIdx = next[currIdx][dstIdx];
        }
        path.add(graph.getCity(destinationId));
        
        double totalDistance = dist[srcIdx][dstIdx];
        int numberOfStops = path.size() - 2;
        if(numberOfStops < 0) numberOfStops = 0;
        double estimatedTime = totalDistance / 60.0;
        
        return new RouteResult(path, totalDistance, estimatedTime, numberOfStops);
    }

    private static class NodeDistance {
        int nodeId;
        double distance;
        
        NodeDistance(int nodeId, double distance) {
            this.nodeId = nodeId;
            this.distance = distance;
        }
    }
}
