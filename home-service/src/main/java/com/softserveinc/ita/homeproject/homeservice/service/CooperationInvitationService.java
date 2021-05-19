package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import java.util.List;

public interface CooperationInvitationService extends InvitationService {
    List<CooperationInvitationDto> getAllActiveInvitations();
}
