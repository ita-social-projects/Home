package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
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
