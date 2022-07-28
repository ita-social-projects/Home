package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation;

import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationStatusDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.service.general.email.Mailable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InvitationDto extends Mailable {

    private InvitationStatusDto status;

    private InvitationTypeDto type;

    private String email;

    private String token;

}
