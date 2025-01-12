import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { v4 as uuidv4 } from 'uuid';
import './SelectTemplatePage.css';

function SelectTemplatePage() {
    const [templateDreams, setTemplateDreams] = useState([]);
    const [selectedTemplateId, setSelectedTemplateId] = useState(null);
    const [selectedDream, setSelectedDream] = useState(null);
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        const fetchTemplateDreams = async () => {
            try {
                const response = await fetch('http://localhost:8080/api/dreams/templates', {
                    headers: { Authorization: `Bearer ${token}` },
                });

                if (response.ok) {
                    const data = await response.json();
                    setTemplateDreams(data);

                    // Set initial selected template if necessary
                    if (data.length > 0) {
                        setSelectedTemplateId(data[0].id);
                        setSelectedDream(data[0]);
                    }
                } else {
                    console.error('Failed to fetch template dreams');
                }
            } catch (error) {
                console.error('Error fetching templates:', error);
            }
        };

        fetchTemplateDreams();
    }, [navigate]);


    const handleTemplateSelect = (templateId, dream) => {
        setSelectedTemplateId(templateId);

        const dreamId = uuidv4();
        // Assign a new unique id to the dream while keeping the template ID
        const dreamWithNewId = { ...dream, id: dreamId, originalTemplateId: templateId };

        setSelectedDream(dreamWithNewId);

        try {
            // Get existing dreams from localStorage
            const storedDreams = JSON.parse(localStorage.getItem('dreams')) || [];

            // Add the new dream to the array
            storedDreams.push(dreamWithNewId);

            // Save updated array back to localStorage
            localStorage.setItem('dreams', JSON.stringify(storedDreams));

            // Optionally log the stored dreams for debugging
            console.log('Stored dreams updated:', storedDreams);
        } catch (error) {
            console.error('Error saving dream to localStorage:', error);
        }

        console.log('Template selected and saved with new ID:', dreamWithNewId);
    };

    const handleChooseDateTime = () => {
        if (selectedDream) {
            const dreamId = selectedDream.id;

            // Navigate to date and time selection page with the selected dream ID
            navigate(`/dreams/select-datetime/${dreamId}`);
        } else {
            setErrorMessage('Пожалуйста, выберите шаблон перед сохранением.');
        }
    };

    const handleGoBack = () => {
        navigate(-1);
    };

    return (
        <div className="select-template-container">
            <button className="back-button" onClick={handleGoBack}>
                Назад
            </button>
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
                            <th>Шаблон</th>
                            <th>Архитектор</th>
                            <th>Рейтинг архитектора</th>
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
                                        onChange={() => handleTemplateSelect(dream.id, dream)}
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
                                <td>{dream.template}</td>
                                <td>{dream.architectName}</td>
                                <td>{dream.architectRating}</td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <p className="no-templates">Шаблоны отсутствуют</p>
            )}

            {errorMessage && <p className="error-message">{errorMessage}</p>}

            {/* Button to move to the next page for date and time selection */}
            {selectedTemplateId && (
                <div>
                    <button onClick={handleChooseDateTime}>Выбрать дату и время</button>
                </div>
            )}
        </div>
    );
}

export default SelectTemplatePage;
