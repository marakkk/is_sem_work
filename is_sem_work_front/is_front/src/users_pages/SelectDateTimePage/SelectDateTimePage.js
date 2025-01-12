import React, {useEffect, useState} from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './SelectDateTimePage.css';

function SelectDateTimePage() {
    const { dreamId } = useParams(); // Get dreamId from route params
    const [selectedDate, setSelectedDate] = useState('');
    const [selectedTime, setSelectedTime] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        console.log('Dream ID from route params:', dreamId);
    }, [dreamId]);

    const handleConfirmDateTime = () => {
        if (selectedDate && selectedTime) {
            const reservationDetails = {
                dreamId,
                date: selectedDate,
                time: selectedTime,
            };

            console.log('Dream ID:', dreamId);

            localStorage.setItem('reservationDetails', JSON.stringify(reservationDetails));
            console.log('Reservation Details:', reservationDetails);

            navigate(`/dreams/confirm-selection/${dreamId}`); // Pass dreamId to next page
        } else {
            console.error('Date and Time must be selected');
        }
    };

    return (
        <div className="select-datetime-container">
            <h2>Выберите дату и время для вашего сна</h2>
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
