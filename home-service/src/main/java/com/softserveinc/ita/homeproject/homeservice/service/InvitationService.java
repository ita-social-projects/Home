package com.softserveinc.ita.homeproject.homeservice.service;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;

public interface InvitationService {

    CooperationInvitationDto createInvitation(CooperationInvitationDto invitation);

    CooperationInvitationDto getInvitation(Long id);

    List<CooperationInvitationDto> getAllActiveInvitations();

    void updateSentDateTime(Long id, LocalDateTime dateTime);

}
