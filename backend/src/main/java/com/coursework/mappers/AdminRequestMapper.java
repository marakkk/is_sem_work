package com.coursework.mappers;

import com.coursework.dto.AdminRequestDto;
import com.coursework.model.Admin;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AdminRequestMapper {
    AdminRequestMapper INSTANCE = Mappers.getMapper(AdminRequestMapper.class);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "status", target = "status")
    AdminRequestDto adminToAdminRequestDto(Admin admin);
}