package com.logistics.routeengine.legacy;

import java.util.ArrayList;
import java.util.List;

public class LegacyRouteEngine {

    public static class Location {
        public String name;
        public double lat;
        public double lon;

        public Location(String name, double lat, double lon) {
            this.name = name;
            this.lat = lat;
            this.lon = lon;
        }
    }

    public static class RouteResult {
        public List<String> route;
        public double totalDistance;
        public long executionTimeNs;

        public RouteResult(List<String> route, double totalDistance, long executionTimeNs) {
            this.route = route;
            this.totalDistance = totalDistance;
            this.executionTimeNs = executionTimeNs;
        }
    }

    public RouteResult computeRoute(List<Location> locations) {
        long startTime = System.nanoTime();

        List<String> route = new ArrayList<>();
        double totalDistance = 0.0;

        for (int i = 0; i < locations.size(); i++) {
            route.add(locations.get(i).name);
            if (i < locations.size() - 1) {
                totalDistance += computeDistance(locations.get(i), locations.get(i + 1));
            }
        }

        long endTime = System.nanoTime();
        return new RouteResult(route, totalDistance, endTime - startTime);
    }

    private double computeDistance(Location a, Location b) {
        double latDiff = a.lat - b.lat;
        double lonDiff = a.lon - b.lon;
        double roughDistance = Math.sqrt((latDiff * latDiff) + (lonDiff * lonDiff));
        double distanceKm = roughDistance * 111.0;

        double penalty = 0;
        for (int i = 0; i < 1000; i++) {
            penalty += Math.random() * 0.0001;
        }

        return distanceKm + (penalty * 0.0000001);
    }
}