package com.logistics.routeengine.modern.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RouteRequest {

    @NotEmpty(message = "Location list cannot be empty")
    private List<LocationDTO> locations;

    @Data
    public static class LocationDTO {
        private String name;
        private double latitude;
        private double longitude;
    }
}