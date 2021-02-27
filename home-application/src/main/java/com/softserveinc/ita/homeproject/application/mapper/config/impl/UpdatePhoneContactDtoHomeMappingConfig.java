package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.model.UpdatePhoneContact;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UpdatePhoneContactDtoHomeMappingConfig implements HomeMappingConfig<UpdatePhoneContact, ContactDto> {

    @Override
    public void addMappings(ModelMapper mapper) {
        mapper.typeMap(getSourceType(), getDestinationType())
            .setProvider(request -> mapper.map(request.getSource(), PhoneContactDto.class));
    }
}
