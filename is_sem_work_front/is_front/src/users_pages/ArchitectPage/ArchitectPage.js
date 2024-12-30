import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ArchitectPage.css';

function ArchitectPage() {
    const [requests, setRequests] = useState([]);
    const [ratings, setRatings] = useState([]);
    const [newPrice, setNewPrice] = useState('');
    const [selectedDreamId, setSelectedDreamId] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchRequests = async () => {
            const response = await fetch('/api/dreams/architect-page/requests');
            if (response.ok) {
                const data = await response.json();
                setRequests(data);
            }
        };

        const fetchRatings = async () => {
            const response = await fetch('/api/dreams/architect-page/ratings');
            if (response.ok) {
                const data = await response.json();
                setRatings(data);
            }
        };

        fetchRequests();
        fetchRatings();
    }, []);

    const handlePriceChange = async () => {
        if (selectedDreamId && newPrice) {
            const response = await fetch(`/api/dreams/architect-page/update-price/${selectedDreamId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ price: parseFloat(newPrice) }),
            });

            if (response.ok) {
                const updatedDream = await response.json();
                console.log('Цена обновлена', updatedDream);
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
            </div>
        </div>
    );
}

export default ArchitectPage;
