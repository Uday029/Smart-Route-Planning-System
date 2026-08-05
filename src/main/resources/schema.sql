CREATE DATABASE IF NOT EXISTS smart_route_planner;
USE smart_route_planner;

CREATE TABLE IF NOT EXISTS Users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('USER', 'ADMIN') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS Cities (
    city_id INT AUTO_INCREMENT PRIMARY KEY,
    city_name VARCHAR(100) NOT NULL UNIQUE,
    state VARCHAR(100),
    latitude DOUBLE NOT NULL,
    longitude DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS Roads (
    road_id INT AUTO_INCREMENT PRIMARY KEY,
    source_city_id INT NOT NULL,
    destination_city_id INT NOT NULL,
    distance DOUBLE NOT NULL, -- in km
    speed_limit INT DEFAULT 60, -- km/h
    road_type VARCHAR(50), -- e.g., Highway, City Road
    is_one_way BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (source_city_id) REFERENCES Cities(city_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_city_id) REFERENCES Cities(city_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TollCharges (
    toll_id INT AUTO_INCREMENT PRIMARY KEY,
    road_id INT NOT NULL,
    cost DOUBLE NOT NULL,
    FOREIGN KEY (road_id) REFERENCES Roads(road_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS TrafficStatus (
    status_id INT AUTO_INCREMENT PRIMARY KEY,
    road_id INT NOT NULL,
    status_type ENUM('NORMAL', 'MEDIUM', 'HEAVY', 'CLOSED', 'CONSTRUCTION', 'ACCIDENT') DEFAULT 'NORMAL',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (road_id) REFERENCES Roads(road_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS WeatherStatus (
    weather_id INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT NOT NULL,
    weather_type ENUM('NORMAL', 'RAIN', 'FLOOD', 'FOG', 'BLOCKED') DEFAULT 'NORMAL',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (city_id) REFERENCES Cities(city_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS SearchHistory (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    source_city_id INT NOT NULL,
    destination_city_id INT NOT NULL,
    search_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    algorithm_used VARCHAR(50),
    distance_calculated DOUBLE,
    time_calculated VARCHAR(50),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (source_city_id) REFERENCES Cities(city_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_city_id) REFERENCES Cities(city_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FavoriteRoutes (
    fav_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    source_city_id INT NOT NULL,
    destination_city_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (source_city_id) REFERENCES Cities(city_id) ON DELETE CASCADE,
    FOREIGN KEY (destination_city_id) REFERENCES Cities(city_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS EmergencyServices (
    service_id INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT NOT NULL,
    service_type ENUM('HOSPITAL', 'POLICE', 'FIRE_STATION') NOT NULL,
    name VARCHAR(150) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    FOREIGN KEY (city_id) REFERENCES Cities(city_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS NearbyPlaces (
    place_id INT AUTO_INCREMENT PRIMARY KEY,
    city_id INT NOT NULL,
    place_type ENUM('PETROL_PUMP', 'RESTAURANT', 'HOTEL', 'ATM', 'HOSPITAL', 'POLICE_STATION') NOT NULL,
    name VARCHAR(150) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    FOREIGN KEY (city_id) REFERENCES Cities(city_id) ON DELETE CASCADE
);
