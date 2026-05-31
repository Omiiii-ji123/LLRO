# Legacy Logistics Route Optimization Modernization Engine

A backend engineering project that demonstrates the real-world process of identifying inefficiencies in a legacy routing system and replacing them with a modern, optimized solution — with measurable proof.

Built with Java 21, Spring Boot 3.5, Spring Data JPA, MySQL, and JUnit 5.

---

## Problem Statement

Legacy logistics systems often compute delivery routes using naive approaches — visiting locations in whatever order they arrive, using inaccurate distance calculations, and containing years of accumulated inefficiencies that compound as input size grows.

This project simulates that exact scenario and modernizes it:

- The **legacy module** represents how older systems handled route computation — no optimization, flat-earth distance math, and a wasteful computation pattern that scales poorly.
- The **modern module** replaces it with a clean Spring Boot architecture, an optimized routing algorithm, and accurate geographical distance calculation.
- The **benchmark module** runs both systems against identical inputs and produces concrete performance comparison data.

---

## Architecture

```
com.logistics.routeengine
├── legacy
│   └── LegacyRouteEngine.java            # Naive route computation engine
├── modern
│   ├── controller
│   │   └── RouteController.java          # REST API endpoints
│   ├── service
│   │   └── RouteOptimizationService.java # Nearest Neighbor Greedy algorithm
│   ├── repository
│   │   └── LocationRepository.java       # Spring Data JPA repository
│   ├── model
│   │   └── Location.java                 # JPA entity
│   └── dto
│       ├── RouteRequest.java             # API input
│       └── RouteResponse.java            # API output
├── benchmark
│   ├── BenchmarkRunner.java              # Head-to-head comparison logic
│   └── BenchmarkController.java          # Benchmark REST endpoints
├── exception
│   └── GlobalExceptionHandler.java       # Centralized error handling
└── config
    └── SwaggerConfig.java                # OpenAPI documentation
```

### Request Flow

```
HTTP Request
     ↓
RouteController        → validates input with @Valid
     ↓
RouteOptimizationService → runs Nearest Neighbor Greedy algorithm
     ↓
RouteResponse          → returns optimized route as JSON
```

---

## Legacy System — What Was Wrong

### 1. No Route Optimization
The legacy engine visited locations in exact input order with no consideration for geography. Sending `[Mumbai, Delhi, Chennai]` would route Mumbai → Delhi → Chennai even if Mumbai → Chennai → Delhi was significantly shorter.

### 2. Inaccurate Distance Calculation
Distance was computed using Euclidean geometry — treating Earth as flat. This produces significant errors over large distances. Mumbai to Delhi is approximately 1,400 km — flat-earth math introduces meaningful inaccuracy at this scale.

### 3. Wasteful Computation
Every distance calculation ran an unnecessary loop of 1,000 iterations, adding computational overhead that compounded with every location pair. At 200 locations this produced 199,000 wasted iterations per route computation.

### 4. Poor Code Design
No separation of concerns, public fields with no encapsulation, all logic in a single class with no layering or testability.

---

## Modern System — What Changed

### 1. Nearest Neighbor Greedy Algorithm
```
1. Start at the first location (depot/warehouse)
2. Find the closest unvisited location
3. Travel there, mark as visited
4. Repeat until all locations are visited
```
This produces routes that are measurably shorter than random-order traversal, especially at larger input sizes.

### 2. Haversine Formula
Replaced flat-earth Euclidean distance with the Haversine formula — the standard for calculating distances between GPS coordinates on a sphere. Accurate for real geographic distances across India and globally.

```java
final double R = 6371.0; // Earth's radius in km
double a = Math.sin(dLat/2) * Math.sin(dLat/2)
         + Math.cos(lat1) * Math.cos(lat2)
         * Math.sin(dLon/2) * Math.sin(dLon/2);
return R * 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
```

### 3. Clean Layered Architecture
Controller → Service → Repository separation with constructor injection, DTO-based API contracts, and global exception handling.

---

## Algorithm Analysis

| Property | Legacy | Modern |
|---|---|---|
| Algorithm | Linear scan (no optimization) | Nearest Neighbor Greedy |
| Complexity | O(1000n) effective | O(n²) |
| Distance Formula | Euclidean (flat earth) | Haversine (spherical) |
| Route Quality | Input order, unoptimized | Geographically optimized |

The legacy system appears O(n) but contains a hidden O(1000) constant inside each distance calculation, making it effectively O(1000n). The modern system is genuinely O(n²) but each operation is a lightweight mathematical calculation. At practical input sizes modern is faster in both computation time and route quality.

---

## Benchmark Results

Both systems were tested with identical randomly generated coordinates covering India's geographic bounds (lat: 8–38, lon: 68–98) using a fixed random seed (42) for deterministic, reproducible results.

