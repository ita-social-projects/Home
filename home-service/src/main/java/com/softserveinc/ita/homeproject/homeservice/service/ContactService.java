package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import org.springframework.data.domain.Page;

public interface ContactService {

    ContactDto createContact(Long userId, ContactDto createContactDto);

    ContactDto updateContact(Long id, ContactDto updateContactDto);

    Page<ContactDto> getAllContacts(Long usersId, Integer pageNumber, Integer pageSize);

    ContactDto getContactById(Long id);

    void deactivateContact(Long id);
}
