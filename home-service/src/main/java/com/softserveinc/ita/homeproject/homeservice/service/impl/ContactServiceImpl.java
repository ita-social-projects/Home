package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import com.softserveinc.ita.homeproject.homedata.entity.EmailContact;
import com.softserveinc.ita.homeproject.homedata.entity.PhoneContact;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.ContactRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PhoneContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.TypeOfTheContactDoesntMatchHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    private final UserRepository userRepository;

    private final ServiceMapper mapper;

    @Override
    public ContactDto createContact(Long userId, ContactDto createContactDto) {
        Contact contact = mapper.convert(createContactDto, Contact.class);
        User user = getUserById(userId);
        contact.setUser(user);
        contact.setEnabled(true);
        contactRepository.save(contact);
        return mapper.convert(contact, ContactDto.class);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundHomeException("User with id " + id + "wasn't found"));
    }

    @Override
    public ContactDto updateContact(Long id, ContactDto updateContactDto) {
        Optional<Contact> contactOptional = contactRepository.findById(id).filter(Contact::getEnabled);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            ContactTypeDto existingContactType = mapper.convert(contact.getType(), ContactTypeDto.class);
            if (existingContactType == updateContactDto.getType()) {
                return updateContact(contact, updateContactDto);
            } else {
                throw new TypeOfTheContactDoesntMatchHomeException("Type of the contact doesn't match");
            }
        } else {
            throw new NotFoundHomeException("User with id:" + id + " is not found");
        }
    }

    private ContactDto updateContact(Contact contact, ContactDto updateContactDto) {
        if (contact.getType().equals(ContactType.PHONE)) {
            return updatePhone((PhoneContact) contact, (PhoneContactDto) updateContactDto);
        } else if (contact.getType().equals(ContactType.EMAIL)) {
            return updateEmail((EmailContact) contact, (EmailContactDto) updateContactDto);
        } else {
            throw new NotFoundHomeException("Type of the contact is not found");
        }
    }

    private ContactDto updatePhone(PhoneContact phone, PhoneContactDto phoneContactDto) {
        phone.setPhone(phoneContactDto.getPhone());
        contactRepository.save(phone);
        return mapper.convert(phone, PhoneContactDto.class);
    }

    private ContactDto updateEmail(EmailContact email, EmailContactDto emailContactDto) {
        email.setEmail(emailContactDto.getEmail());
        contactRepository.save(email);
        return mapper.convert(email, EmailContactDto.class);
    }

    @Override
    public Page<ContactDto> getAllContacts(Integer pageNumber, Integer pageSize,
                                            Specification<Contact> specification) {
        Specification<Contact> contactSpecification = specification
            .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("enabled"), true));
        return contactRepository.findAll(contactSpecification, PageRequest.of(pageNumber - 1, pageSize))
                .map(contact -> mapper.convert(contact, ContactDto.class));
    }

    @Override
    public ContactDto getContactById(Long id) {
        Contact contactResponse = contactRepository.findById(id).filter(Contact::getEnabled)
                .orElseThrow(() -> new NotFoundHomeException("Can't find contact with given ID:" + id));
        return mapper.convert(contactResponse, ContactDto.class);
    }

    @Override
    public void deactivateContact(Long id) {
        Contact contact = contactRepository.findById(id).filter(Contact::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException("Can't find contact with given ID:" + id));
        contact.setEnabled(false);
        contactRepository.save(contact);
    }

}
