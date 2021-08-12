package com.softserveinc.ita.homeproject.homeservice.service.invitation;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.invitation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.invitation.InvitationService;

public interface CooperationInvitationService extends InvitationService {
    List<CooperationInvitationDto> getAllActiveInvitations();
}
