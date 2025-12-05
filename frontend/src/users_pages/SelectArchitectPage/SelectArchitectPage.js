import React, {useState, useEffect} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import './SelectArchitectPage.css';

function SelectArchitectPage() {
    const {dreamId} = useParams();
    const [architects, setArchitects] = useState([]);
    const [selectedArchitectId, setSelectedArchitectId] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchArchitects = async () => {
            try {
                const token = localStorage.getItem('token');
                const response = await fetch('http://localhost:8080/api/dreams/architect/all', {
                    method: 'GET',
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                if (!response.ok) {
                    throw new Error('Failed to fetch architects');
                }

                const data = await response.json();
                console.log('Fetched architects:', data);
                setArchitects(data);
            } catch (error) {
                console.error('Error fetching architects:', error);
            }
        };

        fetchArchitects();
    }, []);

    const handleSelectArchitect = () => {
        if (selectedArchitectId) {
            const selectedArchitect = architects.find(a => a.architectId === selectedArchitectId);

            localStorage.setItem('selectedArchitect', JSON.stringify({
                architectId: selectedArchitect.architectId,
                username: selectedArchitect.username,
                price: selectedArchitect.price,
                rating: selectedArchitect.rating
            }));

            localStorage.setItem('selectedDreamId', dreamId);

            console.log(`Architect details saved: ${selectedArchitect.username}, Price: ${selectedArchitect.price}, ${selectedArchitect.rating}`);
            navigate(`/dreams/select-datetime/${dreamId}`);
        } else {
            console.error('No architect selected');
        }
    };

    return (
        <div className="select-architect-container">
            <h2>Выберите архитектора для вашего сна</h2>
            <div className="architect-list">
                {architects.length > 0 ? (
                    architects.map((architect) => (
                        <div
                            key={architect.architectId}
                            className={`architect-card ${
                                selectedArchitectId === architect.architectId ? 'selected' : ''
                            }`}
                            onClick={() => setSelectedArchitectId(architect.architectId)}
                        >
                            <h3>Архитектор: {architect.username}</h3>
                            <p><strong>ID:</strong> {architect.architectId}</p>
                            <p><strong>Цена:</strong> {architect.price}</p>
                            <p><strong>Рейтинг:</strong> {architect.rating}</p>
                        </div>
                    ))
                ) : (
                    <p>Загрузка архитекторов...</p>
                )}
            </div>
            <button onClick={handleSelectArchitect} disabled={!selectedArchitectId}>
                Подтвердить выбор
            </button>
        </div>
    );
}

export default SelectArchitectPage;
