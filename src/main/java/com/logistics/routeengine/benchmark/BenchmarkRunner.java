package com.logistics.routeengine.benchmark;

import com.logistics.routeengine.legacy.LegacyRouteEngine;
import com.logistics.routeengine.modern.dto.RouteRequest;
import com.logistics.routeengine.modern.service.RouteOptimizationService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class BenchmarkRunner {

    private final RouteOptimizationService routeOptimizationService;

    public BenchmarkRunner(RouteOptimizationService routeOptimizationService) {
        this.routeOptimizationService = routeOptimizationService;
    }

    public BenchmarkResult runBenchmark(int locationCount) {

        // Generate random locations
        List<LegacyRouteEngine.Location> legacyLocations = generateLegacyLocations(locationCount);
        RouteRequest modernRequest = generateModernRequest(locationCount);

        // Run legacy
        LegacyRouteEngine legacyEngine = new LegacyRouteEngine();
        long legacyStart = System.nanoTime();
        LegacyRouteEngine.RouteResult legacyResult = legacyEngine.computeRoute(legacyLocations);
        long legacyEnd = System.nanoTime();
        double legacyTimeMs = (legacyEnd - legacyStart) / 1_000_000.0;

        // Run modern
        long modernStart = System.nanoTime();
        var modernResult = routeOptimizationService.optimizeRoute(modernRequest);
        long modernEnd = System.nanoTime();
        double modernTimeMs = (modernEnd - modernStart) / 1_000_000.0;

        // Calculate improvement
        double speedImprovementPercent = ((legacyTimeMs - modernTimeMs) / legacyTimeMs) * 100;
        double distanceImprovementPercent = ((legacyResult.totalDistance - modernResult.getTotalDistanceKm())
                / legacyResult.totalDistance) * 100;

        return new BenchmarkResult(
                locationCount,
                legacyTimeMs,
                modernTimeMs,
                speedImprovementPercent,
                legacyResult.totalDistance,
                modernResult.getTotalDistanceKm(),
                distanceImprovementPercent
        );
    }

    public List<BenchmarkResult> runScalabilityTest() {
        List<BenchmarkResult> results = new ArrayList<>();
        int[] sizes = {5, 10, 25, 50, 100, 200};
        for (int size : sizes) {
            results.add(runBenchmark(size));
        }
        return results;
    }

    private List<LegacyRouteEngine.Location> generateLegacyLocations(int count) {
        List<LegacyRouteEngine.Location> locations = new ArrayList<>();
        Random random = new Random(42);
        for (int i = 0; i < count; i++) {
            double lat = 8.0 + random.nextDouble() * 30.0;
            double lon = 68.0 + random.nextDouble() * 30.0;
            locations.add(new LegacyRouteEngine.Location("Location_" + i, lat, lon));
        }
        return locations;
    }

    private RouteRequest generateModernRequest(int count) {
        RouteRequest request = new RouteRequest();
        List<RouteRequest.LocationDTO> locations = new ArrayList<>();
        Random random = new Random(42);
        for (int i = 0; i < count; i++) {
            double lat = 8.0 + random.nextDouble() * 30.0;
            double lon = 68.0 + random.nextDouble() * 30.0;
            RouteRequest.LocationDTO dto = new RouteRequest.LocationDTO();
            dto.setName("Location_" + i);
            dto.setLatitude(lat);
            dto.setLongitude(lon);
            locations.add(dto);
        }
        request.setLocations(locations);
        return request;
    }

    // Inner result class
    public static class BenchmarkResult {
        public int locationCount;
        public double legacyTimeMs;
        public double modernTimeMs;
        public double speedImprovementPercent;
        public double legacyDistanceKm;
        public double modernDistanceKm;
        public double distanceImprovementPercent;

        public BenchmarkResult(int locationCount, double legacyTimeMs, double modernTimeMs,
                               double speedImprovementPercent, double legacyDistanceKm,
                               double modernDistanceKm, double distanceImprovementPercent) {
            this.locationCount = locationCount;
            this.legacyTimeMs = legacyTimeMs;
            this.modernTimeMs = modernTimeMs;
            this.speedImprovementPercent = speedImprovementPercent;
            this.legacyDistanceKm = legacyDistanceKm;
            this.modernDistanceKm = modernDistanceKm;
            this.distanceImprovementPercent = distanceImprovementPercent;
        }
    }
}