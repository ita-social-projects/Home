package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface ContactService extends QueryableService<Contact, ContactDto> {

    ContactDto createContact(Long parentEntityId, ContactDto createContactDto);

    ContactDto updateContact(Long parentEntityId, Long contactId, ContactDto updateContactDto);

    Page<ContactDto> findAll(Integer pageNumber, Integer pageSize, Specification<Contact> specification);

    ContactDto getContactById(Long id);

    void deactivateContact(Long id);
}
