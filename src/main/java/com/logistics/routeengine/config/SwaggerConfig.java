package com.logistics.routeengine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Legacy Logistics Route Optimization Engine")
                        .version("1.0.0")
                        .description("""
        Legacy Logistics Route Optimization Modernization Engine is a 
        backend engineering project that demonstrates the real-world 
        process of identifying inefficiencies in a legacy routing system 
        and replacing them with a modern, optimized solution.
        
        The legacy module simulates common problems found in older 
        codebases — naive route computation, inaccurate distance 
        calculation, and poor scalability under increasing input load.
        
        The modern module addresses each of these with a Nearest Neighbor 
        Greedy optimization algorithm, Haversine formula for accurate 
        geographical distance computation, and a clean layered Spring Boot 
        architecture.
        
        A built-in benchmarking module runs both systems against identical 
        inputs and measures execution time and route efficiency across 
        multiple input sizes, providing concrete performance comparison data.
        
        Tech Stack: Java 21, Spring Boot 3.5, Spring Data JPA, MySQL, JUnit 5
        """)
                        .contact(new Contact()
                                .name("Omkar Deshpande")
                                .email("deshpande.omkar2007@gmail.com")));
    }
}