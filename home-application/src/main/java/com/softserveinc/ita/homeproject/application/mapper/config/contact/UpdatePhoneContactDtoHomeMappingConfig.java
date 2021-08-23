package com.softserveinc.ita.homeproject.application.mapper.config.contact;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact.PhoneContactDto;
import com.softserveinc.ita.homeproject.model.UpdatePhoneContact;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePhoneContactDtoHomeMappingConfig implements HomeMappingConfig<UpdatePhoneContact, ContactDto> {

    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<UpdatePhoneContact, ContactDto> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), PhoneContactDto.class));
    }
}
