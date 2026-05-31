package com.logistics.routeengine.legacy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LegacyRouteEngineTest {

    private LegacyRouteEngine legacyRouteEngine;

    @BeforeEach
    void setUp() {
        legacyRouteEngine = new LegacyRouteEngine();
    }

    @Test
    void testRouteContainsAllLocations() {
        List<LegacyRouteEngine.Location> locations = buildLocations(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"},
                {"Chennai", "13.08", "80.27"}
        });

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertNotNull(result);
        assertEquals(3, result.route.size());
        assertTrue(result.route.containsAll(List.of("Mumbai", "Delhi", "Chennai")));
    }

    @Test
    void testRoutePreservesInputOrder() {
        List<LegacyRouteEngine.Location> locations = buildLocations(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"},
                {"Chennai", "13.08", "80.27"}
        });

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertEquals("Mumbai", result.route.get(0));
        assertEquals("Delhi", result.route.get(1));
        assertEquals("Chennai", result.route.get(2));
    }

    @Test
    void testTotalDistanceIsPositive() {
        List<LegacyRouteEngine.Location> locations = buildLocations(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"}
        });

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertTrue(result.totalDistance > 0);
    }

    @Test
    void testExecutionTimeIsRecorded() {
        List<LegacyRouteEngine.Location> locations = buildLocations(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"}
        });

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertTrue(result.executionTimeNs > 0);
    }

    @Test
    void testSingleLocationRoute() {
        List<LegacyRouteEngine.Location> locations = buildLocations(new String[][]{
                {"Mumbai", "19.07", "72.87"}
        });

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertEquals(1, result.route.size());
        assertEquals(0.0, result.totalDistance);
    }

    @Test
    void testEmptyLocationList() {
        List<LegacyRouteEngine.Location> locations = new ArrayList<>();

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertNotNull(result);
        assertEquals(0, result.route.size());
        assertEquals(0.0, result.totalDistance);
    }

    @Test
    void testLargeInputCompletesSuccessfully() {
        List<LegacyRouteEngine.Location> locations = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            locations.add(new LegacyRouteEngine.Location(
                    "Location_" + i,
                    8.0 + (i * 0.2),
                    68.0 + (i * 0.2)
            ));
        }

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertEquals(100, result.route.size());
        assertTrue(result.totalDistance > 0);
        assertTrue(result.executionTimeNs > 0);
    }

    @Test
    void testTwoLocationRoute() {
        List<LegacyRouteEngine.Location> locations = buildLocations(new String[][]{
                {"Mumbai", "19.07", "72.87"},
                {"Delhi", "28.61", "77.20"}
        });

        LegacyRouteEngine.RouteResult result = legacyRouteEngine.computeRoute(locations);

        assertEquals(2, result.route.size());
        assertTrue(result.totalDistance > 0);
    }

    private List<LegacyRouteEngine.Location> buildLocations(String[][] data) {
        List<LegacyRouteEngine.Location> locations = new ArrayList<>();
        for (String[] row : data) {
            locations.add(new LegacyRouteEngine.Location(
                    row[0],
                    Double.parseDouble(row[1]),
                    Double.parseDouble(row[2])
            ));
        }
        return locations;
    }
}