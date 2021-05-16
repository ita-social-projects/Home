package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.CooperationInvitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {
    private final InvitationRepository invitationRepository;

    private final ServiceMapper mapper;

    @Override
    public CooperationInvitationDto createInvitation(CooperationInvitationDto invitationDto) {
        CooperationInvitation invitation = mapper.convert(invitationDto, CooperationInvitation.class);
        invitation.setRequestEndTime(LocalDateTime.from(LocalDateTime.now()).plusDays(7));
        invitation.setSentDatetime(LocalDateTime.now());
        invitation.setStatus(InvitationStatus.PENDING);
        invitationRepository.save(invitation);
        return mapper.convert(invitation, CooperationInvitationDto.class);
    }

    @Override
    public CooperationInvitationDto getInvitation(Long id) {
        CooperationInvitation invitation = findInvitationById(id);
        return mapper.convert(invitation, CooperationInvitationDto.class);
    }

    @Override
    public List<CooperationInvitationDto> getAllActiveInvitations() {
        List<CooperationInvitation> allNotSentInvitations = invitationRepository.findAllBySentDateTimeIsNull();
        return allNotSentInvitations.stream()
                .map(invitation -> mapper.convert(invitation, CooperationInvitationDto.class))
                .collect(Collectors.toList());
    }

    private CooperationInvitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new InvitationException("Invitation with id " + id + " was not found"));
    }
}
