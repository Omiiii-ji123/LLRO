import axios from 'axios';

const BASE_URL = 'http://localhost:8081/api';

const api = axios.create({
    baseURL: BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

export const optimizeRoute = async (locations) => {
    const response = await api.post('/routes/optimize-route', { locations });
    return response.data;
};

export const runSingleBenchmark = async (locationCount) => {
    const response = await api.get(`/benchmark/run/${locationCount}`);
    return response.data;
};

export const runScalabilityBenchmark = async () => {
    const response = await api.get('/benchmark/scalability');
    return response.data;
};

export const healthCheck = async () => {
    const response = await api.get('/routes/health');
    return response.data;
};