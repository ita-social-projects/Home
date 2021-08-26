package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.InvitationDto;

public interface InvitationService {

    InvitationDto createInvitation(InvitationDto invitation);

    void updateSentDateTimeAndStatus(Long id);

    void acceptUserInvitation(Invitation invitation);

    void registerWithRegistrationToken(String token);

    InvitationDto findInvitationByRegistrationToken(String token);
}
