package com.softserveinc.ita.homeproject.homeservice.service.general.contact.cooperation;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_COOPERATION_HAS_MAIN_CONTACT_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_COOPERATION_MESSAGE;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homedata.general.contact.ContactRepository;
import com.softserveinc.ita.homeproject.homedata.general.contact.ContactType;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.general.contact.BaseContactService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CooperationContactServiceImpl extends BaseContactService implements CooperationContactService {

    private final CooperationRepository cooperationRepository;

    public CooperationContactServiceImpl(ContactRepository contactRepository,
                                         ServiceMapper mapper,
                                         CooperationRepository cooperationRepository) {
        super(contactRepository, mapper);
        this.cooperationRepository = cooperationRepository;
    }

    @Override
    protected void checkAndFillParentEntity(ContactDto contactDto, Contact createContact, Long parentEntityId) {
        Cooperation cooperation = getCooperationById(parentEntityId);
        if (Boolean.TRUE.equals(contactDto.getMain())) {
            List<Contact> getAllContactByCoopId = contactRepository
                .findAllByCooperationIdAndType(parentEntityId, mapper.convert(contactDto.getType(), ContactType.class));
            getAllContactByCoopId
                .stream()
                .filter(Contact::getMain)
                .findAny()
                .ifPresent(contact -> {
                    throw new BadRequestHomeException(String.format(ALERT_COOPERATION_HAS_MAIN_CONTACT_MESSAGE,
                            parentEntityId, contactDto.getType()));
                });
        }

        createContact.setCooperation(cooperation);
    }

    @Override
    protected Contact checkAndGetContactByParentId(Long id, Long parentEntityId) {
        Cooperation cooperation = getCooperationById(parentEntityId);
        return cooperation.getContacts().stream()
            .filter(Contact::getEnabled).filter(contact -> contact.getId().equals(id)).findFirst()
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_COOPERATION_MESSAGE, id)));
    }

    @Override
    protected <T extends Contact> Specification<T> updateSpecification(Specification<T> specification) {
        return specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("cooperation").get("enabled"), true));
    }

    private Cooperation getCooperationById(Long id) {
        return cooperationRepository.findById(id).filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_COOPERATION_MESSAGE, id)));
    }
}
