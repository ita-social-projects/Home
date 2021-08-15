package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.time.LocalDateTime;

import com.softserveinc.ita.homeproject.homedata.entity.Invitation;
import com.softserveinc.ita.homeproject.homedata.entity.InvitationStatus;
import com.softserveinc.ita.homeproject.homedata.repository.InvitationRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.InvitationDto;
import com.softserveinc.ita.homeproject.homeservice.exception.InvitationException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.InvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public abstract class InvitationServiceImpl implements InvitationService {

    protected final InvitationRepository invitationRepository;

    protected final ServiceMapper mapper;

    @Override
    public InvitationDto createInvitation(InvitationDto invitationDto) {
        return saveInvitation(invitationDto);
    }

    protected abstract InvitationDto saveInvitation(InvitationDto invitationDto);

    @Override
    public void updateSentDateTimeAndStatus(Long id) {
        var invitation = findInvitationById(id);
        invitation.setStatus(InvitationStatus.PROCESSING);
        invitation.setSentDatetime(LocalDateTime.now());
        invitationRepository.save(invitation);
    }

    private Invitation findInvitationById(Long id) {
        return invitationRepository.findById(id).orElseThrow(() ->
                new InvitationException("Invitation with id " + id + " was not found"));
    }

    @Override
    public InvitationDto findInvitationByRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
                .orElseThrow(() -> new NotFoundHomeException("Registration token not found"));
        return mapper.convert(invitation, ApartmentInvitationDto.class);
    }

    @Override
    public void registerWithRegistrationToken(String token) {
        Invitation invitation = invitationRepository.findInvitationByRegistrationToken(token)
                .orElseThrow(() -> new NotFoundHomeException("Registration token not found"));

        if(!invitation.getStatus().equals(InvitationStatus.PROCESSING)){
            throw new InvitationException("Invitation status is not equal to processing");
        }
        acceptUserInvitation(invitation);
    }

}
