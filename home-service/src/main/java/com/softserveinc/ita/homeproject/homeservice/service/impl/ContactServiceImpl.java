package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.Email;
import com.softserveinc.ita.homeproject.homedata.entity.Phone;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.ContactRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ContactService;
import com.softserveinc.ita.homeproject.homeservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;
    private final ServiceMapper mapper;
    private final UserService userService;

    @Override
    public ContactDto createContact(Long userId, ContactDto createContactDto) {
        Contact contact = mapper.convert(createContactDto, Contact.class);
        User user = mapper.convert(userService.getUserById(userId), User.class);
        contact.setUser(user);
        contactRepository.save(contact);
        return mapper.convert(contact, ContactDto.class);
    }

    @Transactional
    @Override
    public ContactDto updateContact(Long id, ContactDto updateContactDto) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            if (contact instanceof Phone) {
                Phone phone = (Phone) contact;
                phone.setPhone(((PhoneContactDto) updateContactDto).getPhone());
                contactRepository.save(contact);
                return mapper.convert(phone, PhoneContactDto.class);
            } else {
                Email email = (Email) contact;
                email.setEmail(((EmailContactDto) updateContactDto).getEmail());
                contactRepository.save(contact);
                return mapper.convert(email, EmailContactDto.class);
            }
        } else {
            throw new NotFoundHomeException("User with id:" + id + " is not found");
        }
    }

    @Transactional
    @Override
    public Page<ContactDto> getAllContacts(Long usersId, Integer pageNumber, Integer pageSize) {
        return contactRepository.getAllByUser_Id(usersId, PageRequest.of(pageNumber - 1, pageSize))
                .map(contact -> mapper.convert(contact, ContactDto.class));
    }

    @Override
    public ContactDto getContactById(Long id) {
        Contact contactResponse = contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Can't find contact with given ID:" + id));
        return mapper.convert(contactResponse, ContactDto.class);
    }

    @Override
    public void deactivateContact(Long id) {
        contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("Can't find contact with given ID:" + id));
        contactRepository.deleteById(id);
    }
}