package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.application.model.CreateCooperationInvitation;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class CooperationInvitationDTOMappingConfig
    implements HomeMappingConfig<CreateCooperationInvitation, CooperationInvitationDto> {

    @Override
    public void addMappings(TypeMap<CreateCooperationInvitation, CooperationInvitationDto> typeMap) {
        typeMap.addMappings(mp -> mp.skip(CooperationInvitationDto::setId));
    }
}
