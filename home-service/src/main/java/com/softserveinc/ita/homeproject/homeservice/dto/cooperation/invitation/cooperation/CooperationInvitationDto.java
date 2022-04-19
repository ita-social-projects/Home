package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation;

import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.role.RoleDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class CooperationInvitationDto extends InvitationDto {

    private RoleDto role;

    private Long cooperationId;
}
