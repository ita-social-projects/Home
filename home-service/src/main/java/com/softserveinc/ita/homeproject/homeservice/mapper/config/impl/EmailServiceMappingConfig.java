package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmailServiceMappingConfig implements ServiceMappingConfig {

    @Override
    public void addMappings(ModelMapper mapper) {
        mapper.typeMap(Email.class, ContactDto.class)
            .setProvider(request -> mapper.map(request.getSource(), EmailContactDto.class));
    }
}
