package com.routeplanner;

import com.routeplanner.model.City;
import com.routeplanner.model.Road;
import com.routeplanner.dsa.Graph;
import com.routeplanner.ui.MainFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Smart Route Planner starting GUI ===");
        
        // 1. Initialize Graph
        Graph graph = new Graph();
        
        // 2. Add Cities
        City delhi = new City(1, "Delhi", "Delhi", 28.7041, 77.1025);
        City jaipur = new City(2, "Jaipur", "Rajasthan", 26.9124, 75.7873);
        City ajmer = new City(3, "Ajmer", "Rajasthan", 26.4499, 74.6399);
        City udaipur = new City(4, "Udaipur", "Rajasthan", 24.5854, 73.7125);
        City mumbai = new City(5, "Mumbai", "Maharashtra", 19.0760, 72.8777);
        City pune = new City(6, "Pune", "Maharashtra", 18.5204, 73.8567);
        City ahmedabad = new City(7, "Ahmedabad", "Gujarat", 23.0225, 72.5714);
        City surat = new City(8, "Surat", "Gujarat", 21.1702, 72.8311);
        City bengaluru = new City(9, "Bengaluru", "Karnataka", 12.9716, 77.5946);
        City hyderabad = new City(10, "Hyderabad", "Telangana", 17.3850, 78.4867);
        City chennai = new City(11, "Chennai", "Tamil Nadu", 13.0827, 80.2707);
        City kolkata = new City(12, "Kolkata", "West Bengal", 22.5726, 88.3639);
        City patna = new City(13, "Patna", "Bihar", 25.5941, 85.1376);
        City lucknow = new City(14, "Lucknow", "Uttar Pradesh", 26.8467, 80.9462);
        City bhopal = new City(15, "Bhopal", "Madhya Pradesh", 23.2599, 77.4126);
        City nagpur = new City(16, "Nagpur", "Maharashtra", 21.1458, 79.0882);
        City kochi = new City(17, "Kochi", "Kerala", 9.9312, 76.2673);
        
        graph.addCity(delhi);
        graph.addCity(jaipur);
        graph.addCity(ajmer);
        graph.addCity(udaipur);
        graph.addCity(mumbai);
        graph.addCity(pune);
        graph.addCity(ahmedabad);
        graph.addCity(surat);
        graph.addCity(bengaluru);
        graph.addCity(hyderabad);
        graph.addCity(chennai);
        graph.addCity(kolkata);
        graph.addCity(patna);
        graph.addCity(lucknow);
        graph.addCity(bhopal);
        graph.addCity(nagpur);
        graph.addCity(kochi);
        
        // 3. Add Roads (Distances are approx)
        graph.addRoad(new Road(1, 1, 2, 280.0, 80, "Highway", false)); // Delhi <-> Jaipur
        graph.addRoad(new Road(2, 2, 3, 135.0, 80, "Highway", false)); // Jaipur <-> Ajmer
        graph.addRoad(new Road(3, 3, 4, 265.0, 80, "Highway", false)); // Ajmer <-> Udaipur
        graph.addRoad(new Road(4, 2, 4, 395.0, 80, "Highway", false)); // Jaipur <-> Udaipur (direct)
        graph.addRoad(new Road(5, 4, 7, 260.0, 80, "Highway", false)); // Udaipur <-> Ahmedabad
        graph.addRoad(new Road(6, 7, 8, 265.0, 80, "Highway", false)); // Ahmedabad <-> Surat
        graph.addRoad(new Road(7, 8, 5, 290.0, 80, "Highway", false)); // Surat <-> Mumbai
        graph.addRoad(new Road(8, 5, 6, 150.0, 80, "Expressway", false)); // Mumbai <-> Pune
        graph.addRoad(new Road(9, 6, 9, 840.0, 80, "Highway", false)); // Pune <-> Bengaluru
        graph.addRoad(new Road(10, 6, 10, 560.0, 80, "Highway", false)); // Pune <-> Hyderabad
        graph.addRoad(new Road(11, 10, 9, 570.0, 80, "Highway", false)); // Hyderabad <-> Bengaluru
        graph.addRoad(new Road(12, 9, 11, 350.0, 80, "Highway", false)); // Bengaluru <-> Chennai
        graph.addRoad(new Road(13, 10, 11, 630.0, 80, "Highway", false)); // Hyderabad <-> Chennai
        graph.addRoad(new Road(14, 1, 5, 1400.0, 80, "Highway", false)); // Delhi <-> Mumbai (direct)
        graph.addRoad(new Road(15, 1, 14, 550.0, 80, "Highway", false)); // Delhi <-> Lucknow
        graph.addRoad(new Road(16, 14, 13, 500.0, 80, "Highway", false)); // Lucknow <-> Patna
        graph.addRoad(new Road(17, 13, 12, 580.0, 80, "Highway", false)); // Patna <-> Kolkata
        graph.addRoad(new Road(18, 1, 15, 780.0, 80, "Highway", false)); // Delhi <-> Bhopal
        graph.addRoad(new Road(19, 15, 16, 350.0, 80, "Highway", false)); // Bhopal <-> Nagpur
        graph.addRoad(new Road(20, 16, 10, 500.0, 80, "Highway", false)); // Nagpur <-> Hyderabad
        graph.addRoad(new Road(21, 12, 11, 1670.0, 80, "Highway", false)); // Kolkata <-> Chennai
        graph.addRoad(new Road(22, 9, 17, 530.0, 80, "Highway", false)); // Bengaluru <-> Kochi
        graph.addRoad(new Road(23, 11, 17, 690.0, 80, "Highway", false)); // Chennai <-> Kochi

        // 4. Launch UI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(graph);
            frame.setVisible(true);
        });
    }
}
