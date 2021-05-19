package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.repository.ContactRepository;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationContactService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CooperationContactServiceImpl extends BaseContactService implements CooperationContactService {

    private static final String NOT_FOUND_MESSAGE = "Cooperation with 'id: %s' is not found";

    private final CooperationRepository cooperationRepository;

    public CooperationContactServiceImpl(ContactRepository contactRepository,
                                         ServiceMapper mapper,
                                         CooperationRepository cooperationRepository) {
        super(contactRepository, mapper);
        this.cooperationRepository = cooperationRepository;
    }

    @Override
    protected void checkAndFillParentEntity(ContactDto contactDto, Contact createContact, Long parentEntityId) {
        var cooperation = getCooperationById(parentEntityId);
        if (Boolean.TRUE.equals(contactDto.getMain())) {
            List<Contact> getAllContactByCoopId = contactRepository
                .findAllByCooperationIdAndType(parentEntityId, mapper.convert(contactDto.getType(), ContactType.class));
            getAllContactByCoopId
                .stream()
                .filter(Contact::getMain)
                .findAny()
                .ifPresent(contact -> {
                    throw new AlreadyExistHomeException("Cooperation with id "
                        + parentEntityId + " already has main " + contactDto.getType() + " contact");
                });
        }

        createContact.setCooperation(cooperation);
    }

    @Override
    protected Contact checkAndGetContactByParentId(Long id, Long parentEntityId) {
        var cooperation = getCooperationById(parentEntityId);
        return cooperation.getContacts().stream()
            .filter(Contact::getEnabled).filter(contact -> contact.getId().equals(id)).findFirst()
            .orElseThrow(() -> new NotFoundHomeException("Contact with id:" + id + " is not found"));
    }

    @Override
    protected <T extends Contact> Specification<T> updateSpecification(Specification<T> specification) {
        return specification.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
            .equal(root.get("cooperation").get("enabled"), true));
    }

    private Cooperation getCooperationById(Long id) {
        return cooperationRepository.findById(id).filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, id)));
    }
}
