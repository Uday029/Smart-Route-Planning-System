package com.routeplanner.manager;

import com.routeplanner.dsa.RouteResult;

public class TollFuelCalculator {

    // Assuming average toll is ₹1.5 per km for highways. 
    // In a real app, this would be fetched from the TollCharges table based on Road IDs.
    public static double calculateTotalToll(RouteResult route) {
        if (route == null) return 0.0;
        
        // Simplified mock calculation based on total distance
        double distance = route.getTotalDistance();
        
        // Let's say 70% of the distance is tolled highway
        double tolledDistance = distance * 0.70;
        return tolledDistance * 1.5; 
    }

    public static double calculateFuelRequired(RouteResult route, double vehicleMileage) {
        if (route == null || vehicleMileage <= 0) return 0.0;
        return route.getTotalDistance() / vehicleMileage;
    }

    public static double calculateFuelCost(double fuelRequired, double fuelPricePerLitre) {
        return fuelRequired * fuelPricePerLitre;
    }

    public static void displayJourneyCost(RouteResult route, double mileage, double fuelPrice) {
        if (route == null) {
            System.out.println("No route to calculate cost.");
            return;
        }

        double totalToll = calculateTotalToll(route);
        double fuelReq = calculateFuelRequired(route, mileage);
        double fuelCost = calculateFuelCost(fuelReq, fuelPrice);
        double totalCost = totalToll + fuelCost;

        System.out.println("--- Journey Cost Estimator ---");
        System.out.printf("Distance: %.2f KM\n", route.getTotalDistance());
        System.out.printf("Mileage: %.2f km/L\n", mileage);
        System.out.printf("Fuel Required: %.2f L\n", fuelReq);
        System.out.printf("Fuel Cost: ₹%.2f\n", fuelCost);
        System.out.printf("Total Toll: ₹%.2f\n", totalToll);
        System.out.printf("Total Journey Cost: ₹%.2f\n", totalCost);
        System.out.println("------------------------------");
    }
}
