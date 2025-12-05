import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './ReservationPage.css';

function ReservationPage() {
    const [reservationDetails, setReservationDetails] = useState(null);
    const [isCollective, setIsCollective] = useState(null); // Track if dream is collective
    const [collectivePartnerId, setCollectivePartnerId] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const storedReservation = JSON.parse(localStorage.getItem('reservationDetails'));
        const storedDreams = JSON.parse(localStorage.getItem('dreams')) || [];
        const storedArchitect = JSON.parse(localStorage.getItem('selectedArchitect'));

        console.log('Contents of localStorage in ReservationPage:');
        console.log('reservationDetails:', storedReservation);
        console.log('storedArchitect:', storedArchitect);
        console.log('storedDreams:', storedDreams);

        if (storedReservation) {
            const dream = storedDreams.find(d => d.id === storedReservation.dreamId);

            if (dream) {
                // Ensure the template field is set correctly
                if (dream.template === undefined) {
                    console.warn('Template field is undefined. Setting it to false.');
                    dream.template = true;
                }

                const architect = dream.template
                    ? {
                        username: dream.architectName,
                        price: dream.architectPrice,
                        rating: dream.architect.rating,
                    }
                    : storedArchitect;

                setReservationDetails({
                    ...storedReservation,
                    dream,
                    architect,
                });
            } else {
                console.error('Dream not found for reservation.');
                navigate('/'); // Navigate back if dream is not found
            }
        } else {
            console.error('Missing reservation details or architect');
            navigate('/'); // Navigate back if details are missing
        }
    }, [navigate]);



    const handleCancel = () => {
        localStorage.removeItem('reservationDetails');
        localStorage.removeItem('selectedArchitectId');
        localStorage.removeItem('selectedDreamId');
        navigate('/dreams/home-page'); // Navigate back to home or previous page
    };

    const handleReserve = async () => {
        try {
            const token = localStorage.getItem('token');
            const { dream, architect, date, time} = reservationDetails;

            let response;

            if (dream.template === true) {
                // Payload for template-based dreams
                const templateReservationPayload = {
                    originalTemplateId: dream.originalTemplateId,
                    architectId: dream.architectId,
                    date: date,
                    time: time,
                    timeOfReservation: new Date().toISOString(),

                };

                response = await fetch('http://localhost:8080/api/reservations/confirm-template', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify(templateReservationPayload),
                });
            } else {
                const reservationPayload = {
                    dreamName: dream.name,
                    timeEra: dream.timeEra,
                    virtualEnvironment: dream.virtualEnvironment,
                    specialPowers: dream.specialPowers,
                    physicalRules: dream.physicalRules,
                    role: dream.role,
                    genre: dream.genre,
                    scenario: dream.scenario,
                    template: dream.template,
                    price: dream.price,
                    characters: dream.characters?.map(character => ({
                        name: character.name,
                        characteristics: character.characteristics,
                        appearance: character.appearance,
                        relation: character.relation,
                        occupation: character.occupation,
                    })) || [],
                    architectId: architect.architectId,
                    architectPrice: architect.price,
                    date: date,
                    time: time,
                    timeOfReservation: new Date().toISOString(),
                    collectivePartner: isCollective ? collectivePartnerId : null,

                };

                response = await fetch('http://localhost:8080/api/reservations/confirm', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        Authorization: `Bearer ${token}`,
                    },
                    body: JSON.stringify(reservationPayload),
                });
            }
            console.info(reservationDetails)

            if (!response.ok) {
                const errorData = await response.json().catch(() => null);
                if (errorData && errorData.message) {
                    throw new Error(errorData.message);
                }
                throw new Error(`Failed to reserve dream: ${response.status}`);
            }

            console.log('Reservation successful!');

            // Cleanup after successful reservation
            localStorage.removeItem('reservationDetails');
            localStorage.removeItem('selectedArchitectId');
            localStorage.removeItem('selectedDreamId');

            // Navigate to success page after reservation is confirmed
            navigate('/dreams/success');

        } catch (error) {
            console.error('Error during reservation:', error.message);
        }
    };


    if (!reservationDetails) {
        return <p>Loading reservation details...</p>;
    }

    const { dream, architect, date, time} = reservationDetails;

    return (
        <div className="reservation-page">
            <h2>Подтверждение бронирования</h2>

            <div className="reservation-details dream-container">
                <h3>Информация о сне</h3>
                <p><strong>Сон:</strong> {dream?.name}</p>
                <p><strong>Эра времени:</strong> {dream?.timeEra}</p>
                <p><strong>Виртуальная среда:</strong> {dream?.virtualEnvironment}</p>
                <p><strong>Особые силы:</strong> {dream?.specialPowers}</p>
                <p><strong>Физические законы:</strong> {dream?.physicalRules}</p>
                <p><strong>Роль:</strong> {dream?.role}</p>
                <p><strong>Жанр:</strong> {dream?.genre}</p>
                <p><strong>Сценарий:</strong> {dream?.scenario}</p>
                <h4>Персонажи:</h4>
                {dream?.characters && dream.characters.length > 0 ? (
                    <ul className="character-list">
                        {dream.characters.map((character, index) => (
                            <li key={index}>
                                <strong>Имя:</strong> {character.name},
                                <strong> Характеристики:</strong> {character.characteristics},
                                <strong> Внешность:</strong> {character.appearance},
                                <strong> Роль:</strong> {character.relation},
                                <strong> Занятие:</strong> {character.occupation}
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>Персонажи не указаны</p>
                )}
            </div>

            <div className="reservation-details info-container">
                <div className="architect-info">
                    <h3>Информация об архитекторе</h3>
                    <p><strong>Имя архитектора:</strong> {architect?.username}</p>
                    <p><strong>Цена:</strong> {architect?.price}</p>
                    {dream.template && <p><strong>Рейтинг:</strong> {architect?.rating}</p>}

                </div>
                <div className="datetime-info">
                    <h3>Дата и время</h3>
                    <p><strong>Дата:</strong> {date}</p>
                    <p><strong>Время:</strong> {time}</p>
                </div>
            </div>

            <div className="collective-option">
                <h3>Вы хотите сделать сон коллективным?</h3>
                <button onClick={() => setIsCollective(true)} className={isCollective === true ? 'selected' : ''}>
                    Да
                </button>
                <button onClick={() => setIsCollective(false)} className={isCollective === false ? 'selected' : ''}>
                    Нет
                </button>
                {isCollective && (
                    <div className="collective-input">
                        <label>
                            <strong>ID пользователя партнера:</strong>
                            <input
                                type="text"
                                value={collectivePartnerId}
                                onChange={(e) => setCollectivePartnerId(e.target.value)}
                                placeholder="Введите ID пользователя"
                            />
                        </label>
                    </div>
                )}
            </div>

            <div className="reservation-buttons">
                <button onClick={handleCancel} className="cancel-button">Отменить</button>
                <button onClick={handleReserve} className="reserve-button">Забронировать</button>
            </div>
        </div>
    );
}

export default ReservationPage;
