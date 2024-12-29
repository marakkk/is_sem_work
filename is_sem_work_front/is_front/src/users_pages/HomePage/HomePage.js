import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import CustomCalendar from '../components/Calendar/Calendar';
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
        navigate('/select-template');
    };

    useEffect(() => {
        const token = localStorage.getItem('token');

        if (token) {
            const payload = token.split('.')[1];
            const decodedPayload = JSON.parse(atob(payload));

            setUserName(decodedPayload.sub);
        }

        const fetchReservations = async () => {
            const response = await fetch('http://localhost:8080/api/dreams/home-page/reservations', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setReservations(data);
            } else {
                console.error('Failed to fetch reservations');
            }
        };

        fetchReservations();
    }, []);

    return (
        <div className="home-container">
            <div className="left-column">
                <div className="header-user">
                    <p>Добро пожаловать, {userName}</p>
                    <button onClick={handleLogout} className="logout-button">
                        Выйти
                    </button>
                </div>

                <div className="reservations-block">
                    <h2>История бронирований</h2>
                    <div className="reservations-list">
                        {reservations.length > 0 ? (
                            reservations.map((reservation) => (
                                <div key={reservation.reservationId} className="reservation-item">
                                    <p><strong>Мечта:</strong> {reservation.dreamId}</p>
                                    <p><strong>Дата:</strong> {new Date(reservation.timeOfReservation).toLocaleString()}</p>
                                    <p><strong>Статус:</strong> {reservation.status}</p>
                                </div>
                            ))
                        ) : (
                            <p>Бронирования отсутствуют</p>
                        )}
                    </div>
                    <button onClick={handleCreateReservation} className="action-button">Создать бронь</button>
                </div>
            </div>

            <div className="right-column">
                <h1>Добро пожаловать в DreamLand</h1>
                <p>Выберите и забронируйте свои мечты!</p>

                <div className="action-buttons">
                    <button className="action-button" onClick={handleLeaveReview}>Оставить отзыв</button>
                </div>

                <CustomCalendar />
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
