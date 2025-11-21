package org.marakobz.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.marakobz.model.Admin;

@Mapper
public interface AdminRequestMapper {
    AdminRequestMapper INSTANCE = Mappers.getMapper(AdminRequestMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "status", target = "status")
    AdminRequestDto adminToAdminRequestDto(Admin admin);
}