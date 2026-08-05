package com.routeplanner.ui;

import com.routeplanner.dsa.Graph;
import com.routeplanner.dsa.RouteResult;
import com.routeplanner.model.City;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class MapPanel extends JPanel {
    private Graph graph;
    private RouteResult currentRoute;
    private BufferedImage bgImage;
    
    // Global bounding box for Equirectangular Projection
    private final double minLat = -90.0;
    private final double maxLat = 90.0;
    private final double minLon = -180.0;
    private final double maxLon = 180.0;

    // Zoom and Pan variables
    private double scale = 1.0;
    private double translateX = 0.0;
    private double translateY = 0.0;
    private Point dragStartScreen;
    private Point dragStartOffset;

    public MapPanel(Graph graph) {
        this.graph = graph;
        setBackground(new Color(28, 35, 49)); // Dark ocean backup color
        try {
            bgImage = ImageIO.read(new File("src/main/resources/world_map.jpg"));
        } catch (IOException e) {
            System.err.println("Could not load world map image.");
        }

        // Initialize starting zoom to show the full map
        scale = 1.0;
        translateX = 0;
        translateY = 0;

        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartScreen = e.getPoint();
                dragStartOffset = new Point((int)translateX, (int)translateY);
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStartScreen != null) {
                    double dx = e.getPoint().x - dragStartScreen.x;
                    double dy = e.getPoint().y - dragStartScreen.y;
                    translateX = dragStartOffset.x + dx;
                    translateY = dragStartOffset.y + dy;
                    repaint();
                }
            }
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double oldScale = scale;
                if (e.getWheelRotation() < 0) {
                    scale *= 1.1; // zoom in
                } else {
                    scale /= 1.1; // zoom out
                }
                scale = Math.max(1.0, Math.min(scale, 20.0));
                
                // Math for zoom-to-cursor
                double x = e.getX();
                double y = e.getY();
                translateX = x - (x - translateX) * (scale / oldScale);
                translateY = y - (y - translateY) * (scale / oldScale);
                
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
        addMouseWheelListener(ma);
    }

    public void zoomIn() {
        double oldScale = scale;
        scale *= 1.2;
        scale = Math.min(scale, 20.0);
        zoomCenter(oldScale);
    }

    public void zoomOut() {
        double oldScale = scale;
        scale /= 1.2;
        scale = Math.max(1.0, scale);
        zoomCenter(oldScale);
    }

    private void zoomCenter(double oldScale) {
        double x = getWidth() / 2.0;
        double y = getHeight() / 2.0;
        translateX = x - (x - translateX) * (scale / oldScale);
        translateY = y - (y - translateY) * (scale / oldScale);
        repaint();
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

        // 1. Draw scaled background map
        if (bgImage != null) {
            int imgW = (int)(width * scale);
            int imgH = (int)(height * scale);
            g2.drawImage(bgImage, (int)translateX, (int)translateY, imgW, imgH, null);
        }

        if (graph == null) return;

        // 2. Draw all roads (edges) in light gray
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

        // 3. Draw highlighted route if it exists
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

        // 4. Draw all cities as dots
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

        // Map to raw global coordinates
        double rawX = (((lon - minLon) / (maxLon - minLon)) * width);
        double rawY = height - (((lat - minLat) / (maxLat - minLat)) * height);

        // Apply dynamic scale and pan
        int finalX = (int)(rawX * scale + translateX);
        int finalY = (int)(rawY * scale + translateY);

        return new Point(finalX, finalY);
    }

    private City getCityById(int id) {
        for (City c : graph.getCities()) {
            if (c.getCityId() == id) return c;
        }
        return null;
    }
}
