package com.routeplanner.model;

public class City {
    private int cityId;
    private String cityName;
    private String state;
    private double latitude;
    private double longitude;

    public City() {}

    public City(int cityId, String cityName, String state, double latitude, double longitude) {
        this.cityId = cityId;
        this.cityName = cityName;
        this.state = state;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getCityId() { return cityId; }
    public void setCityId(int cityId) { this.cityId = cityId; }
    
    public String getCityName() { return cityName; }
    public void setCityName(String cityName) { this.cityName = cityName; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return cityName;
    }
}
