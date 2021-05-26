package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.repository.ContactRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.UserContactService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserContactServiceImpl extends BaseContactService implements UserContactService {

    private static final String NOT_FOUND_MESSAGE = "User with 'id: %s' is not found";

    private final UserRepository userRepository;

    public UserContactServiceImpl(ContactRepository contactRepository,
                                  ServiceMapper mapper,
                                  UserRepository userRepository) {
        super(contactRepository, mapper);
        this.userRepository = userRepository;
    }

    @Override
    protected void checkAndFillParentEntity(ContactDto contactDto, Contact createContact, Long parentEntityId) {
        var user = getUserById(parentEntityId);
        if (Boolean.TRUE.equals(contactDto.getMain())) {
            List<Contact> allByUserIdAndType = contactRepository
                .findAllByUserIdAndType(parentEntityId, mapper.convert(contactDto.getType(), ContactType.class));
            allByUserIdAndType
                .stream()
                .filter(Contact::getMain)
                .findAny()
                .ifPresent(contact -> {
                    throw new AlreadyExistHomeException("User with id "
                        + parentEntityId + " already has main " + contactDto.getType() + " contact");
                });
        }
        createContact.setUser(user);
    }

    @Override
    protected Contact checkAndGetContactByParentId(Long id, Long parentEntityId) {
        var user = getUserById(parentEntityId);
        return user.getContacts().stream()
            .filter(Contact::getEnabled).filter(contact -> contact.getId().equals(id)).findFirst()
            .orElseThrow(() -> new NotFoundHomeException("Contact with id:" + id + " is not found"));
    }

    @Override
    protected <T extends Contact> Specification<T> updateSpecification(Specification<T> specification) {
        return specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("user").get("enabled"), true));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).filter(User::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, id)));
    }
}
