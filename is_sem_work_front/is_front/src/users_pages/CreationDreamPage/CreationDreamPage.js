import React, { useState, useEffect } from 'react';

function CreateDreamPage() {
    const [name, setName] = useState('');
    const [timeEra, setTimeEra] = useState('');
    const [virtualEnvironment, setVirtualEnvironment] = useState('');
    const [specialPowers, setSpecialPowers] = useState('');
    const [physicalRules, setPhysicalRules] = useState('');
    const [role, setRole] = useState('');
    const [genre, setGenre] = useState('');
    const [scenario, setScenario] = useState('');
    const [template, setTemplate] = useState(false);
    const [price, setPrice] = useState(0);
    const [characters, setCharacters] = useState([]);
    const [selectedCharacters, setSelectedCharacters] = useState([]);


    const handleCharacterChange = (characterId, field, value) => {
        setSelectedCharacters(prev => {
            const updated = prev.map(character =>
                character.characterId === characterId
                    ? { ...character, [field]: value }
                    : character
            );
            return updated;
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const dreamData = {
            name,
            timeEra,
            virtualEnvironment,
            specialPowers,
            physicalRules,
            role,
            genre,
            scenario,
            template,
            price,
            characterIds: selectedCharacters.map(character => character.characterId),
        };

        const token = localStorage.getItem('authToken');

        const response = await fetch('http://localhost:8080/api/dreams/create-own-dream', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`,
            },
            body: JSON.stringify(dreamData),
        });

        if (response.ok) {
            console.log('Dream created successfully');
        } else {
            console.error('Failed to create dream', response.status, await response.text());
        }
    };


    const handleSelectCharacter = (characterId) => {
        setSelectedCharacters(prev => [
            ...prev,
            { characterId, characteristics: '', appearance: '', relation: '', occupation: '' }
        ]);
    };

    return (
        <div className="create-dream-page">
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Название сна</label>
                    <input
                        type="text"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Эра времени</label>
                    <select
                        value={timeEra}
                        onChange={(e) => setTimeEra(e.target.value)}
                        required
                    >
                        <option value="">Выберите эру времени</option>
                        <option value="MEDIEVAL">Средневековье</option>
                        <option value="RENESANSE">Ренессанс</option>
                        <option value="MANUFACTURE_REVOLUTION">Промышленная революция</option>
                        <option value="VICTORIAN_TIME">Викторианская эпоха</option>
                        <option value="DISCO_TIME">80-е</option>

                    </select>
                </div>

                <div>
                    <label>Виртуальная среда</label>
                    <select
                        value={virtualEnvironment}
                        onChange={(e) => setVirtualEnvironment(e.target.value)}
                        required
                    >
                        <option value="">Выберите виртуальную среду</option>
                        <option value="CITY">Город</option>
                        <option value="NATURE">Природа</option>
                        <option value="FANTASY_WORLD">Мир фантазий</option>
                        <option value="COSMIC">Космос</option>
                        <option value="UNDERSEA_WORLD">Подводный мир</option>

                    </select>
                </div>

                <div>
                    <label>Особые силы</label>
                    <select
                        value={specialPowers}
                        onChange={(e) => setSpecialPowers(e.target.value)}
                        required
                    >
                        <option value="">Выберите специальные способности</option>
                        <option value="FLY">Умение летать</option>
                        <option value="TELEPORT">Телепортация</option>
                        <option value="TIME_MANIPULATION">Управление временем</option>

                    </select>
                </div>

                <div>
                    <label>Физические законы</label>
                    <select
                        value={physicalRules}
                        onChange={(e) => setPhysicalRules(e.target.value)}
                        required
                    >
                        <option value="">Выберите используемые физические законы</option>
                        <option value="TURN_OFF_HEAVY_POWER">Отключение гравитации</option>
                        <option value="CHANGE_TIME_SPEED">Изменение скорости времени</option>

                    </select>
                </div>

                <div>
                    <label>Роль</label>
                    <input
                        type="text"
                        value={role}
                        onChange={(e) => setRole(e.target.value)}
                        required
                    />
                </div>

                <div>
                    <label>Жанр</label>
                    <select
                        value={genre}
                        onChange={(e) => setGenre(e.target.value)}
                        required
                    >
                        <option value="">Выберите жанр</option>
                        <option value="ACTION">Экшн</option>
                        <option value="DRAMA">Драма</option>
                        <option value="COMEDY">Комедия</option>
                    </select>
                </div>

                <div>
                    <label>Сценарий</label>
                    <input
                        type="text"
                        value={scenario}
                        onChange={(e) => setScenario(e.target.value)}
                    />
                </div>

                <div>
                    <label>Цена</label>
                    <input
                        type="number"
                        value={price}
                        onChange={(e) => setPrice(Number(e.target.value))}
                    />
                </div>

                <div>
                    <label>Персонажи</label>
                    <div>
                        {characters.map((character) => (
                            <div key={character.id}>
                                <label>
                                    <input
                                        type="checkbox"
                                        onChange={() => handleSelectCharacter(character.id)}
                                    />
                                    {character.name}
                                </label>
                                {selectedCharacters.some(item => item.characterId === character.id) && (
                                    <div>
                                        <label>Характеристики</label>
                                        <input
                                            type="text"
                                            onChange={(e) => handleCharacterChange(character.id, 'characteristics', e.target.value)}
                                        />
                                        <label>Внешность</label>
                                        <input
                                            type="text"
                                            onChange={(e) => handleCharacterChange(character.id, 'appearance', e.target.value)}
                                        />
                                        <label>Роль</label>
                                        <input
                                            type="text"
                                            onChange={(e) => handleCharacterChange(character.id, 'relation', e.target.value)}
                                        />
                                        <label>Занятие</label>
                                        <input
                                            type="text"
                                            onChange={(e) => handleCharacterChange(character.id, 'occupation', e.target.value)}
                                        />
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                </div>

                <button type="submit">Создать сон</button>
            </form>
        </div>
    );
}

export default CreateDreamPage;
