package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation;

import com.softserveinc.ita.homeproject.homeservice.dto.security.RoleDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CooperationInvitationDto extends InvitationDto{

    private RoleDto role;

    private String cooperationName;

}
