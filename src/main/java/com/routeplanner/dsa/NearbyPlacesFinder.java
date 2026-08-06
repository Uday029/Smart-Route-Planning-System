package com.routeplanner.dsa;

import com.routeplanner.model.City;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class NearbyPlacesFinder {

    public static class PlaceDistance {
        public String placeName;
        public double distance;
        public double lat;
        public double lon;

        public PlaceDistance(String placeName, double distance, double lat, double lon) {
            this.placeName = placeName;
            this.distance = distance;
            this.lat = lat;
            this.lon = lon;
        }
    }

    // Using Haversine formula to calculate distance between two lat/long points
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; 
    }

    // Find nearest N places using a Max-Heap (PriorityQueue) within a max radius
    public static List<PlaceDistance> findNearestPlaces(double currentLat, double currentLon, 
                                                        Map<String, double[]> allPlaces, int n, double maxRadiusKm) {
        
        PriorityQueue<PlaceDistance> maxHeap = new PriorityQueue<>(
            Comparator.comparingDouble((PlaceDistance p) -> p.distance).reversed()
        );

        for (Map.Entry<String, double[]> entry : allPlaces.entrySet()) {
            double[] coords = entry.getValue();
            double dist = calculateDistance(currentLat, currentLon, coords[0], coords[1]);
            
            if (dist <= maxRadiusKm) {
                maxHeap.offer(new PlaceDistance(entry.getKey(), dist, coords[0], coords[1]));
                
                if (maxHeap.size() > n) {
                    maxHeap.poll(); // Remove the farthest place
                }
            }
        }

        List<PlaceDistance> result = new ArrayList<>();
        while (!maxHeap.isEmpty()) {
            result.add(0, maxHeap.poll()); // Reverse to get closest first
        }
        
        return result;
    }
}
