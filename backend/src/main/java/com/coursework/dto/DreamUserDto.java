package com.coursework.dto;

import lombok.Data;
import java.util.List;

@Data
public class DreamUserDto {
    private Long usersId;
    private String username;
    private String role;
    private Long adminId;
    private List<Long> reservationIds;
    private List<Long> architectIds;
    private List<Long> dreamIds;
}
