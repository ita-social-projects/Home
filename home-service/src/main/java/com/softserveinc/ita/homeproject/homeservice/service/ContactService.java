package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface ContactService extends QueryableService<Contact, ContactDto> {

    ContactDto createContact(Long userId, ContactDto createContactDto);

    ContactDto updateContact(Long id, ContactDto updateContactDto);

    Page<ContactDto> findAll(Integer pageNumber, Integer pageSize, Specification<Contact> specification);

    void deactivateContact(Long id);
}
