import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './styles/HomePage.css';

function HomePage() {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    return (
        <div className="home-container">
            <button onClick={handleLogout} className="logout-button">
                Выйти
            </button>
        </div>
    );
}

export default HomePage;
