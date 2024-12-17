import React, { useState } from 'react';
import './RegistrationForm.css';

function RegisterForm() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [role, setRole] = useState('');
    const [passwordError, setPasswordError] = useState('');
    const [errorMessage, setErrorMessage] = useState('');
    const [roleError, setRoleError] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();
        setPasswordError('');
        setErrorMessage('');
        setRoleError('');

        if (password.length < 10) {
            setPasswordError('Пароль должен быть минимум из 10 символов.');
            return;
        }

        if (!role) {
            setRoleError('Пожалуйста, выберите роль.');
            return;
        }

        const userData = { username, password, role };

        try {
            const response = await fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (response.ok) {
                console.info('Регистрация успешна!');
                setUsername('');
                setPassword('');
                setRole('');
            } else {
                const errorData = await response.json();
                setErrorMessage(errorData.message || 'Ошибка регистрации.');
            }
        } catch (error) {
            console.error('Ошибка при регистрации:', error);
            setErrorMessage('Ошибка при регистрации.');
        }
    };

    const handleRoleChange = (e) => {
        setRole(e.target.value);
        setRoleError('');
    };

    const handleUsernameChange = (e) => {
        setUsername(e.target.value);
        setErrorMessage('');
    };

    const handlePasswordChange = (e) => {
        setPassword(e.target.value);
        setPasswordError('');
    };

    const isSubmitDisabled = !role || passwordError || errorMessage;

    return (
        <div className="main-container">
            <div className="welcome-header">
                <h2>DreamLand Booking Service</h2>
            </div>
            <div className="form-container">
                <form onSubmit={handleSubmit}>
                    <div>
                        <input
                            placeholder="Логин"
                            value={username}
                            onChange={handleUsernameChange}
                        />
                    </div>
                    <div>
                        <input
                            placeholder="Пароль"
                            type="password"
                            value={password}
                            onChange={handlePasswordChange}
                        />
                        {passwordError && <p className="error-message">{passwordError}</p>}
                    </div>

                    <div>
                        <select
                            value={role}
                            onChange={handleRoleChange}
                        >
                            <option value="">Выберите роль</option>
                            <option value="CUSTOMER">Обычный пользователь</option>
                            <option value="ADMIN">Администратор</option>
                            <option value="ARCHITECT">Архитектор снов</option>
                        </select>
                        {roleError && <p className="error-message">{roleError}</p>}
                    </div>

                    {errorMessage && <p className="error-message">{errorMessage}</p>}

                    <button type="submit" disabled={isSubmitDisabled}>Зарегистрироваться</button>

                    {isSubmitDisabled && (
                        <div className="error-summary">
                            {!role && <p className="error-message">Выберите роль перед отправкой формы</p>}
                        </div>
                    )}

                    <p className="account-message">Уже есть аккаунт? <a href="/login">Войти</a></p>
                </form>
            </div>
        </div>
    );
}

export default RegisterForm;
