import React, { useState } from 'react';
import {
    BarChart, Bar, XAxis, YAxis, CartesianGrid,
    Tooltip, Legend, ResponsiveContainer
} from 'recharts';
import { runScalabilityBenchmark } from '../services/api';

function BenchmarkChart() {
    const [results, setResults] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [activeChart, setActiveChart] = useState('time');

    const runBenchmark = async () => {
        setLoading(true);
        setError(null);
        setResults(null);
        try {
            const data = await runScalabilityBenchmark();
            setResults(data);
        } catch (err) {
            setError('Failed to run benchmark. Is the backend running?');
        } finally {
            setLoading(false);
        }
    };

    const timeChartData = results?.map(r => ({
        locations: `${r.locationCount} locs`,
        'Legacy (ms)': parseFloat(r.legacyTimeMs.toFixed(3)),
        'Modern (ms)': parseFloat(r.modernTimeMs.toFixed(3)),
    }));

    const distanceChartData = results?.map(r => ({
        locations: `${r.locationCount} locs`,
        'Legacy (km)': parseFloat(r.legacyDistanceKm.toFixed(0)),
        'Modern (km)': parseFloat(r.modernDistanceKm.toFixed(0)),
    }));

    return (
        <div className="page-container">
            <div className="page-header">
                <h1>Benchmark Comparison</h1>
                <p>Head-to-head performance comparison between legacy and modern routing systems</p>
            </div>

            <div className="card">
                <div className="card-header">
                    <h2>Scalability Test</h2>
                    <button
                        className="btn btn-primary"
                        onClick={runBenchmark}
                        disabled={loading}
                    >
                        {loading ? 'Running Benchmark...' : 'Run Benchmark'}
                    </button>
                </div>
                <p className="card-description">
                    Runs both systems against identical inputs at 6 different sizes (5 to 200 locations)
                    and measures execution time and route distance for each.
                </p>
            </div>

            {error && (
                <div className="error-box">⚠️ {error}</div>
            )}

            {results && (
                <>
                    <div className="chart-toggle">
                        <button
                            className={activeChart === 'time' ? 'toggle-btn active' : 'toggle-btn'}
                            onClick={() => setActiveChart('time')}
                        >
                            Execution Time
                        </button>
                        <button
                            className={activeChart === 'distance' ? 'toggle-btn active' : 'toggle-btn'}
                            onClick={() => setActiveChart('distance')}
                        >
                            Route Distance
                        </button>
                    </div>

                    <div className="card">
                        <h2>
                            {activeChart === 'time'
                                ? 'Execution Time: Legacy vs Modern (ms)'
                                : 'Route Distance: Legacy vs Modern (km)'}
                        </h2>
                        <ResponsiveContainer width="100%" height={350}>
                            <BarChart
                                data={activeChart === 'time' ? timeChartData : distanceChartData}
                                margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
                            >
                                <CartesianGrid strokeDasharray="3 3" stroke="#2a2a2a" />
                                <XAxis dataKey="locations" stroke="#888" />
                                <YAxis stroke="#888" />
                                <Tooltip
                                    contentStyle={{
                                        backgroundColor: '#1a1a1a',
                                        border: '1px solid #333',
                                        borderRadius: '8px'
                                    }}
                                />
                                <Legend />
                                <Bar
                                    dataKey={activeChart === 'time' ? 'Legacy (ms)' : 'Legacy (km)'}
                                    fill="#ef4444"
                                    radius={[4, 4, 0, 0]}
                                />
                                <Bar
                                    dataKey={activeChart === 'time' ? 'Modern (ms)' : 'Modern (km)'}
                                    fill="#22c55e"
                                    radius={[4, 4, 0, 0]}
                                />
                            </BarChart>
                        </ResponsiveContainer>
                    </div>

                    <div className="card">
                        <h2>Detailed Results</h2>
                        <div className="table-wrapper">
                            <table className="results-table">
                                <thead>
                                    <tr>
                                        <th>Locations</th>
                                        <th>Legacy Time</th>
                                        <th>Modern Time</th>
                                        <th>Speed Improvement</th>
                                        <th>Legacy Distance</th>
                                        <th>Modern Distance</th>
                                        <th>Distance Improvement</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {results.map((r, i) => (
                                        <tr key={i}>
                                            <td>{r.locationCount}</td>
                                            <td className="legacy-val">{r.legacyTimeMs.toFixed(3)} ms</td>
                                            <td className="modern-val">{r.modernTimeMs.toFixed(3)} ms</td>
                                            <td className="improvement-val">
                                                {r.speedImprovementPercent.toFixed(1)}%
                                            </td>
                                            <td className="legacy-val">
                                                {r.legacyDistanceKm.toFixed(0)} km
                                            </td>
                                            <td className="modern-val">
                                                {r.modernDistanceKm.toFixed(0)} km
                                            </td>
                                            <td className="improvement-val">
                                                {r.distanceImprovementPercent.toFixed(1)}%
                                            </td>
                                        </tr>
                                    ))}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
}

export default BenchmarkChart;