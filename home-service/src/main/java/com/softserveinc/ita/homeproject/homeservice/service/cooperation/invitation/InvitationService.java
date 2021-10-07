package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;

public interface InvitationService {

    InvitationDto createInvitation(InvitationDto invitation);

    void updateSentDateTimeAndStatus(Long id);

    void acceptUserInvitation(Invitation invitation);

    void registerWithRegistrationToken(String token);

    InvitationDto findInvitationByRegistrationToken(String token);
}
