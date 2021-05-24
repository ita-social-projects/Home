package com.softserveinc.ita.homeproject.homeservice.service;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;

public interface CooperationInvitationService extends InvitationService {
    List<CooperationInvitationDto> getAllActiveInvitations();
}
