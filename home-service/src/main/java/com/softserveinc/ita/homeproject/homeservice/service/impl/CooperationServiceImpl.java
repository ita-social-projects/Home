package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.Cooperation;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.RoleDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CooperationServiceImpl implements CooperationService {

    private static final String NOT_FOUND_COOPERATION_FORMAT = "Can't find cooperation with given ID: %d";

    private final CooperationInvitationService invitationService;

    private final CooperationRepository cooperationRepository;

    private final ServiceMapper mapper;

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

        invitationService.createInvitation(createInvitationForAdmin(createCooperationDto));

        return mapper.convert(cooperation, CooperationDto.class);
    }

    private CooperationInvitationDto createInvitationForAdmin(CooperationDto cooperationDto) {
        var invitationDto = new CooperationInvitationDto();
        invitationDto.setRole(RoleDto.ADMIN);
        invitationDto.setCooperationName(cooperationDto.getName());
        invitationDto.setEmail(cooperationDto.getAdminEmail());
        invitationDto.setType(InvitationTypeDto.COOPERATION);
        return invitationDto;
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
        fromDb.setUpdateDate(LocalDateTime.now());
        cooperationRepository.save(fromDb);
        return mapper.convert(fromDb, CooperationDto.class);
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
