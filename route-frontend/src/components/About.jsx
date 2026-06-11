import React from 'react';

function About() {
    const techStack = [
        { name: 'Java 21', purpose: 'Core language' },
        { name: 'Spring Boot 3.5', purpose: 'Backend framework' },
        { name: 'Spring Data JPA', purpose: 'Database layer' },
        { name: 'MySQL 8.0', purpose: 'Persistence' },
        { name: 'JUnit 5', purpose: '30 unit tests' },
        { name: 'Swagger / OpenAPI', purpose: 'API documentation' },
        { name: 'React', purpose: 'Frontend' },
        { name: 'Recharts', purpose: 'Benchmark visualization' },
    ];

    const benchmarkData = [
        { locations: 5, legacyDist: '8,373', modernDist: '3,257', improvement: '61.1%' },
        { locations: 10, legacyDist: '14,478', modernDist: '6,280', improvement: '56.6%' },
        { locations: 25, legacyDist: '38,615', modernDist: '11,928', improvement: '69.1%' },
        { locations: 50, legacyDist: '72,881', modernDist: '20,167', improvement: '72.3%' },
        { locations: 100, legacyDist: '151,902', modernDist: '27,592', improvement: '81.8%' },
        { locations: 200, legacyDist: '319,163', modernDist: '38,604', improvement: '87.9%' },
    ];

    return (
        <div className="page-container">
            <div className="page-header">
                <h1>About This Project</h1>
                <p>A backend engineering portfolio project demonstrating legacy system modernization</p>
            </div>

            <div className="card">
                <h2>Problem Statement</h2>
                <p className="about-text">
                    Legacy logistics systems often compute delivery routes using naive approaches —
                    visiting locations in whatever order they arrive, using inaccurate distance
                    calculations, and containing years of accumulated inefficiencies that compound
                    as input size grows.
                </p>
                <p className="about-text">
                    This project simulates that exact scenario and modernizes it with a clean
                    Spring Boot architecture, an optimized routing algorithm, and accurate
                    geographical distance calculation — with measurable proof of improvement.
                </p>
            </div>

            <div className="about-grid">
                <div className="card">
                    <h2>⚠️ Legacy System</h2>
                    <ul className="about-list">
                        <li>Visits locations in input order — no optimization</li>
                        <li>Euclidean distance — treats Earth as flat</li>
                        <li>1,000 iteration waste loop per location pair</li>
                        <li>No separation of concerns</li>
                        <li>Public fields, no encapsulation</li>
                        <li>Untestable, unmaintainable design</li>
                    </ul>
                </div>

                <div className="card">
                    <h2>✅ Modern System</h2>
                    <ul className="about-list">
                        <li>Nearest Neighbor Greedy algorithm</li>
                        <li>Haversine formula for spherical distance</li>
                        <li>Clean layered Spring Boot architecture</li>
                        <li>Constructor injection, immutable dependencies</li>
                        <li>DTO-based API contracts</li>
                        <li>30 JUnit tests, fully verified</li>
                    </ul>
                </div>
            </div>

            <div className="card">
                <h2>Benchmark Results</h2>
                <p className="about-text">
                    Both systems tested with identical inputs using a fixed random seed
                    for deterministic, reproducible results.
                </p>
                <div className="table-wrapper">
                    <table className="results-table">
                        <thead>
                            <tr>
                                <th>Locations</th>
                                <th>Legacy Distance</th>
                                <th>Modern Distance</th>
                                <th>Improvement</th>
                            </tr>
                        </thead>
                        <tbody>
                            {benchmarkData.map((row, i) => (
                                <tr key={i}>
                                    <td>{row.locations}</td>
                                    <td className="legacy-val">{row.legacyDist} km</td>
                                    <td className="modern-val">{row.modernDist} km</td>
                                    <td className="improvement-val">{row.improvement}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            <div className="card">
                <h2>Tech Stack</h2>
                <div className="tech-grid">
                    {techStack.map((tech, i) => (
                        <div key={i} className="tech-card">
                            <span className="tech-name">{tech.name}</span>
                            <span className="tech-purpose">{tech.purpose}</span>
                        </div>
                    ))}
                </div>
            </div>

            <div className="card">
                <h2>Algorithm Design</h2>
                <div className="about-grid">
                    <div className="algo-box">
                        <h3>Nearest Neighbor Greedy</h3>
                        <ol className="about-list">
                            <li>Start at first location (depot)</li>
                            <li>Find closest unvisited location</li>
                            <li>Travel there, mark as visited</li>
                            <li>Repeat until all visited</li>
                        </ol>
                        <p className="algo-complexity">Time Complexity: O(n²)</p>
                    </div>
                    <div className="algo-box">
                        <h3>Haversine Formula</h3>
                        <ol className="about-list">
                            <li>Accounts for Earth's curvature</li>
                            <li>Uses spherical trigonometry</li>
                            <li>Accurate for large distances</li>
                            <li>Standard in mapping systems</li>
                        </ol>
                        <p className="algo-complexity">Earth Radius: 6,371 km</p>
                    </div>
                </div>
            </div>

        </div>
    );
}

export default About;