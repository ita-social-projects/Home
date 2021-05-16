package com.softserveinc.ita.homeproject.homeservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationOnCooperationDto;

public interface InvitationService {

    InvitationOnCooperationDto createInvitation(InvitationOnCooperationDto invitation);

    InvitationOnCooperationDto getInvitation(Long id);

    List<InvitationOnCooperationDto> getAllActiveInvitations();

    void updateSentDateTime(Long id, LocalDateTime dateTime);

}
