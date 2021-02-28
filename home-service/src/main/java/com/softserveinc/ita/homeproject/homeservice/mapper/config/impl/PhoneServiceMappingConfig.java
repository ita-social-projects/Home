package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Phone;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class PhoneServiceMappingConfig implements ServiceMappingConfig {

    @Override
    public void addMappings(ModelMapper mapper) {
        mapper.typeMap(Phone.class, ContactDto.class)
            .setProvider(request -> mapper.map(request.getSource(), PhoneContactDto.class));
    }
}
