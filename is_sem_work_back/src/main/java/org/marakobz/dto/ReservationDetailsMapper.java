package org.marakobz.dto;

import org.mapstruct.*;
import org.marakobz.model.Reservation;

@Mapper(componentModel = "spring")
public interface ReservationDetailsMapper {

    // Mapping for ReservationDetailsDto
    @Mapping(source = "dream.name", target = "dreamName")
    @Mapping(source = "dream.timeEra", target = "timeEra")
    @Mapping(source = "dream.virtualEnvironment", target = "virtualEnvironment")
    @Mapping(source = "dream.specialPowers", target = "specialPowers")
    @Mapping(source = "dream.physicalRules", target = "physicalRules")
    @Mapping(source = "dream.role", target = "role")
    @Mapping(source = "dream.genre", target = "genre")
    @Mapping(source = "dream.scenario", target = "scenario")
    @Mapping(source = "dream.template", target = "template")
    @Mapping(source = "dream.price", target = "price")
    @Mapping(source = "calendar.date", target = "date", dateFormat = "dd-MM-yyyy")
    @Mapping(source = "calendar.time", target = "time", dateFormat = "HH:mm:ss")
    @Mapping(source = "timeOfReservation", target = "timeOfReservation")
    @Mapping(source = "architect.user.username", target = "architectUsername") // Architect's username
    @Mapping(source = "status", target = "status")
    ReservationDetailsDto toDetailsDto(Reservation reservation);

}
