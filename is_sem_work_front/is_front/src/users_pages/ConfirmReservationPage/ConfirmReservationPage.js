// ConfirmDreamPage.jsx
import React from 'react';
import { useNavigate } from 'react-router-dom';
import './ConfirmReservationPage.css';

function ConfirmReservationPage() {
    const navigate = useNavigate();

    return (
        <div className="confirm-dream-page">
            <h2>Ваш сон успешно забронирован!</h2>
            <p>Спасибо за использование нашей платформы.</p>
            <button onClick={() => navigate('/dreams/home-page')} className="home-button">
                На главную
            </button>
        </div>
    );
}

export default ConfirmReservationPage;
