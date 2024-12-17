import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import LoginForm from './auth_pages/LoginForm/LoginForm';
import RegisterForm from './auth_pages/RegistrationForm/RegisterForm';
import AdminPage from './users_pages/AdminPage'
import HomePage from './users_pages/HomePage'
import ArchitectPage from './users_pages/ArchitectPage'



function App() {
    const [token, setToken] = useState(null);
    const [username, setUsername] = useState('');
    const storedUsername = sessionStorage.getItem('username');

    useEffect(() => {
        if (storedUsername) {
            setUsername(storedUsername);
        }
    }, []);


    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/login" element={<LoginForm onLogin={setToken} />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/" element={<Navigate to="/login" />} />
                    <Route path="/admin-page" element={<AdminPage />} />
                    <Route path="/home-page" element={<HomePage />} />
                    <Route path="/architect-page" element={<ArchitectPage />} />


                </Routes>
            </div>
        </Router>
    );
}

export default App;
