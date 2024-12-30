import React, { useState, useEffect } from 'react';
import './SelectTemplatePage.css';

function SelectTemplatePage() {
    const [templateDreams, setTemplateDreams] = useState([]);

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
            } else {
                console.error('Failed to fetch template dreams');
            }
        };

        fetchTemplateDreams();
    }, []);

    const handleGoBack = () => {
        window.history.back();
    };

    return (
        <div className="select-template-container">
            <button className="back-button" onClick={handleGoBack}>Назад</button>
            <h2>Выберите шаблон</h2>
            {templateDreams.length > 0 ? (
                <table className="template-table">
                    <thead>
                    <tr>
                        <th>Мечта</th>
                        <th>Эпоха</th>
                        <th>Виртуальная среда</th>
                        <th>Специальные способности</th>
                        <th>Правила физики</th>
                        <th>Роль</th>
                        <th>Жанр</th>
                        <th>Сценарий</th>
                    </tr>
                    </thead>
                    <tbody>
                    {templateDreams.map((dream) => (
                        <tr key={dream.name}>
                            <td>{dream.name}</td>
                            <td>{dream.timeEra}</td>
                            <td>{dream.virtualEnvironment}</td>
                            <td>{dream.specialPowers}</td>
                            <td>{dream.physicalRules}</td>
                            <td>{dream.role}</td>
                            <td>{dream.genre}</td>
                            <td>{dream.scenario}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            ) : (
                <p className="no-templates">Шаблоны отсутствуют</p>
            )}
        </div>
    );
}

export default SelectTemplatePage;
