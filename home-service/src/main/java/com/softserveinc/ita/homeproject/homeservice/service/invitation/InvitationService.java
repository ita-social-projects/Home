package com.softserveinc.ita.homeproject.homeservice.service.invitation;

import com.softserveinc.ita.homeproject.homedata.entity.invitation.Invitation;
import com.softserveinc.ita.homeproject.homeservice.dto.invitation.InvitationDto;

public interface InvitationService {

    InvitationDto createInvitation(InvitationDto invitation);

    void updateSentDateTimeAndStatus(Long id);

    void acceptUserInvitation(Invitation invitation);
}
