import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './SelectArchitectPage.css';

function SelectArchitectPage() {
    const { dreamId } = useParams(); // Get dreamId from route params
    const [architects, setArchitects] = useState([]); // Store architect list
    const [selectedArchitectId, setSelectedArchitectId] = useState(null); // Track selected architect
    const navigate = useNavigate();

    // Fetch architects from the backend
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

    // Handle architect selection
    const handleSelectArchitect = () => {
        if (selectedArchitectId) {
            // Find the selected architect details from the list of architects
            const selectedArchitect = architects.find(a => a.architectId === selectedArchitectId);

            // Save architect details to localStorage
            localStorage.setItem('selectedArchitect', JSON.stringify({
                architectId: selectedArchitect.architectId,
                username: selectedArchitect.username,
                price: selectedArchitect.price
            }));

            // Save the selected dreamId in localStorage
            localStorage.setItem('selectedDreamId', dreamId);

            console.log(`Architect details saved: ${selectedArchitect.username}, Price: ${selectedArchitect.price}`);
            navigate(`/dreams/select-datetime/${dreamId}`); // Navigate to the next page
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
