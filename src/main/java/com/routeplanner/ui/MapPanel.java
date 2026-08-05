package com.routeplanner.ui;

import com.routeplanner.dsa.Graph;
import com.routeplanner.dsa.RouteResult;
import com.routeplanner.model.City;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MapPanel extends JPanel {
    private Graph graph;
    private RouteResult currentRoute;
    
    // Approximate bounding box for India (for scaling to panel size)
    private final double minLat = 8.0;
    private final double maxLat = 32.0;
    private final double minLon = 68.0;
    private final double maxLon = 92.0;

    public MapPanel(Graph graph) {
        this.graph = graph;
        setBackground(new Color(245, 245, 250)); // Light map background
    }

    public void setRoute(RouteResult route) {
        this.currentRoute = route;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        if (graph == null) return;

        // 1. Draw all roads (edges) in light gray
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        
        for (City city : graph.getCities()) {
            Point p1 = getPoint(city, width, height);
            List<Graph.Edge> edges = graph.getAdjacencyList().get(city.getCityId());
            if (edges != null) {
                for (Graph.Edge edge : edges) {
                    City target = getCityById(edge.destinationCityId);
                    if (target != null) {
                        Point p2 = getPoint(target, width, height);
                        g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                    }
                }
            }
        }

        // 2. Draw highlighted route if it exists
        if (currentRoute != null && currentRoute.getPath() != null && currentRoute.getPath().size() > 1) {
            g2.setColor(new Color(220, 53, 69)); // Bootstrap Danger Red
            g2.setStroke(new BasicStroke(4));
            
            for (int i = 0; i < currentRoute.getPath().size() - 1; i++) {
                City c1 = currentRoute.getPath().get(i);
                City c2 = currentRoute.getPath().get(i+1);
                if (c1 != null && c2 != null) {
                    Point p1 = getPoint(c1, width, height);
                    Point p2 = getPoint(c2, width, height);
                    g2.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }

        // 3. Draw all cities as dots
        for (City city : graph.getCities()) {
            Point p = getPoint(city, width, height);
            
            // Check if city is in the path
            boolean inPath = false;
            if (currentRoute != null && currentRoute.getPath() != null) {
                inPath = currentRoute.getPath().stream().anyMatch(c -> c.getCityId() == city.getCityId());
            }

            if (inPath) {
                g2.setColor(new Color(220, 53, 69)); // Red dot for path
                g2.fillOval(p.x - 6, p.y - 6, 12, 12);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 12));
            } else {
                g2.setColor(new Color(100, 150, 255)); // Blue dot
                g2.fillOval(p.x - 4, p.y - 4, 8, 8);
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            }
            
            // Draw City Name
            g2.drawString(city.getCityName(), p.x + 8, p.y + 4);
        }
    }

    private Point getPoint(City city, int width, int height) {
        double lat = city.getLatitude();
        double lon = city.getLongitude();

        // Map to X, Y
        int x = (int) (((lon - minLon) / (maxLon - minLon)) * width);
        int y = height - (int) (((lat - minLat) / (maxLat - minLat)) * height);

        // Add 10% padding margin so nodes don't hug the edge
        x = (int) (x * 0.8) + (int)(width * 0.1);
        y = (int) (y * 0.8) + (int)(height * 0.1);

        return new Point(x, y);
    }

    private City getCityById(int id) {
        for (City c : graph.getCities()) {
            if (c.getCityId() == id) return c;
        }
        return null;
    }
}
