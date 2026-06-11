import React from 'react';
import { NavLink } from 'react-router-dom';

function Navbar() {
    return (
        <nav className="navbar">
            <div className="navbar-brand">
                <span className="brand-icon">⚡</span>
                <span className="brand-text">Route Engine</span>
            </div>
            <div className="navbar-links">
                <NavLink
                    to="/optimize"
                    className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                >
                    Route Optimizer
                </NavLink>
                <NavLink
                    to="/benchmark"
                    className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                >
                    Benchmark
                </NavLink>
                <NavLink
                    to="/about"
                    className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
                >
                    About
                </NavLink>
            </div>
        </nav>
    );
}

export default Navbar;