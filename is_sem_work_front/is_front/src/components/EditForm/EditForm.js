import React, { useState, useEffect } from 'react';
import './EditForm.css';
import Button from "../Button/Button";

const EditForm = (args) => {
    const finalClassName = 'edit-form ' + (args.visible ? '' : 'edit-form--hidden') + (args.className || '');
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

    useEffect(() => {
        if (args.creature) {
            setName(args.creature.name || '');
            setX(args.creature.coordinates?.x || '');
            setY(args.creature.coordinates?.y || '');
            setAge(args.creature.age || '');
            setCreatureType(args.creature.creatureType || '');
            setCreatureLocationName(args.creature.creatureLocation?.name || '');
            setArea(args.creature.creatureLocation?.area || '');
            setPopulation(args.creature.creatureLocation?.population || '');
            setEstablishmentDate(args.creature.creatureLocation?.establishmentDate || '');
            setGovernor(args.creature.creatureLocation?.governor || '');
            setCapital(args.creature.creatureLocation?.capital || false);
            setPopulationDensity(args.creature.creatureLocation?.populationDensity || '');
            setAttackLevel(args.creature.attackLevel || '');
            setDefenseLevel(args.creature.defenseLevel || '');
            setRingName(args.creature.ring?.name || '');
            setRingPower(args.creature.ring?.power || '');
            setRingWeight(args.creature.ring?.weight || '');
            setApproveUpdates(args.creature.approveUpdates || false);

        }
    }, [args.creature]);

    const handleSubmit = (e) => {
        e.preventDefault();

        args.onUpdate(args.creature.id, {
            name,
            coordinates: { x: parseFloat(x), y: parseFloat(y) },
            age: parseInt(age),
            creatureType,
            creatureLocation: {
                name: creatureLocationName,
                area: parseFloat(area) > 0 ? parseFloat(area) : null,
                population: parseInt(population) > 0 ? parseInt(population) : null,
                establishmentDate,
                governor: governor || null,
                capital,
                populationDensity: parseFloat(populationDensity) > 0 ? parseFloat(populationDensity) : null
            },
            attackLevel: parseFloat(attackLevel) || null,
            defenseLevel: parseFloat(defenseLevel) || null,
            ring: ringName ? {
                name: ringName,
                power: parseInt(ringPower) > 0 ? parseInt(ringPower) : null,
                weight: parseFloat(ringWeight) > 0 ? parseFloat(ringWeight) : null
            } : null,
            approveUpdates,

        });

        // Reset form fields after submit
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
        args.func(false);
    };

    return (
        <div className={finalClassName}>
            <form className="form edit-form__form scrollable-form" onSubmit={handleSubmit}>
                <div className='form__couple'>
                    <label className='form__label'>Имя</label>
                    <input className='form__input' value={name} onChange={(e) => setName(e.target.value)} required/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Координата X</label>
                    <input className='form__input' type="number" value={x} onChange={(e) => setX(e.target.value)}
                           required/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Координата Y</label>
                    <input className='form__input' type="number" value={y} onChange={(e) => setY(e.target.value)}
                           required/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Возраст</label>
                    <input className='form__input' type="number" value={age} onChange={(e) => setAge(e.target.value)}
                           min="0"/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Тип существа</label>
                    <select className='form__input' value={creatureType}
                            onChange={(e) => setCreatureType(e.target.value)}>
                        <option value="">Выберите тип существа</option>
                        <option value="HOBBIT">Хоббит</option>
                        <option value="ELF">Эльф</option>
                        <option value="HUMAN">Человек</option>
                    </select>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Мир существа</label>
                    <input className='form__input' value={creatureLocationName}
                           onChange={(e) => setCreatureLocationName(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Площадь</label>
                    <input className='form__input' type="number" value={area}
                           onChange={(e) => setArea(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Население</label>
                    <input className='form__input' type="number" value={population}
                           onChange={(e) => setPopulation(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Дата основания</label>
                    <input className='form__input' type="date" value={establishmentDate}
                           onChange={(e) => setEstablishmentDate(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Губернатор</label>
                    <select className='form__input' value={governor} onChange={(e) => setGovernor(e.target.value)}>
                        <option value="">Выберите тип губернатора</option>
                        <option value="HOBBIT">Хоббит</option>
                        <option value="ELF">Эльф</option>
                        <option value="HUMAN">Человек</option>
                    </select>
                </div>
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
                <div className='form__couple'>
                    <label className='form__label'>Плотность населения</label>
                    <input className='form__input' type="number" value={populationDensity}
                           onChange={(e) => setPopulationDensity(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Уровень атаки</label>
                    <input className='form__input' type="number" value={attackLevel}
                           onChange={(e) => setAttackLevel(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Уровень защиты</label>
                    <input className='form__input' type="number" value={defenseLevel}
                           onChange={(e) => setDefenseLevel(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Имя кольца</label>
                    <input className='form__input' value={ringName} onChange={(e) => setRingName(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Сила кольца</label>
                    <input className='form__input' type="number" value={ringPower}
                           onChange={(e) => setRingPower(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Вес кольца</label>
                    <input className='form__input' type="number" value={ringWeight}
                           onChange={(e) => setRingWeight(e.target.value)}/>
                </div>
                <div className='form__couple'>
                    <label className='form__label'>Одобрить обновления</label>
                    <input
                        type="checkbox"
                        checked={approveUpdates}
                        onChange={(e) => setApproveUpdates(e.target.checked)}
                    />
                </div>

                <div className="form-actions">
                    <Button className='form__button button--rounded button--mini' onClick={handleSubmit}
                            label="Применить"/>
                </div>
            </form>
        </div>
    );
};

export default EditForm;
