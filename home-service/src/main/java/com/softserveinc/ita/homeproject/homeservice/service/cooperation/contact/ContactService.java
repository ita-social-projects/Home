package com.softserveinc.ita.homeproject.homeservice.service.cooperation.contact;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.contact.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface ContactService extends QueryableService<Contact, ContactDto> {

    ContactDto createContact(Long parentEntityId, ContactDto createContactDto);

    ContactDto updateContact(Long parentEntityId, Long id, ContactDto updateContactDto);

    void deactivateContact(Long id);
}
