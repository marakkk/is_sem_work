import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import './SelectArchitectPage.css'

function SelectArchitectPage() {
    const { dreamId } = useParams();
    const [architects, setArchitects] = useState([]);
    const [selectedArchitect, setSelectedArchitect] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        const fetchArchitects = async () => {
            const response = await fetch('http://localhost:8080/api/dreams/architects', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setArchitects(data);
            } else {
                console.error('Failed to fetch architects');
            }
        };

        fetchArchitects();
    }, []);

    const handleSelectArchitect = async () => {
        const token = localStorage.getItem('token');
        const response = await fetch(`http://localhost:8080/api/dreams/assign-architect/${dreamId}`, {
            method: 'POST',
            headers: {
                Authorization: `Bearer ${token}`,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(selectedArchitect),
        });

        if (response.ok) {
            navigate(`/dreams/${dreamId}`);
        } else {
            console.error('Failed to assign architect');
        }
    };

    return (
        <div className="select-architect-container">
            <h2>Выберите архитектора для вашего сна</h2>
            <select onChange={(e) => setSelectedArchitect(Number(e.target.value))}>
                <option value="">--Выберите архитектора--</option>
                {architects.map((architect) => (
                    <option key={architect.id} value={architect.id}>
                        {architect.id}
                    </option>
                ))}
            </select>
            <button onClick={handleSelectArchitect} disabled={!selectedArchitect}>
                Подтвердить выбор
            </button>
        </div>
    );
}

export default SelectArchitectPage;
