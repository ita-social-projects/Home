package com.softserveinc.ita.homeproject.homeservice.service;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.ApartmentInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;

public interface ApartmentInvitationService extends InvitationService,
        QueryableService<ApartmentInvitation, ApartmentInvitationDto> {
    List<ApartmentInvitationDto> getAllActiveInvitations();

    void deactivateInvitationById(Long apartmentId, Long id);

    ApartmentInvitation getInvitation(Long apartmentId, Long id);

    ApartmentInvitation updateInvitation(Long apartmentId, Long id, ApartmentInvitationDto updateInvitationDto);
}

