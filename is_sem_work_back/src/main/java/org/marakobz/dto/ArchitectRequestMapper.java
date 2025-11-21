package org.marakobz.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.marakobz.model.Architect;

@Mapper
public interface ArchitectRequestMapper {
    ArchitectRequestMapper INSTANCE = Mappers.getMapper(ArchitectRequestMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "status", target = "status")
    ArchitectRequestDto architectToArchitectRequestDto(Architect architect);
}