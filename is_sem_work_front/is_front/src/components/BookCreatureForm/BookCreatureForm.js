import React, { useState } from 'react';
import SimpleRow from '../SimpleRow/SimpleRow';
import Button from '../Button/Button';
import './BookCreatureForm.css';

function BookCreatureForm({ onCreate }) {
    const [name, setName] = useState('');
    const [x, setX] = useState('');
    const [y, setY] = useState('');
    const [age, setAge] = useState('');
    const [creatureType, setCreatureType] = useState('');
    const [creatureLocationName, setCreatureLocationName] = useState('');
    const [area, setArea] = useState('');
    const [population, setPopulation] = useState('');
    const [establishmentDate, setEstablishmentDate] = useState('');
    const [governor, setGovernor] = useState('');
    const [capital, setCapital] = useState(false);
    const [populationDensity, setPopulationDensity] = useState('');
    const [attackLevel, setAttackLevel] = useState('');
    const [defenseLevel, setDefenseLevel] = useState('');
    const [ringName, setRingName] = useState('');
    const [ringPower, setRingPower] = useState('');
    const [ringWeight, setRingWeight] = useState('');
    const [approveUpdates, setApproveUpdates] = useState(false);

    const [errors, setErrors] = useState({});

    const handleSubmit = async (e) => {
        e.preventDefault();

        setErrors({});
        const newErrors = {};

        if (!name || name.trim() === '') newErrors.name = "Имя не может быть пустым";

        if (isNaN(parseFloat(x))) newErrors.x = "Координата X должна быть числом";

        if (isNaN(parseFloat(y))) newErrors.y = "Координата Y должна быть числом";
        else if (parseFloat(y) <= -935) newErrors.y = "Координата Y должна быть больше -935";

        if (isNaN(parseInt(age)) || parseInt(age) <= 0) newErrors.age = "Возраст должен быть больше 0";

        if (!creatureLocationName || creatureLocationName.trim() === '')
            newErrors.creatureLocationName = "Имя локации не может быть пустым";

        if (isNaN(parseFloat(area)) || parseFloat(area) <= 0) newErrors.area = "Площадь должна быть больше 0";

        if (isNaN(parseInt(population)) || parseInt(population) <= 0)
            newErrors.population = "Население должно быть больше 0";

        if (isNaN(parseFloat(populationDensity)) || parseFloat(populationDensity) <= 0)
            newErrors.populationDensity = "Плотность населения должна быть больше 0";

        if (isNaN(parseFloat(attackLevel)) || parseFloat(attackLevel) <= 0)
            newErrors.attackLevel = "Уровень атаки должен быть больше 0";

        if (isNaN(parseFloat(defenseLevel)) || parseFloat(defenseLevel) <= 0)
            newErrors.defenseLevel = "Уровень защиты должен быть больше 0";

        if (!ringName || ringName.trim() === '') newErrors.ringName = "Имя кольца не может быть пустым";

        if (isNaN(parseFloat(ringWeight)) || parseFloat(ringWeight) <= 0)
            newErrors.ringWeight = "Вес кольца должен быть больше 0";

        if (Object.keys(newErrors).length > 0) {
            setErrors(newErrors);
            return;
        }

        try {
            await onCreate({
                name,
                coordinates: {x: parseFloat(x), y: parseFloat(y)},
                creationDate: new Date().toISOString(),
                age: parseInt(age),
                creatureType: creatureType || null,
                creatureLocation: {
                    name: creatureLocationName,
                    area: parseFloat(area),
                    population: parseInt(population),
                    establishmentDate: establishmentDate || null,
                    governor: governor || null,
                    capital,
                    populationDensity: parseFloat(populationDensity),
                },
                attackLevel: parseFloat(attackLevel),
                defenseLevel: parseFloat(defenseLevel),
                ring: {
                    name: ringName,
                    power: parseInt(ringPower) > 0 ? parseInt(ringPower) : null,
                    weight: parseFloat(ringWeight),
                },
            });
        } catch (error) {
            if (error.response) {
                if (error.response.status === 409) {
                    setErrors({general: `Ошибка: ${error.response.data.message}`});
                } else {
                    setErrors({general: `Ошибка: ${error.response.data}`});
                }
            } else {
                setErrors({general: `Ошибка: ${error.message || 'Неизвестная ошибка'}`});
            }
        }

        setName('');
        setX('');
        setY('');
        setAge('');
        setCreatureType('');
        setCreatureLocationName('');
        setArea('');
        setPopulation('');
        setEstablishmentDate('');
        setGovernor('');
        setCapital(false);
        setPopulationDensity('');
        setAttackLevel('');
        setDefenseLevel('');
        setRingName('');
        setRingPower('');
        setRingWeight('');
        setApproveUpdates(false);

    };

    return (
        <form className="form" onSubmit={handleSubmit}>
            {errors.general && <p className="error-message">{errors.general}</p>}


            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Имя</label>
                    <input
                        className="form__input form__input--fixed"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        required
                    />
                    {errors.name && <p className="error-message">{errors.name}</p>}
                </div>
                <div className="form__couple">
                    <label className="form__label">Координата X</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={x}
                        onChange={(e) => setX(e.target.value)}
                        required
                    />
                    {errors.x && <p className="error-message">{errors.x}</p>}
                </div>
            </SimpleRow>

            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Координата Y</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={y}
                        onChange={(e) => setY(e.target.value)}
                        required
                    />
                    {errors.y && <p className="error-message">{errors.y}</p>}
                </div>
                <div className="form__couple">
                    <label className="form__label">Возраст</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={age}
                        onChange={(e) => setAge(e.target.value)}
                        required
                    />
                    {errors.age && <p className="error-message">{errors.age}</p>}
                </div>
            </SimpleRow>

            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Тип существа</label>
                    <select
                        className="form__input form__input--fixed"
                        value={creatureType}
                        onChange={(e) => setCreatureType(e.target.value)}
                    >
                        <option value="">Выберите тип существа</option>
                        <option value="HOBBIT">Хоббит</option>
                        <option value="ELF">Эльф</option>
                        <option value="HUMAN">Человек</option>
                    </select>
                </div>
                <div className="form__couple">
                    <label className="form__label">Мир существа</label>
                    <input
                        className="form__input form__input--fixed"
                        value={creatureLocationName}
                        onChange={(e) => setCreatureLocationName(e.target.value)}
                    />
                    {errors.creatureLocationName&& <p className="error-message">{errors.creatureLocationName}</p>}
                </div>
            </SimpleRow>

            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Площадь</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={area}
                        onChange={(e) => setArea(e.target.value)}
                    />
                    {errors.area && <p className="error-message">{errors.area}</p>}
                </div>
                <div className="form__couple">
                    <label className="form__label">Население</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={population}
                        onChange={(e) => setPopulation(e.target.value)}
                    />
                    {errors.population && <p className="error-message">{errors.population}</p>}
                </div>
            </SimpleRow>

            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Дата основания</label>
                    <input
                        className="form__input form__input--fixed"
                        type="date"
                        value={establishmentDate}
                        onChange={(e) => setEstablishmentDate(e.target.value)}
                    />
                </div>
                <div className="form__couple">
                    <label className="form__label">Губернатор</label>
                    <select
                        className="form__input form__input--fixed"
                        value={governor}
                        onChange={(e) => setGovernor(e.target.value)}
                    >
                        <option value="">Выберите тип губернатора</option>
                        <option value="HOBBIT">Хоббит</option>
                        <option value="ELF">Эльф</option>
                        <option value="HUMAN">Человек</option>
                    </select>
                </div>
            </SimpleRow>
            <div className="form__couple">
                <label className="form__label">Столица</label>
                <div className="form__capital-buttons">
                    <button
                        type="button"
                        className={`form__capital-button ${capital ? 'selected' : ''}`}
                        onClick={() => setCapital(true)}
                    >
                        Да
                    </button>
                    <button
                        type="button"
                        className={`form__capital-button ${!capital ? 'selected' : ''}`}
                        onClick={() => setCapital(false)}
                    >
                        Нет
                    </button>
                </div>
            </div>
            <SimpleRow>
                <div className="form__couple form__couple--wide">
                    <label className="form__label">Плотность населения</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={populationDensity}
                        onChange={(e) => setPopulationDensity(e.target.value)}
                    />
                    {errors.populationDensity && <p className="error-message">{errors.populationDensity}</p>}
                </div>
                <div className="form__couple form__couple--wide">
                    <label className="form__label">Кольцо (имя)</label>
                    <input
                        className="form__input form__input--fixed"
                        value={ringName}
                        onChange={(e) => setRingName(e.target.value)}
                    />
                    {errors.ringName && <p className="error-message">{errors.ringName}</p>}
                </div>
            </SimpleRow>
            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Уровень атаки</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={attackLevel}
                        onChange={(e) => setAttackLevel(e.target.value)}
                    />
                    {errors.attackLevel && <p className="error-message">{errors.attackLevel}</p>}
                </div>
                <div className="form__couple">
                    <label className="form__label">Уровень защиты</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={defenseLevel}
                        onChange={(e) => setDefenseLevel(e.target.value)}
                    />
                    {errors.defenseLevel && <p className="error-message">{errors.defenseLevel}</p>}
                </div>
            </SimpleRow>
            <SimpleRow>
                <div className="form__couple">
                    <label className="form__label">Мощность кольца</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={ringPower}
                        onChange={(e) => setRingPower(e.target.value)}
                    />
                </div>
                <div className="form__couple">
                    <label className="form__label">Вес кольца</label>
                    <input
                        className="form__input form__input--fixed"
                        type="number"
                        value={ringWeight}
                        onChange={(e) => setRingWeight(e.target.value)}
                    />
                    {errors.ringWeight && <p className="error-message">{errors.ringWeight}</p>}
                </div>

            </SimpleRow>
            <SimpleRow>
                <div className="form__couple form__couple--wide">
                    <label className="form__label">
                        <input
                            type="checkbox"
                            checked={approveUpdates}
                            onChange={(e) => setApproveUpdates(e.target.checked)}
                        />
                        Разрешить изменения
                    </label>
                </div>
            </SimpleRow>
            <div className="form__buttons">
                <Button className="form__button button--rounded button--mini" onClick={handleSubmit} label="Создать"/>
            </div>
        </form>
    );
}

export default BookCreatureForm;

