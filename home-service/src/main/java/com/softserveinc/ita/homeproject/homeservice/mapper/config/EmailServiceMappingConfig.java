package com.softserveinc.ita.homeproject.homeservice.mapper.config;

import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailServiceMappingConfig implements ServiceMappingConfig<Email, ContactDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<Email, ContactDto> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), EmailContactDto.class));
    }
}
