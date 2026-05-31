package com.logistics.routeengine.benchmark;

import com.logistics.routeengine.modern.service.RouteOptimizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BenchmarkRunnerTest {

    private BenchmarkRunner benchmarkRunner;

    @BeforeEach
    void setUp() {
        benchmarkRunner = new BenchmarkRunner(new RouteOptimizationService());
    }

    @Test
    void testBenchmarkResultIsNotNull() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(10);
        assertNotNull(result);
    }

    @Test
    void testBenchmarkLocationCountMatches() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(10);
        assertEquals(10, result.locationCount);
    }

    @Test
    void testLegacyTimeIsPositive() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(10);
        assertTrue(result.legacyTimeMs >= 0);
    }

    @Test
    void testModernTimeIsPositive() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(10);
        assertTrue(result.modernTimeMs >= 0);
    }

    @Test
    void testLegacyDistanceIsPositive() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(10);
        assertTrue(result.legacyDistanceKm > 0);
    }

    @Test
    void testModernDistanceIsPositive() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(10);
        assertTrue(result.modernDistanceKm > 0);
    }

    @Test
    void testModernRouteIsShorterThanLegacy() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(50);
        assertTrue(result.modernDistanceKm <= result.legacyDistanceKm,
                "Modern route should be shorter or equal to legacy route");
    }

    @Test
    void testDistanceImprovementIsNonNegative() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(50);
        assertTrue(result.distanceImprovementPercent >= 0,
                "Distance improvement should never be negative");
    }

    @Test
    void testScalabilityTestReturnsSixResults() {
        List<BenchmarkRunner.BenchmarkResult> results = benchmarkRunner.runScalabilityTest();
        assertEquals(6, results.size());
    }

    @Test
    void testScalabilityTestInputSizesAreCorrect() {
        List<BenchmarkRunner.BenchmarkResult> results = benchmarkRunner.runScalabilityTest();
        int[] expectedSizes = {5, 10, 25, 50, 100, 200};
        for (int i = 0; i < expectedSizes.length; i++) {
            assertEquals(expectedSizes[i], results.get(i).locationCount,
                    "Input size mismatch at index " + i);
        }
    }

    @Test
    void testScalabilityAllResultsHavePositiveDistances() {
        List<BenchmarkRunner.BenchmarkResult> results = benchmarkRunner.runScalabilityTest();
        for (BenchmarkRunner.BenchmarkResult result : results) {
            assertTrue(result.legacyDistanceKm > 0,
                    "Legacy distance should be positive for size: " + result.locationCount);
            assertTrue(result.modernDistanceKm > 0,
                    "Modern distance should be positive for size: " + result.locationCount);
        }
    }

    @Test
    void testSmallBenchmarkCompletesSuccessfully() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(5);
        assertNotNull(result);
        assertEquals(5, result.locationCount);
    }

    @Test
    void testLargeBenchmarkCompletesSuccessfully() {
        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(200);
        assertNotNull(result);
        assertEquals(200, result.locationCount);
    }
}