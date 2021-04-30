package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.Contact;
import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.repository.ContactRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.exception.AlreadyExistHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationContactService;
import org.springframework.stereotype.Service;


@Service
public class CooperationContactServiceImpl extends BaseContactService implements CooperationContactService {

    private final CooperationServiceImpl cooperationService;

    public CooperationContactServiceImpl(ContactRepository contactRepository,
                                         ServiceMapper mapper,
                                         CooperationServiceImpl cooperationService) {
        super(contactRepository, mapper);
        this.cooperationService = cooperationService;
    }

    @Override
    protected void checkAndFillParentEntity(ContactDto contactDto, Contact createContact, Long parentEntityId) {
        var cooperation = mapper
            .convert(cooperationService.getCooperationById(parentEntityId), Cooperation.class);
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
    protected Contact checkAndGetContactByParentId(Long contactId, Long parentEntityId) {
        return contactRepository.findByIdAndCooperationId(contactId, parentEntityId)
            .filter(Contact::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException("User with id:" + contactId + " is not found"));
    }
}
