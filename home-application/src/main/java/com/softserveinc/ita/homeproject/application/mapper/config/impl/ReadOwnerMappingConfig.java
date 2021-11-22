package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.application.model.ReadOwnership;
import com.softserveinc.ita.homeproject.homeservice.dto.user.ownership.OwnershipDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadOwnerMappingConfig implements HomeMappingConfig<OwnershipDto, ReadOwnership> {

    @Override
    public void addMappings(TypeMap<OwnershipDto, ReadOwnership> typeMap) {
        typeMap.addMapping(OwnershipDto::getUser, ReadOwnership::setOwner);
    }
}
