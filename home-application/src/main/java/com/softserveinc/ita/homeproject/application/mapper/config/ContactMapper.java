package com.softserveinc.ita.homeproject.application.mapper.config;

import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper implements HomeMappingConfig<CreateContact, ContactDto> {

    @Override
    public void addMappings(TypeMap<CreateContact, ContactDto> typeMap) {
        typeMap.include(CreateEmailContact.class, EmailContactDto.class);
        typeMap.include(CreatePhoneContact.class, PhoneContactDto.class);
    }
}
