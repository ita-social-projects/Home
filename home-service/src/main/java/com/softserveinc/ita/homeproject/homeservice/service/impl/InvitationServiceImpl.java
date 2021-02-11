package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitationServiceImpl implements InvitationService {

    private final InvitationRepository invitationRepository;
    private final ServiceMapper mapper;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        Invitation convert = mapper.convert(invitationDto, Invitation.class);
        Invitation invitation = invitationRepository.save(convert);
        return mapper.convert(invitation, InvitationDto.class);
    }

    @Override
    public void changeInvitationStatus(Set<Long> ids) {
        invitationRepository.updateStatus(ids);
    }

    @Override
    public void updateSentDateTime(Long id, LocalDateTime dateTime) {
        invitationRepository.updateSentDateTime(id, dateTime);
    }

    @Override
    public InvitationDto getInvitation(Long id) {
        return invitationRepository.findById(id)
            .map(invitation -> mapper.convert(invitation, InvitationDto.class)).orElse(null);
    }

    @Override public List<InvitationDto> getAllActiveInvitations() {
        List<Invitation> allNotSentInvitations = invitationRepository.getAllNotSentInvitations();
        return allNotSentInvitations.stream()
            .map(invitation -> mapper.convert(invitation, InvitationDto.class))
            .collect(Collectors.toList());
    }
}
