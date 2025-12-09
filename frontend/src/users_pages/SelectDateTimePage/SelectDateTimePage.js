import React, {useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import './SelectDateTimePage.css';

function SelectDateTimePage() {
    const {dreamId} = useParams();
    const [selectedDate, setSelectedDate] = useState('');
    const [selectedTime, setSelectedTime] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const navigate = useNavigate();

    const validateDateTime = () => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        const selectedDateObj = new Date(selectedDate);

        const dayOfWeek = selectedDateObj.getUTCDay();
        if (dayOfWeek === 6 || dayOfWeek === 0) {
            setErrorMessage('Архитекторы не работают в выходные (субботу и воскресенье)');
            return false;
        }
        const [hours, minutes] = selectedTime.split(':').map(Number);
        if (hours < 10 || hours > 19) {
            setErrorMessage('Архитекторы работают только с 10 до 19 часов.');
            return false;
        }

        if (minutes % 10 !== 0) {
            setErrorMessage('Можно выбрать время только с интервалом в 10 минут');
            return false;
        }

        setErrorMessage('');
        return true;
    };

    const handleConfirmDateTime = async () => {
        if (selectedDate && selectedTime && validateDateTime()) {

            const reservationDetails = {
                dreamId,
                date: selectedDate,
                time: selectedTime,
            };

            localStorage.setItem('reservationDetails', JSON.stringify(reservationDetails));
            console.log('Reservation Details:', reservationDetails);

            navigate(`/dreams/confirm-selection/${dreamId}`);
        } else {
            console.error('Date and Time must be selected');
        }
    };

    return (
        <div className="select-datetime-container">
            <h2>Выберите дату и время для вашего сна</h2>
            {errorMessage && <div className="error-message">{errorMessage}</div>}

            <div className="datetime-input">
                <label>Дата:</label>
                <input
                    type="date"
                    value={selectedDate}
                    onChange={(e) => setSelectedDate(e.target.value)}
                />
            </div>

            <div className="datetime-input">
                <label>Время:</label>
                <input
                    type="time"
                    value={selectedTime}
                    onChange={(e) => setSelectedTime(e.target.value)}
                />
            </div>

            <button onClick={handleConfirmDateTime} disabled={!selectedDate || !selectedTime}>
                Подтвердить дату и время
            </button>
        </div>
    );
}

export default SelectDateTimePage;
