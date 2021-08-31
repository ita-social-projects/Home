package com.softserveinc.ita.homeproject.homeservice.mapper.config.contact;

import com.softserveinc.ita.homeproject.homedata.general.entity.contact.PhoneContact;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.PhoneContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PhoneServiceMappingConfig implements ServiceMappingConfig<PhoneContact, ContactDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<PhoneContact, ContactDto> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), PhoneContactDto.class));
    }
}
