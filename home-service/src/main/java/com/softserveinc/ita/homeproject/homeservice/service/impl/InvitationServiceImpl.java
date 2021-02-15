package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
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
    public void changeInvitationStatus(Long id) {
        Invitation invitation = findInvitationById(id);
        invitation.setStatus(true);
        invitationRepository.save(invitation);
    }

    @Override
    public void updateSentDateTime(Long id, LocalDateTime dateTime) {
        Invitation invitation = findInvitationById(id);
        invitation.setSentDateTime(dateTime);
        invitationRepository.save(invitation);
    }

    @Override
    public InvitationDto getInvitation(Long id) {
        Invitation invitation = findInvitationById(id);
        return mapper.convert(invitation, InvitationDto.class);
    }

    @Override
    public List<InvitationDto> getAllActiveInvitations() {
        List<Invitation> allNotSentInvitations = invitationRepository.findAllBySentDateTimeIsNull();
        return allNotSentInvitations.stream()
                .map(invitation -> mapper.convert(invitation, InvitationDto.class))
                .collect(Collectors.toList());
    }

    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new InvitationException("Invitation with id " + id + " was not found"));
    }
}
