import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
    const [reservations, setReservations] = useState([]);
    const [userName, setUserName] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const navigate = useNavigate();

    const token = localStorage.getItem('token');

    useEffect(() => {
        if (!token) {
            navigate('/login');
        } else {
            const payload = token.split('.')[1];
            const decodedPayload = JSON.parse(atob(payload));
            setUserName(decodedPayload.username); // Assuming the username is stored in the JWT payload
        }
    }, [token, navigate]);

    const handleLogout = () => {
        localStorage.clear();
        navigate('/login');
    };

    const handleCreateReservation = () => {
        setIsModalOpen(true);
    };

    const handleLeaveReview = () => {
        navigate('/leave-review');
    };

    const handleCreateOwnDream = () => {
        setIsModalOpen(false);
        navigate('/dreams/create-own-dream');
    };

    const handleSelectTemplate = () => {
        setIsModalOpen(false);
        navigate('/dreams/templates');
    };

    useEffect(() => {
        const fetchReservations = async () => {
            const response = await fetch('http://localhost:8080/api/reservations/history', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setReservations(data); // Set reservations from the response
            } else {
                console.error('Failed to fetch reservations');
            }
        };

        fetchReservations();
    }, [token]);

    return (
        <div className="home-container">
            <div className="header">
                <h1>Добро пожаловать в DreamLand</h1>
                <button onClick={handleLogout} className="logout-button">Выйти</button>
            </div>

            <div className="main-content">
                <button onClick={handleCreateReservation} className="action-button">Создать бронь</button>

                <div className="leave-review">
                    <button className="action-button" onClick={handleLeaveReview}>Оставить отзыв</button>
                </div>

                <div className="reservations-block">
                    <h2>История бронирований</h2>
                    <div className="reservations-table-container">
                        {reservations.length > 0 ? (
                            <table className="reservations-table">
                                <thead>
                                <tr>
                                    <th>Название мечты</th>
                                    <th>Эра времени</th>
                                    <th>Виртуальная среда</th>
                                    <th>Специальные способности</th>
                                    <th>Физические правила</th>
                                    <th>Роль</th>
                                    <th>Жанр</th>
                                    <th>Сценарий</th>
                                    <th>Цена</th>
                                    <th>Архитектор</th>
                                    <th>Дата</th>
                                    <th>Время</th>
                                    <th>Статус</th>
                                    <th>Время бронирования</th>
                                </tr>
                                </thead>
                                <tbody>
                                {reservations.map((reservation) => (
                                    <tr key={reservation.timeOfReservation}>
                                        <td>{reservation.dreamName}</td>
                                        <td>{reservation.timeEra}</td>
                                        <td>{reservation.virtualEnvironment}</td>
                                        <td>{reservation.specialPowers || 'Нет'}</td>
                                        <td>{reservation.physicalRules || 'Нет'}</td>
                                        <td>{reservation.role}</td>
                                        <td>{reservation.genre}</td>
                                        <td>{reservation.scenario || 'Нет'}</td>
                                        <td>{reservation.price}</td>
                                        <td>{reservation.architectUsername}</td>
                                        <td>{reservation.date}</td>
                                        <td>{reservation.time}</td>
                                        <td>{reservation.status}</td>
                                        <td>{new Date(reservation.timeOfReservation).toLocaleString()}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <p>Бронирования отсутствуют</p>
                        )}
                    </div>
                </div>
            </div>

            {isModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Выберите действие</h2>
                        <button onClick={handleCreateOwnDream}>Создать свой сон</button>
                        <button onClick={handleSelectTemplate}>Выбрать из шаблонов</button>
                        <button onClick={() => setIsModalOpen(false)}>Закрыть</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default HomePage;
