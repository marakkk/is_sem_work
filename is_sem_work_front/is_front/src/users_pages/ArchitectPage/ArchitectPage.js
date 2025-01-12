import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ArchitectPage.css';

function ArchitectPage() {
    const [requests, setRequests] = useState([]);
    const [ratings, setRatings] = useState([]);
    const [newPrice, setNewPrice] = useState('');
    const [selectedDreamId, setSelectedDreamId] = useState(null);
    const [history, setHistory] = useState([]); // State for architect's work history
    const navigate = useNavigate();
    const token = localStorage.getItem('token');

    useEffect(() => {
        if (!token) {
            navigate('/login');
        }
    }, [token, navigate]);

    const fetchHistory = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/reservations/history', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setHistory(data);
            } else {
                console.error('Не удалось загрузить историю работы архитектора.');
            }
        } catch (error) {
            console.error('Ошибка при загрузке истории работы:', error);
        }
    };

    const fetchRequests = async () => {
        try {
            const response = await fetch('/api/dreams/architect/requests', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setRequests(data);
            } else {
                console.error('Не удалось загрузить запросы на создание снов.');
            }
        } catch (error) {
            console.error('Ошибка при загрузке запросов:', error);
        }
    };

    const fetchRatings = async () => {
        try {
            const response = await fetch('/api/dreams/architect/ratings', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setRatings(data);
            } else {
                console.error('Не удалось загрузить рейтинги снов.');
            }
        } catch (error) {
            console.error('Ошибка при загрузке рейтингов:', error);
        }
    };

    const handlePriceChange = async () => {
        if (selectedDreamId && newPrice) {
            try {
                const response = await fetch(`/api/dreams/architect/update-price/${selectedDreamId}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify({ price: parseFloat(newPrice) }),
                });

                if (response.ok) {
                    const updatedDream = await response.json();
                    console.log('Цена обновлена:', updatedDream);
                } else {
                    console.error('Ошибка при обновлении цены.');
                }
            } catch (error) {
                console.error('Ошибка при обновлении цены:', error);
            }
        }
    };

    const handleCreateTemplate = () => {
        navigate('/dreams/architect-page/create-template');
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    useEffect(() => {
        fetchHistory();
        fetchRequests();
        fetchRatings();
    }, []); // Fetch data on component mount

    return (
        <div className="home-container">
            <div className="header">
                <h1>Здравствуйте, архитектор</h1>
                <button className="logout-button" onClick={handleLogout}>
                    Выйти
                </button>
            </div>

            <div className="left-column">
                <h2>Запросы на создание снов</h2>
                <div className="reservations-list">
                    {requests.map((request) => (
                        <div key={request.dreamId}>
                            <p>{request.name}</p>
                            <p>Цена: {request.price}</p>
                        </div>
                    ))}
                </div>
                <button className="action-button" onClick={handleCreateTemplate}>
                    Создать новый шаблон
                </button>
            </div>

            <div className="right-column">
                <h2>Рейтинг снов</h2>
                <div className="reservations-list">
                    {ratings.map((rating) => (
                        <div key={rating.dreamId}>
                            <p>{rating.name}</p>
                            <p>Рейтинг: {rating.rating}</p>
                        </div>
                    ))}
                </div>

                <div className="price-management">
                    <h3>Управление ценой</h3>
                    <input
                        type="number"
                        placeholder="Новая цена"
                        value={newPrice}
                        onChange={(e) => setNewPrice(e.target.value)}
                    />
                    <button className="action-button" onClick={handlePriceChange}>
                        Обновить цену
                    </button>
                </div>

                {/* History Table */}
                <h3>История работы архитектора</h3>
                <div className="history-table-container">
                    {history.length > 0 ? (
                        <table className="history-table">
                            <thead>
                            <tr>
                                <th>Название мечты</th>
                                <th>Дата</th>
                                <th>Цена</th>
                                <th>Статус</th>
                            </tr>
                            </thead>
                            <tbody>
                            {history.map((item) => (
                                <tr key={item.dreamId}>
                                    <td>{item.dreamName}</td>
                                    <td>{item.date}</td>
                                    <td>{item.price}</td>
                                    <td>{item.status}</td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>История работы пустая</p>
                    )}
                </div>
            </div>
        </div>
    );
}

export default ArchitectPage;
