import React, { useState, useEffect } from 'react';
import './CustomCalendar.css';

function CustomCalendar() {
    const [currentDate, setCurrentDate] = useState(new Date());
    const [selectedDate, setSelectedDate] = useState(null);
    const [bookedDates, setBookedDates] = useState([]);

    const weekDays = ['Вск', 'Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб'];

    const months = [
        'Январь', 'Февраль', 'Март', 'Апрель', 'Май', 'Июнь',
        'Июль', 'Август', 'Сентябрь', 'Октябрь', 'Ноябрь', 'Декабрь'
    ];

    const getDaysInMonth = (month, year) => {
        const days = [];
        const date = new Date(year, month, 1);
        const firstDayOfMonth = date.getDay();
        const lastDateOfMonth = new Date(year, month + 1, 0).getDate();

        for (let i = 0; i < firstDayOfMonth; i++) {
            days.push(null);
        }

        for (let i = 1; i <= lastDateOfMonth; i++) {
            days.push(i);
        }

        return days;
    };

    useEffect(() => {
        const fetchBookedDates = async () => {
            const token = localStorage.getItem('jwtToken');
            const response = await fetch('http://localhost:8080/api/dreams/home-page/calendar', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                const dates = data.map(entry => new Date(entry.date).toLocaleDateString());
                setBookedDates(dates);
            } else {
                console.error('Failed to fetch calendar events');
            }
        };

        fetchBookedDates();
    }, []);

    const changeMonth = (direction) => {
        const newDate = new Date(currentDate);
        newDate.setMonth(currentDate.getMonth() + direction);
        setCurrentDate(newDate);
    };

    const handleDayClick = (day) => {
        if (!day) return;
        setSelectedDate(new Date(currentDate.getFullYear(), currentDate.getMonth(), day));
    };

    const isBooked = (day) => {
        const dayString = new Date(currentDate.getFullYear(), currentDate.getMonth(), day).toLocaleDateString();
        return bookedDates.includes(dayString);
    };

    const daysInMonth = getDaysInMonth(currentDate.getMonth(), currentDate.getFullYear());

    // Отображаем календарь только если есть хотя бы одна забронированная дата
    if (bookedDates.length === 0) {
        return (
            <div className="no-reservation-message">
                Нет доступных бронирований в системе.
            </div>
        );
    }

    return (
        <div className="calendar-container">
            <div className="calendar-header">
                <button className="month-button" onClick={() => changeMonth(-1)}>Предыдущий месяц</button>
                <h2>{months[currentDate.getMonth()]} {currentDate.getFullYear()}</h2>
                <button className="month-button" onClick={() => changeMonth(1)}>Следующий месяц</button>
            </div>
            <div className="calendar-body">
                {weekDays.map((day, index) => (
                    <div className="calendar-day-name" key={index}>
                        {day}
                    </div>
                ))}
                {daysInMonth.map((day, index) => (
                    <div
                        key={index}
                        className={`calendar-day ${isBooked(day) ? 'booked' : ''} ${selectedDate?.getDate() === day ? 'selected' : ''}`}
                        onClick={() => handleDayClick(day)}
                    >
                        {day}
                    </div>
                ))}
            </div>
        </div>
    );
}

export default CustomCalendar;
