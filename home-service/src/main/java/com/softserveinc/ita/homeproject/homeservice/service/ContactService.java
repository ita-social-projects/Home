package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;

public interface ContactService extends QueryableService<Contact, ContactDto> {

    ContactDto createContact(Long userId, ContactDto createContactDto);

    ContactDto updateContact(Long id, ContactDto updateContactDto);

    void deactivateContact(Long id);
}
