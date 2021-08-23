package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.CooperationInvitationDto;

public interface CooperationInvitationService extends InvitationService {
    List<CooperationInvitationDto> getAllActiveInvitations();
}
