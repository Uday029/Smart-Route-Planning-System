package com.routeplanner.dao;

import com.routeplanner.model.City;
import com.routeplanner.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDAO {
    
    public boolean addCity(City city) {
        String query = "INSERT INTO Cities (city_name, state, latitude, longitude) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, city.getCityName());
            stmt.setString(2, city.getState());
            stmt.setDouble(3, city.getLatitude());
            stmt.setDouble(4, city.getLongitude());
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        city.setCityId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<City> getAllCities() {
        List<City> cities = new ArrayList<>();
        String query = "SELECT * FROM Cities";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                City city = new City(
                        rs.getInt("city_id"),
                        rs.getString("city_name"),
                        rs.getString("state"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
                cities.add(city);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public City getCityById(int id) {
        String query = "SELECT * FROM Cities WHERE city_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new City(
                            rs.getInt("city_id"),
                            rs.getString("city_name"),
                            rs.getString("state"),
                            rs.getDouble("latitude"),
                            rs.getDouble("longitude")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
