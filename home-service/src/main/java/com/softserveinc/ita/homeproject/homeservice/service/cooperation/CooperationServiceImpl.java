package com.softserveinc.ita.homeproject.homeservice.service.cooperation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.general.contact.Contact;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.cooperation.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.general.contact.ContactDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.role.RoleDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.cooperation.invitation.InvitationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CooperationServiceImpl implements CooperationService {

    private static final String NOT_FOUND_COOPERATION_FORMAT = "Can't find cooperation with given ID: %d";

    private final InvitationService<CooperationInvitation, CooperationInvitationDto> invitationService;

    private final CooperationRepository cooperationRepository;

    private final ServiceMapper mapper;

    public CooperationServiceImpl(
        @Qualifier("cooperationInvitationServiceImpl")
            InvitationService<CooperationInvitation, CooperationInvitationDto> invitationService,
        CooperationRepository cooperationRepository,
        ServiceMapper mapper) {
        this.invitationService = invitationService;
        this.cooperationRepository = cooperationRepository;
        this.mapper = mapper;
    }

    @Transactional
    @Override
    public CooperationDto createCooperation(CooperationDto createCooperationDto) {
        Cooperation cooperation = mapper.convert(createCooperationDto, Cooperation.class);
        cooperation.setEnabled(true);
        cooperation.setRegisterDate(LocalDate.now());
        cooperation.getHouses().forEach(element -> {
            element.setCooperation(cooperation);
            element.setCreateDate(LocalDateTime.now());
            element.setEnabled(true);
        });
        cooperation.getContacts().forEach(contact -> {
            contact.setCooperation(cooperation);
            contact.setEnabled(true);
        });

        cooperationRepository.save(cooperation);

        Long id = cooperation.getId();

        createCooperationDto.setId(id);

        invitationService.createInvitation(createInvitationForAdmin(id, createCooperationDto.getAdminEmail()));

        return mapper.convert(cooperation, CooperationDto.class);
    }

    private CooperationInvitationDto createInvitationForAdmin(Long cooperationId, String adminEmail) {
        return CooperationInvitationDto.builder()
            .role(RoleDto.COOPERATION_ADMIN)
            .cooperationId(cooperationId)
            .email(adminEmail)
            .type(InvitationTypeDto.COOPERATION).build();
    }

    @Transactional
    @Override
    public CooperationDto updateCooperation(Long id, CooperationDto updateCooperationDto) {
        Cooperation fromDb = cooperationRepository.findById(id)
            .filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_COOPERATION_FORMAT, id)));

        if (updateCooperationDto.getName() != null) {
            fromDb.setName(updateCooperationDto.getName());
        }
        if (updateCooperationDto.getUsreo() != null) {
            fromDb.setUsreo(updateCooperationDto.getUsreo());
        }
        if (updateCooperationDto.getIban() != null) {
            fromDb.setIban(updateCooperationDto.getIban());
        }
        if (updateCooperationDto.getAddress() != null) {
            fromDb.setAddress(updateCooperationDto.getAddress());
        }
        if (updateCooperationDto.getContacts() != null) {
            fromDb.getContacts().clear();
            List<ContactDto> contactDtoList = updateCooperationDto.getContacts();
            for (ContactDto contactDto : contactDtoList) {
                Contact contact = mapper.convert(contactDto, Contact.class);
                contact.setCooperation(fromDb);
                contact.setEnabled(true);
                fromDb.getContacts().add(contact);
            }
        }
        fromDb.setUpdateDate(LocalDateTime.now());
        Cooperation updatedCooperation = cooperationRepository.save(fromDb);
        return mapper.convert(updatedCooperation, CooperationDto.class);
    }

    @Override
    public Page<CooperationDto> findAll(Integer pageNumber, Integer pageSize,
                                        Specification<Cooperation> specification) {
        return cooperationRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(cooperation -> mapper.convert(cooperation, CooperationDto.class));
    }

    @Override
    public void deactivateCooperation(Long id) {
        Cooperation toDelete = cooperationRepository.findById(id).filter(Cooperation::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_COOPERATION_FORMAT, id)));
        toDelete.setEnabled(false);
        cooperationRepository.save(toDelete);
    }
}
