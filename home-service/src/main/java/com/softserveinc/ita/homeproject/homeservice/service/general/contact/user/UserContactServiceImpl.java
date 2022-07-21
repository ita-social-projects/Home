package com.softserveinc.ita.homeproject.homeservice.service.general.contact.user;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .ALERT_USER_HAS_MAIN_CONTACT_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_FOUND_CONTACT_ID_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_FOUND_USER_FORMAT_MESSAGE;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.general.contact.ContactRepository;
import com.softserveinc.ita.homeproject.homedata.general.contact.ContactType;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.general.contact.BaseContactService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserContactServiceImpl extends BaseContactService implements UserContactService {

    private final UserRepository userRepository;

    public UserContactServiceImpl(ContactRepository contactRepository,
                                  ServiceMapper mapper,
                                  UserRepository userRepository) {
        super(contactRepository, mapper);
        this.userRepository = userRepository;
    }

    @Override
    protected void checkAndFillParentEntity(ContactDto contactDto, Contact createContact, Long parentEntityId) {
        User user = getUserById(parentEntityId);
        if (Boolean.TRUE.equals(contactDto.getMain())) {
            List<Contact> allByUserIdAndType = contactRepository
                .findAllByUserIdAndType(parentEntityId, mapper.convert(contactDto.getType(), ContactType.class));
            allByUserIdAndType
                .stream()
                .filter(Contact::getMain)
                .findAny()
                .ifPresent(contact -> {
                    throw new BadRequestHomeException(String.format(ALERT_USER_HAS_MAIN_CONTACT_MESSAGE,
                            parentEntityId, contactDto.getType()));
                });
        }
        createContact.setUser(user);
    }

    @Override
    protected Contact checkAndGetContactByParentId(Long id, Long parentEntityId) {
        User user = getUserById(parentEntityId);
        return user.getContacts().stream()
            .filter(Contact::getEnabled).filter(contact -> contact.getId().equals(id)).findFirst()
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_CONTACT_ID_MESSAGE, id)));
    }

    @Override
    protected <T extends Contact> Specification<T> updateSpecification(Specification<T> specification) {
        return specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("user").get("enabled"), true));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).filter(User::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_USER_FORMAT_MESSAGE, id)));
    }
}
