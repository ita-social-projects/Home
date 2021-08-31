package com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.apartment;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;

public interface ApartmentInvitationService extends InvitationService,
        QueryableService<ApartmentInvitation, ApartmentInvitationDto> {
    List<ApartmentInvitationDto> getAllActiveInvitations();

    void deactivateInvitationById(Long apartmentId, Long id);

    ApartmentInvitation updateInvitation(Long apartmentId, Long id, ApartmentInvitationDto updateInvitationDto);
}

