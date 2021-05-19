package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homedata.repository.RoleRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.CooperationInvitationService;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CooperationInvitationServiceImpl extends InvitationServiceImpl implements CooperationInvitationService {


    public CooperationInvitationServiceImpl(InvitationRepository invitationRepository, ServiceMapper mapper, RoleRepository roleRepository) {
        super(invitationRepository, mapper, roleRepository);
    }

    @Override
    protected Invitation fillFieldsByTheType(InvitationDto invitationDto) {
        CooperationInvitationDto cooperationInvitationDto =
                mapper.convert(invitationDto, CooperationInvitationDto.class);
        CooperationInvitation cooperationInvitation =
                mapper.convert(cooperationInvitationDto, CooperationInvitation.class);

        String role = cooperationInvitationDto.getRole();
        cooperationInvitation.setRole(
                roleRepository.findByName(role)
                    .orElseThrow(() -> new NotFoundHomeException("Role " + role + " not found.")));
        cooperationInvitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(7));
        cooperationInvitation.setCooperationName(cooperationInvitationDto.getCooperationName());
        cooperationInvitation.setStatus(InvitationStatus.PENDING);
        return invitationRepository.save(cooperationInvitation);
    }


    @Override
    public List<CooperationInvitationDto> getAllActiveInvitations() {
        List<Invitation> allNotSentInvitations = invitationRepository.findAllBySentDatetimeIsNull();
        return allNotSentInvitations.stream()
                .map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class))
                .collect(Collectors.toList());
    }
}
