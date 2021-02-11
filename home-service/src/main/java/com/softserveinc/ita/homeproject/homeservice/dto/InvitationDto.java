package com.softserveinc.ita.homeproject.homeservice.dto;

import java.time.LocalDateTime;
import com.softserveinc.ita.homeproject.homedata.entity.Role;

import lombok.Data;

@Data
public class InvitationDto extends BaseDto {

    private String name;
    private String email;
    private boolean status;
    private LocalDateTime createDateTime;
    private Role role;
}
