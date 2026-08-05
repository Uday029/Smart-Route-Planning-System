package com.routeplanner.model;

public class Road {
    private int roadId;
    private int sourceCityId;
    private int destinationCityId;
    private double distance;
    private int speedLimit;
    private String roadType;
    private boolean isOneWay;

    public Road() {}

    public Road(int roadId, int sourceCityId, int destinationCityId, double distance, int speedLimit, String roadType, boolean isOneWay) {
        this.roadId = roadId;
        this.sourceCityId = sourceCityId;
        this.destinationCityId = destinationCityId;
        this.distance = distance;
        this.speedLimit = speedLimit;
        this.roadType = roadType;
        this.isOneWay = isOneWay;
    }

    public int getRoadId() { return roadId; }
    public void setRoadId(int roadId) { this.roadId = roadId; }

    public int getSourceCityId() { return sourceCityId; }
    public void setSourceCityId(int sourceCityId) { this.sourceCityId = sourceCityId; }

    public int getDestinationCityId() { return destinationCityId; }
    public void setDestinationCityId(int destinationCityId) { this.destinationCityId = destinationCityId; }

    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }

    public int getSpeedLimit() { return speedLimit; }
    public void setSpeedLimit(int speedLimit) { this.speedLimit = speedLimit; }

    public String getRoadType() { return roadType; }
    public void setRoadType(String roadType) { this.roadType = roadType; }

    public boolean isOneWay() { return isOneWay; }
    public void setOneWay(boolean oneWay) { isOneWay = oneWay; }
}
