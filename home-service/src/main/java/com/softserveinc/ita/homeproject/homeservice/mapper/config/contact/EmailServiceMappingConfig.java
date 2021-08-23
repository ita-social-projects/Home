package com.softserveinc.ita.homeproject.homeservice.mapper.config.contact;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.contact.EmailContact;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceMappingConfig implements ServiceMappingConfig<EmailContact, ContactDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<EmailContact, ContactDto> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), EmailContactDto.class));
    }
}
