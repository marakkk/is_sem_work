import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import './CreationTemplatesPage.css';

function CreationTemplatesPage() {
    const [formData, setFormData] = useState({
        name: '',
        timeEra: '',
        virtualEnvironment: '',
        specialPowers: '',
        physicalRules: '',
        role: '',
        genre: '',
        scenario: '',
        price: 0,
        selectedCharacters: [],
    });

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
            return;
        }

        try {
            setIsLoggedIn(true);
        } catch (error) {
            setErrorMessage('Ошибка при обработке токена.');
        }
    }, []);

    const handleChange = (field, value) => {
        setFormData(prev => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleNewCharacterChange = (field, value) => {
        setNewCharacter(prev => ({
            ...prev,
            [field]: value,
        }));
    };

    const handleAddCharacter = () => {
        const {name, characteristics, appearance, relation, occupation} = newCharacter;
        if (!name || !characteristics || !appearance || !relation || !occupation) {
            setErrorMessage('Заполните все поля персонажа.');
            return;
        }

        setFormData(prev => ({
            ...prev,
            selectedCharacters: [...prev.selectedCharacters, {...newCharacter}],
        }));

        setNewCharacter({
            name: '',
            characteristics: '',
            appearance: '',
            relation: '',
            occupation: '',
        });

        setErrorMessage('');
    };

    const handleEditCharacter = (index) => {
        const selected = formData.selectedCharacters[index];
        setNewCharacter(selected);
        handleDeleteCharacter(index);
    };

    const handleDeleteCharacter = (index) => {
        setFormData(prev => ({
            ...prev,
            selectedCharacters: prev.selectedCharacters.filter((_, i) => i !== index),
        }));
    };

    const validateForm = () => {
        const errors = {};
        const {name, timeEra, virtualEnvironment, role, genre, price, selectedCharacters} = formData;

        if (!name) errors.name = 'Введите название шаблона.';
        if (!timeEra) errors.timeEra = 'Выберите эру времени.';
        if (!virtualEnvironment) errors.virtualEnvironment = 'Выберите виртуальную среду.';
        if (price <= 0) errors.price = 'Цена должна быть больше нуля.';
        if (!role) errors.role = 'Выберите роль.';
        if (!genre) errors.genre = 'Выберите жанр.';
        if (selectedCharacters.length === 0) errors.selectedCharacters = 'Добавьте хотя бы одного персонажа.';

        setFormErrors(errors);
        return Object.keys(errors).length === 0;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!validateForm()) return;

        const token = localStorage.getItem('token');
        const {selectedCharacters, ...otherFields} = formData;

        const templateData = {
            ...otherFields,
            characters: selectedCharacters,
            template: true,
        };

        try {
            const response = await fetch('http://localhost:8080/api/dreams/architect/create-template', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`,
                },
                body: JSON.stringify(templateData),
            });

            if (response.ok) {
                setSuccessMessage('Шаблон успешно создан!');
                resetForm();
            } else {
                const errorText = await response.json();
                setErrorMessage(errorText.message || 'Ошибка при создании шаблона.');
            }
        } catch (error) {
            setErrorMessage('Ошибка при отправке данных на сервер.');
        }
    };

    const resetForm = () => {
        setFormData({
            name: '',
            timeEra: '',
            virtualEnvironment: '',
            specialPowers: '',
            physicalRules: '',
            role: '',
            genre: '',
            scenario: '',
            price: 0,
            selectedCharacters: [],
        });
        setFormErrors({});
        setSuccessMessage('');
    };

    const handleBack = () => {
        navigate(-1);
    };

    return (
        <div className="create-template-page">
            {!isLoggedIn ? (
                <div className="error-message">{errorMessage}</div>
            ) : (
                <form onSubmit={handleSubmit}>
                    <div className="create-form-container">
                        <div>
                            <label>Название шаблона</label>
                            <input
                                type="text"
                                value={formData.name}
                                onChange={(e) => handleChange('name', e.target.value)}
                            />
                            {formErrors.name && <div className="error-message">{formErrors.name}</div>}
                        </div>


                        <div>
                            <label>Цена</label>
                            <input
                                type="number"
                                value={formData.price}
                                onChange={(e) => handleChange('price', Number(e.target.value))}
                                min="0"
                            />
                            {formErrors.price && <div className="error-message">{formErrors.price}</div>}
                        </div>


                        <div>
                            <label>Эра времени</label>
                            <select
                                value={formData.timeEra}
                                onChange={(e) => handleChange('timeEra', e.target.value)}
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
                                value={formData.virtualEnvironment}
                                onChange={(e) => handleChange('virtualEnvironment', e.target.value)}
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
                                value={formData.specialPowers}
                                onChange={(e) => handleChange('specialPowers', e.target.value)}
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
                                value={formData.physicalRules}
                                onChange={(e) => handleChange('physicalRules', e.target.value)}
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
                                value={formData.role}
                                onChange={(e) => handleChange('role', e.target.value)}
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
                                value={formData.genre}
                                onChange={(e) => handleChange('genre', e.target.value)}
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
                                value={formData.scenario}
                                onChange={(e) => handleChange('scenario', e.target.value)}
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


                        <div>
                            <h3>Добавленные персонажи</h3>
                            <ul>
                                {formData.selectedCharacters.map((character, index) => (
                                    <li key={index}>
                                        {character.name}
                                        <button type="button" onClick={() => handleDeleteCharacter(index)}>Удалить
                                        </button>
                                        <button type="button" onClick={() => handleEditCharacter(index)}>Редактировать
                                        </button>
                                    </li>
                                ))}
                            </ul>
                            {formErrors.selectedCharacters && (
                                <div className="error-message">{formErrors.selectedCharacters}</div>
                            )}
                        </div>

                        {errorMessage && <div className="error-message">{errorMessage}</div>}
                        <div>
                            <button type="submit">Создать шаблон</button>
                            {successMessage && <div className="success-message">{successMessage}</div>}
                        </div>

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

export default CreationTemplatesPage;