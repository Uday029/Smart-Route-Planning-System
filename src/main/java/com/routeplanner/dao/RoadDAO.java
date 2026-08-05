package com.routeplanner.dao;

import com.routeplanner.model.Road;
import com.routeplanner.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoadDAO {
    
    public boolean addRoad(Road road) {
        String query = "INSERT INTO Roads (source_city_id, destination_city_id, distance, speed_limit, road_type, is_one_way) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, road.getSourceCityId());
            stmt.setInt(2, road.getDestinationCityId());
            stmt.setDouble(3, road.getDistance());
            stmt.setInt(4, road.getSpeedLimit());
            stmt.setString(5, road.getRoadType());
            stmt.setBoolean(6, road.isOneWay());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        road.setRoadId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Road> getAllRoads() {
        List<Road> roads = new ArrayList<>();
        String query = "SELECT * FROM Roads";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Road road = new Road(
                        rs.getInt("road_id"),
                        rs.getInt("source_city_id"),
                        rs.getInt("destination_city_id"),
                        rs.getDouble("distance"),
                        rs.getInt("speed_limit"),
                        rs.getString("road_type"),
                        rs.getBoolean("is_one_way")
                );
                roads.add(road);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roads;
    }
}
