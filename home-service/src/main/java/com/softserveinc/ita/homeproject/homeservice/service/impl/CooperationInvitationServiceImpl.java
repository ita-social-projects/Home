package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.entity.RoleEnum;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import org.springframework.stereotype.Service;


@Service
public class CooperationInvitationServiceImpl extends InvitationServiceImpl implements CooperationInvitationService {

    public CooperationInvitationServiceImpl(InvitationRepository invitationRepository,
                                            ServiceMapper mapper) {
        super(invitationRepository, mapper);
    }

    @Override
    protected InvitationDto fillFieldsByTheType(InvitationDto invitationDto) {
        CooperationInvitationDto cooperationInvitationDto =
                mapper.convert(invitationDto, CooperationInvitationDto.class);
        CooperationInvitation cooperationInvitation =
                mapper.convert(cooperationInvitationDto, CooperationInvitation.class);

        cooperationInvitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(7));
        cooperationInvitation.setCooperationName(cooperationInvitationDto.getCooperationName());
        cooperationInvitation.setStatus(InvitationStatus.PENDING);
        invitationRepository.save(cooperationInvitation);
        cooperationInvitationDto.setId(cooperationInvitation.getId());
        return cooperationInvitationDto;
    }


    @Override
    public List<CooperationInvitationDto> getAllActiveInvitations() {
        List<Invitation> allNotSentInvitations = invitationRepository.findAllBySentDatetimeIsNull();
        return allNotSentInvitations.stream()
                .map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class))
                .collect(Collectors.toList());
    }
}
