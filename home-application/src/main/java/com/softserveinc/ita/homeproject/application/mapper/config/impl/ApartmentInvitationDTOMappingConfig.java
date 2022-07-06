package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.application.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class ApartmentInvitationDTOMappingConfig
    implements HomeMappingConfig<CreateApartmentInvitation, ApartmentInvitationDto> {

    @Override
    public void addMappings(TypeMap<CreateApartmentInvitation, ApartmentInvitationDto> typeMap) {
        typeMap.addMappings(mp -> mp.skip(ApartmentInvitationDto::setId));
    }
}
