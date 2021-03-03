package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.EmailContact;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailContactDtoServiceMappingConfig implements ServiceMappingConfig<EmailContactDto, Contact> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<EmailContactDto, Contact> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), EmailContact.class));
    }
}
