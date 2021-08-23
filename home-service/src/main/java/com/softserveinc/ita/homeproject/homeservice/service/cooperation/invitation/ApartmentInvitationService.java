package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.invitation.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface ApartmentInvitationService extends InvitationService,
        QueryableService<ApartmentInvitation, ApartmentInvitationDto> {
    List<ApartmentInvitationDto> getAllActiveInvitations();

    void deactivateInvitationById(Long apartmentId, Long id);

    ApartmentInvitation updateInvitation(Long apartmentId, Long id, ApartmentInvitationDto updateInvitationDto);
}

