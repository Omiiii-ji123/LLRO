package com.logistics.routeengine.benchmark;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/benchmark")
public class BenchmarkController {

    private final BenchmarkRunner benchmarkRunner;

    public BenchmarkController(BenchmarkRunner benchmarkRunner) {
        this.benchmarkRunner = benchmarkRunner;
    }

    @GetMapping("/run/{locationCount}")
    public ResponseEntity<BenchmarkRunner.BenchmarkResult> runSingle(
            @PathVariable int locationCount) {

        if (locationCount < 2 || locationCount > 500) {
            return ResponseEntity.badRequest().build();
        }

        BenchmarkRunner.BenchmarkResult result = benchmarkRunner.runBenchmark(locationCount);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/scalability")
    public ResponseEntity<List<BenchmarkRunner.BenchmarkResult>> runScalability() {
        List<BenchmarkRunner.BenchmarkResult> results = benchmarkRunner.runScalabilityTest();
        return ResponseEntity.ok(results);
    }
}