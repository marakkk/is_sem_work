package org.marakobz.dto;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.marakobz.dto.DreamTemplateDto;
import org.marakobz.model.Dream;

@Mapper
public interface DreamTemplateMapper {
    DreamTemplateMapper INSTANCE = Mappers.getMapper(DreamTemplateMapper.class);

    @Mapping(source = "architect.user.username", target = "architectName")
    @Mapping(source = "architect.rating", target = "architectRating")
    @Mapping(source = "architect.price", target = "architectPrice")
    @Mapping(source = "architect.id", target = "architectId")  //
    DreamTemplateDto dreamToDreamTemplateDto(Dream dream);
}
