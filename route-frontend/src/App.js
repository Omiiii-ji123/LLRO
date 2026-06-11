import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Navbar from './components/Navbar';
import RouteOptimizer from './components/RouteOptimizer';
import BenchmarkChart from './components/BenchmarkChart';
import About from './components/About';
import './App.css';

function App() {
  return (
    <Router>
      <div className="app">
        <Navbar />
        <main className="main-content">
          <Routes>
            <Route path="/" element={<Navigate to="/optimize" />} />
            <Route path="/optimize" element={<RouteOptimizer />} />
            <Route path="/benchmark" element={<BenchmarkChart />} />
            <Route path="/about" element={<About />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;