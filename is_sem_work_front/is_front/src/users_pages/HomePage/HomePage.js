import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './HomePage.css';

function HomePage() {
    const [reservations, setReservations] = useState([]);
    const [userName, setUserName] = useState('');
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isReviewModalOpen, setIsReviewModalOpen] = useState(false); // Новый стейт для модального окна отзывов
    const [selectedArchitect, setSelectedArchitect] = useState('');
    const [selectedRating, setSelectedRating] = useState(1);
    const navigate = useNavigate();
    const [isCharacterModalOpen, setIsCharacterModalOpen] = useState(false);
    const [selectedCharacters, setSelectedCharacters] = useState([]);
    const token = localStorage.getItem('token');
    const [selectedUsersDreamsId, setSelectedUsersDreamId] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [selectedRatings, setSelectedRatings] = useState({});


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
        setIsReviewModalOpen(true); // Открытие модального окна для оставления отзыва
    };

    const handleCloseReviewModal = () => {
        setIsReviewModalOpen(false);
    };

    const handleRatingChange = (reservationId, newRating) => {
        setSelectedRatings(prevRatings => ({
            ...prevRatings,
            [reservationId]: newRating, // Сохраняем рейтинг для конкретного бронирования
        }));
    };


    const handleSubmitReview = async () => {
        if (!selectedUsersDreamsId) {
            alert("Пожалуйста, выберите сон для отзыва.");
            return;
        }

        // Находим reservationId для выбранного usersDreamsId
        const selectedReservation = reservations.find(reservation => reservation.usersDreamsId === selectedUsersDreamsId);
        if (!selectedReservation) {
            alert("Выбранный сон не найден.");
            return;
        }

        const reviewData = {
            architectId: selectedArchitect,
            mark: selectedRatings[selectedReservation.reservationId] || 1, // Используем reservationId для получения оценки
            usersDreamsId: selectedUsersDreamsId,
        };

        try {
            const response = await fetch('http://localhost:8080/api/dreams/reviews', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${token}`,
                },
                body: JSON.stringify(reviewData),
            });

            if (response.ok) {
                console.info('Отзыв успешно оставлен!');
                setIsReviewModalOpen(false);
            } else {
                console.error('Ошибка при отправке отзыва');
                console.log('Review data:', reviewData);
            }
        } catch (error) {
            console.error('Ошибка при отправке отзыва:', error);
        }
    };


    const handleCreateOwnDream = () => {
        setIsModalOpen(false);
        navigate('/dreams/create-own-dream');
    };

    const handleSelectTemplate = () => {
        setIsModalOpen(false);
        navigate('/dreams/templates');
    };

    const handleShowCharacters = async (reservationId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/reservations/${reservationId}/characters`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const characters = await response.json();
                setSelectedCharacters(characters);
                setIsCharacterModalOpen(true);
            } else {
                console.error('Не удалось загрузить персонажей');
            }
        } catch (error) {
            console.error('Ошибка при загрузке персонажей:', error);
        }
    };

    const handleCloseCharacterModal = () => {
        setSelectedCharacters([]);
        setIsCharacterModalOpen(false);
    };

    useEffect(() => {
        const fetchReservations = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/reservations/history', {
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
            } catch (error) {
                console.error('Ошибка при загрузке бронирований:', error);
            }
        };

        fetchReservations();
    }, [token]);

    useEffect(() => {
        if (selectedArchitect && selectedUsersDreamsId) {
            console.log("Selected Architect:", selectedArchitect);
            console.log("Selected UsersDreams ID:", selectedUsersDreamsId);
        }
    }, [selectedArchitect, selectedUsersDreamsId]);

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
                                    <th>Персонажи</th>
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
                                        <td>
                                            <button
                                                onClick={() => handleShowCharacters(reservation.reservationId)}
                                                className="action-button"
                                            >
                                                Узнать
                                            </button>
                                        </td>
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

            {isCharacterModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Персонажи</h2>
                        {selectedCharacters.length > 0 ? (
                            <table className="characters-table">
                                <thead>
                                <tr>
                                    <th>Имя</th>
                                    <th>Характеристика</th>
                                    <th>Внешность</th>
                                    <th>Роль</th>
                                </tr>
                                </thead>
                                <tbody>
                                {selectedCharacters.map((character, index) => (
                                    <tr key={index}>
                                        <td>{character.name}</td>
                                        <td>{character.characteristics || 'Нет'}</td>
                                        <td>{character.appearance || 'Нет'}</td>
                                        <td>{character.relation || 'Нет'}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <p>Нет персонажей</p>
                        )}
                        <button onClick={handleCloseCharacterModal} className="close-modal-button">Закрыть</button>
                    </div>
                </div>
            )}

            {isReviewModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Оставить отзыв</h2>
                        <table>
                            <thead>
                            <tr>
                                <th>Архитектор</th>
                                <th>Название сна</th>
                                <th>Рейтинг</th>
                            </tr>
                            </thead>
                            <tbody>
                            {reservations.map((reservation) => (
                                <tr key={reservation.reservationId}>
                                    <td>{reservation.architectUsername}</td>
                                    <td>{reservation.dreamName}</td>
                                    <td>
                                        <select
                                            value={selectedRatings[reservation.reservationId] || 1}
                                            onChange={(e) => handleRatingChange(reservation.reservationId, e.target.value)}
                                        >
                                            {[1, 2, 3, 4, 5].map((mark) => (
                                                <option key={mark} value={mark}>
                                                    {mark}
                                                </option>
                                            ))}
                                        </select>
                                    </td>
                                    <td>
                                        <button
                                            onClick={() => {
                                                console.log("Architect ID:", reservation.architectId);
                                                console.log("UsersDreams ID:", reservation.usersDreamsId);
                                                setSelectedArchitect(reservation.architectId);
                                                setSelectedUsersDreamId(reservation.usersDreamsId);
                                            }}
                                            className="action-button"
                                        >
                                            Выбрать
                                        </button>
                                    </td>
                                </tr>
                            ))}

                            </tbody>
                        </table>
                        <button onClick={handleSubmitReview}>Отправить отзыв</button>
                        <button onClick={handleCloseReviewModal}>Закрыть</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default HomePage;
