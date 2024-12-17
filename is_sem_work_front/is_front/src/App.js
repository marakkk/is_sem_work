import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import LoginForm from './components/LoginForm/LoginForm';
import RegisterForm from './components/RegistrationForm/RegisterForm';

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
                {token && (
                    <header className="welcome-header">
                        <h2>Добро пожаловать, {storedUsername}!</h2>
                    </header>
                )}
                <Routes>
                    <Route path="/login" element={<LoginForm onLogin={setToken} />} />
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/" element={<Navigate to="/login" />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
