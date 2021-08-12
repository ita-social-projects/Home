package com.softserveinc.ita.homeproject.homeservice.service.contact;

import com.softserveinc.ita.homeproject.homedata.entity.contact.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface ContactService extends QueryableService<Contact, ContactDto> {

    ContactDto createContact(Long parentEntityId, ContactDto createContactDto);

    ContactDto updateContact(Long parentEntityId, Long id, ContactDto updateContactDto);

    void deactivateContact(Long id);
}
