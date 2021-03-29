package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailContactDtoHomeMappingConfig implements HomeMappingConfig<CreateEmailContact, ContactDto> {

    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<CreateEmailContact, ContactDto> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), EmailContactDto.class));
    }
}
