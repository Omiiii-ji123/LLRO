package com.logistics.routeengine.modern.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteResponse {

    private List<String> optimizedRoute;
    private double totalDistanceKm;
    private double executionTimeMs;
    private String algorithmUsed;
    private int totalLocations;
}