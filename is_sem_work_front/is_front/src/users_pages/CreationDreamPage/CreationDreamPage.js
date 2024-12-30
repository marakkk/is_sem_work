import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './CreationDreamPage.css';

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
    const [selectedCharacters, setSelectedCharacters] = useState([]);
    const [newCharacter, setNewCharacter] = useState({
        name: '',
        characteristics: '',
        appearance: '',
        relation: '',
        occupation: '',
    });

    const [errorMessage, setErrorMessage] = useState('');
    const [successMessage, setSuccessMessage] = useState('');
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [formErrors, setFormErrors] = useState({});
    const navigate = useNavigate();


    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            setErrorMessage('Вы не авторизованы. Пожалуйста, войдите в систему.');
        } else {
            setIsLoggedIn(true);
        }
    }, []);

    const handleNewCharacterChange = (field, value) => {
        setNewCharacter(prev => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleAddCharacter = () => {
        if (!newCharacter.name || !newCharacter.characteristics || !newCharacter.appearance || !newCharacter.relation || !newCharacter.occupation) {
            setErrorMessage('Пожалуйста, заполните все поля персонажа.');
            return;
        }

        setSelectedCharacters(prev => [
            ...prev,
            {
                ...newCharacter,
            },
        ]);

        setNewCharacter({
            name: '',
            characteristics: '',
            appearance: '',
            relation: '',
            occupation: '',
        });

        setErrorMessage('');
    };

    const handleDeleteCharacter = (index) => {
        setSelectedCharacters(prev => prev.filter((_, i) => i !== index));
    };

    const handleEditCharacter = (index) => {
        const characterToEdit = selectedCharacters[index];
        setNewCharacter(characterToEdit);
        handleDeleteCharacter(index);
    };

    const validateForm = () => {
        let errors = {};

        if (!name) errors.name = 'Пожалуйста, введите название сна.';
        if (!timeEra) errors.timeEra = 'Пожалуйста, выберите эру времени.';
        if (!virtualEnvironment) errors.virtualEnvironment = 'Пожалуйста, выберите виртуальную среду.';

        if (!role) errors.role = 'Пожалуйста, выберите роль.';
        if (!genre) errors.genre = 'Пожалуйста, выберите жанр.';
        if (selectedCharacters.length === 0) errors.characters = 'Добавьте хотя бы одного персонажа.';

        setFormErrors(errors);
        console.log("Ошибки валидации:", errors);

        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) return;

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
            characters: selectedCharacters.map(character => ({
                name: character.name,
                characteristics: character.characteristics,
                appearance: character.appearance,
                relation: character.relation,
                occupation: character.occupation,
            })),
        };

        const token = localStorage.getItem('token');

        try {
            const response = await fetch('http://localhost:8080/api/dreams/create-own-dream', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(dreamData),
            });

            if (response.ok) {
                setErrorMessage('');
                setSuccessMessage('Сон успешно создан!');

                setName('');
                setTimeEra('');
                setVirtualEnvironment('');
                setSpecialPowers('');
                setPhysicalRules('');
                setRole('');
                setGenre('');
                setScenario('');
                setTemplate(false);
                setPrice(0);
                setSelectedCharacters([]);
                setNewCharacter({
                    name: '',
                    characteristics: '',
                    appearance: '',
                    relation: '',
                    occupation: '',
                });
            } else {
                const errorText = await response.json();
                setErrorMessage(errorText.message || 'Ошибка при создании сна.');
            }
        } catch (error) {
            setErrorMessage('Произошла ошибка при отправке данных на сервер.');
        }
    };

    const handleBack = () => {
        navigate(-1);
    };
    return (
        <div className="create-dream-page">
            {!isLoggedIn ? (
                <div className="error-message">
                    {errorMessage}
                </div>
            ) : (
                <form onSubmit={handleSubmit}>
                    <div className="create-form-container">
                        <div>
                            <label>Название сна</label>
                            <input
                                type="text"
                                value={name}
                                onChange={(e) => setName(e.target.value)}
                            />
                            {formErrors.name && <div className="error-message">{formErrors.name}</div>}
                        </div>

                        <div>
                            <label>Эра времени</label>
                            <select
                                value={timeEra}
                                onChange={(e) => setTimeEra(e.target.value)}
                            >
                                <option value="">Выберите эру времени</option>
                                <option value="MEDIEVAL">Средневековье</option>
                                <option value="RENESANSE">Ренессанс</option>
                                <option value="MANUFACTURE_REVOLUTION">Промышленная революция</option>
                                <option value="VICTORIAN_TIME">Викторианская эпоха</option>
                                <option value="DISCO_TIME">80-е</option>
                            </select>
                            {formErrors.timeEra && <div className="error-message">{formErrors.timeEra}</div>}
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
                            {formErrors.virtualEnvironment &&
                                <div className="error-message">{formErrors.virtualEnvironment}</div>}
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
                            <select
                                value={role}
                                onChange={(e) => setRole(e.target.value)}
                                required
                            >
                                <option value="">Выберите роль во сне</option>
                                <option value="MAIN_CHARACTER">Главный герой</option>
                                <option value="SIDE_CHARACTER">Второстепенный герой</option>
                                <option value="VIEWER">Сторонний наблюдатель</option>
                            </select>
                            {formErrors.role && <div className="error-message">{formErrors.role}</div>}

                        </div>

                        <div>
                            <label>Жанр</label>
                            <select
                                value={genre}
                                onChange={(e) => setGenre(e.target.value)}
                                required
                            >
                                <option value="">Выберите жанр</option>
                                <option value="ADVENTURE">Приключения</option>
                                <option value="DRAMA">Драма</option>
                                <option value="HORROR">Хоррор</option>
                                <option value="FANTASY">Фэнтези</option>
                            </select>
                            {formErrors.genre && <div className="error-message">{formErrors.genre}</div>}

                        </div>

                        <div>
                            <label>Сценарий</label>
                            <input
                                type="text"
                                value={scenario}
                                onChange={(e) => setScenario(e.target.value)}
                            />
                            {formErrors.scenario && <div className="error-message">{formErrors.scenario}</div>}

                        </div>

                        <div>
                            <h3>Добавить персонажа</h3>
                            <div>
                                <label>Имя</label>
                                <input
                                    type="text"
                                    value={newCharacter.name}
                                    onChange={(e) => handleNewCharacterChange('name', e.target.value)}
                                />
                            </div>
                            <div>
                                <label>Характеристики</label>
                                <input
                                    type="text"
                                    value={newCharacter.characteristics}
                                    onChange={(e) => handleNewCharacterChange('characteristics', e.target.value)}
                                />
                            </div>
                            <div>
                                <label>Внешность</label>
                                <input
                                    type="text"
                                    value={newCharacter.appearance}
                                    onChange={(e) => handleNewCharacterChange('appearance', e.target.value)}
                                />
                            </div>
                            <div>
                                <label>Роль</label>
                                <select
                                    value={newCharacter.relation}
                                    onChange={(e) => handleNewCharacterChange('relation', e.target.value)}
                                >
                                    <option value="">Выберите роль</option>
                                    <option value="FRIEND">Друг</option>
                                    <option value="ENEMY">Враг</option>
                                    <option value="FATHER">Отец</option>
                                    <option value="MOTHER">Мать</option>
                                </select>
                            </div>
                            <div>
                                <label>Занятие</label>
                                <select
                                    value={newCharacter.occupation}
                                    onChange={(e) => handleNewCharacterChange('occupation', e.target.value)}
                                >
                                    <option value="">Выберите занятие</option>
                                    <option value="TEACHER">Учитель</option>
                                    <option value="DOCTOR">Доктор</option>
                                    <option value="ACTOR">Актер</option>
                                    <option value="PROGRAMMER">Программист</option>
                                </select>
                            </div>
                            <button type="button" onClick={handleAddCharacter}>Добавить персонажа</button>
                            {formErrors.characters && <div className="error-message">{formErrors.characters}</div>}
                        </div>
                    </div>

                    <h3>Добавленные персонажи</h3>
                    <ul>
                        {selectedCharacters.map((character, index) => (
                            <li key={index}>
                                {character.name}
                                <button type="button" onClick={() => handleDeleteCharacter(index)}>Удалить</button>
                                <button type="button" onClick={() => handleEditCharacter(index)}>Редактировать</button>
                            </li>
                        ))}
                    </ul>

                    {errorMessage && (
                        <div className="error-message">
                            {errorMessage}
                        </div>
                    )}


                    <div>
                        <button type="submit">Создать сон</button>
                        {successMessage && (
                            <div className="success-message">
                                {successMessage}
                            </div>
                        )}
                    </div>

                    <div>
                        <button
                            type="button"
                            onClick={handleBack}
                            className="back-button"
                        >
                            Назад
                        </button>
                    </div>

                </form>
            )}
        </div>
    );
}

export default CreateDreamPage;