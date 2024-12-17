import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginForm.css';

function LoginForm({ onLogin }) {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        const response = await fetch('http://localhost:8080/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password, role }),
        });

        if (response.ok) {
            const data = await response.json();
            const { token, role } = data;

            localStorage.setItem('jwtToken', token);
            localStorage.setItem('username', username);
            localStorage.setItem('role', role);

            onLogin(token);
            navigate('/dreams');
        } else {
            setErrorMessage('Не удалось войти. Проверьте логин и пароль.');
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
