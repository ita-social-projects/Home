package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CooperationInvitationDto extends InvitationDto{

    private String role;

    private String cooperationName;

}
