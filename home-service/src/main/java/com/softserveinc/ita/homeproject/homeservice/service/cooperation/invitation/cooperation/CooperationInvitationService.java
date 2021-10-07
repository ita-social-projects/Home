package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.cooperation;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;

public interface CooperationInvitationService extends InvitationService {
    List<CooperationInvitationDto> getAllActiveInvitations();
}
