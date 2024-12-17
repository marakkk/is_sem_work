import React, { useState } from 'react';
import Button from "../Button/Button";
import './SpecialActions.css';

const API_BASE_URL = 'http://localhost:8080/api/book-creatures';

function SpecialActions({ token }) {
    const [minWeightCreature, setMinWeightCreature] = useState(null);
    const [ringName, setRingName] = useState('');
    const [creatureCount, setCreatureCount] = useState(null);
    const [creatureSearchResults, setCreatureSearchResults] = useState([]);
    const [nameSubstring, setNameSubstring] = useState('');
    const [error, setError] = useState(null);
    const [creatureId1, setCreatureId1] = useState('');
    const [creatureId2, setCreatureId2] = useState('');
    const [strongestRingCreature, setStrongestRingCreature] = useState(null);
    const [successMessage, setSuccessMessage] = useState(null);

    const getBookCreatureWithMinRingWeight = async (token) => {
        const response = await fetch(`${API_BASE_URL}/min-ring-weight`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Ошибка при поиске самого легковесного кольца: ${response.status}`);
        }

        const data = await response.json();
        return data;
    };

    const handleGetMinRingWeightCreature = async () => {
        try {
            const creature = await getBookCreatureWithMinRingWeight(token);
            setMinWeightCreature(creature);
            setError(null);
        } catch (err) {
            setError(err.message);
            setMinWeightCreature(null);
        }
    };

    const getCreatureCountByRing = async (ringName, token) => {
        const response = await fetch(`${API_BASE_URL}/count-by-ring?ringName=${encodeURIComponent(ringName)}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Ошибка при подсчете количества: ${ringName}`);
        }

        const count = await response.json();
        return count;
    };

    const handleGetCreatureCountByRing = async () => {
        try {
            const count = await getCreatureCountByRing(ringName, token);
            setCreatureCount(count);
            setError(null);
        } catch (err) {
            setError(err.message);
            setCreatureCount(null);
        }
    };

    const searchCreaturesByNameSubstring = async (nameSubstring, token) => {
        const response = await fetch(`${API_BASE_URL}/search-by-name?substring=${encodeURIComponent(nameSubstring)}`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Ошибка в поиске по подстроке имени: ${response.status}`);
        }

        const creatures = await response.json();
        return creatures;
    };

    const handleSearchCreaturesByName = async () => {
        try {
            const creatures = await searchCreaturesByNameSubstring(nameSubstring, token);
            setCreatureSearchResults(creatures);
            setError(null);
        } catch (err) {
            setError(err.message);
            setCreatureSearchResults([]);
        }
    };

    const exchangeRings = async () => {
        const response = await fetch(`${API_BASE_URL}/exchange-rings?id1=${creatureId1}&id2=${creatureId2}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Ошибка при обмене кольцами: ${errorText}`);
        }

        setCreatureId1('');
        setCreatureId2('');
    };

    const handleExchangeRings = async () => {
        try {
            await exchangeRings();
            setError(null);
            setSuccessMessage("Обмен кольцами произошел успешно!");
            setTimeout(() => setSuccessMessage(null), 5000);
        } catch (err) {
            setError(err.message);
        }
    };

    const getBookCreatureWithStrongestRing = async (token) => {
        const response = await fetch(`${API_BASE_URL}/max-ring-weight`, {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            const errorText = await response.text();
            throw new Error(`Ошибка при поиске самого сильного кольца: ${response.status}`);
        }

        const data = await response.json();
        return data;
    };

    const handleGetStrongestRingCreature = async () => {
        try {
            const creature = await getBookCreatureWithStrongestRing(token);
            setStrongestRingCreature(creature);
            setError(null);
        } catch (err) {
            setError(err.message);
            setStrongestRingCreature(null);
        }
    };

    return (
        <div className="special-actions">
            <h3>Специальные действия</h3>
            <div className="button-container">
                <Button onClick={handleGetMinRingWeightCreature} label="Найти сущность с самым легковесным кольцом"/>
            </div>

            {minWeightCreature && (
                <div className="creature-info">
                    <h4>Сущность с самым легковесным кольцом:</h4>
                    <p>Name: {minWeightCreature.name}</p>
                    <p>Ring Name: {minWeightCreature.ring.name}</p>
                    <p>Ring Weight: {minWeightCreature.ring.weight}</p>
                </div>
            )}
            <div className="button-container">
                <Button onClick={handleGetStrongestRingCreature} label="Найти сущность с самым сильным кольцом"/>
            </div>
            {strongestRingCreature && (
                <div className="creature-info">
                    <h4>Сущность с наисильнейшим кольцом:</h4>
                    <p>Name: {strongestRingCreature.name}</p>
                    <p>Ring Name: {strongestRingCreature.ring.name}</p>
                    <p>Ring Weight: {strongestRingCreature.ring.weight}</p>
                </div>
            )}

            <div className="creature-count-section">
                <h4>Подсчет количества сущностей по кольцу</h4>
                <input
                    type="text"
                    value={ringName}
                    onChange={(e) => setRingName(e.target.value)}
                    placeholder="Введите имя кольца"
                />

                <div className="button-container">
                    <Button onClick={handleGetCreatureCountByRing} label="Узнать количество сущностей"/>
                </div>

                {creatureCount !== null && (
                    <div className="creature-count-info">
                        <p>Количество сущностей с данным кольцом "{ringName}": {creatureCount}</p>
                    </div>
                )}
            </div>

            <div className="creature-search-section">
                <h4>Поиск сущностей по подстроке</h4>
                <input
                    type="text"
                    value={nameSubstring}
                    onChange={(e) => setNameSubstring(e.target.value)}
                    placeholder="Введите подстроку"
                />
                <div className="button-container">
                    <Button onClick={handleSearchCreaturesByName} label="Найти сущностей"/>
                </div>

                {creatureSearchResults.length > 0 && (
                    <div className="creature-search-results">
                        <h4>Creatures Matching "{nameSubstring}":</h4>
                        <ul>
                            {creatureSearchResults.map(creature => (
                                <li key={creature.id}>
                                    <strong>Name:</strong> {creature.name} <br/>
                                    <strong>Age:</strong> {creature.age} <br/>
                                    <strong>Type:</strong> {creature.creatureType} <br/>
                                    <strong>Location:</strong> {creature.creatureLocation?.name} <br/>
                                    <strong>Coordinates:</strong> {creature.coordinates?.latitude}, {creature.coordinates?.longitude}
                                    <br/>
                                    <strong>Attack Level:</strong> {creature.attackLevel} <br/>
                                    <strong>Defense Level:</strong> {creature.defenseLevel} <br/>
                                    <strong>Creation Date:</strong> {new Date(creature.creationDate).toLocaleString()}
                                    <br/>
                                    <strong>Ring:</strong> {creature.ring?.name} <br/>
                                    <strong>Ring Weight:</strong> {creature.ring?.weight} <br/>
                                </li>
                            ))}
                        </ul>
                    </div>
                )}
            </div>

            <div className="ring-exchange-section">
                <h4>Обмен кольцами между сущностями</h4>
                <input
                    type="text"
                    value={creatureId1}
                    onChange={(e) => setCreatureId1(e.target.value)}
                    placeholder="Введите ID первой сущности"
                />
                <input
                    type="text"
                    value={creatureId2}
                    onChange={(e) => setCreatureId2(e.target.value)}
                    placeholder="Введите ID второй сущности"
                />
                <div className="button-container">
                    <Button onClick={handleExchangeRings} label="Обмен кольцами"/>
                </div>
            </div>

            {successMessage && (
                <div className="success-message">
                    <p>{successMessage}</p>
                </div>
            )}

            {error && (
                <div className="error-message">
                    <p>{error}</p>
                </div>
            )}
        </div>
    );
}

export default SpecialActions;
