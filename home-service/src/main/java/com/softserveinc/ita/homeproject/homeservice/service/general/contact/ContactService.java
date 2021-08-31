package com.softserveinc.ita.homeproject.homeservice.service.general.contact;

import com.softserveinc.ita.homeproject.homedata.general.entity.contact.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface ContactService extends QueryableService<Contact, ContactDto> {

    ContactDto createContact(Long parentEntityId, ContactDto createContactDto);

    ContactDto updateContact(Long parentEntityId, Long id, ContactDto updateContactDto);

    void deactivateContact(Long id);
}
