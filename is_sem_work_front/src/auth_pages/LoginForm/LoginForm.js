import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginForm.css';

function LoginForm({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch('http://localhost:8080/api/auth/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                const data = await response.json();
                const { token, role } = data;

                // Skip status check for CUSTOMER role
                if (role === 'ADMIN' || role === 'ARCHITECT') {
                    const statusResponse = await fetch(`http://localhost:8080/api/auth/status?username=${username}`, {
                        method: 'GET',
                        headers: { 'Authorization': `Bearer ${token}` },
                    });

                    if (statusResponse.ok) {
                        const statusData = await statusResponse.json();
                        const { status } = statusData;

                        if (status !== 'APPROVED') {
                            setErrorMessage('Your account is not approved yet.');
                            return;
                        }
                    } else {
                        setErrorMessage('Failed to fetch user status.');
                        return;
                    }
                }

                // Save token, username, and role to localStorage
                localStorage.setItem('token', token);
                localStorage.setItem('username', username);
                localStorage.setItem('role', role);

                // Call the onLogin callback
                onLogin(token);

                // Redirect based on role
                if (role === 'ADMIN') {
                    navigate('/dreams/admin-page');
                } else if (role === 'CUSTOMER') {
                    navigate('/dreams/home-page');
                } else if (role === 'ARCHITECT') {
                    navigate('/dreams/architect-page');
                }
            } else {
                setErrorMessage('Не удалось войти. Проверьте логин и пароль.');
            }
        } catch (error) {
            console.error('Error during login:', error);
            setErrorMessage('An error occurred. Please try again.');
        }
    };

    return (
        <div className="main-container">
            <div className="welcome-header">
                <h2>DreamLand Booking Service</h2>
            </div>
            <form onSubmit={handleSubmit} className="login-form">
                <div>
                    <input
                        placeholder="Логин"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </div>
                <div>
                    <input
                        placeholder="Пароль"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />
                </div>
                {errorMessage && <p className="error-message">{errorMessage}</p>}
                <button type="submit">Войти</button>
                <p className="account-message">Нет аккаунта? <a href="/register">Зарегистрироваться</a></p>
            </form>
        </div>
    );
}

export default LoginForm;