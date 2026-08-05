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
        
        City trivandrum = new City(18, "Thiruvananthapuram", "Kerala", 8.5241, 76.9366);
        City coimbatore = new City(19, "Coimbatore", "Tamil Nadu", 11.0168, 76.9558);
        City madurai = new City(20, "Madurai", "Tamil Nadu", 9.9252, 78.1198);
        City visakhapatnam = new City(21, "Visakhapatnam", "Andhra", 17.6868, 83.2185);
        City bhubaneswar = new City(22, "Bhubaneswar", "Odisha", 20.2961, 85.8245);
        City ranchi = new City(23, "Ranchi", "Jharkhand", 23.3441, 85.3096);
        City raipur = new City(24, "Raipur", "Chhattisgarh", 21.2514, 81.6296);
        City indore = new City(25, "Indore", "Madhya Pradesh", 22.7196, 75.8577);
        City vadodara = new City(26, "Vadodara", "Gujarat", 22.3072, 73.1812);
        City rajkot = new City(27, "Rajkot", "Gujarat", 22.3039, 70.8022);
        City amritsar = new City(28, "Amritsar", "Punjab", 31.6340, 74.8723);
        City chandigarh = new City(29, "Chandigarh", "Chandigarh", 30.7333, 76.7794);
        City guwahati = new City(30, "Guwahati", "Assam", 26.1445, 91.7362);
        City srinagar = new City(31, "Srinagar", "J&K", 34.0837, 74.7973);
        City dehradun = new City(32, "Dehradun", "Uttarakhand", 30.3165, 78.0322);

        City[] allCities = {delhi, jaipur, ajmer, udaipur, mumbai, pune, ahmedabad, surat, 
            bengaluru, hyderabad, chennai, kolkata, patna, lucknow, bhopal, nagpur, kochi,
            trivandrum, coimbatore, madurai, visakhapatnam, bhubaneswar, ranchi, raipur, 
            indore, vadodara, rajkot, amritsar, chandigarh, guwahati, srinagar, dehradun};
            
        for (City c : allCities) {
            graph.addCity(c);
        }
        
        // 3. Add Roads (Distances are approx)
        graph.addRoad(new Road(1, 1, 2, 280.0, 80, "Highway", false));
        graph.addRoad(new Road(2, 2, 3, 135.0, 80, "Highway", false));
        graph.addRoad(new Road(3, 3, 4, 265.0, 80, "Highway", false));
        graph.addRoad(new Road(4, 2, 4, 395.0, 80, "Highway", false));
        graph.addRoad(new Road(5, 4, 7, 260.0, 80, "Highway", false));
        graph.addRoad(new Road(6, 7, 8, 265.0, 80, "Highway", false));
        graph.addRoad(new Road(7, 8, 5, 290.0, 80, "Highway", false));
        graph.addRoad(new Road(8, 5, 6, 150.0, 80, "Expressway", false));
        graph.addRoad(new Road(9, 6, 9, 840.0, 80, "Highway", false));
        graph.addRoad(new Road(10, 6, 10, 560.0, 80, "Highway", false));
        graph.addRoad(new Road(11, 10, 9, 570.0, 80, "Highway", false));
        graph.addRoad(new Road(12, 9, 11, 350.0, 80, "Highway", false));
        graph.addRoad(new Road(13, 10, 11, 630.0, 80, "Highway", false));
        graph.addRoad(new Road(14, 1, 5, 1400.0, 80, "Highway", false));
        graph.addRoad(new Road(15, 1, 14, 550.0, 80, "Highway", false));
        graph.addRoad(new Road(16, 14, 13, 500.0, 80, "Highway", false));
        graph.addRoad(new Road(17, 13, 12, 580.0, 80, "Highway", false));
        graph.addRoad(new Road(18, 1, 15, 780.0, 80, "Highway", false));
        graph.addRoad(new Road(19, 15, 16, 350.0, 80, "Highway", false));
        graph.addRoad(new Road(20, 16, 10, 500.0, 80, "Highway", false));
        graph.addRoad(new Road(21, 12, 11, 1670.0, 80, "Highway", false));
        graph.addRoad(new Road(22, 9, 17, 530.0, 80, "Highway", false));
        graph.addRoad(new Road(23, 11, 17, 690.0, 80, "Highway", false));
        
        // New Roads
        graph.addRoad(new Road(24, 17, 18, 200.0, 80, "Highway", false));
        graph.addRoad(new Road(25, 9, 19, 360.0, 80, "Highway", false));
        graph.addRoad(new Road(26, 19, 20, 210.0, 80, "Highway", false));
        graph.addRoad(new Road(27, 20, 11, 460.0, 80, "Highway", false));
        graph.addRoad(new Road(28, 11, 21, 800.0, 80, "Highway", false));
        graph.addRoad(new Road(29, 21, 22, 440.0, 80, "Highway", false));
        graph.addRoad(new Road(30, 22, 12, 440.0, 80, "Highway", false));
        graph.addRoad(new Road(31, 12, 23, 400.0, 80, "Highway", false));
        graph.addRoad(new Road(32, 23, 24, 600.0, 80, "Highway", false));
        graph.addRoad(new Road(33, 24, 16, 280.0, 80, "Highway", false));
        graph.addRoad(new Road(34, 15, 25, 190.0, 80, "Highway", false));
        graph.addRoad(new Road(35, 25, 7, 390.0, 80, "Highway", false));
        graph.addRoad(new Road(36, 7, 26, 110.0, 80, "Expressway", false));
        graph.addRoad(new Road(37, 7, 27, 215.0, 80, "Highway", false));
        graph.addRoad(new Road(38, 1, 29, 240.0, 80, "Highway", false));
        graph.addRoad(new Road(39, 29, 28, 225.0, 80, "Highway", false));
        graph.addRoad(new Road(40, 29, 31, 560.0, 80, "Highway", false));
        graph.addRoad(new Road(41, 1, 32, 250.0, 80, "Highway", false));
        graph.addRoad(new Road(42, 12, 30, 1000.0, 80, "Highway", false));

        // 4. Launch UI
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(graph);
            frame.setVisible(true);
        });
    }
}
