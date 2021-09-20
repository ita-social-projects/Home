package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationStatusDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitationDto extends BaseDto {

    private InvitationStatusDto status;

    private InvitationTypeDto type;

    private String email;

    private String registrationToken;

}
