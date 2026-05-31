package com.logistics.routeengine.modern.controller;

import com.logistics.routeengine.modern.dto.RouteRequest;
import com.logistics.routeengine.modern.dto.RouteResponse;
import com.logistics.routeengine.modern.service.RouteOptimizationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {

    private final RouteOptimizationService routeOptimizationService;

    public RouteController(RouteOptimizationService routeOptimizationService) {
        this.routeOptimizationService = routeOptimizationService;
    }

    @PostMapping("/optimize-route")
    public ResponseEntity<RouteResponse> optimizeRoute(@Valid @RequestBody RouteRequest request) {
        RouteResponse response = routeOptimizationService.optimizeRoute(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Route Engine is running");
    }
}