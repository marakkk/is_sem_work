import React, { useState, useEffect } from 'react';
import './BookCreatureList.css';
import CreatureListElement from '../CreatureListElement/CreatureListElement';
import FiltersBlock from '../FiltersBlock/FiltersBlock';
import Button from '../Button/Button';
import EditForm from '../EditForm/EditForm';
import { getBookCreatures, getFilteredBookCreatures, deleteBookCreature, updateBookCreature } from '../../services/api';

function BookCreatureList({ onUpdate, onDelete, username}) {
    const [selectedCreatures, setSelectedCreatures] = useState([]);
    const [filters, setFilters] = useState({
        creatorName:'',
        name: '',
        x: '',
        y: '',
        age: '',
        creatureType: '',
        locationName: '',
        area: '',
        population: '',
        governor: '',
        populationDensity: '',
        attackLevel: '',
        defenseLevel: '',
        ringName: '',
        ringPower: '',
        ringWeight: '',
    });
    const [sortField, setSortField] = useState(null);
    const [sortOrder, setSortOrder] = useState(null);

    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [creatures, setBookCreatures] = useState([]);
    const [editFormVisible, setEditFormVisible] = useState(false);
    const [selectedCreature, setSelectedCreature] = useState(null);
    const [showEditWarning, setShowEditWarning] = useState(false);
    const [permissionError, setPermissionError] = useState(null);

    const token = localStorage.getItem('jwtToken');

    const isApproved = sessionStorage.getItem('isApproved') === 'true';

    const fetchCreatures = async (page = 0) => {
        try {
            let response = await getFilteredBookCreatures(token, { ...filters, page, sortField, sortDirection: sortOrder?.toUpperCase() ?? null });

            setBookCreatures(response.content);

            setTotalPages(Math.ceil(response.totalElements / 5));
        } catch (error) {
            console.error('Error fetching creatures:', error);
        }
    };

    useEffect(() => {
        const intervalId = setInterval(() => {
            fetchCreatures(currentPage - 1);
        }, 40000);

        return () => clearInterval(intervalId);
    }, [fetchCreatures]);

    useEffect(() => {
        fetchCreatures(currentPage - 1);
    }, [filters, currentPage, sortField, sortOrder]);

    const handleFilterChange = (updatedFilters) => {
        setFilters(updatedFilters);
        setCurrentPage(1);
    };

    const handlePageChange = (page) => {
        setCurrentPage(page);
    };

    const handlePrevious = () => {
        if (currentPage > 1) {
            handlePageChange(currentPage - 1);
        }
    };

    const handleNext = () => {
        if (currentPage < totalPages) {
            handlePageChange(currentPage + 1);
        }
    };

    const handleSelectCreature = (id) => {
        const updatedSelection = selectedCreatures.includes(id)
            ? selectedCreatures.filter(creatureId => creatureId !== id)
            : [...selectedCreatures, id];

        setSelectedCreatures(updatedSelection);

        if (updatedSelection.length === 0) {
            setEditFormVisible(false);
            setSelectedCreature(null);
            setShowEditWarning(false);
        } else if (updatedSelection.length === 1) {
            const creatureToEdit = creatures.find(creature => creature.id === updatedSelection[0]);
            setSelectedCreature(creatureToEdit);
            setShowEditWarning(false);
        } else {
            setEditFormVisible(false);
            setShowEditWarning(false);
        }
    };

    const handleMassDelete = () => {
        let errorOccurred = false;

        selectedCreatures.forEach(creatureId => {
            const creatureToDelete = creatures.find(creature => creature.id === creatureId);

            if (creatureToDelete.creatorName === username || isApproved) {
                onDelete(creatureId);
            } else {
                errorOccurred = true;
            }
        });


        setEditFormVisible(false);
        setPermissionError(errorOccurred ? "У вас нет прав на удаление этого существа." : null);
    };

    const handleEditSelected = () => {
        if (selectedCreatures.length === 1) {
            const creatureToEdit = creatures.find(creature => creature.id === selectedCreatures[0]);

            if (creatureToEdit.creatorName === username || (isApproved && creatureToEdit.approveUpdates)) {
                setSelectedCreature(creatureToEdit);
                setEditFormVisible(true);
            } else {
                setPermissionError("У вас нет прав на редактирование этого существа.");
            }
        } else {
            setShowEditWarning(true);
        }
    };


    const handleSort = (field) => {
        if (sortField === field) {
            setSortOrder((prevOrder) => (prevOrder === 'asc' ? 'desc' : 'asc'));
        } else {
            setSortField(field);
            setSortOrder('asc');
        }
    };

    return (
        <div className='book-creature-list'>
            <FiltersBlock filters={filters} onFilterChange={handleFilterChange} />

            <div className="filters-and-actions">
                <div className="actions-block">
                    <Button
                        className="form__button button--rounded button--mini"
                        onClick={handleMassDelete}
                        label="Удалить выбранных"
                        disabled={selectedCreatures.length === 0}
                    />
                    <Button
                        className="form__button button--rounded button--mini"
                        onClick={handleEditSelected}
                        label="Редактировать выбранных"
                        disabled={selectedCreatures.length !== 1}
                    />
                </div>
            </div>

            {showEditWarning && (
                <div className="no-selection-message">
                    Пожалуйста, выберите одно существо для редактирования.
                </div>
            )}

            {permissionError && (
                <div className="permission-error-message">
                    {permissionError}
                    <button className="clear-error-button" onClick={() => setPermissionError(null)}>
                        &times;
                    </button>
                </div>
            )}

            <div className="table-container">
                <table className="creature-details-table">
                    <thead>
                    <tr>
                        <th>Выбрать</th>
                        <th>Создатель</th>
                        <th
                            onClick={() => handleSort('name')}
                            className={`sortable ${sortField === 'name' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Имя
                        </th>
                        <th>Координаты</th>
                        <th>Дата создания</th>
                        <th
                            onClick={() => handleSort('age')}
                            className={`sortable ${sortField === 'age' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Возраст
                        </th>
                        <th>Тип</th>
                        <th>Название города</th>
                        <th
                            onClick={() => handleSort('creatureLocation.area')}
                            className={`sortable ${sortField === 'creatureLocation.area' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Площадь
                        </th>
                        <th
                            onClick={() => handleSort('creatureLocation.population')}
                            className={`sortable ${sortField === 'creatureLocation.population' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Население
                        </th>
                        <th>Дата основания</th>
                        <th>Губернатор</th>
                        <th>Столица</th>
                        <th
                            onClick={() => handleSort('creatureLocation.populationDensity')}
                            className={`sortable ${sortField === 'creatureLocation.populationDensity' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Плотность населения
                        </th>
                        <th
                            onClick={() => handleSort('attackLevel')}
                            className={`sortable ${sortField === 'attackLevel' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Уровень атаки
                        </th>
                        <th
                            onClick={() => handleSort('defenseLevel')}
                            className={`sortable ${sortField === 'defenseLevel' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Уровень защиты
                        </th>
                        <th>Название кольца</th>
                        <th
                            onClick={() => handleSort('ring.power')}
                            className={`sortable ${sortField === 'ring.power' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Сила кольца
                        </th>
                        <th
                            onClick={() => handleSort('ring.weight')}
                            className={`sortable ${sortField === 'ring.weight' ? (sortOrder === 'asc' ? 'sort-asc' : 'sort-desc') : ''}`}>
                            Вес кольца
                        </th>
                        <th>Редактирование</th>
                    </tr>
                    </thead>
                    <tbody>
                    {creatures.map(creature => (
                        <CreatureListElement
                            key={creature.id}
                            creature={creature}
                            onUpdate={onUpdate}
                            onSelect={handleSelectCreature}
                            isSelected={selectedCreatures.includes(creature.id)}
                        />
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="pagination">
                <button
                    className="pagination__button"
                    onClick={handlePrevious}
                    disabled={currentPage === 1}
                >
                    Предыдущая
                </button>

                <span className="pagination__info">
                    Страница {currentPage} из {totalPages}
                </span>

                <button
                    className="pagination__button"
                    onClick={handleNext}
                    disabled={currentPage === totalPages}
                >
                    Следующая
                </button>
            </div>

            {editFormVisible && selectedCreature && (
                <EditForm
                    visible={editFormVisible}
                    creature={selectedCreature}
                    onUpdate={(id, updatedCreature) => {
                        onUpdate(id, updatedCreature);
                        setEditFormVisible(false);
                    }}
                    func={setEditFormVisible}
                    className="edit-form"
                />
            )}
        </div>
    );
}

export default BookCreatureList;
