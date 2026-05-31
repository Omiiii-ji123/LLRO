package com.logistics.routeengine.modern.service;

import com.logistics.routeengine.modern.dto.RouteRequest;
import com.logistics.routeengine.modern.dto.RouteResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RouteOptimizationServiceTest {

    private RouteOptimizationService service;

    @BeforeEach
    void setUp() {
        service = new RouteOptimizationService();
    }

    @Test
    void testOptimizedRouteContainsAllLocations() {
        RouteRequest request = buildRequest(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"},
                {"Chennai", "13.08", "80.27"},
                {"Kolkata", "22.57", "88.36"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertNotNull(response);
        assertEquals(4, response.getTotalLocations());
        assertEquals(4, response.getOptimizedRoute().size());
        assertTrue(response.getOptimizedRoute().containsAll(
                List.of("Mumbai", "Delhi", "Chennai", "Kolkata")
        ));
    }

    @Test
    void testOptimizedRouteStartsFromFirstLocation() {
        RouteRequest request = buildRequest(new String[][]{
                {"Warehouse", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"},
                {"Chennai", "13.08", "80.27"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertEquals("Warehouse", response.getOptimizedRoute().get(0));
    }

    @Test
    void testTotalDistanceIsPositive() {
        RouteRequest request = buildRequest(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"},
                {"Chennai", "13.08", "80.27"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertTrue(response.getTotalDistanceKm() > 0);
    }

    @Test
    void testExecutionTimeIsPositive() {
        RouteRequest request = buildRequest(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertTrue(response.getExecutionTimeMs() >= 0);
    }

    @Test
    void testAlgorithmNameIsCorrect() {
        RouteRequest request = buildRequest(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertEquals("Nearest Neighbor Greedy", response.getAlgorithmUsed());
    }

    @Test
    void testSingleLocationRoute() {
        RouteRequest request = buildRequest(new String[][]{
                {"Mumbai", "19.07", "72.87"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertEquals(1, response.getTotalLocations());
        assertEquals(1, response.getOptimizedRoute().size());
        assertEquals(0.0, response.getTotalDistanceKm());
    }

    @Test
    void testTwoLocationRoute() {
        RouteRequest request = buildRequest(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"}
        });

        RouteResponse response = service.optimizeRoute(request);

        assertEquals(2, response.getTotalLocations());
        assertEquals(2, response.getOptimizedRoute().size());
        assertTrue(response.getTotalDistanceKm() > 0);
    }

    @Test
    void testLargeInputCompletesSuccessfully() {
        List<RouteRequest.LocationDTO> locations = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            RouteRequest.LocationDTO dto = new RouteRequest.LocationDTO();
            dto.setName("Location_" + i);
            dto.setLatitude(8.0 + (i * 0.2));
            dto.setLongitude(68.0 + (i * 0.2));
            locations.add(dto);
        }
        RouteRequest request = new RouteRequest();
        request.setLocations(locations);

        RouteResponse response = service.optimizeRoute(request);

        assertEquals(100, response.getTotalLocations());
        assertEquals(100, response.getOptimizedRoute().size());
    }

    // helper method
    private RouteRequest buildRequest(String[][] data) {
        List<RouteRequest.LocationDTO> locations = new ArrayList<>();
        for (String[] row : data) {
            RouteRequest.LocationDTO dto = new RouteRequest.LocationDTO();
            dto.setName(row[0]);
            dto.setLatitude(Double.parseDouble(row[1]));
            dto.setLongitude(Double.parseDouble(row[2]));
            locations.add(dto);
        }
        RouteRequest request = new RouteRequest();
        request.setLocations(locations);
        return request;
    }
}