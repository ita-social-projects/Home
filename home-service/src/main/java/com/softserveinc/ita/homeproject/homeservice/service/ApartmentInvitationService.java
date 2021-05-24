package com.softserveinc.ita.homeproject.homeservice.service;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;


public interface ApartmentInvitationService extends InvitationService{
    List<ApartmentInvitationDto> getAllActiveInvitations();
}
