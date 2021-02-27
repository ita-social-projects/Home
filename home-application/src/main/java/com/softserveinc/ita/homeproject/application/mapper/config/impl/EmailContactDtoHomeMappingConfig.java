package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailContactDtoHomeMappingConfig implements HomeMappingConfig<CreateEmailContact, ContactDto> {

    @Override
    public void addMappings(ModelMapper mapper) {
        mapper.typeMap(getSourceType(), getDestinationType())
            .setProvider(request -> mapper.map(request.getSource(), EmailContactDto.class));
    }
}
