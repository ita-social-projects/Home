package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.user.ownership.OwnershipDto;
import com.softserveinc.ita.homeproject.model.ReadOwnership;
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
