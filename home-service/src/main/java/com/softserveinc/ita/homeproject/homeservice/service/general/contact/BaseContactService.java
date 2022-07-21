package com.softserveinc.ita.homeproject.homeservice.service.general.contact;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_FOUND_CONTACT_FORMAT_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_FOUND_TYPE_CONTACT_FORMAT_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_MATCH_TYPE_CONTACT_FORMAT_MESSAGE;

import javax.transaction.Transactional;

import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.general.contact.ContactRepository;
import com.softserveinc.ita.homeproject.homedata.general.contact.ContactType;
import com.softserveinc.ita.homeproject.homedata.general.contact.email.EmailContact;
import com.softserveinc.ita.homeproject.homedata.general.contact.phone.PhoneContact;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.EmailContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.PhoneContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.enums.ContactTypeDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class BaseContactService implements ContactService {

    protected final ContactRepository contactRepository;

    protected final ServiceMapper mapper;

    @Transactional
    @Override
    public ContactDto createContact(Long parentEntityId, ContactDto createContactDto) {
        Contact contact = mapper.convert(createContactDto, Contact.class);
        checkAndFillParentEntity(createContactDto, contact, parentEntityId);
        contact.setEnabled(true);
        contactRepository.save(contact);
        return mapper.convert(contact, ContactDto.class);
    }

    protected abstract void checkAndFillParentEntity(ContactDto contactDto, Contact createContact, Long parentEntityId);

    @Override
    @Transactional
    public ContactDto updateContact(Long parentEntityId, Long id, ContactDto updateContactDto) {
        Contact contact = checkAndGetContactByParentId(id, parentEntityId);

        ContactTypeDto existingContactType = mapper.convert(contact.getType(), ContactTypeDto.class);
        if (existingContactType == updateContactDto.getType()) {
            return updateContact(contact, updateContactDto);
        } else {
            throw new BadRequestHomeException(NOT_MATCH_TYPE_CONTACT_FORMAT_MESSAGE);
        }
    }

    private ContactDto updateContact(Contact contact, ContactDto updateContactDto) {
        if (contact.getType().equals(ContactType.PHONE)) {
            return updatePhone((PhoneContact) contact, (PhoneContactDto) updateContactDto);
        } else if (contact.getType().equals(ContactType.EMAIL)) {
            return updateEmail((EmailContact) contact, (EmailContactDto) updateContactDto);
        } else {
            throw new NotFoundHomeException(NOT_FOUND_TYPE_CONTACT_FORMAT_MESSAGE);
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

    protected abstract Contact checkAndGetContactByParentId(Long id, Long parentEntityId);

    protected abstract <T extends Contact> Specification<T> updateSpecification(Specification<T> specification);

    @Override
    public Page<ContactDto> findAll(Integer pageNumber, Integer pageSize, Specification<Contact> specification) {
        specification = updateSpecification(specification);
        return contactRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(contact -> mapper.convert(contact, ContactDto.class));
    }

    @Override
    public void deactivateContact(Long id) {
        Contact contact = contactRepository.findById(id).filter(Contact::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_CONTACT_FORMAT_MESSAGE, id)));
        contact.setEnabled(false);
        contactRepository.save(contact);
    }

}
