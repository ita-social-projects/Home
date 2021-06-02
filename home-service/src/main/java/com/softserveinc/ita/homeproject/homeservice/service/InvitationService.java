package com.softserveinc.ita.homeproject.homeservice.service;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;

public interface InvitationService {

    InvitationDto createInvitation(InvitationDto invitation);

    InvitationDto getInvitation(Long id);

    void updateSentDateTimeAndStatus(Long id, LocalDateTime dateTime);
}
