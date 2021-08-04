package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;

public interface InvitationService {

    InvitationDto createInvitation(InvitationDto invitation);

    void updateSentDateTimeAndStatus(Long id);

    void acceptUserInvitation(Invitation invitation);
}