| Locations | Legacy Time | Modern Time | Speed Improvement | Legacy Distance | Modern Distance | Distance Improvement |
|---|---|---|---|---|---|---|
| 5 | 0.14ms | 0.02ms | 86.3% | 8,373 km | 3,257 km | 61.1% |
| 10 | 0.35ms | 0.07ms | 79.6% | 14,478 km | 6,280 km | 56.6% |
| 25 | 0.65ms | 0.06ms | 90.7% | 38,615 km | 11,928 km | 69.1% |
| 50 | 1.14ms | 0.20ms | 82.6% | 72,881 km | 20,167 km | 72.3% |
| 100 | 2.22ms | 0.49ms | 77.8% | 151,902 km | 27,592 km | 81.8% |
| 200 | 6.17ms | 3.39ms | 45.1% | 319,163 km | 38,604 km | 87.9% |

**Key finding:** At 200 delivery locations the modern system produces routes that are 87.9% shorter than the legacy system. The distance improvement grows consistently with input size — from 61% at 5 locations to 88% at 200 locations — demonstrating that the Nearest Neighbor algorithm's advantage compounds as the optimization space increases.

---

## API Endpoints

### Route Optimization
```
POST /api/routes/optimize-route
```
Request:
```json
{
  "locations": [
    { "name": "Warehouse", "latitude": 19.07, "longitude": 72.87 },
    { "name": "Delhi Hub", "latitude": 28.61, "longitude": 77.20 },
    { "name": "Chennai DC", "latitude": 13.08, "longitude": 80.27 }
  ]
}
```
Response:
```json
{
  "optimizedRoute": ["Warehouse", "Chennai DC", "Delhi Hub"],
  "totalDistanceKm": 3798.11,
  "executionTimeMs": 1.26,
  "algorithmUsed": "Nearest Neighbor Greedy",
  "totalLocations": 3
}
```

### Benchmark
```
GET /api/benchmark/run/{locationCount}    # Single benchmark
GET /api/benchmark/scalability            # Full scalability test (5–200 locations)
```

### Health
```
GET /api/routes/health
```

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Core language |
| Spring Boot | 3.5.14 | Backend framework |
| Spring Data JPA | — | Database layer |
| MySQL | 8.0.44 | Persistence |
| Lombok | — | Boilerplate reduction |
| JUnit 5 | — | Unit testing |
| Swagger / OpenAPI | 2.8.8 | API documentation |

---

## Running the Project

### Prerequisites
- Java 21
- Maven 3.9+
- MySQL 8.0+

### Setup

1. Clone the repository:
```bash
git clone https://github.com/Omiiii-ji123/LLRO.git
cd LLRO
```

2. Create the database:
```sql
CREATE DATABASE logistics_db;
```

3. Configure `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/logistics_db
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

4. Run the application:
```bash
mvn spring-boot:run
```

5. Access Swagger UI:
```
http://localhost:8081/swagger-ui.html
```

---

## Testing

```bash
mvn test
```

30 unit tests across three test classes:

| Test Class | Tests | Coverage |
|---|---|---|
| RouteOptimizationServiceTest | 8 | Algorithm correctness, edge cases, large input |
| LegacyRouteEngineTest | 8 | Legacy behavior, input order preservation, edge cases |
| BenchmarkRunnerTest | 14 | Benchmark validity, scalability, comparison correctness |

---

## Key Engineering Decisions

**Why Nearest Neighbor Greedy over Dijkstra:**
Dijkstra finds the shortest path between two fixed points. This problem requires visiting all locations — a variant of the Travelling Salesman Problem. Nearest Neighbor Greedy is the appropriate algorithm class for this use case, practical to implement, and produces measurably better results than random traversal.

**Why Haversine over Euclidean distance:**
Euclidean distance treats Earth as flat. For coordinates spanning hundreds or thousands of kilometers the error is significant. Haversine accounts for Earth's curvature and is the standard formula used in mapping and logistics systems.

**Why constructor injection over @Autowired:**
Constructor injection makes dependencies explicit, allows fields to be final (immutable), and makes unit testing straightforward without needing a Spring context.

**Why DTOs over direct entity exposure:**
Separating API contracts from database entities prevents tight coupling between persistence layer changes and API consumers, and avoids accidental exposure of database internals.

---

## Future Extensibility

- **2-Opt Algorithm:** Post-processing step that improves Nearest Neighbor output by swapping route segments — would further reduce total distance
- **Strategy Pattern:** Pluggable algorithm selection via API parameter
- **Route History Persistence:** Save and retrieve past optimization results
- **React Frontend:** Visualize routes on a map with benchmark comparison charts
- **Caching:** Cache repeated identical requests with Spring Cache

---


