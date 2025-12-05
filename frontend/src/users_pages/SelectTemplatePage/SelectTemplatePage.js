import React, {useState, useEffect} from 'react';
import {useNavigate} from 'react-router-dom';
import {v4 as uuidv4} from 'uuid';
import './SelectTemplatePage.css';

function SelectTemplatePage() {
    const [templateDreams, setTemplateDreams] = useState([]);
    const [selectedTemplateId, setSelectedTemplateId] = useState(null);
    const [dreamId, setDreamId] = useState(null);
    const [isCharacterModalOpen, setIsCharacterModalOpen] = useState(false);
    const [selectedCharacters, setSelectedCharacters] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        const fetchTemplateDreams = async () => {
            const response = await fetch('http://localhost:8080/api/dreams/templates', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const data = await response.json();
                setTemplateDreams(data);

                if (data.length > 0) {
                    setSelectedTemplateId(data[0].id);
                }
            } else {
                console.error('Failed to fetch template dreams');
            }
        };

        fetchTemplateDreams();
    }, []);

    const handleTemplateSelect = (templateId, architectId, dream) => {
        setSelectedTemplateId(templateId);
        localStorage.setItem('selectedTemplateId', templateId);

        const newDreamId = uuidv4();
        setDreamId(newDreamId);
        localStorage.setItem('dreamId', newDreamId);

        localStorage.setItem('selectedArchitectId', architectId);

        localStorage.setItem('selectedDream', JSON.stringify(dream));
    };

    const handleGoBack = () => {
        window.history.back();
    };

    const handleChooseDateTime = () => {
        navigate(`/dreams/select-datetime/${dreamId}`);
    };

    const handleShowCharacters = async (dreamId) => {
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`http://localhost:8080/api/dreams/${dreamId}/characters`, {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            if (response.ok) {
                const characters = await response.json();
                setSelectedCharacters(characters);
                setIsCharacterModalOpen(true);
            } else {
                console.error('Failed to fetch characters');
            }
        } catch (error) {
            console.error('Error fetching characters:', error);
        }
    };

    const handleCloseCharacterModal = () => {
        setSelectedCharacters([]);
        setIsCharacterModalOpen(false);
    };

    return (
        <div className="select-template-container">
            <button className="back-button" onClick={handleGoBack}>Назад</button>
            <h2>Выберите шаблон</h2>
            {templateDreams.length > 0 ? (
                <div className="template-table-container">
                    <table className="template-table">
                        <thead>
                        <tr>
                            <th>Выбрать</th>
                            <th>Мечта</th>
                            <th>Эпоха</th>
                            <th>Виртуальная среда</th>
                            <th>Специальные способности</th>
                            <th>Правила физики</th>
                            <th>Роль</th>
                            <th>Жанр</th>
                            <th>Сценарий</th>
                            <th>Цена</th>
                            <th>Архитектор</th>
                            <th>Рейтинг архитектора</th>
                            <th>Персонажи</th>
                        </tr>
                        </thead>
                        <tbody>
                        {templateDreams.map((dream) => (
                            <tr key={dream.id}>
                                <td style={{textAlign: 'center'}}>
                                    <input
                                        type="radio"
                                        name="selectedTemplate"
                                        value={dream.id}
                                        checked={selectedTemplateId === dream.id}
                                        onChange={() => handleTemplateSelect(dream.id, dream.architectId, dream)}
                                    />
                                </td>
                                <td>{dream.name}</td>
                                <td>{dream.timeEra}</td>
                                <td>{dream.virtualEnvironment}</td>
                                <td>{dream.specialPowers}</td>
                                <td>{dream.physicalRules}</td>
                                <td>{dream.role}</td>
                                <td>{dream.genre}</td>
                                <td>{dream.scenario}</td>
                                <td>{dream.architectPrice}</td>
                                <td>{dream.architectName}</td>
                                <td>{dream.architectRating}</td>
                                <td>
                                    <button onClick={() => handleShowCharacters(dream.id)} className="action-button">
                                        Узнать
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <p className="no-templates">Шаблоны отсутствуют</p>
            )}

            {dreamId && (
                <button onClick={handleChooseDateTime}>Выбрать дату и время</button>
            )}

            {isCharacterModalOpen && (
                <div className="modal-overlay">
                    <div className="modal-content">
                        <h2>Персонажи</h2>
                        {selectedCharacters.length > 0 ? (
                            <table className="characters-table">
                                <thead>
                                <tr>
                                    <th>Имя</th>
                                    <th>Характеристика</th>
                                    <th>Внешность</th>
                                    <th>Роль</th>
                                </tr>
                                </thead>
                                <tbody>
                                {selectedCharacters.map((character, index) => (
                                    <tr key={index}>
                                        <td>{character.name}</td>
                                        <td>{character.characteristics || 'Нет'}</td>
                                        <td>{character.appearance || 'Нет'}</td>
                                        <td>{character.relation || 'Нет'}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>
                        ) : (
                            <p>Нет персонажей</p>
                        )}
                        <button onClick={handleCloseCharacterModal} className="close-modal-button">Закрыть</button>
                    </div>
                </div>
            )}
        </div>
    );
}

export default SelectTemplatePage;
