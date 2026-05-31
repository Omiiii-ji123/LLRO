package com.logistics.routeengine.modern.service;

import com.logistics.routeengine.modern.dto.RouteRequest;
import com.logistics.routeengine.modern.dto.RouteResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteOptimizationService {

    public RouteResponse optimizeRoute(RouteRequest request) {
        long startTime = System.nanoTime();

        List<RouteRequest.LocationDTO> locations = request.getLocations();
        List<RouteRequest.LocationDTO> unvisited = new ArrayList<>(locations);
        List<String> optimizedRoute = new ArrayList<>();

        RouteRequest.LocationDTO current = unvisited.remove(0);
        optimizedRoute.add(current.getName());
        double totalDistance = 0.0;

        while (!unvisited.isEmpty()) {
            RouteRequest.LocationDTO nearest = findNearest(current, unvisited);
            totalDistance += haversineDistance(
                    current.getLatitude(), current.getLongitude(),
                    nearest.getLatitude(), nearest.getLongitude()
            );
            optimizedRoute.add(nearest.getName());
            unvisited.remove(nearest);
            current = nearest;
        }

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new RouteResponse(
                optimizedRoute, Math.round(totalDistance * 100.0) / 100.0, executionTimeMs, "Nearest Neighbor Greedy", locations.size()
        );
    }

    private RouteRequest.LocationDTO findNearest(
            RouteRequest.LocationDTO current,
            List<RouteRequest.LocationDTO> unvisited) {

        RouteRequest.LocationDTO nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (RouteRequest.LocationDTO candidate : unvisited) {
            double distance = haversineDistance(current.getLatitude(), current.getLongitude(), candidate.getLatitude(), candidate.getLongitude()
            );
            if (distance < minDistance) {
                minDistance = distance;
                nearest = candidate;
            }
        }

        return nearest;
    }

    private double haversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }
}