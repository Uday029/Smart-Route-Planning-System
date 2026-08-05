package com.routeplanner.ui;

import com.routeplanner.dsa.*;
import com.routeplanner.model.*;
import com.routeplanner.manager.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class MainFrame extends JFrame {
    private Graph graph;
    
    // Route Planner Tab
    private JComboBox<City> sourceCombo;
    private JComboBox<City> destCombo;
    private JComboBox<String> algoCombo;
    private JCheckBox trafficCheck;
    private JCheckBox weatherCheck;
    private JTextArea resultArea;
    private MapPanel mapPanel;

    // Nearby Tab
    private JComboBox<City> currentLocCombo;
    private JComboBox<String> placeTypeCombo;
    private JTextArea nearbyResultArea;
    private Map<String, Map<String, double[]>> mockPlacesData;

    public MainFrame(Graph graph) {
        this.graph = graph;
        initMockPlaces();
        
        setTitle("Smart Route Planner - Dashboard");
        setSize(850, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Route Planner", createRoutePanel());
        tabbedPane.addTab("Nearby & Emergency", createNearbyPanel());
        tabbedPane.addTab("Admin Panel", createAdminPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createRoutePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(new JLabel("Source City:"));
        sourceCombo = new JComboBox<>(graph.getCities().toArray(new City[0]));
        topPanel.add(sourceCombo);

        topPanel.add(new JLabel("Destination City:"));
        destCombo = new JComboBox<>(graph.getCities().toArray(new City[0]));
        topPanel.add(destCombo);

        topPanel.add(new JLabel("Algorithm:"));
        algoCombo = new JComboBox<>(new String[]{"Dijkstra (Shortest)", "A* (Fastest)", "BFS (Min Stops)", "DFS (Any Route)", "Floyd-Warshall"});
        topPanel.add(algoCombo);

        trafficCheck = new JCheckBox("Apply Live Traffic (Delays)");
        weatherCheck = new JCheckBox("Avoid Bad Weather/Blocked Roads");
        topPanel.add(trafficCheck);
        topPanel.add(weatherCheck);

        JButton findBtn = new JButton("Find Best Route");
        topPanel.add(new JLabel("")); // empty
        topPanel.add(findBtn);

        panel.add(topPanel, BorderLayout.NORTH);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        resultArea.setMargin(new Insets(10, 10, 10, 10));
        
        mapPanel = new MapPanel(graph);
        
        JPanel mapContainer = new JPanel(new BorderLayout());
        mapContainer.add(mapPanel, BorderLayout.CENTER);
        mapContainer.setPreferredSize(new Dimension(400, 400));

        JToolBar zoomBar = new JToolBar();
        zoomBar.setFloatable(false);
        JButton zoomInBtn = new JButton("+");
        zoomInBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        JButton zoomOutBtn = new JButton("-");
        zoomOutBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        
        zoomInBtn.addActionListener(e -> mapPanel.zoomIn());
        zoomOutBtn.addActionListener(e -> mapPanel.zoomOut());
        
        zoomBar.add(zoomOutBtn);
        zoomBar.add(zoomInBtn);
        mapContainer.add(zoomBar, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                new JScrollPane(resultArea), mapContainer);
        splitPane.setDividerLocation(350);
        
        panel.add(splitPane, BorderLayout.CENTER);

        findBtn.addActionListener(e -> calculateRoute());
        
        return panel;
    }

    private JPanel createNearbyPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        topPanel.add(new JLabel("Current City:"));
        currentLocCombo = new JComboBox<>(graph.getCities().toArray(new City[0]));
        topPanel.add(currentLocCombo);
        
        topPanel.add(new JLabel("Find Nearest:"));
        placeTypeCombo = new JComboBox<>(new String[]{"Hospital", "Police Station", "Petrol Pump", "Restaurant", "ATM"});
        topPanel.add(placeTypeCombo);
        
        JButton findPlacesBtn = new JButton("Search Nearby");
        topPanel.add(new JLabel(""));
        topPanel.add(findPlacesBtn);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        nearbyResultArea = new JTextArea();
        nearbyResultArea.setEditable(false);
        nearbyResultArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        nearbyResultArea.setMargin(new Insets(10, 10, 10, 10));
        panel.add(new JScrollPane(nearbyResultArea), BorderLayout.CENTER);
        
        findPlacesBtn.addActionListener(e -> findNearbyPlaces());
        
        return panel;
    }

    private JPanel createAdminPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Add City Panel
        JPanel cityPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        cityPanel.setBorder(BorderFactory.createTitledBorder("Add New City"));
        
        JTextField cityNameField = new JTextField();
        JTextField cityStateField = new JTextField();
        JButton addCityBtn = new JButton("Add City");
        
        cityPanel.add(new JLabel("City Name:")); cityPanel.add(cityNameField);
        cityPanel.add(new JLabel("State:")); cityPanel.add(cityStateField);
        cityPanel.add(new JLabel("")); cityPanel.add(addCityBtn);
        
        // Add Road Panel
        JPanel roadPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        roadPanel.setBorder(BorderFactory.createTitledBorder("Add New Road"));
        
        JTextField roadIdField = new JTextField();
        JComboBox<City> srcCityCombo = new JComboBox<>(graph.getCities().toArray(new City[0]));
        JComboBox<City> destCityCombo = new JComboBox<>(graph.getCities().toArray(new City[0]));
        JTextField distanceField = new JTextField();
        JTextField speedLimitField = new JTextField();
        JTextField roadTypeField = new JTextField("Highway");
        JCheckBox isOneWayCheck = new JCheckBox("Is One Way?");
        JButton addRoadBtn = new JButton("Add Road");
        
        roadPanel.add(new JLabel("Road ID:")); roadPanel.add(roadIdField);
        roadPanel.add(new JLabel("Source City:")); roadPanel.add(srcCityCombo);
        roadPanel.add(new JLabel("Destination City:")); roadPanel.add(destCityCombo);
        roadPanel.add(new JLabel("Distance (km):")); roadPanel.add(distanceField);
        roadPanel.add(new JLabel("Speed Limit:")); roadPanel.add(speedLimitField);
        roadPanel.add(new JLabel("Road Type:")); roadPanel.add(roadTypeField);
        roadPanel.add(new JLabel("")); roadPanel.add(isOneWayCheck);
        roadPanel.add(new JLabel("")); roadPanel.add(addRoadBtn);
        
        topPanel.add(cityPanel);
        topPanel.add(roadPanel);
        panel.add(topPanel, BorderLayout.NORTH);
        
        JTextArea adminLogArea = new JTextArea();
        adminLogArea.setEditable(false);
        panel.add(new JScrollPane(adminLogArea), BorderLayout.CENTER);
        
        // Actions
        addCityBtn.addActionListener(e -> {
            try {
                // Auto-generate City ID
                int id = graph.getCities().size() + 1;
                String name = cityNameField.getText();
                String state = cityStateField.getText();
                // Assign random geographic coordinates within India's approximate bounds
                // Lat: 10 to 30, Lon: 70 to 88
                double lat = 10.0 + (Math.random() * 20.0);
                double lon = 70.0 + (Math.random() * 18.0);
                
                City newCity = new City(id, name, state, lat, lon);
                graph.addCity(newCity);
                
                sourceCombo.addItem(newCity);
                destCombo.addItem(newCity);
                currentLocCombo.addItem(newCity);
                srcCityCombo.addItem(newCity);
                destCityCombo.addItem(newCity);
                
                adminLogArea.append("Success: Added City - " + name + " (Auto-assigned ID: " + id + ")\n");
                
                cityNameField.setText(""); cityStateField.setText("");
            } catch (Exception ex) {
                adminLogArea.append("Error adding city: Please check the inputs.\n");
            }
        });
        
        addRoadBtn.addActionListener(e -> {
            try {
                int id = Integer.parseInt(roadIdField.getText());
                City src = (City) srcCityCombo.getSelectedItem();
                City dest = (City) destCityCombo.getSelectedItem();
                double dist = Double.parseDouble(distanceField.getText());
                int speed = Integer.parseInt(speedLimitField.getText());
                String type = roadTypeField.getText();
                boolean isOneWay = isOneWayCheck.isSelected();
                
                if(src == null || dest == null) return;
                
                Road newRoad = new Road(id, src.getCityId(), dest.getCityId(), dist, speed, type, isOneWay);
                graph.addRoad(newRoad);
                
                adminLogArea.append("Success: Added Road from " + src.getCityName() + " to " + dest.getCityName() + "\n");
                
                roadIdField.setText(""); distanceField.setText(""); speedLimitField.setText("");
            } catch (Exception ex) {
                adminLogArea.append("Error adding road: Please check the inputs.\n");
            }
        });
        
        return panel;
    }
    
    private void initMockPlaces() {
        mockPlacesData = new HashMap<>();
        
        Map<String, double[]> hospitals = new HashMap<>();
        // Near Delhi
        hospitals.put("City Care Hospital (Delhi)", new double[]{28.71, 77.11});
        hospitals.put("AIIMS (Delhi)", new double[]{28.56, 77.20});
        // Near Jaipur
        hospitals.put("SMS Hospital (Jaipur)", new double[]{26.90, 75.80});
        hospitals.put("Fortis Escorts (Jaipur)", new double[]{26.85, 75.80});
        // Near Ajmer
        hospitals.put("JLN Hospital (Ajmer)", new double[]{26.45, 74.64});
        // Near Udaipur
        hospitals.put("Geetanjali Hospital (Udaipur)", new double[]{24.59, 73.72});
        // Near Mumbai
        hospitals.put("Apollo Hospital (Mumbai)", new double[]{19.08, 72.88});
        hospitals.put("Lilavati Hospital (Mumbai)", new double[]{19.05, 72.82});
        // Near Pune
        hospitals.put("Ruby Hall Clinic (Pune)", new double[]{18.53, 73.87});
        // Near Ahmedabad
        hospitals.put("Zydus Hospital (Ahmedabad)", new double[]{23.04, 72.52});
        // Near Surat
        hospitals.put("Kiran Hospital (Surat)", new double[]{21.20, 72.84});
        // Near Bengaluru
        hospitals.put("Fortis Healthcare (Bengaluru)", new double[]{12.98, 77.60});
        hospitals.put("Manipal Hospital (Bengaluru)", new double[]{12.95, 77.64});
        // Near Hyderabad
        hospitals.put("Yashoda Hospital (Hyderabad)", new double[]{17.42, 78.49});
        // Near Chennai
        hospitals.put("MIOT Hospital (Chennai)", new double[]{13.02, 80.18});
        hospitals.put("Apollo Speciality (Chennai)", new double[]{13.06, 80.25});
        // Near Kolkata
        hospitals.put("AMRI Hospital (Kolkata)", new double[]{22.51, 88.36});
        // Near Patna
        hospitals.put("Ruban Hospital (Patna)", new double[]{25.61, 85.12});
        // Near Lucknow
        hospitals.put("Sahara Hospital (Lucknow)", new double[]{26.86, 81.01});
        // Near Bhopal
        hospitals.put("Bansal Hospital (Bhopal)", new double[]{23.21, 77.43});
        // Near Nagpur
        hospitals.put("Wockhardt Hospital (Nagpur)", new double[]{21.13, 79.06});
        // Near Kochi
        hospitals.put("Amrita Hospital (Kochi)", new double[]{10.02, 76.29});

        mockPlacesData.put("Hospital", hospitals);
        
        Map<String, double[]> petrolPumps = new HashMap<>();
        petrolPumps.put("IndianOil Petrol Pump (Jaipur)", new double[]{26.92, 75.79});
        petrolPumps.put("Bharat Petroleum (Ahmedabad)", new double[]{23.03, 72.58});
        petrolPumps.put("HP Petrol Pump (Delhi)", new double[]{28.72, 77.10});
        petrolPumps.put("Shell Petrol Pump (Bengaluru)", new double[]{12.99, 77.62});
        mockPlacesData.put("Petrol Pump", petrolPumps);
        
        // Add empty maps for others to avoid null ptr
        mockPlacesData.put("Police Station", new HashMap<>());
        mockPlacesData.put("Restaurant", new HashMap<>());
        mockPlacesData.put("ATM", new HashMap<>());
    }

    private void findNearbyPlaces() {
        City src = (City) currentLocCombo.getSelectedItem();
        String type = (String) placeTypeCombo.getSelectedItem();
        if(src == null || type == null) return;
        
        Map<String, double[]> places = mockPlacesData.get(type);
        if(places.isEmpty()) {
            nearbyResultArea.setText("No " + type + " found in the database currently.");
            return;
        }
        
        List<NearbyPlacesFinder.PlaceDistance> nearest = NearbyPlacesFinder.findNearestPlaces(
                src.getLatitude(), src.getLongitude(), places, 3);
                
        StringBuilder sb = new StringBuilder();
        sb.append("=== Nearest ").append(type).append("s to ").append(src.getCityName()).append(" ===\n\n");
        for(NearbyPlacesFinder.PlaceDistance p : nearest) {
            sb.append("- ").append(p.placeName).append(" (").append(String.format("%.2f", p.distance)).append(" km away)\n");
        }
        
        nearbyResultArea.setText(sb.toString());
    }

    private void calculateRoute() {
        City src = (City) sourceCombo.getSelectedItem();
        City dest = (City) destCombo.getSelectedItem();
        String algo = (String) algoCombo.getSelectedItem();

        if (src == null || dest == null) return;
        if (src.getCityId() == dest.getCityId()) {
            resultArea.setText("Source and Destination cannot be the same.");
            return;
        }
        
        // Reset graph distances to normal before applying new dynamic conditions
        // (In a real app, we'd deep copy or have a base distance map, doing a quick hack here)
        // This is a simplification.

        if (trafficCheck.isSelected()) {
            TrafficSimulator.applyTraffic(graph);
            resultArea.setText("Live Traffic Conditions Applied!\n\n");
        }
        if (weatherCheck.isSelected()) {
            WeatherSimulator.applyWeather(graph, true);
        }

        RouteResult result = null;
        try {
            switch (algo) {
                case "Dijkstra (Shortest)":
                    result = RoutingAlgorithms.dijkstra(graph, src.getCityId(), dest.getCityId());
                    break;
                case "A* (Fastest)":
                    result = RoutingAlgorithms.aStar(graph, src.getCityId(), dest.getCityId());
                    break;
                case "BFS (Min Stops)":
                    result = RoutingAlgorithms.bfs(graph, src.getCityId(), dest.getCityId());
                    break;
                case "DFS (Any Route)":
                    result = RoutingAlgorithms.dfs(graph, src.getCityId(), dest.getCityId());
                    break;
                case "Floyd-Warshall":
                    result = RoutingAlgorithms.floydWarshall(graph, src.getCityId(), dest.getCityId());
                    break;
            }
        } catch(Exception ex) {
            resultArea.setText("Error calculating route.");
            return;
        }

        if (result != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("=== Route Found ===\n");
            sb.append(result.toString()).append("\n\n");
            
            double mileage = 15.0; // km/l default
            double fuelPrice = 100.0; // default
            
            double totalToll = TollFuelCalculator.calculateTotalToll(result);
            double fuelReq = TollFuelCalculator.calculateFuelRequired(result, mileage);
            double fuelCost = TollFuelCalculator.calculateFuelCost(fuelReq, fuelPrice);
            double totalCost = totalToll + fuelCost;
            
            sb.append("--- Cost Estimation ---\n");
            sb.append(String.format("Fuel Required: %.2f L (at %.2f km/L)\n", fuelReq, mileage));
            sb.append(String.format("Fuel Cost: ₹%.2f\n", fuelCost));
            sb.append(String.format("Estimated Tolls: ₹%.2f\n", totalToll));
            sb.append(String.format("Total Journey Cost: ₹%.2f\n", totalCost));
            
            String currentText = resultArea.getText();
            resultArea.setText((currentText.contains("Live Traffic") ? currentText : "") + sb.toString());
            mapPanel.setRoute(result);
        } else {
            resultArea.setText("No route found between " + src.getCityName() + " and " + dest.getCityName() + ".\n(Roads might be closed due to severe weather!)");
            mapPanel.setRoute(null);
        }
    }
}
