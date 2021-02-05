package com.softserveinc.ita.homeproject.homeservice.service;

import java.util.List;
import java.util.Set;

import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;

public interface InvitationService {

    InvitationDto createInvitation(InvitationDto invitation);

    void changeInvitationStatus(Set<Long> ids);

    InvitationDto getInvitation(Long id);

    List<InvitationDto> getAllActiveInvitations();

}
