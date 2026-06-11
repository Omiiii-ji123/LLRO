import React, { useState } from 'react';
import { optimizeRoute } from '../services/api';

function RouteOptimizer() {
    const [locations, setLocations] = useState([
        { name: '', latitude: '', longitude: '' },
        { name: '', latitude: '', longitude: '' },
        { name: '', latitude: '', longitude: '' }
    ]);
    const [result, setResult] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

  const handleLocationChange = (index, field, value) => {
    const updated = [...locations];
    updated[index][field] = value;
    setLocations(updated);
};

const handleCityLookup = async (index, cityName) => {
    if (!cityName.trim()) return;
    try {
        const res = await fetch(
            `https://nominatim.openstreetmap.org/search?q=${encodeURIComponent(cityName)}&format=json&limit=1`,
            { headers: { 'Accept-Language': 'en' } }
        );
        const data = await res.json();
        if (data.length > 0) {
            const updated = [...locations];
            updated[index].latitude = parseFloat(data[0].lat).toFixed(4);
            updated[index].longitude = parseFloat(data[0].lon).toFixed(4);
            setLocations(updated);
        }
    } catch (err) {
        console.error('City lookup failed:', err);
    }
};

    const addLocation = () => {
        setLocations([...locations, { name: '', latitude: '', longitude: '' }]);
    };

    const removeLocation = (index) => {
        if (locations.length === 1) return;
        setLocations(locations.filter((_, i) => i !== index));
    };

    const handleSubmit = async () => {
        setError(null);
        setResult(null);

        const parsed = locations.map(loc => ({
            name: loc.name.trim(),
            latitude: parseFloat(loc.latitude),
            longitude: parseFloat(loc.longitude),
        }));

        const invalid = parsed.some(
            loc => !loc.name || isNaN(loc.latitude) || isNaN(loc.longitude)
        );

        if (invalid) {
            setError('Please fill in all fields with valid values.');
            return;
        }

        setLoading(true);
        try {
            const data = await optimizeRoute(parsed);
            setResult(data);
        } catch (err) {
            setError(err.response?.data?.message || 'Something went wrong. Is the backend running?');
        } finally {
            setLoading(false);
        }
    };

    const loadSampleData = () => {
        setLocations([
            { name: 'Warehouse', latitude: '19.07', longitude: '72.87' },
            { name: 'Delhi Hub', latitude: '28.61', longitude: '77.20' },
            { name: 'Chennai DC', latitude: '13.08', longitude: '80.27' },
            { name: 'Kolkata Center', latitude: '22.57', longitude: '88.36' },
            { name: 'Bangalore Node', latitude: '12.97', longitude: '77.59' },
        ]);
        setResult(null);
        setError(null);
    };

    return (
        <div className="page-container">
            <div className="page-header">
                <h1>Route Optimizer</h1>
                <p>Enter delivery locations to compute the most efficient route</p>
            </div>

            <div className="card">
                <div className="card-header">
                    <h2>Delivery Locations</h2>
                    <button className="btn btn-secondary" onClick={loadSampleData}>
                        Load Sample Data
                    </button>
                </div>

                <div className="locations-list">
                    {locations.map((loc, index) => (
                        <div key={index} className="location-row">
                            <span className="location-index">{index + 1}</span>
                           <input
                                className="input-field"
                                placeholder="Location name"
                                value={loc.name}
                                onChange={e => handleLocationChange(index, 'name', e.target.value)}
                                onBlur={e => handleCityLookup(index, e.target.value)}
                            />
                            <input
                                className="input-field input-small"
                                placeholder="Latitude"
                                value={loc.latitude}
                                onChange={e => handleLocationChange(index, 'latitude', e.target.value)}
                            />
                            <input
                                className="input-field input-small"
                                placeholder="Longitude"
                                value={loc.longitude}
                                onChange={e => handleLocationChange(index, 'longitude', e.target.value)}
                            />
                            <button
                                className="btn btn-danger"
                                onClick={() => removeLocation(index)}
                                disabled={locations.length === 1}
                            >
                                ✕
                            </button>
                        </div>
                    ))}
                </div>

                <div className="card-actions">
                    <button className="btn btn-secondary" onClick={addLocation}>
                        + Add Location
                    </button>
                    <button
                        className="btn btn-primary"
                        onClick={handleSubmit}
                        disabled={loading}
                    >
                        {loading ? 'Optimizing...' : 'Optimize Route'}
                    </button>
                </div>
            </div>

            {error && (
                <div className="error-box">
                    ⚠️ {error}
                </div>
            )}

            {result && (
                <div className="card result-card">
                    <div className="card-header">
                        <h2>Optimized Route</h2>
                    </div>

                <div className="route-stops-horizontal">
                    {result.optimizedRoute.map((stop, index) => (
        <React.Fragment key={index}>
            <div className="route-stop-h">
                <div className="stop-number">{index + 1}</div>
                <div className="stop-name">{stop}</div>
            </div>
            {index < result.optimizedRoute.length - 1 && (
                <div className="stop-arrow-h">→</div>
            )}
        </React.Fragment>
    ))}
</div>

                    <div className="result-stats">
                        <div className="stat">
                            <span className="stat-label">Total Distance</span>
                            <span className="stat-value">{result.totalDistanceKm} km</span>
                        </div>
                        <div className="stat">
    <span className="stat-label">Estimated Travel Time</span>
    <span className="stat-value">
        {(result.totalDistanceKm / 60).toFixed(1)} hrs
    </span>
</div>
                        <div className="stat">
                            <span className="stat-label">Total Locations</span>
                            <span className="stat-value">{result.totalLocations}</span>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default RouteOptimizer;