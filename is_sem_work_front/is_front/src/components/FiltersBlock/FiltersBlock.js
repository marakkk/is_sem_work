import React from 'react';
import './FiltersBlock.css';

function FiltersBlock({ filters, onFilterChange }) {
    const handleChange = (filterName, value) => {
        onFilterChange({ ...filters, [filterName]: value });
    };

    return (
        <div className="filters-and-actions">
            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Имя существа"
                    value={filters.name}
                    onChange={(e) => handleChange('name', e.target.value)}
                />
            </div>
            <div className="filter-container">
                <input
                    type="number"
                    placeholder="x"
                    value={filters.x}
                    onChange={(e) => handleChange('x', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="y"
                    value={filters.y}
                    onChange={(e) => handleChange('y', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Возраст"
                    value={filters.age}
                    onChange={(e) => handleChange('age', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <select
                    value={filters.type}
                    onChange={(e) => handleChange('creatureType', e.target.value)}
                >
                    <option value="">Выберите тип существа</option>
                    <option value="HOBBIT">HOBBIT</option>
                    <option value="ELF">ELF</option>
                    <option value="HUMAN">HUMAN</option>
                </select>
            </div>

            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Название города"
                    value={filters.location}
                    onChange={(e) => handleChange('creatureLocationName', e.target.value)}

                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Площадь"
                    value={filters.area}
                    onChange={(e) => handleChange('area', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Население"
                    value={filters.population}
                    onChange={(e) => handleChange('population', e.target.value)}

                />
            </div>

            <div className="filter-container">
                <select
                    value={filters.governor}
                    onChange={(e) => handleChange('governor', e.target.value)}
                >
                    <option value="">Выберите тип губернатора</option>
                    <option value="HOBBIT">HOBBIT</option>
                    <option value="ELF">ELF</option>
                    <option value="HUMAN">HUMAN</option>
                </select>
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Плотность населения"
                    value={filters.populationDensity}
                    onChange={(e) => handleChange('populationDensity', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Уровень атаки"
                    value={filters.attackLevel}
                    onChange={(e) => handleChange('attackLevel', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Уровень защиты"
                    value={filters.defenseLevel}
                    onChange={(e) => handleChange('defenseLevel', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="text"
                    placeholder="Имя кольца"
                    value={filters.ringPower}
                    onChange={(e) => handleChange('ringPower', e.target.value)}
                />
            </div>

            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Сила кольца"
                    value={filters.ringPower}
                    onChange={(e) => handleChange('ringPower', e.target.value)}
                />
            </div>


            <div className="filter-container">
                <input
                    type="number"
                    placeholder="Вес кольца"
                    value={filters.ringWeight}
                    onChange={(e) => handleChange('ringWeight', e.target.value)}
                />
            </div>


        </div>
    );
}

export default FiltersBlock;
