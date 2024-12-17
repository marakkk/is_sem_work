import React, { useEffect, useState } from 'react';
import './CreatureListElement.css';

const CreatureListElement = ({ creature, onSelect, isSelected }) => {
    const formattedCreationDate = creature.creationDate;

    const formattedEstablishmentDate = new Date(creature.creatureLocation.establishmentDate).toLocaleDateString('ru-RU', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    });

    return (
        <tr>
            <td>
                <input
                    type="checkbox"
                    checked={isSelected}
                    onChange={() => onSelect(creature.id)}
                />
            </td>
            <td>{creature.creatorName}</td>
            <td>{creature.name}</td>
            <td>X: {creature.coordinates.x}, Y: {creature.coordinates.y}</td>
            <td>{formattedCreationDate}</td>
            <td>{creature.age}</td>
            <td>{creature.creatureType}</td>
            <td>{creature.creatureLocation.name}</td>
            <td>{creature.creatureLocation.area}</td>
            <td>{creature.creatureLocation.population}</td>
            <td>{formattedEstablishmentDate}</td>
            <td>{creature.creatureLocation.governor}</td>
            <td>{creature.creatureLocation.capital ? 'Да' : 'Нет'}</td>
            <td>{creature.creatureLocation.populationDensity}</td>
            <td>{creature.attackLevel}</td>
            <td>{creature.defenseLevel}</td>
            <td>{creature.ring.name}</td>
            <td>{creature.ring.power}</td>
            <td>{creature.ring.weight}</td>
            <td>{creature.approveUpdates ? "Одобрено" : "Не одобрено"}</td>
        </tr>
    );
};

export default CreatureListElement;

