package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.PhoneContact;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneContactDtoServiceMappingConfig implements ServiceMappingConfig<PhoneContactDto, Contact> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<PhoneContactDto, Contact> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), PhoneContact.class));
    }
}
