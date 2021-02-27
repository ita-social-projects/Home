package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadPhoneContact;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadPhoneContactHomeMappingConfig implements HomeMappingConfig<PhoneContactDto, ReadContact> {

    @Override
    public void addMappings(ModelMapper mapper) {
        mapper.typeMap(getSourceType(), getDestinationType())
            .setProvider(request -> mapper.map(request.getSource(), ReadPhoneContact.class));
    }
}
