package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.CooperationInvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CooperationInvitationServiceImpl extends InvitationServiceImpl implements CooperationInvitationService {

    private final CooperationInvitationRepository cooperationInvitationRepository;

    public CooperationInvitationServiceImpl(InvitationRepository invitationRepository,
                                            ServiceMapper mapper,
                                            CooperationInvitationRepository cooperationInvitationRepository) {
        super(invitationRepository, mapper);
        this.cooperationInvitationRepository = cooperationInvitationRepository;
    }

    @Override
    protected InvitationDto saveInvitation(InvitationDto invitationDto) {
        var cooperationInvitationDto =
            mapper.convert(invitationDto, CooperationInvitationDto.class);
        var cooperationInvitation =
            mapper.convert(cooperationInvitationDto, CooperationInvitation.class);

        cooperationInvitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(EXPIRATION_TERM));
        cooperationInvitation.setCooperationName(cooperationInvitationDto.getCooperationName());
        cooperationInvitation.setStatus(InvitationStatus.PENDING);
        invitationRepository.save(cooperationInvitation);
        cooperationInvitationDto.setId(cooperationInvitation.getId());
        return cooperationInvitationDto;
    }

    @Override
    public List<CooperationInvitationDto> getAllActiveInvitations() {
        var allNotSentInvitations = cooperationInvitationRepository
            .findAllBySentDatetimeIsNullAndStatusEquals(
                InvitationStatus.PENDING);
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class))
            .collect(Collectors.toList());
    }

    public void markAsOverdueCooperationInvitations() {
        var overdueCooperationInvitations = cooperationInvitationRepository
            .findAll((Specification<CooperationInvitation>) (root, criteriaQuery, criteriaBuilder) ->
                getOverdueInvitation(root, criteriaBuilder));
        overdueCooperationInvitations.forEach(invitation -> {
            invitation.setStatus(InvitationStatus.OVERDUE);
            cooperationInvitationRepository.save(invitation);
        });
    }
}
