import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './AdminPage.css';

function AdminPage() {
    const [architectRequests, setArchitectRequests] = useState([]);
    const [adminRequests, setAdminRequests] = useState([]);
    const [reservations, setReservations] = useState([]);
    const [editingReservation, setEditingReservation] = useState(null);
    const [selectedDate, setSelectedDate] = useState('');
    const [selectedTime, setSelectedTime] = useState('');
    const [selectedArchitectId, setSelectedArchitectId] = useState('');
    const [selectedPrice, setSelectedPrice] = useState('');
    const [architects, setArchitects] = useState([]);
    const navigate = useNavigate();

    const token = localStorage.getItem('token');

    useEffect(() => {
        if (!token) {
            navigate('/login');
        } else {
            fetchArchitectRequests();
            fetchAdminRequests();
            fetchReservations();
            fetchArchitects();
        }
    }, [token, navigate]);

    const fetchArchitects = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/dreams/architect/all', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const responseText = await response.text();
            console.log('Architects Response:', responseText);

            if (response.ok) {
                const data = JSON.parse(responseText);
                const mappedArchitects = data.map(architect => ({
                    id: architect.architectId,
                    username: architect.username,
                }));
                setArchitects(mappedArchitects);
            } else {
                console.error('Failed to fetch architects:', responseText);
            }
        } catch (error) {
            console.error('Error fetching architects:', error);
        }
    };

    const fetchArchitectRequests = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/admin/architect-requests', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const responseText = await response.text();
            console.log('Architect Requests Response:', responseText);

            if (response.ok) {
                const data = JSON.parse(responseText);
                setArchitectRequests(data);
            } else {
                console.error('Failed to fetch architect requests:', responseText);
            }
        } catch (error) {
            console.error('Error fetching architect requests:', error);
        }
    };

    const fetchAdminRequests = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/admin/admin-requests', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const responseText = await response.text();
            console.log('Admin Requests Response:', responseText);

            if (response.ok) {
                const data = JSON.parse(responseText);
                setAdminRequests(data);
            } else {
                console.error('Failed to fetch admin requests:', responseText);
            }
        } catch (error) {
            console.error('Error fetching admin requests:', error);
        }
    };

    const fetchReservations = async () => {
        try {
            const response = await fetch('http://localhost:8080/api/reservations/history', {
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });
            const responseText = await response.text();
            console.log('Reservations Response:', responseText);

            if (response.ok) {
                const data = JSON.parse(responseText);
                setReservations(data);
            } else {
                console.error('Failed to fetch reservations:', responseText);
            }
        } catch (error) {
            console.error('Error fetching reservations:', error);
        }
    };

    const handleEditReservation = (reservation) => {
        setEditingReservation(reservation);
        setSelectedDate(reservation.date);
        setSelectedTime(reservation.time);
        setSelectedArchitectId(reservation.architectId);
        setSelectedPrice(reservation.price);
    };

    const handleSaveReservation = async () => {
        if (!editingReservation || !selectedDate || !selectedTime || !selectedArchitectId || !selectedPrice) return;

        const updatedReservation = {
            date: selectedDate,
            time: selectedTime,
            architectId: selectedArchitectId,
            price: selectedPrice,
        };

        const response = await fetch(`http://localhost:8080/api/reservations/update-reservation/${editingReservation.id}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(updatedReservation),
        });

        if (response.ok) {
            fetchReservations();
            setEditingReservation(null);
        } else {
            console.error('Failed to update reservation');
        }
    };

    const handleCancelEdit = () => {
        setEditingReservation(null);
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const handleDeleteReservation = async (reservationId) => {
        try {
            const response = await fetch(`http://localhost:8080/api/reservations/delete-reservation/${reservationId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${token}`,
                },
            });

            if (response.ok) {
                fetchReservations();
            } else {
                console.error('Failed to delete reservation');
            }
        } catch (error) {
            console.error('Error deleting reservation:', error);
        }
    };

    const handleApproveArchitect = async (id) => {
        const response = await fetch(`http://localhost:8080/api/admin/approve-architect/${id}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });
        if (response.ok) {
            fetchArchitectRequests();
        } else {
            console.error('Failed to approve architect');
        }
    };

    const handleDenyArchitect = async (id) => {
        const response = await fetch(`http://localhost:8080/api/admin/deny-architect/${id}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });
        if (response.ok) {
            fetchArchitectRequests();
        } else {
            console.error('Failed to deny architect');
        }
    };

    const handleApproveAdmin = async (id) => {
        const response = await fetch(`http://localhost:8080/api/admin/approve-admin/${id}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });
        if (response.ok) {
            fetchAdminRequests();
        } else {
            console.error('Failed to approve admin');
        }
    };

    const handleDenyAdmin = async (id) => {
        const response = await fetch(`http://localhost:8080/api/admin/deny-admin/${id}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
        });
        if (response.ok) {
            fetchAdminRequests();
        } else {
            console.error('Failed to deny admin');
        }
    };


    return (
        <div className="admin-container">
            <div className="header">
                <h1>Здравствуйте, администратор</h1>
                <button className="logout-button" onClick={handleLogout}>Выйти</button>
            </div>

            <div className="content">
                <h2><br/><br/><br/>Запросы архитекторов</h2>
                <div className="table-container">
                    {architectRequests.length > 0 ? (
                        <table className="data-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Имя пользователя</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {architectRequests.map((request) => (
                                <tr key={request.id}>
                                    <td>{request.userId}</td>
                                    <td>{request.username || 'N/A'}</td>
                                    <td>
                                        <div className="action-buttons">
                                            <button className="approve-button"
                                                    onClick={() => handleApproveArchitect(request.id)}>Одобрить
                                            </button>
                                            <button className="deny-button"
                                                    onClick={() => handleDenyArchitect(request.id)}>Отклонить
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>Нет запросов архитекторов</p>
                    )}
                </div>

                <h2>Запросы администраторов</h2>
                <div className="table-container">
                    {adminRequests.length > 0 ? (
                        <table className="data-table">
                            <thead>
                            <tr>
                                <th>ID</th>
                                <th>Имя пользователя</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {adminRequests.map((request) => (
                                <tr key={request.id}>
                                    <td>{request.userId}</td>
                                    <td>{request.username || 'N/A'}</td>
                                    <td>
                                        <div className="action-buttons">
                                            <button className="approve-button"
                                                    onClick={() => handleApproveAdmin(request.id)}>Одобрить
                                            </button>
                                            <button className="deny-button"
                                                    onClick={() => handleDenyAdmin(request.id)}>Отклонить
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>Нет запросов администраторов</p>
                    )}
                </div>

                <h2>Все бронирования</h2>
                <div className="table-container">
                    {reservations.length > 0 ? (
                        <table className="data-table">
                            <thead>
                            <tr>
                                <th>Название мечты</th>
                                <th>Пользователь</th>
                                <th>Цена</th>
                                <th>Архитектор</th>
                                <th>Шаблон</th>
                                <th>Дата</th>
                                <th>Время</th>
                                <th>Статус</th>
                                <th>Время бронирования</th>
                                <th>Действия</th>
                            </tr>
                            </thead>
                            <tbody>
                            {reservations.map((reservation) => (
                                <tr key={reservation.id}>
                                    <td>{reservation.dreamName || 'N/A'}</td>
                                    <td>{reservation.creator || 'N/A'}</td>
                                    <td>{reservation.price || 'N/A'}</td>
                                    <td>{reservation.architectUsername || 'N/A'}</td>
                                    <td>{reservation.template.toString() || 'N/A'}</td>
                                    <td>{reservation.date || 'N/A'}</td>
                                    <td>{reservation.time || 'N/A'}</td>
                                    <td>{reservation.status || 'N/A'}</td>
                                    <td>{new Date(reservation.timeOfReservation).toLocaleString() || 'N/A'}</td>
                                    <td>
                                        <div className="action-buttons">
                                            {reservation.status === 'CONFIRMED' && (
                                                <button className="edit-button"
                                                        onClick={() => handleEditReservation(reservation)}>Редактировать
                                                </button>
                                            )}
                                            {reservation.status === 'CONFIRMED' && (
                                                <button className="deny-button"
                                                        onClick={() => handleDenyReservation(reservation.id)}>Отклонить</button>
                                            )}

                                            {(reservation.status === 'CANCELLED' || reservation.status === 'DENIED') && (
                                                <button className="delete-button"
                                                        onClick={() => handleDeleteReservation(reservation.id)}>Удалить
                                                </button>
                                            )}
                                        </div>
                                    </td>
                                </tr>
                            ))}
                            </tbody>
                        </table>
                    ) : (
                        <p>Нет бронирований</p>
                    )}
                </div>
            </div>

            {editingReservation && (
                <div className="modal">
                    <div className="modal-content">
                        <h2>Редактировать бронирование</h2>
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
                        <div className="datetime-input">
                            <label>Архитектор:</label>
                            <select
                                value={selectedArchitectId}
                                onChange={(e) => setSelectedArchitectId(e.target.value)}
                            >
                                <option value="">Выберите архитектора</option>
                                {architects.map((architect) => (
                                    <option key={architect.id} value={architect.id}>
                                        {architect.username}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div className="datetime-input">
                            <label>Цена:</label>
                            <input
                                type="number"
                                value={selectedPrice}
                                onChange={(e) => setSelectedPrice(e.target.value)}
                            />
                        </div>
                        <div className="modal-buttons">
                            <button onClick={handleSaveReservation}>Сохранить</button>
                            <button onClick={handleCancelEdit}>Отмена</button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default AdminPage;