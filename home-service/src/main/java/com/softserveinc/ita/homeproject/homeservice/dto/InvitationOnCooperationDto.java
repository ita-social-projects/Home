package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationOnCooperationDto extends BaseDto{

    private String email;

    private RoleDto role;
}
