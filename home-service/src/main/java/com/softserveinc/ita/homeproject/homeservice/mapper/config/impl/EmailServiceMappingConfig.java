package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceMappingConfig implements ServiceMappingConfig<Email, ContactDto> {

    @Override
    public void addMappings(ModelMapper mapper) {
        mapper.typeMap(getSourceType(), getDestinationType())
            .setProvider(request -> mapper.map(request.getSource(), EmailContactDto.class));
    }
}
